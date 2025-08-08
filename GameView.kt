package com.example.endlesstaprunner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.concurrent.thread
import kotlin.random.Random

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback {
    var running = false
    private var gameThread: Thread? = null
    private var playerY = 0f
    private var velocity = 0f
    private var gravity = 0.8f
    private var groundY = 0f
    private var score = 0
    var coins = 0
    var onScoreChanged: ((Int)->Unit)? = null

    private val paint = Paint()
    private val bg = try { BitmapFactory.decodeResource(resources, R.drawable.bg_hd) } catch (e: Exception){ BitmapFactory.decodeResource(resources, R.drawable.bg) }
    private val player = try { BitmapFactory.decodeResource(resources, R.drawable.player_hd) } catch (e: Exception){ BitmapFactory.decodeResource(resources, R.drawable.player) }
    private val coinBmp = try { BitmapFactory.decodeResource(resources, R.drawable.coin_hd) } catch (e: Exception){ BitmapFactory.decodeResource(resources, R.drawable.coin) }
    private val obstacles = mutableListOf<RectF>()

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        running = true
        playerY = height / 2f
        groundY = height * 0.85f
        startGameLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        running = false
        gameThread?.join()
    }

    private fun startGameLoop() {
        gameThread = thread {
            var lastSpawn = System.currentTimeMillis()
            while (running) {
                val canvas = holder.lockCanvas()
                if (canvas != null) {
                    update()
                    drawGame(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
                if (System.currentTimeMillis() - lastSpawn > 1200) {
                    spawnObstacle()
                    lastSpawn = System.currentTimeMillis()
                }
                Thread.sleep(16)
            }
        }
    }

    private fun update() {
        velocity += gravity
        playerY += velocity
        if (playerY > groundY - player.height) {
            playerY = groundY - player.height
            velocity = 0f
        }
        val it = obstacles.iterator()
        val toRemove = mutableListOf<RectF>()
        while (it.hasNext()) {
            val r = it.next()
            r.left -= 8
            r.right -= 8
            if (r.right < -100) toRemove.add(r)
            // collision with player
            val playerRect = RectF(100f, playerY, 100f + player.width, playerY + player.height)
            if (RectF.intersects(playerRect, r)) {
                // hit - reset score and coins (simple punishment)
                score = 0
                coins = maxOf(0, coins-10)
                onScoreChanged?.invoke(score)
            }
            // collect coin if overlaps
            val coinRect = RectF(r.left, r.top - 60, r.left + 40, r.top - 20)
            if (RectF.intersects(playerRect, coinRect)) {
                coins += 5
                score += 10
                onScoreChanged?.invoke(score)
                toRemove.add(r)
            }
        }
        obstacles.removeAll(toRemove)
    }

    private fun drawGame(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        // draw bg tiled
        val src = Rect(0,0,bg.width,bg.height)
        val dst = Rect(0,0,width,height)
        canvas.drawBitmap(bg, src, dst, paint)
        // ground
        paint.color = Color.argb(180, 0, 0, 0)
        canvas.drawRect(0f, groundY, width.toFloat(), height.toFloat(), paint)
        // player
        canvas.drawBitmap(player, 100f, playerY, paint)
        // obstacles & coins
        paint.color = Color.RED
        for (r in obstacles) {
            canvas.drawRect(r, paint)
            // coin
            canvas.drawBitmap(coinBmp, r.left, r.top - 60, paint)
        }
    }

    private fun spawnObstacle() {
        val h = Random.nextInt(80, 220)
        val y = groundY - h
        val r = RectF(width.toFloat(), y, width + 60f, groundY)
        obstacles.add(r)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // flap
            velocity = -18f
            score += 1
            onScoreChanged?.invoke(score)
        }
        return true
    }

    fun pause() {
        running = false
        gameThread?.join()
    }

    fun resume() {
        if (!running) {
            running = true
            startGameLoop()
        }
    }
}

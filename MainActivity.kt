package com.example.endlesstaprunner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private lateinit var scoreText: TextView
    private lateinit var shopBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameView = findViewById(R.id.gameView)
        scoreText = findViewById(R.id.scoreText)
        shopBtn = findViewById(R.id.shopBtn)

        gameView.onScoreChanged = { score ->
            runOnUiThread { scoreText.text = "Score: $score" }
        }

        shopBtn.setOnClickListener {
            val i = Intent(this, ShopActivity::class.java)
            i.putExtra("coins", gameView.coins)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }
}

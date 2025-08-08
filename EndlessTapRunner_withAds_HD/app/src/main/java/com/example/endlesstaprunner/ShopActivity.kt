package com.example.endlesstaprunner

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import androidx.appcompat.app.AppCompatActivity

class ShopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        val coinsText = findViewById<TextView>(R.id.coinsText)
        val buySmall = findViewById<Button>(R.id.buySmall)
        val buyMedium = findViewById<Button>(R.id.buyMedium)
        val closeBtn = findViewById<Button>(R.id.closeBtn)

        val coins = intent.getIntExtra("coins",0)
        coinsText.text = "Coins: $coins"

        // BillingManager would handle purchases. Here we show stubs.
        buySmall.setOnClickListener {
            // TODO: trigger BillingManager.purchase("small_coins")
            coinsText.text = "Coins: " + (coins + 100)
        }
        buyMedium.setOnClickListener {
            // TODO: trigger BillingManager.purchase("medium_coins")
            coinsText.text = "Coins: " + (coins + 600)
        }
        closeBtn.setOnClickListener { finish() }

            // Rewarded ad setup (test ad unit)
            var rewardedAd: RewardedAd? = null
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
            })

            // Create a Watch Ad button dynamically
            val watchBtn = Button(this)
            watchBtn.text = "Watch Ad: Get 50 Coins"
            (findViewById(android.R.id.content) as android.view.ViewGroup).addView(watchBtn)
            watchBtn.setOnClickListener {
                if (rewardedAd != null) {
                    rewardedAd?.show(this) { rewardItem: RewardItem ->
                        // Grant reward
                        coinsText.text = "Coins: " + (coins + 50)
                    }
                }
            }
    }
}

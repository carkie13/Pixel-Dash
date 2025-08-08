Endless Tap Runner - Minimal Android Game (Kotlin)
================================================

What's included
- Android Studio project skeleton (app module).
- Simple endless-tap runner using a SurfaceView (GameView).
- Shop activity with stubbed purchase buttons.
- Simple programmatically generated PNG assets (player, coin, background).
- BillingManager.kt stub with guidance where to integrate Google Play Billing.

How to open and build
1. Install Android Studio (Electric Eel or later).
2. Open this folder as a project (the folder that contains settings.gradle).
3. Let Gradle sync. You might need to upgrade Gradle wrapper and Android Gradle plugin versions to match your Android Studio.
4. Build and run on an emulator or physical device (minSdk 21).

Integrating Play Billing (for live microtransactions)
----------------------------------------------------
1. In Play Console, create your app and set up In-app products (Managed products / Subscriptions).
2. Add product IDs (e.g. small_coins, medium_coins).
3. Implement BillingClient in BillingManager.kt:
   - Initialize BillingClient with BillingClient.newBuilder(context).enablePendingPurchases().build()
   - QuerySkuDetails for your product IDs.
   - Launch BillingFlow from the activity when user taps buy.
   - Handle purchases in PurchasesUpdatedListener and acknowledge consumables with acknowledgePurchase.
   - Use signed purchase verification or server-side validation for security.
4. Add the 'com.android.billingclient:billing-ktx' dependency (already in build.gradle).
5. Test purchases with license testers in the Play Console and use internal testing tracks.

Preparing for Play Store release
-------------------------------
1. Create a Keystore (release key) and configure signing in Gradle.
2. Build a release APK/AAB (recommended: AAB).
3. Complete Play Console store listing: title, description, screenshots (device screenshots), icons, feature graphic.
4. Content rating, pricing & distribution, privacy policy URL.
5. Upload AAB to the internal test track and test purchases with test accounts.
6. When ready, promote to production.

Notes & Next steps
- This sample is intentionally minimal to be auditable and editable.
- Replace the BillingManager.kt stub with a proper implementation using Play Billing docs:
  https://developer.android.com/google/play/billing
- Add analytics, obfuscation (ProGuard/R8 rules), server-side verification for higher security.
- Improve artwork and animations. The current PNGs are simple placeholders.

License: MIT


AdMob (Ads) Integration and HD graphics
--------------------------------------
- This updated project includes Google Mobile Ads (AdMob) integration for banners and rewarded ads using Google's **test** ad units.
- Banner ad unit ID (test): ca-app-pub-3940256099942544/6300978111
- Rewarded test ad unit ID: ca-app-pub-3940256099942544/5224354917

Testing notes:
1. The AndroidManifest includes the test AdMob App ID meta-data. Replace with your real App ID before production.
2. Test purchases and test ads should be used during internal testing. Do NOT release test ad unit IDs to production.
3. To use real ads, register your app in AdMob and replace ad unit IDs and app id before publishing.

Asset notes:
- Higher-resolution assets added in res/drawable: player_hd.png, coin_hd.png, bg_hd.png. The GameView is updated to prefer these HD assets if available.

package com.all.documents.files.reader.documentfiles.viewer.ads;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.AppPurchaseHelperStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.LoadAdsStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.R

class NativeStreetViewAds {

    companion object {

    private fun loadNativeAd(mContext: Context,frameLayout: FrameLayout) {
         //val nativeAdContainerView: ViewGroup? = null
         var nativeAdLoader: MaxNativeAdLoader? = null
         var loadedNativeAd: MaxAd? = null

        nativeAdLoader = MaxNativeAdLoader(LoadAdsStreetViewClock.max_native_id, mContext)
        nativeAdLoader!!.setNativeAdListener(object : MaxNativeAdListener() {

            override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                super.onNativeAdLoaded(p0, p1)
                // Clean up any pre-existing native ad to prevent memory leaks.
                if (loadedNativeAd != null)
                {
                    nativeAdLoader!!.destroy(loadedNativeAd)
                }
                // Save ad for cleanup.
                loadedNativeAd = p1
                frameLayout.removeAllViews()
                frameLayout.addView(p0)
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError)
            {
                // Native ad load failed.
                // AppLovin recommends retrying with exponentially higher delays up to a maximum delay.
            }

            override fun onNativeAdClicked(nativeAd: MaxAd) {}

        })
        nativeAdLoader!!.loadAd(createNativeAdView(mContext))
    }

    private fun createNativeAdView(mContext: Context): MaxNativeAdView
    {
        val binder: MaxNativeAdViewBinder =
            MaxNativeAdViewBinder.Builder(R.layout.earth_live_native_max_layout)
                .setTitleTextViewId(R.id.title_text_view)
                .setBodyTextViewId(R.id.body_text_view)
                .setAdvertiserTextViewId(R.id.advertiser_textView)
                .setIconImageViewId(R.id.icon_image_view)
                .setMediaContentViewGroupId(R.id.media_view_container)
                .setOptionsContentViewGroupId(R.id.options_view)
                .setCallToActionButtonId(R.id.cta_button)
                .build()

        return MaxNativeAdView(binder, mContext)
    }

    ///////////////


        fun loadWeatherAdmobNative(mContext: Context, frameLayout: FrameLayout) {
            val inflater = LayoutInflater.from(mContext)
            val builder = AdLoader.Builder(
                mContext,
                LoadAdsStreetViewClock.native_admob_inApp
            )
            var admobNativeAd: NativeAd? = null
            builder.forNativeAd { unifiedNativeAd ->
                if (admobNativeAd != null) {
                    admobNativeAd!!.destroy()
                }
                try {
                    admobNativeAd = unifiedNativeAd
                    val adView =  //actually card view of fb ad
                        inflater.inflate(R.layout.earth_live_map_native_admob_layout, null) as NativeAdView
                    populateUnifiedNativeAdView(unifiedNativeAd, adView)
                    frameLayout.removeAllViews()
                    frameLayout.addView(adView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val videoOptions = VideoOptions.Builder()
                .setStartMuted(true/*startVideoAdsMuted.isChecked()*/)
                .build()
            val adOptions = NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build()
            builder.withNativeAdOptions(adOptions)
            val adLoader = builder.withAdListener(object : com.google.android.gms.ads.AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    loadNativeAd(mContext,frameLayout)
                }
            }).build()
            val billingHelper =
                AppPurchaseHelperStreetViewClock(
                    mContext
                )
            if (billingHelper.shouldShowAds()) {
                adLoader.loadAd(AdRequest.Builder().build())
            }
        }

        private fun populateUnifiedNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {

            val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
            adView.mediaView = mediaView

            // Set other ad assets.
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)
            adView.priceView = adView.findViewById(R.id.ad_price)
            adView.starRatingView = adView.findViewById(R.id.ad_stars)
            adView.storeView = adView.findViewById(R.id.ad_store)
            adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
            (adView.headlineView as TextView).text = nativeAd.headline
            if (nativeAd.body == null) {
                adView.bodyView.visibility = View.INVISIBLE
            } else {
                adView.bodyView.visibility = View.VISIBLE
                (adView.bodyView as TextView).text = nativeAd.body
            }

            if (nativeAd.callToAction == null) {
                adView.callToActionView.visibility = View.INVISIBLE
            } else {
                adView.callToActionView.visibility = View.VISIBLE
                (adView.callToActionView as TextView).text = nativeAd.callToAction
            }

            if (nativeAd.icon == null) {
                adView.iconView.visibility = View.GONE
            } else {
                (adView.iconView as ImageView).setImageDrawable(
                    nativeAd.icon.drawable
                )
                adView.iconView.visibility = View.VISIBLE
            }

            if (nativeAd.price == null) {
                adView.priceView.visibility = View.INVISIBLE
            } else {
                adView.priceView.visibility = View.VISIBLE
                (adView.priceView as TextView).text = nativeAd.price
            }

            if (nativeAd.store == null) {
                adView.storeView.visibility = View.INVISIBLE
            } else {
                adView.storeView.visibility = View.VISIBLE
                (adView.storeView as TextView).text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                adView.starRatingView.visibility = View.INVISIBLE
            } else {
                (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                adView.starRatingView.visibility = View.VISIBLE
            }

            if (nativeAd.advertiser == null) {
                adView.advertiserView.visibility = View.INVISIBLE
            } else {
                (adView.advertiserView as TextView).text = nativeAd.advertiser
                adView.advertiserView.visibility = View.VISIBLE
            }
            adView.setNativeAd(nativeAd)
            val vc = nativeAd.mediaContent.videoController

            if (vc.hasVideoContent()) {
                vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                }
            } else {
            }
        }
    }

}
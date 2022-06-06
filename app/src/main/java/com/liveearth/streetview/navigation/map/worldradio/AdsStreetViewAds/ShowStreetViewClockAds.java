package com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.liveearth.streetview.navigation.map.worldradio.StreetViewDialog.ADsLoadingDialog;

public class ShowStreetViewClockAds {
    public static int adDialogTime = 1000;


    public static void simpleAdsStreetViewClockAdmobActivity(final Activity context, final InterstitialAd mInterstitialAd,final MaxInterstitialAd maxInterstitialAd, final Intent intent) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
        if (mInterstitialAd != null && LoadAdsStreetViewClock.shouldGoForAds) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mInterstitialAd.show(context);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                            LoadAdsStreetViewClock.canShowAppOpen=false;
                        }
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            LoadAdsStreetViewClock.canShowAppOpen=true;
                            LoadAdsStreetViewClock.admobInterstitialAd = null;
                            LoadAdsStreetViewClock.preReLoadAdsEarthLiveMap(context);
                            LoadAdsStreetViewClock.setHandlerForAdRequest(context);
                            LoadAdsStreetViewClock.setHandlerForAd();
                            context.startActivity(intent);
                        }
                    });
                }
            }, adDialogTime);
        }else if (maxInterstitialAd!=null && maxInterstitialAd.isReady() && LoadAdsStreetViewClock.shouldGoForAds) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    maxInterstitialAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax loaded");

                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed");
                            LoadAdsStreetViewClock.canShowAppOpen=false;
                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden");
                            LoadAdsStreetViewClock.canShowAppOpen=true;
                            LoadAdsStreetViewClock.preReLoadAdsEarthLiveMap(context);
                            LoadAdsStreetViewClock.setHandlerForAdRequest(context);
                            LoadAdsStreetViewClock.setHandlerForAd();
                            context.startActivity(intent);
                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdClicked");
                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "ShowMax error"+error.toString());

                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed"+error.toString());
                            LoadAdsStreetViewClock.preReLoadAdsEarthLiveMap(context);
                            context.startActivity(intent);
                        }
                    });
                    maxInterstitialAd.showAd();
                }
            }, adDialogTime);
        }
        else {
            LoadAdsStreetViewClock.preReLoadAdsEarthLiveMap(context);
            LoadAdsStreetViewClock.setHandlerForAdRequest(context);
            context.startActivity(intent);
        }
    }


}





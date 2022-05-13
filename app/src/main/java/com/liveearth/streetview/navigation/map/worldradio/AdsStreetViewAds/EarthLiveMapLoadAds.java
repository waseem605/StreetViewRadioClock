package com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.StreetViewMyApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liveearth.streetview.navigation.map.worldradio.R;

@SuppressLint("LogNotTimber")
public class EarthLiveMapLoadAds {
    public static String appid_admob_inApp = StreetViewMyApp.Companion.getStr(R.string.admob_ad_id);
    public static String banner_admob_inApp = StreetViewMyApp.Companion.getStr(R.string.admob_banner_id);
    public static String interstitial_admob_inApp = StreetViewMyApp.Companion.getStr(R.string.admob_interstitial_id);
    public static String interstitial_admob_inApp_two = StreetViewMyApp.Companion.getStr(R.string.admob_interstitial_id_two);
    public static String native_admob_inApp = StreetViewMyApp.Companion.getStr(R.string.admob_native_id);
    public static String app_open_ad_id_admob = StreetViewMyApp.Companion.getStr(R.string.app_open_ad_id_admob);

    public static String app_open_splash_ad_id_admob = StreetViewMyApp.Companion.getStr(R.string.app_open_splash_ad_id_admob);

    public static String max_banner_id = StreetViewMyApp.Companion.getStr(R.string.max_banner_id);
    public static String max_interstitial_id = StreetViewMyApp.Companion.getStr(R.string.max_interstitial_id);
    public static String max_native_id = StreetViewMyApp.Companion.getStr(R.string.max_native_id);


    public static MaxInterstitialAd maxInterstitialAdLiveEarth;
    public static boolean canReLoadedMax = false;

    public static boolean canShowAppOpen = true;

    public static InterstitialAd admobInterstitialAd;
    public static boolean canReLoadedAdMob = false;
    public static InterstitialAd admobInterstitialAdTwo;
    public static boolean canReLoadedAdMobTwo = false;

    public static boolean haveGotSnapshot = false;
    public static boolean shouldGoForAds = true;

    public static boolean shouldGoForBackPressAds = true;
    public static long next_ads_backPress_time = 15000;

    public static long next_ads_time = 500;
    public static boolean should_show_app_open = true;
    public static boolean should_show_splash_app_open = true;
    public static double current_counter = 50;

    public static float percentage = 1;
    public static double ctr_control_value = 9;
    public static double ad_click_value_key_var = 1;
    public static double ad_impression_value_key_var = 6;

    private static final Handler myHandlerCompass = new Handler();
    private static final Handler myHandlerCForBackAd = new Handler();
    private static final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("EarthLiveMap");


    public static void setHandlerForAd() {
        shouldGoForAds = false;
        Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeStart: " + shouldGoForAds);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldGoForAds = true;
                Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeComplete: " + shouldGoForAds);
            }
        }, next_ads_time);
    }

    public static void setHandlerForBackPressTimerAd() {
        shouldGoForBackPressAds = false;
        Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeStart: " + shouldGoForBackPressAds);
        myHandlerCForBackAd.removeCallbacksAndMessages(null);
        myHandlerCForBackAd.postDelayed(new Runnable() {
            @Override
            public void run() {
                shouldGoForBackPressAds = true;
                Log.d("ConstantAdsLoadAds", "shouldGoForAds onTimeComplete: " + shouldGoForBackPressAds);
            }
        }, next_ads_backPress_time);
    }

    public static void setHandlerForAdRequest(Context context) {
        Log.i("ConstantAdsLoadAds", "myHandler : setHandlerForAdRequest");
        myHandlerCompass.removeCallbacksAndMessages(null);
        myHandlerCompass.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("ConstantAdsLoadAds ", "myHandlerzzz start Request");
                EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
            }
        }, 10000);
    }


    public static void loadEarthLiveMapBannerAdMob(final LinearLayout adContainer, ConstraintLayout view, final Context context) {
        EarthLiveMapAppPurchaseHelper billingHelper = new EarthLiveMapAppPurchaseHelper(context);
        if (billingHelper.shouldShowAds()) {
            AdView mAdView = new AdView(context);
            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(EarthLiveMapLoadAds.banner_admob_inApp);

            try {
                mAdView.loadAd(new AdRequest.Builder().build());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    view.setVisibility(View.VISIBLE);
                    try {
                        adContainer.removeAllViews();
                        adContainer.addView(mAdView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    mAdView.destroy();
                    view.setVisibility(View.GONE);

                    loadLiveEarthBannerMax(adContainer,view, context);
                }
            });
        } else {
            view.setVisibility(View.GONE);
        }
    }

    //MAX Banner
    public static void loadLiveEarthBannerMax(final LinearLayout adContainer, ConstraintLayout view, final Context context) {
        EarthLiveMapAppPurchaseHelper billingHelper = new EarthLiveMapAppPurchaseHelper(context);
        if (billingHelper.shouldShowAds()) {

            MaxAdView adView = new MaxAdView(max_banner_id, context);
            adView.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd ad) {

                }

                @Override
                public void onAdCollapsed(MaxAd ad) {

                }

                @Override
                public void onAdLoaded(MaxAd ad) {
                    Log.i("ConstantAdsLoadAds ", "Max onAdLoaded");
                    view.setVisibility(View.VISIBLE);
                    try {
                        adContainer.removeAllViews();
                        adContainer.addView(adView);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    Log.i("ConstantAdsLoadAds ", "Max onAdDisplayed");

                }

                @Override
                public void onAdHidden(MaxAd ad) {

                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    Log.i("ConstantAdsLoadAds ", "Max onAdLoadFailed");
                    adView.destroy();
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    adView.destroy();
                    view.setVisibility(View.GONE);
                }
            });
            adView.loadAd();

        } else {
            view.setVisibility(View.GONE);
        }
    }
    //maxload
    public static void preloadMax(Activity context) {
        //maxload
        EarthLiveMapAppPurchaseHelper billingHelper = new EarthLiveMapAppPurchaseHelper(context);
        if (billingHelper.shouldShowAds()) {
            if (maxInterstitialAdLiveEarth == null) {
                canReLoadedMax = false;
                maxInterstitialAdLiveEarth = new MaxInterstitialAd(max_interstitial_id, context);
                maxInterstitialAdLiveEarth.setListener(new MaxAdListener() {
                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        Log.d("ConstantAdsLoadAds", "Max loaded");
                        canReLoadedMax = true;


                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {
                        Log.d("ConstantAdsLoadAds", "Max onAdDisplayed");
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        Log.d("ConstantAdsLoadAds", "Max error" + error.toString());
                        canReLoadedMax = true;
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        Log.d("ConstantAdsLoadAds", "Max onAdDisplayFailed" + error.toString());
                    }
                });

                maxInterstitialAdLiveEarth.loadAd();
            }
        } else {
            Log.d("ConstantAdsLoadAds", "Max AlReady loaded");
        }

    }



    public static void preLoadAdsInterstitialTwo(final Context context) {
        EarthLiveMapAppPurchaseHelper billingHelper = new EarthLiveMapAppPurchaseHelper(context);
        if (billingHelper.shouldShowAds()) {
            //admobeloadTwo
            if (admobInterstitialAdTwo == null) {
                canReLoadedAdMobTwo = false;
                InterstitialAd.load(context, EarthLiveMapLoadAds.interstitial_admob_inApp_two, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.d("ConstantAdsLoadAds", "Admob loaded two");
                        canReLoadedAdMobTwo = true;
                        admobInterstitialAdTwo = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("ConstantAdsLoadAds", "Admob Faild: " + loadAdError.toString());
                        canReLoadedAdMobTwo = true;
                        admobInterstitialAdTwo = null;
                    }
                });

            } else {
                Log.d("ConstantAdsLoadAds", "admobe AlReady loaded");
            }
        }
    }


        public static void preLoadAdsEarthLiveMap(final Context context) {
        EarthLiveMapAppPurchaseHelper billingHelper = new EarthLiveMapAppPurchaseHelper(context);
        if (billingHelper.shouldShowAds()) {
            //admobeload
            if (admobInterstitialAd == null) {
                canReLoadedAdMob = false;
                InterstitialAd.load(context, EarthLiveMapLoadAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.d("ConstantAdsLoadAds", "Admob loaded");
                        canReLoadedAdMob = true;
                        admobInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d("ConstantAdsLoadAds", "Admob Faild: " + loadAdError.toString());
                        canReLoadedAdMob = true;
                        admobInterstitialAd = null;
                    }
                });

            } else {
                Log.d("ConstantAdsLoadAds", "admobe AlReady loaded");
            }

        }
    }

    public static void preReLoadAdsEarthLiveMap(final Context context) {
        EarthLiveMapAppPurchaseHelper billingHelper = new EarthLiveMapAppPurchaseHelper(context);
        if (billingHelper.shouldShowAds()) {
            //admobeload
            if (admobInterstitialAd != null) {
                Log.d("ConstantAdsLoadAds", "admobe ReAlReady loaded");
            } else {
                Log.d("ConstantAdsLoadAds", "canReLoadedAdMob " + canReLoadedAdMob);
                if (canReLoadedAdMob) {
                    canReLoadedAdMob = false;
                    InterstitialAd.load(context, EarthLiveMapLoadAds.interstitial_admob_inApp, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            super.onAdLoaded(interstitialAd);
                            Log.d("ConstantAdsLoadAds", "Admob Reloaded");
                            canReLoadedAdMob = true;
                            admobInterstitialAd = interstitialAd;
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            Log.d("ConstantAdsLoadAds", "Admob ReFaild: " + loadAdError.toString());
                            canReLoadedAdMob = true;
                            admobInterstitialAd = null;
                        }
                    });

                } else {
                    Log.d("ConstantAdsLoadAds", "Admob last ad request is in pending");
                }
            }


            //maxReLoad
            if (maxInterstitialAdLiveEarth !=null && maxInterstitialAdLiveEarth.isReady()){
                Log.d("ConstantAdsLoadAds", "max ReAlReady loaded");
            }else {
                if (canReLoadedMax){
                    canReLoadedMax = false;
                    maxInterstitialAdLiveEarth = new MaxInterstitialAd(max_interstitial_id, (Activity) context );
                    maxInterstitialAdLiveEarth.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "Max Reloaded");
                            canReLoadedMax = true;
                        }
                        @Override
                        public void onAdDisplayed(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "Max ReonAdDisplayed");
                        }
                        @Override
                        public void onAdHidden(MaxAd ad) {
                        }
                        @Override
                        public void onAdClicked(MaxAd ad) {
                        }
                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "Max Reerror" + error.toString());
                            canReLoadedMax = true;
                        }
                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "Max ReonAdDisplayFailed" + error.toString());
                        }
                    });
                    maxInterstitialAdLiveEarth.loadAd();
                }
            }

        }
    }

    public static void adClickedEarthLiveMap(Context mContext) {
        if (haveGotSnapshot && ad_impression_value_key_var > 0 && ad_click_value_key_var > 0) {
            ad_click_value_key_var++;
            Log.e("STATUSES", "adClicked: " + ad_click_value_key_var);
            haveGotSnapshot = false;
            databaseReference.child("ADS_IDS").child("ad_click_value_key_var").setValue(ad_click_value_key_var);
            Log.e("PERCENTAGE", "" + ad_click_value_key_var);
        }
    }

    public static void adImpressedEarthLiveMap(Context mContext) {
        if (haveGotSnapshot && ad_impression_value_key_var > 0 && ad_click_value_key_var > 0) {
            ad_impression_value_key_var++;
            Log.e("STATUSES", "adImpressed: " + ad_impression_value_key_var);
            haveGotSnapshot = false;
            databaseReference.child("ADS_IDS").child("ad_impression_value_key_var").setValue(ad_impression_value_key_var);
            Log.e("PERCENTAGE", "" + ad_impression_value_key_var);
        }
    }

    /* not using*/
    public static void insertADSToFirebase(Context mContext) {
        Log.d("insertDataToFirebase", "insertADSToFirebase: ");
        databaseReference.child("ADS_IDS").setValue(new MyAdsModel(
                appid_admob_inApp
                , banner_admob_inApp
                , interstitial_admob_inApp
                , native_admob_inApp
                , app_open_ad_id_admob
                ,app_open_splash_ad_id_admob
                ,should_show_splash_app_open
                , should_show_app_open
                , next_ads_time
                , next_ads_backPress_time
                , current_counter
                ,ctr_control_value
                ,ad_click_value_key_var
                ,ad_impression_value_key_var
        ));
    }
}

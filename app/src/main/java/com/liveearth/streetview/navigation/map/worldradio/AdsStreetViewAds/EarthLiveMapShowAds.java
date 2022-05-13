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

public class EarthLiveMapShowAds {
    public static int adDialogTime = 1000;

    public static void clearActivityEarthLiveMapWithAdmobActivity(final Activity context, final InterstitialAd mInterstitialAd,final MaxInterstitialAd maxInterstitialAd, final Intent intent) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
            if (mInterstitialAd != null && EarthLiveMapLoadAds.shouldGoForAds) {
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
                                EarthLiveMapLoadAds.canShowAppOpen=false;
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                EarthLiveMapLoadAds.canShowAppOpen=true;
                                EarthLiveMapLoadAds.admobInterstitialAd = null;
                                EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                                EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                                EarthLiveMapLoadAds.setHandlerForAd();
                                EarthLiveMapLoadAds.setHandlerForBackPressTimerAd();
                                context.startActivity(intent);
                                context.finish();
                            }
                        });

                    }
                }, adDialogTime);


            }else if (maxInterstitialAd !=null && maxInterstitialAd.isReady() && EarthLiveMapLoadAds.shouldGoForAds) {

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
                        EarthLiveMapLoadAds.canShowAppOpen=false;
                        EarthLiveMapLoadAds.adImpressedEarthLiveMap(context);
                        if (EarthLiveMapLoadAds.percentage > EarthLiveMapLoadAds.ctr_control_value) {
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            EarthLiveMapLoadAds.setHandlerForAd();
                            EarthLiveMapLoadAds.setHandlerForBackPressTimerAd();
                            context.startActivity(intent);
                            context.finish();
                        }
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
                        Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden");
                          EarthLiveMapLoadAds.canShowAppOpen=true;
                        EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                        EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                        EarthLiveMapLoadAds.setHandlerForAd();
                        EarthLiveMapLoadAds.setHandlerForBackPressTimerAd();
                        context.startActivity(intent);
                        context.finish();
                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {
                        Log.d("ConstantAdsLoadAds", "ShowMax onAdClicked");
                        EarthLiveMapLoadAds.adClickedEarthLiveMap(context);
                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        Log.d("ConstantAdsLoadAds", "ShowMax error"+error.toString());

                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed"+error.toString());
                        EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                        context.startActivity(intent);
                        context.finish();
                    }
                });
                maxInterstitialAd.showAd();
                 }
                }, adDialogTime);
            } else {
                EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                context.startActivity(intent);
                context.finish();
            }
    }

    public static void clearActivityEarthLiveMapWithAdmobActivityTwo(final Activity context, final InterstitialAd mInterstitialAd, final Intent intent) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
            if (mInterstitialAd != null && EarthLiveMapLoadAds.shouldGoForAds) {
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
                                Log.d("onAdShowedTwo", "onAdShowedFullScreenContent: Two");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                context.startActivity(intent);
                                context.finish();
                            }
                        });

                    }
                }, adDialogTime);

            } else {
                context.startActivity(intent);
                context.finish();
            }
    }

    //Intent ad with timer
    public static void clearActivityTimerEarthLiveMapWithAdmobActivity(final Activity context, final InterstitialAd mInterstitialAd,final MaxInterstitialAd maxInterstitialAd, final Intent intent) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
        if (mInterstitialAd != null && EarthLiveMapLoadAds.shouldGoForBackPressAds) {
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
                            EarthLiveMapLoadAds.canShowAppOpen=false;
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            EarthLiveMapLoadAds.canShowAppOpen=true;
                            EarthLiveMapLoadAds.admobInterstitialAd = null;
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                            EarthLiveMapLoadAds.setHandlerForBackPressTimerAd();
                            context.startActivity(intent);
                            context.finish();
                        }
                    });

                }
            }, adDialogTime);


        }else if (maxInterstitialAd !=null && maxInterstitialAd.isReady() && EarthLiveMapLoadAds.shouldGoForBackPressAds) {

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
                            EarthLiveMapLoadAds.canShowAppOpen=false;
                            EarthLiveMapLoadAds.adImpressedEarthLiveMap(context);
                            if (EarthLiveMapLoadAds.percentage > EarthLiveMapLoadAds.ctr_control_value) {
                                EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                                EarthLiveMapLoadAds.setHandlerForBackPressTimerAd();
                                context.startActivity(intent);
                                context.finish();
                            }
                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden");
                            EarthLiveMapLoadAds.canShowAppOpen=true;
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                            EarthLiveMapLoadAds.setHandlerForBackPressTimerAd();
                            context.startActivity(intent);
                            context.finish();
                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdClicked");
                            EarthLiveMapLoadAds.adClickedEarthLiveMap(context);
                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "ShowMax error"+error.toString());

                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed"+error.toString());
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            context.startActivity(intent);
                            context.finish();
                        }
                    });
                    maxInterstitialAd.showAd();
                }
            }, adDialogTime);
        } else {
            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
            EarthLiveMapLoadAds.setHandlerForAdRequest(context);
            context.startActivity(intent);
            context.finish();
        }
    }

    public static void simpleEarthLiveMapWithAdmobActivity(final Activity context, final InterstitialAd mInterstitialAd,final MaxInterstitialAd maxInterstitialAd, final Intent intent) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
            if (mInterstitialAd != null && EarthLiveMapLoadAds.shouldGoForAds) {
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
                        EarthLiveMapLoadAds.canShowAppOpen=false;
                    }
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                          EarthLiveMapLoadAds.canShowAppOpen=true;
                        EarthLiveMapLoadAds.admobInterstitialAd = null;
                        EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                        EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                        EarthLiveMapLoadAds.setHandlerForAd();
                        context.startActivity(intent);

                    }
                });
                 }
                }, adDialogTime);
            }else if (maxInterstitialAd!=null && maxInterstitialAd.isReady() && EarthLiveMapLoadAds.shouldGoForAds) {

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
                        EarthLiveMapLoadAds.canShowAppOpen=false;
                        EarthLiveMapLoadAds.adImpressedEarthLiveMap(context);
                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {
                        Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden");
                          EarthLiveMapLoadAds.canShowAppOpen=true;
                        EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                        EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                        EarthLiveMapLoadAds.setHandlerForAd();
                        context.startActivity(intent);
                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {
                        Log.d("ConstantAdsLoadAds", "ShowMax onAdClicked");
                        EarthLiveMapLoadAds.adClickedEarthLiveMap(context);
                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {
                        Log.d("ConstantAdsLoadAds", "ShowMax error"+error.toString());

                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                        Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed"+error.toString());
                        EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                        context.startActivity(intent);
                    }
                });
                maxInterstitialAd.showAd();
                 }
                }, adDialogTime);
            }
            else {
                EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                context.startActivity(intent);
            }
    }

    public static void simpleBackPressEarthLiveMapWithAdmobActivity(final Activity context, final InterstitialAd mInterstitialAd,final MaxInterstitialAd maxInterstitialAd) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
        if (mInterstitialAd != null && EarthLiveMapLoadAds.shouldGoForAds) {
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
                    EarthLiveMapLoadAds.canShowAppOpen=false;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                      EarthLiveMapLoadAds.canShowAppOpen=true;
                    EarthLiveMapLoadAds.admobInterstitialAd = null;
                    EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                    EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                    EarthLiveMapLoadAds.setHandlerForAd();
                    context.finish();

                }
            });
             }
                }, adDialogTime);
        }else if (maxInterstitialAd!=null && maxInterstitialAd.isReady() && EarthLiveMapLoadAds.shouldGoForAds) {
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
            maxInterstitialAd.showAd();
            maxInterstitialAd.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    Log.d("ConstantAdsLoadAds", "ShowMax loaded");

                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed");
                    EarthLiveMapLoadAds.canShowAppOpen=false;
                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden");
                      EarthLiveMapLoadAds.canShowAppOpen=true;
                    EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                    EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                    EarthLiveMapLoadAds.setHandlerForAd();
                    context.finish();
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    Log.d("ConstantAdsLoadAds", "ShowMax error"+error.toString());

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed"+error.toString());
                    EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                    context.finish();
                }
            });
             }
                }, adDialogTime);

        }
        else {
            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
            EarthLiveMapLoadAds.setHandlerForAdRequest(context);
            context.finish();

        }
    }


    //BackPress ad with timer
    public static void simpleBackPressTimerEarthLiveMapWithAdmobActivity(final Activity context, final InterstitialAd mInterstitialAd,final MaxInterstitialAd maxInterstitialAd) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
        if (mInterstitialAd != null && EarthLiveMapLoadAds.shouldGoForBackPressAds) {
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
                            EarthLiveMapLoadAds.canShowAppOpen=false;
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            EarthLiveMapLoadAds.canShowAppOpen=true;
                            EarthLiveMapLoadAds.admobInterstitialAd = null;
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            EarthLiveMapLoadAds.setHandlerForBackPressTimerAd();
                            context.finish();
                        }
                    });
                }
            }, adDialogTime);
        }else if (maxInterstitialAd!=null && maxInterstitialAd.isReady() && EarthLiveMapLoadAds.shouldGoForBackPressAds) {
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
                    maxInterstitialAd.showAd();
                    maxInterstitialAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax loaded");

                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed");
                            EarthLiveMapLoadAds.canShowAppOpen=false;
                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden");
                            EarthLiveMapLoadAds.canShowAppOpen=true;
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            EarthLiveMapLoadAds.setHandlerForBackPressTimerAd();
                            context.finish();
                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "ShowMax error"+error.toString());

                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed"+error.toString());
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            context.finish();
                        }
                    });
                }
            }, adDialogTime);

        }
        else {
            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
            EarthLiveMapLoadAds.setHandlerForAdRequest(context);
            context.finish();
        }
    }


    public static void simpleOnClickEarthLiveMapWithAdmobActivity(final Activity context, final InterstitialAd mInterstitialAd,final MaxInterstitialAd maxInterstitialAd) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
        if (mInterstitialAd != null && EarthLiveMapLoadAds.shouldGoForAds) {
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
                            EarthLiveMapLoadAds.canShowAppOpen=false;
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            EarthLiveMapLoadAds.canShowAppOpen=true;
                            EarthLiveMapLoadAds.admobInterstitialAd = null;
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                            EarthLiveMapLoadAds.setHandlerForAd();
                            //context.finish();

                        }
                    });
                }
            }, adDialogTime);
        }else if (maxInterstitialAd!=null && maxInterstitialAd.isReady() && EarthLiveMapLoadAds.shouldGoForAds) {
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
                    maxInterstitialAd.showAd();
                    maxInterstitialAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax loaded");

                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed");
                            EarthLiveMapLoadAds.canShowAppOpen=false;
                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden");
                            EarthLiveMapLoadAds.canShowAppOpen=true;
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                            EarthLiveMapLoadAds.setHandlerForAd();
                            //context.finish();
                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "ShowMax error"+error.toString());

                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                            Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed"+error.toString());
                            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                            //context.finish();
                        }
                    });
                }
            }, adDialogTime);

        }
        else {
            EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
            EarthLiveMapLoadAds.setHandlerForAdRequest(context);
            //context.finish();
        }
    }



    //Max show not using
    public static void simpleLiveEarthMapWithMax(final Activity context, final MaxInterstitialAd maxInterstitialAd) {
        ADsLoadingDialog dialog = new ADsLoadingDialog(context);
        if (maxInterstitialAd.isReady() && EarthLiveMapLoadAds.shouldGoForAds) {
            maxInterstitialAd.showAd();
            maxInterstitialAd.setListener(new MaxAdListener() {
                @Override
                public void onAdLoaded(MaxAd ad) {
                    Log.d("ConstantAdsLoadAds", "ShowMax loaded");
                }

                @Override
                public void onAdDisplayed(MaxAd ad) {
                    Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayed");
                    EarthLiveMapLoadAds.canShowAppOpen=false;
                    EarthLiveMapLoadAds.preReLoadAdsEarthLiveMap(context);
                    EarthLiveMapLoadAds.setHandlerForAdRequest(context);
                    EarthLiveMapLoadAds.setHandlerForAd();
                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    Log.d("ConstantAdsLoadAds", "ShowMax onAdHidden");
                      EarthLiveMapLoadAds.canShowAppOpen=true;
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {
                    Log.d("ConstantAdsLoadAds", "ShowMax error"+error.toString());

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                    Log.d("ConstantAdsLoadAds", "ShowMax onAdDisplayFailed"+error.toString());
                }
            });
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
                }
            }, adDialogTime);

        } else {
           /* EarthLiveMapLoadAds.preReLoadAdsWeather(context);
            EarthLiveMapLoadAds.setHandlerWeatherForAdRequest(context);*/
        }
    }

}





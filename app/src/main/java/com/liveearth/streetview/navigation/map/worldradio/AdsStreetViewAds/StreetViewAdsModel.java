package com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds;

public class StreetViewAdsModel {
    private String appid_admob_inApp;
    private String banner_admob_inApp;
    private String interstitial_admob_inApp;
    private String native_admob_inApp;
    private String app_open_admob_inApp;
    private boolean should_show_open_app;
    public double next_ads_time;
    public double current_counter;


    public StreetViewAdsModel() {
    }

    public StreetViewAdsModel(String appid_admob_inApp, String banner_admob_inApp, String interstitial_admob_inApp, String native_admob_inApp, String app_open_admob_inApp, boolean should_show_open_app, double next_ads_time, double current_counter) {
        this.appid_admob_inApp = appid_admob_inApp;
        this.banner_admob_inApp = banner_admob_inApp;
        this.interstitial_admob_inApp = interstitial_admob_inApp;
        this.native_admob_inApp = native_admob_inApp;
        this.app_open_admob_inApp = app_open_admob_inApp;
        this.should_show_open_app = should_show_open_app;
        this.next_ads_time = next_ads_time;
        this.current_counter = current_counter;
    }

    public String getAppid_admob_inApp() {
        return appid_admob_inApp;
    }

    public void setAppid_admob_inApp(String appid_admob_inApp) {
        this.appid_admob_inApp = appid_admob_inApp;
    }

    public String getBanner_admob_inApp() {
        return banner_admob_inApp;
    }

    public void setBanner_admob_inApp(String banner_admob_inApp) {
        this.banner_admob_inApp = banner_admob_inApp;
    }

    public String getInterstitial_admob_inApp() {
        return interstitial_admob_inApp;
    }

    public void setInterstitial_admob_inApp(String interstitial_admob_inApp) {
        this.interstitial_admob_inApp = interstitial_admob_inApp;
    }

    public String getNative_admob_inApp() {
        return native_admob_inApp;
    }

    public void setNative_admob_inApp(String native_admob_inApp) {
        this.native_admob_inApp = native_admob_inApp;
    }

    public String getApp_open_admob_inApp() {
        return app_open_admob_inApp;
    }

    public void setApp_open_admob_inApp(String app_open_admob_inApp) {
        this.app_open_admob_inApp = app_open_admob_inApp;
    }

    public boolean isShould_show_open_app() {
        return should_show_open_app;
    }

    public void setShould_show_open_app(boolean should_show_open_app) {
        this.should_show_open_app = should_show_open_app;
    }

    public double getNext_ads_time() {
        return next_ads_time;
    }

    public void setNext_ads_time(double next_ads_time) {
        this.next_ads_time = next_ads_time;
    }

    public double getCurrent_counter() {
        return current_counter;
    }

    public void setCurrent_counter(double current_counter) {
        this.current_counter = current_counter;
    }
}

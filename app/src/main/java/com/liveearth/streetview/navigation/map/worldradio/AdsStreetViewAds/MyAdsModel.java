package com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds;

public class MyAdsModel {
    private String appid_admob_inApp;
    private String banner_admob_inApp;
    private String interstitial_admob_inApp;
    private String native_admob_inApp;
    private String app_open_admob_inApp;
    private String app_open_splash_ad_id_admob;
    private boolean should_show_splash_app_open;
    private boolean should_show_open_app;
    public double next_ads_time;
    public double next_ads_backPress_time;
    public double current_counter;
    public double ctr_control_value;
    public double ad_click_value_key_var;
    public double ad_impression_value_key_var;

    public MyAdsModel() {
    }

    public MyAdsModel(String appid_admob_inApp, String banner_admob_inApp, String interstitial_admob_inApp, String native_admob_inApp, String app_open_admob_inApp, String app_open_splash_ad_id_admob, boolean should_show_splash_app_open, boolean should_show_open_app, double next_ads_time, double next_ads_backPress_time, double current_counter, double ctr_control_value, double ad_click_value_key_var, double ad_impression_value_key_var) {
        this.appid_admob_inApp = appid_admob_inApp;
        this.banner_admob_inApp = banner_admob_inApp;
        this.interstitial_admob_inApp = interstitial_admob_inApp;
        this.native_admob_inApp = native_admob_inApp;
        this.app_open_admob_inApp = app_open_admob_inApp;
        this.app_open_splash_ad_id_admob = app_open_splash_ad_id_admob;
        this.should_show_splash_app_open = should_show_splash_app_open;
        this.should_show_open_app = should_show_open_app;
        this.next_ads_time = next_ads_time;
        this.next_ads_backPress_time = next_ads_backPress_time;
        this.current_counter = current_counter;
        this.ctr_control_value = ctr_control_value;
        this.ad_click_value_key_var = ad_click_value_key_var;
        this.ad_impression_value_key_var = ad_impression_value_key_var;
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

    public String getApp_open_splash_ad_id_admob() {
        return app_open_splash_ad_id_admob;
    }

    public void setApp_open_splash_ad_id_admob(String app_open_splash_ad_id_admob) {
        this.app_open_splash_ad_id_admob = app_open_splash_ad_id_admob;
    }

    public boolean isShould_show_splash_app_open() {
        return should_show_splash_app_open;
    }

    public void setShould_show_splash_app_open(boolean should_show_splash_app_open) {
        this.should_show_splash_app_open = should_show_splash_app_open;
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

    public double getNext_ads_backPress_time() {
        return next_ads_backPress_time;
    }

    public void setNext_ads_backPress_time(double next_ads_backPress_time) {
        this.next_ads_backPress_time = next_ads_backPress_time;
    }

    public double getCurrent_counter() {
        return current_counter;
    }

    public void setCurrent_counter(double current_counter) {
        this.current_counter = current_counter;
    }

    public double getCtr_control_value() {
        return ctr_control_value;
    }

    public void setCtr_control_value(double ctr_control_value) {
        this.ctr_control_value = ctr_control_value;
    }

    public double getAd_click_value_key_var() {
        return ad_click_value_key_var;
    }

    public void setAd_click_value_key_var(double ad_click_value_key_var) {
        this.ad_click_value_key_var = ad_click_value_key_var;
    }

    public double getAd_impression_value_key_var() {
        return ad_impression_value_key_var;
    }

    public void setAd_impression_value_key_var(double ad_impression_value_key_var) {
        this.ad_impression_value_key_var = ad_impression_value_key_var;
    }
}

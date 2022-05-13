package com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds;

import android.content.Context;
import android.content.SharedPreferences;

public class EarthLiveMapAppPurchaseHelper {
    private SharedPreferences billingPreferences;

    public EarthLiveMapAppPurchaseHelper(Context content) {
        billingPreferences = content.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE);
    }

    public boolean shouldShowAds() {
        return !(billingPreferences.getBoolean("ads_purchase", false));
    }
}

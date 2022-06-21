package com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPurchaseHelperStreetViewClock {
    private final SharedPreferences billingPreferences;

    public AppPurchaseHelperStreetViewClock(Context content) {
        billingPreferences = content.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE);
    }

    public boolean shouldShowAds() {
       return !(billingPreferences.getBoolean("ads_purchase", false));
    }
}

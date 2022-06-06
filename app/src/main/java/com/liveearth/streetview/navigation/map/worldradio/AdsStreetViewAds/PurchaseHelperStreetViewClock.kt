package com.all.documents.files.reader.documentfiles.viewer.ads

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*


class PurchaseHelperStreetViewClock(private val activityContext: Context) :
    PurchasesUpdatedListener {

    private lateinit var googleBillingEarthLiveMapClient: BillingClient
    private val TAG = "BillingLogger:"
    private val listAvailEarthLiveMapPurchases = ArrayList<SkuDetails>()
    private lateinit var billingEarthLiveMapPreferences: SharedPreferences


    init {
        initMyBillingClientEarthLiveMap()
    }

    private fun initMyBillingClientEarthLiveMap() {
        billingEarthLiveMapPreferences =
            activityContext.getSharedPreferences("PurchasePrefs", Context.MODE_PRIVATE)
        googleBillingEarthLiveMapClient = BillingClient
            .newBuilder(activityContext)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        googleBillingEarthLiveMapClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Google Billing is Connected")
                    fetchEarthLiveMapAllInAppsFromConsole() /*available on console*/
                    fetchEarthLiveMapPurchasedInAppsFromConsole()  /*user has purchased*/
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.d(TAG, "Google Billing is  Disconnected")
            }
        })
    }

    private fun fetchEarthLiveMapAllInAppsFromConsole() {
        val skuListToQuery = ArrayList<String>()
        skuListToQuery.add("ads_purchase")
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuListToQuery).setType(BillingClient.SkuType.INAPP)
        googleBillingEarthLiveMapClient.querySkuDetailsAsync(
            params.build(),
            object : SkuDetailsResponseListener {
                override fun onSkuDetailsResponse(
                    result: BillingResult,
                    skuDetails: MutableList<SkuDetails>?
                ) {
                    //Log.i(TAG, "onSkuResponse ${result?.responseCode}")
                    if (skuDetails != null) {
                        for (skuDetail in skuDetails) {
                            listAvailEarthLiveMapPurchases.add(skuDetail)
                            Log.i(TAG, skuDetail.toString())
                        }
                    } else {
                        Log.i(TAG, "No skus for this application")
                    }
                }

            })
    }


/*    fun fetchEarthLiveMapPurchasedInAppsFromConsole() {
        val purchaseResults: Purchase.PurchasesResult =
            googleBillingEarthLiveMapClient.queryPurchases(BillingClient.SkuType.INAPP)
        if (purchaseResults.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            val listPurchased = purchaseResults.purchasesList
            if (listPurchased != null && listPurchased.size > 0) {
                for (singlePurchase in listPurchased) {
                    if (singlePurchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        Log.d(TAG, "Product Purchased: ${singlePurchase.sku}")
                        billingEarthLiveMapPreferences.edit().putBoolean(singlePurchase.sku, true).apply()
                    } else {
                        billingEarthLiveMapPreferences.edit().putBoolean(singlePurchase.sku, false).apply()
                        Log.d(TAG, "Product Not Purchased: ${singlePurchase.sku}")
                    }
                }
            } else {
                Log.d(TAG, "Array List Purchase Null$listPurchased")
            }
        } else {
            Log.d(TAG, "Billing Checker Failed 1: ${purchaseResults.billingResult.responseCode}")
        }

    }*/


    fun fetchEarthLiveMapPurchasedInAppsFromConsole() {

        googleBillingEarthLiveMapClient.queryPurchasesAsync(BillingClient.SkuType.INAPP,object :PurchasesResponseListener{
            override fun onQueryPurchasesResponse(billingResult: BillingResult, listPurchased: MutableList<Purchase>) {

                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (listPurchased.size > 0) {
                        for (singlePurchase in listPurchased) {
                            if (singlePurchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                Log.d(TAG, "Product Purchased: ${singlePurchase.skus[0]}")
                                billingEarthLiveMapPreferences.edit().putBoolean(singlePurchase.skus[0], true).apply()
                            } else {
                                billingEarthLiveMapPreferences.edit().putBoolean(singlePurchase.skus[0], false).apply()
                                Log.d(TAG, "Product Not Purchased: ${singlePurchase.skus[0]}")
                            }
                        }
                    } else {
                        Log.d(TAG, "Array List Purchase Null$listPurchased")
                    }
                } else {
                    Log.d(TAG, "Billing Checker Failed 1: ${billingResult.responseCode}")
                }

            }

        })

    }

    fun purchaseStreetViewClockAdsPackage() {
        Log.d(TAG, "Going to purchase ads_purchase")
        if (listAvailEarthLiveMapPurchases.size > 0) {
            try {
                val flowParams = BillingFlowParams.newBuilder()
                    .setSkuDetails(listAvailEarthLiveMapPurchases[0])
                    .build()
                val responseCode =
                    googleBillingEarthLiveMapClient.launchBillingFlow(
                        activityContext as Activity,
                        flowParams
                    ).responseCode
                Log.d(TAG, "Google Billing Response : $responseCode")
            } catch (e: Exception) {
            }
        } else {
            Log.d(TAG, "Nothing to purchase for google billing")
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                Log.d(TAG, "onPurchases Successfully Purchased : " + purchase.skus[0])
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d(TAG, "Google Billing Cancelled")
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Log.d(TAG, "Google Billing Purchased Already")
            Toast.makeText(
                activityContext,
                "You have already purchased this item",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            Log.d(TAG, "Google billing other error " + billingResult.responseCode)
        }
    }

    private val purchaseAcknowledgedListener: AcknowledgePurchaseResponseListener = object
        : AcknowledgePurchaseResponseListener {
        override fun onAcknowledgePurchaseResponse(p0: BillingResult) {
            Log.d(TAG, "Success Acknowledged : ${p0.responseCode}  :${p0.debugMessage}")
            fetchEarthLiveMapPurchasedInAppsFromConsole()
        }

    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                Log.d(TAG, "Process acknowledging: ${purchase.skus[0]}")
                val acknowledgeParamaters = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                googleBillingEarthLiveMapClient.acknowledgePurchase(
                    acknowledgeParamaters,
                    purchaseAcknowledgedListener
                )
                /*Now update preferences..either restart app so that query will
                execute or here make preferences true for ads*/

                /*here only one product so call preferences in acknowledged.*/

                /*or after acknowledged call query Method*/
            }
        }
    }
}
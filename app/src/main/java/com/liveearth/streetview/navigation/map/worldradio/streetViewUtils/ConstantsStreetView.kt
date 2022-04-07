package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import kotlin.properties.Delegates

object ConstantsStreetView {

    var accessToken =
        "pk.eyJ1IjoidWFlMDAwMSIsImEiOiJjbDB1aHc1bTAwZmRiM2JwZWYxMG85a2pvIn0.HaaaJlwnk7HFCS-eTR01ow"

    const val sharedPrefName = "StreetViewRadio"

    const val RATE_APP = "rateApp"
    const val APP_COLOR = "appColor"
    var APP_SELECTED_COLOR = "#1E54B4"
    const val WHITE_COLOR = "#FFFFFFFF"


    var currentCountryName = "currentCountryName"
    var currentCountryCode = "currentCountryCode"

    // Latitude and Longitude intents StringExtra
    const val OriginLatitude = "OriginLatitude"
    const val OriginLongitude = "OriginLongitude"
    const val DestinationLatitude = "DestinationLatitude"
    const val DestinationLongitude = "DestinationLongitude"

    //Multiples waypoints intent StringExtra
    const val MultiPointsRoute = "MultiPointsRoute"
    const val MultiPointsRouteList = "MultiPointsRouteList"


    //BackGround Location Services Intent
    const val EXTRA_LATITUDE = "extra_latitude"
    const val EXTRA_LONGITUDE = "extra_longitude"





    //World Clock Intent
    const val All_TIME_INTENT = "All_TIME_INTENT"
    const val INTENT_Value = "INTENT_Value"
    const val Show_ADD_Btn = "Show_ADD_Btn"

    //CurrentAddress
    var CURRENT_ADDRESS = "CurrentAddress"

    //background Services
    const val MY_TIMER_BROADCAST = "MY_TIMER_BROADCAST"
    const val ACTION_PLAY = "ACTION_PLAY"
    const val ACTION_PAUSE = "ACTION_PAUSE"
    const val ACTION_RESUME = "ACTION_RESUME"
    const val ACTION_STOP = "ACTION_STOP"

    //Radio Intent
    const val Radio_Country_Name = "RadioCountryName"
    //Radio intents
    const val RADIO_FLAGE = "RadioFlage"
    const val RADIO_CHANNEL_NAME = "RadioChannelName"
     var RADIO_POSITION by Delegates.notNull<Int>()

}
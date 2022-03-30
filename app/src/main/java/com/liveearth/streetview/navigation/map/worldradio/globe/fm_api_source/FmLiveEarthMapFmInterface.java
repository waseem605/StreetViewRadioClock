package com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FmLiveEarthMapFmInterface {
    //oneCountryName
    @GET("/json/stations/bycountry/{countryName}")
    Call<List<MainOneCountryFMModel>> getOneCountryFM(@Path("countryName") String countryName);
}

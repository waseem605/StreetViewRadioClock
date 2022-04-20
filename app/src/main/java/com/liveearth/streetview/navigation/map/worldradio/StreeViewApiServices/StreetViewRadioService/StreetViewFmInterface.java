package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewRadioService;



import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StreetViewFmInterface {
    //oneCountryName
    @GET("/json/stations/bycountry/{countryName}")
    Call<List<CountryMainFMModel>> getOneCountryFM(@Path("countryName") String countryName);
}

package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel;
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.TrackingLocationModel;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class TrackLocationItemTypeConverters {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<TrackingLocationModel> stringToUserModelList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<TrackingLocationModel>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<TrackingLocationModel> someObjects) {
        return gson.toJson(someObjects);
    }
}

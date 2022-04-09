package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel;
import com.mapbox.geojson.Point;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ExpenseItemTypeConverters {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<ExpenseItemModel> stringToUserModelList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ExpenseItemModel>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<ExpenseItemModel> someObjects) {
        return gson.toJson(someObjects);
    }
}

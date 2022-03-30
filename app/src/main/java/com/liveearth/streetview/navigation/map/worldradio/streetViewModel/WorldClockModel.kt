package com.liveearth.streetview.navigation.map.worldradio.streetViewModel

import android.os.Parcel
import android.os.Parcelable

class WorldClockModel(val timezone:String?, val iso:String?, val country:String?):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(timezone)
        parcel.writeString(iso)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorldClockModel> {
        override fun createFromParcel(parcel: Parcel): WorldClockModel {
            return WorldClockModel(parcel)
        }

        override fun newArray(size: Int): Array<WorldClockModel?> {
            return arrayOfNulls(size)
        }
    }
}
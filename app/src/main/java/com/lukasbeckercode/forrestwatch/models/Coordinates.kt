package com.lukasbeckercode.forrestwatch.models

import android.os.Parcel
import android.os.Parcelable

data class Coordinates(var lat:Double, var long:Double):Parcelable {
    constructor(parcel: Parcel) : this(
        lat = parcel.readDouble(),
        long= parcel.readDouble()
    ) {
    }



    companion object CREATOR : Parcelable.Creator<Coordinates> {
        override fun createFromParcel(parcel: Parcel): Coordinates {
            return Coordinates(parcel)
        }

        override fun newArray(size: Int): Array<Coordinates?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeDouble(lat)
        p0.writeDouble(long)
    }
}
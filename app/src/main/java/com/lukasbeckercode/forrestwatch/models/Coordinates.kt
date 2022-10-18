package com.lukasbeckercode.forrestwatch.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Simple data class representing a pair of latitude and longitude
 * @param lat latitude
 * @param long longitude
 *
 * Implements Parcelable Interface so it can be sent between activities using and intent
 */
data class Coordinates(var lat:Double, var long:Double):Parcelable {
    constructor(parcel: Parcel) : this(
        lat = parcel.readDouble(),
        long= parcel.readDouble()
    )

//------ Implementation of Parcelable interface -----
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
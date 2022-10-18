package com.lukasbeckercode.forrestwatch.models

import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.lukasbeckercode.forrestwatch.R

class User(
    var id: String? ="",
    var email: String? ="",
    var firstname: String? ="",
    var lastname: String?=""

):Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    constructor() : this(
        id="",
        email ="",
        firstname ="",
        lastname=""
    )

    fun validate(passwd:String, confirmPasswd:String): Boolean {
        email = email!!.trim()
        firstname = firstname!!.trim()
        lastname = lastname!!.trim()
        when{
            TextUtils.isEmpty(email) -> throw IllegalArgumentException(Resources.getSystem().getString(R.string.error_empty_email))
            TextUtils.isEmpty(firstname) -> throw IllegalArgumentException(Resources.getSystem().getString(R.string.error_empty_firstname))
            TextUtils.isEmpty(lastname) -> throw IllegalArgumentException(Resources.getSystem().getString(R.string.error_empty_lastname))
        }
        when{
             passwd.trim() != confirmPasswd.trim() -> return false
             !email!!.contains('@') -> return false
         }
  return true
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
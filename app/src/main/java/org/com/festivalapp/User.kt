package org.com.festivalapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User( var userId : String, var email : String, var firstname: String, var lastname : String ) :
    Parcelable
package org.com.festivalapp.tickets

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TicketItem( var userId : String, var userName : String, var userDay: String, var musicType : String, var userLocation: String ) : Parcelable
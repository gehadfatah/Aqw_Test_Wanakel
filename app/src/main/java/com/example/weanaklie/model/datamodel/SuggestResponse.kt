package com.example.weanaklie.model.datamodel


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SuggestResponse(
    @SerializedName("cat")
    var cat: String = "",
    @SerializedName("catId")
    var catId: String = "",
    @SerializedName("error")
    var error: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("image")
    var image: List<String> = listOf(),
    @SerializedName("lat")
    var lat: String = "",
    @SerializedName("link")
    var link: String = "",
    @SerializedName("lon")
    var lon: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("open")
    var open: String = "",
    @SerializedName("rating")
    var rating: String = "",
    @SerializedName("Ulat")
    var ulat: String = "",
    @SerializedName("Ulon")
    var ulon: String = ""
):Parcelable
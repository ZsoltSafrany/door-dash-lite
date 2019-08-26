package safi.doordashlite.api

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: Long,
    val name: String,
    val description: String,
    val status: String,
    @SerializedName("cover_img_url") val coverImageUrl: String?
)

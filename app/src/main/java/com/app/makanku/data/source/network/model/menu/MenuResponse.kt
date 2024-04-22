package com.app.makanku.data.source.network.model.menu

import com.google.gson.annotations.SerializedName
import com.app.makanku.data.source.network.model.category.CategoryItemResponse

data class MenuResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<MenuItemResponse>
)
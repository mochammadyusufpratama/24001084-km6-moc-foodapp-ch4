package com.app.makanku.data.source.network.model.category

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<CategoryItemResponse>,
)

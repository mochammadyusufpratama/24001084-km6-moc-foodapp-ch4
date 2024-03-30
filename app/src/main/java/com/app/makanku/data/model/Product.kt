package com.app.makanku.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Product(
    var id: String? = UUID.randomUUID().toString(),
    var name: String,
    var imgUrl: String,
    var price: Double,
    var desc: String,
    var location: String,
    var locationUrl: String
): Parcelable
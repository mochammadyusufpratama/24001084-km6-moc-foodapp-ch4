package com.app.makanku.data.mapper

import com.app.makanku.data.model.Menu
import com.app.makanku.data.source.network.model.menu.MenuItemResponse

fun MenuItemResponse?.toMenu() =
    Menu(
        name = this?.name.orEmpty(),
        formattedPrice = this?.formattedPrice.orEmpty(),
        price = this?.price ?: 0.0,
        imageUrl = this?.imgUrl.orEmpty(),
        desc = this?.menuDesc.orEmpty(),
        locationAddress = this?.restoAddress.orEmpty(),
        locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
    )

fun Collection<MenuItemResponse>?.toMenus() =
    this?.map { it.toMenu() } ?: listOf()
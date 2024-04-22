package com.app.makanku.data.mapper

import com.app.makanku.data.model.Category
import com.app.makanku.data.source.network.model.category.CategoryItemResponse

fun CategoryItemResponse?.toCategory() =
    Category(
        imgUrl = this?.imgUrl.orEmpty(),
        name = this?.name.orEmpty()
    )

fun Collection<CategoryItemResponse>?.toCategories() =
    this?.map { it.toCategory() } ?: listOf()
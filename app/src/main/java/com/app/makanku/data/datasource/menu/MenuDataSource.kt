package com.app.makanku.data.datasource.menu

import com.app.makanku.data.source.network.model.checkout.CheckoutRequestPayload
import com.app.makanku.data.source.network.model.checkout.CheckoutResponse
import com.app.makanku.data.source.network.model.menu.MenuResponse

interface MenuDataSource {
    suspend fun getMenuData(categoryName: String? = null): MenuResponse

    suspend fun createOrder(payload: CheckoutRequestPayload): CheckoutResponse
}

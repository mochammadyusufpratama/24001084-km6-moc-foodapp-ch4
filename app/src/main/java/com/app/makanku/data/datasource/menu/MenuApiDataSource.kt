package com.app.makanku.data.datasource.menu

import com.app.makanku.data.source.network.model.checkout.CheckoutRequestPayload
import com.app.makanku.data.source.network.model.checkout.CheckoutResponse
import com.app.makanku.data.source.network.model.menu.MenuResponse
import com.app.makanku.data.source.network.services.FoodAppApiService

class MenuApiDataSource(
    private val service: FoodAppApiService,
) : MenuDataSource {
    override suspend fun getMenuData(categoryName: String?): MenuResponse {
        return service.getMenu(categoryName)
    }

    override suspend fun createOrder(payload: CheckoutRequestPayload): CheckoutResponse {
        return service.createOrder(payload)
    }
}

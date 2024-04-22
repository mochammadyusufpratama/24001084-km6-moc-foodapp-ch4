package com.app.makanku.data.repository

import com.app.makanku.data.datasource.menu.MenuDataSource
import com.app.makanku.data.mapper.toMenus
import com.app.makanku.data.model.Cart
import com.app.makanku.data.model.Menu
import com.app.makanku.data.source.network.model.checkout.CheckoutRequestPayload
import com.app.makanku.data.source.network.model.checkout.CheckoutResponsePayload
import com.app.makanku.utils.ResultWrapper
import com.app.makanku.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MenuRepository {
    fun getMenu(categoryName: String? = null): Flow<ResultWrapper<List<Menu>>>
    fun createOrder(items: List<Cart>): Flow<ResultWrapper<Boolean>>
}

class MenuRepositoryImpl(
    private val dataSource: MenuDataSource,
    private val userRepository: UserRepository
) : MenuRepository {
    override fun getMenu(categoryName: String?): Flow<ResultWrapper<List<Menu>>> {
        return flow {
            emit(ResultWrapper.Loading())
            delay(1000)
            val menuData = dataSource.getMenuData(categoryName).data.toMenus()
            emit(ResultWrapper.Success(menuData))
        }
    }

    override fun createOrder(items: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {

            val currentUser = userRepository.getCurrentUser()
            val total = items.sumOf { it.menuPrice }
            dataSource.createOrder(
                CheckoutRequestPayload(
                    total = total.toInt(),
                    username = currentUser?.fullName,
                    orders = items.map {
                        CheckoutResponsePayload(
                            notes = it.itemNotes,
                            price = it.menuPrice.toInt(),
                            name = it.menuName,
                            qty = it.itemQuantity,
                        )
                    }
                )
            ).status ?: false
        }
    }

}
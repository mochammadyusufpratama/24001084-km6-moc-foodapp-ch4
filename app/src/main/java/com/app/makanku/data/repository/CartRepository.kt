package com.app.makanku.data.repository

import com.app.makanku.data.datasource.cart.CartDataSource
import com.app.makanku.data.mapper.toCartEntity
import com.app.makanku.data.mapper.toCartList
import com.app.makanku.data.model.Cart
import com.app.makanku.data.model.PriceItem
import com.app.makanku.data.model.Product
import com.app.makanku.data.source.local.database.entity.CartEntity
import com.app.makanku.utils.ResultWrapper
import com.app.makanku.utils.proceed
import com.app.makanku.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.lang.IllegalStateException

interface CartRepository {
    fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>>
    fun getCheckoutData(): Flow<ResultWrapper<Triple<List<Cart>, List<PriceItem>, Double>>>
    fun createCart(
        product: Product,
        quantity: Int,
        notes: String? = null
    ): Flow<ResultWrapper<Boolean>>

    fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>
    fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>

    fun  deleteAllCart(): Flow<ResultWrapper<Unit>>

}

class CartRepositoryImpl(private val cartDataSource: CartDataSource) : CartRepository {
    override fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>> {
        return cartDataSource.getAllCarts()
            .map {
                //mapping into cart list and sum the total price
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf { it.productPrice * it.itemQuantity }
                    Pair(result, totalPrice)
                }
            }.map {
                //map to check when list is empty
                if (it.payload?.first?.isEmpty() == false) return@map it
                ResultWrapper.Empty(it.payload)
            }.onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override fun getCheckoutData(): Flow<ResultWrapper<Triple<List<Cart>, List<PriceItem>, Double>>> {
        return cartDataSource.getAllCarts()
            .map {
                //mapping into cart list and sum the total price
                proceed {
                    val result = it.toCartList()
                    val priceItemList =
                        result.map { PriceItem(it.productName, it.productPrice * it.itemQuantity) }
                    val totalPrice = priceItemList.sumOf { it.total }
                    Triple(result, priceItemList, totalPrice)
                }
            }.map {
                //map to check when list is empty
                if (it.payload?.first?.isEmpty() == false) return@map it
                ResultWrapper.Empty(it.payload)
            }.onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override fun createCart(
        product: Product,
        quantity: Int,
        notes: String?
    ): Flow<ResultWrapper<Boolean>> {
        return product.id?.let { productId ->
            //when id is not null
            proceedFlow {
                val affectedRow = cartDataSource.insertCart(
                    CartEntity(
                        productId = productId,
                        itemQuantity = quantity,
                        productName = product.name,
                        productImgUrl = product.imgUrl,
                        productPrice = product.price,
                        itemNotes = notes
                    )
                )
                delay(2000)
                affectedRow > 0
            }
        } ?: flow {
            //when id is doesnt exist
            emit(ResultWrapper.Error(IllegalStateException("Product ID not found")))
        }
    }

    override fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity -= 1
        }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { cartDataSource.deleteCart(item.toCartEntity()) > 0 }
        } else {
            proceedFlow { cartDataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply {
            itemQuantity += 1
        }
        return proceedFlow { cartDataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { cartDataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { cartDataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override fun deleteAllCart(): Flow<ResultWrapper<Unit>> {
        return proceedFlow { cartDataSource.deleteAll() }
    }

}
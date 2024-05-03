package com.app.makanku.data.source.network.services

import com.app.makanku.BuildConfig
import com.app.makanku.data.source.network.model.category.CategoryResponse
import com.app.makanku.data.source.network.model.checkout.CheckoutRequestPayload
import com.app.makanku.data.source.network.model.checkout.CheckoutResponse
import com.app.makanku.data.source.network.model.menu.MenuResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FoodAppApiService {
    @GET("category")
    suspend fun getCategories(): CategoryResponse

    @GET("listmenu")
    suspend fun getMenu(
        @Query("c") category: String? = null,
    ): MenuResponse

    @POST("order")
    suspend fun createOrder(
        @Body payload: CheckoutRequestPayload,
    ): CheckoutResponse

    companion object {
        @JvmStatic
        operator fun invoke(): FoodAppApiService {
            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(FoodAppApiService::class.java)
        }
    }
}

package com.keyvalue.siren.androidsdk.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object {
        private var retrofit: Retrofit? = null

        fun getRetrofitInstance(baseUrl: String): Retrofit? {
            if (retrofit == null && baseUrl.isNotEmpty()) {
                var absoluteBaseUrl = baseUrl
                if (absoluteBaseUrl[baseUrl.length - 1] != '/') absoluteBaseUrl = "$baseUrl/"
                val okHttpClientOptions =
                    OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)

                retrofit =
                    Retrofit.Builder().baseUrl(
                        absoluteBaseUrl,
                    ).addConverterFactory((GsonConverterFactory.create()))
                        .client(okHttpClientOptions.build()).build()
            }
            return retrofit
        }
    }
}

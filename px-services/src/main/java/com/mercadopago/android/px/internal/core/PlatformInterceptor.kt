package com.mercadopago.android.px.internal.core

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PlatformInterceptor(context: Context) : Interceptor {
    private val currentPlatform = getPlatform(context)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .header(HEADER_PLATFORM, currentPlatform)
            .build()
        return chain.proceed(request)
    }

    private fun getPlatform(context: Context): String {
        val packageName = context.applicationInfo.packageName
        return if (packageName.contains("com.mercadolibre")) PLATFORM_ML else PLATFORM_MP
    }

    companion object {
        private const val HEADER_PLATFORM = "x-platform"
        private const val PLATFORM_MP = "MP"
        private const val PLATFORM_ML = "ML"
    }
}
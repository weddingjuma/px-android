package com.mercadopago.android.px.internal.core

import android.content.Context
import android.net.ConnectivityManager

class ConnectionHelper {

    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context
    }

    fun checkConnection() = try {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                if (networkInfo.isConnectedOrConnecting) {
                    haveConnectedWifi = true
                }
            }
            if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                if (networkInfo.isConnectedOrConnecting) {
                    haveConnectedMobile = true
                }
            }
        }

        haveConnectedWifi || haveConnectedMobile
    } catch (ex: Exception) {
        false
    }

    companion object {
        @JvmStatic
        val instance by lazy { ConnectionHelper() }
    }

}
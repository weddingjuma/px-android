package com.mercadopago.android.px.internal.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment

internal class ViewModelModule() {
    private val factory = ViewModelFactory()
    fun <T : ViewModel?> get(fragment: Fragment, modelClass: Class<T>): T {
        return ViewModelProviders.of(fragment, factory).get(modelClass)
    }
}
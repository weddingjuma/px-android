package com.mercadopago.android.px.tracking.internal.events

import com.mercadopago.android.px.tracking.internal.model.GenericDialogTrackData

class GenericDialogOpenEvent(private val data: GenericDialogTrackData.Open) : EventTracker() {

    override fun getEventPath(): String {
        return "${BASE_PATH}/dialog/open"
    }

    override fun getEventData(): MutableMap<String, Any> = data.toMap()
}
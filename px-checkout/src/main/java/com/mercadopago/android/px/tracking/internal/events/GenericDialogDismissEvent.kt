package com.mercadopago.android.px.tracking.internal.events

import com.mercadopago.android.px.tracking.internal.model.GenericDialogTrackData

class GenericDialogDismissEvent(private val data: GenericDialogTrackData.Dismiss) : EventTracker() {

    override fun getEventPath(): String {
        return "${BASE_PATH}/dialog/dismiss"
    }

    override fun getEventData(): MutableMap<String, Any> = data.toMap()
}
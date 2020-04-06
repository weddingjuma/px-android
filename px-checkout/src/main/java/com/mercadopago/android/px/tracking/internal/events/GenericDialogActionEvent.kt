package com.mercadopago.android.px.tracking.internal.events

import com.mercadopago.android.px.tracking.internal.model.GenericDialogTrackData

class GenericDialogActionEvent(private val data: GenericDialogTrackData.Action) : EventTracker() {

    override fun getEventPath(): String {
        return "${BASE_PATH}/dialog/action"
    }

    override fun getEventData(): MutableMap<String, Any> = data.toMap()
}
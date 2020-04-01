package com.mercadopago.android.px.tracking.internal.events

import com.mercadopago.android.px.tracking.internal.model.RemedyTrackData

internal class RemedyEvent(private val data: RemedyTrackData): EventTracker() {

    override fun getEventPath() = "$BASE_PATH/result/error$REMEDY"

    override fun getEventData(): MutableMap<String, Any> = data.toMap()

    companion object {
        private const val REMEDY = "/remedy"
    }
}
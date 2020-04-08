package com.mercadopago.android.px.tracking.internal.model

internal data class RemedyTrackData(
        private val type: String,
        private val extraInfo: Map<String, String>
) : TrackingMapModel()
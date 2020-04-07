package com.mercadopago.android.px.tracking.internal.events

import com.mercadopago.android.px.tracking.internal.model.TargetBehaviourTrackData
import com.mercadopago.android.px.tracking.internal.views.ViewTracker

class TargetBehaviourEvent(private val data: TargetBehaviourTrackData) : EventTracker() {
    private var eventPath: String? = null

    override fun getEventPath(): String {
        check(eventPath != null) {
            "event path should not be null"
        }
        return eventPath!!
    }

    override fun getEventData(): MutableMap<String, Any> = data.toMap()

    override fun trackFromView(viewTracker: ViewTracker) {
        eventPath = "${viewTracker.viewPath}/target_behaviour"
        track()
    }
}
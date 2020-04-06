package com.mercadopago.android.px.internal.features.generic_modal

import com.mercadopago.android.px.internal.viewmodel.TextLocalized
import com.mercadopago.android.px.internal.viewmodel.mappers.Mapper
import com.mercadopago.android.px.model.internal.Modal

class FromModalToGenericDialogItem(
    @ActionType private val action: String, private val dialogDescription: String) : Mapper<Modal, GenericDialogItem>() {

    override fun map(value: Modal): GenericDialogItem {
        return GenericDialogItem(
            dialogDescription,
            value.imageUrl,
            TextLocalized(value.title, 0),
            TextLocalized(value.description, 0),
            value.mainButton.run { Actionable(label, target, action) },
            value.secondaryButton?.run { Actionable(label, target, action) })
    }
}
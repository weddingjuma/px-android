package com.mercadopago.android.px.internal.features.generic_modal

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringDef
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.mercadolibre.android.ui.widgets.MeliDialog
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.extensions.gone
import com.mercadopago.android.px.internal.extensions.isNotNullNorEmpty
import com.mercadopago.android.px.internal.util.PicassoLoader
import com.mercadopago.android.px.internal.util.ViewUtils
import com.mercadopago.android.px.internal.view.OnSingleClickListener

class GenericDialog : MeliDialog() {

    private lateinit var item: GenericDialogItem
    private var listener: Listener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.item = arguments?.getParcelable(ARG_GENERIC_DIALOG_ITEM)!!

        listener = when {
            targetFragment is Listener -> {
                targetFragment as Listener
            }
            parentFragment is Listener -> {
                parentFragment as Listener
            }
            context is Listener -> {
                context
            }
            else -> {
                throw IllegalStateException("Parent of ${javaClass.simpleName} " +
                    "should implement ${Listener::class.java.simpleName}")
            }
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            val image = findViewById<ImageView>(R.id.px_dialog_generic_image)
            item.imageUrl?.let { url -> PicassoLoader.getPicasso().load(url).into(image) } ?: image.gone()

            loadButton(findViewById(R.id.px_dialog_generic_button_main), item.mainAction)
            findViewById<Button>(R.id.px_dialog_generic_button_secondary).apply {
                loadButton(this, item.secondaryAction)
                background = ContextCompat.getDrawable(context, R.drawable.px_quiet_button_selector)
            }

            ViewUtils.loadOrGone(item.title.get(context), findViewById(R.id.px_dialog_generic_text_title))
            ViewUtils.loadOrGone(item.description.get(context), findViewById(R.id.px_dialog_generic_text_description))
        }

    }

    private fun loadButton(button: Button, actionable: Actionable?) {
        actionable?.let {
            button.text = it.label
            button.setOnClickListener(object : OnSingleClickListener() {
                override fun onSingleClick(v: View?) {
                    dismiss()
                    listener?.apply {
                        val action = if (it.deepLink.isNotNullNorEmpty()) {
                            Action.DeepLinkAction(it.deepLink!!)
                        } else {
                            Action.CustomAction(it.action!!)
                        }
                        onAction(action)
                    }
                }
            })
        } ?: button.gone()
    }

    override fun getContentView(): Int {
        return R.layout.px_dialog_generic
    }

    companion object {
        private const val TAG = "GENERIC_DIALOG_TAG"
        private const val ARG_GENERIC_DIALOG_ITEM = "arg_generic_dialog_item"

        @JvmStatic
        fun showDialog(fragmentManager: FragmentManager, item: GenericDialogItem): GenericDialog {
            val instance = GenericDialog()
            val arguments = Bundle()
            arguments.putParcelable(ARG_GENERIC_DIALOG_ITEM, item)
            instance.arguments = arguments
            instance.show(fragmentManager, TAG)
            return instance
        }
    }

    interface Listener {
        fun onAction(action: Action)
    }

    sealed class Action {
        class DeepLinkAction(val deepLink: String) : Action()
        class CustomAction(@ActionType val type: String) : Action()
    }

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(ActionType.PAY_WITH_OTHER_METHOD, ActionType.PAY_WITH_OFFLINE_METHOD, ActionType.ADD_NEW_CARD)
    annotation class ActionType {
        companion object {
            const val PAY_WITH_OTHER_METHOD = "pay_with_other_method"
            const val PAY_WITH_OFFLINE_METHOD = "pay_with_offline_method"
            const val ADD_NEW_CARD = "add_new_card"
        }
    }
}
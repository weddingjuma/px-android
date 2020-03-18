package com.mercadopago.android.px.internal.features.pay_button

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.mercadolibre.android.ui.widgets.MeliButton
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.di.Session
import com.mercadopago.android.px.internal.features.Constants
import com.mercadopago.android.px.internal.features.SecurityCodeActivity
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment
import com.mercadopago.android.px.internal.util.FragmentUtil
import com.mercadopago.android.px.internal.util.ViewUtils
import com.mercadopago.android.px.internal.view.OnSingleClickListener
import com.mercadopago.android.px.internal.viewmodel.PostPaymentAction
import com.mercadopago.android.px.model.Card
import com.mercadopago.android.px.model.IPaymentDescriptor
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.tracking.internal.model.Reason
import com.mercadopago.android.px.internal.viewmodel.PayButtonViewModel as ButtonConfig


class PayButtonFragment : Fragment(), PayButton.View {

    private lateinit var button: MeliButton
    private lateinit var viewModel: PayButtonViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pay_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.confirm_button)
        button.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                startPayment()
            }
        })

        viewModel.buttonTextLiveData.observe(viewLifecycleOwner, Observer { buttonConfig -> button.text = buttonConfig!!.getButtonText(this.context!!) })
        viewModel.paymentStartedLiveData.observe(viewLifecycleOwner, Observer { pair -> startLoadingButton(pair!!.first, pair.second) })
        viewModel.finishLoadingLiveData.observe(viewLifecycleOwner, Observer { v -> finishLoading(v!!) })
        viewModel.cvvRequiredLiveData.observe(viewLifecycleOwner, Observer { p -> showSecurityCodeScreen(p!!.first, p.second) })
        viewModel.recoverRequiredLiveData.observe(viewLifecycleOwner, Observer { r -> showSecurityCodeForRecovery(r!!) })
    }

    override fun onConfigurationProvided(paymentConfiguration: PaymentConfiguration) {
        viewModel.startPayment(paymentConfiguration)
    }

    override fun onReadyForPayment() {
        viewModel.requireConfiguration()
    }

    override fun onAnimationFinished() {
        viewModel.hasFinishPaymentAnimation()
    }

    fun startPayment() {
        viewModel.prePayment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_SECURITY_CODE) {
            cancelLoading()
            if (resultCode == Activity.RESULT_OK) {
                viewModel.prePayment()
            }
        } else if (resultCode == Constants.RESULT_ACTION) {
            handleAction(data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleAction(data: Intent?) {
        if (data != null && data.extras != null) {
            PostPaymentAction.fromBundle(data.extras).execute(object : PostPaymentAction.ActionController {
                override fun recoverPayment(postPaymentAction: PostPaymentAction) {
                    cancelLoading()
                    viewModel.recoverPayment()
                }

                override fun onChangePaymentMethod() {
                    cancelLoading()
                }
            })
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = Session.getInstance().viewModelModule.get(this, PayButtonViewModel::class.java)

        when {
            targetFragment is PayButtonHandler -> viewModel.attach(targetFragment as PayButtonHandler)
            context is PayButtonHandler -> viewModel.attach(context)
            else -> throw IllegalStateException("Parent should implement ${PayButtonHandler::class.java.simpleName}")
        }
    }

    override fun onDestroy() {
        FragmentUtil.removeFragment(activity!!.supportFragmentManager, TAG_EXPLODING_FRAGMENT)
        super.onDestroy()
    }


    fun finishLoading(params: ExplodeDecorator) {
        val fragment = FragmentUtil.getFragmentByTag(activity!!.supportFragmentManager, TAG_EXPLODING_FRAGMENT, ExplodingFragment::class.java)
        if (fragment != null) {
            fragment.finishLoading(params)
        } else {
            viewModel.hasFinishPaymentAnimation()
        }
    }

    private fun startLoadingButton(paymentTimeout: Int, buttonConfig: ButtonConfig) {
        hideConfirmButton()
        ViewUtils.runWhenViewIsFullyMeasured(view!!) {
            val explodeParams = ExplodingFragment.getParams(button,
                buttonConfig.getButtonProgressText(context!!), paymentTimeout)
            val explodingFragment = ExplodingFragment.newInstance(explodeParams)
            explodingFragment.setTargetFragment(this, 0)
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(android.R.id.content, explodingFragment, TAG_EXPLODING_FRAGMENT)
                ?.commitNowAllowingStateLoss()
        }
    }

    private fun cancelLoading() {
        viewModel.onLoadingCanceled()
        showConfirmButton()
        val fragment = activity!!.supportFragmentManager.findFragmentByTag(TAG_EXPLODING_FRAGMENT) as ExplodingFragment?
        if (fragment != null && fragment.isAdded && fragment.hasFinished()) {
            activity!!.supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commitNowAllowingStateLoss()
            restoreStatusBar()
        }
    }

    private fun restoreStatusBar() {
        activity?.let { ViewUtils.setStatusBarColor(ContextCompat.getColor(it, R.color.px_colorPrimaryDark), it.window) }
    }

    private fun hideConfirmButton() {
        button.clearAnimation()
        button.visibility = INVISIBLE
    }

    private fun showConfirmButton() {
        button.clearAnimation()
        button.visibility = VISIBLE
    }

    private fun showSecurityCodeForRecovery(recovery: PaymentRecovery) {
        cancelLoading()
        SecurityCodeActivity.startForRecovery(this, recovery, REQ_CODE_SECURITY_CODE)
    }

    private fun showSecurityCodeScreen(card: Card, reason: Reason?) {
        SecurityCodeActivity.startForSavedCard(this, card, reason, REQ_CODE_SECURITY_CODE)
    }

    override fun handlePaymentRecovery(paymentRecovery: PaymentRecovery) {
        viewModel.recoverPayment(paymentRecovery)
    }

    companion object {
        private const val REQ_CODE_SECURITY_CODE = 301
        private const val TAG_EXPLODING_FRAGMENT = "TAG_EXPLODING_FRAGMENT"

        @JvmStatic
        fun newInstance(targetFragment: Fragment) = PayButtonFragment().apply { setTargetFragment(targetFragment, 0) }
    }

    interface PayButtonHandler {
        fun requireConfiguration()
        fun onPaymentFinished(payment: IPaymentDescriptor)
        fun onPaymentError(error: MercadoPagoError)
        fun prePayment()
        fun onLoadingStarted()
        fun onLoadingCanceled()
    }
}
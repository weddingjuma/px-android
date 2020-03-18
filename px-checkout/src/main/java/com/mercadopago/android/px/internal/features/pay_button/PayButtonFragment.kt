package com.mercadopago.android.px.internal.features.pay_button

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.mercadolibre.android.ui.widgets.MeliButton
import com.mercadolibre.android.ui.widgets.MeliSnackbar
import com.mercadopago.android.px.R
import com.mercadopago.android.px.addons.BehaviourProvider
import com.mercadopago.android.px.addons.model.SecurityValidationData
import com.mercadopago.android.px.internal.di.Session
import com.mercadopago.android.px.internal.features.Constants
import com.mercadopago.android.px.internal.features.SecurityCodeActivity
import com.mercadopago.android.px.internal.features.explode.ExplodeDecorator
import com.mercadopago.android.px.internal.features.explode.ExplodingFragment
import com.mercadopago.android.px.internal.features.plugins.PaymentProcessorActivity
import com.mercadopago.android.px.internal.util.FragmentUtil
import com.mercadopago.android.px.internal.util.ViewUtils
import com.mercadopago.android.px.internal.view.OnSingleClickListener
import com.mercadopago.android.px.internal.viewmodel.PostPaymentAction
import com.mercadopago.android.px.model.Card
import com.mercadopago.android.px.model.PaymentRecovery
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import com.mercadopago.android.px.model.internal.PaymentConfiguration
import com.mercadopago.android.px.tracking.internal.model.ConfirmData
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
                viewModel.preparePayment()
            }
        })

        viewModel.buttonTextLiveData.observe(viewLifecycleOwner,
            Observer { buttonConfig -> button.text = buttonConfig!!.getButtonText(this.context!!) })
        viewModel.paymentStartedLiveData.observe(viewLifecycleOwner,
            Observer { pair -> startLoadingButton(pair!!.first, pair.second) })
        viewModel.cvvRequiredLiveData.observe(viewLifecycleOwner,
            Observer { p -> showSecurityCodeScreen(p!!.first, p.second) })
        viewModel.recoverRequiredLiveData.observe(viewLifecycleOwner,
            Observer { r -> showSecurityCodeForRecovery(r!!) })
        viewModel.stateUILiveData.observe(viewLifecycleOwner, Observer { s -> onStateUIChanged(s!!) })
    }

    private fun onStateUIChanged(stateUI: PayButtonState) {
        when (stateUI) {
            is UIProgress.FingerprintRequired -> startBiometricsValidation(stateUI.validationData)
            is UIProgress.ButtonLoadingFinished -> finishLoading(stateUI.explodeDecorator)
            is UIProgress.ButtonLoadingCanceled -> cancelLoading()
            is UIResult.VisualProcessorResult -> PaymentProcessorActivity.start(this, REQ_CODE_PAYMENT_PROCESSOR)
            is UIError.ConnectionError -> showSnackBar(stateUI.error)
        }
    }

    override fun stimulate() {
        viewModel.preparePayment()
    }

    @SuppressLint("Range")
    private fun showSnackBar(error: MercadoPagoError) {
        view?.let {
            MeliSnackbar.make(it, error.message, Snackbar.LENGTH_LONG, MeliSnackbar.SnackbarType.ERROR).show()
        }
    }

    private fun startBiometricsValidation(validationData: SecurityValidationData) {
        button.state = MeliButton.State.DISABLED
        BehaviourProvider.getSecurityBehaviour().startValidation(this, validationData, REQ_CODE_BIOMETRICS)

    }

    override fun onReadyForPayment(paymentConfiguration: PaymentConfiguration, confirmTrackerData: ConfirmData) {
        viewModel.startSecuredPayment(paymentConfiguration, confirmTrackerData)
    }

    override fun onAnimationFinished() {
        viewModel.hasFinishPaymentAnimation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_CODE_BIOMETRICS) {
            button.state = MeliButton.State.NORMAL
            viewModel.handleBiometricsResult(resultCode == Activity.RESULT_OK)
        } else if (requestCode == REQ_CODE_SECURITY_CODE) {
            cancelLoading()
            if (resultCode == Activity.RESULT_OK) {
                viewModel.startPayment()
            }
        } else if (resultCode == Constants.RESULT_ACTION) {
            handleAction(data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleAction(data: Intent?) {
        data?.extras?.let {
            PostPaymentAction.fromBundle(data.extras!!).execute(object : PostPaymentAction.ActionController {
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
            targetFragment is PayButton.Handler -> viewModel.attach(targetFragment as PayButton.Handler)
            context is PayButton.Handler -> viewModel.attach(context)
            else -> throw IllegalStateException("Parent should implement ${PayButton.Handler::class.java.simpleName}")
        }
    }

    private val parentFragmentManager: FragmentManager
        get() = activity!!.supportFragmentManager

    override fun onDestroy() {
        FragmentUtil.removeFragment(parentFragmentManager, ExplodingFragment.TAG)
        super.onDestroy()
    }


    fun finishLoading(params: ExplodeDecorator) {
        FragmentUtil.getFragmentByTag(parentFragmentManager, ExplodingFragment.TAG, ExplodingFragment::class.java)
            ?.finishLoading(params)
            ?: viewModel.hasFinishPaymentAnimation()
    }

    private fun startLoadingButton(paymentTimeout: Int, buttonConfig: ButtonConfig) {
        hideConfirmButton()
        ViewUtils.runWhenViewIsFullyMeasured(view!!) {
            val explodeParams = ExplodingFragment.getParams(button,
                buttonConfig.getButtonProgressText(context!!), paymentTimeout)
            val explodingFragment = ExplodingFragment.newInstance(explodeParams)
            explodingFragment.setTargetFragment(this, 0)
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(android.R.id.content, explodingFragment, ExplodingFragment.TAG)
                ?.commitNowAllowingStateLoss()
        }
    }

    private fun cancelLoading() {
        showConfirmButton()
        val fragment = parentFragmentManager.findFragmentByTag(ExplodingFragment.TAG) as ExplodingFragment?
        if (fragment != null && fragment.isAdded && fragment.hasFinished()) {
            parentFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commitNowAllowingStateLoss()
            restoreStatusBar()
        }
    }

    private fun restoreStatusBar() {
        activity?.let {
            ViewUtils.setStatusBarColor(ContextCompat.getColor(it, R.color.px_colorPrimaryDark), it.window)
        }
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

    override fun isExploding(): Boolean {
        return FragmentUtil.isFragmentVisible(parentFragmentManager, ExplodingFragment.TAG)
    }

    companion object {
        const val TAG = "TAG_BUTTON_FRAGMENT"
        private const val REQ_CODE_SECURITY_CODE = 301
        private const val REQ_CODE_PAYMENT_PROCESSOR = 302
        private const val REQ_CODE_BIOMETRICS = 303

        @JvmStatic
        fun newInstance(targetFragment: Fragment) = PayButtonFragment().apply { setTargetFragment(targetFragment, 0) }
    }

}
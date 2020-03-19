package com.mercadopago.android.px.internal.features.payment_result.remedies

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.di.Session
import com.mercadopago.android.px.internal.features.payment_result.remedies.view.CvvRemedy
import com.mercadopago.android.px.internal.util.nonNullObserve
import com.mercadopago.android.px.internal.viewmodel.PaymentModel
import com.mercadopago.android.px.model.IPaymentDescriptor
import kotlinx.android.synthetic.main.px_remedies.*

internal class RemediesFragment : Fragment(), Remedies.View, CvvRemedy.Listener {

    private lateinit var viewModel: RemediesViewModel
    private var listener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.px_remedies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            getParcelable<RemediesModel>(REMEDIES_MODEL)?.let {
                val session = Session.getInstance()
                viewModel = RemediesViewModel(it, session.paymentRepository, session.configurationModule.paymentSettings,
                    session.cardTokenRepository, session.mercadoPagoESC, session.congratsRepository)
                buildViewModel()
                cvv.listener = this@RemediesFragment
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        } else {
            throw IllegalStateException("Parent should implement remedies listener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCvvFilled(cvv: String) {
        viewModel.onCvvFilled(cvv)
        listener?.enablePayButton()
    }

    override fun onCvvDeleted() {
        listener?.disablePayButton()
    }

    override fun onPayButtonPressed() {
        viewModel.onPayButtonPressed()
    }

    override fun onPaymentFinished(payment: IPaymentDescriptor) {
        viewModel.onPaymentFinished(payment)
    }

    private fun buildViewModel() {
        viewModel.remedyState.nonNullObserve(viewLifecycleOwner) {
            when (it) {
                is RemedyState.ShowCvvRemedy -> {
                    cvv.init(it.model)
                }

                is RemedyState.ShowKyCRemedy -> {

                }

                is RemedyState.StartPayment -> {
                    listener?.startPayment()
                }

                is RemedyState.ShowResult -> {
                    listener?.showResult(it.paymentModel)
                }
            }
        }
    }

    companion object {
        const val REMEDIES_TAG = "remedies"
        private const val REMEDIES_MODEL = "remedies_model"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RemediesFragment.
         */

        @JvmStatic
        fun newInstance(model: RemediesModel) = RemediesFragment().apply {
            arguments = Bundle().apply {
                putParcelable(REMEDIES_MODEL, model)
            }
        }
    }

    interface Listener {
        fun enablePayButton()
        fun disablePayButton()
        fun startPayment()
        fun showResult(paymentModel: PaymentModel)
    }
}
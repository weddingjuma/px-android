package com.mercadopago.android.px.internal.features.payment_result.remedies

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.util.nonNullObserve
import kotlinx.android.synthetic.main.px_remedies.*

internal class RemediesFragment : Fragment() {

    private lateinit var remediesViewModel: RemediesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.px_remedies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            getParcelable<RemediesModel>(REMEDIES_MODEL)?.let {
                remediesViewModel = RemediesViewModel(it)
                buildViewModel()
            }
        }
    }

    private fun buildViewModel() {
        remediesViewModel.remedyState.nonNullObserve(viewLifecycleOwner) {
            when(it) {
                is RemedyState.ShowCvvRemedy -> {
                    cvv.init(it.model)
                }

                is RemedyState.ShowKyCRemedy -> {

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

        fun newInstance(model: RemediesModel) = RemediesFragment().apply {
            arguments = Bundle().apply {
                putParcelable(REMEDIES_MODEL, model)
            }
        }
    }
}
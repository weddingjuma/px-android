package com.mercadopago.android.px.internal.features.payment_result.remedies

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadopago.android.px.R
import com.mercadopago.android.px.internal.base.BaseViewModel
import com.mercadopago.android.px.internal.features.payment_result.remedies.view.CvvRemedy

internal class RemediesFragment : Fragment(), Remedies.View {

    private lateinit var remediesViewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.px_remedies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remediesViewModel = RemediesViewModel()
    }

    override fun showCvvRemedy() {

    }

    companion object {
        private const val REMEDIES_MODEL = "remedies_model"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RemediesFragment.
         */

        fun newInstance(model: CvvRemedy.Model) = RemediesFragment().apply {
            arguments = Bundle().apply {
                putParcelable(REMEDIES_MODEL, model)
            }
        }
    }
}
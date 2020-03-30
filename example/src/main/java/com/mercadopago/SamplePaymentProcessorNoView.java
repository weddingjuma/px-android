package com.mercadopago;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.app.Fragment;
import com.mercadopago.android.px.core.SplitPaymentProcessor;
import com.mercadopago.android.px.model.IPayment;
import com.mercadopago.android.px.model.IPaymentDescriptor;
import com.mercadopago.android.px.model.internal.IParcelablePaymentDescriptor;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.util.Collections;
import java.util.List;

public class SamplePaymentProcessorNoView implements SplitPaymentProcessor {

    private static final int TIMEOUT = 20000;
    private static final int LOADING_TIME = 2000;

    public static final Creator<SamplePaymentProcessorNoView> CREATOR = new Creator<SamplePaymentProcessorNoView>() {
        @Override
        public SamplePaymentProcessorNoView createFromParcel(final Parcel in) {
            return new SamplePaymentProcessorNoView(in);
        }

        @Override
        public SamplePaymentProcessorNoView[] newArray(final int size) {
            return new SamplePaymentProcessorNoView[size];
        }
    };

    private int paymentIndex = 0;
    protected final List<? extends IPaymentDescriptor> payments;
    private final Handler handler = new Handler();

    public SamplePaymentProcessorNoView(final IPayment payment) {
        payments = Collections.singletonList(IParcelablePaymentDescriptor.with(payment));
    }

    public SamplePaymentProcessorNoView(final IPaymentDescriptor payment) {
        payments = Collections.singletonList(payment);
    }

    public SamplePaymentProcessorNoView(@NonNull @Size(min = 1) final List<? extends IPaymentDescriptor> payments) {
        this.payments = payments;
    }

    /* default */ SamplePaymentProcessorNoView(final Parcel in) {
        //Not used as parcel
        payments = null;
    }

    @Override
    public void startPayment(@NonNull final Context context, @NonNull final SplitPaymentProcessor.CheckoutData data,
        @NonNull final SplitPaymentProcessor.OnPaymentListener paymentListener) {
        handler.postDelayed(() -> paymentListener.onPaymentFinished(getPayment()), LOADING_TIME);
    }

    @Override
    public int getPaymentTimeout(@NonNull final CheckoutPreference checkoutPreference) {
        return TIMEOUT;
    }

    @Override
    public boolean shouldShowFragmentOnPayment(@NonNull final CheckoutPreference checkoutPreference) {
        return false;
    }

    @Override
    public boolean supportsSplitPayment(@Nullable final CheckoutPreference checkoutPreference) {
        return true;
    }

    @Nullable
    @Override
    public Fragment getFragment(@NonNull final CheckoutData data, @NonNull final Context context) {
        throw new IllegalStateException("this should never happen, is not a visual payment processor");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        //Not used as parcel
    }

    @NonNull
    protected IPaymentDescriptor getPayment() {
        if (paymentIndex >= payments.size()) {
            paymentIndex = 0;
        }
        return payments.get(paymentIndex++);
    }
}
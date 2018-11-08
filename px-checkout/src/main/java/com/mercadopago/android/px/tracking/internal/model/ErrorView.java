package com.mercadopago.android.px.tracking.internal.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.tracking.internal.utils.TrackingUtil;

public class ErrorView {

    public enum ErrorType {
        SNACKBAR("snackbar"), SCREEN("screen");

        final String description;

        ErrorType(final String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    private String path;
    private String style;
    private String id;
    private String message;
    private String attributableTo;
    @SerializedName("extra_info")
    private ApiErrorInfo apiErrorInfo;

    private ErrorView(@NonNull final String path, @NonNull final String style, @NonNull final String id,
        @NonNull final String message, @NonNull final String attributableTo, @NonNull final ApiErrorInfo apiErrorInfo) {
        this.path = path;
        this.style = style;
        this.id = id;
        this.message = message;
        this.attributableTo = attributableTo;
        this.apiErrorInfo = apiErrorInfo;
    }

    public static ErrorView createFrom(@Nullable final String path, @NonNull final MercadoPagoError mercadoPagoError,
        @NonNull final ErrorType errorType, @NonNull final String visibleMessage) {

        final ApiErrorInfo apiErrorInfo = ApiErrorInfo.createFrom(mercadoPagoError);
        final String eventPath = TextUtil.isEmpty(path) ? TrackingUtil.EVENT_PATH_GENERIC_ERROR : path;
        return new ErrorView(eventPath, errorType.description,
            TrackingUtil.EVENT_PATH_GENERIC_ERROR_ID, visibleMessage,
            TrackingUtil.EVENT_PATH_GENERIC_ERROR_ATTRIBUTABLE_TO, apiErrorInfo);
    }
}

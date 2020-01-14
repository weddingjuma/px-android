package com.mercadopago.android.px.internal.features.express.add_new_card;

import android.content.Context;
import android.support.annotation.NonNull;
import com.mercadopago.android.px.internal.util.ResourceUtil;
import com.mercadopago.android.px.model.OfflinePaymentMethod;
import com.mercadopago.android.px.model.OfflinePaymentType;
import com.mercadopago.android.px.model.OfflinePaymentTypesMetadata;
import java.util.ArrayList;
import java.util.List;

import static com.mercadopago.android.px.internal.util.TextUtil.isNotEmpty;

/* default */ final class FromOfflinePaymentTypesMetadataToOfflineItems {

    private final Context context;

    public FromOfflinePaymentTypesMetadataToOfflineItems(final Context context) {
        this.context = context;
    }

    @NonNull
    public List<OfflineMethodItem> map(@NonNull final OfflinePaymentTypesMetadata metadata) {
        final List<OfflineMethodItem> offlineMethodItems = new ArrayList<>();

        for (final OfflinePaymentType offlinePaymentType : metadata.getPaymentTypes()) {
            offlineMethodItems.add(new OfflineMethodItem(offlinePaymentType.getName()));
            for (final OfflinePaymentMethod offlinePaymentMethod : offlinePaymentType.getPaymentMethods()) {
                offlineMethodItems.add(
                    new OfflineMethodItem(offlinePaymentMethod.getId(), offlinePaymentType.getId(),
                        offlinePaymentMethod.getName(), offlinePaymentMethod.getDescription(),
                        getIconResourceId(offlinePaymentMethod.getId())));
            }
        }

        return offlineMethodItems;
    }

    private int getIconResourceId(@NonNull final String methodId) {
        if (isNotEmpty(methodId)) {
            return ResourceUtil.getIconResource(context, methodId);
        } else {
            return 0;
        }
    }
}
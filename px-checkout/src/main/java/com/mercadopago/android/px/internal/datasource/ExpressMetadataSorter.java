package com.mercadopago.android.px.internal.datasource;

import android.support.annotation.NonNull;
import com.mercadopago.android.px.model.ExpressMetadata;
import com.mercadopago.android.px.model.internal.DisabledPaymentMethod;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/* default */ public final class ExpressMetadataSorter {
    @NonNull private final Collection<ExpressMetadata> expressMetadataList;
    @NonNull private final Map<String, DisabledPaymentMethod> disabledPaymentMethodMap;

    /* default */ ExpressMetadataSorter(@NonNull final Collection<ExpressMetadata> expressMetadataList,
        @NonNull final Map<String, DisabledPaymentMethod> disabledPaymentMethodMap) {
        this.expressMetadataList = expressMetadataList;
        this.disabledPaymentMethodMap = disabledPaymentMethodMap;
    }

    /* default */ void sort() {
        final Iterator<ExpressMetadata> expressMetadataIterator = expressMetadataList.iterator();
        final Collection<ExpressMetadata> disabledExpressMetadataList = new ArrayList<>();
        while (expressMetadataIterator.hasNext()) {
            final ExpressMetadata expressMetadata = expressMetadataIterator.next();
            if (expressMetadata.isNewCard()) {
                break;
            } else if (disabledPaymentMethodMap.containsKey(expressMetadata.getCustomOptionId())) {
                expressMetadataIterator.remove();
                disabledExpressMetadataList.add(expressMetadata);
            }
        }
        expressMetadataList.addAll(disabledExpressMetadataList);
    }
}
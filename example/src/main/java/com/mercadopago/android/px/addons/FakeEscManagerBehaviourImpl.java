package com.mercadopago.android.px.addons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.util.TextUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FakeEscManagerBehaviourImpl implements ESCManagerBehaviour {

    private final Map<String, String> storage = new HashMap<>();

    @Override
    public void setSessionId(@NonNull final String sessionId) {
    }

    @Nullable
    @Override
    public String getESC(@Nullable final String cardId, @Nullable final String firstDigits,
        @Nullable final String lastDigits) {
        if (cardId != null && !cardId.isEmpty()) {
            return storage.get(cardId);
        } else if (TextUtil.isNotEmpty(firstDigits) && TextUtil.isNotEmpty(lastDigits)) {
            return storage.get(getKey(firstDigits, lastDigits));
        } else {
            return TextUtil.EMPTY;
        }
    }

    @Override
    public boolean saveESCWith(@NonNull final String cardId, @NonNull final String esc) {
        storage.put(cardId, esc);
        return true;
    }

    @Override
    public boolean saveESCWith(@NonNull final String firstDigits, @NonNull final String lastDigits,
        @NonNull final String esc) {
        storage.put(getKey(firstDigits, lastDigits), esc);
        return true;
    }

    @Override
    public void deleteESCWith(@NonNull final String cardId) {
        storage.remove(cardId);
    }

    @Override
    public void deleteESCWith(@NonNull final String firstDigits, @NonNull final String lastDigits) {
        storage.remove(getKey(firstDigits, lastDigits));
    }

    private String getKey(@NonNull final String firstDigits, @NonNull final String lastDigits) {
        return firstDigits + "_" + lastDigits;
    }

    @NonNull
    @Override
    public Set<String> getESCCardIds() {
        return storage.keySet();
    }

    @Override
    public boolean isESCEnabled() {
        return true;
    }
}
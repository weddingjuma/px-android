package com.mercadopago.android.px.mocks;

import com.mercadopago.android.px.internal.util.JsonUtil;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.utils.ResourcesUtil;

public class Cards {

    public static final String FAKE_BIN = "AAAAAA";
    public static final String MOCKED_BIN_VISA = "454461";
    public static final String MOCKED_BIN_CORDIAL = "522135";
    public static final String MOCKED_BIN_MASTER = "503175";

    private Cards() {
    }

    public static Card getCard() {
        String json = ResourcesUtil.getStringResource("card.json");
        return JsonUtil.fromJson(json, Card.class);
    }
}
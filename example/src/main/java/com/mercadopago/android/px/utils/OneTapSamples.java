package com.mercadopago.android.px.utils;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import com.mercadopago.SamplePaymentProcessorNoView;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.configuration.DiscountConfiguration;
import com.mercadopago.android.px.configuration.PaymentConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.core.SplitPaymentProcessor;
import com.mercadopago.android.px.model.GenericPayment;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.PaymentType;
import com.mercadopago.android.px.model.PaymentTypes;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mercadopago.android.px.utils.PaymentUtils.getBusinessPaymentApproved;
import static com.mercadopago.android.px.utils.PaymentUtils.getGenericPaymentApproved;

public final class OneTapSamples {

    private static final String ONE_TAP_PAYER_1_ACCESS_TOKEN =
        "APP_USR-6519316523937252-070516-964fafa7e2c91a2c740155fcb5474280__LA_LD__-261748045";
    private static final String ONE_TAP_PAYER_2_ACCESS_TOKEN =
        "APP_USR-3456261032857473-073011-c49291f53879f65accfaf28764e55f3e-340764447";
    private static final String ONE_TAP_PAYER_3_ACCESS_TOKEN =
        "TEST-244508097630521-031308-7b8b58d617aec50b3e528ca98606b116__LC_LA__-150216849";
    private static final String ONE_TAP_PAYER_4_ACCESS_TOKEN =
        "APP_USR-3841407354354687-070311-e89f762e2fc6bdb9131c40c58b98f2c4-333082795";
    private static final String ONE_TAP_PAYER_5_ACCESS_TOKEN =
        "APP_USR-7548115878322835-070311-e172a5d11f7f782622163724dbecb9cf-333082950";
    private static final String ONE_TAP_PAYER_6_ACCESS_TOKEN =
        "APP_USR-2962379700180713-073014-662103afe87bd62b4172af7e9599573c-340790299";
    private static final String ONE_TAP_PAYER_7_ACCESS_TOKEN =
        "TEST-7779559135594958-090815-348ca6a8851b34c17bf23a24b19a7b99__LA_LD__-227815697";
    private static final String ONE_TAP_PAYER_8_ACCESS_TOKEN =
        "TEST-1458038826212807-062020-ff9273c67bc567320eae1a07d1c2d5b5-246046416";
    private static final String ONE_TAP_PAYER_9_ACCESS_TOKEN =
        "APP_USR-1031243024729642-070215-4ce0d8f4d71d238fa10c33ac79428e85-332848643";
    private static final String ONE_TAP_MERCHANT_PUBLIC_KEY = "APP_USR-648a260d-6fd9-4ad7-9284-90f22262c18d";
    private static final String ONE_TAP_DIRECT_DISCOUNT_MERCHANT_PUBLIC_KEY =
        "APP_USR-ef65214d-59a2-4c82-be23-6cf6eb945d4c";
    private static final String PAYER_EMAIL_DUMMY = "prueba@gmail.com";
    private static final String SAVED_CARD_MERCHANT_PUBLIC_KEY_1 = "TEST-92f16019-1533-4f21-aaf9-70482692f41e";
    private static final String SAVED_CARD_PAYER_PRIVATE_KEY_1 =
        "TEST-4008515596580497-071112-4d6622f6fb95cb093fd38760751ec98d-335851940";

    private OneTapSamples() {
        //Do nothing
    }

    public static void addAll(final Collection<Pair<String, MercadoPagoCheckout.Builder>> options) {
        int i = 1;
        options.add(new Pair<>(i++ + " - One tap - MLA - with credit card and off methods",
            startMLAOneTapWithCreditCardAndOffMethods()));
        options.add(new Pair<>(i++ + " - One tap - MLM - with credit card and off methods",
            startMLMOneTapWithCreditCardAndOffMethods()));
        options.add(new Pair<>(i++ + " - One tap - MLA - without credit card", startMLAWithoutCreditAndDebitCard()));
        options.add(new Pair<>(i++ + " - One tap - MLA - without off methods", startMLAWithoutOffMethods()));

        options.add(new Pair<>(i++ + " - One tap - Should suggest account money (debit and credit cards)",
            startOneTapWithAccountMoneyAndCardsDebitCredit()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest debit card (excluded account money)",
            startOneTapWithAccountMoneyAndCardsDebitCreditAndExcludedAccountMoney()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest credit card (excluded account money and debit)",
            startOneTapWithAccountMoneyAndCardsDebitCreditAndExcludedAccountMoneyAndDebit()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest credit card (no account money)",
            startOneTapNoAccountMoneyWithCreditCard()));
        options.add(new Pair<>(i++ + " - One tap - Shouldn't suggest one tap (no cards no account money)",
            startOneTapNoAccountMoneyNoCards()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest credit card (no account money)",
            startOneTapNoAccountMoneyWithCredit()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest credit card (account money with second factor auth",
            startOneTapWithAccountMoneyAndSecondFactorAuthWithCredit()));
        options.add(new Pair<>(i++ + " - One tap - Shouldn't suggest one tap (second factor and excuded credit card)",
            startOneTapWithAccountMoneyAndSecondFactorAuthWithExcludedCreditCard()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest account money (credit card)",
            startOneTapWithAccountMoneyWithCreditCard()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest account money (amount lower than cap)",
            startOneTapWithAccountMoneyLowerThanCap()));
        options.add(new Pair<>(i++ + " - One tap - Shouldn't suggest one tap (amount greater than cap)",
            startOneTapWithAmountGreaterThanCap()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest account money (low account money)",
            startOneTapWithLowAccountMoneyWithLowerAmount()));
        options
            .add(new Pair<>(i++ + " - One tap - Should suggest credit card (low account money, amount lower than cap)",
                startOneTapWithLowAccountMoneyWithLowerAmountAndLowerCap()));
        options
            .add(new Pair<>(i++ + " - One tap - Shouldn't suggest one tap (low account money, amount greater than cap)",
                startOneTapWithLowAccountMoneyWithLowerAmountAndGreaterCap()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest credit card (no account money) with direct discount",
            startOneTapNoAccountMoneyWithCreditCardAndDirectDiscount()));
        options.add(
            new Pair<>(i++ + " - One tap - Should suggest credit card (no account money) with not available discount",
                startOneTapNoAccountMoneyWithCreditCardAndNoAvailableDiscount()));
        options.add(new Pair<>(i++ + " - One tap - Should suggest credit card and get call for authorize result",
            startOneTapWithCreditCardAndShowCallForAuthorize()));
    }

    // It should suggest one tap with credit card, call for authorize
    private static MercadoPagoCheckout.Builder startOneTapWithCreditCardAndShowCallForAuthorize() {
        final GenericPayment payment = new GenericPayment.Builder(Payment.StatusCodes.STATUS_REJECTED,
            Payment.StatusDetail.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE).setPaymentId(123L)
            .createGenericPayment();
        final SplitPaymentProcessor samplePaymentProcessor = new SamplePaymentProcessorNoView(payment);
        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add("account_money");
        excludedPaymentTypes.add("debit_card");
        final CheckoutPreference checkoutPreferenceWithPayerEmail =
            getCheckoutPreferenceWithPayerEmail(excludedPaymentTypes, 120);
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, checkoutPreferenceWithPayerEmail,
            PaymentConfigurationUtils.create(samplePaymentProcessor))
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setEscEnabled(true).build())
            .setPrivateKey(ONE_TAP_PAYER_2_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }




















    // It should suggest one tap with account money
    private static MercadoPagoCheckout.Builder startMLAOneTapWithCreditCardAndOffMethods() {

        final GenericPayment payment = getGenericPaymentApproved();

        final List<Item> items = new ArrayList<>();
        final Item item = new Item.Builder("Android", 1, new BigDecimal(500))
            .setDescription("Androide")
            .setPictureUrl("https://www.androidsis.com/wp-content/uploads/2015/08/marshmallow.png")
            .setId("1234")
            .build();
        items.add(item);

        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(PaymentTypes.DEBIT_CARD);
        //excludedPaymentTypes.add(PaymentTypes.CREDIT_CARD);

        final CheckoutPreference preference = new CheckoutPreference.Builder(Sites.ARGENTINA,
            PAYER_EMAIL_DUMMY, items)
            .addExcludedPaymentTypes(excludedPaymentTypes)
            .build();

        final PaymentConfiguration paymentConfiguration =
            PaymentConfigurationUtils.create(new SamplePaymentProcessorNoView(payment));

        return new MercadoPagoCheckout.Builder("APP_USR-3668ce4b-329e-4663-b833-496a2d06b7c7", "443971311-5aaf2d72-0e92-4c0d-81ca-5b1176dbcfc0")
            .setPrivateKey("APP_USR-3320780344791828-011417-7e4540adef34805626529f7d12759ae0-514187106")
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    //MLA Collector
    //test_user_40859539@testuser.com
    //qatest7645
    //Public key:TEST-53c2696b-85c6-4185-ad57-a969d91e2fe4
    //Access token:TEST-4129514163640002-080619-bfdc029b9f5220768d774f680c19f44f-443971311
    //Public key:APP_USR-3668ce4b-329e-4663-b833-496a2d06b7c7
    //Access token:APP_USR-4129514163640002-080619-2e291fd0b5144b87a8dc2f8e5abefbb8-443971311


    //MLA Payer
    //Access token:TEST-8666946229145884-060616-9baa5719327f2ef704b313e1a585f329__LB_LD__-17623406

    //test_user_61602078@testuser.com
    //qatest1127
    //Public key:TEST-cfc672e4-2143-442b-afbc-3079f02de7db
    //Access token:TEST-3320780344791828-011417-6774f44940b508eb8c4c87e7d8161f1b-514187106
    //Public key:APP_USR-4419b284-393a-4759-a961-e39525a50541
    //Access token:APP_USR-3320780344791828-011417-7e4540adef34805626529f7d12759ae0-514187106


    private static MercadoPagoCheckout.Builder startMLMOneTapWithCreditCardAndOffMethods() {

        final GenericPayment payment = getGenericPaymentApproved();

        final List<Item> items = new ArrayList<>();
        final Item item = new Item.Builder("Android", 1, new BigDecimal(500))
            .setDescription("Androide")
            .setPictureUrl("https://www.androidsis.com/wp-content/uploads/2015/08/marshmallow.png")
            .setId("1234")
            .build();
        items.add(item);

        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(PaymentTypes.DEBIT_CARD);
        excludedPaymentTypes.add(PaymentTypes.CREDIT_CARD);

        final CheckoutPreference preference = new CheckoutPreference.Builder(Sites.MEXICO,
            "test_user_16686261@testuser.com", items)
            .addExcludedPaymentTypes(excludedPaymentTypes)
            .build();

        final PaymentConfiguration paymentConfiguration =
            PaymentConfigurationUtils.create(new SamplePaymentProcessorNoView(payment));

        return new MercadoPagoCheckout.Builder("APP_USR-c9156fad-4d37-4d19-aa5e-f3338192c140", "241259769-b78cf0df-5d6b-42cc-bfb5-f1118f353c46")
            .setPrivateKey("APP_USR-4649111276090223-011417-70fbaf62c11a606806fa6b08eeda7a95-514191195")
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }
    //MLM Collector
    //test_user_6240411@testuser.com
    //qatest9064
    //Public key:TEST-629c94f7-7757-4303-891d-a2f21becd98f
    //Access token:TEST-3058575594414176-011909-b10bf3a28de77b25441fb85d3cc322fa__LA_LB__-241259769
    //Public key:APP_USR-c9156fad-4d37-4d19-aa5e-f3338192c140
    //Access token:APP_USR-3058575594414176-011909-1a1a757c87765143b3b5f77efe033ea1__LC_LD__-241259769

    //MLM Payer
    //test_user_71892562@testuser.com
    //qatest8852
    //Public key:TEST-edb908ad-b164-4bf5-ab14-842e9658e6d9
    //Access token:TEST-4649111276090223-011417-1cbf897207e3b7f33b1f095c0084588e-514191195
    //Public key:APP_USR-fe841928-f4d4-4f33-92ef-d5c60eaee76d
    //Access token:APP_USR-4649111276090223-011417-70fbaf62c11a606806fa6b08eeda7a95-514191195

    private static MercadoPagoCheckout.Builder startMLAWithoutCreditAndDebitCard() {

        final GenericPayment payment = getGenericPaymentApproved();

        final List<Item> items = new ArrayList<>();
        final Item item = new Item.Builder("Android", 1, new BigDecimal(500))
            .setDescription("Androide")
            .setPictureUrl("https://www.androidsis.com/wp-content/uploads/2015/08/marshmallow.png")
            .setId("1234")
            .build();
        items.add(item);

        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(PaymentTypes.DEBIT_CARD);
        excludedPaymentTypes.add(PaymentTypes.CREDIT_CARD);

        final CheckoutPreference preference = new CheckoutPreference.Builder(Sites.ARGENTINA,
            PAYER_EMAIL_DUMMY, items)
            .addExcludedPaymentTypes(excludedPaymentTypes)
            .build();

        final PaymentConfiguration paymentConfiguration =
            PaymentConfigurationUtils.create(new SamplePaymentProcessorNoView(payment));

        return new MercadoPagoCheckout.Builder("APP_USR-648a260d-6fd9-4ad7-9284-90f22262c18d", preference,
            paymentConfiguration)
            .setPrivateKey("APP_USR-6519316523937252-070516-964fafa7e2c91a2c740155fcb5474280__LA_LD__-261748045")
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    private static MercadoPagoCheckout.Builder startMLAWithoutOffMethods() {

        final GenericPayment payment = getGenericPaymentApproved();

        final List<Item> items = new ArrayList<>();
        final Item item = new Item.Builder("Android", 1, new BigDecimal(500))
            .setDescription("Androide")
            .setPictureUrl("https://www.androidsis.com/wp-content/uploads/2015/08/marshmallow.png")
            .setId("1234")
            .build();
        items.add(item);

        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(PaymentTypes.ATM);
        excludedPaymentTypes.add(PaymentTypes.BANK_TRANSFER);
        excludedPaymentTypes.add(PaymentTypes.TICKET);
        excludedPaymentTypes.add(PaymentTypes.DEBIT_CARD);
        excludedPaymentTypes.add(PaymentTypes.ACCOUNT_MONEY);

        final CheckoutPreference preference = new CheckoutPreference.Builder(Sites.ARGENTINA,
            PAYER_EMAIL_DUMMY, items)
            .addExcludedPaymentTypes(excludedPaymentTypes)
            .build();

        final PaymentConfiguration paymentConfiguration =
            PaymentConfigurationUtils.create(new SamplePaymentProcessorNoView(payment));

        return new MercadoPagoCheckout.Builder("APP_USR-648a260d-6fd9-4ad7-9284-90f22262c18d", preference,
            paymentConfiguration)
            .setPrivateKey("TEST-8666946229145884-060616-9baa5719327f2ef704b313e1a585f329__LB_LD__-176234066")
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }
























    // It should suggest one tap with account money
    private static MercadoPagoCheckout.Builder startOneTapWithAccountMoneyAndCardsDebitCredit() {

        final GenericPayment payment = getGenericPaymentApproved();
        final SplitPaymentProcessor samplePaymentProcessor = new SamplePaymentProcessorNoView(payment);
        final CheckoutPreference preference = getCheckoutPreferenceWithPayerEmail(120);
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, preference,
            PaymentConfigurationUtils
                .create(samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_2_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with debit card
    private static MercadoPagoCheckout.Builder startOneTapWithAccountMoneyAndCardsDebitCreditAndExcludedAccountMoney() {
        final GenericPayment payment = getGenericPaymentApproved();
        final SplitPaymentProcessor samplePaymentProcessor = new SamplePaymentProcessorNoView(payment);
        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add("account_money");
        final CheckoutPreference checkoutPreferenceWithPayerEmail =
            getCheckoutPreferenceWithPayerEmail(excludedPaymentTypes, 120);
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, checkoutPreferenceWithPayerEmail,
            PaymentConfigurationUtils.create(samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_2_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with credit card
    private static MercadoPagoCheckout.Builder startOneTapWithAccountMoneyAndCardsDebitCreditAndExcludedAccountMoneyAndDebit() {
        final GenericPayment payment = getGenericPaymentApproved();
        final SplitPaymentProcessor samplePaymentProcessor = new SamplePaymentProcessorNoView(payment);
        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add("account_money");
        excludedPaymentTypes.add("debit_card");
        final CheckoutPreference checkoutPreferenceWithPayerEmail =
            getCheckoutPreferenceWithPayerEmail(excludedPaymentTypes, 120);
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, checkoutPreferenceWithPayerEmail,
            PaymentConfigurationUtils.create(samplePaymentProcessor))
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setEscEnabled(true).build())
            .setPrivateKey(ONE_TAP_PAYER_2_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with credit card
    private static MercadoPagoCheckout.Builder startOneTapNoAccountMoneyWithCreditCard() {
        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, getCheckoutPreferenceWithPayerEmail(120),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_3_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It shouldn't suggest one tap
    private static MercadoPagoCheckout.Builder startOneTapNoAccountMoneyNoCards() {
        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, getCheckoutPreferenceWithPayerEmail(120),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_4_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with credit card
    private static MercadoPagoCheckout.Builder startOneTapNoAccountMoneyWithCredit() {
        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, getCheckoutPreferenceWithPayerEmail(120),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_5_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with credit card
    private static MercadoPagoCheckout.Builder startOneTapWithAccountMoneyAndSecondFactorAuthWithCredit() {

        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY,
            getCheckoutPreferenceWithPayerEmail(120),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_6_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It shouldn't suggest one tap
    private static MercadoPagoCheckout.Builder startOneTapWithAccountMoneyAndSecondFactorAuthWithExcludedCreditCard() {
        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add("credit_card");
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY,
            getCheckoutPreferenceWithPayerEmail(excludedPaymentTypes, 120), PaymentConfigurationUtils
            .create(
                samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_6_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with acount money
    private static MercadoPagoCheckout.Builder startOneTapWithAccountMoneyWithCreditCard() {
        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, getCheckoutPreferenceWithPayerEmail(120),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_7_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with acount money
    private static MercadoPagoCheckout.Builder startOneTapWithAccountMoneyLowerThanCap() {
        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY,
            getCheckoutPreferenceWithPayerEmail(120),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_8_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It shouldn't suggest one tap
    private static MercadoPagoCheckout.Builder startOneTapWithAmountGreaterThanCap() {
        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, getCheckoutPreferenceWithPayerEmail(800),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_8_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with acount money
    private static MercadoPagoCheckout.Builder startOneTapWithLowAccountMoneyWithLowerAmount() {

        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY,
            getCheckoutPreferenceWithPayerEmail(120),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_9_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with credit card
    private static MercadoPagoCheckout.Builder startOneTapWithLowAccountMoneyWithLowerAmountAndLowerCap() {

        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY,
            getCheckoutPreferenceWithPayerEmail(500),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_9_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It shouldn't suggest one tap
    private static MercadoPagoCheckout.Builder startOneTapWithLowAccountMoneyWithLowerAmountAndGreaterCap() {
        final SplitPaymentProcessor samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        return new MercadoPagoCheckout.Builder(ONE_TAP_MERCHANT_PUBLIC_KEY, getCheckoutPreferenceWithPayerEmail(701),
            PaymentConfigurationUtils
                .create(
                    samplePaymentProcessor))
            .setPrivateKey(ONE_TAP_PAYER_9_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with credit card
    private static MercadoPagoCheckout.Builder startOneTapNoAccountMoneyWithCreditCardAndDirectDiscount() {
        return new MercadoPagoCheckout.Builder(ONE_TAP_DIRECT_DISCOUNT_MERCHANT_PUBLIC_KEY,
            getCheckoutPreferenceWithPayerEmail(120),
            PaymentConfigurationUtils.create())
            .setPrivateKey(ONE_TAP_PAYER_3_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    // It should suggest one tap with credit card and not available discount
    private static MercadoPagoCheckout.Builder startOneTapNoAccountMoneyWithCreditCardAndNoAvailableDiscount() {
        final SamplePaymentProcessorNoView samplePaymentProcessor =
            new SamplePaymentProcessorNoView(getBusinessPaymentApproved());
        final CheckoutPreference preference = getCheckoutPreferenceWithPayerEmail(new ArrayList<>(), 120);
        return new MercadoPagoCheckout.Builder(ONE_TAP_DIRECT_DISCOUNT_MERCHANT_PUBLIC_KEY, preference,
            new PaymentConfiguration.Builder(samplePaymentProcessor)
                .setDiscountConfiguration(DiscountConfiguration.forNotAvailableDiscount()).build())
            .setPrivateKey(ONE_TAP_PAYER_3_ACCESS_TOKEN)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }

    private static CheckoutPreference getCheckoutPreferenceWithPayerEmail(final int amount) {
        return getCheckoutPreferenceWithPayerEmail(new ArrayList<>(), amount);
    }

    private static CheckoutPreference getCheckoutPreferenceWithPayerEmail(
        @NonNull final Collection<String> excludedPaymentTypes, final int amount) {
        final List<Item> items = new ArrayList<>();
        final Item item = new Item.Builder("Android", 1, new BigDecimal(amount))
            .setDescription("Androide")
            .setPictureUrl("https://www.androidsis.com/wp-content/uploads/2015/08/marshmallow.png")
            .setId("1234")
            .build();
        items.add(item);

        excludedPaymentTypes.add(PaymentTypes.DEBIT_CARD);
        excludedPaymentTypes.add(PaymentTypes.CREDIT_CARD);

        return new CheckoutPreference.Builder(Sites.ARGENTINA,
            PAYER_EMAIL_DUMMY, items)
            .addExcludedPaymentTypes(excludedPaymentTypes)
            .build();
    }

    private static CheckoutPreference getCheckoutPreferenceWithPayerEmail(
        @NonNull final Collection<String> excludedPaymentTypes, final int amount, final int defaultInstallments) {
        final List<Item> items = new ArrayList<>();
        final Item item =
            new Item.Builder("Product title", 1, new BigDecimal(amount))
                .build();
        items.add(item);
        return new CheckoutPreference.Builder(Sites.ARGENTINA,
            PAYER_EMAIL_DUMMY, items)
            .addExcludedPaymentTypes(excludedPaymentTypes)
            .setDefaultInstallments(defaultInstallments)
            .build();
    }

    // It should suggest one tap with debit card
    private static MercadoPagoCheckout.Builder startSavedCardsDefaultInstallments() {
        final GenericPayment payment = getGenericPaymentApproved();
        final SplitPaymentProcessor samplePaymentProcessor = new SamplePaymentProcessorNoView(payment);
        final Collection<String> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add("account_money");
        final CheckoutPreference checkoutPreferenceWithPayerEmail =
            getCheckoutPreferenceWithPayerEmail(excludedPaymentTypes, 120, 1);
        return new MercadoPagoCheckout.Builder(SAVED_CARD_MERCHANT_PUBLIC_KEY_1, checkoutPreferenceWithPayerEmail,
            PaymentConfigurationUtils.create(samplePaymentProcessor))
            .setPrivateKey(SAVED_CARD_PAYER_PRIVATE_KEY_1)
            .setAdvancedConfiguration(new AdvancedConfiguration.Builder().setExpressPaymentEnable(true).build());
    }
}
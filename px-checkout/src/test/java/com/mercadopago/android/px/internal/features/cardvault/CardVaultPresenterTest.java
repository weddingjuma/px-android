package com.mercadopago.android.px.internal.features.cardvault;

import android.content.Intent;
import android.support.annotation.Nullable;
import com.mercadopago.android.px.internal.datasource.IESCManager;
import com.mercadopago.android.px.internal.features.installments.PayerCostSolver;
import com.mercadopago.android.px.internal.repository.AmountConfigurationRepository;
import com.mercadopago.android.px.internal.repository.CardTokenRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.internal.util.ApiUtil;
import com.mercadopago.android.px.internal.util.TextUtil;
import com.mercadopago.android.px.model.AmountConfiguration;
import com.mercadopago.android.px.model.Card;
import com.mercadopago.android.px.model.Cause;
import com.mercadopago.android.px.model.CardInfo;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.model.PayerCost;
import com.mercadopago.android.px.model.PaymentRecovery;
import com.mercadopago.android.px.model.SavedESCCardToken;
import com.mercadopago.android.px.model.Token;
import com.mercadopago.android.px.model.exceptions.ApiException;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.mercadopago.android.px.preferences.PaymentPreference;
import com.mercadopago.android.px.tracking.internal.model.Reason;
import com.mercadopago.android.px.utils.StubFailMpCall;
import com.mercadopago.android.px.utils.StubSuccessMpCall;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardVaultPresenterTest {

    private CardVaultPresenter presenter;

    @Mock private PaymentSettingRepository paymentSettingRepository;
    @Mock private UserSelectionRepository userSelectionRepository;
    @Mock private AmountConfigurationRepository amountConfigurationRepository;
    @Mock private CardTokenRepository cardTokenRepository;
    @Mock private IESCManager iESCManager;
    @Mock private PayerCostSolver payerCostSolver;

    @Mock private CardVault.View view;

    @Before
    public void setUp() {
        configurePaymentPreferenceMock(null);

        presenter = new CardVaultPresenter(userSelectionRepository, paymentSettingRepository, iESCManager,
            amountConfigurationRepository, cardTokenRepository, payerCostSolver);

        presenter.setPaymentRecovery(null);
        presenter.attachView(view);
    }

    private void configurePaymentPreferenceMock(@Nullable final Integer defaultInstallments) {
        final PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setDefaultInstallments(defaultInstallments);
    }

    private PaymentRecovery providePaymentRecoveryMock() {
        final PaymentRecovery paymentRecovery = mock(PaymentRecovery.class);
        when(paymentRecovery.isTokenRecoverable()).thenReturn(true);
        return paymentRecovery;
    }

    private void configureMockedCardWith() {
        final Card card = mock(Card.class);
        when(card.getId()).thenReturn("1");
        when(userSelectionRepository.getCard()).thenReturn(card);
        presenter.setCard(card);
    }

    @Test
    public void whenTokenIsRecoverableThenStartTokenRecoveryFlow() {

        when(paymentSettingRepository.getToken()).thenReturn(mock(Token.class));

        PaymentRecovery paymentRecovery = providePaymentRecoveryMock();
        presenter.setPaymentRecovery(paymentRecovery);

        presenter.initialize();

        verify(view).askForSecurityCodeFromTokenRecovery(Reason.from(paymentRecovery));
    }

    @Test
    public void whenSavedCardNotPresentThenStartGuessingFlow() {
        presenter.initialize();

        verify(view).askForCardInformation();
    }

    @Test
    public void whenFlowCanceledThenFinishFlow() {
        presenter.onResultCancel();

        verify(view).cancelCardVault();
    }

    @Test
    public void whenCardHasInstallmentSelectedAndTokenThenFinishFlow() {
        presenter.resolveSecurityCodeRequest();

        verify(view).finishWithResult();
    }

    /**
     * Guessing
     */

    @Test
    public void whenGuessingCardHasInstallmentSelectedAndWithoutTokenThenStartSecurityCodeFlow() {
        configureMockedCardWith();
        final Card card = userSelectionRepository.getCard();
        when(iESCManager.getESC(card.getId(), card.getFirstSixDigits(), card.getLastFourDigits()))
            .thenReturn(TextUtil.EMPTY);

        presenter.resolveInstallmentsRequest();

        verify(view).startSecurityCodeActivity(Reason.SAVED_CARD);
    }

    @Test
    public void whenGuessingHasIssuerAndPayerCostThenFinishWithResult() {
        when(userSelectionRepository.getIssuer()).thenReturn(mock(Issuer.class));
        when(userSelectionRepository.getPayerCost()).thenReturn(mock(PayerCost.class));
        when(paymentSettingRepository.getToken()).thenReturn(mock(Token.class));

        presenter.resolveNewCardRequest(mock(Intent.class));

        verify(view).finishWithResult();
    }

    @Test
    public void whenGuessingWithIssuerSelectedAndPayerCostNotSelectedThenStartInstallmentFlow() {
        when(userSelectionRepository.getIssuer()).thenReturn(mock(Issuer.class));
        when(paymentSettingRepository.getToken()).thenReturn(mock(Token.class));

        presenter.resolveNewCardRequest(mock(Intent.class));

        verify(view).askForInstallments(any(CardInfo.class));
    }

    @Test
    public void whenGuessingCardHasNoIssuerThenStartIssuerFlow() {
        when(paymentSettingRepository.getToken()).thenReturn(mock(Token.class));

        presenter.resolveNewCardRequest(mock(Intent.class));

        verify(view).startIssuersActivity(argThat(List::isEmpty));
    }

    /**
     * Saved card
     */

    @Test
    public void whenEmptyOptionsThenShowEmptyPayerCostScreen() {
        presenter.onEmptyOptions();

        verify(view).showEmptyPayerCostScreen();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void whenSelectedPayerCostWithoutESCThenStartSecurityCodeActivity() {
        configureMockedCardWith();
        final Card card = userSelectionRepository.getCard();
        when(iESCManager.getESC(card.getId(), card.getFirstSixDigits(), card.getLastFourDigits()))
            .thenReturn(TextUtil.EMPTY);

        presenter.onSelectedPayerCost();

        verify(view).startSecurityCodeActivity(Reason.SAVED_CARD);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void whenPayerCostIsSelectedWithESCThenGetPayerCostList() {
        configureMockedCardWith();
        final Card card = userSelectionRepository.getCard();
        final SavedESCCardToken savedESCCardToken = any(SavedESCCardToken.class);

        when(cardTokenRepository.createToken(savedESCCardToken))
            .thenReturn(new StubSuccessMpCall<>(mock(Token.class)));
        when(iESCManager.getESC(card.getId(), card.getFirstSixDigits(), card.getLastFourDigits()))
            .thenReturn("1");

        presenter.onSelectedPayerCost();

        verify(view).showProgressLayout();
    }

    @Test
    public void whenCardInfoIsSetAndDisplayInstallmentsThenAskForInstallments() {
        final List<PayerCost> payerCosts = Collections.singletonList(mock(PayerCost.class));
        final CardInfo cardInfo = mock(CardInfo.class);

        presenter.setCardInfo(cardInfo);
        presenter.displayInstallments(payerCosts);

        verify(view).askForInstallments(cardInfo);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void whenSelectedPayerCostPayerWithEscAndTokenApiCallFailsThenShowError() {
        configureMockedCardWith();
        final Card card = userSelectionRepository.getCard();
        final SavedESCCardToken savedESCCardToken = any(SavedESCCardToken.class);

        when(cardTokenRepository.createToken(savedESCCardToken))
            .thenReturn(new StubFailMpCall<>(mock(ApiException.class)));
        when(iESCManager.getESC(card.getId(), card.getFirstSixDigits(), card.getLastFourDigits()))
            .thenReturn("1");

        presenter.onSelectedPayerCost();

        verify(view).showProgressLayout();
        verify(view).showError(any(MercadoPagoError.class), anyString());
        verifyNoMoreInteractions(view);
    }

    @Test
    public void whenRecoverFromFailureThenSaveESCAndFinishWithResult() {
        configureMockedCardWith();
        final Card card = userSelectionRepository.getCard();
        final Token token = mock(Token.class);

        when(cardTokenRepository.createToken(any(SavedESCCardToken.class)))
            .thenReturn(new StubFailMpCall<>(mock(ApiException.class)));
        when(iESCManager.getESC(card.getId(), card.getFirstSixDigits(), card.getLastFourDigits()))
            .thenReturn("1");

        presenter.onSelectedPayerCost();

        verify(view).showError(any(MercadoPagoError.class), anyString());

        when(cardTokenRepository.createToken(any(SavedESCCardToken.class)))
            .thenReturn(new StubSuccessMpCall<>(token));

        presenter.recoverFromFailure();

        verify(view, times(2)).showProgressLayout();
        verify(paymentSettingRepository).configure(any(Token.class));
        verify(iESCManager).saveESCWith(token.getCardId(), token.getEsc());
        verify(view).finishWithResult();

        verifyNoMoreInteractions(view);
    }

    @Test
    public void whenSelectedPayerCostPayerWithEscAndTokenApiCallFailsWithInvalidEscThenDeleteESCAndStartSecurityCodeActivity() {
        configureMockedCardWith();
        final Card card = userSelectionRepository.getCard();
        final ApiException apiException = mock(ApiException.class);
        final Cause cause = mock(Cause.class);
        final List<Cause> causes = new ArrayList<>();
        causes.add(cause);


        when(cause.getCode()).thenReturn(ApiException.ErrorCodes.INVALID_ESC);
        when(apiException.getStatus()).thenReturn(ApiUtil.StatusCodes.BAD_REQUEST);
        when(apiException.getCause()).thenReturn(causes);
        when(cardTokenRepository.createToken(any(SavedESCCardToken.class)))
            .thenReturn(new StubFailMpCall<>(apiException));
        when(iESCManager.getESC(card.getId(), card.getFirstSixDigits(), card.getLastFourDigits()))
            .thenReturn("1");

        presenter.onSelectedPayerCost();

        verify(view).showProgressLayout();

        verify(iESCManager).deleteESCWith(anyString());
        verify(view).startSecurityCodeActivity(Reason.SAVED_CARD);

        verifyNoMoreInteractions(view);
    }

    @Test
    public void whenAmountConfigurationSetAndInitializePresenterThenSolvePayerCosts() {
        configureMockedCardWith();
        when(amountConfigurationRepository.getCurrentConfiguration()).thenReturn(mock(AmountConfiguration.class));

        presenter.initialize();

        verify(payerCostSolver)
            .solve(presenter, amountConfigurationRepository.getCurrentConfiguration().getPayerCosts());
    }

    @Test
    public void whenIsOneTapFlowThenDoNotAskForInstallmentsAndContinueFlow() {
        configureMockedCardWith();
        when(userSelectionRepository.getPayerCost()).thenReturn(mock(PayerCost.class));

        presenter.initialize();

        verifyNoMoreInteractions(payerCostSolver);
    }
}

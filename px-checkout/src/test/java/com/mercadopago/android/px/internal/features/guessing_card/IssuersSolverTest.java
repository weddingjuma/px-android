package com.mercadopago.android.px.internal.features.guessing_card;

import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.repository.UserSelectionRepository;
import com.mercadopago.android.px.model.Issuer;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.preferences.PaymentPreference;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IssuersSolverTest {

    private static final Integer MOCKED_DEFAULT_INSTALLMENT = 6;
    @Mock private PaymentSettingRepository paymentSettingRepository;
    @Mock private UserSelectionRepository userSelectionRepository;

    @Mock private SummaryAmountListener listener;
    private List<Issuer> issuers;

    private IssuersSolver solver;

    @Before
    public void setUp() {
        issuers = new ArrayList<>();
        solver = new IssuersSolver(userSelectionRepository, paymentSettingRepository);
    }

    @Test
    public void whenDefaultInstallmentSelectedThenNotifyListener() {
        mockIssuerListWithOneItem();
        final CheckoutPreference mockCheckoutPref = mock(CheckoutPreference.class);
        final PaymentPreference paymentPreference = mock(PaymentPreference.class);
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(mockCheckoutPref);
        assert paymentSettingRepository.getCheckoutPreference() != null;
        when(mockCheckoutPref.getPaymentPreference()).thenReturn(paymentPreference);

        when(paymentSettingRepository.getCheckoutPreference().getPaymentPreference().getDefaultInstallments())
            .thenReturn(MOCKED_DEFAULT_INSTALLMENT);

        solver.solve(listener, issuers);

        verify(userSelectionRepository).select(issuers.get(0));
        verify(listener).onDefaultInstallmentSet();
    }

    @Test
    public void whenMultipleIssuersNotifyListener() {
        mockIssuerListWithTwoItem();
        solver.solve(listener, issuers);
        verifyNoMoreInteractions(userSelectionRepository);
        verify(listener).onMultipleIssuers(issuers);
    }

    @Test
    public void whenSelectedIssuerWithoutDefaultInstallmentNotifyListener() {
        mockIssuerListWithOneItem();
        final CheckoutPreference mockCheckoutPref = mock(CheckoutPreference.class);
        final PaymentPreference paymentPreference = mock(PaymentPreference.class);
        when(paymentSettingRepository.getCheckoutPreference()).thenReturn(mockCheckoutPref);
        assert paymentSettingRepository.getCheckoutPreference() != null;
        when(mockCheckoutPref.getPaymentPreference()).thenReturn(paymentPreference);

        when(paymentSettingRepository.getCheckoutPreference().getPaymentPreference().getDefaultInstallments())
            .thenReturn(null);

        solver.solve(listener, issuers);

        verify(userSelectionRepository).select(issuers.get(0));
        verify(listener).onIssuerWithoutDefaultInstallment();

    }

    private void mockIssuerListWithOneItem() {
        issuers.clear();
        final Issuer mockIssuer = mock(Issuer.class);
        issuers.add(mockIssuer);
    }

    private void mockIssuerListWithTwoItem() {
        issuers.clear();
        final Issuer mockIssuer = mock(Issuer.class);
        final Issuer anotherMockedIssuer = mock(Issuer.class);
        issuers.add(mockIssuer);
        issuers.add(anotherMockedIssuer);
    }
}

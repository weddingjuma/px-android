package com.mercadopago.android.px.internal.features.express;

import com.mercadopago.android.px.internal.repository.AmountRepository;
import com.mercadopago.android.px.internal.repository.DiscountRepository;
import com.mercadopago.android.px.internal.repository.GroupsRepository;
import com.mercadopago.android.px.internal.repository.PaymentRepository;
import com.mercadopago.android.px.internal.repository.PaymentSettingRepository;
import com.mercadopago.android.px.internal.view.InstallmentsDescriptorView;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.mappers.ElementDescriptorMapper;
import com.mercadopago.android.px.model.PaymentMethodSearch;
import com.mercadopago.android.px.model.Site;
import com.mercadopago.android.px.model.Sites;
import com.mercadopago.android.px.preferences.CheckoutPreference;
import com.mercadopago.android.px.utils.StubSuccessMpCall;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ExpressPaymentPresenterTest {

    @Mock
    private ExpressPayment.View view;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentSettingRepository configuration;

    @Mock
    private ElementDescriptorMapper elementDescriptorMapper;

    @Mock
    private GroupsRepository groupsRepository;

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private AmountRepository amountRepository;

    private ExpressPaymentPresenter expressPaymentPresenter;

    @Before
    public void setUp() {
        final CheckoutPreference preference = mock(CheckoutPreference.class);
        when(preference.getSite()).thenReturn(Sites.ARGENTINA);
        when(configuration.getCheckoutPreference()).thenReturn(preference);

        when(groupsRepository.getGroups())
            .thenReturn(new StubSuccessMpCall<>(mock(PaymentMethodSearch.class)));
        expressPaymentPresenter = new ExpressPaymentPresenter(paymentRepository, configuration, discountRepository, amountRepository,
            elementDescriptorMapper, groupsRepository);
        expressPaymentPresenter.attachView(view);

        verify(view).configurePagerAndInstallments(Matchers.anyListOf(DrawableFragmentItem.class), any(Site.class),
            anyInt(),
            Matchers.anyListOf(InstallmentsDescriptorView.Model.class));
    }

    @Test
    public void whenCanceledThenCancelAndTrack() {
        expressPaymentPresenter.cancel();
        verify(view).cancel();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void whenPresenterDetachedThenPaymentRepositoryIsDetached() {
        verify(paymentRepository).attach(expressPaymentPresenter);
        expressPaymentPresenter.detachView();
        verify(paymentRepository).detach(expressPaymentPresenter);
        verifyNoMoreInteractions(paymentRepository);
    }
}
package com.mercadopago.android.px.internal.features.express.slider;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.mercadopago.android.px.internal.viewmodel.RenderMode;
import com.mercadopago.android.px.internal.viewmodel.drawables.DrawableFragmentItem;
import com.mercadopago.android.px.internal.viewmodel.drawables.PaymentMethodFragmentDrawer;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PaymentMethodFragmentAdapter extends FragmentStatePagerAdapter {

    @NonNull private List<DrawableFragmentItem> items;
    @NonNull private PaymentMethodFragmentDrawer drawer;
    private int currentInstallment;

    public PaymentMethodFragmentAdapter(@NonNull final FragmentManager fm) {
        super(fm);
        items = Collections.emptyList();
        drawer = new PaymentMethodHighResDrawer();
    }

    public void setItems(@NonNull final List<DrawableFragmentItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(final int position) {
        return items.get(position).draw(drawer);
    }

    @Override
    public int getItemPosition(@NonNull final Object object) {
        return POSITION_NONE;
    }

    @Override
    public void setPrimaryItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        if (object instanceof ConsumerCreditsFragment) {
            ((ConsumerCreditsFragment) object).updateInstallment(currentInstallment);
        }
        super.setPrimaryItem(container, position, object);
    }

    public void updateInstallment(@NonNull Integer installmentSelected) {
        this.currentInstallment = installmentSelected;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void setRenderMode(@NonNull @RenderMode final String renderMode) {
        if (renderMode.equals(RenderMode.LOW_RES)) {
            drawer = new PaymentMethodLowResDrawer();
            notifyDataSetChanged();
        }
    }
}
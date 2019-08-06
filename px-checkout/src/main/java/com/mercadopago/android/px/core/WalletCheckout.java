package com.mercadopago.android.px.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import java.util.Objects;

/**
 * This class allows you to start payments with MercadoPagoApp / Wallet. It provides access to most of the checkout
 * experience behaviour.
 */
@SuppressWarnings("unused")
public final class WalletCheckout {

    private static final String WALLET_PACKAGE = "com.mercadopago.wallet";
    private static final String WALLET_PACKAGE_DEBUG = "com.mercadopago.wallet.debug";

    private final String preferenceId;

    public interface InitListener {

        void walletNotFound();
    }

    public WalletCheckout(@NonNull final String preferenceId) {
        this.preferenceId = Objects.requireNonNull(preferenceId);
    }

    public void startWallet(@NonNull final Activity activity, @NonNull final InitListener listener, final int reqCode) {
        final PackageManager packageManager = activity.getPackageManager();
        String packageName;
        if (isPackageInstalled(WALLET_PACKAGE, packageManager) ||
            isPackageInstalled(WALLET_PACKAGE_DEBUG, packageManager)) {
            startWalletIntent(activity, reqCode);
        } else {
            listener.walletNotFound();
        }
    }

    private void startWalletIntent(@NonNull final Activity activity, final int reqCode) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("mercadopago://checkout?pref_id=" + preferenceId));
        //TODO add hack flag depending on version.
        activity.startActivityForResult(intent, reqCode);
    }

    public void installWallet(@NonNull final Context context) {

    }

    private boolean isPackageInstalled(@NonNull final String packageName, final PackageManager packageManager) {
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            // TODO verify version
            return packageInfo != null;
        } catch (final PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
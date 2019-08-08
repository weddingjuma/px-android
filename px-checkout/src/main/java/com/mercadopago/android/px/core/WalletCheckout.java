package com.mercadopago.android.px.core;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import java.util.Objects;

/**
 * This class allows you to start payments with MercadoPago's App / Wallet. It provides access to most of the checkout
 * experience behaviour. As a fallback when the MercadoPago application is not installed you can choose to install,
 * start the web checkout or do whatever you want with it.
 */
@SuppressWarnings("unused")
public final class WalletCheckout {

    private static final String WALLET_PACKAGE = "com.mercadopago.wallet";

    private final Uri webLinkUri;
    private final Uri deepLinkUri;
    private final Uri installationUri;

    // TODO delete.
    public enum Site {
        ARGENTINA("mla");

        /* default */ final String siteId;

        Site(final String siteId) {
            this.siteId = siteId;
        }

    }

    public interface Listener {

        /**
         * Callback to be called when the MercadoPago's application is not installed.
         */
        void checkoutFailedWalletIsNotInstalled();

        /**
         * Callback to be called when the MercadoPago's application has not supported version.
         */
        void checkoutFailedWalletNeedsUpdate();

        /**
         * Callback to be called when the MercadoPago's application is available to pay.
         */
        void walletAvailable();
    }

    /**
     * It prepare the checkout minimum data needed to start the payment.
     *
     * @param preferenceId the preference to be payed.
     * @param site site needed to create the url for web checkout support.
     * @return an instance of wallet configuration to start the payment.
     */
    public static WalletCheckout configure(@NonNull final String preferenceId, @NonNull final Site site) {
        return new WalletCheckout(Objects.requireNonNull(preferenceId), site);
    }

    private WalletCheckout(@NonNull final String preferenceId, final Site site) {
        deepLinkUri = Uri.parse("mercadopago://checkout")
            .buildUpon()
            .appendQueryParameter("pref_id", preferenceId)
            .build();
        webLinkUri = Uri.parse("https://www.mercadopago.com/" + site.siteId + "/checkout/start")
            .buildUpon()
            .appendQueryParameter("pref_id", preferenceId)
            .build();
        installationUri = getDynamicInstallationUri(preferenceId);
    }

    /**
     * Resolves automatically with the best option available in which platform will perform the payment process.
     */
    public void startCheckout(@NonNull final AppCompatActivity activity, final int reqCode) {
        checkWalletAvailability(activity, new Listener() {
            @Override
            public void checkoutFailedWalletIsNotInstalled() {
                installAndStartWalletCheckout(activity, reqCode);
            }

            @Override
            public void checkoutFailedWalletNeedsUpdate() {
                startWebCheckout(activity, reqCode);
            }

            @Override
            public void walletAvailable() {
                startWalletCheckout(activity, reqCode);
            }
        });
    }

    /**
     * When this method is called it will try to start MercadoPago's app to start the payment.
     * If it fails in the process then it will call {@link Listener#checkoutFailedWalletIsNotInstalled()} , or {@link Listener#checkoutFailedWalletNeedsUpdate()}.
     *
     * If it succeed
     *
     * @param activity your activity. It won't be retained.
     * @param listener your error listener.
     */
    public void checkWalletAvailability(@NonNull final AppCompatActivity activity, @NonNull final Listener listener) {
        final PackageManager packageManager = activity.getPackageManager();
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(WALLET_PACKAGE, 0);
            //TODO add hack flag depending on version.
            listener.walletAvailable();
        } catch (final PackageManager.NameNotFoundException e) {
            listener.checkoutFailedWalletIsNotInstalled();
        }
    }

    /**
     * starts checkout in a browser.
     * <p>
     * WARNING - intent data result will be empty when the payment finishes.
     *
     * @param activity your activity. It won't be retained.
     * @param reqCode request code to be handled when checkout closes.
     */
    public void startWebCheckout(@NonNull final AppCompatActivity activity, final int reqCode) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(webLinkUri);
        activity.startActivityForResult(intent, reqCode);
    }

    /**
     * When this method is called it starts the native checkout experience inside MercadoPago's app.
     *
     * @param activity your activity. It won't be retained.
     * @param reqCode request code to be handled when checkout closes.
     */
    public void startWalletCheckout(@NonNull final AppCompatActivity activity, final int reqCode) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(deepLinkUri);
        intent.setPackage(WALLET_PACKAGE);
        activity.startActivityForResult(intent, reqCode);
    }

    /**
     * try to install MercadoPago's app and then when the user opens the app drives to the checkout.
     * <p>
     * WARNING - intent data result will be empty when the payment finishes.
     *
     * @param activity your activity. It won't be retained.
     * @param reqCode request code returned when checkout closes.
     */
    public void installAndStartWalletCheckout(@NonNull final AppCompatActivity activity, final int reqCode) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(installationUri);
        activity.startActivityForResult(intent, reqCode);
    }

    private Uri getDynamicInstallationUri(@NonNull final String preferenceId) {
        return Uri.parse("https://s9p2q.app.goo.gl/")
            .buildUpon()
            .appendQueryParameter("apn", WALLET_PACKAGE)
            .appendQueryParameter("efr", "1") // removes one of the loadings - only available in iOS.
            .appendQueryParameter("link", "https://www.mercadopago.com/m/checkout?pref_id=" + preferenceId)
            .build();
    }
}
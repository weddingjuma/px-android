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

    public interface ErrorHandler {

        /**
         * Callback to be called when the MercadoPago's application is not installed.
         */
        void checkoutFailedWalletIsNotInstalled();
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
     * When this method is called it will try to start MercadoPago's app to start the payment. If it fails in the
     * process then it will call {@link ErrorHandler#checkoutFailedWalletIsNotInstalled()}
     *
     * @param activity your activity. It won't be retained.
     * @param errorHandler your error handler.
     * @param reqCode request code to be handled when checkout closes.
     */
    public void startWalletCheckout(@NonNull final AppCompatActivity activity, @NonNull final ErrorHandler errorHandler,
        final int reqCode) {
        final PackageManager packageManager = activity.getPackageManager();
        String packageName;
        if (isPackageInstalled(WALLET_PACKAGE, packageManager)) {
            startWalletIntent(activity, reqCode);
        } else {
            errorHandler.checkoutFailedWalletIsNotInstalled();
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
    public void startInWeb(@NonNull final AppCompatActivity activity, final int reqCode) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(webLinkUri);
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
    public void installAndStartCheckout(@NonNull final AppCompatActivity activity, final int reqCode) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(installationUri);
        activity.startActivityForResult(intent, reqCode);
    }

    private Uri getDynamicInstallationUri(final String preferenceId) {
        return Uri.parse("https://s9p2q.app.goo.gl/")
            .buildUpon()
            .appendQueryParameter("apn", "com.mercadopago.wallet")
            .appendQueryParameter("efr", "1") // removes one of the loadings - only available in iOS.
            .appendQueryParameter("link", "https://www.mercadopago.com/checkout?pref_id=" + preferenceId)
            .build();
    }

    private void startWalletIntent(@NonNull final AppCompatActivity activity, final int reqCode) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(deepLinkUri);
        intent.setPackage(WALLET_PACKAGE);
        activity.startActivityForResult(intent, reqCode);
    }

    private boolean isPackageInstalled(@NonNull final String packageName, final PackageManager packageManager) {
        //TODO add hack flag depending on version.
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo != null;
        } catch (final PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
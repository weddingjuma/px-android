package com.mercadopago.android.px.internal.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import com.mercadopago.android.px.R;
import com.mercadopago.android.px.internal.features.uicontrollers.FontCache;

public final class FontUtil {

    private static Handler mHandler;

    private FontUtil() {
    }

    public static void fetchFonts(@NonNull final Context context) {
        if (!FontCache.hasTypeface(FontCache.CUSTOM_REGULAR_FONT)) {
            fetchRegularFont(context);
        }
        if (!FontCache.hasTypeface(FontCache.CUSTOM_MONO_FONT)) {
            fetchMonoFont(context);
        }
        if (!FontCache.hasTypeface(FontCache.CUSTOM_LIGHT_FONT)) {
            fetchLightFont(context);
        }
    }

    private static void fetchRegularFont(@NonNull final Context context) {
        FontsContractCompat.FontRequestCallback regularFontCallback = new FontsContractCompat
            .FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                FontCache.setTypeface(FontCache.CUSTOM_REGULAR_FONT, typeface);
            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
                //Do nothing
            }
        };
        FontsContractCompat.requestFont(context,
            getFontRequest(FontCache.FONT_ROBOTO, QueryBuilder.WIDTH_DEFAULT,
                QueryBuilder.WEIGHT_DEFAULT, QueryBuilder.ITALIC_DEFAULT),
            regularFontCallback,
            getHandlerThreadHandler());
    }

    private static void fetchLightFont(@NonNull final Context context) {
        FontsContractCompat.FontRequestCallback lightFontCallback = new FontsContractCompat
            .FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                FontCache.setTypeface(FontCache.CUSTOM_LIGHT_FONT, typeface);
            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
                //Do nothing
            }
        };
        FontsContractCompat.requestFont(context,
            getFontRequest(FontCache.FONT_ROBOTO, QueryBuilder.WIDTH_DEFAULT,
                QueryBuilder.WEIGHT_LIGHT, QueryBuilder.ITALIC_DEFAULT),
            lightFontCallback,
            getHandlerThreadHandler());
    }

    private static void fetchMonoFont(@NonNull final Context context) {
        FontsContractCompat.FontRequestCallback monoFontCallback = new FontsContractCompat
            .FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                FontCache.setTypeface(FontCache.CUSTOM_MONO_FONT, typeface);
            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
                //Do nothing
            }
        };
        FontsContractCompat.requestFont(context,
            getFontRequest(FontCache.FONT_ROBOTO_MONO, QueryBuilder.WIDTH_DEFAULT,
                QueryBuilder.WEIGHT_DEFAULT, QueryBuilder.ITALIC_DEFAULT),
            monoFontCallback,
            getHandlerThreadHandler());
    }

    private static FontRequest getFontRequest(@NonNull final String fontName, final int width, final int weight, final float italic) {
        QueryBuilder queryBuilder = new QueryBuilder(fontName)
            .withWidth(width)
            .withWeight(weight)
            .withItalic(italic)
            .withBestEffort(true);
        String query = queryBuilder.build();

        return new FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            query,
            R.array.com_google_android_gms_fonts_certs);
    }

    private static Handler getHandlerThreadHandler() {
        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }
}

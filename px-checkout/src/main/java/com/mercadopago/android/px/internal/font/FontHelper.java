package com.mercadopago.android.px.internal.font;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.util.SparseArray;
import android.widget.TextView;
import com.mercadolibre.android.ui.font.TypefaceHelper;
import com.mercadopago.android.px.R;

public final class FontHelper {

    private static final String FONT_ROBOTO_MONO = "Roboto Mono";
    private static final String PROVIDER_AUTHORITY = "com.google.android.gms.fonts";
    private static final String PROVIDER_PACKAGE = "com.google.android.gms";

    /* default */ static final SparseArray<Typeface> CACHE = new SparseArray<>();
    private static final Handler HANDLER;
    private static boolean initialized = false;

    static {
        final HandlerThread handlerThread = new HandlerThread("fonts");
        handlerThread.start();
        HANDLER = new Handler(handlerThread.getLooper());
    }

    private FontHelper() {
    }

    public static void init(@NonNull final Context context) {
        if (initialized) {
            return;
        }
        initialized = true;
        fetchFont(context, PxFont.MONOSPACE.id, FONT_ROBOTO_MONO, Typeface.MONOSPACE);
    }

    public static void setFont(@NonNull final CollapsingToolbarLayout toolbar, @NonNull final PxFont font) {
        final Typeface typeface = getFont(toolbar.getContext(), font);
        if (typeface != null) {
            toolbar.setCollapsedTitleTypeface(typeface);
            toolbar.setExpandedTitleTypeface(typeface);
        }
    }

    public static void setFont(@NonNull final TextView view, @NonNull final PxFont font) {
        final Typeface typeface = CACHE.get(font.id);
        if (typeface != null) {
            view.setTypeface(typeface);
        } else if (font.font != null) {
            TypefaceHelper.setTypeface(view, font.font);
        }
    }

    @Nullable
    public static Typeface getFont(@NonNull final Context context, @NonNull final PxFont font) {
        final Typeface typeface = CACHE.get(font.id);
        return typeface != null ? typeface : TypefaceHelper.getFontTypeface(context, font.font);
    }

    private static void fetchFont(@NonNull final Context context, final int id, @NonNull final String fontName,
        @Nullable final Typeface fallback) {
        final FontsContractCompat.FontRequestCallback callback = new FontsContractCompat.FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(final Typeface typeface) {
                CACHE.put(id, typeface);
            }

            @Override
            public void onTypefaceRequestFailed(final int reason) {
                if (fallback != null) {
                    CACHE.put(id, fallback);
                }
            }
        };
        FontsContractCompat.requestFont(context,
            new FontRequest(PROVIDER_AUTHORITY, PROVIDER_PACKAGE, fontName, R.array.com_google_android_gms_fonts_certs),
            callback, HANDLER);
    }
}
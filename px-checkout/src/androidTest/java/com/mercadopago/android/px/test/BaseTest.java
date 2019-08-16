package com.mercadopago.android.px.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.Rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BaseTest<T extends Activity> {

    @Rule
    public ActivityTestRule<T> testRule;

    protected void setup(final Class<T> activityClass) {
        testRule = new ActivityTestRule<>(activityClass, true, false);
    }

    protected Context getApplicationContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    protected void assertFinishCalledWithResult(final Activity activity, final int resultCode) {
        assertTrue(activity.isFinishing());
        try {
            final Field field = Activity.class.getDeclaredField("mResultCode");
            field.setAccessible(true);
            final int actualResultCode = (Integer) field.get(activity);
            assertEquals(actualResultCode, resultCode);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException(
                "Looks like the Android Activity class has changed it's private fields for mResultCode or mResultData.Time to update the reflection code.",
                e);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ActivityResult getActivityResult(final Activity activity) {
        try {
            final ActivityResult activityResult = new ActivityResult();
            // Result code
            Field field = Activity.class.getDeclaredField("mResultCode");
            field.setAccessible(true);
            activityResult.setResultCode((Integer) field.get(activity));
            // Extras
            field = Activity.class.getDeclaredField("mResultData");
            field.setAccessible(true);
            Intent resultData = (Intent) field.get(activity);
            if (resultData != null) {
                activityResult.setExtras(resultData.getExtras());
            }
            // Return
            return activityResult;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(
                "Looks like the Android Activity class has changed it's private fields for mResultCode or mResultData.Time to update the reflection code.",
                e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T> void putListExtra(final Intent intent, final String listName, final List<T> list) {
        if (list != null) {
            final Gson gson = new Gson();
            final Type listType = new TypeToken<List<T>>() { }.getType();
            intent.putExtra(listName, gson.toJson(list, listType));
        }
    }

    protected void sleepThread() {
        sleepThread(3000);
    }

    protected void sleepThread(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (final Exception ex) {
            // do nothing
        }
    }
}
package com.srenon.thunder.sdk;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Seb on 30/09/2017.
 */

public class ThunderSdk {

    private static ThunderSdk sInstance;

    @NonNull
    private final String mApiKey;

    private ThunderSdk(@NonNull String apiKey) {
        this.mApiKey = apiKey;
    }

    public static void init(@NonNull String apiKey) {
        if (sInstance == null) {
            sInstance = new ThunderSdk(apiKey);
        }
    }

    public static ThunderSdk getInstance() {
        if (sInstance == null) {
            Log.e("ThunderSdk", "ThunderSdk must be initialized before getting its instance.");
        }
        return sInstance;
    }
}

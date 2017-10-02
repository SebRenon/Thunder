/*
 *  Copyright (C) 2017 Sebastien Renon Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.srenon.thunder.sdk;

import com.srenon.thunder.sdk.callback.InteractionCallback;
import com.srenon.thunder.sdk.consumer.impl.ConsumerImpl;
import com.srenon.thunder.sdk.di.ThunderRegistry;
import com.srenon.thunder.sdk.domain.client.Consumer;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;
import com.srenon.thunder.sdk.util.ThunderUtil;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.observers.DefaultObserver;

/**
 * Created by Seb on 30/09/2017.
 *
 * Class to manage the SDK and provide functions to the client {@link ThunderSdkActions}
 */

public final class ThunderSdk implements ThunderSdkActions {

    private static ThunderSdk sInstance;

    private ThunderSdk(@NonNull String siteKey, @NonNull String apiKey, @NonNull String loginId, @NonNull String touchPoint, @NonNull String sharedSecret) {

        // Create a consumer object with the data provided
        Consumer consumer = new ConsumerImpl();
        consumer.setApiKey(apiKey);
        consumer.setTouchPoint(touchPoint);
        consumer.setSiteKey(siteKey);
        consumer.setLoginId(loginId);
        consumer.setSharedSecret(sharedSecret);

        // Init or registry to build the Thunder Component
        ThunderRegistry.init(consumer);
    }

    public static void init(@NonNull String siteKey, @NonNull String apiKey, @NonNull String loginId, @NonNull String touchPoint, @NonNull String sharedSecret) {
        if (sInstance == null) {
            try {
                // Validate data - throw exception if not valid
                ThunderUtil.validateInitData(siteKey, apiKey, loginId, touchPoint, sharedSecret);
                sInstance = new ThunderSdk(siteKey, apiKey, loginId, touchPoint, sharedSecret);
            } catch (MissingDataException e) {
                // We inform the developer
                Log.e("ThunderSdk", e.getMessage());
            }
        }
    }

    public static ThunderSdkActions getInstance() {
        if (sInstance == null) {
            Log.e("ThunderSdk", "ThunderSdk must be initialized before getting its instance.");
        }
        return sInstance;
    }

    /**
     * Function to send an interaction and get the response back via a callback, on the Android Main Thread.
     *
     * @param interaction {@link Interactions} you want to send
     * @param callback    the listener that will get the response back if not detached
     */
    @Override
    public void sendInteraction(@Interactions String interaction, @Nullable final InteractionCallback callback) {
        sendInteraction(interaction).subscribe(new DefaultObserver<InteractionResponse>() {

            @Override
            public void onNext(@io.reactivex.annotations.NonNull InteractionResponse interactionResponse) {
                if (callback != null) {
                    callback.onSuccess(interactionResponse);
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {
                // not needed
            }
        });
    }

    /**
     * Function to send an interaction and get an observable back
     * By default, the response will is observed on the Android Main Thread.
     * The consumer can override the behavior if desired.
     *
     * @param interaction {@link Interactions} you want to send
     * @return an observable to get the response
     */
    @Override
    public Observable<InteractionResponse> sendInteraction(@Interactions String interaction) {
        return ThunderRegistry.getComponent().provideInteractionUseCase().sendInteraction(interaction);
    }
}

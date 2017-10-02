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

package com.srenon.thunder.sdk.network;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Seb on 30/09/2017.
 *
 * This class is to build and manage Retrofit in order to handle the communication with the API
 */

public final class NetworkManager {

    private static final String API_BASE_URL = "https://onedemo.thunderhead.com/";

    private Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create());

    public NetworkManager(@NonNull String apiKey, @NonNull String loginId, @NonNull String sharedSecret) {

        // Here we build the interceptor which will add the required Authorization header
        OauthInterceptor interceptor = new OauthInterceptor.Builder()
                .consumerKey(apiKey + "!" + loginId)
                .consumerSecret(sharedSecret)
                .build();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add the interceptor to the client
        httpClient.addInterceptor(interceptor);
        builder.client(httpClient.build());
    }

    /**
     * Function to create a service
     *
     * @param serviceClass the Retrofit class where we defined the endpoints
     * @param <S>          the service created
     * @return the service
     */
    @NonNull
    public <S> S createService(Class<S> serviceClass) {
        return builder.build().create(serviceClass);
    }
}

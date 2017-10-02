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

package com.srenon.thunder.sdk.network.datasource.impl;

import com.srenon.thunder.sdk.data.DataSource;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;
import com.srenon.thunder.sdk.network.service.interaction.InteractionServices;
import com.srenon.thunder.sdk.network.service.interaction.request.InteractionBody;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;

/**
 * Implementation of the Data Source contract defined by the Data layer {@link DataSource}
 * Created by Seb on 01/10/2017.
 */

public final class WebDataSourceImpl implements DataSource {

    private InteractionServices mInteractionServices;

    public WebDataSourceImpl(@NonNull InteractionServices interactionServices) {
        mInteractionServices = interactionServices;
    }

    @Override
    public Observable<InteractionResponse> sendInteraction(@NonNull String touchPoint, @NonNull String interaction, @NonNull String siteKey, @Nullable String tid) {
        // Build the body of the request
        InteractionBody interactionBody = new InteractionBody(touchPoint, interaction);
        // Build the request and get back an Observable
        // at this time the request is not trigger,
        // it's only once the Observable has been subscribed on that the request is executed
        return mInteractionServices.sendInteraction(interactionBody, siteKey, tid);
    }
}

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

package com.srenon.thunder.sdk.domain.interactor.impl;

import com.srenon.thunder.sdk.domain.client.Consumer;
import com.srenon.thunder.sdk.domain.interactor.InteractionUseCase;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;
import com.srenon.thunder.sdk.domain.repository.DataRepository;

import android.support.annotation.NonNull;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

/**
 * Implementation of the UseCase contract.
 * Created by Seb on 01/10/2017.
 */

public final class InteractionUseCaseImpl implements InteractionUseCase {

    private final Consumer mConsumer;

    private final Scheduler mObserveOn;

    private final Scheduler mSubscribeOn;

    private DataRepository mDataRepository;

    public InteractionUseCaseImpl(@NonNull Consumer consumer, @NonNull DataRepository dataRepository, @Nonnull Scheduler observeOn, @Nonnull Scheduler subscribeOn) {
        mConsumer = consumer;
        mDataRepository = dataRepository;
        mObserveOn = observeOn;
        mSubscribeOn = subscribeOn;
    }

    @Override
    public Observable<InteractionResponse> sendInteraction(@NonNull String interaction) {
        // Here we ask the Data layer (Data Repository) to send an interaction
        // we don't know how it's done, we don't need to.
        return mDataRepository.getOptimization(mConsumer.getTouchPoint(), interaction, mConsumer.getSiteKey(), mConsumer.getTid())
                .map(new Function<InteractionResponse, InteractionResponse>() {
                    @Override
                    public InteractionResponse apply(@io.reactivex.annotations.NonNull InteractionResponse interactionResponse) throws Exception {

                        // Implement business logic:
                        // The tid received in the response should be stored and sent with the
                        // subsequent interaction request. The tid should be updated with every response
                        // received from ONE.
                        if (!mConsumer.hasTid()) {
                            mConsumer.setTid(interactionResponse.getTid());
                        }
                        // we pass the data along
                        return interactionResponse;
                    }
                })
                .subscribeOn(mSubscribeOn)
                .observeOn(mObserveOn);
    }
}

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

package com.srenon.thunder.sdk.di;

import com.srenon.thunder.sdk.data.DataSource;
import com.srenon.thunder.sdk.data.impl.DataRepositoryImpl;
import com.srenon.thunder.sdk.domain.client.Consumer;
import com.srenon.thunder.sdk.domain.interactor.InteractionUseCase;
import com.srenon.thunder.sdk.domain.interactor.impl.InteractionUseCaseImpl;
import com.srenon.thunder.sdk.domain.repository.DataRepository;
import com.srenon.thunder.sdk.network.NetworkManager;
import com.srenon.thunder.sdk.network.datasource.impl.WebDataSourceImpl;
import com.srenon.thunder.sdk.network.service.interaction.InteractionServices;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Dagger Module, I prefer define all the dependencies and their creations here vs using @Inject because
 * it gives me the capacity to use Interfaces when defining a constructor.
 *
 * For example:
 * //     public DataRepositoryImpl(DataSource dataSource) {
 * //         mDataSource = dataSource;
 * //     }
 *
 * Instead of having to pass an implementation of the interface:
 * //     @Inject
 * //     public DataRepositoryImpl(DataSourceImpl dataSource) {
 * //         mDataSource = dataSource;
 * //     }
 *
 * Created by Seb on 01/10/2017.
 */

@Module
public final class ThunderModule {

    private static final String API_BASE_URL = "https://onedemo.thunderhead.com/";

    @Singleton
    @Provides
    InteractionUseCase interactionUseCase(Consumer consumer, DataRepository dataRepository, @Named("OBSERVE_ON") Scheduler observeOn, @Named("SUBSCRIBE_ON") Scheduler subscribeOn) {
        return new InteractionUseCaseImpl(consumer, dataRepository, observeOn, subscribeOn);
    }

    @Singleton
    @Provides
    DataRepository dataRepository(DataSource dataSource) {
        return new DataRepositoryImpl(dataSource);
    }

    @Singleton
    @Provides
    DataSource dataSource(InteractionServices interactionServices) {
        return new WebDataSourceImpl(interactionServices);
    }

    @Singleton
    @Provides
    InteractionServices interactionServices(NetworkManager networkManager) {
        return networkManager.createService(InteractionServices.class);
    }

    @Singleton
    @Provides
    NetworkManager networkManager(Consumer consumer) {
        return new NetworkManager(API_BASE_URL, consumer.getApiKey(), consumer.getLoginId(), consumer.getSharedSecret());
    }


    @Provides
    @Named("OBSERVE_ON")
    Scheduler observeOnScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named("SUBSCRIBE_ON")
    Scheduler subscribeOnScheduler() {
        return Schedulers.io();
    }
}

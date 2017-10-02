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

import com.srenon.thunder.sdk.domain.client.Consumer;
import com.srenon.thunder.sdk.domain.interactor.InteractionUseCase;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * This is the Dagger Component for the SDK
 * Created by Seb on 01/10/2017.
 */

@Singleton
@Component(modules = ThunderModule.class)
public interface ThunderComponent {

    // All UseCases accessible to the client will come here
    InteractionUseCase provideInteractionUseCase();

    /**
     * This is to build the component {@link ThunderRegistry#init(Consumer)}
     */
    @Component.Builder
    interface Builder {

        ThunderComponent build();

        @BindsInstance
        Builder consumer(Consumer consumer);
    }
}

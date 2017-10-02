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

import android.support.annotation.NonNull;

/**
 * Class to manage the Dagger component used by the SDK.
 *
 * Created by Seb on 01/10/2017.
 */

public final class ThunderRegistry {

    private static ThunderComponent sComponent;

    public static void init(@NonNull Consumer consumer) {
        // Build the component once, then return the Singleton
        if (sComponent == null) {
            sComponent = DaggerThunderComponent.builder().consumer(consumer).build();
        }
    }

    public static ThunderComponent getComponent() {
        return sComponent;
    }
}

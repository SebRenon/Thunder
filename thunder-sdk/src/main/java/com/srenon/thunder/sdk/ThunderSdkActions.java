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
import com.srenon.thunder.sdk.domain.model.InteractionResponse;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.Observable;

/**
 * Created by Seb on 01/10/2017.
 */

public interface ThunderSdkActions {

    String INTERACTION_HOME = "homeView";

    String INTERACTION_LOGIN = "loginView";

    void sendInteraction(@Interactions String interaction, @Nullable final InteractionCallback callback);

    Observable<InteractionResponse> sendInteraction(@Interactions String interaction);


    @StringDef(value = {
            INTERACTION_HOME,
            INTERACTION_LOGIN
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Interactions {

    }
}

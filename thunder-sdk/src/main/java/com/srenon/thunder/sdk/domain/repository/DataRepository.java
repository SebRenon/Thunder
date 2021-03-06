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

package com.srenon.thunder.sdk.domain.repository;

import com.srenon.thunder.sdk.domain.model.InteractionResponse;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;

/**
 * Defintion of the contract for the Data Repository
 * Created by Seb on 01/10/2017.
 */

public interface DataRepository {

    Observable<InteractionResponse> sentInteraction(@NonNull String touchPoint, @NonNull String interaction, @NonNull String siteKey, @Nullable String tid);
}

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

package com.srenon.thunder.sdk.data.impl;

import com.srenon.thunder.sdk.data.DataSource;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;
import com.srenon.thunder.sdk.domain.repository.DataRepository;

import android.support.annotation.NonNull;

import io.reactivex.Observable;

/**
 * This is the Data Repository, responsible to use the correct Data Source
 * in order to fetch and return the data expected by the UseCase.
 * Here we only have one Data Source type, but we can imagine having multiples like Web, Database, ContentProvider, etc.
 *
 * Created by Seb on 01/10/2017.
 */

public final class DataRepositoryImpl implements DataRepository {

    private DataSource mDataSource;

    public DataRepositoryImpl(DataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public Observable<InteractionResponse> sentInteraction(@NonNull String touchPoint, @NonNull String interaction, @NonNull String siteKey, @NonNull String tid) {
        return mDataSource.sendInteraction(touchPoint, interaction, siteKey, tid);
    }
}

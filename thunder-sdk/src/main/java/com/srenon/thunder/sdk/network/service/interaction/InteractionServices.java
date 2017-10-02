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

package com.srenon.thunder.sdk.network.service.interaction;

import com.srenon.thunder.sdk.domain.model.InteractionResponse;
import com.srenon.thunder.sdk.network.service.interaction.request.InteractionBody;

import android.support.annotation.Nullable;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * This is the Retrofit definition of the ONE service we are using
 * Created by Seb on 30/09/2017.
 */

public interface InteractionServices {

    @Headers({
            "Content-Type: application/json",
            "Accept-Language: en-us",
            "Accept-Encoding: gzip, deflate",
            "X-Requested-With: XMLHttpRequest",
            "Datamimetype: application/json"
    })
    @POST("one/oauth1/rt/api/2.0/interaction")
    Observable<InteractionResponse> sendInteraction(@Body InteractionBody interactionBody, @Query("sk") String siteKey, @Nullable @Query("tid") String tid);
}

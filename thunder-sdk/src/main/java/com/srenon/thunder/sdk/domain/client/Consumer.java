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

package com.srenon.thunder.sdk.domain.client;


import android.support.annotation.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface for the Consumer manager, this is used by the UseCase so
 * the domain layer is responsible to define the contract
 * Created by Seb on 01/10/2017.
 */

public interface Consumer {

    @Nonnull
    String getApiKey();

    void setApiKey(@Nonnull String apiKey);

    @Nonnull
    String getLoginId();

    void setLoginId(@Nonnull String loginId);

    @Nonnull
    String getSiteKey();

    void setSiteKey(@Nonnull String siteKey);

    @Nonnull
    String getTouchPoint();

    void setTouchPoint(@Nonnull String touchPoint);

    @Nonnull
    String getTid();

    void setTid(@Nonnull String tid);

    boolean hasTid();

    void setSharedSecret(@Nonnull String sharedSecret);

    @Nonnull
    String getSharedSecret();
}

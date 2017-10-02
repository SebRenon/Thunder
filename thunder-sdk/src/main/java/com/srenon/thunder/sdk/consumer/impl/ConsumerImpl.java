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

package com.srenon.thunder.sdk.consumer.impl;

import com.google.common.base.Strings;

import com.srenon.thunder.sdk.domain.client.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is to manage the Consumer/Client information
 * Created by Seb on 01/10/2017.
 */

public final class ConsumerImpl implements Consumer {

    private String mApiKey;

    private String mSiteKey;

    @Nullable
    private String mTid;

    private String mTouchPoint;

    private String mLoginId;

    private String mSharedSecret;

    @Nonnull
    @Override
    public String getApiKey() {
        return mApiKey;
    }

    @Override
    public void setApiKey(@Nonnull String apiKey) {
        this.mApiKey = apiKey;
    }

    @Nonnull
    @Override
    public String getLoginId() {
        return mLoginId;
    }

    @Override
    public void setLoginId(@Nonnull String loginId) {
        this.mLoginId = loginId;
    }

    @Nonnull
    @Override
    public String getSiteKey() {
        return mSiteKey;
    }

    @Override
    public void setSiteKey(@Nonnull String siteKey) {
        this.mSiteKey = siteKey;
    }

    @Nonnull
    @Override
    public String getTouchPoint() {
        return mTouchPoint;
    }

    @Override
    public void setTouchPoint(@Nonnull String touchPoint) {
        this.mTouchPoint = touchPoint;
    }

    @Nullable
    @Override
    public String getTid() {
        return mTid;
    }

    @Override
    public void setTid(@Nonnull String tid) {
        this.mTid = tid;
    }

    @Override
    public boolean hasTid() {
        return !Strings.isNullOrEmpty(mTid);
    }

    @Nonnull
    @Override
    public String getSharedSecret() {
        return mSharedSecret;
    }

    @Override
    public void setSharedSecret(@Nonnull String sharedSecret) {
        this.mSharedSecret = sharedSecret;
    }
}

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

package com.srenon.thunder.sdk.network.service.interaction.request;

import android.support.annotation.NonNull;

/**
 * The model for the Request
 * Created by Seb on 30/09/2017.
 */

public class InteractionBody {

    private String uri;

    public InteractionBody(@NonNull String touchPoint, @NonNull String interactionPath) {
        this.uri = String.format("%s/%s", touchPoint, interactionPath);
    }

    public String getUri() {
        return uri;
    }
}

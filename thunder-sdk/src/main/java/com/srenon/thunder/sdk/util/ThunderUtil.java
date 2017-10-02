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

package com.srenon.thunder.sdk.util;

import com.google.common.base.Strings;

import com.srenon.thunder.sdk.MissingDataException;

/**
 * Created by Seb on 01/10/2017.
 *
 * Util class for the Sdk
 */

public final class ThunderUtil {

    private ThunderUtil() {
        // empty constructor
    }

    public static boolean validateInitData(String siteKey, String apiKey, String loginId, String touchPoint, String sharedSecret) throws MissingDataException {
        if (Strings.isNullOrEmpty(siteKey)) {
            throw new MissingDataException("ThunderSdk(init): siteKey is missing.");
        } else if (Strings.isNullOrEmpty(apiKey)) {
            throw new MissingDataException("ThunderSdk(init): apiKey is missing.");
        } else if (Strings.isNullOrEmpty(loginId)) {
            throw new MissingDataException("ThunderSdk(init): loginId is missing.");
        } else if (Strings.isNullOrEmpty(touchPoint)) {
            throw new MissingDataException("ThunderSdk(init): touchPoint is missing.");
        } else if (Strings.isNullOrEmpty(sharedSecret)) {
            throw new MissingDataException("ThunderSdk(init): sharedSecret is missing.");
        }
        return true;
    }
}

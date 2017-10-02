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

import com.srenon.thunder.sdk.MissingDataException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Seb on 02/10/2017.
 */

public class ThunderUtilTest {

    @Test
    public void validateInitData() {

        try {
            ThunderUtil.validateInitData("a", "a", "a", "a", "");
            fail("I was expecting an exception");
        } catch (MissingDataException e) {
            assertEquals(e.getMessage(), "ThunderSdk(init): sharedSecret is missing.");
        }

        try {
            ThunderUtil.validateInitData("a", "a", "a", "", "a");
            fail("I was expecting an exception");
        } catch (MissingDataException e) {
            assertEquals(e.getMessage(), "ThunderSdk(init): touchPoint is missing.");
        }

        try {
            ThunderUtil.validateInitData("a", "a", "", "a", "a");
            fail("I was expecting an exception");
        } catch (MissingDataException e) {
            assertEquals(e.getMessage(), "ThunderSdk(init): loginId is missing.");
        }

        try {
            ThunderUtil.validateInitData("a", "", "a", "a", "a");
            fail("I was expecting an exception");
        } catch (MissingDataException e) {
            assertEquals(e.getMessage(), "ThunderSdk(init): apiKey is missing.");
        }

        try {
            ThunderUtil.validateInitData("", "a", "a", "a", "a");
            fail("I was expecting an exception");
        } catch (MissingDataException e) {
            assertEquals(e.getMessage(), "ThunderSdk(init): siteKey is missing.");
        }

        try {
            ThunderUtil.validateInitData("a", "a", "a", "a", "a");
        } catch (MissingDataException e) {
            fail("I was not expecting an exception");
        }
    }
}

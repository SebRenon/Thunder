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

package com.srenon.thunder.sdk.consumer;

import com.srenon.thunder.sdk.consumer.impl.ConsumerImpl;
import com.srenon.thunder.sdk.domain.client.Consumer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Seb on 02/10/2017.
 */

public class ConsumerImplTest {

    @Test
    public void consumer() {

        Consumer consumer = new ConsumerImpl();

        assertFalse(consumer.hasTid());
        consumer.setTid("tid");
        assertTrue(consumer.hasTid());
        assertEquals(consumer.getTid(), "tid");

        consumer.setApiKey("apiKey");
        assertEquals(consumer.getApiKey(), "apiKey");

        consumer.setSiteKey("siteKey");
        assertEquals(consumer.getSiteKey(), "siteKey");

        consumer.setTouchPoint("touchPoint");
        assertEquals(consumer.getTouchPoint(), "touchPoint");

        consumer.setLoginId("loginId");
        assertEquals(consumer.getLoginId(), "loginId");

        consumer.setSharedSecret("sharedSecret");
        assertEquals(consumer.getSharedSecret(), "sharedSecret");
    }
}

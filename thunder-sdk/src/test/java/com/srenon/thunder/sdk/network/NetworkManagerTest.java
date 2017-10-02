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

package com.srenon.thunder.sdk.network;

import com.srenon.thunder.sdk.network.service.interaction.InteractionServices;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Seb on 02/10/2017.
 */

public class NetworkManagerTest {

    @Test
    public void createService() {
        NetworkManager networkManager = new NetworkManager("http://baseUrl.com", "", "", "");
        assertNotNull(networkManager.createService(InteractionServices.class));
    }
}

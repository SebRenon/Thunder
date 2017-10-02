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

import com.srenon.thunder.sdk.data.DataSource;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;
import com.srenon.thunder.sdk.network.datasource.impl.WebDataSourceImpl;
import com.srenon.thunder.sdk.network.service.interaction.InteractionServices;
import com.srenon.thunder.sdk.network.service.interaction.request.InteractionBody;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Seb on 02/10/2017.
 */

public class WebDataSourceImplTest {

    @Test
    public void constructor() {
        InteractionServices services = mock(InteractionServices.class);
        DataSource webDataSource = new WebDataSourceImpl(services);
        assertNotNull(webDataSource);
    }

    @Test
    public void getOptimization_success() {
        InteractionServices services = mock(InteractionServices.class);
        DataSource webDataSource = new WebDataSourceImpl(services);

        final InteractionResponse mockedInteractionResponse = mock(InteractionResponse.class);

        when(services.sendInteraction(any(InteractionBody.class), anyString(), anyString())).thenReturn(Observable.just(mockedInteractionResponse));

        Observable<InteractionResponse> result = webDataSource.sendInteraction("a", "b", "siteKey", "tid");

        ArgumentCaptor<InteractionBody> body = ArgumentCaptor.forClass(InteractionBody.class);

        verify(services).sendInteraction(body.capture(), eq("siteKey"), eq("tid"));
        verifyNoMoreInteractions(services);

        InteractionBody interactionBody = body.getValue();
        assertEquals(interactionBody.getUri(), "a/b");

        assertNotNull(result);

        result.test().assertNoErrors().assertValue(new Predicate<InteractionResponse>() {
            @Override
            public boolean test(@NonNull InteractionResponse interactionResponse) throws Exception {
                assertEquals(interactionResponse, mockedInteractionResponse);
                return true;
            }
        });
    }

    @Test
    public void getOptimization_error() {
        InteractionServices services = mock(InteractionServices.class);
        DataSource webDataSource = new WebDataSourceImpl(services);

        Observable<InteractionResponse> observable = Observable.error(new Exception("my exception"));

        when(services.sendInteraction(any(InteractionBody.class), anyString(), anyString())).thenReturn(observable);

        Observable<InteractionResponse> result = webDataSource.sendInteraction("a", "b", "siteKey", "tid");

        assertNotNull(result);

        result.test().assertError(new Predicate<Throwable>() {
            @Override
            public boolean test(@NonNull Throwable throwable) throws Exception {
                return "my exception".equals(throwable.getMessage());
            }
        });
    }

    @Test
    public void getOptimization_success_noTid() {
        InteractionServices services = mock(InteractionServices.class);
        DataSource webDataSource = new WebDataSourceImpl(services);

        final InteractionResponse mockedInteractionResponse = mock(InteractionResponse.class);

        when(services.sendInteraction(any(InteractionBody.class), anyString(), anyString())).thenReturn(Observable.just(mockedInteractionResponse));

        Observable<InteractionResponse> result = webDataSource.sendInteraction("a", "b", "siteKey", null);

        ArgumentCaptor<InteractionBody> body = ArgumentCaptor.forClass(InteractionBody.class);

        verify(services).sendInteraction(body.capture(), eq("siteKey"), (String) isNull());
        verifyNoMoreInteractions(services);

        InteractionBody interactionBody = body.getValue();
        assertEquals(interactionBody.getUri(), "a/b");

        assertNotNull(result);

        result.test().assertNoErrors().assertValue(new Predicate<InteractionResponse>() {
            @Override
            public boolean test(@NonNull InteractionResponse interactionResponse) throws Exception {
                assertEquals(interactionResponse, mockedInteractionResponse);
                return true;
            }
        });
    }

    @Test
    public void interactionRequest() throws IOException, InterruptedException {

        MockWebServer server = prepareMockedServer();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("");

        InteractionServices interactionServices = new NetworkManager(baseUrl.toString(), "apiKey", "loginId", "sharedSecret").createService(InteractionServices.class);
        DataSource webDataSource = new WebDataSourceImpl(interactionServices);

        webDataSource.sendInteraction("touchPoint", "interaction", "siteKey", "tid").test().assertNoErrors().assertValue(new Predicate<InteractionResponse>() {
            @Override
            public boolean test(@NonNull InteractionResponse interactionResponse) throws Exception {
                assertEquals(interactionResponse.getStatusCode(), 200);
                assertEquals(interactionResponse.getTid(), "a08b10a4-ead9-7667-9116-add167624b57");
                assertEquals(interactionResponse.getOptimizations().get(0).getDirectives(), "REPLACE");
                return true;
            }
        });

        RecordedRequest request = server.takeRequest();
        assertEquals("/one/oauth1/rt/api/2.0/interaction?sk=siteKey&tid=tid", request.getPath());
        assertEquals(request.getMethod(), "POST");
        assertNotNull(request.getHeader("Authorization"));
        assertTrue(request.getHeader("Authorization").contains("oauth_consumer_key=\"apiKey%21loginId"));
        assertTrue(request.getHeader("Authorization").contains("oauth_signature_method=\"HMAC-SHA1\""));
        assertTrue(request.getHeader("Authorization").contains("oauth_version=\"1.0\""));
        assertEquals(request.getHeader("Content-Type"), "application/json");
        assertEquals(request.getHeader("X-Requested-With"), "XMLHttpRequest");
        assertEquals(request.getHeader("Accept-Language"), "en-us");
        assertEquals(request.getHeader("Accept-Encoding"), "gzip, deflate");
        assertEquals(request.getHeader("Datamimetype"), "application/json");
        assertEquals(request.getBody().readUtf8(), "{\"uri\":\"touchPoint/interaction\"}");
    }

    @Test
    public void interactionRequest_noTid() throws IOException, InterruptedException {

        MockWebServer server = prepareMockedServer();

        // Ask the server for its URL. You'll need this to make HTTP requests.
        HttpUrl baseUrl = server.url("");

        InteractionServices interactionServices = new NetworkManager(baseUrl.toString(), "apiKey", "loginId", "sharedSecret").createService(InteractionServices.class);
        DataSource webDataSource = new WebDataSourceImpl(interactionServices);

        webDataSource.sendInteraction("touchPoint", "interaction", "siteKey", null).test().assertNoErrors().assertValue(new Predicate<InteractionResponse>() {
            @Override
            public boolean test(@NonNull InteractionResponse interactionResponse) throws Exception {
                assertEquals(interactionResponse.getStatusCode(), 200);
                assertEquals(interactionResponse.getTid(), "a08b10a4-ead9-7667-9116-add167624b57");
                assertEquals(interactionResponse.getOptimizations().get(0).getDirectives(), "REPLACE");
                return true;
            }
        });

        RecordedRequest request = server.takeRequest();
        assertEquals(request.getMethod(), "POST");
        assertEquals("/one/oauth1/rt/api/2.0/interaction?sk=siteKey", request.getPath());
    }

    private MockWebServer prepareMockedServer() throws IOException {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        MockWebServer server = new MockWebServer();

        String folder = System.getProperty("user.dir");

        if (!folder.endsWith("thunder-sdk")) {
            folder += "/thunder-sdk";
        }

        String content = new Scanner(new File(folder + "/src/test/resources/interaction_success.json")).useDelimiter("\\Z").next();

        // Schedule some responses.
        server.enqueue(new MockResponse().setBody(content).setResponseCode(200));

        // Start the server.
        server.start();

        return server;
    }
}

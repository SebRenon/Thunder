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

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
}

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

package com.srenon.thunder.sdk.data;

import com.srenon.thunder.sdk.data.impl.DataRepositoryImpl;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;
import com.srenon.thunder.sdk.domain.repository.DataRepository;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Seb on 01/10/2017.
 */

public class DataRepositoryImplTest {

    @Test
    public void constructor() {
        DataSource dataSource = mock(DataSource.class);
        DataRepository dataRepository = new DataRepositoryImpl(dataSource);
        assertNotNull(dataRepository);
    }

    @Test
    public void getOptimization_success() {
        DataSource dataSource = mock(DataSource.class);
        DataRepository dataRepository = new DataRepositoryImpl(dataSource);

        final InteractionResponse mockedInteractionResponse = mock(InteractionResponse.class);

        when(dataSource.sendInteraction(anyString(), anyString(), anyString(), anyString())).thenReturn(Observable.just(mockedInteractionResponse));

        Observable<InteractionResponse> result = dataRepository.sentInteraction("a", "b", "c", "d");

        verify(dataSource).sendInteraction("a", "b", "c", "d");
        verifyNoMoreInteractions(dataSource);

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
    public void getOptimization_fail() {
        DataSource dataSource = mock(DataSource.class);
        DataRepository dataRepository = new DataRepositoryImpl(dataSource);

        Observable<InteractionResponse> observable = Observable.error(new Exception("my exception"));

        when(dataSource.sendInteraction(anyString(), anyString(), anyString(), anyString())).thenReturn(observable);

        Observable<InteractionResponse> result = dataRepository.sentInteraction("a", "b", "c", "d");

        verify(dataSource).sendInteraction("a", "b", "c", "d");
        verifyNoMoreInteractions(dataSource);

        assertNotNull(result);

        result.test().assertError(new Predicate<Throwable>() {
            @Override
            public boolean test(@NonNull Throwable throwable) throws Exception {
                return "my exception".equals(throwable.getMessage());
            }
        });
    }
}

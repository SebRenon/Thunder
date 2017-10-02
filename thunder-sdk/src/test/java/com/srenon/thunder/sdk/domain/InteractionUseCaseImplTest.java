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

package com.srenon.thunder.sdk.domain;

import com.srenon.thunder.sdk.consumer.impl.ConsumerImpl;
import com.srenon.thunder.sdk.domain.client.Consumer;
import com.srenon.thunder.sdk.domain.interactor.InteractionUseCase;
import com.srenon.thunder.sdk.domain.interactor.impl.InteractionUseCaseImpl;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;
import com.srenon.thunder.sdk.domain.repository.DataRepository;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Seb on 02/10/2017.
 */

public class InteractionUseCaseImplTest {

    @Test
    public void constructor() {
        Consumer consumer = mock(Consumer.class);
        DataRepository repository = mock(DataRepository.class);
        Scheduler schedulerObserve = mock(Scheduler.class);
        Scheduler schedulerSub = mock(Scheduler.class);
        InteractionUseCase interactionUseCase = new InteractionUseCaseImpl(consumer, repository, schedulerObserve, schedulerSub);
        assertNotNull(interactionUseCase);
    }

    @Test
    public void sendInteraction() {

        final Consumer consumer = new ConsumerImpl();
        consumer.setTouchPoint("touchPoint");
        consumer.setSiteKey("siteKey");
        DataRepository repository = mock(DataRepository.class);
        InteractionUseCase interactionUseCase = new InteractionUseCaseImpl(consumer, repository, Schedulers.io(), Schedulers.io());

        final InteractionResponse mockedInteractionResponse = mock(InteractionResponse.class);
        when(mockedInteractionResponse.getTid()).thenReturn("myNewTid");

        when(repository.sentInteraction(anyString(), anyString(), anyString(), anyString())).thenReturn(Observable.just(mockedInteractionResponse));

        Observable<InteractionResponse> result = interactionUseCase.sendInteraction("myInteraction");

        verify(repository).sentInteraction("touchPoint", "myInteraction", "siteKey", null);
        verifyNoMoreInteractions(repository);

        result.test().awaitCount(1).assertNoErrors().assertValue(new Predicate<InteractionResponse>() {
            @Override
            public boolean test(@NonNull InteractionResponse interactionResponse) throws Exception {
                assertEquals(interactionResponse, mockedInteractionResponse);
                assertEquals(consumer.getTid(), "myNewTid");
                return true;
            }
        });
    }

    @Test
    public void sendInteraction_withTid() {

        final Consumer consumer = new ConsumerImpl();
        consumer.setTouchPoint("touchPoint");
        consumer.setSiteKey("siteKey");
        consumer.setTid("tid");
        DataRepository repository = mock(DataRepository.class);
        InteractionUseCase interactionUseCase = new InteractionUseCaseImpl(consumer, repository, Schedulers.io(), Schedulers.io());

        final InteractionResponse mockedInteractionResponse = mock(InteractionResponse.class);
        when(mockedInteractionResponse.getTid()).thenReturn("myNewTid");

        when(repository.sentInteraction(anyString(), anyString(), anyString(), anyString())).thenReturn(Observable.just(mockedInteractionResponse));

        Observable<InteractionResponse> result = interactionUseCase.sendInteraction("myInteraction");

        verify(repository).sentInteraction("touchPoint", "myInteraction", "siteKey", "tid");
        verifyNoMoreInteractions(repository);

        result.test().awaitCount(1).assertNoErrors().assertValue(new Predicate<InteractionResponse>() {
            @Override
            public boolean test(@NonNull InteractionResponse interactionResponse) throws Exception {
                assertEquals(interactionResponse, mockedInteractionResponse);
                assertEquals(consumer.getTid(), "tid");
                return true;
            }
        });
    }

    @Test
    public void sendInteraction_error() {

        final Consumer consumer = new ConsumerImpl();
        consumer.setTouchPoint("touchPoint");
        consumer.setSiteKey("siteKey");
        consumer.setTid("tid");
        DataRepository repository = mock(DataRepository.class);
        InteractionUseCase interactionUseCase = new InteractionUseCaseImpl(consumer, repository, Schedulers.computation(), Schedulers.computation());

        Observable<InteractionResponse> observable = Observable.error(new Exception("my exception"));

        when(repository.sentInteraction(anyString(), anyString(), anyString(), anyString())).thenReturn(observable);

        Observable<InteractionResponse> result = interactionUseCase.sendInteraction("myInteraction");

        verify(repository).sentInteraction("touchPoint", "myInteraction", "siteKey", "tid");
        verifyNoMoreInteractions(repository);

        result.test().awaitCount(1).assertError(new Predicate<Throwable>() {
            @Override
            public boolean test(@NonNull Throwable throwable) throws Exception {
                assertEquals(throwable.getMessage(), "my exception");
                return true;
            }
        });
    }
}

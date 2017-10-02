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

package com.srenon.thunder.sdk;

import com.srenon.thunder.sdk.callback.InteractionCallback;
import com.srenon.thunder.sdk.di.ThunderComponent;
import com.srenon.thunder.sdk.di.ThunderRegistryTest;
import com.srenon.thunder.sdk.domain.interactor.InteractionUseCase;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;

import io.reactivex.Observable;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ThunderSdkTest {

    @After
    public void tearDown() {
        ThunderSdk.destroy();
    }

    @Test
    public void initialize() {
        // Not initialized yet
        assertNull(ThunderSdk.getInstance());

        ThunderSdk.init("", "", "", "", "");
        assertNull(ThunderSdk.getInstance());

        ThunderSdk.init("", "a", "a", "a", "a");
        assertNull(ThunderSdk.getInstance());

        ThunderSdk.init("a", "", "a", "a", "a");
        assertNull(ThunderSdk.getInstance());

        ThunderSdk.init("a", "a", "", "a", "a");
        assertNull(ThunderSdk.getInstance());

        ThunderSdk.init("a", "a", "a", "", "a");
        assertNull(ThunderSdk.getInstance());

        ThunderSdk.init("a", "a", "a", "a", "");
        assertNull(ThunderSdk.getInstance());

        ThunderSdk.init("a", "a", "a", "a", "a");
        assertNotNull(ThunderSdk.getInstance());
    }

    @Test
    public void sendInteraction() {

        ThunderSdk.init("a", "a", "a", "a", "a");

        ThunderComponent component = mock(ThunderComponent.class);
        InteractionUseCase interactionUseCase = mock(InteractionUseCase.class);
        InteractionResponse interactionResponse = new InteractionResponse();
        Observable<InteractionResponse> observable = Observable.just(interactionResponse);
        InteractionCallback interactionCallback = mock(InteractionCallback.class);

        when(interactionUseCase.sendInteraction(anyString())).thenReturn(observable);
        when(component.provideInteractionUseCase()).thenReturn(interactionUseCase);

        ThunderRegistryTest.overrideRegistry(component);
        ThunderSdk.getInstance().sendInteraction(ThunderSdkActions.INTERACTION_LOGIN, interactionCallback);

        verify(interactionUseCase).sendInteraction("loginView");
        verifyNoMoreInteractions(interactionUseCase);

        verify(interactionCallback).onSuccess(interactionResponse);
        verifyNoMoreInteractions(interactionCallback);
    }

    @Test
    public void sendInteraction_error() {

        ThunderSdk.init("a", "a", "a", "a", "a");

        ThunderComponent component = mock(ThunderComponent.class);
        InteractionUseCase interactionUseCase = mock(InteractionUseCase.class);
        InteractionResponse interactionResponse = new InteractionResponse();
        Observable<InteractionResponse> observable = Observable.error(new IOException("Error I/O"));
        InteractionCallback interactionCallback = mock(InteractionCallback.class);

        when(interactionUseCase.sendInteraction(anyString())).thenReturn(observable);
        when(component.provideInteractionUseCase()).thenReturn(interactionUseCase);

        ThunderRegistryTest.overrideRegistry(component);
        ThunderSdk.getInstance().sendInteraction(ThunderSdkActions.INTERACTION_HOME, interactionCallback);

        verify(interactionUseCase).sendInteraction("homeView");
        verifyNoMoreInteractions(interactionUseCase);

        verify(interactionCallback).onError("Error I/O");
        verifyNoMoreInteractions(interactionCallback);
    }
}
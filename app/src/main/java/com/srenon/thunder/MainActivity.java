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

package com.srenon.thunder;

import com.srenon.thunder.sdk.ThunderSdk;
import com.srenon.thunder.sdk.callback.InteractionCallback;
import com.srenon.thunder.sdk.domain.model.InteractionResponse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_main_login)
    Button mBtnLogin;

    @BindView(R.id.btn_main_home)
    Button mBtnHome;

    @BindView(R.id.prog_main)
    ProgressBar mProgressBar;

    @OnClick(R.id.btn_main_home)
    public void submitHome(View view) {
        triggerSDK(true);
    }

    @OnClick(R.id.btn_main_login)
    public void submitLogin(View view) {
        triggerSDK(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Init the SDK - you can init from anywhere
        ThunderSdk.init(
                "YOUR_SITE_KEY",
                "YOUR_API_KEY",
                "YOUR_LOGIN_ID",
                "YOUR_TOUCHPOINT",
                "YOUR_SHARED_SECRET");
    }

    private void triggerSDK(boolean isHomeInteraction) {
        showProgress();

        String interaction = isHomeInteraction ? ThunderSdk.INTERACTION_HOME : ThunderSdk.INTERACTION_LOGIN;

        // This how to send an interaction to ONE
        ThunderSdk.getInstance().sendInteraction(interaction, new InteractionCallback() {
            @Override
            public void onSuccess(InteractionResponse response) {
                hideProgress();
                startActivity(DetailsActivity.getStartIntent(MainActivity.this, response));
            }

            @Override
            public void onError(String error) {
                hideProgress();
                MainActivity.this.handleError(error);
            }
        });
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void handleError(String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
    }
}

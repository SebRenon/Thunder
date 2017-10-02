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

import com.srenon.thunder.sdk.domain.model.InteractionResponse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Seb on 01/10/2017.
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String INTENT_KEY_STATUS = "KEY_STATUS";

    private static final String INTENT_KEY_TID = "KEY_TID";

    private static final String INTENT_KEY_DIRECTIVES = "KEY_DIRECTIVES";

    @BindView(R.id.txtv_status)
    TextView mTxtStatus;

    @BindView(R.id.txtv_tid)
    TextView mTxtTid;

    @BindView(R.id.txtv_directives)
    TextView mTxtDirectives;

    public static Intent getStartIntent(@NonNull Context context, @NonNull InteractionResponse interactionResponse) {
        Intent intent = new Intent(context, DetailsActivity.class);

        StringBuilder directives = new StringBuilder();
        for (InteractionResponse.Optimizations optimization : interactionResponse.getOptimizations()) {
            directives.append(optimization.getDirectives()).append(",");
        }
        int length = directives.length();
        if (length > 0) {
            intent.putExtra(INTENT_KEY_DIRECTIVES, directives.toString().substring(0, length - 1));
        }

        intent.putExtra(INTENT_KEY_STATUS, String.valueOf(interactionResponse.getStatusCode()));
        intent.putExtra(INTENT_KEY_TID, interactionResponse.getTid());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mTxtStatus.setText(getIntent().getStringExtra(INTENT_KEY_STATUS));
        mTxtTid.setText(getIntent().getStringExtra(INTENT_KEY_TID));
        if (getIntent().hasExtra(INTENT_KEY_DIRECTIVES)) {
            mTxtDirectives.setText(getIntent().getStringExtra(INTENT_KEY_DIRECTIVES));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            DetailsActivity.this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}

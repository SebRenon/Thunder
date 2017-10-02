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

package com.srenon.thunder.sdk.domain.model;

import java.util.List;

/**
 * Created by Seb on 01/10/2017.
 */

public class InteractionResponse {

    private int statusCode;

    private String tid;

    private String session;

    private List<Object> trackers;

    private List<Object> captures;

    private List<Optimizations> optimizations;

    public int getStatusCode() {
        return statusCode;
    }

    public String getTid() {
        return tid;
    }

    public String getSession() {
        return session;
    }

    public List<Object> getTrackers() {
        return trackers;
    }

    public List<Object> getCaptures() {
        return captures;
    }

    public List<Optimizations> getOptimizations() {
        return optimizations;
    }

    public static class Optimizations {

        private String data;

        private String path;

        private String dataMimeType;

        private String directives;

        public String getData() {
            return data;
        }

        public String getPath() {
            return path;
        }

        public String getDataMimeType() {
            return dataMimeType;
        }

        public String getDirectives() {
            return directives;
        }
    }
}

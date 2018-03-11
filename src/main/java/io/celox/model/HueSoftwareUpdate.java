/*
 * Copyright (c) 2018 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.celox.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class HueSoftwareUpdate {

    @SuppressWarnings("unused")
    private static final String TAG = "HueSoftwareUpdate";

    private String lastInstall;
    private String state;

    HueSoftwareUpdate(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.lastInstall = jsonObject.getString("lastinstall");
            this.state = jsonObject.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HueSoftwareUpdate(String lastInstall, String state) {
        this.lastInstall = lastInstall;
        this.state = state;
    }

    public String getLastInstall() {
        return lastInstall;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "HueSoftwareUpdate{" +
                "lastInstall='" + lastInstall + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

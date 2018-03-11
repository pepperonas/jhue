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
public class HueLamp {

    @SuppressWarnings("unused")
    private static final String TAG = "HueLamp";

    private String key;

    private String manufacturerName;
    private String modelId;
    private String name;
    private String productId;
    private HueLampState hueLampState;
    private String swConfigId;
    private HueSoftwareUpdate hueSoftwareUpdate;
    private String uniqueId;
    private String swVersion;
    private String type;

    public HueLamp(String key, String json) {
        this.key = key;
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.manufacturerName = jsonObject.getString("manufacturername");
            this.modelId = jsonObject.getString("modelid");
            this.name = jsonObject.getString("name");
            this.productId = jsonObject.getString("productid");
            this.hueLampState = new HueLampState(jsonObject.getString("state"));
            this.swConfigId = jsonObject.getString("swconfigid");
            this.hueSoftwareUpdate = new HueSoftwareUpdate(jsonObject.getString("swupdate"));
            this.uniqueId = jsonObject.getString("uniqueid");
            this.swVersion = jsonObject.getString("swversion");
            this.type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getKey() {
        return key;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getModelId() {
        return modelId;
    }

    public String getName() {
        return name;
    }

    public String getProductId() {
        return productId;
    }

    public HueLampState getHueLampState() {
        return hueLampState;
    }

    public String getSwConfigId() {
        return swConfigId;
    }

    public HueSoftwareUpdate getHueSoftwareUpdate() {
        return hueSoftwareUpdate;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "HueLamp{" +
                "key='" + manufacturerName + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", modelId='" + modelId + '\'' +
                ", name='" + name + '\'' +
                ", productId='" + productId + '\'' +
                ", hueLampState=" + hueLampState +
                ", swConfigId='" + swConfigId + '\'' +
                ", hueSoftwareUpdate=" + hueSoftwareUpdate +
                ", uniqueId='" + uniqueId + '\'' +
                ", swVersion='" + swVersion + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

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
public class HueLampState {

    @SuppressWarnings("unused")
    private static final String TAG = "HueLampState";

    private String alert;
    private int brightness;
    private String colorMode;
    private int ct;
    private String effect;
    private int hue;
    private String mode;
    private boolean on;
    private boolean reachable;
    private int saturation;
    private HueXyColor hueXyColor;

    HueLampState(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.alert = jsonObject.getString("alert");
            this.brightness = jsonObject.getInt("bri");
            this.colorMode = jsonObject.getString("colormode");
            this.ct = jsonObject.getInt("ct");
            this.effect = jsonObject.getString("effect");
            this.hue = jsonObject.getInt("hue");
            this.mode = jsonObject.getString("mode");
            this.on = jsonObject.getBoolean("on");
            this.reachable = jsonObject.getBoolean("reachable");
            this.saturation = jsonObject.getInt("sat");
            this.hueXyColor = new HueXyColor(jsonObject.getString("xy"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAlert() {
        return alert;
    }

    public int getBrightness() {
        return brightness;
    }

    public String getColorMode() {
        return colorMode;
    }

    public int getCt() {
        return ct;
    }

    public String getEffect() {
        return effect;
    }

    public int getHue() {
        return hue;
    }

    public String getMode() {
        return mode;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isReachable() {
        return reachable;
    }

    public int getSaturation() {
        return saturation;
    }

    public HueXyColor getHueXyColor() {
        return hueXyColor;
    }

    @Override
    public String toString() {
        return "HueLampState{" +
                "alert='" + alert + '\'' +
                ", brightness=" + brightness +
                ", colorMode='" + colorMode + '\'' +
                ", ct=" + ct +
                ", effect='" + effect + '\'' +
                ", hue='" + hue + '\'' +
                ", mode='" + mode + '\'' +
                ", on=" + on +
                ", reachable=" + reachable +
                ", saturation=" + saturation +
                ", hueXyColor=" + hueXyColor +
                '}';
    }
}

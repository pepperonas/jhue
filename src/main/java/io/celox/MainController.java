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

package io.celox;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import com.pepperonas.jbasx.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.celox.dialog.DialogAbout;
import io.celox.dialog.DialogInfo;
import io.celox.model.HueLamp;
import io.celox.utils.HttpUtils;
import io.celox.utils.HueUtils;
import io.celox.utils.Setup;
import io.celox.utils.Utils;
import io.celox.utils.UtilsGui;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * @author Martin Pfeffer
 * <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 * @see <a href="https://celox.io">https://celox.io</a>
 */
public class MainController {

    @SuppressWarnings("unused")
    private static final String TAG = "MainController";

    @FXML
    public VBox vbox_root;
    @FXML
    public Label label_info_result_count, label_info_1, label_info_2;
    @FXML
    public GridPane grid_actions_higher, grid_actions_lower;
    @FXML
    public JFXButton btn_register;
    @FXML
    public JFXToggleButton tgl_btn_on_off;
    @FXML
    public JFXColorPicker color_picker;
    @FXML
    public JFXSlider slider_saturation, slider_brightness;
    @FXML
    public JFXComboBox<HueLamp> cb_lamp_selection;

    private Application mApp;

    private Color mColor = Color.WHITESMOKE;

    private HashMap<String, HueLamp> mHueLampMap = new HashMap<>();
    private boolean mReadOnly = false;

    @FXML
    public void initialize() {
        Utils.initLogger("huej");

        vbox_root.getStylesheets().add("/styles/styles.css");
        initGui();

        Platform.runLater(() -> {
            UtilsGui.closeOnEsc(vbox_root);
        });
    }

    private void initGui() {

        slider_saturation.valueProperty().addListener((observable, oldValue, newValue) -> {
            Log.i(TAG, "changed: sat=" + newValue);
            applyColor();
        });

        slider_brightness.valueProperty().addListener((observable, oldValue, newValue) -> {
            Log.i(TAG, "changed: bri=" + newValue);
            applyColor();
        });

        cb_lamp_selection.setConverter(new StringConverter<HueLamp>() {
            @Override
            public String toString(HueLamp object) {
                return object.getName();
            }

            @Override
            public HueLamp fromString(String string) {
                return null;
            }
        });

        if (Setup.hasValidUsername()) {
            Log.i(TAG, "initGui: valid user found, will load lamp config...");

            btn_register.setDisable(true);

            loadLampMap();
            //            for (Map.Entry<String, HueLamp> entry : mHueLampMap.entrySet()) {
            //                String key = entry.getKey();
            //                Object value = entry.getValue();
            //                System.out.println("key=" + key + " -> " + value.toString());
            //            }
            String lastSelected = Setup.getLastSelectedLamp();
            HueLamp lastSelectedLamp = mHueLampMap.get(lastSelected);
            cb_lamp_selection.getSelectionModel().select(lastSelectedLamp);
        }
    }

    public void onMenuExit(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onMenuExit: ");

        Stage sb = (Stage) vbox_root.getScene().getWindow();
        sb.close();
        System.out.println("Bye!");
        Platform.exit();
        System.exit(0);
    }

    public void onMenuPreferences(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.v(TAG, "onMenuPreferences: ");
    }

    public void onMenuShowLog(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onMenuShowLog: ");
    }

    public void onMenuInstructions(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onMenuInstructions: ");

        mApp.getHostServices().showDocument("manual.html");
        //        mApp.getHostServices().showDocument("https://google.de");
    }

    public void onMenuAbout(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onMenuAbout: ");
        new DialogAbout(mApp);
    }

    void setApp(Application app) {
        this.mApp = app;
    }

    public void onBtnRegister(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.v(TAG, "onBtnRegister: ");

        String result = HttpUtils.executePost("http://192.168.178.134/api",
                "{\"devicetype\":\"my_hue_app#huej martin\"}");

        if (result != null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if (jsonObject.has("error")) {
                    String response = jsonObject.getString("error");
                    Log.w(TAG, "onBtnRegister: FAILED! response=" + response);
                } else {
                    String response = jsonObject.getString("success");
                    JSONObject successObject = jsonObject.getJSONObject("success");
                    String username = successObject.getString("username");
                    Log.i(TAG, "onBtnRegister: SUCCESS! response=" + response + "\nwill store username " + username);
                    Setup.setUsername(username);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onBtnReset(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.v(TAG, "onBtnReset: will reset username...");
        Setup.setUsername("");
    }

    public void onBtnPutColor(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onBtnPut: ");
        HueLamp lamp = cb_lamp_selection.getSelectionModel().getSelectedItem();

        String saturation = String.valueOf((int) (slider_saturation.getValue() * 2.54f));
        String brightness = String.valueOf((int) (slider_brightness.getValue() * 2.54f));

        String result = HttpUtils.executePut("http://192.168.178.134/api/" + Setup.getUsername() + "/lights/" + lamp.getKey() + "/state",
                "{\"on\":true, " +
                        "\"sat\":" + saturation + ", " +
                        "\"bri\":" + brightness + ", " +
                        "\"hue\":" + mColor + "}");
        Log.i(TAG, "onBtnPut: " + result);
    }

    public void onBtnPutColor2(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.v(TAG, "onBtnPutColor2: ");
        applyColor();
    }

    private void applyColor() {
        HueLamp lamp = cb_lamp_selection.getSelectionModel().getSelectedItem();

        if (mReadOnly) {
            return;
        }
        String saturation = String.valueOf((int) (slider_saturation.getValue() * 2.54f));
        String brightness = String.valueOf((int) (slider_brightness.getValue() * 2.54f));
        Log.v(TAG, "applyColor: sat=" + saturation);
        Log.v(TAG, "applyColor: bri=" + brightness);

        HashMap<String, Double> colorMap = HueUtils.rgbToXy(mColor);
        String xyColor = HueUtils.formatXyToString(colorMap);

        String result = HttpUtils.executePut(
                "http://192.168.178.134/api/" + Setup.getUsername() + "/lights/" + lamp.getKey() + "/state",
                "{\"on\":true," +
                        "\"sat\":" + saturation + "," +
                        "\"bri\":" + brightness + "," +
                        "\"xy\":" + xyColor + "}");
        Log.i(TAG, "onBtnPut: " + result);
    }

    public void onBtnInfo(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.v(TAG, "onBtnInfo: ");
        String result = HttpUtils.executeGet("http://192.168.178.134/api/" + Setup.getUsername() + "/lights");
        if (result != null && !result.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                new DialogInfo("Info", jsonObject.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLampMap() {
        Log.v(TAG, "loadLampMap: ");

        String result = HttpUtils.executeGet("http://192.168.178.134/api/" + Setup.getUsername() + "/lights");
        if (result != null && !result.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(result);

                Log.i(TAG, "onBtnInfo: found " + jsonObject.length() + " lights.");

                Iterator<?> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (jsonObject.get(key) instanceof JSONObject) {
                        JSONObject jsonObj = (JSONObject) jsonObject.get(key);
                        Log.i(TAG, "onBtnInfo: key=" + key + "\n" + jsonObj.toString(2));
                        HueLamp hueLamp = new HueLamp(key, jsonObj.toString());

                        mHueLampMap.put(key, hueLamp);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, HueLamp> entry : mHueLampMap.entrySet()) {
            String key = entry.getKey();
            HueLamp value = entry.getValue();
            System.out.println("key=" + key + " -> " + value.toString());

            cb_lamp_selection.getItems().add(value);
        }
    }

    private HueLamp getInfoLamp(String key) {
        Log.i(TAG, "getInfoLamp: ");
        String result = HttpUtils.executeGet("http://192.168.178.134/api/" + Setup.getUsername() + "/lights/" + key);
        if (result != null && !result.isEmpty()) {
            return new HueLamp(key, result);
        }
        return null;
    }

    public void onTglBtnOnOff(@SuppressWarnings("unused") ActionEvent actionEvent) {
        String state = tgl_btn_on_off.isSelected() ? "true" : "false";
        HueLamp lamp = cb_lamp_selection.getSelectionModel().getSelectedItem();
        Log.i(TAG, "onBtnPut: " + state);
        String result = HttpUtils.executePut("http://192.168.178.134/api/" + Setup.getUsername() + "/lights/" + lamp.getKey() + "/state",
                "{\"on\":" + state + "}");

        Log.i(TAG, "onBtnPut: " + result);
    }

    public void onColorSelected(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Paint paint = color_picker.getValue();
        mColor = Color.web(paint.toString(), 1.0);
        Log.i(TAG, "onColorSelected: " + mColor);
        applyColor();
    }

    public void onCbLampSelection(@SuppressWarnings("unused") ActionEvent actionEvent) {
        mReadOnly = true;
        HueLamp selectedLamp = cb_lamp_selection.getSelectionModel().getSelectedItem();

        Setup.setLastSelectedLamp(selectedLamp.getKey());

        HueLamp hueLamp = getInfoLamp(selectedLamp.getKey());
        if (hueLamp != null) {
            Log.i(TAG, "onCbLampSelection: " + hueLamp.toString());
            slider_brightness.setValue(hueLamp.getHueLampState().getBrightness() / 2.54f);
            slider_saturation.setValue(hueLamp.getHueLampState().getSaturation() / 2.54f);
            Log.w(TAG, "onCbLampSelection: on=" + hueLamp.getHueLampState().isOn());
            tgl_btn_on_off.setSelected(hueLamp.getHueLampState().isOn());
        } else {
            Log.w(TAG, "onCbLampSelection: lamp not found.");
        }
        mReadOnly = false;
    }

    public void onClose() {
        Log.i(TAG, "onClose: ");
    }
}

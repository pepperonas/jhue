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

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.pepperonas.jbasx.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.HashMap;

import io.celox.dialog.DialogAbout;
import io.celox.dialog.DialogInfo;
import io.celox.utils.HttpUtils;
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
    public JFXToggleButton tgl_btn_on_off;
    @FXML
    public JFXTextField tf_lamp;
    @FXML
    public JFXColorPicker color_picker;
    @FXML
    public JFXSlider slider_saturation, slider_brightness;

    private Connection mConnection;

    private Application mApp;
    private Color mColor = Color.WHITESMOKE;

    @FXML
    public void initialize() {
        Utils.initLogger("huej");

        vbox_root.getStylesheets().add("/styles/styles.css");
        initGui();

        //        HttpUtils.executePost("http://192.168.178.134/api/newdeveloper", "");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                UtilsGui.closeOnEsc(vbox_root);
            }
        });
    }

    private void initGui() {
        rgbToXy(Color.GREEN);
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
        Log.i(TAG, "onMenuPreferences: ");
    }

    public void onMenuShowLog(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onMenuShowLog: ");
    }

    public void onMenuInstructions(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onMenuInstructions: ");

        mApp.getHostServices().showDocument("manual.html");
    }

    public void onMenuAbout(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onMenuAbout: ");
        new DialogAbout(mApp);
    }

    void setApp(Application app) {
        this.mApp = app;
    }

    public void onBtnRegister(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onBtnRegister: ");

        String result = HttpUtils.executePost("http://192.168.178.134/api", "{\"devicetype\":\"my_hue_app#huej martin\"}");
        if (result != null) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if (jsonObject.has("error")) {
                    String response = jsonObject.getString("error");
                    Log.w(TAG, "onBtnRegister: FAILED! response=" + response);
                } else {
                    String response = jsonObject.getString("success");
                    Log.i(TAG, "onBtnRegister: SUCCESS! response=" + response);
                    JSONObject successObject = jsonObject.getJSONObject("success");
                    String username = successObject.getString("username");
                    Log.i(TAG, "onBtnRegister: " + username);
                    Setup.setUsername(username);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onBtnReset(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onBtnReset: will reset username...");
        Setup.setUsername("");
    }

    public void onBtnPutColor(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onBtnPut: ");
        if (tf_lamp.getText().isEmpty()) {
            tf_lamp.setText("1");
        }
        String lamp = tf_lamp.getText();

        String saturation = String.valueOf((int) (slider_saturation.getValue() * 2.54f));
        String brightness = String.valueOf((int) (slider_brightness.getValue() * 2.54f));
        Log.i(TAG, "applyColor: sat=" + saturation);
        Log.i(TAG, "applyColor: bri=" + brightness);

        String result = HttpUtils.executePut("http://192.168.178.134/api/" + Setup.getUsername() + "/lights/" + lamp + "/state", "{\"on\":true, " +
                "\"sat\":" + saturation + ", " +
                "\"bri\":" + brightness + ", " +
                "\"hue\":" + mColor + "}");
        Log.i(TAG, "onBtnPut: " + result);
    }

    public void onBtnPutColor2(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onBtnPutColor2: ");
        applyColor();
    }

    private void applyColor() {
        if (tf_lamp.getText().isEmpty()) {
            tf_lamp.setText("1");
        }
        String lamp = tf_lamp.getText();

        String saturation = String.valueOf((int) (slider_saturation.getValue() * 2.54f));
        String brightness = String.valueOf((int) (slider_brightness.getValue() * 2.54f));
        Log.i(TAG, "applyColor: sat=" + saturation);
        Log.i(TAG, "applyColor: bri=" + brightness);

        HashMap<String, Double> c = rgbToXy(mColor);
        String xyColor = "[" + c.get("x") + "," + c.get("y") + "]";

        String result = HttpUtils.executePut(
                "http://192.168.178.134/api/" + Setup.getUsername() + "/lights/" + lamp + "/state",
                "{\"on\":true," +
                        "\"sat\":" + saturation + "," +
                        "\"bri\":" + brightness + "," +
                        "\"xy\":" + xyColor + "}");
        Log.i(TAG, "onBtnPut: " + result);
    }

    public void onBtnInfo(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onBtnInfo: ");
        String result = HttpUtils.executeGet("http://192.168.178.134/api/" + Setup.getUsername() + "/lights");
        if (!result.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                new DialogInfo("Info", jsonObject.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onTglBtnOnOff(@SuppressWarnings("unused") ActionEvent actionEvent) {
        String state = tgl_btn_on_off.isSelected() ? "true" : "false";
        if (tf_lamp.getText().isEmpty()) {
            tf_lamp.setText("1");
        }
        String lamp = tf_lamp.getText();
        Log.i(TAG, "onBtnPut: " + state);
        String result = HttpUtils.executePut("http://192.168.178.134/api/" + Setup.getUsername() + "/lights/" + lamp + "/state", "{\"on\":" + state + "}");
        Log.i(TAG, "onBtnPut: " + result);
    }

    public static HashMap<String, Double> rgbToXy(Color c) {
        // for the hue bulb the corners of the triangle are:
        // -Red: 0.675, 0.322
        // -Green: 0.4091, 0.518
        // -Blue: 0.167, 0.04
        double[] normalizedToOne = new double[3];
        double cred, cgreen, cblue;
        cred = c.getRed();
        cgreen = c.getGreen();
        cblue = c.getBlue();
        normalizedToOne[0] = (cred / 255);
        normalizedToOne[1] = (cgreen / 255);
        normalizedToOne[2] = (cblue / 255);
        float red, green, blue;

        // Make red more vivid
        if (normalizedToOne[0] > 0.04045) {
            red = (float) Math.pow((normalizedToOne[0] + 0.055) / (1.0 + 0.055), 2.4);
        } else {
            red = (float) (normalizedToOne[0] / 12.92);
        }

        // Make green more vivid
        if (normalizedToOne[1] > 0.04045) {
            green = (float) Math.pow((normalizedToOne[1] + 0.055) / (1.0 + 0.055), 2.4);
        } else {
            green = (float) (normalizedToOne[1] / 12.92);
        }

        // Make blue more vivid
        if (normalizedToOne[2] > 0.04045) {
            blue = (float) Math.pow((normalizedToOne[2] + 0.055) / (1.0 + 0.055), 2.4);
        } else {
            blue = (float) (normalizedToOne[2] / 12.92);
        }

        float _x = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
        float _y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
        float _z = (float) (red * 0.0000000 + green * 0.053077 + blue * 1.035763);

        float x = _x / (_x + _y + _z);
        float y = _y / (_x + _y + _z);

        double[] xy = new double[2];
        xy[0] = x;
        xy[1] = y;

        HashMap<String, Double> map = new HashMap<>();
        map.put("x", xy[0]);
        map.put("y", xy[1]);
        return map;
    }

    public void onColorSelected(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Paint paint = color_picker.getValue();
        mColor = Color.web(paint.toString(), 1.0);
        Log.i(TAG, "onColorSelected: " + mColor);
        applyColor();
    }
}

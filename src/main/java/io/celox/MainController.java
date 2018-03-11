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

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.pepperonas.jbasx.log.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;

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
    public JFXTextField tf_hue, tf_saturation, tf_brightness;

    private Connection mConnection;

    private Application mApp;

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
        if (tf_hue.getText().isEmpty()) {
            tf_hue.setText("10000");
        }
        String color = tf_hue.getText();

        if (tf_brightness.getText().isEmpty()) {
            tf_brightness.setText("254");
        }
        String brightness = tf_brightness.getText();

        if (tf_saturation.getText().isEmpty()) {
            tf_saturation.setText("254");
        }
        String saturation = tf_saturation.getText();

        String result = HttpUtils.executePut("http://192.168.178.134/api/" + Setup.getUsername() + "/lights/" + lamp + "/state", "{\"on\":true, " +
                "\"sat\":" + saturation + ", " +
                "\"bri\":" + brightness + ", " +
                "\"hue\":" + color + "}");
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
}

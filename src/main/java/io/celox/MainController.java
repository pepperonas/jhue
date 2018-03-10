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

import com.pepperonas.jbasx.log.Log;

import java.sql.Connection;

import io.celox.dialog.DialogAbout;
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

    private Connection mConnection;

    private Application mApp;

    @FXML
    public void initialize() {
        vbox_root.getStylesheets().add("/styles/styles.css");
        initGui();
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

    public void onMenuInfo(@SuppressWarnings("unused") ActionEvent actionEvent) {
        Log.i(TAG, "onMenuInfo: ");
        new DialogAbout(mApp);
    }

    void setApp(Application app) {
        this.mApp = app;
    }

}

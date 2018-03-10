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

package io.celox.utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.pepperonas.jbasx.log.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Martin Pfeffer <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 */
public class UtilsGui {

    @SuppressWarnings("unused")
    private static final String TAG = "UtilsGui";

    public static void runAnotherApp(Class<? extends Application> appClass) {
        Platform.runLater(() -> {
            try {
                Application application;
                Stage anotherStage = new Stage();
                application = appClass.newInstance();
                application.start(anotherStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void closeOnEsc(Parent root) {
        root.getScene().addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                System.out.println("-ESC- pressed, closing stage...");
                Stage sb = (Stage) root.getScene().getWindow();
                sb.close();
            }
        });
    }

    @SuppressWarnings("unused")
    public static void closeOnEscAndExit(Parent root) {
        root.getScene().addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                System.out.println("-ESC- pressed, closing stage and logging out...");
                Stage sb = (Stage) root.getScene().getWindow();
                sb.close();
                System.out.println("Bye!");
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void makeTextFieldNumeric(JFXTextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static ChangeListener<String> makeTextFieldDecimal(JFXTextField textField) {
        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        };
        textField.textProperty().addListener(changeListener);
        return changeListener;
    }

    public static String makeReadableTimeStamp(String string) {
        if (string != null) {
            if (string.length() >= 13) {
                string = string.substring(0, 13);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            return sdf.format(new Date(Long.parseLong(string)));
        }
        return null;
    }

    public static void displayReadableTimeStampFailSafe(JFXComboBox<String> comboBox) {
        comboBox.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    if (item.length() >= 13) {
                        item = item.substring(0, 13);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    setText(sdf.format(new Date(Long.parseLong(item))));
                }
            }
        });
    }

    public static String formatTimeStampFailSafe(String timestamp) {
        if (timestamp.length() >= 13) {
            timestamp = timestamp.substring(0, 13);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(new Date(Long.parseLong(timestamp)));
    }

    public static void displayReadableTimeStampFailSafe(JFXCheckBox checkBox, String timestamp) {
        checkBox.setId(timestamp);
        if (timestamp.length() >= 13) {
            timestamp = timestamp.substring(0, 13);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        checkBox.setText(sdf.format(new Date(Long.parseLong(timestamp))));
    }

    public static void displayReadableTimeStampFailSafe(JFXButton button, String timestamp) {
        button.setId(timestamp);
        if (timestamp.length() >= 13) {
            timestamp = timestamp.substring(0, 13);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        button.setText(sdf.format(new Date(Long.parseLong(timestamp))));
    }

    public static void makeFullScreen(Stage primaryStage, boolean onSecondaryMonitor) {
        Screen screen;
        List<Screen> allScreens = Screen.getScreens();
        if (allScreens.size() > 1 && onSecondaryMonitor) {
            screen = allScreens.get(1);
        } else {
            screen = allScreens.get(0);
        }
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
    }

    public static void sizeStageToScene(Stage stage) {
        try {
            Platform.runLater(() -> {
                stage.sizeToScene();
                stage.setWidth(stage.getWidth() + 1);
            });
        } catch (Exception e) {
            Log.e(TAG, "sizeStageToScene: ", e);
        }
    }

    //    public class TheConverter extends StringConverter<> {
    //
    //        public myClass fromString(String string) {
    //            // convert from a string to a myClass instance
    //        }
    //
    //        public String toString(myClass myClassinstance) {
    //            // convert a myClass instance to the text displayed in the choice box
    //        }
    //    }
}

package io.celox.dialog;

import io.celox.utils.UtilsGui;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Martin Pfeffer <a href="mailto:martin.pfeffer@celox.io">martin.pfeffer@celox.io</a>
 */
public class DialogInfo {

    @SuppressWarnings("unused")
    private static final String TAG = "DialogInfo";

    @SuppressWarnings("UnusedAssignment")
    public DialogInfo(String title, String content) {
        Stage dialogStage = new Stage(StageStyle.UTILITY);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setWidth(400);
        dialogStage.setHeight(600);
        dialogStage.setResizable(false);

        BorderPane borderPane = new BorderPane();

        borderPane.setPadding(new Insets(15));
        borderPane.setPrefWidth(Integer.MAX_VALUE);
        borderPane.setPrefHeight(Integer.MAX_VALUE);

        Scene scene = new Scene(borderPane);
        dialogStage.setScene(scene);
        dialogStage.show();
        UtilsGui.closeOnEsc(borderPane);

        // Top
        Label textTitle = new Label(title);
        textTitle.setStyle("-fx-font-size: 18px;");

        HBox hBoxTop = new HBox(0);
        hBoxTop.getChildren().addAll(textTitle);
        borderPane.setTop(hBoxTop);

        VBox vBoxCenter = new VBox(10);
        vBoxCenter.setPrefHeight(500);
        vBoxCenter.setPadding(new Insets(15, 0, 15, 0));

        TextArea textArea = new TextArea(content);
        textArea.setPrefHeight(500);
        vBoxCenter.getChildren().addAll(textArea);
        borderPane.setCenter(vBoxCenter);

        // Bottom
        HBox hBoxBottom = new HBox();
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBoxBottom.getChildren().addAll(spacer);
        borderPane.setBottom(hBoxBottom);
    }

}
package main.UI.menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import main.Configuration;

public class OptionsMenu extends BorderPane {

    public OptionsMenu(GraphicalMenus graphicalMenus) {
        super();

        ImageView backgroundBlured = new ImageView(new Image("images/blured.jpg"));

        backgroundBlured.setOpacity(1);

        backgroundBlured.fitWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        backgroundBlured.fitHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        this.getChildren().add(backgroundBlured);

        this.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        this.prefHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        StackPane titlePane = new StackPane();
        Rectangle backgroundForTitle = new Rectangle(0, 0, 600, 50);
        backgroundForTitle.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        backgroundForTitle.setOpacity(0.3);

        Label title = new Label("Options");
        title.setFont(new Font(30));

        Button back = new Button("Retour");
        back.setPrefHeight(50);
        back.setOnMouseClicked((e) -> {
            graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getHomeScreen());
        });

        HBox titleBox = new HBox(back, title);
        title.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().subtract(back.widthProperty().multiply(2)));
        titleBox.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);

        BorderPane.setAlignment(titlePane, Pos.CENTER);
        this.setTop(titlePane);


        GridPane settings = new GridPane();
        settings.setHgap(20);

        {
            Label useEyeTracker = new Label("Desactiver l'eye Tracker:");
            CheckBox useEyeTrackerCheckBox = new CheckBox();
            useEyeTrackerCheckBox.selectedProperty().addListener((obj, newVal, oldVal) -> {
                if (newVal) {
                    graphicalMenus.getConfiguration().setMode(Configuration.GAZE_INTERACTION);
                } else {
                    graphicalMenus.getConfiguration().setMode(Configuration.MOUSE_INTERACTION);
                }
            });

            settings.add(useEyeTracker, 0, 0);
            settings.add(useEyeTrackerCheckBox, 1, 0);
        }

        settings.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(settings, Pos.CENTER);
        this.setCenter(settings);
    }
}

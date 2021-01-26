package main.UI.menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.Configuration;

public class OptionsMenu extends BorderPane {

    final Configuration configuration;

    public OptionsMenu(Configuration configuration, Stage primaryStage){
        super();
        this.configuration = configuration;
        this.configuration.setOptionsMenu(this);

        ImageView backgroundBlured = new ImageView(new Image("images/blured.jpg"));

        backgroundBlured.setOpacity(1);

        backgroundBlured.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundBlured.fitHeightProperty().bind(primaryStage.heightProperty());

        this.getChildren().add(backgroundBlured);

        this.prefWidthProperty().bind(primaryStage.widthProperty());
        this.prefHeightProperty().bind(primaryStage.heightProperty());

        StackPane titlePane = new StackPane();
        Rectangle backgroundForTitle = new Rectangle(0,0, 600,50);
        backgroundForTitle.widthProperty().bind(primaryStage.widthProperty());
        backgroundForTitle.setOpacity(0.3);

        Label title = new Label("Options");
        title.setFont(new Font(30));

        Button back = new Button("Retour");
        back.setPrefHeight(50);
        back.setOnMouseClicked((e)->{
            configuration.scene.setRoot(configuration.homeScreen);
        });

        HBox titleBox = new HBox(back, title);
         title.prefWidthProperty().bind(primaryStage.widthProperty().subtract(back.widthProperty().multiply(2)));
        titleBox.prefWidthProperty().bind(primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);

        BorderPane.setAlignment(titlePane,Pos.CENTER);
        this.setTop(titlePane);



        GridPane settings = new GridPane();
        settings.setHgap(20);

        {
            Label useEyeTracker = new Label("Desactiver l'eye Tracker:");
            CheckBox useEyeTrackerCheckBox = new CheckBox();
            useEyeTrackerCheckBox.selectedProperty().addListener((obj,newVal,oldVal)->{
                if(newVal){
                    configuration.setMode(Configuration.GAZE_INTERACTION);
                } else {
                    configuration.setMode(Configuration.MOUSE_INTERACTION);
                }
            });

            settings.add(useEyeTracker,0,0);
            settings.add(useEyeTrackerCheckBox,1,0);
        }

        settings.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(settings,Pos.CENTER);
        this.setCenter(settings);
    }
}

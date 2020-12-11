package main.process;

import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import main.SecondStage;
import main.UtilsOS;
import main.gaze.devicemanager.AbstractGazeDeviceManager;

import java.io.File;

public interface AppProcess {

    Process start();

    void init();

    StackPane createButton(BorderPane borderPane, SecondStage stage, AbstractGazeDeviceManager tgdm);

    static String getBrowser(){
        if(UtilsOS.isWindows()) {
            return "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";
        } else if (UtilsOS.isUnix()) {
            return "chromium-browser";
        }
        return "";
    }

    default Button initButton(String ressourcePath, BorderPane borderPane) {
        Button newButton = new Button();

        File f = new File(ressourcePath);
        ImageView logo = new ImageView(new Image("file:" + f.getAbsolutePath()));

        newButton.setPrefWidth(200);
        newButton.setPrefHeight(200);

//        newButton.prefWidthProperty().bind(borderPane.widthProperty().divide(8));
//        newButton.prefHeightProperty().bind(borderPane.heightProperty().divide(4));

        logo.setFitWidth(newButton.getWidth() * 0.7);
        logo.setFitHeight(newButton.getHeight() * 0.7);

        logo.fitWidthProperty().bind(newButton.widthProperty().multiply(0.7));
        logo.fitHeightProperty().bind(newButton.heightProperty().multiply(0.7));
        logo.setPreserveRatio(true);

        newButton.setGraphic(logo);

        newButton.setStyle("-fx-background-radius: 50%; ");

        Reflection reflection = new Reflection();
        reflection.setBottomOpacity(0);
        reflection.setTopOffset(-30);
        reflection.setTopOpacity(0.8);
        reflection.setFraction(0.6);


        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(-3.0);

        reflection.setInput(dropShadow);

        newButton.setEffect(reflection);
        return newButton;
    }

}

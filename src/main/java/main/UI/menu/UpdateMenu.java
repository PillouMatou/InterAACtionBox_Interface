package main.UI.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import main.UI.DoubleClickedButton;
import main.utils.JsonReader;
import main.utils.UpdateManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class UpdateMenu extends BorderPane {

    UpdateManager updateManager;

    public UpdateMenu(GraphicalMenus graphicalMenus, UpdateManager updateManager) {
        super();

        this.updateManager = updateManager;

        Rectangle r = new Rectangle();
        r.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        r.heightProperty().bind(graphicalMenus.primaryStage.heightProperty());
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#faeaed")), new Stop(1, Color.web("#cd2653"))};
        LinearGradient lg1 = new LinearGradient(0, 1, 1.5, 0, true, CycleMethod.NO_CYCLE, stops);
        r.setFill(lg1);

        this.getChildren().add(r);

        this.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        this.prefHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        StackPane titlePane = new StackPane();
        Rectangle backgroundForTitle = new Rectangle(0, 0, 600, 50);
        backgroundForTitle.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        backgroundForTitle.setFill(Color.web("#cd2653"));

        Label title = new Label("Mises \u00e0 jour");
        title.setFont(new Font(30));
        title.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        title.setTextFill(Color.web("#faeaed"));

        DoubleClickedButton back = new DoubleClickedButton("Retour");
        back.setPrefHeight(50);
        back.setMaxHeight(50);
        back.setStyle(
                "-fx-border-color: transparent; " +
                        "-fx-border-width: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-background-color: transparent; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-text-fill: #faeaed"
        );
        ImageView graphic = new ImageView("images/back.png");
        graphic.setPreserveRatio(true);
        graphic.setFitHeight(30);
        back.setGraphic(graphic);

        back.assignHandler((e) -> graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getHomeScreen()));

        HBox titleBox = new HBox(back, title);
        title.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().subtract(back.widthProperty().multiply(2)));
        titleBox.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);

        BorderPane.setAlignment(titlePane, Pos.CENTER);
        this.setTop(titlePane);


        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);

        HBox downloadEverythin = new HBox();
        downloadEverythin.setAlignment(Pos.CENTER);
        downloadEverythin.setSpacing(20);

        Label displayedLabel = new Label("Mettre à jour tous les logiciels");
        displayedLabel.setStyle("-fx-font-weight: bold; " +
                "-fx-font-family: Helvetica; " +
                "-fx-text-fill: #cd2653");
        displayedLabel.setFont(new Font(30));
        menu.setSpacing(100);

        Button downloadButton = new Button("Installer");

        downloadButton.setOnMouseClicked((event) -> {
           startUpdate();
        });

        downloadEverythin.getChildren().addAll(displayedLabel,downloadButton);

        GridPane settings = new GridPane();
        settings.setHgap(20);

        createGnomeControlCenterButton(graphicalMenus, settings, "Système:", 1);
        createGnomeControlCenterButton(graphicalMenus, settings, "AugCom:", 2);
        createGnomeControlCenterButton(graphicalMenus, settings, "InterAACtionScene:", 3);
        createGnomeControlCenterButton(graphicalMenus, settings, "GazePlay:", 4);
        createGnomeControlCenterButton(graphicalMenus, settings, "InterAACtionPlayer:", 5);

        settings.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(settings, Pos.CENTER);

        menu.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(downloadEverythin,settings);

        this.setCenter(menu);
    }

    void startUpdate(){
        List<ProcessBuilder> processList = new LinkedList<>();
        if(updateManager.interaactionSceneNeedsUpdate){
            processList.add(new ProcessBuilder("sh", "../../Update/interAACtionSceneUpdate.sh"));
        }

        if(updateManager.interaactionPlayerNeedsUpdate){
            processList.add(new ProcessBuilder("sh", "../../Update/interAACtionPlayerUpdate.sh"));
        }

        if(updateManager.gazePlayNeedsUpdate){
            processList.add(new ProcessBuilder("sh", "../../Update/gazeplayUpdate.sh"));
        }

        if(updateManager.augComNeedsUpdate){
            processList.add(new ProcessBuilder("sh", "../../Update/augcomUpdate.sh"));
        }

        letsRunTheProcessList(processList);

    }

    Runnable letsRunTheProcessList(List<ProcessBuilder> processList){
        if(! processList.isEmpty()) {
            ProcessBuilder processBuilder = processList.remove(0);
            return () -> {
                try {
                    processBuilder.inheritIO().start().onExit().thenRun(letsRunTheProcessList(processList));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }

        return () -> {};
    }

    void createGnomeControlCenterButton(GraphicalMenus graphicalMenus, GridPane settings, String label, int row) {
        Label displayedLabel = new Label(label);
        displayedLabel.setFont(new Font(20));
        displayedLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        displayedLabel.setTextFill(Color.web("#cd2653"));

        boolean needUpdate = false;
        String newVersion = "";

        if (label.equals("AugCom:")) {
            needUpdate = updateManager.augComNeedsUpdate;
            newVersion = updateManager.augComVersion;
        } else if (label.equals("InterAACtionScene:")) {
            needUpdate = updateManager.interaactionSceneNeedsUpdate;
            newVersion = updateManager.interaactionSceneVersion;
        } else if (label.equals("InterAACtionPlayer:")) {
            needUpdate = updateManager.interaactionPlayerNeedsUpdate;
            newVersion = updateManager.interaactionPlayerVersion;
        } else if (label.equals("GazePlay:")) {
            needUpdate = updateManager.gazePlayNeedsUpdate;
            newVersion = updateManager.gazePlayVersion;
        } else if (label.equals("Système:")) {
            needUpdate = updateManager.systemNeedsUpdate;
            newVersion = updateManager.systemVersion;
        }

        Button button = createTopBarButton(
                needUpdate ? "Mise \u00e0 jour disponible ! Téléchargez " + newVersion : "Le logiciel est \u00e0 jour",
                (e) -> {
                },
                needUpdate ? "images/refresh.png" : "images/tick-mark.png"
        );
        if(needUpdate){
            Timeline t = new Timeline();
            t.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(button.opacityProperty(), 0.2)));
            t.setCycleCount(20);
            t.setAutoReverse(true);
            t.play();
        }

        button.setTextFill(Color.web("#faeaed"));

        settings.add(displayedLabel, 0, row);
        settings.add(button, 1, row);
    }

    private boolean checkAugComUpdate() {
        System.out.println("AUGCOM CHECK");
        try {
            JSONObject augComJSON = JsonReader.readJsonFromUrl("https://api.github.com/repos/AFSR/AugCom-AFSR/releases/latest");
            File augComDirectory = new File("~/" + augComJSON.get("name"));
            return !augComDirectory.exists() || !augComDirectory.isDirectory();
        } catch (IOException | JSONException e) {
            return false;
        }
    }

    private boolean checkInteraactionSceneUpdate() {
        System.out.println("SCENE CHECK");
        try {
            JSONObject interaactionSceneJSON = JsonReader.readJsonFromUrl("https://api.github.com/repos/AFSR/InteraactionScene-AFSR/releases/latest");
            File interaactionSceneDirectory = new File("~/" + interaactionSceneJSON.get("name"));
            return !interaactionSceneDirectory.exists() || !interaactionSceneDirectory.isDirectory();
        } catch (IOException | JSONException e) {
            return false;
        }

    }

    private boolean checkAInteraactionPlayerUpdate() {
        System.out.println("PLAYER CHECK");
        //        JSONObject interaactionPlayerJSON = JsonReader.readJsonFromUrl("https://api.github.com/repos/AFSR/InteraactionPlayer-AFSR/releases/latest");
//        File interaactionPlayerDirectory = new File("~/"+interaactionPlayerJSON.get("name"));
        return false;
    }

    private boolean checkgazePlayUpdate() {
        System.out.println("GAZEPLAY CHECK");
        try {
            JSONObject gazePlayJSON = JsonReader.readJsonFromUrl("https://api.github.com/repos/AFSR/GazePlay-AFSR/releases/latest");
            File gazePlayDirectory = new File("~/" + gazePlayJSON.get("name"));
            return !gazePlayDirectory.exists() || !gazePlayDirectory.isDirectory();
        } catch (IOException | JSONException e) {
            return false;
        }
    }

    Button createTopBarButton(String text, EventHandler eventhandler, String imagePath) {
        DoubleClickedButton optionButton = new DoubleClickedButton(text);
        optionButton.setPrefHeight(50);
        optionButton.setMaxHeight(50);
        optionButton.setStyle(
                "-fx-border-color: transparent; " +
                        "-fx-border-width: 0; " +
                        "-fx-background-radius: 0; " +
                        "-fx-background-color: transparent; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-text-fill: #faeaed"
        );
        ImageView graphic = new ImageView(imagePath);
        graphic.setPreserveRatio(true);
        graphic.setFitHeight(20);
        optionButton.setGraphic(graphic);
        optionButton.assignHandler(eventhandler);
        return optionButton;
    }


}

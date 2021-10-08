package main.UI.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
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
import main.UI.UpdateService;
import main.utils.UpdateManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class UpdateMenu extends BorderPane {

    UpdateManager updateManager;
    Button downloadButton;
    ProgressBar[] progressBars = new ProgressBar[6];
    Label displayedLabel;

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

        displayedLabel = new Label("Mettre à jour tous les logiciels");
        displayedLabel.setStyle("-fx-font-weight: bold; " +
                "-fx-font-family: Helvetica; " +
                "-fx-text-fill: #cd2653");
        displayedLabel.setFont(new Font(30));
        menu.setSpacing(30);

        downloadButton = new Button("Installer tous");

        downloadButton.setOnMouseClicked((event) -> {
            downloadButton.setVisible(false);
            startUpdateAll();
        });

        downloadEverythin.getChildren().addAll(displayedLabel, downloadButton);

        if (!updateManager.anyUpdateNeeded.get()) {
            displayedLabel.setText("Votre système est à jour");
            downloadEverythin.getChildren().remove(downloadButton);
        }

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
        progressBars[0]=new ProgressBar();
        for(ProgressBar progressBar:progressBars) {
            progressBar.setProgress(0);
        }
        progressBars[0].prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(3));
        progressBars[0].setVisible(false);
        progressBars[0].progressProperty().addListener((obj, oldval, newval)-> {
            if(!progressBars[0].isVisible() && newval.doubleValue() > 0) {
                progressBars[0].setVisible(true);
            }
        });
        menu.getChildren().addAll(downloadEverythin, progressBars[0], settings);

        this.setCenter(menu);
    }


    void startUpdateAll(){
        Thread t = new Thread() {
            public void run() {
                for(ProgressBar progressBar:progressBars) {
                    progressBar.setProgress(0);
                }
                double systemCoeff = updateManager.updateServices[UpdateService.SYSTEME].getUpdateProperty().get() ? 1 : 0;
                double augcomCoeff = updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get() ? 1 : 0;
                double interaactionSceneCoef = updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get() ? 1 : 0;
                double gazeplayCoeff = updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get() ? 1 : 0;
                double interaactionPlayerCoeff = updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get() ? 1 : 0;

                double coeff = 1 / (systemCoeff+augcomCoeff+interaactionSceneCoef+gazeplayCoeff+interaactionPlayerCoeff);

                while(progressBars[0].getProgress() < 1){
                    progressBars[0].setProgress(
                            progressBars[1].getProgress()*coeff+
                            progressBars[2].getProgress()*coeff+
                            progressBars[3].getProgress()*coeff+
                            progressBars[4].getProgress()*coeff+
                            progressBars[5].getProgress()*coeff
                    );
                }
            }
        };
        t.start();


        Thread t2 = new Thread() {
            public void run() {
                if( updateManager.updateServices[UpdateService.SYSTEME].getUpdateProperty().get()) {
                    startUpdateSystem();
                }
                if(updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get()) {
                    startUpdateAugCom();
                }
                if(updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get()) {
                    startUpdateInterAACtonScene();
                }
                if(updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get()) {
                    startUpdateGazePlay();
                }
                if(updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get()) {
                    startUpdateInterAACtionPlayer();
                }
            }
        };
        t2.start();
    }
    void startUpdateSystem(){
                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dir /s /b \"C:/Users/Sebastien\" ");
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    progressThing(p, 1);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }

    }
    void startUpdateAugCom(){
                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dir /s /b \"C:/Users/Sebastien\" ");
                    //ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/augcomUpdate.sh");
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    progressThing(p, 2);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
    }
    void startUpdateInterAACtonScene(){
                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dir /s /b \"C:/Users/Sebastien\" ");
                    //ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionSceneUpdate.sh");
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    progressThing(p, 3);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
    }
    void startUpdateGazePlay(){
                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dir /s /b \"C:/Users/Sebastien\" ");
                    //ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/gazeplayUpdate.sh");
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    progressThing(p, 4);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
    }
    void startUpdateInterAACtionPlayer(){
                try {
                    ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dir /s /b \"C:/Users/Sebastien\" ");
                    //ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionPlayerUpdate.sh");
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    progressThing(p, 5);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
    }

    void progressThing(Process p, int index) throws IOException {
        String s;
        BufferedReader stdout = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
        double i = 0;
        while ((s = stdout.readLine()) != null && progressBars[index].getProgress()<1) {
            i = i + 0.001*index;
            String t = "AugCom.tar.gze       "+ ((int)i) +"% [======>               ]   9.65M  5.6MB/s";
            int indexPercent = t.indexOf('%');
            int progress = Integer.parseInt(t.substring(indexPercent-3, indexPercent).replace(" ", ""));
            progressBars[index].setProgress(progress / 100.);
        }
        p.getInputStream().close();
        p.getOutputStream().close();
        p.getErrorStream().close();
        p.destroy();
    }

    void startUpdate2() {
        List<ProcessBuilder> processList = new LinkedList<>();
        if (updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get()) {
            processList.add(new ProcessBuilder("sh", "../../Update/interAACtionSceneUpdate.sh"));
        }

        if (updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get()) {
            processList.add(new ProcessBuilder("sh", "../../Update/interAACtionPlayerUpdate.sh"));
        }

        if (updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get()) {
            processList.add(new ProcessBuilder("sh", "../../Update/gazeplayUpdate.sh"));
        }

        if (updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get()) {
            processList.add(new ProcessBuilder("sh", "../../Update/augcomUpdate.sh"));
        }

        letsRunTheProcessList(processList);

    }

    Runnable letsRunTheProcessList(List<ProcessBuilder> processList) {
        if (!processList.isEmpty()) {
            ProcessBuilder processBuilder = processList.remove(0);
            return () -> {
                try {
                    processBuilder.inheritIO().start().onExit().thenRun(letsRunTheProcessList(processList));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        }

        return () -> {
        };
    }

    void createGnomeControlCenterButton(GraphicalMenus graphicalMenus, GridPane settings, String label, int row) {
        Label displayedLabel = new Label(label);
        displayedLabel.setFont(new Font(20));
        displayedLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        displayedLabel.setTextFill(Color.web("#cd2653"));

        Button button = createTopBarButton(
                "Le logiciel est \u00e0 jour",
                (e) -> {
                },
                "images/tick-mark.png"
        );


        button.setTextFill(Color.web("#faeaed"));

        progressBars[row]= new ProgressBar();

        settings.add(displayedLabel, 0, row);
        settings.add(button, 1, row);

        checkMainUpdate(settings, label, row, button);
    }

    void checkMainUpdate( GridPane settings, String label, int row, Button button){

        boolean needUpdate = false;
        String newVersion = "";

        switch (label) {
            // Warning ! Don't forget to add ":" at the end of the string
            case "AugCom:":
                needUpdate = updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get();
                newVersion = updateManager.updateServices[UpdateService.AUGCOM].getVersion();
                break;
            case "InterAACtionScene:":
                needUpdate = updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get();
                newVersion = updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getVersion();
                break;
            case "InterAACtionPlayer:":
                needUpdate = updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get();
                newVersion = updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getVersion();
                break;
            case "GazePlay:":
                needUpdate = updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get();
                newVersion = updateManager.updateServices[UpdateService.GAZEPLAY].getVersion();
                break;
            case "Système:":
                needUpdate = updateManager.updateServices[UpdateService.SYSTEME].getUpdateProperty().get();
                newVersion = updateManager.updateServices[UpdateService.SYSTEME].getVersion();
                break;
        }
        if (needUpdate) {
            Timeline t = new Timeline();
            t.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(button.opacityProperty(), 0.2)));
            t.setCycleCount(20);
            t.setAutoReverse(true);
            t.play();

            progressBars[row].progressProperty().addListener((observ,oldval, newval)->{
                if(!settings.getChildren().contains(progressBars[row]) && newval.doubleValue() > 0){
                    Platform.runLater(()->{
                        settings.add(progressBars[row],2, row);
                    });
                }
            });
        }

        button.setText(needUpdate ? "Mise \u00e0 jour disponible ! Téléchargez " + newVersion : "Le logiciel est \u00e0 jour");

        ((ImageView)button.getGraphic()).setImage(new Image(needUpdate ? "images/refresh.png" : "images/tick-mark.png"));

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

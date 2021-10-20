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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import main.utils.UpdateManager;
import main.utils.UpdateService;
import main.utils.UtilsUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class UpdateMenu extends BorderPane {

    UpdateManager updateManager;
    Button downloadButton;
    ProgressBar[] progressBars = new ProgressBar[6];
    Label displayedLabel;
    GraphicalMenus graphicalMenus;

    public UpdateMenu(GraphicalMenus graphicalMenus, UpdateManager updateManager) {
        super();

        this.updateManager = updateManager;
        this.graphicalMenus = graphicalMenus;
        for (int i = 0; i <= 5; i++) {
            progressBars[i] = new ProgressBar();
            progressBars[i].setProgress(0);
        }

        this.getChildren().add(UtilsUI.createBackground(graphicalMenus));

        this.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        this.prefHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        Button back = UtilsUI.getDoubleClickedButton("Retour", "images/back.png", (e) -> graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getHomeScreen()), graphicalMenus.primaryStage);

        this.setTop(createTopBar(back));


        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);

        HBox downloadEverythin = new HBox();
        downloadEverythin.setAlignment(Pos.CENTER);
        downloadEverythin.setSpacing(20);

        displayedLabel = new Label("Mettre \u00e0 jour tous les logiciels");
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

        downloadEverythin.getChildren().addAll(displayedLabel);

        updateManager.anyUpdateNeeded.addListener((obs, oldval, newval) -> {
            if (newval) {
                displayedLabel.setText("Mettre \u00e0 jour tous les logiciels");
                downloadEverythin.getChildren().add(downloadButton);
            } else {
                displayedLabel.setText("Votre syst\u00e8me est \u00e0 jour");
                downloadEverythin.getChildren().remove(downloadButton);
            }
        });


        GridPane settings = new GridPane();
        settings.setHgap(20);
        settings.setVgap(graphicalMenus.primaryStage.getHeight() / 30);

        createGnomeControlCenterButton(settings, UpdateService.SYSTEME);
        createGnomeControlCenterButton(settings, UpdateService.AUGCOM);
        createGnomeControlCenterButton(settings, UpdateService.INTERAACTION_SCENE);
        createGnomeControlCenterButton(settings, UpdateService.GAZEPLAY);
        createGnomeControlCenterButton(settings, UpdateService.INTERAACTION_PLAYER);

        settings.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(settings, Pos.CENTER);

        menu.setAlignment(Pos.CENTER);
        progressBars[0].prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(3));
        progressBars[0].visibleProperty().bind(
                progressBars[1].visibleProperty().or(
                        progressBars[2].visibleProperty()).or(
                        progressBars[3].visibleProperty()).or(
                        progressBars[4].visibleProperty()).or(
                        progressBars[5].visibleProperty()
                )
        );
        menu.getChildren().addAll(downloadEverythin, progressBars[0], settings);

        this.setCenter(menu);
    }

    StackPane createTopBar(Button back) {
        StackPane titlePane = new StackPane();
        Rectangle backgroundForTitle = new Rectangle(0, 0, graphicalMenus.primaryStage.getWidth(), graphicalMenus.primaryStage.getHeight() / 10);
        backgroundForTitle.heightProperty().bind(graphicalMenus.primaryStage.heightProperty().divide(10));
        backgroundForTitle.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        backgroundForTitle.setFill(Color.web("#cd2653"));

        back.prefHeightProperty().bind(backgroundForTitle.heightProperty());

        Label title = new Label("Mises \u00e0 jour");
        title.setFont(new Font(30));
        title.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        title.setTextFill(Color.web("#faeaed"));
        BorderPane titleBox = new BorderPane();
        title.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().subtract(back.widthProperty().multiply(2)));
        titleBox.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titleBox.setLeft(back);
        titleBox.setCenter(title);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);

        BorderPane.setAlignment(titlePane, Pos.CENTER);
        return titlePane;
    }


    void startUpdateAll() {
        Thread t = new Thread() {
            public void run() {
                for (ProgressBar progressBar : progressBars) {
                    progressBar.setProgress(0);
                }
                double systemCoeff = updateManager.updateServices[UpdateService.SYSTEME].getUpdateProperty().get() ? 1 : 0;
                double augcomCoeff = updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get() ? 1 : 0;
                double interaactionSceneCoef = updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get() ? 1 : 0;
                double gazeplayCoeff = updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get() ? 1 : 0;
                double interaactionPlayerCoeff = updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get() ? 1 : 0;

                double coeff = 1 / (systemCoeff + augcomCoeff + interaactionSceneCoef + gazeplayCoeff + interaactionPlayerCoeff);

                while (progressBars[0].getProgress() < 1) {
                    progressBars[0].setProgress(
                            progressBars[1].getProgress() * coeff +
                                    progressBars[2].getProgress() * coeff +
                                    progressBars[3].getProgress() * coeff +
                                    progressBars[4].getProgress() * coeff +
                                    progressBars[5].getProgress() * coeff
                    );
                }
            }
        };
        t.setDaemon(true);
        t.start();


        Thread t2 = new Thread() {
            public void run() {
                if (updateManager.updateServices[UpdateService.SYSTEME].getUpdateProperty().get()) {
                    startUpdateSystem();
                }
                if (updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get()) {
                    startUpdateAugCom();
                }
                if (updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get()) {
                    startUpdateInterAACtonScene();
                }
                if (updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get()) {
                    startUpdateGazePlay();
                }
                if (updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get()) {
                    startUpdateInterAACtionPlayer();
                }


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateManager.checkUpdates();
            }
        };
        t2.setDaemon(true);
        t2.start();
    }

    void closeProcessStream(Process p) {
        try {
            p.getInputStream().close();
            p.getOutputStream().close();
            p.getErrorStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.destroy();
    }

    void startUpdateSystem() {
        try {
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "dir /s /b \"C:/Users/Sebastien\" ");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.onExit().thenRun(() -> {
                closeProcessStream(p);
                progressBars[UpdateService.SYSTEME + 1].setVisible(false);
            });
            progressThing(p, 1);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

    }

    void startUpdateAugCom() {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/augcomUpdate.sh");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.onExit().thenRun(() -> {
                closeProcessStream(p);
                progressBars[UpdateService.AUGCOM + 1].setVisible(false);
            });
            progressThing(p, 2);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    void startUpdateInterAACtonScene() {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionSceneUpdate.sh");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.onExit().thenRun(() -> {
                closeProcessStream(p);
                progressBars[UpdateService.INTERAACTION_SCENE + 1].setVisible(false);
            });
            progressThing(p, 3);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    void startUpdateGazePlay() {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/gazeplayUpdate.sh");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.onExit().thenRun(() -> {
                closeProcessStream(p);
                progressBars[UpdateService.GAZEPLAY + 1].setVisible(false);
            });
            progressThing(p, 4);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    void startUpdateInterAACtionPlayer() {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionPlayerUpdate.sh");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            p.onExit().thenRun(() -> {
                closeProcessStream(p);
                progressBars[UpdateService.INTERAACTION_PLAYER + 1].setVisible(false);
            });
            progressThing(p, 5);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    void progressThing(Process p, int index) throws IOException {
        String s;
        BufferedReader stdout = new BufferedReader(
                new InputStreamReader(p.getInputStream()));
        while ((s = stdout.readLine()) != null && progressBars[index].getProgress() < 1) {
            int indexPercent = s.indexOf('%');
            if (indexPercent != -1) {
                try {
                    int progress = Integer.parseInt(s.substring(indexPercent - 3, indexPercent).replace(" ", ""));
                    progressBars[index].setProgress(progress / 100.);
                } catch (NumberFormatException e) {
                    //DO NOTHING
                }
            }
        }
    }

    void createGnomeControlCenterButton(GridPane settings, int serviceIndex) {
        Label displayedLabel = new Label(updateManager.updateServices[serviceIndex].getName() + ":");
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

        int row = serviceIndex + 1;

        settings.add(displayedLabel, 0, row);
        settings.add(button, 1, row);

        checkMainUpdate(settings, serviceIndex, button);
    }

    void checkMainUpdate(GridPane settings, int serviceIndex, Button button) {
        updateManager.updateServices[serviceIndex].getUpdateProperty().addListener((obs, oldval, newval) -> {
            if (newval) {
                Timeline t = new Timeline();
                t.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(button.opacityProperty(), 0.2)));
                t.setCycleCount(20);
                t.setAutoReverse(true);
                t.play();
                String newVersion = updateManager.updateServices[serviceIndex].getVersion();
                button.setText("Mise \u00e0 jour " + newVersion + " disponible !");
                ((ImageView) button.getGraphic()).setImage(new Image("images/refresh.png"));
            } else {
                button.setText("Le logiciel est \u00e0 jour");
                ((ImageView) button.getGraphic()).setImage(new Image("images/tick-mark.png"));
            }
        });

        progressBars[serviceIndex + 1].progressProperty().addListener((observ, oldval, newval) -> {
            if (!settings.getChildren().contains(progressBars[serviceIndex + 1]) && newval.doubleValue() > 0) {
                Platform.runLater(() -> {
                    settings.add(progressBars[serviceIndex + 1], 2, serviceIndex + 1);
                });
            }
        });
    }

    Button createTopBarButton(String text, EventHandler eventhandler, String imagePath) {
        return UtilsUI.getDoubleClickedButton(text, imagePath, eventhandler, graphicalMenus.primaryStage);
    }


}

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import main.Configuration;
import main.Main;
import main.UI.Translator;
import main.utils.UpdateManager;
import main.utils.UpdateService;
import main.utils.UtilsUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class UpdateMenu extends BorderPane {

    UpdateManager updateManager;
    ProgressBar[] progressBars = new ProgressBar[8];
    Button installAllButton;
    GraphicalMenus graphicalMenus;

    public UpdateMenu(GraphicalMenus graphicalMenus, UpdateManager updateManager, Main main, Configuration configuration) {
        super();

        Translator translator = main.getTranslator();

        this.updateManager = updateManager;
        this.graphicalMenus = graphicalMenus;
        for (int i = 0; i <= 7; i++) {
            progressBars[i] = new ProgressBar();
            progressBars[i].setProgress(0);
        }

        this.getChildren().add(UtilsUI.createBackground(graphicalMenus));

        this.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        this.prefHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        this.setTop(UtilsUI.createTopBar(translator,graphicalMenus.getHomeScreen(), graphicalMenus, "Mises \u00e0 jour"));


        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);

        HBox downloadEverythin = new HBox();
        downloadEverythin.setAlignment(Pos.CENTER);
        downloadEverythin.setSpacing(20);

        installAllButton = createTopBarI18NButton(translator, "Mettre \u00e0 jour tous les logiciels", (event) -> {
            if (updateManager.anyUpdateNeeded.getValue()) {
                startUpdateAll();
            }
        }, null);
        String style = "-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-text-fill: #cd2653; -fx-font-size: 3em; " +
                "-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0; -fx-background-color: transparent; ";
        installAllButton.setStyle(style);
        installAllButton.hoverProperty().addListener((obs, oldval, newval) -> {
            if (newval && updateManager.anyUpdateNeeded.getValue()) {
                installAllButton.setStyle(style + "-fx-cursor: hand; -fx-underline: true");
            } else {
                installAllButton.setStyle(style);
            }
        });
        menu.setSpacing(30);

        downloadEverythin.getChildren().addAll(installAllButton);

        updateManager.anyUpdateNeeded.addListener((obs, oldval, newval) -> {
            if (newval) {
                installAllButton.setText("Mettre \u00e0 jour tous les logiciels");
            } else {
                installAllButton.setText("Votre syst\u00e8me est \u00e0 jour");
            }
        });


        GridPane settings = new GridPane();
        settings.setHgap(20);
        settings.setVgap(graphicalMenus.primaryStage.getHeight() / 30);

        createGnomeControlCenterButton(translator, settings, UpdateService.SYSTEME);
        createGnomeControlCenterButton(translator, settings, UpdateService.AUGCOM);
        createGnomeControlCenterButton(translator, settings, UpdateService.INTERAACTION_SCENE);
        createGnomeControlCenterButton(translator, settings, UpdateService.GAZEPLAY);
        createGnomeControlCenterButton(translator, settings, UpdateService.INTERAACTION_PLAYER);
        createGnomeControlCenterButton(translator, settings, UpdateService.INTERAACTION_GAZE);
        createGnomeControlCenterButton(translator, settings, UpdateService.INTERAACTION_INTERFACE);

        settings.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(settings, Pos.CENTER);

        menu.setAlignment(Pos.CENTER);
        progressBars[0].prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(3));
        progressBars[0].visibleProperty().bind(
                progressBars[1].visibleProperty().or(
                        progressBars[2].visibleProperty()).or(
                        progressBars[3].visibleProperty()).or(
                        progressBars[4].visibleProperty()).or(
                        progressBars[5].visibleProperty()).or(
                        progressBars[6].visibleProperty()).or(
                        progressBars[7].visibleProperty()
                )
        );
        menu.getChildren().addAll(downloadEverythin, progressBars[0], settings);

        this.setCenter(menu);
        this.setBottom(new Label(Configuration.VERSION));
    }

    void startUpdateAll() {
        Thread t = new Thread() {
            public void run() {
                for (ProgressBar progressBar : progressBars) {
                    progressBar.setProgress(0);
                }
                double systemCoeff = updateManager.updateServices[UpdateService.SYSTEME].getUpdateProperty().get() ? 1 : 0;
                double augcomCoeff = updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get() ? 1 : 0;
                double interaactionSceneCoeff = updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get() ? 1 : 0;
                double gazeplayCoeff = updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get() ? 1 : 0;
                double interaactionPlayerCoeff = updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get() ? 1 : 0;
                double interaactionGazeCoeff = updateManager.updateServices[UpdateService.INTERAACTION_GAZE].getUpdateProperty().get() ? 1 : 0;
                double interaactionInterfaceCoeff = updateManager.updateServices[UpdateService.INTERAACTION_INTERFACE].getUpdateProperty().get() ? 1 : 0;

                double coeff = 1 / (systemCoeff + augcomCoeff + interaactionSceneCoeff + gazeplayCoeff + interaactionPlayerCoeff + interaactionGazeCoeff + interaactionInterfaceCoeff);

                while (progressBars[0].getProgress() < 1) {
                    progressBars[0].setProgress(
                            progressBars[1].getProgress() * coeff +
                                    progressBars[2].getProgress() * coeff +
                                    progressBars[3].getProgress() * coeff +
                                    progressBars[4].getProgress() * coeff +
                                    progressBars[5].getProgress() * coeff +
                                    progressBars[6].getProgress() * coeff +
                                    progressBars[7].getProgress() * coeff
                    );
                }
            }
        };
        t.setDaemon(true);
        t.start();


        Thread t2 = new Thread() {
            public void run() {
                startUpdateSystem();
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
        if (updateManager.updateServices[UpdateService.SYSTEME].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.SYSTEME + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.SYSTEME].getOutput().setValue("");
                    });
                    startUpdateAugCom();
                });
                progressPercent(p, 1);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        } else {
            startUpdateAugCom();
        }
    }

    void startUpdateOnlySystem() {
        if (updateManager.updateServices[UpdateService.SYSTEME].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.SYSTEME + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.SYSTEME].getOutput().setValue("");
                    });
                });
                progressPercent(p, 1);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    void startUpdateAugCom() {
        if (updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/augcomUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.AUGCOM + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.AUGCOM].getOutput().setValue("");
                    });
                    startUpdateInterAACtonScene();
                });
                progressPercent(p, 2);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        } else {
            startUpdateInterAACtonScene();
        }
    }

    void startUpdateOnlyAugCom() {
        if (updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/augcomUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.AUGCOM + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.AUGCOM].getOutput().setValue("");
                    });
                });
                progressPercent(p, 2);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    void startUpdateInterAACtonScene() {
        if (updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionSceneUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.INTERAACTION_SCENE + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getOutput().setValue("");
                    });
                    startUpdateInterAACtionPlayer();
                });
                progressPercent(p, 3);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        } else {
            startUpdateInterAACtionPlayer();
        }
    }

    void startUpdateOnlyInterAACtonScene() {
        if (updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionSceneUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.INTERAACTION_SCENE + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getOutput().setValue("");
                    });
                });
                progressPercent(p, 3);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    void startUpdateGazePlay() {
        if (updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/gazeplayUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.GAZEPLAY + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.GAZEPLAY].getOutput().setValue("");
                    });
                    startUpdateInterAACtionGaze();
                });
                progressPercent(p, 4);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        } else {
            startUpdateInterAACtionGaze();
        }
    }

    void startUpdateOnlyGazePlay() {
        if (updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/gazeplayUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.GAZEPLAY + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.GAZEPLAY].getOutput().setValue("");
                    });
                });
                progressPercent(p, 4);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    void startUpdateInterAACtionPlayer() {

        if (updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionPlayerUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.INTERAACTION_PLAYER + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getOutput().setValue("");
                    });
                    startUpdateGazePlay();
                });
                progressPercent(p, 5);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        } else {
            startUpdateGazePlay();
        }
    }

    void startUpdateOnlyInterAACtionPlayer() {

        if (updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionPlayerUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.INTERAACTION_PLAYER + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getOutput().setValue("");
                    });
                });
                progressPercent(p, 5);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    void startUpdateInterAACtionGaze(){
        if (updateManager.updateServices[UpdateService.INTERAACTION_GAZE].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionGazeUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.INTERAACTION_GAZE + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.INTERAACTION_GAZE].getOutput().setValue("");
                    });
                    startUpdateInterAACtionInterface();
                });
                progressPercent(p, 6);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }else{
            startUpdateInterAACtionInterface();
        }
    }

    void startUpdateOnlyInterAACtionGaze(){
        if (updateManager.updateServices[UpdateService.INTERAACTION_GAZE].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionGazeUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.INTERAACTION_GAZE + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.INTERAACTION_GAZE].getOutput().setValue("");
                    });
                });
                progressPercent(p, 6);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    void startUpdateInterAACtionInterface(){
        if (updateManager.updateServices[UpdateService.INTERAACTION_INTERFACE].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionBoxInterfaceUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.INTERAACTION_INTERFACE + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.INTERAACTION_INTERFACE].getOutput().setValue("");
                    });
                    updateManager.checkUpdates();
                });
                progressPercent(p, 7);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }else {
            updateManager.checkUpdates();
        }
    }

    void startUpdateOnlyInterAACtionInterface(){
        if (updateManager.updateServices[UpdateService.INTERAACTION_INTERFACE].getUpdateProperty().get()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("sh", "../../Update/interAACtionBoxInterfaceUpdate.sh");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                p.onExit().thenRun(() -> {
                    closeProcessStream(p);
                    Platform.runLater(() -> {
                        progressBars[UpdateService.INTERAACTION_INTERFACE + 1].setVisible(false);
                        updateManager.updateServices[UpdateService.INTERAACTION_INTERFACE].getOutput().setValue("");
                    });
                });
                progressPercent(p, 7);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    void progressPercent(Process p, int index) {
        Thread t = new Thread(() -> {
            String s;
            try {
                BufferedReader stdout = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                while ((s = stdout.readLine()) != null) {
                    int indexPercent = s.indexOf('%');
                    if (indexPercent != -1) {
                        try {
                            int progress = Integer.parseInt(s.substring(indexPercent - 3, indexPercent).replace(" ", ""));
                            progressBars[index].setProgress(progress / 100.);
                        } catch (NumberFormatException e) {
                            //DO NOTHING
                        }
                    } else {
                        String finalS = s;
                        Platform.runLater(() -> {
                            updateManager.updateServices[index - 1].getOutput().setValue(finalS);
                        });
                    }
                }
            } catch (IOException e) {
                //DO NOTHING
            }
        });
        t.setDaemon(true);
        t.start();
    }

    void createGnomeControlCenterButton(Translator translator,GridPane settings, int serviceIndex) {
        Label displayedLabel = new Label(updateManager.updateServices[serviceIndex].getName() + ":");
        displayedLabel.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica; -fx-font-size: 2.5em");
        displayedLabel.setTextFill(Color.web("#cd2653"));

        Button button = createTopBarI18NButton(translator,
                "Le logiciel est \u00e0 jour",
                (event) -> {
                    if (updateManager.updateServices[serviceIndex].getUpdateProperty().getValue()) {
                        switch (serviceIndex) {
                            case UpdateService.SYSTEME:
                                startUpdateOnlySystem();
                                break;
                            case UpdateService.AUGCOM:
                                startUpdateOnlyAugCom();
                                break;
                            case UpdateService.INTERAACTION_SCENE:
                                startUpdateOnlyInterAACtonScene();
                                break;
                            case UpdateService.GAZEPLAY:
                                startUpdateOnlyGazePlay();
                                break;
                            case UpdateService.INTERAACTION_PLAYER:
                                startUpdateOnlyInterAACtionPlayer();
                                break;
                            case UpdateService.INTERAACTION_GAZE:
                                startUpdateOnlyInterAACtionGaze();
                                break;
                            case UpdateService.INTERAACTION_INTERFACE:
                                startUpdateOnlyInterAACtionInterface();
                                break;
                            default:
                                break;
                        }
                    }
                },
                "images/tick-mark.png"
        );

        String style = "-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0; -fx-font-size: 2em ;" +
                "-fx-background-color: transparent; -fx-font-weight: bold; -fx-font-family: Helvetica; -fx-text-fill: #faeaed;";
        button.setStyle(style);
        button.hoverProperty().addListener((obs, oldval, newval) -> {
            if (newval && updateManager.updateServices[serviceIndex].getUpdateProperty().getValue()) {
                button.setStyle(style + "-fx-cursor: hand; -fx-underline: true");
            } else {
                button.setStyle(style);
            }
        });

        int row = serviceIndex + 1;

        settings.add(displayedLabel, 0, row);
        settings.add(button, 1, row);
        Label output = new Label();
        output.textProperty().bind(updateManager.updateServices[serviceIndex].getOutput());
        output.setOpacity(0.5);
        output.setWrapText(true);
        output.setStyle("-fx-font-size: 0.5em");
        output.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(10));
        output.maxWidthProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(10));
        settings.add(output, 3, row);

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

    Button createTopBarI18NButton(Translator translator,String text, EventHandler eventhandler, String imagePath) {
        return UtilsUI.getDoubleClickedI18NButton(translator,text, imagePath, eventhandler, graphicalMenus.primaryStage);
    }


}

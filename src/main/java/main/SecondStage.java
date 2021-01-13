package main;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.gaze.devicemanager.AbstractGazeDeviceManager;
import main.process.AugComProcess;
import main.process.GazePlayProcess;
import main.process.InteraactionSceneProcess;
import main.process.YoutubeProcess;

import java.io.File;
import java.util.LinkedList;

public class SecondStage extends Stage {
    public Process proc;
    public AbstractGazeDeviceManager tgdm;
    public MenuPane secondSageRoot;

    public SecondStage(Configuration configuration, Stage primaryStage, AbstractGazeDeviceManager tgdm, String gazePlayInstallationRepo) {
        super();
        this.initStyle(StageStyle.TRANSPARENT);
        this.tgdm = tgdm;
        secondSageRoot = new MenuPane(primaryStage);
        secondSageRoot.setCloseMenuHandler((event) -> {
            if (proc == null) {
                primaryStage.show();
                primaryStage.toFront();
            }
            this.hide();
        });
        secondSageRoot.buttons = setButtons(primaryStage, gazePlayInstallationRepo);
        secondSageRoot.createCircularButtons();

        Scene scene = new Scene(secondSageRoot, Color.TRANSPARENT);
        this.setScene(scene);

        scene.setOnMouseMoved((e)->{
            if (configuration.isGazeInteraction()) {
                configuration.analyse(e.getScreenX(), e.getScreenY());
            }
        });

    }

    public LinkedList<ProgressButton> setButtons(Stage primaryStage, String gazePlayInstallationRepo) {
        EventHandler<Event> eventhandler = null;
        ImageView logo;
        LinkedList<ProgressButton> buttons = new LinkedList<ProgressButton>();
        for (int i = 0; i < 6; i++) {
            buttons.add(new ProgressButton());
            switch (i) {
                case 0:
                    buttons.get(i).getLabel().setText("Exit");
                    eventhandler = e -> {
                        if (proc != null) {
                            proc.destroy();
                            proc = null;
                        }
                        Platform.exit();
                        System.exit(0);
                    };
                    break;
                case 1:
                    buttons.get(i).getLabel().setText("Menu");
                    eventhandler = (event) -> {
                        if (proc != null) {
                            proc.destroy();
                            proc = null;
                        }
                        primaryStage.show();
                        primaryStage.toFront();
                        this.hide();
                    };
                    break;
                case 2:
                    buttons.get(i).getLabel().setText("AugCom");
                    logo = new ImageView(new Image("images/angular.png"));
                    logo.setPreserveRatio(true);
                    logo.setFitWidth(100);
                    logo.setFitHeight(100);
                    buttons.get(i).setImage(logo);
                    eventhandler = e -> {
                        if (proc != null) {
                            proc.destroy();
                            proc = null;
                        }
                        AugComProcess augComProcess = new AugComProcess();
                        augComProcess.init();
                        proc = augComProcess.start();
                        proc.onExit().thenRunAsync(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        primaryStage.show();
                                        primaryStage.toFront();
                                    }
                                }
                        );
                    };
                    break;
                case 3:
                    buttons.get(i).getLabel().setText("InteraactionScene");
                    logo = new ImageView(new Image("images/angular.png"));
                    logo.setPreserveRatio(true);
                    logo.setFitWidth(100);
                    logo.setFitHeight(100);
                    // buttons.get(i).setText("");
                    buttons.get(i).setImage(logo);
                    eventhandler = e -> {
                        if (proc != null) {
                            proc.destroy();
                            proc = null;
                        }
                        InteraactionSceneProcess interaactionSceneProcess = new InteraactionSceneProcess();
                        interaactionSceneProcess.init();
                        proc = interaactionSceneProcess.start();
                        proc.onExit().thenRunAsync(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        primaryStage.show();
                                        primaryStage.toFront();
                                    }
                                }
                        );
                    };
                    break;
                case 4:
                    buttons.get(i).getLabel().setText("Youtube");
                    logo = new ImageView(new Image("images/yt.png"));
                    logo.setPreserveRatio(true);
                    logo.setFitWidth(100);
                    logo.setFitHeight(100);
                    //buttons.get(i).setText("");
                    buttons.get(i).setImage(logo);
                    eventhandler = e -> {
                        if (proc != null) {
                            proc.destroy();
                            proc = null;
                        }
                        YoutubeProcess youtubeProcess = new YoutubeProcess();
                        youtubeProcess.init();
                        proc = youtubeProcess.start();
                        proc.onExit().thenRunAsync(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        primaryStage.show();
                                        primaryStage.toFront();
                                    }
                                }
                        );
                    };
                    break;
                case 5:
                    buttons.get(i).getLabel().setText("GazePlay");
                    logo = new ImageView(new Image("images/gazeplayicon.png"));
                    logo.setPreserveRatio(true);
                    logo.setFitWidth(100);
                    logo.setFitHeight(100);
                    // buttons.get(i).setText("");
                    buttons.get(i).setImage(logo);
                    eventhandler = e -> {
                        if (proc != null) {
                            proc.destroy();
                            proc = null;
                        }
                        GazePlayProcess gazePlayProcess = new GazePlayProcess(gazePlayInstallationRepo);
                        gazePlayProcess.init();
                        proc = gazePlayProcess.start();
                        proc.onExit().thenRunAsync(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        primaryStage.show();
                                        primaryStage.toFront();
                                    }
                                }
                        );
                    };
                    break;

                default:
                    break;
            }

            buttons.get(i).setOnMouseClicked(eventhandler);
            buttons.get(i).assignIndicator(eventhandler, 500);
            buttons.get(i).active();
            tgdm.addEventFilter(buttons.get(i));
        }
        return buttons;
    }

}

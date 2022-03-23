package main.UI.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import main.Configuration;
import main.Main;
import main.UI.*;
import main.gaze.devicemanager.TobiiGazeDeviceManager;
import main.process.*;
import main.process.xdotoolProcess.ActivateMainWindowProcess;
import main.utils.*;
import tobii.Tobii;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

@Slf4j
public class HomeScreen extends BorderPane {

    private final GraphicalMenus graphicalMenus;
    private final I18NProgressButton closeMenuButton;
    private final VBox centerMenu;
    private final EventHandler goToUpdateMenu;
    private UpdateManager updateManager;

    InterAACtionGazeNamedProcessCreator interAACtionGazeNamedProcessCreator = new InterAACtionGazeNamedProcessCreator();


    public HomeScreen(GraphicalMenus graphicalMenus, UpdateManager updateManager, Main main, Configuration configuration) {
        super();
        this.graphicalMenus = graphicalMenus;
        this.updateManager = updateManager;

        Translator translator = main.getTranslator();

        this.getChildren().add(UtilsUI.createBackground(graphicalMenus));

        String tobiiNotConnected = Arrays.toString(Tobii.gazePosition());

        centerMenu = new VBox();

        centerMenu.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(centerMenu, Pos.CENTER);
        double rest = graphicalMenus.primaryStage.getHeight() - (graphicalMenus.primaryStage.getHeight() / 10 + 2 * graphicalMenus.primaryStage.getWidth() / 5);
        centerMenu.setSpacing(rest / 3);
        centerMenu.setTranslateY(rest / 3);

        goToUpdateMenu = (e) -> {
            updateManager.checkUpdates();
            graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getUpdateMenu());
        };

        I18NButton optionButton = createTopBarI18NButton(
                translator,
                "Options",
                "images/settings_white.png",
                (e) -> graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getOptionsMenu())
        );

        I18NButton updateButton = createTopBarI18NButton(
                translator,
                "Votre syst\u00e8me est \u00e0 jour",
                "images/refresh.png",
                goToUpdateMenu
        );

        checkUpdatesAndAdjustButton(updateButton,configuration);

        HBox menuBar = createMenuBar(configuration);

        closeMenuButton = createCloseMenuButton();
        centerMenu.getChildren().addAll(closeMenuButton, menuBar);
        this.setCenter(centerMenu);

        showCloseProcessButtonIfProcessNotNull();

        I18NButton tobiiButton2 = createTopBarI18NButton(translator,
                "Calibration",
                "images/eye-tracking_white.png",
                (e) -> {

                    if (Arrays.toString(Tobii.gazePosition()).equals(tobiiNotConnected)){
                        StageUtils.killRunningProcess(graphicalMenus);
                        TobiiManagerNamedProcessCreator tobiiManagerProcess = new TobiiManagerNamedProcessCreator();
                        tobiiManagerProcess.setUpProcessBuilder();
                        graphicalMenus.process = tobiiManagerProcess.start(graphicalMenus);
                    }
                    else {
                        interAACtionGazeNamedProcessCreator.start();
                    }
                }
        );


        ProgressDoubleClickedButtonI18N exitButton = createExitTopBarButton(
                translator,
                "Exit",
                "images/on-off-button_white.png",
                (e) -> {
                    this.graphicalMenus.primaryStage.getScene().setRoot(new ExitMenu(graphicalMenus,main,configuration));
                },
                configuration
        );

        exitButton.setOnMouseEntered(e -> {
            if(configuration.isGazeInteraction()){
                exitButton.assignIndicator((ev) -> this.graphicalMenus.primaryStage.getScene().setRoot(new ExitMenu(graphicalMenus,main,configuration)));
            }else{
                exitButton.deactivate();
            }
        });

        exitButton.start();

        StackPane optionBar = createOptionBar(optionButton, updateButton,tobiiButton2, exitButton);
        this.setTop(optionBar);

        ((TobiiGazeDeviceManager) graphicalMenus.getGazeDeviceManager()).init(graphicalMenus.getConfiguration());
    }


    private StackPane createOptionBar(Button optionButton, Button updateButton,Button tobiiButton2, StackPane exitButton) {
        StackPane titlePane = new StackPane();
        Rectangle backgroundForTitle = new Rectangle(0, 0, 600, 50);
        backgroundForTitle.setHeight(graphicalMenus.primaryStage.getHeight() / 10);
        backgroundForTitle.setWidth(graphicalMenus.primaryStage.getWidth());
        backgroundForTitle.setFill(Color.web("#cd2653"));


        optionButton.setPrefHeight(graphicalMenus.primaryStage.getHeight() / 10);
        updateButton.setPrefHeight(graphicalMenus.primaryStage.getHeight() / 10);
        tobiiButton2.setPrefHeight(graphicalMenus.primaryStage.getHeight() / 10);
        exitButton.setPrefHeight(graphicalMenus.primaryStage.getHeight() / 10);


        Label title = new Label("InteraactionBox-AFSR");
        title.setFont(new Font(30));
        title.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        title.setTextFill(Color.web("#faeaed"));

        BorderPane titleBox = new BorderPane();
        titleBox.setCenter(title);
        titleBox.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);


        titleBox.setLeft(new HBox(optionButton, updateButton));
        titleBox.setRight(new HBox(tobiiButton2, exitButton));

        BorderPane.setAlignment(titlePane, Pos.CENTER_LEFT);

        return titlePane;
    }

    private void checkUpdatesAndAdjustButton(Button updateButton, Configuration configuration) {
        updateManager.anyUpdateNeeded.addListener((obs, oldval, newval) -> {
            Platform.runLater(() -> {
                if (UtilsOS.isConnectedToInternet()) {
                    if (newval) {
                        updateButton.setOpacity(1);
                        if(configuration.getLanguage().equals("fra")){
                            updateButton.setText("Mise \u00e0 jour disponible !");
                        }else{
                            updateButton.setText("Update available!");
                        }
                        updateButton.setDisable(false);
                        Timeline t = new Timeline();
                        t.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(updateButton.opacityProperty(), 0.2)));
                        t.setCycleCount(20);
                        t.setAutoReverse(true);
                        t.play();
                    } else {
                        updateButton.setOpacity(1);
                        if(configuration.getLanguage().equals("fra")) {
                            updateButton.setText("Votre syst\u00e8me est \u00e0 jour");
                        }else{
                            updateButton.setText("Your system is up to date");
                        }
                        updateButton.setDisable(false);
                    }
                } else {
                    updateButton.setOpacity(0.5);
                    if(configuration.getLanguage().equals("fra")) {
                        updateButton.setText("Connection Introuvable");
                    }else{
                        updateButton.setText("Connection Not Found");
                    }
                    updateButton.setDisable(true);
                }
            });
        });
    }

    I18NButton createTopBarI18NButton(Translator translator, String text, String imagePath, EventHandler eventhandler) {
        return UtilsUI.getDoubleClickedI18NButton(translator,text, imagePath, eventhandler, graphicalMenus.primaryStage);
    }

    ProgressDoubleClickedButtonI18N createExitTopBarButton(Translator translator,String text, String imagePath, EventHandler eventhandler, Configuration configuration) {

        return new ProgressDoubleClickedButtonI18N(translator, text, imagePath, eventhandler, graphicalMenus.primaryStage, configuration);
    }

    private HBox createMenuBar(Configuration configuration) {
        HBox menuBar = new HBox(
                createAppButtonLauncher(new AugComNamedProcessCreator(), "AugCom", "images/Logos_AugCom.png", configuration),
                createAppButtonLauncher(new InterAACtionSceneNamedProcessCreator(), "InterAACtionScene", "images/VisuelSceneDisplay.png", configuration),
                createAppButtonLauncher(new GazePlayNamedProcessCreator(), "GazePlay", "images/gazeplayicon.png", configuration),
                createAppButtonLauncher(new InterAACtionPlayerNamedProcessCreator(), "InterAACtionPlayer", "images/gazeMediaPlayer.png", configuration)
        );

        menuBar.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(menuBar, Pos.CENTER);
        menuBar.spacingProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(4 * (menuBar.getChildren().size() + 1)));
        return menuBar;
    }

    private StackPane createAppButtonLauncher(AppNamedProcessCreator processCreator, String name, String imageURL, Configuration configuration) {
        I18NProgressButton processButton = processCreator.createButton(new Image(imageURL), graphicalMenus,configuration);
        processButton.getLabel().setText(name);
        processButton.getLabel().setFont(new Font(20));
        processButton.getButton().setStroke(Color.web("#cd2653"));
        processButton.getButton().setStrokeWidth(3);
        StackPane borderPaneLauncher = new StackPane();
        borderPaneLauncher.getChildren().add(processButton);
        graphicalMenus.getGazeDeviceManager().addEventFilter(processButton.getButton());
        ImageView downnloadImageView = UtilsUI.getDownloadImageView(processButton);

        if (name.equals(updateManager.updateServices[UpdateService.AUGCOM].getName())) {
            updateLaunchButtonIfExist(borderPaneLauncher, processButton, downnloadImageView, updateManager.updateServices[UpdateService.AUGCOM].getExistProperty().getValue());
            updateManager.updateServices[UpdateService.AUGCOM].getExistProperty().addListener((obj, oldval, newval) -> {
                updateLaunchButtonIfExist(borderPaneLauncher, processButton, downnloadImageView, newval);
            });
        } else if (name.equals(updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getName())) {
            updateLaunchButtonIfExist(borderPaneLauncher, processButton, downnloadImageView, updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getExistProperty().getValue());
            updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getExistProperty().addListener((obj, oldval, newval) -> {
                updateLaunchButtonIfExist(borderPaneLauncher, processButton, downnloadImageView, newval);
            });
        } else if (name.equals(updateManager.updateServices[UpdateService.GAZEPLAY].getName())) {
            updateLaunchButtonIfExist(borderPaneLauncher, processButton, downnloadImageView, updateManager.updateServices[UpdateService.GAZEPLAY].getExistProperty().getValue());
            updateManager.updateServices[UpdateService.GAZEPLAY].getExistProperty().addListener((obj, oldval, newval) -> {
                updateLaunchButtonIfExist(borderPaneLauncher, processButton, downnloadImageView, newval);
            });
        } else if (name.equals(updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getName())) {
            updateLaunchButtonIfExist(borderPaneLauncher, processButton, downnloadImageView, updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getExistProperty().getValue());
            updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getExistProperty().addListener((obj, oldval, newval) -> {
                updateLaunchButtonIfExist(borderPaneLauncher, processButton, downnloadImageView, newval);
            });
            startMouseListener();
        }

        return borderPaneLauncher;
    }

    private void updateLaunchButtonIfExist(StackPane borderPaneLauncher, I18NProgressButton processButton, ImageView downnloadImageView, boolean needsUpdate) {

        if (needsUpdate) {
            Platform.runLater(() -> {
                processButton.stop();
                processButton.setOpacity(0.5);
                borderPaneLauncher.getChildren().add(downnloadImageView);
                borderPaneLauncher.setOnMouseClicked(goToUpdateMenu);
            });
        } else {
            Platform.runLater(() -> {
                processButton.start();
                processButton.setOpacity(1);
                borderPaneLauncher.getChildren().remove(downnloadImageView);
                borderPaneLauncher.setOnMouseClicked(null);
            });
        }
    }

    private void startMouseListener() {
        Thread t = new Thread(() -> {
            while (true) {
                checkMouse();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    synchronized private void checkMouse() {
        PointerInfo pointer = MouseInfo.getPointerInfo();
        Point pointerLocation = pointer.getLocation();
        int x = (int) pointerLocation.getX();
        int y = (int) pointerLocation.getY();
        if ((x > 500) && (x < Screen.getPrimary().getBounds().getWidth() - 500) && (y <= 50) &&
                (!UtilsOS.isUnix() && !graphicalMenus.primaryStage.isShowing() || (UtilsOS.isUnix() && !graphicalMenus.primaryStage.isShowing()))
        ) {
            Platform.runLater(() -> {
                this.takeSnapShot();
                graphicalMenus.getHomeScreen().showCloseProcessButtonIfProcessNotNull();
                graphicalMenus.primaryStage.requestFocus();
                graphicalMenus.primaryStage.show();
                graphicalMenus.primaryStage.requestFocus();
                ActivateMainWindowProcess.start();
            });

        }
    }

    private void takeSnapShot() {
        Thread t = new Thread(() -> {
            try {
                Robot robot = new Robot();
                BufferedImage bufi = robot.createScreenCapture(new java.awt.Rectangle(0, 0, (int) this.graphicalMenus.primaryStage.getWidth(), (int) this.graphicalMenus.primaryStage.getHeight()));
                Platform.runLater(() -> {
                    ImageView img = new ImageView(UtilsUI.convertToFxImage(bufi));
                    img.fitWidthProperty().bind(closeMenuButton.getButton().radiusProperty().multiply(1.2));
                    img.fitHeightProperty().bind(closeMenuButton.getButton().radiusProperty().multiply(1.2));
                    img.setPreserveRatio(true);
                    closeMenuButton.setImage(img);
                });
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public I18NProgressButton createCloseMenuButton() {
        I18NProgressButton closeButton = new I18NProgressButton();
        closeButton.getButton().setRadius(graphicalMenus.primaryStage.getWidth() / 15);
        closeButton.getButton().setStroke(Color.web("#cd2653"));
        closeButton.getButton().setStrokeWidth(3);

        closeButton.assignIndicator((e) -> {
            graphicalMenus.primaryStage.hide();
        });

        closeButton.start();
        graphicalMenus.getGazeDeviceManager().addEventFilter(closeButton.getButton());

        return closeButton;
    }

    public void showCloseProcessButtonIfProcessNotNull() {
        if (graphicalMenus.process != null && graphicalMenus.process.get() != null && !centerMenu.getChildren().contains(closeMenuButton)) {
            double rest = graphicalMenus.primaryStage.getHeight() - (graphicalMenus.primaryStage.getHeight() / 10 + 2 * graphicalMenus.primaryStage.getWidth() / 5);
            centerMenu.setSpacing(rest / 2);
            centerMenu.setTranslateY(rest / 2);
            centerMenu.getChildren().add(0, closeMenuButton);
            closeMenuButton.getLabel().setText("Retourner sur :\n" + graphicalMenus.process.getName());
        } else if (graphicalMenus.process == null || graphicalMenus.process.get() == null) {
            double rest = graphicalMenus.primaryStage.getHeight() - (graphicalMenus.primaryStage.getHeight() / 10 + graphicalMenus.primaryStage.getWidth() / 5);
            double spacingoffset = (graphicalMenus.primaryStage.getHeight() - (graphicalMenus.primaryStage.getHeight() / 10 + 2 * graphicalMenus.primaryStage.getWidth() / 5)) / 3;
            centerMenu.setTranslateY(rest / 3 + spacingoffset);
            removeMenu();
        }
    }

    public void removeMenu() {
        centerMenu.getChildren().remove(closeMenuButton);
    }

}

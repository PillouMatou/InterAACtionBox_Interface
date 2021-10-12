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
import main.UI.ProgressButton;
import main.gaze.devicemanager.TobiiGazeDeviceManager;
import main.process.*;
import main.process.xdotoolProcess.ActivateMainWindowProcess;
import main.utils.*;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class HomeScreen extends BorderPane {

    private final GraphicalMenus graphicalMenus;
    private final ProgressButton closeMenuButton;
    private final VBox centerMenu;
    private UpdateManager updateManager;

    public HomeScreen(GraphicalMenus graphicalMenus, UpdateManager updateManager) {
        super();
        this.graphicalMenus = graphicalMenus;
        this.updateManager = updateManager;

        this.getChildren().add(UtilsUI.createBackground(graphicalMenus));

        centerMenu = new VBox();

        centerMenu.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(centerMenu, Pos.CENTER);
        centerMenu.spacingProperty().bind(graphicalMenus.primaryStage.heightProperty().divide(6));
        centerMenu.translateYProperty().bind(graphicalMenus.primaryStage.heightProperty().divide(5));

        Button optionButton = createTopBarButton(
                "Options",
                "images/settings_white.png",
                (e) -> graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getOptionsMenu())
        );

        Button updateButton = createTopBarButton(
                "Votre système est à jour",
                "images/refresh.png",
                (e) -> graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getUpdateMenu())
        );

        checkUpdatesAndAdjustButton(updateButton);

        HBox menuBar = createMenuBar();

        closeMenuButton = createCloseMenuButton();
        centerMenu.getChildren().addAll(closeMenuButton, menuBar);
        this.setCenter(centerMenu);

        showCloseProcessButtonIfProcessNotNull();

        Button tobiiButton = createTopBarButton(
                "Tobii Manager",
                "images/eye-tracking_white.png",
                (e) -> {
                    StageUtils.killRunningProcess(graphicalMenus);
                    TobiiManagerNamedProcessCreator tobiiManagerProcess = new TobiiManagerNamedProcessCreator();
                    tobiiManagerProcess.setUpProcessBuilder();
                    graphicalMenus.process = tobiiManagerProcess.start(graphicalMenus);
                    showCloseProcessButtonIfProcessNotNull();
                }
        );

        Button exitButton = createTopBarButton(
                "Exit",
                "images/on-off-button_white.png",
                (e) -> {
                    this.graphicalMenus.primaryStage.getScene().setRoot(new ExitMenu(graphicalMenus));
                }
        );

        StackPane optionBar = createOptionBar(optionButton, updateButton, tobiiButton, exitButton);
        this.setTop(optionBar);

        ((TobiiGazeDeviceManager) graphicalMenus.getGazeDeviceManager()).init(graphicalMenus.getConfiguration());

        startMouseListener();
    }

    private StackPane createOptionBar(Button optionButton, Button updateButton, Button tobiiButton, Button exitButton) {
        StackPane titlePane = new StackPane();
        Rectangle backgroundForTitle = new Rectangle(0, 0, 600, 50);
        backgroundForTitle.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        backgroundForTitle.setFill(Color.web("#cd2653"));

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
        titleBox.setRight(new HBox(tobiiButton, exitButton));

        BorderPane.setAlignment(titlePane, Pos.CENTER_LEFT);

        return titlePane;
    }

    private void checkUpdatesAndAdjustButton(Button updateButton) {
        updateManager.anyUpdateNeeded.addListener((obs, oldval, newval) -> {
            if (UtilsOS.isConnectedToInternet()) {
                if (newval) {
                    updateButton.setOpacity(1);
                    updateButton.setText("Mise à jour disponible !");
                    updateButton.setDisable(false);
                    Timeline t = new Timeline();
                    t.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(updateButton.opacityProperty(), 0.2)));
                    t.setCycleCount(20);
                    t.setAutoReverse(true);
                    t.play();
                } else {
                    updateButton.setOpacity(1);
                    updateButton.setText("Votre système est à jour");
                    updateButton.setDisable(false);
                }
            } else {
                updateButton.setOpacity(0.5);
                updateButton.setText("Connection Introuvable");
                updateButton.setDisable(true);
            }
        });
    }

    Button createTopBarButton(String text, String imagePath, EventHandler eventhandler) {
        return UtilsUI.getDoubleClickedButton(text, imagePath, eventhandler, 30);
    }

    private HBox createMenuBar() {
        HBox menuBar = new HBox(
                createAppButtonLauncher(new AugComNamedProcessCreator(), "AugCom", "images/Logos_AugCom.png"),
                createAppButtonLauncher(new InterAACtionSceneNamedProcessCreator(), "InterAACtionScene", "images/VisuelSceneDisplay.png"),
                createAppButtonLauncher(new GazePlayNamedProcessCreator(), "GazePlay", "images/gazeplayicon.png"),
                createAppButtonLauncher(new InterAACtionPlayerNamedProcessCreator(), "InterAACtionPlayer", "images/gazeMediaPlayer.png")
        );

        menuBar.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(menuBar, Pos.CENTER);
        menuBar.spacingProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(4 * (menuBar.getChildren().size() + 1)));
        return menuBar;
    }

    private StackPane createAppButtonLauncher(AppNamedProcessCreator processCreator, String name, String imageURL) {
        ProgressButton processButton = processCreator.createButton(new Image(imageURL), graphicalMenus);
        processButton.getLabel().setText(name);
        processButton.getButton().setStroke(Color.web("#cd2653"));
        processButton.getButton().setStrokeWidth(3);
        StackPane borderPaneLauncher = new StackPane();
        borderPaneLauncher.getChildren().add(processButton);
        graphicalMenus.getGazeDeviceManager().addEventFilter(processButton.getButton());
        ImageView downnloadImageView = UtilsUI.getDownloadImageView(processButton);

        if (name.equals(updateManager.updateServices[UpdateService.AUGCOM].getName())) {
            updateManager.updateServices[UpdateService.AUGCOM].getUpdateProperty().addListener((obj, oldval, newval) -> {
                updateLaunchButton(borderPaneLauncher, processButton, downnloadImageView, newval);
            });
        } else if (name.equals(updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getName())) {
            updateManager.updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty().addListener((obj, oldval, newval) -> {
                updateLaunchButton(borderPaneLauncher, processButton, downnloadImageView, newval);
            });
        } else if (name.equals(updateManager.updateServices[UpdateService.GAZEPLAY].getName())) {
            updateManager.updateServices[UpdateService.GAZEPLAY].getUpdateProperty().addListener((obj, oldval, newval) -> {
                updateLaunchButton(borderPaneLauncher, processButton, downnloadImageView, newval);
            });
        } else if (name.equals(updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getName())) {
            updateManager.updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty().addListener((obj, oldval, newval) -> {
                updateLaunchButton(borderPaneLauncher, processButton, downnloadImageView, newval);
            });
        }

        return borderPaneLauncher;
    }

    private void updateLaunchButton(StackPane borderPaneLauncher, ProgressButton processButton, ImageView downnloadImageView, boolean needsUpdate) {
//        if (needsUpdate) {
//            processButton.stop();
//            processButton.setOpacity(0.5);
//            borderPaneLauncher.getChildren().add(downnloadImageView);
//        } else {
//            processButton.start();
//            processButton.setOpacity(1);
//            borderPaneLauncher.getChildren().remove(downnloadImageView);
//        }
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

    public ProgressButton createCloseMenuButton() {
        ProgressButton closeButton = new ProgressButton();

        closeButton.getButton().radiusProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(1d / 12d));
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
            centerMenu.translateYProperty().unbind();
            centerMenu.translateYProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(1d / 12d));
            centerMenu.getChildren().add(0, closeMenuButton);
            closeMenuButton.getLabel().setText("Back To :\n" + graphicalMenus.process.getName());
        } else if (graphicalMenus.process == null || graphicalMenus.process.get() == null) {
            centerMenu.translateYProperty().unbind();
            centerMenu.translateYProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(4d / 12d));
            removeMenu();
        }
    }

    public void removeMenu() {
        centerMenu.getChildren().remove(closeMenuButton);
    }

}

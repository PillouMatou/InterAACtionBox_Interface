package main.UI.menu;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import lombok.extern.slf4j.Slf4j;
import main.UI.DoubleClickedButton;
import main.UI.ProgressButton;
import main.gaze.devicemanager.TobiiGazeDeviceManager;
import main.process.*;
import main.process.xdotoolProcess.ActivateMainWindowProcess;
import main.utils.UtilsOS;

import java.awt.*;
import java.awt.image.BufferedImage;

@Slf4j
public class HomeScreen extends BorderPane {

    private final GraphicalMenus graphicalMenus;
    private final ProgressButton closeMenuButton;
    private final VBox centerMenu;


    public HomeScreen(GraphicalMenus graphicalMenus) {
        super();
        this.graphicalMenus = graphicalMenus;

        Rectangle r = new Rectangle();
        r.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        r.heightProperty().bind(graphicalMenus.primaryStage.heightProperty());
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#faeaed")), new Stop(1, Color.web("#cd2653"))};
        LinearGradient lg1 = new LinearGradient(0, 1, 1.5, 0, true, CycleMethod.NO_CYCLE, stops);
        r.setFill(lg1);

        this.getChildren().add(r);

        centerMenu = new VBox();

        centerMenu.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(centerMenu, Pos.CENTER);
        centerMenu.spacingProperty().bind(graphicalMenus.primaryStage.heightProperty().divide(6));
        centerMenu.translateYProperty().bind(graphicalMenus.primaryStage.heightProperty().divide(5));

        HBox menuBar = createMenuBar(graphicalMenus.getGazePlayInstallationRepo());

        closeMenuButton = createCloseMenuButton();
        centerMenu.getChildren().addAll(closeMenuButton, menuBar);
        this.setCenter(centerMenu);

        showCloseMenuIfProcessNotNull();

        StackPane titlePane = new StackPane();
        javafx.scene.shape.Rectangle backgroundForTitle = new Rectangle(0, 0, 600, 50);
        backgroundForTitle.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        backgroundForTitle.setFill(Color.web("#cd2653"));

        javafx.scene.control.Label title = new Label("InteraactionBox");
        title.setFont(new Font(30));
        title.setStyle("-fx-font-weight: bold; -fx-font-family: Helvetica");
        title.setTextFill(Color.web("#faeaed"));

        Button optionButton = createTopBarButton(
                "Options",
                "images/settings_white.png",
                (e) -> graphicalMenus.getConfiguration().scene.setRoot(graphicalMenus.getOptionsMenu())
        );

        Button tobiiButton = createTopBarButton(
                "Tobii Manager",
                "images/eye-tracking_white.png",
                (e) -> {
                    if (graphicalMenus.process.get() != null) {
                        graphicalMenus.process.exitAskedByUser = true;
                        graphicalMenus.process.destroy();
                        graphicalMenus.process.set(null);
                    }
                    TobiiManagerNamedProcessCreator tobiiManagerProcess = new TobiiManagerNamedProcessCreator();
                    tobiiManagerProcess.setUpProcessBuilder();
                    graphicalMenus.process = tobiiManagerProcess.start(graphicalMenus);
                    showCloseMenuIfProcessNotNull();
                }
        );

        Button exitButton = createTopBarButton(
                "Exit",
                "images/on-off-button_white.png",
                (e) -> {
                    this.graphicalMenus.primaryStage.getScene().setRoot(new ExitMenu(graphicalMenus));
                }
        );

        BorderPane titleBox = new BorderPane();
        titleBox.setLeft(optionButton);
        titleBox.setCenter(title);
        titleBox.setRight(new HBox(tobiiButton, exitButton));
        titleBox.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);

        BorderPane.setAlignment(titlePane, Pos.CENTER_LEFT);
        this.setTop(titlePane);

        ((TobiiGazeDeviceManager) graphicalMenus.getGazeDeviceManager()).init(graphicalMenus.getConfiguration());
        startMouseListener();
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    Button createTopBarButton(String text, String imagePath, EventHandler eventhandler) {
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
        graphic.setFitHeight(30);
        optionButton.setGraphic(graphic);

        optionButton.assignHandler(eventhandler);
        return optionButton;
    }

    private HBox createMenuBar(String gazePlayInstallationRepo) {
        AugComNamedProcessCreator augComProcess = new AugComNamedProcessCreator();
        InterAACtionSceneNamedProcessCreator interaactionSceneProcess = new InterAACtionSceneNamedProcessCreator();
        GazePlayNamedProcessCreator gazePlayProcess = new GazePlayNamedProcessCreator(gazePlayInstallationRepo);
        InterAACtionPlayerNamedProcessCreator interAACtionPlayerProcess = new InterAACtionPlayerNamedProcessCreator();

        ProgressButton augComProcessButton = augComProcess.createButton(new Image("images/angular.png"), graphicalMenus);
        augComProcessButton.getLabel().setText("AugCom");
        ProgressButton interaactionSceneProcessButton = interaactionSceneProcess.createButton(new Image("images/angular.png"), graphicalMenus);
        interaactionSceneProcessButton.getLabel().setText("InteraactionScene");
        ProgressButton gazePlayProcessButton = gazePlayProcess.createButton(new Image("images/gazeplayicon.png"), graphicalMenus);
        gazePlayProcessButton.getLabel().setText("GazePlay");
        ProgressButton interAACtionPlayerProcessButton = interAACtionPlayerProcess.createButton(new Image("images/gazeMediaPlayer.png"), graphicalMenus);
        interAACtionPlayerProcessButton.getLabel().setText("GazeMediaPLayer");

        augComProcessButton.getButton().setStroke(Color.web("#cd2653"));
        augComProcessButton.getButton().setStrokeWidth(3);
        interaactionSceneProcessButton.getButton().setStroke(Color.web("#cd2653"));
        interaactionSceneProcessButton.getButton().setStrokeWidth(3);
        gazePlayProcessButton.getButton().setStroke(Color.web("#cd2653"));
        gazePlayProcessButton.getButton().setStrokeWidth(3);
        interAACtionPlayerProcessButton.getButton().setStroke(Color.web("#cd2653"));
        interAACtionPlayerProcessButton.getButton().setStrokeWidth(3);

        HBox menuBar = new HBox(
                augComProcessButton,
                interaactionSceneProcessButton,
                gazePlayProcessButton,
                interAACtionPlayerProcessButton
        );

        graphicalMenus.getGazeDeviceManager().addEventFilter(augComProcessButton.getButton());
        graphicalMenus.getGazeDeviceManager().addEventFilter(interaactionSceneProcessButton.getButton());
        graphicalMenus.getGazeDeviceManager().addEventFilter(gazePlayProcessButton.getButton());
        graphicalMenus.getGazeDeviceManager().addEventFilter(interAACtionPlayerProcessButton.getButton());

        menuBar.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(menuBar, Pos.CENTER);
        menuBar.spacingProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(4 * (menuBar.getChildren().size() + 1)));
        return menuBar;
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
        log.info("{} : {}, {}",x,y, UtilsOS.isUnix());
        if (x > 500 &&
                x < Screen.getPrimary().getBounds().getWidth() - 500 &&
                y <= 50 &&
                (!UtilsOS.isUnix() || (UtilsOS.isUnix() && !graphicalMenus.primaryStage.isShowing()))
        ) {
            log.info("start platform");
            Platform.runLater(() -> {
                this.takeSnapShot();
                log.info("snapshot");
                graphicalMenus.getHomeScreen().showCloseMenuIfProcessNotNull();
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
                    ImageView img = new ImageView(convertToFxImage(bufi));
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

    public void showCloseMenuIfProcessNotNull() {
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

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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import lombok.extern.slf4j.Slf4j;
import main.UI.ProgressButton;
import main.gaze.devicemanager.TobiiGazeDeviceManager;
import main.process.*;

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

        ImageView backgroundBlured = new ImageView(new Image("images/blured.jpg"));

        backgroundBlured.setOpacity(1);

        backgroundBlured.fitWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        backgroundBlured.fitHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

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
        // backgroundForTitle.setOpacity(0.3);
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

        Button wifiButton = createTopBarButton(
                "Wi-Fi",
                "images/cross.png",
                (e) -> {
                    if (graphicalMenus.process.get() != null) {
                        graphicalMenus.process.destroy();
                    }
                    WifiProcess wifiProcess = new WifiProcess();
                    wifiProcess.setUpProcessBuilder();
                    graphicalMenus.process = wifiProcess.start(graphicalMenus);
                }
        );

        Button tobiiButton = createTopBarButton(
                "Tobii Manager",
                "images/cross.png",
                (e) -> {
                    if (graphicalMenus.process.get() != null) {
                        graphicalMenus.process.destroy();
                    }
                    TobiiManagerProcess tobiiManagerProcess = new TobiiManagerProcess();
                    tobiiManagerProcess.setUpProcessBuilder();
                    graphicalMenus.process = tobiiManagerProcess.start(graphicalMenus);
                }
        );

        Button exitButton = createTopBarButton(
                "Exit",
                "images/cross.png",
                (e) -> {
                    if (graphicalMenus.process.get() != null) {
                        graphicalMenus.process.destroy();
                        graphicalMenus.process.set(null);
                    }
                    Platform.exit();
                    System.exit(0);
                }
        );

        BorderPane titleBox = new BorderPane();
        titleBox.setLeft(optionButton);
        titleBox.setCenter(title);
        titleBox.setRight(new HBox(tobiiButton, wifiButton, exitButton));
        titleBox.prefWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);

        BorderPane.setAlignment(titlePane, Pos.CENTER);
        this.setTop(titlePane);

        ((TobiiGazeDeviceManager) graphicalMenus.getGazeDeviceManager()).init(graphicalMenus.getConfiguration());
        startMouseListener();
    }

    private Button createTopBarButton(String text, String imagePath, EventHandler eventhandler) {
        Button optionButton = new Button(text);
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
        optionButton.setOnMouseClicked(eventhandler);
        return optionButton;
    }


    private HBox createMenuBar(String gazePlayInstallationRepo) {
        YoutubeProcess youtubeProcess = new YoutubeProcess();
        AugComProcess augComProcess = new AugComProcess();
        InteraactionSceneProcess interaactionSceneProcess = new InteraactionSceneProcess();
        GazePlayProcess gazePlayProcess = new GazePlayProcess(gazePlayInstallationRepo);

        ProgressButton youtubeProgressButton = youtubeProcess.createButton(new Image("images/yt.png"), graphicalMenus, "google-chrome");
        youtubeProgressButton.getLabel().setText("Youtube");
        ProgressButton augComProcessButton = augComProcess.createButton(new Image("images/angular.png"), graphicalMenus, "google-chrome");
        augComProcessButton.getLabel().setText("AugCom");
        ProgressButton interaactionSceneProcessButton = interaactionSceneProcess.createButton(new Image("images/angular.png"), graphicalMenus, "google-chrome");
        interaactionSceneProcessButton.getLabel().setText("InteraactionScene");
        ProgressButton gazePlayProcessButton = gazePlayProcess.createButton(new Image("images/gazeplayicon.png"), graphicalMenus, "gazeplay");
        gazePlayProcessButton.getLabel().setText("GazePlay");
        youtubeProgressButton.getButton().setStroke(Color.web("#cd2653"));
        youtubeProgressButton.getButton().setStrokeWidth(3);
        augComProcessButton.getButton().setStroke(Color.web("#cd2653"));
        augComProcessButton.getButton().setStrokeWidth(3);
        interaactionSceneProcessButton.getButton().setStroke(Color.web("#cd2653"));
        interaactionSceneProcessButton.getButton().setStrokeWidth(3);
        gazePlayProcessButton.getButton().setStroke(Color.web("#cd2653"));
        gazePlayProcessButton.getButton().setStrokeWidth(3);

        HBox menuBar = new HBox(
                youtubeProgressButton,
                augComProcessButton,
                interaactionSceneProcessButton,
                gazePlayProcessButton
        );
        graphicalMenus.getGazeDeviceManager().addEventFilter(youtubeProgressButton.getButton());
        graphicalMenus.getGazeDeviceManager().addEventFilter(augComProcessButton.getButton());
        graphicalMenus.getGazeDeviceManager().addEventFilter(interaactionSceneProcessButton.getButton());
        graphicalMenus.getGazeDeviceManager().addEventFilter(gazePlayProcessButton.getButton());

        menuBar.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(menuBar, Pos.CENTER);
        menuBar.spacingProperty().bind(this.widthProperty().divide(2 * (menuBar.getChildren().size() + 1)));
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
        if (x > 500 && x < Screen.getPrimary().getBounds().getWidth() - 500 && y <= 10) {
            Platform.runLater(() -> {
//               graphicalMenus.primaryStage.hide();
//                if (graphicalMenus.getQuickMenu().process != null) {
//                    graphicalMenus.getQuickMenu().process.destroy();
//                }
                this.takeSnapShot();
                graphicalMenus.getHomeScreen().showCloseMenuIfProcessNotNull();
                graphicalMenus.primaryStage.show();
                //StageUtils.displayUnclosable(graphicalMenus.getQuickMenu(), graphicalMenus.primaryStage);
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
                    img.fitWidthProperty().bind(closeMenuButton.widthProperty().divide(1.5));
                    img.fitHeightProperty().bind(closeMenuButton.heightProperty().divide(1.5));
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
        closeButton.prefWidthProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(3d / 12d));
        closeButton.maxWidthProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(3d / 12d));
        closeButton.minWidthProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(3d / 12d));
        closeButton.minHeightProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(3d / 12d));
        closeButton.minHeightProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(3d / 12d));
        closeButton.minHeightProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(3d / 12d));

        Circle shape = new Circle();
        shape.radiusProperty().bind(closeButton.heightProperty().divide(2));

        closeButton.setShape(shape);

        closeButton.setStyle(
                "-fx-border-color: #cd2653; " +
                        "-fx-border-width: 3; " +
                        "-fx-background-color: #faeaed; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-text-fill: #faeaed"
        );

        closeButton.setOnMouseClicked((e) -> {
            graphicalMenus.primaryStage.hide();
        });

//
//        DropShadow shadow = new DropShadow();
//        shadow.setOffsetX(0);
//        shadow.setOffsetY(10);
//        shadow.setRadius(50);
//        closeButton.setEffect(shadow);

        closeButton.setSpacing(20);

        return closeButton;
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

    public void showCloseMenuIfProcessNotNull() {
        if (graphicalMenus.process.get() != null && !centerMenu.getChildren().contains(closeMenuButton)) {
            centerMenu.translateYProperty().unbind();
            centerMenu.translateYProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(1d / 12d));
            centerMenu.getChildren().add(0, closeMenuButton);
            closeMenuButton.getLabel().setText("Back To :\n" + graphicalMenus.process.getName());
        } else if (graphicalMenus.process.get() == null) {
            centerMenu.translateYProperty().unbind();
            centerMenu.translateYProperty().bind(graphicalMenus.primaryStage.heightProperty().multiply(4d / 12d));
            centerMenu.getChildren().remove(closeMenuButton);
        }
    }

}

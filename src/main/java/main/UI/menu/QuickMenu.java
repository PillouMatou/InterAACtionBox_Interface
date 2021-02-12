package main.UI.menu;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.UI.ProgressButton;
import main.process.*;
import main.utils.UtilsOS;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.LinkedList;

public class QuickMenu extends Pane {


    final LinkedList<ProgressButton> buttons;
    final ProgressButton closeMenuButton;

    final GraphicalMenus graphicalMenus;

    public QuickMenu(GraphicalMenus graphicalMenus) {
        super();
        this.graphicalMenus = graphicalMenus;
        this.setStyle("-fx-background-color: transparent;");

//        backgroundBlured = new ImageView(new Image("images/blured.jpg"));
//
//        backgroundBlured.fitWidthProperty().bind(graphicalMenus.primaryStage.widthProperty());
//        backgroundBlured.fitHeightProperty().bind(graphicalMenus.primaryStage.heightProperty());

        Rectangle r = new Rectangle();
        r.widthProperty().bind(graphicalMenus.primaryStage.widthProperty());
        r.heightProperty().bind(graphicalMenus.primaryStage.heightProperty());
        Stop[] stops = new Stop[]{new Stop(0, Color.web("#faeaed")), new Stop(1, Color.web("#cd2653"))};
        LinearGradient lg1 = new LinearGradient(0, 1, 1.5, 0, true, CycleMethod.NO_CYCLE, stops);
        r.setFill(lg1);

        getChildren().add(r);
        setBackgroundListener(r);
        r.setOpacity(1);
        r.setOnMouseClicked((e) -> {
            graphicalMenus.primaryStage.hide();
        });

        closeMenuButton = createCloseMenuButton();
        getChildren().add(closeMenuButton);

        buttons = setButtons(graphicalMenus.primaryStage, graphicalMenus.getGazePlayInstallationRepo());
        createCircularButtons();
    }

    public ProgressButton createCloseMenuButton() {
        ProgressButton closeButton = new ProgressButton();
        closeButton.setPrefWidth(50);
        closeButton.setPrefHeight(50);

        closeButton.setShape(new Circle(50));

        closeButton.layoutXProperty().bind(graphicalMenus.primaryStage.widthProperty().divide(2).subtract(25));
        closeButton.layoutYProperty().bind(graphicalMenus.primaryStage.heightProperty().divide(2).subtract(25));
        ImageView logo = new ImageView(new Image("images/cross.png"));
        logo.setFitWidth(20);
        logo.setFitHeight(20);
        logo.setPreserveRatio(true);
        closeButton.setImage(logo);

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


        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(10);
        shadow.setRadius(50);
        closeButton.setEffect(shadow);

        return closeButton;
    }

    public void setBackgroundListener(Rectangle backgroundBlured) {
        backgroundBlured.setOnMouseMoved(event -> {

            for (ProgressButton button : buttons) {
                if (button != null) {
                    double buttonOrigin = Point2D.distance(
                            Screen.getPrimary().getBounds().getWidth() / 2,
                            Screen.getPrimary().getBounds().getHeight() / 2,
                            button.getLayoutX() + button.getPrefWidth() / 2,
                            button.getLayoutY() + button.getPrefHeight() / 2);
                    double mouseOrigin = Point2D.distance(
                            Screen.getPrimary().getBounds().getWidth() / 2,
                            Screen.getPrimary().getBounds().getHeight() / 2,
                            event.getX(),
                            event.getY());
                    double mouseButton = Point2D.distance(
                            button.getLayoutX() + button.getPrefWidth() / 2,
                            button.getLayoutY() + button.getPrefHeight() / 2,
                            event.getX(),
                            event.getY());

                    if (mouseButton < buttonOrigin) {
                        double factor = (mouseOrigin / buttonOrigin) > 1 ? 1 : (mouseOrigin / buttonOrigin);
                        button.setPrefWidth(150 + 50 * factor);
                        button.setPrefHeight(150 + 50 * factor);
                        button.getButton().setRadius(75 + 25 * factor);
                    } else {
                        button.setPrefWidth(150);
                        button.setPrefHeight(150);
                        button.getButton().setRadius(75);
                    }
                }
            }
        });
    }

    public void createCircularButtons() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setPrefWidth(150);
            buttons.get(i).setPrefHeight(150);
            buttons.get(i).getButton().setRadius(75);
            buttons.get(i).layoutXProperty().bind(closeMenuButton.layoutXProperty().add(250 * Math.cos(Math.toRadians(i * 360d / buttons.size()))).subtract(50));
            buttons.get(i).layoutYProperty().bind(closeMenuButton.layoutYProperty().add(250 * Math.sin(Math.toRadians(i * 360d / buttons.size()))).subtract(50));
            this.getChildren().add(buttons.get(i));

            int index = i;
            buttons.get(i).setOnMouseEntered(event -> {
                buttons.get(index).requestFocus();
            });
        }
    }


    public LinkedList<ProgressButton> setButtons(Stage primaryStage, String gazePlayInstallationRepo) {
        EventHandler<Event> eventhandler = null;
        ImageView logo;
        LinkedList<ProgressButton> buttons = new LinkedList<ProgressButton>();
        for (int i = 0; i < 7; i++) {
            ProgressButton progressButton = new ProgressButton();
            progressButton.getButton().setStroke(Color.web("#cd2653"));
            progressButton.getButton().setStrokeWidth(3);
            DropShadow shadow = new DropShadow();
            shadow.setOffsetX(0);
            shadow.setOffsetY(10);
            shadow.setRadius(50);
            progressButton.setEffect(shadow);
            buttons.add(progressButton);
            switch (i) {
                case 0:
                    buttons.get(i).getLabel().setText("Exit");
                    eventhandler = e -> {

                        if (graphicalMenus.process.get() != null) {
                            graphicalMenus.process.destroy();
                            graphicalMenus.process.set(null);
                        }
                        Platform.exit();
                        System.exit(1);

                    };
                    break;
                case 1:
                    buttons.get(i).getLabel().setText("Menu");
                    eventhandler = (event) -> {
                        if (graphicalMenus.process.get() != null) {
                            graphicalMenus.process.destroy();
                            graphicalMenus.process.set(null);
                        }
                        graphicalMenus.getHomeScreen().showCloseMenuIfProcessNotNull();
                        primaryStage.show();
                        primaryStage.toFront();
                        primaryStage.getScene().setRoot(graphicalMenus.getHomeScreen());
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
                        if (graphicalMenus.process.get() != null) {
                            graphicalMenus.process.destroy();
                            graphicalMenus.process.set(null);
                        }
                        AugComNamedProcessCreator augComProcess = new AugComNamedProcessCreator();
                        initAndStartProcess(augComProcess);
                    };
                    break;
                case 3:
                    buttons.get(i).getLabel().setText("InteraactionScene");
                    logo = new ImageView(new Image("images/angular.png"));
                    logo.setPreserveRatio(true);
                    logo.setFitWidth(100);
                    logo.setFitHeight(100);
                    buttons.get(i).setImage(logo);
                    eventhandler = e -> {
                        if (graphicalMenus.process.get() != null) {
                            graphicalMenus.process.destroy();
                            graphicalMenus.process.set(null);
                        }
                        InteraactionSceneNamedProcessCreator interaactionSceneProcess = new InteraactionSceneNamedProcessCreator();
                        initAndStartProcess(interaactionSceneProcess);
                    };
                    break;
                case 4:
                    buttons.get(i).getLabel().setText("Youtube");
                    logo = new ImageView(new Image("images/yt.png"));
                    logo.setPreserveRatio(true);
                    logo.setFitWidth(100);
                    logo.setFitHeight(100);
                    buttons.get(i).setImage(logo);
                    eventhandler = e -> {
                        if (graphicalMenus.process.get() != null) {
                            graphicalMenus.process.destroy();
                            graphicalMenus.process.set(null);
                        }
                        YoutubeNamedProcessCreator youtubeProcess = new YoutubeNamedProcessCreator();
                        initAndStartProcess(youtubeProcess);
                    };
                    break;
                case 5:
                    buttons.get(i).getLabel().setText("GazePlay");
                    logo = new ImageView(new Image("images/gazeplayicon.png"));
                    logo.setPreserveRatio(true);
                    logo.setFitWidth(100);
                    logo.setFitHeight(100);
                    buttons.get(i).setImage(logo);
                    eventhandler = e -> {
                        if (graphicalMenus.process.get() != null) {
                            graphicalMenus.process.destroy();
                            graphicalMenus.process.set(null);
                        }
                        GazePlayNamedProcessCreator gazePlayProcess = new GazePlayNamedProcessCreator(gazePlayInstallationRepo);
                        initAndStartProcess(gazePlayProcess);
                    };
                    break;
                case 6:
                    buttons.get(i).getLabel().setText("Eteindre");
                    eventhandler = e -> {
//                        if (process != null) {
//                            process.destroy();
//                            process = null;
//                        }
//                        try {
//                            shutdown();
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
                    };
                    break;
                default:
                    break;
            }

            buttons.get(i).setOnMouseClicked(eventhandler);
            buttons.get(i).assignIndicator(eventhandler);
            buttons.get(i).start();
            graphicalMenus.getGazeDeviceManager().addEventFilter(buttons.get(i));
        }
        return buttons;
    }


    public void initAndStartProcess(AppNamedProcessCreator process) {
        process.setUpProcessBuilder();
        this.graphicalMenus.process = process.start(graphicalMenus);
    }

    public static void shutdown() throws RuntimeException, IOException {
        String shutdownCommand;

        if (UtilsOS.isUnix()) {
            shutdownCommand = "shutdown -h now";
        } else if (UtilsOS.isWindows()) {
            shutdownCommand = "shutdown.exe -s -t 0";
        } else {
            throw new RuntimeException("Unsupported operating system.");
        }

        Runtime.getRuntime().exec(shutdownCommand);
        Platform.exit();
        System.exit(0);
    }

}

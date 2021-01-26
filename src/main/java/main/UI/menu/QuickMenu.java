package main.UI.menu;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.UI.ProgressButton;
import main.gaze.devicemanager.AbstractGazeDeviceManager;
import main.process.*;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class QuickMenu extends Pane {


    public Process process;
    public AbstractGazeDeviceManager gazeDeviceManager;
    final Stage primaryStage;
    final HomeScreen homeScreen;

    final LinkedList<ProgressButton> buttons;

    final ImageView backgroundBlured;
    final Button closeMenuButton;

    public QuickMenu(Stage primaryStage, AbstractGazeDeviceManager gazeDeviceManager, String gazePlayInstallationRepo, HomeScreen homeScreen) {
        super();
        this.primaryStage = primaryStage;
        this.homeScreen = homeScreen;
        this.gazeDeviceManager = gazeDeviceManager;
        this.setStyle("-fx-background-color: transparent;");

        backgroundBlured = new ImageView(new Image("images/blured.jpg"));

        setOpacityBackground(1);

        backgroundBlured.fitWidthProperty().bind(this.primaryStage.widthProperty());
        backgroundBlured.fitHeightProperty().bind(this.primaryStage.heightProperty());

        getChildren().add(backgroundBlured);
        setBackgroundListener(backgroundBlured);
        backgroundBlured.setOpacity(0.5);

        closeMenuButton = createCloseMenuButton();
        getChildren().add(closeMenuButton);

        buttons = setButtons(primaryStage, gazePlayInstallationRepo);
        createCircularButtons();
    }

    public Button createCloseMenuButton() {
        Button closeButton = new Button();
        closeButton.setPrefWidth(50);
        closeButton.setPrefHeight(50);

        closeButton.setShape(new Circle(50));

        closeButton.layoutXProperty().bind(primaryStage.widthProperty().divide(2).subtract(25));
        closeButton.layoutYProperty().bind(primaryStage.heightProperty().divide(2).subtract(25));

        ImageView logo = new ImageView(new Image("images/cross.png"));
        logo.setFitWidth(20);
        logo.setFitHeight(20);
        logo.setPreserveRatio(true);
        closeButton.setGraphic(logo);

        return closeButton;
    }

    public void setOpacityBackground(double d) {
        backgroundBlured.setOpacity(d);
    }

    public void setBackgroundListener(ImageView backgroundBlured) {
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
        for (int i = 0; i < 6; i++) {
            buttons.add(new ProgressButton());
            switch (i) {
                case 0:
                    buttons.get(i).getLabel().setText("Exit");
                    eventhandler = e -> {
                        if (process != null) {
                            process.destroy();
                            process = null;
                        }
                        Platform.exit();
                        System.exit(0);
                    };
                    break;
                case 1:
                    buttons.get(i).getLabel().setText("Menu");
                    eventhandler = (event) -> {
                        if (process != null) {
                            process.destroy();
                            process = null;
                        }
                        primaryStage.show();
                        primaryStage.toFront();
                        primaryStage.getScene().setRoot(homeScreen);
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
                        if (process != null) {
                            process.destroy();
                            process = null;
                        }
                        AugComProcess augComProcess = new AugComProcess();
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
                        if (process != null) {
                            process.destroy();
                            process = null;
                        }
                        InteraactionSceneProcess interaactionSceneProcess = new InteraactionSceneProcess();
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
                        if (process != null) {
                            process.destroy();
                            process = null;
                        }
                        YoutubeProcess youtubeProcess = new YoutubeProcess();
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
                        if (process != null) {
                            process.destroy();
                            process = null;
                        }
                        GazePlayProcess gazePlayProcess = new GazePlayProcess(gazePlayInstallationRepo);
                        initAndStartProcess(gazePlayProcess);
                    };
                    break;

                default:
                    break;
            }

            buttons.get(i).setOnMouseClicked(eventhandler);
            buttons.get(i).assignIndicator(eventhandler);
            buttons.get(i).start();
            gazeDeviceManager.addEventFilter(buttons.get(i));
        }
        return buttons;
    }


    public void initAndStartProcess(AppProcess process) {
        process.setUpProcessBuilder();
        this.process = process.start();
        this.process.onExit().thenRunAsync(
                new Runnable() {
                    @Override
                    public void run() {
                        primaryStage.show();
                        primaryStage.toFront();
                    }
                }
        );
    }

}

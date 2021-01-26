package main.UI.menu;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Configuration;
import main.UI.ProgressButton;
import main.gaze.devicemanager.AbstractGazeDeviceManager;
import main.gaze.devicemanager.TobiiGazeDeviceManager;
import main.process.AugComProcess;
import main.process.GazePlayProcess;
import main.process.InteraactionSceneProcess;
import main.process.YoutubeProcess;
import main.utils.StageUtils;

import java.awt.*;

public class HomeScreen extends BorderPane {

    private Stage primaryStage;
    public QuickMenu quickMenu;
    AbstractGazeDeviceManager tgdm;
    final Configuration configuration;

    public HomeScreen(Configuration configuration, Stage primaryStage, String gazePlayInstallationRepo) {
        super();
        this.configuration = configuration;
        this.configuration.setHomeScreen(this);
        this.primaryStage = primaryStage;
        this.tgdm = new TobiiGazeDeviceManager();

        ImageView backgroundBlured = new ImageView(new Image("images/blured.jpg"));

        backgroundBlured.setOpacity(1);

        backgroundBlured.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundBlured.fitHeightProperty().bind(primaryStage.heightProperty());

        this.getChildren().add(backgroundBlured);

        createMenuPane(gazePlayInstallationRepo, this);
        HBox menuBar = createMenuBar(gazePlayInstallationRepo);

        this.setCenter(menuBar);


        StackPane titlePane = new StackPane();
        javafx.scene.shape.Rectangle backgroundForTitle = new Rectangle(0, 0, 600, 50);
        backgroundForTitle.widthProperty().bind(primaryStage.widthProperty());
        backgroundForTitle.setOpacity(0.3);

        javafx.scene.control.Label title = new Label("InteraactionBox");
        title.setFont(new Font(30));

        Button optionButton = new Button("Options");
        optionButton.setPrefHeight(50);
        optionButton.setOnMouseClicked((e) -> {
            configuration.scene.setRoot(configuration.optionsMenu);
        });

        HBox titleBox = new HBox(optionButton, title);
        title.prefWidthProperty().bind(primaryStage.widthProperty().subtract(optionButton.widthProperty().multiply(2)));
        titleBox.prefWidthProperty().bind(primaryStage.widthProperty());
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);
        titlePane.getChildren().addAll(backgroundForTitle, titleBox);

        BorderPane.setAlignment(titlePane, Pos.CENTER);
        this.setTop(titlePane);

        ((TobiiGazeDeviceManager) tgdm).init(configuration);
        startMouseListener();

    }


    private HBox createMenuBar(String gazePlayInstallationRepo) {
        YoutubeProcess youtubeProcess = new YoutubeProcess();
        AugComProcess augComProcess = new AugComProcess();
        InteraactionSceneProcess interaactionSceneProcess = new InteraactionSceneProcess();
        GazePlayProcess gazePlayProcess = new GazePlayProcess(gazePlayInstallationRepo);

        ProgressButton youtubeProgressButton = youtubeProcess.createButton(new Image("images/yt.png"), quickMenu);
        youtubeProgressButton.getLabel().setText("Youtube");
        ProgressButton augComProcessButton = augComProcess.createButton(new Image("images/angular.png"), quickMenu);
        augComProcessButton.getLabel().setText("AugCom");
        ProgressButton interaactionSceneProcessButton = interaactionSceneProcess.createButton(new Image("images/angular.png"), quickMenu);
        interaactionSceneProcessButton.getLabel().setText("InteraactionScene");
        ProgressButton gazePlayProcessButton = gazePlayProcess.createButton(new Image("images/gazeplayicon.png"), quickMenu);
        gazePlayProcessButton.getLabel().setText("GazePlay");
        HBox menuBar = new HBox(
                youtubeProgressButton,
                augComProcessButton,
                interaactionSceneProcessButton,
                gazePlayProcessButton
        );
        tgdm.addEventFilter(youtubeProgressButton.getButton());
        tgdm.addEventFilter(augComProcessButton.getButton());
        tgdm.addEventFilter(interaactionSceneProcessButton.getButton());
        tgdm.addEventFilter(gazePlayProcessButton.getButton());

        menuBar.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(menuBar, Pos.CENTER);

        menuBar.spacingProperty().bind(this.widthProperty().divide(10));
        return menuBar;
    }

    private void createMenuPane(String gazePlayInstallationRepo, HomeScreen homeScreen) {
        quickMenu = new QuickMenu(primaryStage, tgdm, gazePlayInstallationRepo, homeScreen);
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

        t.start();
    }

    synchronized private void checkMouse() {
        PointerInfo pointer = MouseInfo.getPointerInfo();
        Point pointerLocation = pointer.getLocation();
        int x = (int) pointerLocation.getX();
        int y = (int) pointerLocation.getY();
        if (x > 500 && x < Screen.getPrimary().getBounds().getWidth() - 500 && y <= 10) {
            Platform.runLater(() -> {
                primaryStage.hide();
                if (quickMenu.process != null) {
                    quickMenu.process.destroy();
                }
                StageUtils.displayUnclosable(quickMenu, primaryStage);
            });
        }
    }

}

package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.UI.menu.GraphicalMenus;
import main.utils.StageUtils;

import java.util.List;

public class Main extends Application {

    public String getGazePlayInstallationRepo() {
        Parameters parameters = getParameters();
        List<String> rawArguments = parameters.getRaw();
        if (rawArguments.size() > 0) {
            return rawArguments.get(0);
        } else {
            return "C:/Program Files (x86)/GazePlay";
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("InteraactionBox");

        GraphicalMenus graphicalMenus = new GraphicalMenus(primaryStage, getGazePlayInstallationRepo());
        Scene scene = new Scene(graphicalMenus.getHomeScreen(), Color.TRANSPARENT);
        graphicalMenus.getConfiguration().setScene(scene);
        primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight());

        primaryStage.setScene(scene);

        StageUtils.displayUnclosable(primaryStage);

        scene.setOnMouseMoved((e) -> {
            if (graphicalMenus.getConfiguration().isGazeInteraction()) {
                graphicalMenus.getConfiguration().analyse(e.getScreenX(), e.getScreenY());
            }
        });


    }

    public static void main(String[] args) {
        launch(args);
    }

}

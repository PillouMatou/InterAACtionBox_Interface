package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Map;

public class Main extends Application {

    public String getGazePlayInstallationRepo(){
        Parameters parameters = getParameters ();
        List<String> rawArguments = parameters.getRaw ();
        if(rawArguments.size() > 0){
            return rawArguments.get(0);
        } else {
            return "C:\\Program Files (x86)\\GazePlay";
        }
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(new HomeScreen(primaryStage, getGazePlayInstallationRepo()), Color.TRANSPARENT);
        primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight());

        primaryStage.setScene(scene);

        StageUtils.displayUnclosable(primaryStage);

        primaryStage.setOpacity(0.2);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
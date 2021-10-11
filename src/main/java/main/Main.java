package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.UI.menu.GraphicalMenus;
import main.utils.StageUtils;
import main.utils.UtilsOS;

import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("InteraactionBox-AFSR");

        GraphicalMenus graphicalMenus = new GraphicalMenus(primaryStage);
        Scene scene = new Scene(graphicalMenus.getHomeScreen(), Color.TRANSPARENT);
        graphicalMenus.getConfiguration().setScene(scene);
        primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth());
        primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight());

        primaryStage.setScene(scene);

        graphicalMenus.getHomeScreen().showCloseProcessButtonIfProcessNotNull();
        StageUtils.displayUnclosable(primaryStage);

        scene.setOnMouseMoved((e) -> {
            if (graphicalMenus.getConfiguration().isGazeInteraction()) {
                graphicalMenus.getConfiguration().analyse(e.getScreenX(), e.getScreenY());
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>
                () {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.E) {
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
        Platform.setImplicitExit(false);

    }

}

package main.process;

import javafx.application.Platform;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcess;

import java.io.IOException;

public class InteraactionSceneProcess implements AppProcess {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(AppProcess.getBrowser(),
                "--kiosk",
                "--window-position=0,0",
                "--disable-gpu",
                "--no-sandbox",
                "--fullscreen",
                "http://interaactionScene.net/");
    }

    @Override
    public Process start(GraphicalMenus graphicalMenus) {
        try {

            GoogleChromeXdotoolProcess gcxp = new GoogleChromeXdotoolProcess();
            gcxp.setUpProcessBuilder();
            gcxp.start();
            AppProcess.startWindowIdSearcher(graphicalMenus, "google-chrome");

            Process process = processBuilder.inheritIO().start();

            process.onExit().thenRun(
                    new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater((() -> {
                                graphicalMenus.primaryStage.show();
                                graphicalMenus.primaryStage.toFront();
                            }));
                        }
                    }
            );
            return process;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

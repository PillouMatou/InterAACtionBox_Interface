package main.process;

import javafx.application.Platform;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.WifiXdotoolProcess;

import java.io.IOException;

public class WifiProcess implements AppProcess {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder("nm-connection-editor");
    }

    @Override
    public Process start(GraphicalMenus graphicalMenus) {
        try {
            WifiXdotoolProcess gcxp = new WifiXdotoolProcess();
            gcxp.setUpProcessBuilder();
            gcxp.start();

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

            AppProcess.startWindowIdSearcher(graphicalMenus, "wifi");
            return process;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

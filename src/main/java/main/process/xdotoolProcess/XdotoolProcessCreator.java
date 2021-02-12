package main.process.xdotoolProcess;

import javafx.application.Platform;
import main.UI.menu.GraphicalMenus;

import java.io.File;
import java.io.IOException;

public interface XdotoolProcessCreator {

    void setUpProcessBuilder();

    Process start(GraphicalMenus graphicalMenus);

    static void startWindowIdSearcher(GraphicalMenus graphicalMenus, String name) {
        Thread t = new Thread(() -> {
            File file = new File(name + "_windowId.txt");
            while (!file.exists()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException err) {
                    err.printStackTrace();
                }
            }
            Platform.runLater(graphicalMenus.primaryStage::hide);
        });
        t.setDaemon(true);
        t.start();
    }

    static Process getStartingProcess(ProcessBuilder processBuilder, GraphicalMenus graphicalMenus, String name) {
        try {
            File file = new File(name + "_windowId.txt");
            file.delete();
            XdotoolProcessCreator.startWindowIdSearcher(graphicalMenus, name);
            return processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

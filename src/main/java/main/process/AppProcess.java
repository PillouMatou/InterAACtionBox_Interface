package main.process;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.UI.ProgressButton;
import main.UI.menu.GraphicalMenus;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.File;

public interface AppProcess {

    NamedProcess start(GraphicalMenus graphicalMenus);

    void setUpProcessBuilder();

    static String getBrowser() {
        if (UtilsOS.isWindows()) {
            return "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";
        } else if (UtilsOS.isUnix()) {
            return "google-chrome";
        }
        return "";
    }

    default ProgressButton createButton(Image image, GraphicalMenus graphicalMenus, String windowNameToSearch) {
        ProgressButton progressButton = new ProgressButton();
        progressButton.getButton().setRadius(100);

        ImageView logo = new ImageView(image);
        logo.setFitWidth(progressButton.getButton().getRadius() * 0.7);
        logo.setFitHeight(progressButton.getButton().getRadius() * 0.7);
        logo.fitWidthProperty().bind(progressButton.getButton().radiusProperty().multiply(0.7));
        logo.fitHeightProperty().bind(progressButton.getButton().radiusProperty().multiply(0.7));
        logo.setPreserveRatio(true);

        progressButton.setImage(logo);

        setUpProcessBuilder();
        progressButton.assignIndicator((e) -> {
            if (graphicalMenus.process.get() != null) {
                graphicalMenus.process.destroy();
                graphicalMenus.process.set(null);
            }
            graphicalMenus.process = start(graphicalMenus);
        });


        progressButton.start();
        return progressButton;
    }


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
}

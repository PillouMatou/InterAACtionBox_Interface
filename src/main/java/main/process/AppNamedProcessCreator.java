package main.process;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.UI.ProgressButton;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.process.xdotoolProcess.XdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.File;
import java.io.IOException;

public interface AppNamedProcessCreator {

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
                graphicalMenus.process.exitAskedByUser = true;
                graphicalMenus.process.destroy();
                graphicalMenus.process.set(null);
            }
            graphicalMenus.process = start(graphicalMenus);
        });


        progressButton.start();
        return progressButton;
    }



    static NamedProcess createProcress(XdotoolProcessCreator xdotoolProcessCreator, ProcessBuilder processBuilder, GraphicalMenus graphicalMenus, String name){
        try {

            NamedProcess namedProcess = new NamedProcess();
            if (UtilsOS.isUnix()) {
                xdotoolProcessCreator.setUpProcessBuilder();
                xdotoolProcessCreator.start(graphicalMenus);
            } else {
                graphicalMenus.primaryStage.hide();
            }
            Process process = processBuilder.inheritIO().start();

            process.onExit().thenRun(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (!namedProcess.exitAskedByUser) {
                                Platform.runLater((() -> {
                                    graphicalMenus.getHomeScreen().showCloseMenuIfProcessNotNull();
                                    graphicalMenus.primaryStage.show();
                                    graphicalMenus.primaryStage.toFront();
                                }));
                            }
                        }
                    }
            );
            namedProcess.set(process);
            namedProcess.setName(name);
            return namedProcess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package main.process;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Configuration;
import main.UI.I18NProgressButton;
import main.UI.menu.ExitMenu;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.XdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.StageUtils;
import main.utils.UtilsOS;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public interface AppNamedProcessCreator {

    static String getBrowser() {
        if (UtilsOS.isWindows()) {
            return "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";
        } else if (UtilsOS.isUnix()) {
            return "google-chrome";
        }
        return "";
    }

    static NamedProcess createProcress(XdotoolProcessCreator xdotoolProcessCreator, ProcessBuilder processBuilder, GraphicalMenus graphicalMenus, String name) {
        try {
            NamedProcess namedProcess;
            if (UtilsOS.isUnix()) {
                xdotoolProcessCreator.setUpProcessBuilder();
                namedProcess = new NamedProcess(xdotoolProcessCreator.start(graphicalMenus));
            } else {
                namedProcess = new NamedProcess();
                graphicalMenus.primaryStage.hide();
                graphicalMenus.getHomeScreen().removeMenu();
            }
            return getNamedProcess(processBuilder, graphicalMenus, name, namedProcess);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static NamedProcess createProcressAndWaitForClose(XdotoolProcessCreator xdotoolProcessCreator, CloseGoogleChromeProcessCreator closeGoogleChromeProcessCreator, ProcessBuilder processBuilder, GraphicalMenus graphicalMenus, String name) {
        try {
            NamedProcess namedProcess;
            if (UtilsOS.isUnix()) {
                xdotoolProcessCreator.setUpProcessBuilder();
                closeGoogleChromeProcessCreator.setUpProcessBuilder();
                namedProcess = new NamedProcess(xdotoolProcessCreator.start(graphicalMenus), closeGoogleChromeProcessCreator.waitForCloseRequest());
            } else {
                namedProcess = new NamedProcess();
                graphicalMenus.primaryStage.hide();
                graphicalMenus.getHomeScreen().removeMenu();
            }
            return getNamedProcess(processBuilder, graphicalMenus, name, namedProcess);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static NamedProcess getNamedProcess(ProcessBuilder processBuilder, GraphicalMenus graphicalMenus, String name, NamedProcess namedProcess) throws IOException {
        Process process = processBuilder.inheritIO().start();

        process.onExit().thenRun(
                () -> {
                    if (!namedProcess.exitAskedByUser) {
                        Platform.runLater((() -> {
                            graphicalMenus.getHomeScreen().showCloseProcessButtonIfProcessNotNull();
                            graphicalMenus.primaryStage.show();
                            graphicalMenus.primaryStage.toFront();
                            graphicalMenus.process.set(null);
                            graphicalMenus.getHomeScreen().showCloseProcessButtonIfProcessNotNull();
                        }));
                    }
                }
        );
        namedProcess.set(process);
        namedProcess.setName(name);
        return namedProcess;
    }

    NamedProcess start(GraphicalMenus graphicalMenus);

    void setUpProcessBuilder();

    default I18NProgressButton createButton(String name, Image image, GraphicalMenus graphicalMenus, Configuration configuration) {
        I18NProgressButton progressButton = new I18NProgressButton();
        progressButton.getButton().setRadius(graphicalMenus.primaryStage.getWidth() / 10);

        ImageView logo = new ImageView(image);
        logo.setFitWidth(progressButton.getButton().getRadius() * 0.99);
        logo.setFitHeight(progressButton.getButton().getRadius() * 0.99);
        logo.fitWidthProperty().bind(progressButton.getButton().radiusProperty().multiply(0.99));
        logo.fitHeightProperty().bind(progressButton.getButton().radiusProperty().multiply(0.99));
        logo.setPreserveRatio(true);

        progressButton.setImage(logo);

        setUpProcessBuilder();

        progressButton.setOnMouseEntered(ev -> {
            if(configuration.isGazeInteraction()) {
                progressButton.assignIndicator((e) -> {
                    if(name.equals("GazePlay")){
                        graphicalMenus.getHomeScreen().setCursor(Cursor.WAIT);
                    }
                    StageUtils.killRunningProcess(graphicalMenus);
                    graphicalMenus.process = start(graphicalMenus);
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        progressButton.start();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
            }else{
                progressButton.deactivate();
            }
        });

        progressButton.setOnMouseClicked((e) -> {
            if(name.equals("GazePlay")){
                graphicalMenus.getHomeScreen().setCursor(Cursor.WAIT);
            }
            progressButton.stop();
            StageUtils.killRunningProcess(graphicalMenus);
            graphicalMenus.process = start(graphicalMenus);
            progressButton.start();
            /*
            try {
                TimeUnit.SECONDS.sleep(5);
                progressButton.start();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
             */
        });

        return progressButton;
    }
}

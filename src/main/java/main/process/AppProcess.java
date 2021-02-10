package main.process;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.UI.ProgressButton;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcess;
import main.utils.UtilsOS;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public interface AppProcess {

    Process start();

    void setUpProcessBuilder();

    static String getBrowser() {
        if (UtilsOS.isWindows()) {
            return "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe";
        } else if (UtilsOS.isUnix()) {
            return "google-chrome";
        }
        return "";
    }

    default ProgressButton createButton(Image image, GraphicalMenus graphicalMenus, boolean needsGoogleChrome) {
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
            graphicalMenus.getQuickMenu().process = this.start();
            if (needsGoogleChrome) {
                GoogleChromeXdotoolProcess gcxp = new GoogleChromeXdotoolProcess();
                gcxp.setUpProcessBuilder();
                gcxp.start();

                File file = new File("google-chrome_windowId.txt");
                while (!file.exists()){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException err) {
                        err.printStackTrace();
                    }
                }
               Platform.runLater(graphicalMenus.primaryStage::hide);
            } else {
                Platform.runLater(graphicalMenus.primaryStage::hide);
            }
        });


        progressButton.start();
        return progressButton;
    }
}

package main.process;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.UI.ProgressButton;
import main.UI.menu.GraphicalMenus;
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
            Date now = new Date();
            graphicalMenus.getQuickMenu().process = this.start();
            if (needsGoogleChrome) {
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        File file;
                        if (UtilsOS.isWindows()) {
                            file = new File(System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Last Browser");
                        } else { //if( UtilsOS.isUnix()) {
                            file = new File(System.getProperty("user.home") + "/.config/google-chrome/Local State");
                        }
                        if (file.exists()) {
                            System.out.println("Time: " + sdf.format(now));
                            System.out.println("Time Last Modified " + sdf.format(file.lastModified()));
                            if (file.lastModified() > now.getTime()) {
                                System.out.println("Hiding primary Stage");
                                Platform.runLater(graphicalMenus.primaryStage::hide);
                                t.purge();
                                t.cancel();
                            }
                        } else {
                            System.out.println("file does not exist");
                            Platform.runLater(graphicalMenus.primaryStage::hide);
                            t.purge();
                            t.cancel();
                        }
                    }
                }, 0, 100);
            } else {
                Platform.runLater(graphicalMenus.primaryStage::hide);
            }
        });


        progressButton.start();
        return progressButton;
    }
}

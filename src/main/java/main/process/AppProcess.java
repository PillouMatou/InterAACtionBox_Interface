package main.process;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.UI.ProgressButton;
import main.UI.menu.QuickMenu;
import main.utils.UtilsOS;

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

    default ProgressButton createButton(Image image, QuickMenu quickMenu) {
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
            quickMenu.process = this.start();
        });

        progressButton.start();
        return progressButton;
    }
}

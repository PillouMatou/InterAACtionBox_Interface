package main.process;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.ProgressButton;
import main.SecondStage;
import main.gaze.devicemanager.AbstractGazeDeviceManager;

import java.io.IOException;

public class AugComProcess implements AppProcess {

    ProcessBuilder processBuilder;

    @Override
    public void init() {
        processBuilder = new ProcessBuilder(AppProcess.getBrowser(),
                "--kiosk",
                "--window-position=0,0", "--disable-gpu", "--no-sandbox", "--fullscreen", "http://augcom.net/");
    }

    @Override
    public Process start() {
        try {
            return processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ProgressButton createButton(BorderPane borderPane, SecondStage stage, AbstractGazeDeviceManager tgdm) {
        ProgressButton progressButton = new ProgressButton();
        ImageView logo = new ImageView(new Image("images/angular.png"));
        progressButton.getButton().setRadius(100);
        logo.setFitWidth(progressButton.getButton().getRadius() * 0.7);
        logo.setFitHeight(progressButton.getButton().getRadius() * 0.7);
        logo.fitWidthProperty().bind(progressButton.getButton().radiusProperty().multiply(0.7));
        logo.fitHeightProperty().bind(progressButton.getButton().radiusProperty().multiply(0.7));
        logo.setPreserveRatio(true);
        progressButton.setImage(logo);
        progressButton.assignIndicator((e) -> {
            stage.proc = this.start();
        }, 500);
        this.init();
        progressButton.active();
        return progressButton;
    }

}

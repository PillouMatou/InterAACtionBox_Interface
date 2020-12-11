package main.process;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.ProgressButton;
import main.SecondStage;
import main.gaze.devicemanager.AbstractGazeDeviceManager;

import java.io.IOException;

public class InteraactionSceneProcess implements AppProcess {

    ProcessBuilder pb;

    @Override
    public void init() {
        pb = new ProcessBuilder(AppProcess.getBrowser(),
                // "--fullscreen","--app="+"http://www.augcom.net");
                // "--new-window", "--disable-pinch", "--overscroll-history-navigation=0",
                "--kiosk",
                "--window-position=0,0", "--disable-gpu", "--no-sandbox", "--fullscreen", "http://interaactionScene.net/");
        //"--start-fullscreen" ,"--new-window","--window-position=0,0",  "--disable-gpu", "--no-sandbox", "http://www.augcom.net");
    }

    @Override
    public Process start() {
        try {
            return pb.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ProgressButton createButton(BorderPane borderPane, SecondStage stage, AbstractGazeDeviceManager tgdm) {
        ProgressButton pb = new ProgressButton();
        ImageView logo = new ImageView(new Image("images/angular.png"));
        pb.getButton().setRadius(100);
        logo.setFitWidth(pb.getButton().getRadius() * 0.7);
        logo.setFitHeight(pb.getButton().getRadius() * 0.7);
        logo.fitWidthProperty().bind(pb.getButton().radiusProperty().multiply(0.7));
        logo.fitHeightProperty().bind(pb.getButton().radiusProperty().multiply(0.7));
        logo.setPreserveRatio(true);
        pb.setImage(logo);
        pb.assignIndicator((e) -> {
            stage.proc = this.start();
        }, 500);
        this.init();
        pb.active();
        return pb;
    }

}

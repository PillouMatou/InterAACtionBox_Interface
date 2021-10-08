package main.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class UpdateManager {

    public BooleanProperty anyUpdateNeeded = new SimpleBooleanProperty(false);

    public UpdateService[] updateServices = {
            new UpdateService("Syst�me", ""),
            new UpdateService("AugCom", "https://api.github.com/repos/AFSR/AugCom-AFSR/releases/latest"),
            new UpdateService("InterAACtionScene", "https://api.github.com/repos/AFSR/InteraactionScene-AFSR/releases/latest"),
            new UpdateService("GazePlay", "https://api.github.com/repos/AFSR/GazePlay-AFSR/releases/latest"),
            new UpdateService("InterAACtionPlayer", "https://api.github.com/repos/AFSR/InteraactionPlayer-AFSR/releases/latest"),

    };

    public UpdateManager() {
        anyUpdateNeeded.bind(
                updateServices[UpdateService.SYSTEME].getUpdateProperty().or(
                        updateServices[UpdateService.AUGCOM].getUpdateProperty()).or(
                        updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty()).or(
                        updateServices[UpdateService.GAZEPLAY].getUpdateProperty()).or(
                        updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty()
                ));
    }

    public void checkUpdates() {
        for (UpdateService updateService : updateServices)
            updateService.checkUpdate();
    }

}

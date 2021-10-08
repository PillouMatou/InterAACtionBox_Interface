package main.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import main.UI.UpdateService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class UpdateManager {

    public BooleanProperty anyUpdateNeeded = new SimpleBooleanProperty(false);

    public UpdateService[] updateServices = {
            new UpdateService("Système",""),
            new UpdateService("AugCom", "https://api.github.com/repos/AFSR/AugCom-AFSR/releases/latest"),
            new UpdateService("InterAACtionScene", "https://api.github.com/repos/AFSR/InteraactionScene-AFSR/releases/latest"),
            new UpdateService("GazePlay", "https://api.github.com/repos/AFSR/GazePlay-AFSR/releases/latest"),
            new UpdateService("InterAACtionPlayer", "https://api.github.com/repos/AFSR/InteraactionPlayer-AFSR/releases/latest"),

    };

    public UpdateManager(){
        anyUpdateNeeded.bind(
                updateServices[UpdateService.SYSTEME].getUpdateProperty().and(
                updateServices[UpdateService.AUGCOM].getUpdateProperty()).and(
                updateServices[UpdateService.INTERAACTION_SCENE].getUpdateProperty()).and(
                updateServices[UpdateService.GAZEPLAY].getUpdateProperty()).and(
                updateServices[UpdateService.INTERAACTION_PLAYER].getUpdateProperty()
                ));
    }

    public void checkUpdates() {
        for(UpdateService updateService : updateServices)
            updateService.checkUpdate();
    }

}

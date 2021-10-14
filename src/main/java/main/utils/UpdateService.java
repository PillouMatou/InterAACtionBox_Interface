package main.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

@Slf4j
public class UpdateService {

    public static final int SYSTEME = 0;
    public static final int AUGCOM = 1;
    public static final int INTERAACTION_SCENE = 2;
    public static final int GAZEPLAY = 3;
    public static final int INTERAACTION_PLAYER = 4;
    @Getter
    final BooleanProperty updateProperty;
    @Getter
    private final String name;
    @Getter
    private final String updateURL;
    @Getter
    String version;

    public UpdateService(String name, String updateURL) {
        this.name = name;
        this.updateURL = updateURL;
        this.updateProperty = new SimpleBooleanProperty(false);
    }

    public static boolean isInstalledAt() {
        return true;
    }

    public void checkUpdate() {
        if (!updateURL.equals("")) {
            try {
                JSONObject softwareJson = JsonReader.readJsonFromUrl(updateURL);
                File directory = new File("~/" + softwareJson.get("name"));
                this.version = "" + softwareJson.get("name");
                updateProperty.set(!directory.exists() || !directory.isDirectory());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            updateProperty.set(false);
        }
    }

}

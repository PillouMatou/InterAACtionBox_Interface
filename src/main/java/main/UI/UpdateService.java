package main.UI;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import main.utils.JsonReader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

@Slf4j
public class UpdateService {

    public static final int SYSTEME = 0;
    public static final int AUGCOM = 1;
    public static final int INTERAACTION_SCENE = 2;
    public static final int GAZEPLAY= 3;
    public static final int INTERAACTION_PLAYER = 4;

    @Getter
    private final String name;

    @Getter
    private final String updateURL;

    @Getter
    final BooleanProperty updateProperty;

    @Getter
    String version;

    public UpdateService(String name, String updateURL){
        this.name = name;
        this.updateURL = updateURL;
        this.updateProperty = new SimpleBooleanProperty(false);
    }

    public void checkUpdate() {
        try {
            JSONObject augComJSON = JsonReader.readJsonFromUrl(updateURL);
            File directory = new File("~/" + augComJSON.get("name"));
            this.version = "" + augComJSON.get("name");
            updateProperty.set(!directory.exists() || !directory.isDirectory());
        } catch (IOException | JSONException e) {
            // Do nothing
        }
    }


}

package main.utils;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    public static final int INTERAACTION_GAZE = 5;
    public static final int INTERAACTION_INTERFACE = 6;
    @Getter
    final BooleanProperty updateProperty;
    @Getter
    final BooleanProperty existProperty;
    @Getter
    private final String name;
    @Getter
    private final String updateURL;
    @Getter
    String version;

    @Getter
    StringProperty output = new SimpleStringProperty("");

    public UpdateService(String name, String updateURL) {
        this.name = name;
        this.updateURL = updateURL;
        this.updateProperty = new SimpleBooleanProperty(false);
        this.existProperty = new SimpleBooleanProperty(false);
    }

    public void checkUpdate(boolean testBool) {
        if (!updateURL.equals("") && testBool) {
            try {
                if (name.equals("GazePlay")) {
                    JSONObject softwareJson = JsonReader.readJsonFromUrl(updateURL);
                    if (softwareJson != null) {
                        File directory = new File(System.getProperty("user.home") + "/" + softwareJson.get("name"));
                        File directoryspace = new File(System.getProperty("user.home") + "/ " + softwareJson.get("name"));
                        this.version = "" + softwareJson.get("name");
                        log.info(directory.getAbsolutePath());
                        Platform.runLater(() -> {
                            updateProperty.set(!((directory.exists() && directory.isDirectory()) || (directoryspace.exists() && directoryspace.isDirectory())));
                        });
                    }
                }else if (name.equals("InterAACtionGaze")) {
                    JSONObject softwareJson = JsonReader.readJsonFromUrl(updateURL);
                    if (softwareJson != null) {
                        File directory = new File(System.getProperty("user.home") + "/InterAACtionGaze");
                        File directoryspace = new File(System.getProperty("user.home") + "/ InterAACtionGaze");
                        this.version = "" + softwareJson.get("name");
                        log.info(directory.getAbsolutePath());
                        Platform.runLater(() -> {
                            updateProperty.set(!((directory.exists() && directory.isDirectory()) || (directoryspace.exists() && directoryspace.isDirectory())));
                        });
                    }
                }else if (name.equals("InterAACtionBox_Interface-linux")){
                    JSONObject softwareJson = JsonReader.readJsonFromUrl(updateURL);
                    if (softwareJson != null) {
                        File directory = new File(System.getProperty("user.home") + "/InterAACtionBox_Interface-linux");
                        File directoryspace = new File(System.getProperty("user.home") + "/ InterAACtionBox_Interface-linux");
                        this.version = "" + softwareJson.get("name");
                        log.info(directory.getAbsolutePath());
                        Platform.runLater(() -> {
                            updateProperty.set(!((directory.exists() && directory.isDirectory()) || (directoryspace.exists() && directoryspace.isDirectory())));
                        });
                    }
                } else {
                    JSONObject softwareJson = JsonReader.readJsonFromUrl(updateURL);
                    if (softwareJson != null) {
                        File directory = new File(System.getProperty("user.home") + "/dist/" + softwareJson.get("name"));
                        File directoryspace = new File(System.getProperty("user.home") + "/dist/ " + softwareJson.get("name"));
                        this.version = "" + softwareJson.get("name");
                        Platform.runLater(() -> {
                            updateProperty.set(!((directory.exists() && directory.isDirectory()) || (directoryspace.exists() && directoryspace.isDirectory())));
                        });
                    }
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            updateProperty.set(false);
        }
    }

    public void checkExist() {
        if (name.equals("GazePlay")) {
            File[] directories = new File(System.getProperty("user.home")).listFiles(file -> file.isDirectory() && file.getName().toLowerCase().contains(name.toLowerCase() + "-afsr"));
            existProperty.set(directories == null || directories.length == 0);
        } else if (name.equals("InterAACtionGaze") || name.equals("InterAACtionBox_Interface-linux")){
            File[] directories = new File(System.getProperty("user.home")).listFiles(file -> file.isDirectory() && file.getName().toLowerCase().contains(name.toLowerCase()));
            existProperty.set(directories == null || directories.length == 0);
        } else {
            File[] directories = new File(System.getProperty("user.home") + "/dist").listFiles(file -> file.isDirectory() && file.getName().toLowerCase().contains(name.toLowerCase()));
            existProperty.set(directories == null || directories.length == 0);
        }
    }
}

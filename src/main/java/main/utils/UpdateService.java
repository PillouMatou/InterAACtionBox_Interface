package main.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

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
    final BooleanProperty existProperty;
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
        File[] directories = new File(System.getProperty("user.home")).listFiles(file -> file.isDirectory() && file.getName().contains(name));
        this.existProperty = new SimpleBooleanProperty(directories == null || directories.length == 0);
    }

    public void checkUpdate() {
        updateProperty.set(false);

//        if (!updateURL.equals("")) {
//            try {
//                if (name.equals("GazePlay")) {
//                    JSONObject softwareJson = JsonReader.readJsonFromUrl(updateURL);
//                    if (softwareJson != null) {
//                        File directory = new File(System.getProperty("user.home") + "/" + softwareJson.get("name"));
//                        File directoryspace = new File(System.getProperty("user.home") + "/ " + softwareJson.get("name"));
//                        this.version = "" + softwareJson.get("name");
//                        log.info(directory.getAbsolutePath());
//                        updateProperty.set(!((directory.exists() && directory.isDirectory()) || (directoryspace.exists() && directoryspace.isDirectory())));
//                    }
//                }else {
//                    JSONObject softwareJson = JsonReader.readJsonFromUrl(updateURL);
//                    if (softwareJson != null) {
//                        File directory = new File(System.getProperty("user.home") + "/dist/" + softwareJson.get("name"));
//                        File directoryspace = new File(System.getProperty("user.home") + "/dist/ " + softwareJson.get("name"));
//                        this.version = "" + softwareJson.get("name");
//                        log.info(directory.getAbsolutePath());
//                        updateProperty.set(!((directory.exists() && directory.isDirectory()) || (directoryspace.exists() && directoryspace.isDirectory())));
//                    }
//                }
//
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            updateProperty.set(false);
//        }
    }

    public void checkExist() {
//        if (name.equals("GazePlay")) {
//            File[] directories = new File(System.getProperty("user.home")).listFiles(file -> file.isDirectory() && file.getName().toLowerCase().contains(name.toLowerCase()+"-afsr"));
//            existProperty.set(directories == null || directories.length == 0);
//        } else {
//            File[] directories = new File(System.getProperty("user.home") + "/dist").listFiles(file -> file.isDirectory() && file.getName().toLowerCase().contains(name.toLowerCase()));
//            existProperty.set(directories == null || directories.length == 0);
//        }
        existProperty.set(false);
    }
}

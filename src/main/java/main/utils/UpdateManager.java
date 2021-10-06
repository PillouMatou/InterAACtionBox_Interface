package main.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class UpdateManager {

    public boolean augComNeedsUpdate = false;
    public boolean gazePlayNeedsUpdate = false;
    public boolean interaactionSceneNeedsUpdate = false;
    public boolean interaactionPlayerNeedsUpdate = false;
    public boolean systemNeedsUpdate = false;

    public String augComVersion = "";
    public String gazePlayVersion = "";
    public String interaactionSceneVersion = "";
    public String interaactionPlayerVersion = "";
    public String systemVersion = "";

    private void checkAugComUpdate() {
        try {
            JSONObject augComJSON = JsonReader.readJsonFromUrl("https://api.github.com/repos/AFSR/AugCom-AFSR/releases/latest");
            File augComDirectory = new File("~/" + augComJSON.get("name"));
            augComVersion = "" + augComJSON.get("name");
            augComNeedsUpdate = !augComDirectory.exists() || !augComDirectory.isDirectory();
        } catch (IOException | JSONException e) {
            // Do nothing
        }
    }

    private void checkInteraactionSceneUpdate() {
        try {
            JSONObject interaactionSceneJSON = JsonReader.readJsonFromUrl("https://api.github.com/repos/AFSR/InteraactionScene-AFSR/releases/latest");
            File interaactionSceneDirectory = new File("~/" + interaactionSceneJSON.get("name"));
            interaactionSceneVersion = "" + interaactionSceneJSON.get("name");
            interaactionSceneNeedsUpdate = !interaactionSceneDirectory.exists() || !interaactionSceneDirectory.isDirectory();
        } catch (IOException | JSONException e) {
            // Do nothing
        }

    }

    private void checkAInteraactionPlayerUpdate() {
        try {
            JSONObject interaactionPlayerJSON = JsonReader.readJsonFromUrl("https://api.github.com/repos/AFSR/InteraactionPlayer-AFSR/releases/latest");
            File interaactionPlayerDirectory = new File("~/" + interaactionPlayerJSON.get("name"));
            interaactionSceneVersion = "" + interaactionPlayerJSON.get("name");
            interaactionSceneNeedsUpdate = !interaactionPlayerDirectory.exists() || !interaactionPlayerDirectory.isDirectory();
        } catch (IOException | JSONException e) {
            // Do nothing
        }
    }

    private void checkgazePlayUpdate() {
        try {
            JSONObject gazePlayJSON = JsonReader.readJsonFromUrl("https://api.github.com/repos/AFSR/GazePlay-AFSR/releases/latest");
            File gazePlayDirectory = new File("~/" + gazePlayJSON.get("name"));
            gazePlayVersion = "" + gazePlayJSON.get("name");
            gazePlayNeedsUpdate = !gazePlayDirectory.exists() || !gazePlayDirectory.isDirectory();
        } catch (IOException | JSONException e) {
            // Do nothing
        }
    }

    public boolean needsUpdate() {
        return this.augComNeedsUpdate ||
                this.gazePlayNeedsUpdate ||
                this.interaactionPlayerNeedsUpdate ||
                this.interaactionSceneNeedsUpdate ||
                this.systemNeedsUpdate;
    }

    public void checkUpdates() {
        this.checkAInteraactionPlayerUpdate();
        this.checkAugComUpdate();
        this.checkgazePlayUpdate();
        this.checkInteraactionSceneUpdate();
    }

}

package main.process;

import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

public class SpotifyNamedProcessCreator implements AppNamedProcessCreator {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        if(UtilsOS.isWindows()) {
            processBuilder = new ProcessBuilder(AppNamedProcessCreator.getBrowser(),
                    "--kiosk",
                    "--window-position=0,0",
                    "--disable-gpu",
                    "--fullscreen",
                    "--no-sandbox",
                    "https://www.spotify.com/fr/");
        } else {
            processBuilder = new ProcessBuilder("spotify");
        }
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        if (UtilsOS.isWindows()) {
            return AppNamedProcessCreator.createProcress(new GoogleChromeXdotoolProcessCreator(), processBuilder, graphicalMenus, "AugCom");
        } else {
            return AppNamedProcessCreator.createProcress(new GoogleChromeXdotoolProcessCreator(), processBuilder, graphicalMenus, "Spotify");

        }
    }

}

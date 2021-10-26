package main.process;

import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

public class InterAACtionPlayerNamedProcessCreator implements AppNamedProcessCreator {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(AppNamedProcessCreator.getBrowser(),
                "--kiosk",
                "--window-position=0,0",
                "--fullscreen",
                "--disable-component-update",
                "--simulate-outdated-no-au='Tue, 31 Dec 2099 23:59:59 GMT'",
                "--disable-features=Translate",
                "http://localhost:8082/fr/connect/" + UtilsOS.getUserNameFromOSForPWA());
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        return AppNamedProcessCreator.createProcressAndWaitForClose(new GoogleChromeXdotoolProcessCreator(), new CloseGoogleChromeProcessCreator(), processBuilder, graphicalMenus, "GazeMediaPlayer");
    }
}

package main.process;

import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

public class GazeMediaPlayerNamedProcessCreator implements AppNamedProcessCreator{

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(AppNamedProcessCreator.getBrowser(),
                "--kiosk",
                "--window-position=0,0",
                "--fullscreen",
                "http://localhost:8082/");
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        return AppNamedProcessCreator.createProcress(new GoogleChromeXdotoolProcessCreator(), processBuilder, graphicalMenus, "GazeMediaPlayer");
    }
}

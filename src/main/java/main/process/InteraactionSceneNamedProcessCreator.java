package main.process;

import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.utils.NamedProcess;

public class InteraactionSceneNamedProcessCreator implements AppNamedProcessCreator {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(AppNamedProcessCreator.getBrowser(),
                "--kiosk",
                "--window-position=0,0",
                "--disable-gpu",
                "--no-sandbox",
                "--fullscreen",
                "http://interaactionScene.net/");
        // "http://localhost:8081/");
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        return AppNamedProcessCreator.createProcress(new GoogleChromeXdotoolProcessCreator(), processBuilder, graphicalMenus, "InteraactionScene");

    }

}

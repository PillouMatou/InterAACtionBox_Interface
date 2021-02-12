package main.process;

import javafx.application.Platform;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.IOException;

public class AugComNamedProcessCreator implements AppNamedProcessCreator {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(AppNamedProcessCreator.getBrowser(),
                "--kiosk",
                "--window-position=0,0",
                "--disable-gpu",
                "--fullscreen",
                "--no-sandbox",
                "http://augcom.net/");
        //"http://localhost:4200/");
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        return AppNamedProcessCreator.createProcress(new GoogleChromeXdotoolProcessCreator(),processBuilder,graphicalMenus,"AugCom");
    }

}

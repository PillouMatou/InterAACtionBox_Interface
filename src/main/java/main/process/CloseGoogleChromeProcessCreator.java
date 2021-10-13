package main.process;

import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.process.xdotoolProcess.XdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.File;
import java.io.IOException;

public class CloseGoogleChromeProcessCreator {

    ProcessBuilder processBuilder;

    public void setUpProcessBuilder() {
            processBuilder = new ProcessBuilder(
                    "sh",
                    "./scripts/close_chrome.sh");
    }

    Process waitForCloseRequest() {
        try {
            File fileFR = new File("~/Téléchargements/close.txt");
            File fileEN = new File("~/Downloads/close.txt");
            if(fileFR.exists()) {
                fileFR.delete();
            }
            if(fileEN.exists()) {
                fileEN.delete();
            }
            return this.processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

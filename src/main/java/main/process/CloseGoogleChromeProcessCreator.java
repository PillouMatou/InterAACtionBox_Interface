package main.process;

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
            File fileFR = new File("~/Téléchargements/close161918.txt");
            File fileEN = new File("~/Downloads/close161918.txt");
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

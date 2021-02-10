package main.process.xdotoolProcess;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class GoogleChromeXdotoolProcess {

    ProcessBuilder processBuilder;

    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(
                "sh",
                "src/resources/scripts/google-chrome_windowId.sh"
        );
    }

    public Process start() {
        try {
            File file = new File("google-chrome_windowId.txt");
            file.delete();
            return processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

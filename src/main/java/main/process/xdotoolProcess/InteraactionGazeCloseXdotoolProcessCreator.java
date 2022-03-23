package main.process.xdotoolProcess;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class InteraactionGazeCloseXdotoolProcessCreator {

    ProcessBuilder processBuilder;

    public InteraactionGazeCloseXdotoolProcessCreator(){
        setUpProcessBuilder();
    }
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(
                "sh",
                "./scripts/closeGaze.sh"
        );
    }

    public Process start() {
        try {
            return processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

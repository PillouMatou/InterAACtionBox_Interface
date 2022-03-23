package main.process.xdotoolProcess;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class InteraactionGazeReOpenXdotoolProcessCreator {

    ProcessBuilder processBuilder;

    public InteraactionGazeReOpenXdotoolProcessCreator(){
        setUpProcessBuilder();
    }
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(
                "sh",
                "./scripts/reOpenGaze.sh"
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

package main.process;

import main.utils.UtilsOS;

import java.io.IOException;

public class TobiiManagerProcess implements AppProcess {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        if (UtilsOS.isWindows()) {
            processBuilder = new ProcessBuilder(
                    "cmd",
                    "/c",
                    "start ",
                    "/max ",
                    "C:/Users/Sebastien/AppData/Local/Programs/TobiiProEyeTrackerManager/TobiiProEyeTrackerManager.exe");
        } else {
            processBuilder = new ProcessBuilder("TobiiProEyeTrackerManager");
        }
    }

    @Override
    public Process start() {
        try {
            return processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

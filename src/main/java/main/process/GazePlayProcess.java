package main.process;

import main.utils.UtilsOS;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class GazePlayProcess implements AppProcess {
    ProcessBuilder processBuilder;
    String gazePlayInstallationRepo;

    public GazePlayProcess(String gazePlayInstallationRepo) {
        super();
        this.gazePlayInstallationRepo = gazePlayInstallationRepo;
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

    @Override
    public void setUpProcessBuilder() {
        processBuilder = createGazePlayBuilder();
    }

    private ProcessBuilder createGazePlayBuilder() {
        String javaBin;
        if (UtilsOS.isWindows()) {
            javaBin = gazePlayInstallationRepo + "/lib/jre/bin/java.exe";
        } else {
            javaBin = gazePlayInstallationRepo + "/lib/jre/bin/java";
        }
        String classpath = gazePlayInstallationRepo + "/lib/*";

        LinkedList<String> commands = new LinkedList<>(Arrays.asList(javaBin, "-cp", classpath, "net.gazeplay.GazePlayLauncher"));

        return new ProcessBuilder(commands);
    }

}

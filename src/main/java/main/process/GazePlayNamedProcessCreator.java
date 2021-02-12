package main.process;

import javafx.application.Platform;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GazePlayXdotoolProcessCreator;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class GazePlayNamedProcessCreator implements AppNamedProcessCreator {
    ProcessBuilder processBuilder;
    String gazePlayInstallationRepo;

    public GazePlayNamedProcessCreator(String gazePlayInstallationRepo) {
        super();
        this.gazePlayInstallationRepo = gazePlayInstallationRepo;
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

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        return AppNamedProcessCreator.createProcress(new GazePlayXdotoolProcessCreator(),processBuilder,graphicalMenus,"GazePlay");

    }

}

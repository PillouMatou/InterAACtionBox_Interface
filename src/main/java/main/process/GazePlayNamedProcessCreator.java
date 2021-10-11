package main.process;

import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GazePlayXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class GazePlayNamedProcessCreator implements AppNamedProcessCreator {
    ProcessBuilder processBuilder;

    public GazePlayNamedProcessCreator() {
        super();
    }

    @Override
    public void setUpProcessBuilder() {
        processBuilder = createGazePlayBuilder();
    }

    private ProcessBuilder createGazePlayBuilder() {
        String javaBin;
        String gazePlayInstallationRepo = getGazePlayRepo();
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
        processBuilder = createGazePlayBuilder();
        return AppNamedProcessCreator.createProcress(new GazePlayXdotoolProcessCreator(), processBuilder, graphicalMenus, "GazePlay");

    }

    private String getGazePlayRepo() {
        if (UtilsOS.isWindows()) {
            return "C:/Program Files (x86)/GazePlay";
        } else {
            String configFilePath = "~/interaactionBox_Interface-linux/bin/configuration.conf";
            try {
                BufferedReader brTest = new BufferedReader(new FileReader(configFilePath));
                String text = brTest.readLine();
                System.out.println("GazePlat directory is: " + text);
                return text;
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }

}

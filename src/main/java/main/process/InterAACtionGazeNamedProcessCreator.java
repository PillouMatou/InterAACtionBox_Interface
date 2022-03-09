package main.process;

import lombok.extern.slf4j.Slf4j;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.InteraactionGazeXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

@Slf4j
public class InterAACtionGazeNamedProcessCreator {

    ProcessBuilder processBuilder;

    public InterAACtionGazeNamedProcessCreator(){
       setUpProcessBuilder();
    }

    public void setUpProcessBuilder() {
        processBuilder = createInterAACtionGazeBuilder();
    }

    private ProcessBuilder createInterAACtionGazeBuilder() {
        String javaBin;
        String gazeInstallationRepo = getInterAACtionGazeRepo();
        if (!gazeInstallationRepo.equals("")) {
            if (UtilsOS.isWindows()) {
                javaBin = gazeInstallationRepo + "/lib/jre/bin/java.exe";
            } else {
                javaBin = gazeInstallationRepo + "/lib/jre/bin/java";
            }
            String classpath = gazeInstallationRepo + "/lib/*";

            LinkedList<String> commands = new LinkedList<>(Arrays.asList(javaBin, "-cp", classpath, "-Djdk.gtk.version=2", "-jar", gazeInstallationRepo+"/lib/untitled.jar"));

            for(String command:commands){
                log.info(command);
            }

            return new ProcessBuilder(commands);
        } else {
            return new ProcessBuilder();
        }
    }

    public void start() {
            InteraactionGazeXdotoolProcessCreator interaactionGazeXdotoolProcessCreator = new InteraactionGazeXdotoolProcessCreator();
            interaactionGazeXdotoolProcessCreator.start();
    }

    private String getInterAACtionGazeRepo() {
        if (UtilsOS.isWindows()) {
            return "C:/Program Files (x86)/InteraactionGaze";
        } else {
            String text = "../../InterAACtionGaze";
            log.info("InterAACtionGaze directory is: " + text);
            return text;
        }
    }
}

package main.process;

import lombok.extern.slf4j.Slf4j;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GazePlayXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

@Slf4j
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

            return new ProcessBuilder(
                    "sh",
                    "../../Launcher/gazeplayAfsrLauncher.sh"
            );
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        processBuilder = createGazePlayBuilder();
        return AppNamedProcessCreator.createProcress(new GazePlayXdotoolProcessCreator(), processBuilder, graphicalMenus, "GazePlay");
    }
}

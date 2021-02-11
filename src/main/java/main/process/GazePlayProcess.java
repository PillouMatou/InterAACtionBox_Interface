package main.process;

import javafx.application.Platform;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GazePlayXdotoolProcess;
import main.utils.NamedProcess;
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
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        try {
            NamedProcess namedProcess = new NamedProcess();
            GazePlayXdotoolProcess gcxp = new GazePlayXdotoolProcess();
            gcxp.setUpProcessBuilder();
            gcxp.start();
            AppProcess.startWindowIdSearcher(graphicalMenus, "gazeplay");

            Process process = processBuilder.inheritIO().start();

            process.onExit().thenRun(
                    new Runnable() {
                        @Override
                        public void run() {
                            Platform.runLater((() -> {
                                graphicalMenus.getHomeScreen().showCloseMenuIfProcessNotNull();
                                graphicalMenus.primaryStage.show();
                                graphicalMenus.primaryStage.toFront();
                            }));
                        }
                    }
            );

            namedProcess.set(process);
            namedProcess.setName("GazePlay");
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

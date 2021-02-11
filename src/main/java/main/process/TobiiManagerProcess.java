package main.process;

import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.TobiiXdotoolProcess;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

import java.io.IOException;

@Slf4j
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
            processBuilder = new ProcessBuilder("tobiiproeyetrackermanager");
        }
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        try {
            NamedProcess namedProcess = new NamedProcess();
            TobiiXdotoolProcess gcxp = new TobiiXdotoolProcess();
            gcxp.setUpProcessBuilder();
            gcxp.start();
            AppProcess.startWindowIdSearcher(graphicalMenus, "tobii");

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
            namedProcess.setName("Tobii Pro EyeTracker Manager");
            return namedProcess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

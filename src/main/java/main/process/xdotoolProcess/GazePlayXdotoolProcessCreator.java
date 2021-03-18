package main.process.xdotoolProcess;

import lombok.extern.slf4j.Slf4j;
import main.UI.menu.GraphicalMenus;

@Slf4j
public class GazePlayXdotoolProcessCreator implements XdotoolProcessCreator {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder(
                "sh",
                "./scripts/gazeplay_windowId.sh"
        );
    }

    @Override
    public Process start(GraphicalMenus graphicalMenus) {
        return XdotoolProcessCreator.getStartingProcess(processBuilder, graphicalMenus, "gazeplay");
    }

}

package main.process;

import lombok.extern.slf4j.Slf4j;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.WifiXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

@Slf4j
public class WifiNamedProcessCreator implements AppNamedProcessCreator {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        processBuilder = new ProcessBuilder("nm-connection-editor");
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        if(UtilsOS.isUnix()) {
            return AppNamedProcessCreator.createProcress(new WifiXdotoolProcessCreator(), processBuilder, graphicalMenus, "Network Manager");
        } else {
            return new NamedProcess();
        }
    }

}

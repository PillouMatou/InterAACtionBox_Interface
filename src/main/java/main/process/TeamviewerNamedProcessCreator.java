package main.process;

import lombok.extern.slf4j.Slf4j;
import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.TeamViewerXdotoolProcessCreator;
import main.process.xdotoolProcess.TobiiXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

@Slf4j
public class TeamviewerNamedProcessCreator implements AppNamedProcessCreator {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        if (UtilsOS.isWindows()) {
            processBuilder = new ProcessBuilder();
        } else {
            processBuilder = new ProcessBuilder("teamviewer");
        }
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        if (UtilsOS.isWindows()) {
            return null;
           } else {
            return AppNamedProcessCreator.createProcress(new TeamViewerXdotoolProcessCreator(), processBuilder, graphicalMenus, "Team Viewer");
        }
    }

}

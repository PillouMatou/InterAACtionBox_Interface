package main.process;

import main.UI.menu.GraphicalMenus;
import main.process.xdotoolProcess.GoogleChromeXdotoolProcessCreator;
import main.utils.NamedProcess;
import main.utils.UtilsOS;

public class AugComNamedProcessCreator implements AppNamedProcessCreator {

    ProcessBuilder processBuilder;

    @Override
    public void setUpProcessBuilder() {
        if (UtilsOS.isWindows()) {
            processBuilder = new ProcessBuilder(AppNamedProcessCreator.getBrowser(),
                    "--kiosk",
                    "--window-position=0,0",
                    "--fullscreen",
                    "--disable-component-update",
                    "--simulate-outdated-no-au='Tue, 31 Dec 2099 23:59:59 GMT'",
                    "--disable-features=Translate",
                    "https://lig-augcom.imag.fr/stable/");
        } else {
            processBuilder = new ProcessBuilder(AppNamedProcessCreator.getBrowser(),
                    "--kiosk",
                    "--window-position=0,0",
                    "--fullscreen",
                    "--disable-component-update",
                    "--simulate-outdated-no-au='Tue, 31 Dec 2099 23:59:59 GMT'",
                    "--disable-features=Translate",
                    "http://localhost:8080/connect/"+UtilsOS.getUserNameFromOSForPWA());
        }
    }

    @Override
    public NamedProcess start(GraphicalMenus graphicalMenus) {
        return AppNamedProcessCreator.createProcressAndWaitForClose(new GoogleChromeXdotoolProcessCreator(),new CloseGoogleChromeProcessCreator(), processBuilder, graphicalMenus, "AugCom");
    }

}

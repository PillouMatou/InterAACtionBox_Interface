package main.process.xdotoolProcess;

import main.utils.UtilsOS;

import java.io.IOException;

public class ActivateMainWindowProcess {

    public static void start() {
        if (UtilsOS.isWindows()) {
        } else {
             try {
                 ProcessBuilder processBuilderRaise = new ProcessBuilder("xdotool", "search", "--onlyvisible", "--classname", "--sync", "--main.Main", "windowraise");
                 processBuilderRaise.start();
                 ProcessBuilder processBuilderFocus = new ProcessBuilder("xdotool", "search", "--onlyvisible", "--classname", "--sync", "--main.Main", "windowfocus");
                 processBuilderFocus.start();
                 ProcessBuilder processBuilderActivate = new ProcessBuilder("xdotool", "search", "--onlyvisible", "--classname", "--sync", "--main.Main", "windowactivate");
                 processBuilderActivate.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

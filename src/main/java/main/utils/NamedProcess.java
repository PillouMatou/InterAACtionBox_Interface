package main.utils;

import lombok.Getter;
import lombok.Setter;
import main.process.CloseGoogleChromeProcessCreator;
import main.process.xdotoolProcess.XdotoolProcessCreator;

public class NamedProcess {
    public Process process;
    public boolean exitAskedByUser = false;
    @Setter
    @Getter
    private String name;

    Process xdotoolProcess;
    Process closeGoogleChromeProcess;

    public NamedProcess() {
        name = "";
        process = null;
    }

    public NamedProcess(Process xdotoolProcess) {
        name = "";
        process = null;
        this.xdotoolProcess = xdotoolProcess;
    }

    public NamedProcess(Process xdotoolProcess, Process closeGoogleChromeProcess) {
        name = "";
        process = null;
        this.xdotoolProcess = xdotoolProcess;
        this.closeGoogleChromeProcess = closeGoogleChromeProcess;
    }

    public void destroy() {
        process.destroy();
        if(xdotoolProcess != null){
            xdotoolProcess.destroy();
        }
        if(closeGoogleChromeProcess != null){
            closeGoogleChromeProcess.destroy();
        }
    }

    public void set(Process p) {
        process = p;
    }

    public Process get() {
        return process;
    }
}

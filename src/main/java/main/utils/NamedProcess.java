package main.utils;

import lombok.Getter;
import lombok.Setter;

public class NamedProcess {
    public Process process;
    public boolean exitAskedByUser = false;
    @Setter
    @Getter
    private String name;

    public NamedProcess() {
        name = "";
        process = null;
    }

    public void destroy() {
        process.destroy();
    }

    public void set(Process p) {
        process = p;
    }

    public Process get() {
        return process;
    }
}

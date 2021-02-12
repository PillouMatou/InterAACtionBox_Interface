package main.utils;

import lombok.Getter;
import lombok.Setter;

public class NamedProcess {
    @Setter
    @Getter
    private String name;
    public Process process;

    public boolean exitAskedByUser = false;

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

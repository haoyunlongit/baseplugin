package com.steven.stevenPlugin;

/**
 * @Autor : yunlong20
 * @Date : on 2024-01-13
 * @Description :
 */
public class PatchExt {

    private boolean debugOn;

    private String applicationName;

    private String output;

    public PatchExt() {

    }

    public PatchExt(boolean debugOn) {
        this.debugOn = debugOn;
    }

    public PatchExt(boolean debugon, String applicationName, String output) {
        this.debugOn = debugon;
        this.applicationName = applicationName;
        this.output = output;
    }

    public boolean isDebugon() {
        return debugOn;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getOutput() {
        return output;
    }

    public void setDebugon(boolean debugon) {
        this.debugOn = debugon;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}

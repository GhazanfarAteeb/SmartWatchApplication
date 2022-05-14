package com.app.smartwatchapplication.Modals;

public class Watch {
    public String watchName;
    public String watchMacAddress;

    public Watch() {
    }
    public Watch(String watchName, String watchMacAddress) {
        this.watchName = watchName;
        this.watchMacAddress = watchMacAddress;
    }
    public String getWatchName() {
        return watchName;
    }
    public void setWatchName(String watchName) {
        this.watchName = watchName;
    }
    public String getWatchMacAddress() {
        return watchMacAddress;
    }
    public void setWatchMacAddress(String watchMacAddress) {
        this.watchMacAddress = watchMacAddress;
    }
}

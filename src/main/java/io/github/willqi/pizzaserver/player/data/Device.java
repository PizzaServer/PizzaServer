package io.github.willqi.pizzaserver.player.data;

public enum Device {

    WINDOWS_10(7, "Windows 10");

    private int deviceOS;
    private String name;

    Device(int deviceOS, String name) {
        this.deviceOS = deviceOS;
        this.name = name;
    }

    public int getDeviceOS() {
        return this.deviceOS;
    }

    public String getName() {
        return this.name;
    }

    public static Device getPlatformByOS(int deviceOS) {
        for (Device device : Device.values()) {
            if (device.getDeviceOS() == deviceOS) {
                return device;
            }
        }
        return null;
    }

}

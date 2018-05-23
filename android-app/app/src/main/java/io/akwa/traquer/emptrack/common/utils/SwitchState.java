package io.akwa.traquer.emptrack.common.utils;

public enum SwitchState {
    ON("On"), OFF("Off");

    private final String name;

    SwitchState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

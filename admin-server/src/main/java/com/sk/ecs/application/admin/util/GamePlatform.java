package com.sk.ecs.application.admin.util;

import lombok.Getter;

@Getter
public enum GamePlatform {
    Unknown(-1, "未知"),
    General(0, "通用"),
    Android(1, "安卓"),
    IOS(2, "IOS"),
    ;

    private final int value;

    private final String name;

    GamePlatform(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static GamePlatform valueOf(int value) {
        return switch (value) {
            case 0 -> General;
            case 1 -> Android;
            case 2 -> IOS;
            default -> Unknown;
        };
    }

    public static GamePlatform valueOf(Integer value) {
        if (value == null) {
            return General;
        }
        return valueOf(value.intValue());
    }
}

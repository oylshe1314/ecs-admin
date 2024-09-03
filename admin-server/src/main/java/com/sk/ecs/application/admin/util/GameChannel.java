package com.sk.ecs.application.admin.util;

import lombok.Getter;

@Getter
public enum GameChannel {
    Unknown(-1, "未知"),
    General(0, "通用"),
    WechatMiniGame(1, "微信小游戏"),
    TapTap(2, "TapTap"),
    ;

    private final int value;

    private final String name;

    GameChannel(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static GameChannel valueOf(int value) {
        return switch (value) {
            case 0 -> General;
            case 1 -> WechatMiniGame;
            case 2 -> TapTap;
            default -> Unknown;
        };
    }

    public static GameChannel valueOf(Integer value) {
        if (value == null) {
            return General;
        }
        return valueOf(value.intValue());
    }
}

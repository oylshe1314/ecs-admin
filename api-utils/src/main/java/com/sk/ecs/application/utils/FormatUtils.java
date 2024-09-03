package com.sk.ecs.application.utils;

import java.util.regex.Pattern;

public abstract class FormatUtils {

    public static boolean checkUsername(String arg) {
        return false;
    }

    public static boolean checkPhone(String arg) {
        return false;
    }

    public static boolean checkEmail(String arg) {
        return false;
    }

    public static boolean checkPassword(String arg) {
        return false;
    }

    public static boolean isPhone(String arg) {
        return Pattern.matches("^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$", arg);
    }

    public static boolean isEmail(String arg) {
        return false;
    }
}

package com.example.wataru.ambitionlist.error;

/**
 * Created by wataru on 2014/11/03.
 */
public class ErrorLog {
    private static String toastText;

    public static String getToastText() {
        return toastText;
    }

    public static void setToastText(String toastText) {
        ErrorLog.toastText = toastText;
    }
}

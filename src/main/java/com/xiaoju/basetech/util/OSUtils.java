package com.xiaoju.basetech.util;

/**
 * os utils
 *
 */
public class OSUtils {


    /**
     * whether is macOS
     */
    public static boolean isMacOS() {
        String os = System.getProperty("os.name");
        return os.startsWith("Mac");
    }


    /**
     * whether is windows
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.startsWith("Windows");
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        return os.startsWith("Linux");
    }


}

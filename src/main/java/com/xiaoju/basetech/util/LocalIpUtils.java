package com.xiaoju.basetech.util;

import java.net.SocketException;

public class LocalIpUtils {

    public static String getTomcatBaseUrl() {
        try {
            String localIp = GetIPAddress.getLinuxLocalIp();
            return "http://" + localIp + ":8899/";
        } catch (SocketException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}

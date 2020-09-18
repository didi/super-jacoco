package com.xiaoju.basetech.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @description:
 * @author: charlynegaoweiwei
 * @time: 2020/4/16 9:28 PM
 */
public class GetIPAddress {
    /**
       * 获取Linux下的IP地址
       *
       * @return IP地址
       * @throws SocketException
       */
    public static String getLinuxLocalIp() throws SocketException {
           String ip = "";
           try {
                 for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                      en.hasMoreElements();) {
                       NetworkInterface intf = en.nextElement();
                       String name = intf.getName();
                       if (!name.contains("docker") && !name.contains("lo")) {
                             for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                                              enumIpAddr.hasMoreElements();) {
                                  InetAddress inetAddress = enumIpAddr.nextElement();
                                   if (!inetAddress.isLoopbackAddress()) {
                                       String ipaddress = inetAddress.getHostAddress().toString();
                                         if (!ipaddress.contains("::") && !ipaddress.contains("0:0:")
                                             && !ipaddress.contains("fe80")) {
                                              ip = ipaddress;
                                             }
                                       }
                                 }
                           }
                     }
               } catch (SocketException ex) {
                 System.out.println("获取ip地址异常");
                 ex.printStackTrace();
               }
           System.out.println("IP:" + ip);
           return ip;
         }
    public static void main(String[] args) throws SocketException {
        GetIPAddress.getLinuxLocalIp();



    }
}

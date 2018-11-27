package com.xiaoju.hallowmas.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yehonggang on 17/7/14.
 */
public class Scpclient {
    // public Scpclient(){}
    static private Scpclient instance;

    static synchronized public Scpclient getInstance(String IP, int port,
                                                     String username, String passward) {
        instance = new Scpclient(IP, port, username, passward);
        return instance;
    }

    public Scpclient(String IP, int port, String username, String passward) {
        this.ip = IP;
        this.port = port;
        this.username = username;
        this.password = passward;
    }


    public void getFile(String remoteFile, String localTargetDirectory) {
        Connection conn = new Connection(ip,port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false) {
                System.err.println("authentication failed");
            }
            SCPClient client = new SCPClient(conn);
            client.get(remoteFile, localTargetDirectory);
            conn.close();
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }


    public void putFile(String localFile, String remoteTargetDirectory) {
        Connection conn = new Connection(ip,port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false) {
                System.err.println("authentication failed");
            }
            SCPClient client = new SCPClient(conn);
            client.put(localFile, remoteTargetDirectory);
            conn.close();
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }


    public void putFile(String localFile, String remoteFileName,String remoteTargetDirectory,String mode) {
        Connection conn = new Connection(ip,port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false) {
                System.err.println("authentication failed");
            }
            SCPClient client = new SCPClient(conn);
            if((mode == null) || (mode.length() == 0)){
                mode = "0600";
            }
            client.put(localFile, remoteFileName, remoteTargetDirectory, mode);

            //重命名
            ch.ethz.ssh2.Session sess = conn.openSession();
            String tmpPathName = remoteTargetDirectory +File.separator+ remoteFileName;
            String newPathName = tmpPathName.substring(0, tmpPathName.lastIndexOf("."));
            sess.execCommand("mv " + remoteFileName + " " + newPathName);//重命名回来

            conn.close();
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
        }
    }

//    public void putFile(String localFile, String remoteFileName,String remoteTargetDirectory) {
//        Connection conn = new Connection(ip,port);
//        try {
//            conn.connect();
//            boolean isAuthenticated = conn.authenticateWithPassword(username,
//                    password);
//            if (isAuthenticated == false) {
//                System.err.println("authentication failed");
//            }
//            SCPClient client = new SCPClient(conn);
//            client.put(getBytes(localFile), remoteFileName, remoteTargetDirectory);
//            conn.close();
//        } catch (IOException ex) {
//            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null,ex);
//        }
//    }

    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream(1024*1024);
            byte[] b = new byte[1024*1024];
            int i;
            while ((i = fis.read(b)) != -1) {
                byteArray.write(b, 0, i);
            }
            fis.close();
            byteArray.close();
            buffer = byteArray.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    private String ip;
    private int port;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

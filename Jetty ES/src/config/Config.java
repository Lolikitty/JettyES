/*
 * Copyright © 2014 Chenghsi Inc. All rights reserved
 */
package config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author Loli, Last modification time : AM 11:57 2014/11/10
 */
public final class Config {

    public static String HTTP_SERVER_PUBLIC_IP = "127.0.01";
    public static String HTTP_SERVER_PRIVATE_IP = "127.0.01";
    public static int HTTP_SERVER_PORT = 8080;
    public static int TCP_SERVER_PORT = 1314;
    public static String SERVER_PATH = System.getProperty("user.dir");
    public static String ROOT_PATH = "";
    public static String SERVER_ROOT_PATH;
    public static String DB_DRIVER = "org.postgresql.Driver";
    public static String DB_URL = "jdbc:postgresql://localhost:5432/test_db";
    public static String DB_USER_NAME = "postgres";
    public static String DB_PASSWORD = "a";

    public Config() {
//        HTTP_SERVER_PUBLIC_IP = getPublicIp();
        HTTP_SERVER_PRIVATE_IP = getPrivateIP();
    }

    String getPrivateIP() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            return ip;
        } catch (UnknownHostException ex) {
            return null;
        }
    }

    String getPublicIp() {
        try {
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(url.openStream()));
                String ip = br.readLine();
                return ip;
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (Exception e) {
            return "Unknown";
        }
    }

}

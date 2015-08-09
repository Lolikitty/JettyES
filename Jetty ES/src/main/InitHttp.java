/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.net.URL;
import java.net.URLConnection;
import config.Config;
import server.http.HttpServer;

/**
 *
 * @author Loli
 */
public class InitHttp {

    void start(String[] args) {
        int defaultPort = Config.HTTP_SERVER_PORT;
        String defaultPath = "src/webapps/ROOT/";

        if (args.length > 0) {
            defaultPort = Integer.parseInt(args[0]);
        }

        if (args.length > 1) {
            defaultPath = args[1];
        }

        if (args.length > 2) {
            if (args[2].equalsIgnoreCase("close")) {
                CloseServer(defaultPort);
            }
            System.exit(1);
        }

        new Thread(new HttpServer(defaultPort, defaultPath)).start();
    }

    void CloseServer(int port) {
        try {
            URL url = new URL("http://127.0.0.1:" + port + "/StatusServer?status=close");
            URLConnection conn = url.openConnection();
            conn.getInputStream();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

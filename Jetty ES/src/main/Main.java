package main;

import server.http.HttpServer;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import server.config.Config;
import server.tcp.TcpServer;
import tool.SystemInformation;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().start(args);
    }

    void start(String[] args) {
        init();
        startHTTP(args);
//        startTCP();
        runSystemData();
    }

    void runSystemData() {
        try {
            new SystemInformation(new Sigar());
        } catch (SigarException | InterruptedException e) {
            System.err.println(e);
        }
    }

    void startHTTP(String[] args) {
        int defaultPort = 8080;
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
//        new Thread(new HttpServer(Config.HTTP_SERVER_PORT, "src/webapps/ROOT/")).start();
    }

    void startTCP() {
        new Thread(new TcpServer()).start(); // if you need ...    
    }

    void init() {
        createDir();
    }

    void createDir() {
        // Create Dir
        File dir = new File("logs");
        if (!dir.exists()) {
            dir.mkdir();
            System.out.println("Create Dir : " + Config.SERVER_PATH);
        }
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

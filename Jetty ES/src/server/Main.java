package server;

import server.http.HttpServer;
import java.io.File;
import server.config.Config;

public class Main {

    public static void main(String[] args) throws Exception {
//        Init();  // if you need ...
        new Thread(new HttpServer(80)).start(); // if you need ...
        new Thread(new HttpServer(Config.HTTP_SERVER_PORT)).start();
//        new Thread(new TcpServer()).start(); // if you need ...
    }

    static void Init() {
        createDir(); // if you need ...
    }

    static void createDir() {
        // Create Dir
        File dir = new File("Data");
        if (!dir.exists()) {
            dir.mkdir();
            System.out.println("Create Dir : " + Config.SERVER_PATH);
        }
    }

}

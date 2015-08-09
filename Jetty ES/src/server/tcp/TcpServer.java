package server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import config.Config;

public class TcpServer implements Runnable {

    @Override
    public void run() {

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(Config.TCP_SERVER_PORT);
        } catch (IOException ex) {
            System.err.println(ex);
            return;
        }

        while (true) {
            try {
                Socket s = ss.accept();
                TcpClient c = new TcpClient();
                c.s = s;
                new Thread(c).start();

            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

}

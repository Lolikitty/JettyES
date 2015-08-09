/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import config.Config;
import server.tcp.TcpServer;
import tool.SystemInformation;

/**
 *
 * @author Loli
 */
public class Init {

    public void start() throws IOException {
        checkIDE();
        runSystemData();
    }

    public void checkIDE() throws IOException {
        try (
                FileReader fr = new FileReader("IDE.conf");
                BufferedReader br = new BufferedReader(fr);) {

            if (br.ready()) {
                String data = br.readLine();
                if (data.equalsIgnoreCase("Netbeans")) {
                    Config.ROOT_PATH = "src";
                } else {
                    Config.ROOT_PATH = "";
                }
                Config.SERVER_ROOT_PATH = Config.SERVER_PATH + "/" + Config.ROOT_PATH;
            }
            fr.close();
        }
    }

    public void runSystemData() {
        try {
            new SystemInformation(new Sigar());
        } catch (SigarException | InterruptedException e) {
            System.err.println(e);
        }
    }
}

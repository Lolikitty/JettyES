/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import config.Config;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.json.JSONObject;

/**
 *
 * @author Loli
 */
public class InitConfig {

    void start() throws IOException {
        readJettyES();
    }

    void readJettyES() throws IOException {
        String data = "";
        try (
                FileReader fr = new FileReader(Config.ROOT_PATH + "/config/JettyES.json");
                BufferedReader br = new BufferedReader(fr);) {
            while (br.ready()) {
                data += br.readLine();
            }
            fr.close();
        }

        JSONObject obj = new JSONObject(data);
        Config.HTTP_SERVER_PORT = obj.getInt("Port");
    }

    void create() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        JSONObject obj = new JSONObject();
        obj.put("Port", 8080);

        try (FileWriter fw = new FileWriter(Config.ROOT_PATH + "/config/JettyES.json")) {
            fw.write(obj.toString(4));
            fw.write("\n");
            fw.flush();
            fw.close();
        }

        JSONObject objDB = new JSONObject();
        objDB.put("Database_Driver", "org.postgresql.Driver");
        objDB.put("Database_URL", "jdbc:postgresql://localhost:5432/test_db");
        objDB.put("Database_User_Name", "postgres");
        objDB.put("Database_Password", "a");

        JSONObject objDBRoot = new JSONObject();
        objDBRoot.put("Company", objDB);
        objDBRoot.put("Sub", objDB);

        try (FileWriter fw = new FileWriter(Config.ROOT_PATH + "/config/Database.json")) {
            fw.write(objDBRoot.toString(4));
            fw.write("\n");
            fw.flush();
            fw.close();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

/**
 *
 * @author Loli
 */
public class NewClass {
     public static void main(String[] args) throws Exception {
        // 連接本機伺服器，連接埠為 80
        URL url = new URL("http://192.168.1.114:8080/MyServlet");

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
            // 兩個參數之間的連接用 & 號串起來
            // 等於 = 的左邊是 Key 鍵，右邊是 Value 值
            wr.write("name=AAA&lover_name=BBB");
            wr.flush();
            
            // 取得回傳資料
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                // 一行一行列印出來
                String msg = "";
                while ((line = rd.readLine()) != null) {
//                    System.out.println(line);
                    msg += line;
                }
                
            }
        }
    }
}

/*
 * 執行這個程式碼之前，需要起動伺服器 Main.java
 * 這個類別用途是教你如何使用傳送與接收資料給伺服器
 * 簡單的來來說，就是模擬客戶端 ( 瀏覽器 )
 *
 */
package example;

import java.io.*;
import java.net.*;

/**
 *
 * @author Loli
 */
public class Example_UploadValue {

    public static void main(String[] args) throws Exception {
        // 連接本機伺服器，連接埠為 80
        URL url = new URL("http://localhost:80/MyServlet");

        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
            // 兩個參數之間的連接用 & 號串起來
            // 等於 = 的左邊是 Key 鍵，右邊是 Value 值
            wr.write("name=馬英九&lover_name=陳水扁");
            wr.flush();
            
            // 取得回傳資料
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                // 一行一行列印出來
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
    }
}

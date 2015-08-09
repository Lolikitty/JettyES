/*
 * 執行這個程式碼之前，需要起動伺服器 Main.java
 * 這個類別用途是教你如何使用傳送與接收資料給伺服器
 * 簡單的來來說，就是模擬客戶端 ( 瀏覽器 )
 *
 */
package example;

import java.io.*;
import java.net.*;
import tool.WWW;

/**
 *
 * @author Loli
 */
public class Example_UploadValue {

    public static void main(String[] args) throws Exception {
        WWW www = null;
        try {
            www = new WWW("http://169.254.50.54/MyUploadFile");

            www.addValue("name", "艾利娜");
            www.addValue("mail", "aaa@loli.com");
            www.addValue("password", "!@#$%^&*()_+");

            www.upload();            

            System.out.println(www.getText()); // 輸出回傳訊息

        } catch (Exception e) {
            System.out.println("[範例練習] 上傳時發生錯誤... : " + e);
        }
        www.close();
    }
}

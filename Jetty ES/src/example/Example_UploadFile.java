/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import tool.WWW;

/**
 *
 * @author Loli
 */
public class Example_UploadFile {

    public static void main(String[] args) {
        WWW www = null;
        try {
            www = new WWW("http://127.0.0.1:8080/MyUploadFile");
          
            www.addValue("name", "艾利娜");
            www.addValue("mail", "aaa@loli.com");
            www.addValue("password", "!@#$%^&*()_+");
            
//            www.addFile("my_file", "C:/Users/Loli/Desktop/A.jpg"); // my_file 為檔案上傳的 Key，所以 Value 為二進制檔
            www.addFile("my_file", "C:/Users/Loli/Desktop/B.jpg"); // 可以上傳多個檔案
//            www.addFile("my_file", "C:/Users/Loli/Desktop/C.txt");
                        
            www.upload();
            
            System.out.println(www.getText()); // 輸出回傳訊息
            
        } catch (Exception e) {
            System.out.println("[範例練習] 上傳時發生錯誤... : " + e);
        }
        www.close();
    }
}

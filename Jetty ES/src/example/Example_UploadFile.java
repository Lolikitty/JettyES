/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import tool.HttpUploadFile;

/**
 *
 * @author Loli
 */
public class Example_UploadFile {

    public static void main(String[] args) {
        try {
            HttpUploadFile http = new HttpUploadFile("http://localhost:80/MyUploadFile");
            http.addFile("my_file", "C:/Users/Loli/Desktop/A.jpg"); // my_file 為檔案上傳的 Key，所以 Value 為二進制檔
            http.addFile("my_file", "C:/Users/Loli/Desktop/B.jpg"); // 可以上傳多個檔案
            http.upload();
            System.out.println(http.text); // 輸出回傳訊息
        } catch (Exception e) {
            System.out.println("[範例練習] 上傳時發生錯誤... : " + e);
        }

    }
}

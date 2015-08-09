/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import tool.WWW;

/**
 *
 * @author Loli
 */
public class NewClass1 {

    public static void main(String[] args) throws IOException {
//        WWW w = new WWW("http://192.168.1.114:8080/MyServlet");
        WWW w = new WWW("http://192.168.1.114:8080/MyUploadFile");
        w.addValue("name", "aaa");
        w.addValue("lover_name", "bbb");
        w.addValue("中文鍵", "中文值");
        w.addFile("my_file", "C:/Users/Loli/Desktop/A.jpg");
        w.addFile("my_file", "C:/Users/Loli/Desktop/B.jpg");
        w.addFile("my_file", "C:/Users/Loli/Desktop/C.jpg");
        w.upload();
        w.close();
        System.out.println(w.getText());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import config.Config;

/**
 *
 * @author Loli
 */
public class InitDir {
    void start() {
        // Create Dir
        File dir = new File("logs");
        if (!dir.exists()) { // 如果資料夾不存在
            dir.mkdir(); // 就創建資料夾吧...
        }
    }
}

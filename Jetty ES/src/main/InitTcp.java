/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import server.tcp.TcpServer;

/**
 *
 * @author Loli
 */
public class InitTcp {

    void start() {
        new Thread(new TcpServer()).start(); // if you need ...
    }
}

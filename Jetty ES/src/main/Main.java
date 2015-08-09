package main;

import config.Config;

public class Main {

    public static void main(String[] args) throws Exception {
        new Init().start();
        new InitDir().start();
        new InitConfig().start();
        new InitHttp().start(args);
    }
}

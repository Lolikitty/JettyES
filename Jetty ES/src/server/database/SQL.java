/*
 * Copyright © 2014 Chenghsi Inc. All rights reserved
 */
package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import server.config.Config;

/**
 * @author Loli, Last modification time : AM 11:57 2014/11/10
 */
public class SQL {    

    public void setData(String sql)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {

        Connection c = null;
        Statement s = null;
        try {
            Class.forName(Config.DB_DRIVER).newInstance();
            c = DriverManager.getConnection(
                    Config.DB_URL, Config.DB_USER_NAME, Config.DB_PASSWORD);
            s = c.createStatement();
            s.execute(sql);
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (Exception e) {
            }
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public ResultSet getData(String sql)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName(Config.DB_DRIVER).newInstance();
            c = DriverManager.getConnection(
                    Config.DB_URL, Config.DB_USER_NAME, Config.DB_PASSWORD);
            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs;
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
            }
        }
    }

}

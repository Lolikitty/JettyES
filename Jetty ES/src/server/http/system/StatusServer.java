/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.http.system;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Loli
 */
public class StatusServer extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try (PrintWriter out = res.getWriter()) {
            if (req.getRemoteAddr().equals("127.0.0.1")) {
                if (req.getParameter("status").equalsIgnoreCase("close")) {
                    System.exit(1);
                }
            }
        }
    }
}

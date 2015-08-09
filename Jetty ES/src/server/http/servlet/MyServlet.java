package server.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {

    public MyServlet(){
        System.out.println("ok 5");
    }
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8"); // HTML
//        res.setContentType("application/json;charset=UTF-8"); // Json
        try (PrintWriter out = res.getWriter()) {
            out.println("Hello2");
            
            out.println(req.getParameter("name"));

            String name = req.getParameter("name");
            if (name != null) {
                out.println("<br><br>");
                out.println("你的名子是：" + name);
            }
            String lover_name = req.getParameter("lover_name");
            if (lover_name != null) {
                out.println("<br><br>");
                out.println("你的情人是：" + lover_name);
            }
            String chineseKey = req.getParameter("中文鍵");
            if (lover_name != null) {
                out.println("<br><br>");                
                out.println("中文鍵 內的 中文值：" + chineseKey);
            }
        }

    }

}

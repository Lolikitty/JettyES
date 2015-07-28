package server.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import server.config.Config;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 500, // 10MB
        maxRequestSize = 1024 * 1024 * 500)   // 50MB

public class MyUploadFile extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {
            try {
                Part p = req.getPart("my_file"); // 就是 upload.html 的  <input type="file" name="my_file" /> 裡面的 name 命名

//            p.write("A.jpg"); // 也可以直接命名，這樣他會保存在專案根目錄中 ~
                String fileName = extractFileName(p); // 取得檔案名稱
                p.write("/src/webapps/ROOT/" + fileName); // 保存到指定目錄下

                out.println("Upload  Finish !!"); // 顯示上傳成功
            } catch (Exception e) {
                // 如果在 try 中發生錯誤
                out.println("Error : " + e); // 顯示錯誤訊息
            }

        }
    }

    // Get File Extension Name 取得副檔名
    public static String getExtension(String fileName) {
        int startIndex = fileName.lastIndexOf(46) + 1;
        int endIndex = fileName.length();
        return fileName.substring(startIndex, endIndex);
    }

    // Get File Name 取得檔案名稱
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

}

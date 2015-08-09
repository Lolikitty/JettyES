/*
* If You Need More Information To Setting
* You Can Read This : http://www.eclipse.org/jetty/documentation/current/embedding-jetty.html
*/

package server.http;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import server.http.servlet.MyUploadFile;
import config.Config;
import javax.servlet.MultipartConfigElement;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import server.http.servlet.MyServlet;
import server.http.system.StatusServer;
import server.http.system.SysInfo;

public class HttpServer implements Runnable {

    private Server server = null;
    private int port = 0;
    private String base = null;

    public HttpServer(int port, String base) {
        this.port = port;
        this.base = base;
    }

    @Override
    public void run() {

        // -------------- Setup Threadpool        
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(500);

        server = new Server(threadPool);

        // -------------- Init        
        ServerConnector http = new ServerConnector(server);
        http.setPort(port);
        http.setIdleTimeout(1000 * 60 * 60);        
        server.addConnector(http);

        WebAppContext wac = new WebAppContext();
        wac.setResourceBase(base);

        ServletContextHandler context = wac;

        // =====================================================================        
        // 如果你要加入 新頁面 (Servlet)，請在這裡面加入 (等於(//=====)的註解內)
        // 加入方式有兩種，第一種是一般的頁面，第二種是上傳頁面
        // 一般頁面只要加入一行，上傳頁面需要加入三行( 記得 sh 的變數名稱也要改
        
        // ------------- Add Servlets
        // Add Upload Servlet 上傳的 Servlet 如果要新增頁面，要加入下面這三行，而非一般的一行
        ServletHolder sh = new ServletHolder(new MyUploadFile());
        sh.getRegistration().setMultipartConfig(new MultipartConfigElement(Config.SERVER_ROOT_PATH));
        context.addServlet(sh, "/MyUploadFile");

        // Add General Servlet 加入一般的 Servlet
        context.addServlet(new ServletHolder(new MyServlet()), "/MyServlet");

        // =====================================================================
        
        // Add System Servlet 不要動
        context.addServlet(new ServletHolder(new StatusServer()), "/StatusServer");
        context.addServlet(new ServletHolder(new SysInfo()), "/SysInfo");

        // ---------- Close Temp Buffer 不要動
        DefaultServlet defaultServlet = new DefaultServlet();

        ServletHolder holder = new ServletHolder(defaultServlet);

        holder.setInitParameter("useFileMappedBuffer", "false");
        holder.setInitParameter("cacheControl", "max-age=0, public");

        context.addServlet(holder, "/");

        // === jetty-requestlog.xml ===
        NCSARequestLog requestLog = new NCSARequestLog();
        requestLog.setFilename(Config.SERVER_PATH + "/logs/yyyy_mm_dd.request.log");
        requestLog.setFilenameDateFormat("yyyy_MM_dd");
        requestLog.setRetainDays(90);
        requestLog.setAppend(true);
        requestLog.setExtended(true);
        requestLog.setLogCookies(false);
        requestLog.setLogTimeZone("GMT");
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        requestLogHandler.setRequestLog(requestLog);

        // ----------- Add Handler
        HandlerList hl = new HandlerList();
        hl.addHandler(requestLogHandler);
        hl.addHandler(context);

        server.setHandler(hl);

        // ---------- Start Server
        try {
            server.start();
//            openBrowse();
            server.join();
        } catch (Exception ex) {
            System.out.println("伺服器狀態：啟動時發生錯誤  " + ex);
        }

    }

    void openBrowse() {
        try {
            Desktop.getDesktop().browse(URI.create("http://127.0.0.1:" + port));
        } catch (IOException ex) {
            System.out.println("無法開啟網頁");
        }
    }
}

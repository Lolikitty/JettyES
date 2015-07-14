package server.http;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.http.servlet.MyUploadFile;
import server.config.Config;
import javax.servlet.MultipartConfigElement;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import server.http.servlet.MyServlet;
import server.http.system.StatusServer;

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

        server = new Server(port);

        if (server == null) {
            System.err.println("Http Server Error !, Check Port No Using ...");
            return;
        }

        // -------------- Init
        ServerConnector c = new ServerConnector(server);
        c.setIdleTimeout(1000 * 60 * 60);
        server.addConnector(c);

        WebAppContext wac = new WebAppContext();
        wac.setResourceBase(base);

        ServletContextHandler context = wac;

        // ------------- Add Servlets
        // Add Upload Servlet
        ServletHolder sh = new ServletHolder(new MyUploadFile());
        sh.getRegistration().setMultipartConfig(new MultipartConfigElement(Config.SERVER_PATH));
        context.addServlet(sh, "/MyUploadFile");

        // Add General Servlet
        context.addServlet(new ServletHolder(new MyServlet()), "/MyServlet");
        
        // Add System Servlet
        context.addServlet(new ServletHolder(new StatusServer()), "/StatusServer");

        // ---------- Close Temp Buffer
        DefaultServlet defaultServlet = new DefaultServlet();

        ServletHolder holder = new ServletHolder(defaultServlet);

        holder.setInitParameter("useFileMappedBuffer", "false");
        holder.setInitParameter("cacheControl", "max-age=0, public");

        context.addServlet(holder, "/");

        // ----------- Add Handler
        HandlerList hl = new HandlerList();
        hl.addHandler(context);

        server.setHandler(hl);

        // ---------- Start Server
        try {
            server.start();
            openBrowse();
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

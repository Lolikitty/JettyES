package server.http;

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

public class HttpServer implements Runnable {

    private Server SERVER = null;

    public HttpServer(int port) {
        SERVER = new Server(port);
    }

    @Override
    public void run() {
        if (SERVER == null) {
            System.err.println("Http Server Error !, Check Port No Using ...");
            return;
        }

        // -------------- Init
        
        ServerConnector c = new ServerConnector(SERVER);
        c.setIdleTimeout(1000 * 60 * 60);
        SERVER.addConnector(c);

        WebAppContext wac = new WebAppContext();
        wac.setResourceBase(".");

        ServletContextHandler context = wac;
        
        // ------------- Add Servlets

        // Add Upload Servlet
        ServletHolder sh = new ServletHolder(new MyUploadFile());
        sh.getRegistration().setMultipartConfig(new MultipartConfigElement(Config.SERVER_PATH));
        context.addServlet(sh, "/MyUploadFile");
        
        // Add General Servlet
        context.addServlet(new ServletHolder(new MyServlet()), "/MyServlet");

        // ---------- Close Temp Buffer
        DefaultServlet defaultServlet = new DefaultServlet();

        ServletHolder holder = new ServletHolder(defaultServlet);

        holder.setInitParameter("useFileMappedBuffer", "false");
        holder.setInitParameter("cacheControl", "max-age=0, public");

        context.addServlet(holder, "/");

        // ----------- Add Handler
        HandlerList hl = new HandlerList();
        hl.addHandler(context);

        SERVER.setHandler(hl);

        // ---------- Start Server
        try {
            SERVER.start();
            SERVER.join();
        } catch (Exception ex) {
            System.out.println("伺服器狀態：啟動時發生錯誤  " + ex);
        }

    }
}

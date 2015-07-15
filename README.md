# Jetty ES 
Jetty ES (Jetty Embedded Server)

Jetty ES 是嵌入式Web容器，他的 main() 方法在 Jetty ES/src/server/Main.java 中 <br>

這個專案是用 Netbeans 開發，Netbeans 可以直接開啟 <br>

網頁 jsp/html 等檔案放在 Jetty ES/src/webapps/ROOT/ 中 <br>

Server 可以寫在 Jetty ES/src/server/http/servlet/ 裡 <br>

但是只有寫 Servlet 可跑步起來，需要設定 WEB-INF/web.xml 路由 <br>

但是我們是嵌入式的，所以您可以寫在 Jetty ES/src/server/http/HttpServer.java 中 <br>

裡面有一般 Servlet 的 Hello World  <br>

還有比較特殊的上傳 Servlet 使用 <br>



# Jetty ES 
Jetty ES (Jetty Embedded Server)

Jetty ES 是嵌入式Web容器，他的 main() 方法在 src/main/Main.java 中 <br>

這個專案是用 Netbeans 開發，Netbeans 可以直接開啟，要注意的是此專案使用 Java 8 與 Netbeans 8 <br>

如果你的版本不一樣，可以新建一個專案，把 src 與 lib 複製到新專案中，再進行設定，即可使用<br><br>

網頁 jsp/html 等檔案放在 src/webapps/ROOT/ 中 <br>

Servlet 可以寫在 src/server/http/servlet/ 裡 <br>

但是只有寫 Servlet 可跑不起來，需要設定 WEB-INF/web.xml 路由 <br>

但是我們是嵌入式的，所以您可以寫在src/server/http/HttpServer.java 中 <br>

裡面有一般 Servlet 的 Hello World  <br>

還有比較特殊的上傳 Servlet 使用 <br>

<img src="https://raw.githubusercontent.com/Lolikitty/JettyES/master/Github/DirGettingStart.png" />
<br>
# 取得系統資訊
<br>
<img src="https://raw.githubusercontent.com/Lolikitty/JettyES/master/Github/SysInfo.png" />


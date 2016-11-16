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
這個功能可以做很多事情，例如使用負載平衡，當其中一台伺服器CPU、網路滿載，或者當機掛掉，其他台電腦可以接手繼續處理，伺服器並不會因此而癱瘓掉。<br><br>
或者是伺服器當機掛掉，其他台電腦一偵測到異常 (無回應、記憶體不足、硬碟空間不足...等等)，可以發信箱or手機簡訊給管理員，在第一時間搶救電腦。<br><br>
簡單來說有這個功能，系統會穩定不少。
<br><br>
<img src="https://raw.githubusercontent.com/Lolikitty/JettyES/master/Github/SysInfo.png" />

# 部屬注意事項

如果你是想要用 cmd 執行 Jetty ES:

 - 請將 Jetty ES 根目錄的 IDE.conf 複製到 .jar 目錄下，並且把 IDE.conf 內容的 NetBeans 刪除
 - src/webapps 資料夾請記得複製到 .jar 目錄下
 - src/config 資料夾請記得複製到 .jar 目錄下
 - dist/lib 中的建置出的 library 資料，請把所有裡面的有資料夾的 .jar libraray 複製到 src/lib 目錄，否則執行會找不到 library 發生錯誤

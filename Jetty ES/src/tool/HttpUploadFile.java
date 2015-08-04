/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 *
 * @author Loli
 */
public class HttpUploadFile {

    private String url;
    private ArrayList<String[]> fileList = new ArrayList<>();
    private long allFilesLength = 0;
    private long count = 0;

    public String text = "";

    public HttpUploadFile(String url) {
        this.url = url;
    }

    public void addFile(String key, String path) {
        // [0] = key
        // [1] = path
        fileList.add(new String[]{key, path});
    }

    public void upload() throws MalformedURLException, IOException {
        String charset = "UTF-8";
        String param = "value";
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        // --- 取得所有檔案長度 (給進度條使用) ---
        count = 0;
        allFilesLength = 0;
        for (String[] fileInfo : fileList) {
            allFilesLength += new File(fileInfo[1]).length();
        }

        // --- HTTP ---
        URLConnection conn = new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        HttpURLConnection httpConn = (HttpURLConnection) conn; // 回傳使用

        try (
                OutputStream output = conn.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
                InputStreamReader in = new InputStreamReader(httpConn.getInputStream());
                BufferedReader br = new BufferedReader(in);) {

            // Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(param).append(CRLF).flush();

            // 上傳 每個檔案
            // fileInfo [0] = key
            // fileInfo [1] = path                
            for (String[] fileInfo : fileList) {
                File binaryFile = new File(fileInfo[1]);
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"" + fileInfo[0] + "\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
                writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
                writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                writer.append(CRLF).flush();

                // Upload File
                try (InputStream is = new FileInputStream(binaryFile)) {
                    byte[] buf = new byte[8192];
                    int c = 0;
                    while ((c = is.read(buf, 0, buf.length)) > 0) {
                        output.write(buf, 0, c);
                        output.flush();
                    }
                }
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
            }
            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();

            // --- 回傳訊息： -----------------------------------------------
            text = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                text += line;
            }
        }
    }
}

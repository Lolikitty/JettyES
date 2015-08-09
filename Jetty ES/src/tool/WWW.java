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
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 *
 * @author Loli
 */
public class WWW {

    private String charset = "UTF-8";
    private String CRLF = "\r\n"; // Line separator required by multipart/form-data.

    private String url;
    private ArrayList<String[]> fileList = new ArrayList<>();
    private ArrayList<String[]> valueList = new ArrayList<>();

    private String text = "";
    private boolean haveFile = false;

    private OutputStream output;
    private OutputStreamWriter outputW;
    private PrintWriter writer;
    private HttpURLConnection httpConn;
    private InputStreamReader in;
    private BufferedReader br;

    public WWW(String url) {
        this.url = url;
    }

    public WWW(String url, String charset) {
        this.url = url;
        this.charset = charset;
    }

    public WWW(String url, String charset, String CRLF) {
        this.url = url;
        this.charset = charset;
        this.CRLF = CRLF;
    }

    public void addFile(String key, String path) {
        // [0] = key
        // [1] = path
        fileList.add(new String[]{key, path});
        haveFile = true;
    }

    public void addValue(String key, String value) {
        // [0] = key
        // [1] = value
        valueList.add(new String[]{key, value});
    }

    public String getText() {
        return text;
    }

    public void upload() throws MalformedURLException, IOException {

        // --- HTTP ---
        URLConnection conn = new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);

        String boundary = null; // Just generate some unique random value.

        if (haveFile) {
            boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        } else {
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }

        try (
                OutputStream output = conn.getOutputStream();
                OutputStreamWriter outputW = new OutputStreamWriter(output, charset);
                PrintWriter writer = new PrintWriter(outputW, true);) {

            this.output = output;
            this.outputW = outputW;
            this.writer = writer;

            if (haveFile) {
                // 上傳每個參數 -------------------------------------------------                
                // kv [0] = key
                // kv [1] = path
                for (String[] kv : valueList) {
                    writer.append("--" + boundary).append(CRLF);
                    writer.append("Content-Disposition: form-data; name=\"" + kv[0] + "\"").append(CRLF);
                    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                    writer.append(CRLF).append(kv[1]).append(CRLF).flush();
                }
                // 上傳 每個檔案 ------------------------------------------------
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
                        is.close();
                        buf = null;
                    }
                    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
                }
                // End of multipart/form-data.
                writer.append("--" + boundary + "--").append(CRLF).flush();

            } else { // Haven't File , 如果沒有夾帶檔案
                // 上傳每個參數 -------------------------------------------------
                for (int i = 0; i < valueList.size(); i++) {
                    String[] kv = valueList.get(i);
                    String data
                            = URLEncoder.encode(kv[0], charset)
                            + "="
                            + URLEncoder.encode(kv[1], charset);
                    if (i < valueList.size() - 1) {
                        data += "&";
                    }
                    writer.append(data);
                }
                writer.append(CRLF).flush();
            }

            // 回傳訊息 ---------------------------------------------------------
            HttpURLConnection httpConn = (HttpURLConnection) conn; // 回傳使用
            this.httpConn = httpConn;
            try (InputStreamReader in = new InputStreamReader(httpConn.getInputStream());
                    BufferedReader br = new BufferedReader(in);) {
                this.in = in;
                this.br = br;
                text = "";
                String line = "";
                while ((line = br.readLine()) != null) {
                    text += line;
                }
                close();
            }
        }
    }

    public void close() {
        try {
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
        }
        try {
            if (outputW != null) {
                outputW.close();
            }
        } catch (IOException e) {
        }
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (Exception e) {
        }
        try {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        } catch (Exception e) {
        }
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
        }
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
        }
    }

}

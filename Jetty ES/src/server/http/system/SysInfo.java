/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.http.system;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import tool.SystemInformation;

/**
 *
 * @author Loli
 */
public class SysInfo extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = res.getWriter()) {
            JSONObject root = new JSONObject();

            try {

                JSONObject net = new JSONObject();
                net.put("Download_B", SystemInformation.DOWNLOAD);
                net.put("Upload_B", SystemInformation.UPLOAD);
                root.put("Network_B", net);

//                JSONObject netF = new JSONObject();
//                netF.put("Download_Fmt", SystemInformation.DOWNLOAD_FORMAT);
//                netF.put("Upload_Fmt", SystemInformation.UPLOAD_FORMAT);
//                root.put("Network_Fmt", netF);

                // ------------------------------------------- Cpu Sys
                JSONObject cpuSys = new JSONObject();
                int k = 0;
                for (int i : SystemInformation.CPU_USAGE) {
                    cpuSys.put("Usage" + (k++), i);
                }
                cpuSys.put("UsageAll", SystemInformation.CPU_USAGE_ALL);
                root.put("CpuSys_%", cpuSys);

                // ------------------------------------------- Cpu App
                JSONObject cpuApp = new JSONObject();
                cpuApp.put("SysUsage", SystemInformation.CPU_USAGE_SYSTEM);
                cpuApp.put("AppUsage", SystemInformation.CPU_USAGE_APP);
                root.put("CpuApp_%", cpuApp);

                // ------------------------------------------- Thread Count App
                JSONObject appThread = new JSONObject();
                appThread.put("Count", SystemInformation.THREAD_COUNT);
                appThread.put("DaemonCount", SystemInformation.THREAD_COUNT_DAEMON);
                appThread.put("PeakCount", SystemInformation.THREAD_COUNT_PEAK);
                root.put("AppThread", appThread);

                // ------------------------------------------- Ram App
                JSONObject ramApp = new JSONObject();
                ramApp.put("Total", SystemInformation.RAM_TOTAL_MB);
                ramApp.put("Max", SystemInformation.RAM_MAX_MB);
                ramApp.put("Free", SystemInformation.RAM_FREE_MB);
                root.put("RamApp_MB", ramApp);

                // ------------------------------------------- Ram Sys
                JSONObject ramSys = new JSONObject();
                ramSys.put("PhysTotal", SystemInformation.TOTAL_PHYSICAL_MEMORY_SIZE_MB);
                ramSys.put("PhysFree", SystemInformation.FREE_PHYSICAL_MEMORY_SIZE_MB);
                ramSys.put("SwapTotal", SystemInformation.TOTAL_SWAP_SPACE_SIZE_MB);
                ramSys.put("SwapFree", SystemInformation.FREE_SWAP_SPACE_SIZE_MB);
                root.put("RamSys_MB", ramSys);

                // ------------------------------------------- Hdd Sys            
                JSONArray hddSys = new JSONArray();

                for (int i = 0; i < SystemInformation.HDD_NAME.size(); i++) {
                    JSONObject hdd = new JSONObject();
                    hdd.put("Name", SystemInformation.HDD_NAME.get(i));
                    hdd.put("Total", SystemInformation.HDD_TOTAL_SPACE_GB.get(i));
                    hdd.put("Free", SystemInformation.HDD_FREE_SPACE_GB.get(i));
                    hddSys.put(hdd);
                }
                root.put("HddSys_GB", hddSys);
                // ------------------------------------------- Add Root
            } catch (Exception e) {
                out.print("error");
            }
            out.print(root.toString(4));//036670221
        }
    }
}

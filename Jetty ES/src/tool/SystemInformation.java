package tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.io.File;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class SystemInformation {

    // ---------------------- Network
    public static long UPLOAD;
    public static long DOWNLOAD;
    public static String UPLOAD_FORMAT;
    public static String DOWNLOAD_FORMAT;

    // ---------------------- ManagementFactory
    public static int THREAD_COUNT;
    public static int THREAD_COUNT_DAEMON;
    public static int THREAD_COUNT_PEAK;

    public static long RAM_MAX_MB;
    public static long RAM_FREE_MB;
    public static long RAM_TOTAL_MB;

    public static long TOTAL_PHYSICAL_MEMORY_SIZE_MB;
    public static long FREE_PHYSICAL_MEMORY_SIZE_MB;
    public static long TOTAL_SWAP_SPACE_SIZE_MB;
    public static long FREE_SWAP_SPACE_SIZE_MB;

    public static int CPU_USAGE_SYSTEM;
    public static int CPU_USAGE_APP;
    
    // 系统磁盘使用情况
    public static ArrayList<String> HDD_NAME = new ArrayList<>();
    public static ArrayList<Long> HDD_TOTAL_SPACE_GB = new ArrayList<>();
    public static ArrayList<Long> HDD_FREE_SPACE_GB = new ArrayList<>();

    // =========================================================================
    private Map<String, Long> rxCurrentMap = new HashMap<String, Long>();
    private Map<String, List<Long>> rxChangeMap = new HashMap<String, List<Long>>();
    private Map<String, Long> txCurrentMap = new HashMap<String, Long>();
    private Map<String, List<Long>> txChangeMap = new HashMap<String, List<Long>>();
    private Sigar sigar;

    // CPU Usage :
    public static int CPU_USAGE_ALL = 0;
    public static ArrayList<Integer> CPU_USAGE = new ArrayList<>();
    

    /**
     * @param s
     * @throws InterruptedException
     * @throws SigarException
     *
     */
    public SystemInformation(Sigar s) throws SigarException, InterruptedException {
        sigar = s;
        getMetric();
        System.out.println(networkInfo());
        Thread.sleep(1000);
        // Run :
        sysNetSpeed(1000);
        sysCpuUsage(1000);
        appCpuUsage(1000);
        appThreadInfo(1000);
        sysHddInfo(1000);
        appRamInfo(1000);
        sysRamInfo(1000);
    }

    private String networkInfo() throws SigarException {
        String info = sigar.getNetInfo().toString();
        info += "\n" + sigar.getNetInterfaceConfig().toString();
        return info;
    }

    private String getDefaultGateway() throws SigarException {
        return sigar.getNetInfo().getDefaultGateway();
    }

    private Long[] getMetric() throws SigarException {
        for (String ni : sigar.getNetInterfaceList()) {
            // System.out.println(ni);
            NetInterfaceStat netStat = sigar.getNetInterfaceStat(ni);
            NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ni);
            String hwaddr = null;
            if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
                hwaddr = ifConfig.getHwaddr();
            }
            if (hwaddr != null) {
                long rxCurrenttmp = netStat.getRxBytes();
                saveChange(rxCurrentMap, rxChangeMap, hwaddr, rxCurrenttmp, ni);
                long txCurrenttmp = netStat.getTxBytes();
                saveChange(txCurrentMap, txChangeMap, hwaddr, txCurrenttmp, ni);
            }
        }
        long totalrx = getMetricData(rxChangeMap);
        long totaltx = getMetricData(txChangeMap);
        for (List<Long> l : rxChangeMap.values()) {
            l.clear();
        }
        for (List<Long> l : txChangeMap.values()) {
            l.clear();
        }
        return new Long[]{totalrx, totaltx};
    }

    private long getMetricData(Map<String, List<Long>> rxChangeMap) {
        long total = 0;
        for (Entry<String, List<Long>> entry : rxChangeMap.entrySet()) {
            int average = 0;
            for (Long l : entry.getValue()) {
                average += l;
            }
            total += average / entry.getValue().size();
        }
        return total;
    }

    private void saveChange(Map<String, Long> currentMap,
            Map<String, List<Long>> changeMap, String hwaddr, long current,
            String ni) {
        Long oldCurrent = currentMap.get(ni);
        if (oldCurrent != null) {
            List<Long> list = changeMap.get(hwaddr);
            if (list == null) {
                list = new LinkedList<Long>();
                changeMap.put(hwaddr, list);
            }
            list.add((current - oldCurrent));
        }
        currentMap.put(ni, current);
    }

    private void sysNetSpeed(int sleep) {
        new Thread(() -> {
            while (true) {
                try {
                    Long[] m = getMetric();
                    long totalrx = m[0];
                    long totaltx = m[1];
                    UPLOAD = totaltx;
                    DOWNLOAD = totalrx;
                    UPLOAD_FORMAT = Sigar.formatSize(totaltx);
                    DOWNLOAD_FORMAT = Sigar.formatSize(totalrx);
                    Thread.sleep(sleep);
                } catch (SigarException | InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    private void sysCpuUsage(int sleep) {
        new Thread(() -> {
            while (true) {
                try {
                    CpuPerc perc = sigar.getCpuPerc();
                    CPU_USAGE_ALL = (int) (perc.getCombined() * 100);

                    CpuPerc[] cpuPercs = sigar.getCpuPercList();

                    CPU_USAGE.clear();
                    for (CpuPerc cpuPerc : cpuPercs) {
                        CPU_USAGE.add((int) (cpuPerc.getCombined() * 100));
                    }
                    Thread.sleep(sleep);
                } catch (SigarException | InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    private void appCpuUsage(int sleep) {
        new Thread(() -> {
            OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            while (true) {
                try {
                    CPU_USAGE_SYSTEM = (int) (osmb.getSystemCpuLoad() * 100); // CPU 使用率(系統)
                    CPU_USAGE_APP = (int) (osmb.getProcessCpuLoad() * 100); // CPU 使用率(本程式)
                    Thread.sleep(sleep);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    private void appThreadInfo(int sleep) {
        new Thread(() -> {
            ThreadMXBean t = ManagementFactory.getThreadMXBean();
            while (true) {
                try {
                    THREAD_COUNT = t.getThreadCount(); // 本程式執行緒數量
                    THREAD_COUNT_DAEMON = t.getDaemonThreadCount(); // JVM 執行緒數量
                    THREAD_COUNT_PEAK = t.getPeakThreadCount(); // 最大執行緒數量
                    Thread.sleep(sleep);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    private void sysHddInfo(int sleep) {
        new Thread(() -> {
            File[] roots = File.listRoots(); // 取得硬碟分區
            while (true) {
                try {
                    HDD_NAME.clear();
                    HDD_TOTAL_SPACE_GB.clear();
                    HDD_FREE_SPACE_GB.clear();
                    for (File file : roots) {                        
                        HDD_NAME.add(file.getPath());
                        HDD_TOTAL_SPACE_GB.add(file.getTotalSpace()/ 1024 / 1024 / 1024);
                        HDD_FREE_SPACE_GB.add(file.getFreeSpace() / 1024 / 1024 / 1024);
                    }
                    Thread.sleep(sleep);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    private void appRamInfo(int sleep) {
        new Thread(() -> {
            Runtime lRuntime = Runtime.getRuntime();
            while (true) {
                try {
                    RAM_MAX_MB = lRuntime.maxMemory() / 1024 / 1024; // 本程式最大記憶體
                    RAM_FREE_MB = lRuntime.freeMemory() / 1024 / 1024; // 本程式可用記憶體
                    RAM_TOTAL_MB = lRuntime.totalMemory() / 1024 / 1024; // 本程式總記憶體
                    Thread.sleep(sleep);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    private void sysRamInfo(int sleep) {
        new Thread(() -> {
            OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            while (true) {
                try {
                    TOTAL_PHYSICAL_MEMORY_SIZE_MB = osmb.getTotalPhysicalMemorySize() / 1024 / 1024; // 系統物理記憶體總計
                    FREE_PHYSICAL_MEMORY_SIZE_MB = osmb.getFreePhysicalMemorySize() / 1024 / 1024; // 系統物理記憶體可用
                    TOTAL_SWAP_SPACE_SIZE_MB = osmb.getTotalSwapSpaceSize() / 1024 / 1024; // 系統交換記憶體總計
                    FREE_SWAP_SPACE_SIZE_MB = osmb.getFreeSwapSpaceSize() / 1024 / 1024; // 系統交換記憶體可用
                    Thread.sleep(sleep);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

}

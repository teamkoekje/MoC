package controllers;

// <editor-fold defaultstate="collapsed" desc="imports" >

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import management.WorkspaceManagement;

// </editor-fold>

/**
 * A class that retrieves the system information and puts it in an easily
 * handle-able format.
 *
 * @author TeamKoekje
 */
public class SystemInformation {

    //<editor-fold defaultstate="collapsed" desc="Methods" >
    /**
     * Gets the system information.
     *
     * @return A String indication system information variables, in the format
     * of: "server id":{ "variable name1": "variable value1", "variable name2":
     * "variable value2" }
     */
    public static String getInfo() {
        WorkspaceManagement wmInstance = WorkspaceManagement.getInstance();
        try {
            Runtime runtime = Runtime.getRuntime();
            NumberFormat format = NumberFormat.getInstance();
            StringBuilder sb = new StringBuilder();

            long maxMemory = runtime.maxMemory();
            long allocatedMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            int amountProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
            double cpuUsage = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
            long freeSpace = new File("/").getFreeSpace();
            long usableSpace = new File("/").getUsableSpace();
            long totalSpace = new File("/").getTotalSpace();
            String IP = InetAddress.getLocalHost().getHostAddress();

            sb.append("\"" + wmInstance.getServerId() + "\":{");
            sb.append("\"IP\": \"" + IP + "\",");
            sb.append("\"freediskspace\": \"" + format.format(freeSpace) + "\",");
            sb.append("\"allocateddiskspace\": \"" + format.format(usableSpace) + "\",");
            sb.append("\"totaldiskspace\": \"" + format.format(totalSpace) + "\",");
            sb.append("\"freememory\": \"" + format.format(freeMemory / 1024) + "\",");
            sb.append("\"allocatedmemory\": \"" + format.format(allocatedMemory / 1024) + "\",");
            sb.append("\"maxmemory\": \"" + format.format(maxMemory / 1024) + "\",");
            sb.append("\"totalfreememory\": \"" + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "\",");
            sb.append("\"processoramount\": \"" + amountProcessors + "\",");
            sb.append("\"cpuusage\": \"" + cpuUsage + "\",");
            sb.append("\"workspaces\": \"" + wmInstance.getTeamSize() + "\"");
            sb.append("}");

            return sb.toString();
        } catch (UnknownHostException ex) {
            Logger.getLogger(WorkspaceManagement.class.getName()).log(Level.SEVERE, null, ex);
            return "[SYSINFO] NULL";
        }
    }
    //</editor-fold>
}

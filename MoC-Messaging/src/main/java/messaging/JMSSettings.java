package messaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author TeamKoekje
 */
public class JMSSettings {

    public enum RunMode {

        MANUAL, AUTOMATICALLY
    }

    public static final String URL_ACTIVE_MQ = "tcp://192.168.24.34:61616";

    public static final String BROKER_REQUEST = "BROKER_REQUEST";
    public static final String WORKSPACE_REQUEST = "WORKSPACE_REQUEST";
    public static final String BROKER_REPLY = "BROKER_REPLY";
    public static final String SERVICE_REPLY = "SERVICE_REPLY";

    public static final String WORKSPACE_INIT_REPLY = "WORKSPACE_INIT_REPLY";
    public static final String BROKER_INIT_REQUEST = "BROKER_INIT_REQUEST";

    private static RunMode runMode;
    private static HashMap<String, String> map;

    public static void setRunMode(RunMode runMode) {
        JMSSettings.runMode = runMode;
    }

    public static RunMode getRunMode() {
        return JMSSettings.runMode;
    }

    public static void init(String fileName) {
        runMode = RunMode.AUTOMATICALLY;
        File file = new File(fileName);
        map = new HashMap<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().replaceAll(" ", "");
                StringTokenizer tk = new StringTokenizer(line, "=");
                String key = tk.nextToken();
                String value = tk.nextToken();
                System.out.println(key + "=" + value);
                map.put(key, value);
            }

            scanner.close();
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public synchronized static String get(String queue) {
        return map.get(queue);
    }
}

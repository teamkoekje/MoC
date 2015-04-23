
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class NewMain {

    private static Session session;
    private static Connection connection;

    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            Context jndiContext;
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            props.setProperty(Context.PROVIDER_URL, "tcp://192.168.24.34:61616");
            jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            BytesMessage bm = zipToByteArray("C:\\Users\\Casper\\Desktop\\test.zip");
            bytesMessageToFile("C:\\Users\\Casper\\Desktop\\test2.zip", bm);

        } catch (NamingException | JMSException | IOException ex) {
            System.err.println(ex.getLocalizedMessage());
            System.exit(1);
        }
        System.exit(0);
    }

    private static BytesMessage zipToByteArray(String zipPath) throws JMSException, IOException {
        long total = new File(zipPath).length();
        long current = 0;
        byte[] buffer = new byte[4096];
        int read;
        BytesMessage bm = session.createBytesMessage();
        try (FileInputStream inputStream = new FileInputStream(zipPath)) {
            while ((read = inputStream.read(buffer)) != -1) {
                bm.writeBytes(buffer);
                current += read;
                System.out.println("Reading from zip: " + current + "/" + total);
            }
        }
        bm.reset();
        return bm;
    }

    private static void bytesMessageToFile(String outputFile, BytesMessage bm) throws JMSException, IOException {
        long total = bm.getBodyLength();
        long current = 0;
        byte[] buffer = new byte[4096];
        int read;
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            while ((read = bm.readBytes(buffer)) != -1) {
                current += read;
                outputStream.write(buffer);
                System.out.println("Writing to zip: " + current + "/" + total);
            }
        }
    }
}


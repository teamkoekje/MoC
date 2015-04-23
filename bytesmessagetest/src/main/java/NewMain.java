
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Robin
 */
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

            
            BytesMessage bm = fileToBytesMessage("D:\\testzip.zip");
            bytesMessageToFile("D:\\School\\testzip.zip", bm);
            
            
        } catch (NamingException | JMSException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static BytesMessage fileToBytesMessage(String fileName) throws JMSException {
        File f = new File(fileName);
        byte[] b = new byte[(int) f.length()];
        try (FileInputStream fis = new FileInputStream(f)) {
            fis.read(b);
            fis.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        BytesMessage bm = session.createBytesMessage();
        bm.writeBytes(b);
        return bm;
    }

    private static void bytesMessageToFile(String fileName, BytesMessage bm) throws JMSException {
        bm.reset();
        byte[] b = new byte[(int) bm.getBodyLength()];
        bm.readBytes(b);
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(b);
            fos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

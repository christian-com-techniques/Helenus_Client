import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class ConnectionHandler implements Runnable {

    private boolean shouldRun = true;
    private Config conf;
    private int bufferSize = 2048;
	
    public ConnectionHandler(Config conf) {
    	this.conf = conf;
    }
    
	@Override
	public void run() {
		
		int port = conf.intFor("contactPort");
		
		DatagramSocket rcvSocket = null;
        try {
            rcvSocket = new DatagramSocket(port);
        } catch (SocketException e1) {
            System.out.println("Can't listen on port "+port +"\n");
            return;
        }
        
        byte[] buffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        System.out.println("Waiting for UDP packets: Started");

        while(shouldRun) {
            try {
                rcvSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            String msg = new String(buffer, 0, packet.getLength());

            InputSource source = new InputSource(new StringReader(msg));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            Element a = null;
            
            try {
                db = dbf.newDocumentBuilder();
                Document doc = db.parse(source);
                a = doc.getDocumentElement();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
                   
            if(a.getNodeName() == "lookup") {
 
               	NodeList n = a.getChildNodes();
            	String value = null;
            	
            	for(int i=0;i<n.getLength();i++) {
                	if(n.item(i).getNodeName().equals("value")) {
                		value = n.item(i).getTextContent();
                		break;
                	}
            	}
            	
            	GUI.setResult(value);
            	
            }
            
        }
	}
	
    public void kill() {
        this.shouldRun = false;
    }
	
	
}

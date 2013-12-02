import javax.swing.*;
import java.awt.event.*;
import javax.swing.JPanel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GUI extends JFrame {

    private static final String configFileName = "client.conf";
    private static Config conf;
	private static final long serialVersionUID = 1L;
	private JTextField searchfield;
	private JButton button;
	static String conIP = "";
	static int conPort = 0;
	static JPanel panel3;
	static JLabel searchresults;
	
	public GUI() throws MalformedURLException {
		JFrame fr = new JFrame("Namesearch"); 

		fr.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		URL imgURL = new File("res/back.jpg").toURI().toURL();
		
		JLabel background = new JLabel(new ImageIcon(imgURL));
		background.setLayout(new FlowLayout());
		fr.add(background);
		
		JPanel panel1 = new JPanel(new FlowLayout());
		JPanel panel2 = new JPanel(new GridLayout(3,1));
		panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		Font font1 = new Font("SansSerif", Font.PLAIN, 24);
		
		panel3.setBackground(Color.BLACK);
		panel3.setForeground(Color.WHITE);
		searchresults = new JLabel("");
		searchresults.setForeground(Color.WHITE);
		searchresults.setFont(font1);
		
		panel3.add(searchresults);
		panel3.setVisible(false);
		
		searchfield = new JTextField("", 15);
		searchfield.setFont(font1);
		searchfield.setMaximumSize(new Dimension(100,30));
		
		button = new JButton("Search");
		button.setPreferredSize(new Dimension(100,34));

		URL logoURL = new File("res/logo.png").toURI().toURL();
		JLabel logo = new JLabel(new ImageIcon(logoURL));
		
		
		panel1.add(searchfield);
		panel1.add(button);
		panel1.setOpaque(false);
		panel2.setOpaque(false);
		panel2.add(logo);
		panel2.add(panel1);
		panel2.add(panel3);
		
		background.add(panel2);
        

		fr.pack();
		fr.setSize(800, 600);
		fr.setVisible(true); 
		
		
		searchfield.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                	String searchvalue = searchfield.getText();
    				String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<lookup><key>"+searchvalue+"</key><type>clientrequest</type></lookup>\n";
    				try {
						Supplier.send(conIP, conPort, message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                }
            }
		});
    
		
		button.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				String searchvalue = searchfield.getText();
				String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<lookup><key>"+searchvalue+"</key><type>clientrequest</type></lookup>\n";
				try {
					Supplier.send(conIP, conPort, message);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});
		
	}

	
	public static void setResult(String result) {
		
		panel3.setVisible(true);
		
		if(result.equals("") || result == null) {
			searchresults.setText("No results found.");
		} else {
			searchresults.setText(result);
		}
		
	}
	
	
	public static void main(String[] args) throws MalformedURLException {
		
        try {
            conf = new Config(configFileName);
        }
        catch (IOException e) {
            System.out.println("Failed to load config file: " + configFileName);
            return;
        }
		
		conIP = conf.valueFor("contactIP");
		conPort = conf.intFor("contactPort");
		
        ConnectionHandler connectionHandler = new ConnectionHandler(conf);
        Thread handlerThread = new Thread(connectionHandler, "Connection Handler");
        handlerThread.start();
		
		GUI frame = new GUI();
		
        handlerThread.interrupt();
	}

}

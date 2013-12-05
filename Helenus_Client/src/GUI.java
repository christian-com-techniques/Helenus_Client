import javax.swing.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

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
	static int listenPort = 0;
	static JPanel panel1;
	static JPanel panel3;
	static JLabel logo;
	static JLabel searchresults;
	static JComboBox consistency;
	static JLabel edit;
	JPanel editpanel;
	static JLabel addfeedback = new JLabel("");
	String[] sel = { "ONE", "QUO.", "ALL" };
	JComboBox addconsistency = new JComboBox(sel);
	
	JTextField addkeyfield;
	JTextArea addvaluefield;
	JButton addbutton;
	
	JTextField delkeyfield;
	JButton delbutton;

	JTextField upkeyfield;
	JTextArea upvaluefield;
	JButton upbutton;
	
	
	public GUI() throws MalformedURLException {
		JFrame fr = new JFrame("Namesearch"); 

		fr.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); 
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		URL imgURL = new File("res/back.jpg").toURI().toURL();
		
		JLabel background = new JLabel(new ImageIcon(imgURL));
		background.setLayout(new FlowLayout());

		
		
		
		panel1 = new JPanel(new FlowLayout());
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
		
		consistency = new JComboBox(sel);
		consistency.setPreferredSize(new Dimension(60,34));
		
		button = new JButton("Search");
		button.setPreferredSize(new Dimension(100,34));

		URL logoURL = new File("res/logo.png").toURI().toURL();
		logo = new JLabel(new ImageIcon(logoURL));
		
		panel1.add(searchfield);
		panel1.add(consistency);
		panel1.add(button);
		panel1.setOpaque(false);
		panel2.setOpaque(false);
		panel2.add(logo);
		panel2.add(panel1);
		panel2.add(panel3);
		background.add(panel2);

        
        JTabbedPane tabLeiste = new JTabbedPane();
        tabLeiste.addTab("Search", background);
        JPanel edit = new JPanel();

		edit.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

		JPanel edpan = new JPanel();
		edpan.setLayout(new GridLayout(8,1));
		edpan.setBorder(new TitledBorder("Add"));

		JLabel addkey = new JLabel("Key");
		addkeyfield = new JTextField(15);
		edpan.add(addkey);
		edpan.add(addkeyfield);
		
		JLabel addvalue = new JLabel("Value");
		addvaluefield = new JTextArea(2,20);
		
		addbutton = new JButton("Add");
		
		edpan.add(addvalue);
		edpan.add(addvaluefield);
		edpan.add(new JLabel("Consistency Level"));
		edpan.add(addconsistency);
		edpan.add(addbutton);
		edpan.add(addfeedback);
		
		
		JPanel edpande = new JPanel();
		edpande.setLayout(new GridLayout(9,1));
		edpande.setBorder(new TitledBorder("Delete"));
		
		JLabel delkey = new JLabel("Key");
		delkeyfield = new JTextField(15);
		delbutton = new JButton("Delete");
		edpande.add(delkey);
		edpande.add(delkeyfield);
		edpande.add(new JLabel(""));
		edpande.add(new JLabel(""));
		edpande.add(new JLabel(""));
		edpande.add(new JLabel(""));
		edpande.add(new JLabel(""));
		edpande.add(delbutton);
		

		
		

		JPanel edpanup = new JPanel();
		edpanup.setLayout(new GridLayout(8,1));
		edpanup.setBorder(new TitledBorder("Update"));

		JLabel upkey = new JLabel("Key");
		upkeyfield = new JTextField(15);
		edpanup.add(upkey);
		edpanup.add(upkeyfield);
		
		JLabel upvalue = new JLabel("Value");
		upvaluefield = new JTextArea(2,20);
		
		upbutton = new JButton("Update");
		
		edpanup.add(upvalue);
		edpanup.add(upvaluefield);
		edpanup.add(new JLabel(""));
		edpanup.add(new JLabel(""));
		edpanup.add(upbutton);
		
		
		
		edit.add(edpan);
		edit.add(edpande);
		edit.add(edpanup);
		
		
        tabLeiste.addTab("Edit", edit);
		fr.add(tabLeiste);
        
		fr.pack();
		fr.setSize(1024, 600);
		fr.setVisible(true); 
		
		
		searchfield.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                	String searchvalue = searchfield.getText();
                	String level = consistency.getSelectedItem().toString();
    				String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<lookup><key>"+searchvalue+"</key><type>clientrequest</type><consistencylevel>"+level+"</consistencylevel><port>"+listenPort+"</port></lookup>\n";
    				try {
						Supplier.send(conIP, conPort, message);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                }
            }
		});
    
		
		
		addbutton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				
				addfeedback.setText("WAITING FOR FEEDBACK...");
				
            	String level = addconsistency.getSelectedItem().toString();
				String key = addkeyfield.getText();
				String value = addvaluefield.getText();
				String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<insert><key>"+key+"</key><value>"+value+"</value><type>clientrequest</type><consistencylevel>"+level+"</consistencylevel><port>"+listenPort+"</port></insert>";
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
		
		
		delbutton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				String key = delkeyfield.getText();
				String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<delete><key>"+key+"</key><type>clientrequest</type></delete>";
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
		
		
		upbutton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				String key = upkeyfield.getText();
				String value = upvaluefield.getText();
				String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<update><key>"+key+"</key><value>"+value+"</value><type>clientrequest</type></update>";
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
		
		
		button.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				String searchvalue = searchfield.getText();
				String level = consistency.getSelectedItem().toString();
				String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<lookup><key>"+searchvalue+"</key><type>clientrequest</type><level>"+level+"</level><port>"+listenPort+"</port></lookup>\n";
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

	
	public static void setWriteFeedback(String value) {
		if(value.equals(("true"))) {
			addfeedback.setText("SUCCESS");
		} else {
			addfeedback.setText("FAIL");
		}
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
		listenPort = conf.intFor("listenPort");
		
        ConnectionHandler connectionHandler = new ConnectionHandler(conf);
        Thread handlerThread = new Thread(connectionHandler, "Connection Handler");
        handlerThread.start();
		
		GUI frame = new GUI();
		
        handlerThread.interrupt();
	}

}

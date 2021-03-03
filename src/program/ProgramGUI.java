package program;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Toolkit;

public class ProgramGUI extends JFrame {

	private static final long serialVersionUID = 8974671522732960694L;
	// GUI Vars
	private JPanel contentPane;
	private JButton btUpdate, btDownload;
	private JTextField tfDownload, tfFolder;
	private JProgressBar pbar_download, pbar_install;
	
	
	// Other Vars
	WorkingClass wc;
	FileHandler fh = new FileHandler();
	String author = "Chief-Bugs";
	String address = fh.getPropertyByKey("urlSkin");
	String tempDir = fh.getPropertyByKey("tempDir");
	String folderName = fh.getPropertyByKey("folderName");
	String skinFolder = fh.getPropertyByKey("skinFolder");
	String logo = fh.getPropertyByKey("logo");
	String title = fh.getPropertyByKey("title");
	String lastUpdate = fh.getPropertyByKey("lastUpdate");
	String src = String.format("%s/%s", tempDir, folderName);
	String version = fh.getPropertyByKey("version");
	
	public void run() { }
	
	public static void main(String[] args) {
		ProgramGUI frame = new ProgramGUI();
		frame.run();
	}

	/**
	 * Create the frame.
	 */
	public ProgramGUI() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ProgramGUI.class.getResource(logo)));
		setupFrame();
	}

	
	public void setupFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				File f = new File(src);
				if(f.exists())
					deleteFolder(f);
			}
		});
		setTitle(String.format("%s v%s", title, version));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 175);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// GUI Elements
		btDownload = new JButton("Download");
		btDownload.setBounds(331, 24, 103, 23);
		contentPane.add(btDownload);
		
		btUpdate = new JButton("Update Files");
		btUpdate.setBounds(331, 97, 103, 23);
		contentPane.add(btUpdate);
		btUpdate.setEnabled(false);
		
		tfDownload = new JTextField();
		tfDownload.setText(address);
		tfDownload.setBounds(10, 25, 311, 20);
		contentPane.add(tfDownload);
		tfDownload.setColumns(10);
		tfDownload.setEditable(false);
		
		JLabel lbDownload = new JLabel("Download Source");
		lbDownload.setBounds(10, 11, 118, 14);
		contentPane.add(lbDownload);
		
		tfFolder = new JTextField();
		tfFolder.setText(skinFolder);
		tfFolder.setBounds(10, 98, 311, 20);
		contentPane.add(tfFolder);
		tfFolder.setColumns(10);
		tfFolder.setEditable(false);
		tfFolder.setEnabled(false);
		
		JLabel lbFolder = new JLabel("Compact Skin Folder");
		lbFolder.setBounds(10, 83, 118, 14);
		contentPane.add(lbFolder);
		
		pbar_download = new JProgressBar();
		pbar_download.setBounds(331, 48, 103, 16);
		contentPane.add(pbar_download);
		
		pbar_install = new JProgressBar();
		pbar_install.setBounds(331, 121, 103, 16);
		contentPane.add(pbar_install);
		
		
		wc = new WorkingClass(tfDownload, btDownload, tfFolder, btUpdate, pbar_download, pbar_install);
		
		JLabel lblMade = new JLabel(String.format("Made by: %s | Last Updated: %s", author, lastUpdate));
		lblMade.setEnabled(false);
		lblMade.setBounds(2, 129, 320, 14);
		contentPane.add(lblMade);
		wc.start();
		
		
		// ActionPerformed Zone
		btDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wc.download();
			}
		});
		
		btUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wc.upload();
			}
		});
		
		tfDownload.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				address = tfDownload.getText();
			}
		});
		
		tfFolder.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				skinFolder = tfDownload.getText();
			}
		});
		
		
		// Final configs
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files != null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
}

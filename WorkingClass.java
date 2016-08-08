package program;

import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class WorkingClass extends Thread {
	
	UnzipUtility unzipper;
	FileHandler fh;
	
	String address = "https://github.com/badanka/Compact/archive/master.zip";
	String tempDir = System.getenv("TEMP");
	String skinFolder = "C:\\Program Files (x86)\\Steam\\skins\\Compact";
	String src = tempDir + "\\Compact-master\\Steam\\skins\\Compact";
	String fpath;

	private int estado;
	private final int ESPERAR = 0;
	private final int DOWNLOAD = 1;
	private final int UPLOAD = 2;
	
	private JButton btUpdate, btDownload;
	private JTextField tfDownload, tfFolder;
	private JProgressBar pbar_download, pbar_install;
	
	public WorkingClass(JTextField tf1, JButton bt1, JTextField tf2, JButton bt2, JProgressBar pbar1, JProgressBar pbar2) {
		tfDownload = tf1;
		btDownload = bt1;
		tfFolder = tf2;
		btUpdate = bt2;
		pbar_download = pbar1;
		pbar_install = pbar2;
	}
	
	public void run() {
		//System.out.println("Working class running");
		unzipper = new UnzipUtility();
		fh = new FileHandler();

		estado = ESPERAR;
		while(true) {
			switch (estado) {
			case ESPERAR:
				//System.out.println("À espera!!!");
				dormir(750);
				break;

			case DOWNLOAD:
				clearPbars();
				tfDownload.setEnabled(false); // Changes GUI objects
				btDownload.setEnabled(false);
				pbar_download.setIndeterminate(true);
				
				fpath = UrlDownloader.fileDownload(address, tempDir);
				try {
					unzipper.unzip(fpath, tempDir);
				} catch (IOException e1) { }
				ProgramGUI.deleteFolder(new File(fpath));
				
				pbar_download.setIndeterminate(false); // Changes GUI objects
				pbar_download.setValue(100);
				pbar_download.setStringPainted(true);
				pbar_download.setString("Done!");
				tfFolder.setEnabled(true);
				btUpdate.setEnabled(true);
				esperar();
				break;

			case UPLOAD:
				System.out.println("File source:\n" + src);
				tfFolder.setEnabled(false); // Changes GUI objects
				btUpdate.setEnabled(false);
				pbar_install.setIndeterminate(true);
				
				try {
					fh.move(src, skinFolder);
					ProgramGUI.deleteFolder(new File(tempDir + "\\Compact-master"));
				} catch (IOException e1) {
					System.out.println("Couldn't Overwrite the Folder:\n" + skinFolder);
				}
				
				pbar_install.setIndeterminate(false); // Changes GUI objects
				pbar_install.setValue(100);
				pbar_install.setStringPainted(true);
				pbar_install.setString("Done!");
				tfDownload.setEnabled(true);
				btDownload.setEnabled(true);
				esperar();
				break;
			}
		}
	}
	
	public void download() {
		estado = DOWNLOAD;
	}
	
	public void upload() {
		estado = UPLOAD;
	}
	
	public void esperar() {
		dormir(150);
		estado = ESPERAR;
	}

	public void clearPbars() {
		pbar_download.setValue(0);
		pbar_download.setStringPainted(false);
		pbar_install.setValue(0);
		pbar_install.setStringPainted(false);
	}
	
	public void dormir(int m) {
		try {
			Thread.sleep(m);
		} catch(InterruptedException e) { }
	}
}

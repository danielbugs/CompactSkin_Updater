package program;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;

import java.util.Enumeration;
import java.util.Properties;

public class FileHandler {
	
	private String propfile = "file.properties";
	
	public FileHandler() {}
	
	
	public void fileDeleter(File f) throws IOException {
		f.delete();
	}
	
	
	public void move(String src, String dest) {
		Path s = Paths.get(src);
		Path d = Paths.get(dest);
		
		try {
			Files.move(s, d, REPLACE_EXISTING);
			System.out.println("MOVE OK");
		} catch (IOException e) {
			System.out.println("MOVE ERROR\n>>> Folder:\n" + d);
		}
	}
	
	
	public void deleteFolder(File folder) {
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
	
	
	public void deleteFiles(File folder) {
		File[] files = folder.listFiles();
		if(files != null) { //some JVMs return null for empty dirs
			for(File f: files) {
				if(!f.isDirectory()) {
					f.delete();
				}
			}
		}
	}
	
	
	public void createFolder(String src) {
		File sDir = new File(src);
		
		if(sDir.exists()) {
			deleteFolder(sDir);
			System.out.println("Folder found, deleting it!");
		}
		if(sDir.mkdir())
			System.out.println("MKDIR OK");
		else
			System.out.println("MKDIR ERROR");
	}
	
	
	public void dormir(int m) {
		try {
			Thread.sleep(m);
		} catch(InterruptedException e) { }
	}
	
	
	public void setProperties(Properties prop) {
		OutputStream output = null;
		
		try {
			output = new FileOutputStream(propfile);
			prop.store(output, null);
			
			System.out.println(String.format("Properties stored to output file \"%s\"\n", propfile));
		} catch(IOException ioe) {
			System.err.println(String.format("Error while trying to store properties in file\n%s", ioe));
		} finally {
			if(output != null) {
				try {
					output.close();
				} catch(IOException ioe) {
					System.err.println(String.format("Error while trying to close OutputStream Object\n>>>%s", ioe));
				}
			}
		}
	}
	
	
	public Properties getProperties() {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(propfile);
			prop.load(input);
			
			//System.out.println(String.format("Properties loaded from input file \"%s\"", propfile));
		} catch(IOException ioe) {
			System.err.println(String.format("Error while trying to store properties in file\n%s", ioe));
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch(IOException ioe) {
					System.err.println(String.format("Error while trying to close InputStream Object\n>>>%s", ioe));
				}
			}
		}
		
		return prop;
	}
	
	
	public void listProperties() {
		Properties prop = this.getProperties();
		if(prop != null) {
			Enumeration<?> e = prop.propertyNames();
			System.out.println("Listing all properties found");
			
			while(e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				value = this.checkValue(value);
				System.out.println(String.format("Key: %s\nValue: \"%s\"\n", key, value));
			}
		}
	}
	
	
	public String getPropertyByKey(String key, boolean debug) {
		Properties prop = this.getProperties();
				
		if(prop != null) {
			String value = prop.getProperty(key);
			value = this.checkValue(value);
			if(debug) System.out.println(String.format("getPropertyByKey(%s)=\"%s\"\n", key, value));
			return value;
		} else return "";
	}
	
	
	public String getPropertyByKey(String key) {
		return getPropertyByKey(key, false);
	}
	
	
	protected String checkValue(String val) {
		String regex1 = "%(.+)%";
		String regex2 = "%(.+)%(.+)";

		if(val.matches(regex1)) {
			String ss = val.replace("%", "");
			val = System.getenv(ss);
		} else if (val.matches(regex2)) {
			String[] ss = val.split("%"); 
			val = String.format("%s\\%s", System.getenv(ss[1]), ss[2]);
		}
		return val;
	}
	
	
	public static void main(String[] args) {
		System.out.println("BEGIN");
		FileHandler fh = new FileHandler();
		
		/*
		Properties prop = new Properties();
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		String address = "https://github.com/badanka/Compact/archive/master.zip";
		String tempDir = System.getenv("TEMP");
		String programfiles = System.getenv("programfiles(x86)");
		String skinFolder = String.format("%s\\Steam\\skins\\Compact", programfiles);
		
		prop.setProperty("author", "Chief-Bugs");
		prop.setProperty("version", "2.3");
		prop.setProperty("lastUpdate", String.format("%02d/%02d/%d", day, month, year));
		prop.setProperty("url", address);
		prop.setProperty("tempDir", tempDir);
		prop.setProperty("skinFolder", skinFolder);
		fh.setProperties(prop);
		*/
		
		//Properties p = fh.getProperties();
		//fh.getPropertyByKey("author", true);
		fh.listProperties();
		
		
		System.out.println("END");
	}
}

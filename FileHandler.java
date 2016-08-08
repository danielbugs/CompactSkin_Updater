package program;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileHandler {
	
	public FileHandler() {
		
	}
	
	public void fileDeleter(File f) throws IOException {
		f.delete();
	}
	
	public void move(String src, String dest) throws IOException {
		Path s = Paths.get(src);
		Path d = Paths.get(dest);
		File dDir = d.toFile();
		/*
		File dir = new File(dest);
		if(dir.isDirectory() && dir.exists()) {
			for (File f : dir.listFiles()) {
					f.delete();
			}
		}
		
		File folder = new File(src);
		folder.renameTo(dir);
		*/
		if(dDir.exists())
			deleteFolder(dDir);
		else
			dDir.mkdir();
		
		Files.move(s, d);
	
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

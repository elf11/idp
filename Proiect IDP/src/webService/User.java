package webService;

import java.util.Vector;

public interface User {
	
	public String getIp();
	
	public String getName();
	
	public int getPort();
	
	public Vector<String> getFiles();
	
	public void addFile(String fileName);
	
	public void removeFile(String fileName);

}

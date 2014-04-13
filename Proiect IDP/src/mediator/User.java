package mediator;

import java.util.Vector;

import org.apache.log4j.Logger;

public class User {
	
	private String name;
	private String ip;
	private int port;
	private Vector<String> filelist;
	private static Logger log = Logger.getLogger("Network ");
	
	public User(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.filelist = new Vector<String>();
		log.info("The user " + this.name + "succesfully logged on");
	}
	

	public String getIp() {
		return ip;
	}

	public String getName() {
		return name;
	}

	public int getPort() {
		return port;
	}

	public Vector<String> getFiles() {
		return filelist;
	}

	public void addFile(String fileName) {
		filelist.add(fileName);
	}

	public void removeFile(String fileName) {
		filelist.remove(fileName);
	}

}

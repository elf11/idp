package webService;

import java.util.Vector;

import org.apache.log4j.Logger;

public class ConcreteUser implements User{
	
	private String name;
	private String ip;
	private int port;
	private Vector<String> filelist;
	private static Logger log = Logger.getLogger("Network ");
	
	public ConcreteUser(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.filelist = new Vector<String>();
		log.info("The user " + this.name + "succesfully logged on");
	}
	

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public Vector<String> getFiles() {
		return filelist;
	}

	@Override
	public void addFile(String fileName) {
		filelist.add(fileName);
	}

	@Override
	public void removeFile(String fileName) {
		filelist.remove(fileName);
	}

}

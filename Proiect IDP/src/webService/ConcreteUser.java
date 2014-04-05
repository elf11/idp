package webService;

import java.util.Vector;

public class ConcreteUser implements User{
	
	private String name;
	private String ip;
	private int port;
	private Vector<String> filelist;
	
	public ConcreteUser(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.filelist = new Vector<String>();
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

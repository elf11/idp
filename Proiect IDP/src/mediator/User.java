package mediator;

import org.apache.log4j.Logger;

public class User {
	
	private int id;
	private String name;
	private String ip;
	private int port;
	private static Logger log = Logger.getLogger("Network ");
	
	public User(int id, String name, String ip, int port) {
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
		log.info("The user " + this.name + "succesfully logged on");
	}
	
	public int getId() {
		return id;
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
}

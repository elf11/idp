package registry;

public class User {
	
	private int id;
	private String name;
	private String ip;
	private int port;
	
	public User(int id, String name, String ip, int port) {
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
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

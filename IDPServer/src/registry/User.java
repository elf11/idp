package registry;

public class User {
	
	private String name;
	private String ip;
	private int port;
	
	public User(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
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

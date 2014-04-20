package registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ClientRegistry {
	
	public static HashMap<String, User> clients = new HashMap<>();

	public ArrayList<String> getFilesFromUser(String name) {
		ArrayList<String> abc = new ArrayList<>();
		abc.add("asad");
		abc.add("sdd");
		return abc;
	}
	
	public ArrayList<User> getClients() {
		ArrayList<User> clientList = new ArrayList<>();
		for (Entry<String, User> e : clients.entrySet()) {
			System.out.println("here");
			clientList.add(e.getValue());
		}
		System.out.println(clientList);
		return clientList;
	}
	
	public void addUser(String name, String ip, int port) {
		System.out.println("adding " + name);
		clients.put(name, new User(name, ip, port));
	}
	
	public void removeUser(String name) {
		clients.remove(name);
	}
}

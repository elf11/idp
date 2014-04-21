package registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ClientRegistry {
	
	public static HashMap<String, User> clients = new HashMap<>();
	public static HashMap<String, ArrayList<String>> files = new HashMap<>();
	private static int id = 0;

	public ArrayList<String> getFilesFromUser(String user) {
		return files.get(user);
	}
	
	public ArrayList<User> getClients() {
		ArrayList<User> clientList = new ArrayList<>();
		for (Entry<String, User> e : clients.entrySet()) {
			clientList.add(e.getValue());
		}
		System.out.println(clientList);
		return clientList;
	}
	
	public void addFilesToUser(String user, String[] newFiles) {
		ArrayList<String> userFiles = files.get(user);
		if (userFiles != null) {
			for (String file : newFiles) {
				userFiles.add(file);
			}
		}
	}
	
	public void removeFilesFromUser(String user, String[] deletedFiles) {
		ArrayList<String> userFiles = files.get(user);
		if (userFiles != null) {
			for (String file : deletedFiles) {
				userFiles.remove(file);
			}
		}
	}
	
	public int addUser(String name, String ip, int port) {
		if (!clients.containsKey(name)) {
			System.out.println("adding " + name);
			files.put(name, new ArrayList<String>());
			clients.put(name, new User(++id, name, ip, port));
			return id;
		} else {
			return 0;
		}
	}
	
	public void removeUser(String name) {
		files.remove(name);
		clients.remove(name);
	}
}

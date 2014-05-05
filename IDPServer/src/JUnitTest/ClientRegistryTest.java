package JUnitTest;

import java.util.ArrayList;
import java.util.HashMap;



import org.junit.Test;

import registry.User;
import registry.ClientRegistry;

public class ClientRegistryTest {
	
	HashMap<String, User> users;
	ArrayList<String> files;
	ClientRegistry registry;
	String user1 = "oana";
	String user2 = "andrei";
	
	@Test
	public void testClientRegistry() {
		registry = new ClientRegistry();
		int id1 = registry.addUser(user1, "127.0.0.1", 10001);
		int id2 = registry.addUser(user2, "127.0.0.2", 10002);
		
		assert(id1 != id2);
	}

	@Test
	public void testAddFilesToUser() {
		String[] files = {"file_1.txt", "file_2.txt"};
		registry.addFilesToUser(user1, files);
			
		ArrayList<String> userFiles = registry.getFilesFromUser(user1);
			
		assert(userFiles.contains("file_1.txt"));
		assert(userFiles.contains("file_2.txt"));
	}

	@Test
	public void testRemoveUser() {
		registry.removeUser(user1);
		
		assert(!users.containsKey(user1));
	}
}

package mediator;

import java.util.HashMap;
import java.util.Vector;

import network.Network;
import gui.*;

public class Mediator {

	private GUI gui;
	private Network network;
	private String currentUser;

	HashMap<String, Vector<String>> users = new HashMap<String, Vector<String>>();

	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		if (args.length != 1) {
			System.err.println("Introduceti username-ul pentru autentificare!");
			System.exit(-1);
		}
		System.err.println(args[0]);
		Mediator core = new Mediator(args[0]);
		addFiles(core);
		Thread.sleep(1000);
		core.newIncomingTransfer("andrei", "file1");
		Thread.sleep(1000);
		core.newOutgoingTransfer("oana", "file2");
	}
	
	public static void addFiles(Mediator core) {
		Vector<String> files1 = new Vector<String>();
		files1.add("movie.mp4");
		files1.add("so.pdf");
		files1.add("idp.pdf");
		
		Vector<String> files2 = new Vector<String>();
		files2.add("movie1.mp4");
		files2.add("so1.pdf");
		files2.add("idp1.pdf");
		
		Vector<String> files3 = new Vector<String>();
		files3.add("movie2.mp4");
		files3.add("so2.pdf");
		files3.add("idp2.pdf");
		
		core.addUser("mihai", files1);
		core.addUser("oana", files2);
		core.addUser("andrei", files3);
	}
	
	Mediator(String username) {
		currentUser = username;
		gui = new GUI(this);
		network = new Network(this);
		gui.start();
	}
	
	
	public void newIncomingTransfer(String dest, String fileName) {
		int size = 500;
		int id = gui.addTransfer(currentUser, dest, fileName, true);
		TransferInfo tr = new TransferInfo(dest, fileName, id, 0, size, this);
		network.startIncomingTransfer(tr);
	}
	
	public void newOutgoingTransfer(String source, String fileName) {
		int size = 500;
		int id = gui.addTransfer(source, currentUser, fileName, false);
		TransferInfo tr = new TransferInfo(source, fileName, id, 1, size, this);
		network.startOutgoingTransfer(tr);
	}

	public void updateTransfer(int id, Float i) {
		gui.updateProgress(id, i);
	}
	
	public void addUser(String username, Vector<String> files) {
		gui.addUser(username, files);
	}
	
	public void removeFileFromUser(String fileName) {
		gui.removeFileFromUser(fileName);
	}
	
	public void addFilesToUsers(String userName, Vector<String> files) {
		addUser(userName, files);
	}
	
	public HashMap<String, Vector<String>> getUsers() {
		return users;
	}
	
	public String getUsername() {
		return currentUser;
	}
}

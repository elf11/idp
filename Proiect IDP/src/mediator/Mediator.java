package mediator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import network.Listener;
import gui.*;

public class Mediator {

	GUI gui;
	Listener listener;
	public String currentUser;
	
	HashSet<Transfer> incomingFiles = new HashSet<Transfer>();
	HashSet<Transfer> outgoingFiles = new HashSet<Transfer>();
	HashMap<String, Vector<String>> users = new HashMap<String, Vector<String>>();
	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Mediator core = new Mediator();
		Thread.sleep(1000);
		core.newIncomingTransfer("Andrei", "file1");
		Thread.sleep(1000);
		core.newOutgoingTransfer("Oana", "file2");
		addFiles(core);
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
	
	Mediator() {
		currentUser = "_me_";
		gui = new GUI(this);
		listener = new Listener(this);
		gui.start();
	}
	
	
	public void newIncomingTransfer(String dest, String fileName) {
		int id = gui.addTransfer(currentUser, dest, fileName, ((float)45/200), true);
		Transfer tr = new Transfer((float) 0, id);
		incomingFiles.add(tr);
		//listener.transfer(tr);
	}
	
	public void newOutgoingTransfer(String source, String fileName) {
		int id = gui.addTransfer(source, currentUser, fileName, ((float)45/100), false);
		Transfer tr = new Transfer((float) 0, id);
		outgoingFiles.add(tr);
		//listener.transfer(tr);
	}

	public void updateTransfer(Transfer tr, Float i) {
		gui.updateProgress(tr.row, i);
	}
	
	public void addUser(String username, Vector<String> files) {
		gui.addUser(username, files);
	}
	
	public void addFilesToUsers(String userName, Vector<String> files) {
		addUser(userName, files);
	}
	
	public HashMap<String, Vector<String>> getUsers() {
		return users;
	}
}

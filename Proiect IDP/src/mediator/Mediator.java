package mediator;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Vector;

import network.Network;
import gui.*;

/**
 * Mediator class. Holds information about all components and facilitates interaction
 * between them.
 */
public class Mediator {

	private GUI gui;
	private Network network;
	private String currentUser;

	private HashMap<String, Vector<String>> users = new HashMap<String, Vector<String>>();

	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		if (args.length != 1) {
			System.err.println("Introduceti username-ul pentru autentificare!");
			System.exit(-1);
		}
		Mediator core = new Mediator(args[0]);
		
		Vector<String> files = new Vector<String>();
		files.add("movie.mp4");
		files.add("so.pdf");
		files.add("idp.pdf");
		core.addUser(core.getUserName(), files);
		
		new Mock(core).run();
	}
	
	Mediator(String username) {
		currentUser = username;
		gui = new GUI(this);
		network = new Network(this);
		gui.start();
	}
	
	/**
	 * Called by the network to signal that a file is requested by some user
	 */
	public void newIncomingTransfer(String dest, String fileName) {
		int size = 500;
		int id = gui.addTransfer(currentUser, dest, fileName, true);
		TransferInfo tr = new TransferInfo(dest, fileName, id, 0, size, this);
		network.startIncomingTransfer(tr);
	}
	
	/**
	 * Called by the GUI to request starting the transfer of a file
	 */
	public void newOutgoingTransfer(String source, String fileName) {
		int size = 500;
		int id = gui.addTransfer(source, currentUser, fileName, false);
		TransferInfo tr = new TransferInfo(source, fileName, id, 1, size, this);
		network.startOutgoingTransfer(tr);
	}
	
	/**
	 * Add a user to the list of usersr
	 */
	public synchronized void addUser(final String userName, Vector<String> files) {
		users.put(userName, files);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.addUser(userName);
			}
		});
	}
	
	/**
	 * Removes a file shared by a user
	 */
	public synchronized void removeFileFromUser(final String userName, final String fileName) {
		users.get(userName).removeElement(fileName);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.removeFileFromUser(userName, fileName);
			}
		});
	}
	
	/**
	 * Add a file to be shared by a user
	 */
	public synchronized void addFileToUser(String userName, String fileName) {
		users.get(userName).add(fileName);
		gui.addFileToUser(userName, fileName);
	}
	
	public HashMap<String, Vector<String>> getUsers() {
		return users;
	}
	
	public String getUserName() {
		return currentUser;
	}

	public synchronized void removeUser(final String userName) {
		if (users.remove(userName) != null) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					gui.removeUser(userName);
				}	
			});
		}
	}
	
	/**
	 * Updates progress and speed of transfer. Called by the publish method of a Swing Worker
	 * transfering a file
	 */
	public void updateTransfer(int id, Float progress, int speed) {
		gui.updateProgress(id, progress, speed);
	}
}

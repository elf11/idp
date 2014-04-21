package mediator;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import webService.WebService;
import network.Network;
import gui.*;

/**
 * Mediator class. Holds information about all components and facilitates interaction
 * between them.
 */
public class Mediator {

	private GUI gui;
	private Network network;
	private WebService webService;
	private String currentUser;
	private final String PATH = "config";
	private static Logger log = Logger.getLogger("Mediator ");

	private HashMap<String, User> users = new HashMap<String, User>();
	
	public Mediator(String username) throws IOException, UserRegistrationException {
		currentUser = username;
		webService = new WebService(this, PATH);
		gui = new GUI(this);
		webService.loadConfig();
		User localUser = users.get(currentUser);
		network = new Network(this, PATH + "/" + currentUser + "/", localUser.getIp(), localUser.getPort());
		gui.start();
		log.info("Initialized the mediator");
	}
	
	/**
	 * Called by the network to signal that a file is requested by some user
	 */
	public TransferInfo newIncomingTransfer(String dest, String fileName, long fileSize) {
		log.info("Registered a new IncomingTransfer with the mediator");
		int id = gui.addTransfer(currentUser, dest, fileName, true);
		TransferInfo tr = new TransferInfo(dest, fileName, id, 0, fileSize, this);
		return tr;
	}
	
	/**
	 * Called by the GUI to request starting the transfer of a file
	 */
	public void newOutgoingTransfer(String source, String fileName) {
		log.info("Registered a new OutgoingTransfer with the mediator");
		int size = 500;
		int id = gui.addTransfer(source, currentUser, fileName, false);
		TransferInfo tr = new TransferInfo(source, fileName, id, 1, size, this);
		User peer = users.get(source);
		if (peer != null) {
			network.startOutgoingTransfer(tr, fileName, peer.getIp(), peer.getPort());
		}
	}

	/**
	 * Add a user to the list of users
	 */
	public synchronized void addUser(User user) {
		
		final String userName = user.getName();
		users.put(userName, user);
		EventQueue.invokeLater(new Runnable() {
		@Override
		public void run() {
			gui.addUser(userName);
			}
		});
		log.info("Registered the addUser function with the mediator");
	}
	
	/**
	 * Return a list of all files for a specific user after retrieving it
	 * from the web service
	 */
	public String[] getFilesFromUsers(String userName) {
		return webService.getFilesFromUser(userName);
	}

	/**
	 * Removes a file shared by a user
	 */
	public synchronized void removeFileFromUser(final String userName, final String fileName) {
		webService.removeFilesFromUser(userName, new String[] { fileName });
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.removeFileFromUser(userName, fileName);
			}
		});
		log.info("Registered the removeFileFromUser with the mediator");
	}
	
	/**
	 * Add a file to be shared by a user
	 */
	public synchronized void addFileToUser(final String userName, final String fileName) {
		
		webService.addFilesToUser(userName, new String[] { fileName });
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.addFileToUser(userName, fileName);
			}
		});
		log.info("Registered the addFileToUser with the mediator");
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

	
	public String getUserName() {
		return currentUser;
	}

	/** 
	 * Remove a user from the list of active users 
	 */
	public synchronized void removeUser(final String userName) {
		if (users.remove(userName) != null) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					gui.removeUser(userName);
				}	
			});
		}
		log.info("Registered the removing of an user with the mediator");
	}
	
	/**
	 * Updates progress and speed of transfer. Called by the publish method of a Swing Worker
	 * transfering a file
	 */
	public void updateTransfer(final int id, final Float progress, final int speed) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.updateProgress(id, progress, speed);
			}
		});
		log.info("Registered an update in the transfer with the mediator");
	}

	/**
	 * Signals the web service that the user is logging out
	 */
	public void logOut() {
		webService.removeUser(currentUser);
	}
}

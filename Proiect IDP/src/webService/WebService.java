package webService;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.axis2.AxisFault;
import org.apache.log4j.*;

import registry.ClientRegistryStub;
import registry.ClientRegistryStub.AddFilesToUser;
import registry.ClientRegistryStub.AddUser;
import registry.ClientRegistryStub.AddUserResponse;
import registry.ClientRegistryStub.GetClients;
import registry.ClientRegistryStub.GetClientsResponse;
import registry.ClientRegistryStub.GetFilesFromUser;
import registry.ClientRegistryStub.GetFilesFromUserResponse;
import registry.ClientRegistryStub.RemoveFilesFromUser;
import registry.ClientRegistryStub.RemoveUser;
import mediator.Mediator;
import mediator.User;
import mediator.UserRegistrationException;

public class WebService {
	
	private Mediator mediator;
	private String path;
	private ClientRegistryStub service;
	private ScheduledExecutorService pool;
	String[] files1 = null;
	
	Logger log = Logger.getLogger("WebService");
	
	public WebService(Mediator mediator, String path){
		this.mediator = mediator;
		this.path = path;
		
		pool = Executors.newScheduledThreadPool(1);

		try {
			service = new ClientRegistryStub();
		} catch (AxisFault e) {
			log.error(e);
			e.printStackTrace();
		}
		log.info("Succesfully initialized the WebService");
	}
	
	/**
	 * Function to load the users for the application with their
	 * initial configurations
	 * @param path - path to the users/ folder where all the *.txt
	 * files are stored
	 * @throws Exception 
	 * @throws IOException 
	 */
	public void loadConfig() throws UserRegistrationException {
		File dir = new File(path);
		BufferedReader br = null;
		
		String file_name = mediator.getUserName() + ".txt";
		File f = new File(path + "/" + file_name);
		String userName = mediator.getUserName();
		
		
		try {
			br = new BufferedReader(new FileReader(f.getPath()));
			    
			String ip = br.readLine().trim();
			Integer port = Integer.parseInt(br.readLine().trim());
			        
			        // reading the list of files for each user that it's to be connected
			        // to the application, the files reside in users/username_folder folder
			File userDir = new File(path + "/" + userName);
			ArrayList<String> files = new ArrayList<String>();
			for (File userFile : userDir.listFiles()) {
				if (userFile.isFile()) {
					files.add(userFile.getName());
				}	
			}
			        
			        /* register user with the service */
			AddUser req = new AddUser();
			req.setName(userName);
			req.setIp(ip);
			req.setPort(port);
			AddUserResponse res = service.addUser(req);
			int id = res.get_return();
			        /*if (id == 0) {
				        log.error("Failed to register user " + userName + ". A user with that name already exists.");
			        	throw new UserRegistrationException("User could not be registered.");
			        }*/
                    
			mediator.addUser(new User(id, userName, ip, port));
                    
            /* register the users files */
            addFilesToUser(userName, files.toArray(new String[files.size()]));
                    
            /* schedule getting users every 5 seconds */
            pool.scheduleAtFixedRate(new Runnable() {
						
            	@Override
            	public void run() {
            		getClients();
            	}
			}, 0, 5, TimeUnit.SECONDS);

		} catch(IOException e) {
			   e.printStackTrace();
			   log.error("Failed to read the list of files for each user");
		} finally {
			   try {
			        if (br != null) {
			        	br.close();
			        }
				} catch (IOException e) {
					log.error("Failed to close the buffer reader");
					e.printStackTrace();
				}
		}
		return;
	}
	
	/**
	 * Get the client list from the web service and make updates to the client list in the
	 * mediator
	 */
	public void getClients() {
        GetClients req = new GetClients();
        GetClientsResponse res;
		try {
			res = service.getClients(req);
			registry.ClientRegistryStub.User[] clients = res.get_return();
			System.out.println(clients);
			
			/* Check for new clients */
	        for(registry.ClientRegistryStub.User client : clients) {
	        	boolean found = false;
	        	for (Entry<String, User> loggedClient : mediator.getUsers().entrySet()) {
	        		if (loggedClient.getValue().getId() == client.getId()) {
	        			found = true;
	        		}
	        	}
	        	if (!found) {
        			mediator.addUser(new User(client.getId(), client.getName(), client.getIp(), client.getPort()));
	        	}
	        }
	        
			/* Check for clients that have logged out*/
	        for (Entry<String, User> loggedClient : mediator.getUsers().entrySet()) {
	        	boolean found = false;
	        	for(registry.ClientRegistryStub.User client : clients) {
	        		if (loggedClient.getKey().equals(client.getName())) {
	        			found = true;
	        		}
	        	}
	        	if (!found) {
        			mediator.removeUser(loggedClient.getKey());
	        	}
	        }
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve all the files from the web service for a specific user
	 */
	public String[] getFilesFromUser(final String userName) {
		
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				GetFilesFromUser req = new GetFilesFromUser();
				req.setUser(userName);
				GetFilesFromUserResponse res;
				String[] files = null;
				try {
					res = service.getFilesFromUser(req);
					files = res.get_return();
				} catch (RemoteException e) {
					log.error(e);
					e.printStackTrace();
				}
			files1 = files;	
			} 
		});
		
		if (files1 == null)
			return new String[0];
		else
			return files1;
	}
	
	/**
	 * Add some files to a certain user
	 */
	public void addFilesToUser(String userName, String[] files) {
		AddFilesToUser req = new AddFilesToUser();
		req.setUser(userName);
		req.setNewFiles(files);
		try {
			service.addFilesToUser(req);
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove some files from a certain user
	 */
	public void removeFilesFromUser(String userName, String[] files) {
		RemoveFilesFromUser req = new RemoveFilesFromUser();
		req.setUser(userName);
		req.setDeletedFiles(files);
		try {
			service.removeFilesFromUser(req);
			log.info("Removing files from user " + userName);
		} catch (RemoteException e) {
			log.error("Error deleting files from user ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove a user from the system
	 */
	public void removeUser(String userName) {
		pool.shutdown();
		RemoveUser req = new RemoveUser();
		req.setName(userName);
		try {
			service.removeUser(req);
			log.info("Removing user " + userName);
		} catch (RemoteException e) {
			log.error("Failed to delete user " + userName);
			e.printStackTrace();
		}
	}

}

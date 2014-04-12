package webService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.*;

import mediator.Mediator;

public class WebService {
	
	private Mediator mediator;
	private Vector<User> users;
	private String path;
	
	// doar pentru testarea webservice-ului adaugam chestia asta
	private HashMap<String, Vector<String>> localUsers = new HashMap<String, Vector<String>>();
	private Vector<String> localFiles;
	
	Logger log = Logger.getLogger("WebService");
	
	public WebService(Mediator mediator, String path){
		this.mediator = mediator;
		this.path = path;
		this.users = new Vector<User>();
		log.info("Succesfully initialized the WebService");
	}
	
	public Vector<User> getUsers() {
		return users;
	}
	
	// doar pentru testarea webservice-ului adaugam chestia asta - le scot pentru ultima etapa
	public HashMap<String, Vector<String>> getLocalUsers() {
		return localUsers;
	}
	
	/**
	 * Function to load the users for the application with their
	 * initial configurations
	 * @param path - path to the users/ folder where all the *.txt
	 * files are stored
	 * @throws IOException 
	 */
	public void loadConfig() {
		File dir = new File(path);
		BufferedReader br = null;
		
		for (File f : dir.listFiles()) {
			// doar pentru testarea webservice-ului adaugam chestia asta - le scot pentru ultima etapa
			localFiles = new Vector<String>();
			if (f.isFile() && f.getName().endsWith(".txt")) {
				try {
					String username = f.getName().substring(0, f.getName().length() - 4);
					br = new BufferedReader(new FileReader(f.getPath()));
			    
			        String ip = br.readLine().trim();
			        Integer port = Integer.parseInt(br.readLine().trim());
			        User user = new ConcreteUser(username, ip, port);
			        
			        // reading the list of files for each user that it's to be connected
			        // to the application, the files reside in users/username_folder folder
			        File userDir = new File(path + "/" + username);
			        
			        for (File userFile : userDir.listFiles()) {
			        	if (userFile.isFile()) {
			        		localFiles.add(userFile.getName());
			        		user.addFile(userFile.getName());
			        	}
			        }
			        
			        users.add(user);
			        log.info("User added: " + user.getName() + " " + user.getIp() + " " +  user.getPort());
			        
			        // doar pentru testarea webservice-ului adaugam chestia asta - le scot pentru ultima etapa
			        localUsers.put(username, localFiles);
			        
			        mediator.addUser(user.getName(), user.getFiles());
			    } catch(IOException e) {
			    	e.printStackTrace();
			    	log.error("Failed to read the list of files for each user");
			    } finally {
			        try {
						br.close();
					} catch (IOException e) {
						log.error("Failed to close the buffer reader");
						e.printStackTrace();
					}
			    }
			}
		}
		
	}

}

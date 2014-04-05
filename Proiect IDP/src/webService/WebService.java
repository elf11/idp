package webService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import org.apache.log4j.*;

import mediator.Mediator;

public class WebService {
	
	private Mediator mediator;
	private Vector<User> users;
	private String path;
	Logger log = Logger.getLogger("WebService");
	
	public WebService(Mediator mediator, String path){
		this.mediator = mediator;
		this.path = path;
		this.users = new Vector<User>();
	}
	
	public Vector<User> getUsers() {
		return users;
	}
	
	/**
	 * Function to load the users for the application with their
	 * initial configurations
	 * @param path - path to the users/ folder where all the *.txt
	 * files are stored
	 * @throws IOException 
	 */
	public void loadConfig() throws IOException {
		File dir = new File(path);
		
		for (File f : dir.listFiles()) {
			if (f.isFile() && f.getName().endsWith(".txt")) {
				String username = f.getName().substring(0, f.getName().length() - 4);
				BufferedReader br = new BufferedReader(new FileReader(f.getPath()));
			    try {
			        String ip = br.readLine().trim();
			        Integer port = Integer.parseInt(br.readLine().trim());
			        User user = new ConcreteUser(username, ip, port);
			        
			        // reading the list of files for each user that it's to be connected
			        // to the application, the files reside in users/username_folder folder
			        File userDir = new File(path + "/" + username);
			        
			        for (File userFile : userDir.listFiles()) {
			        	if (userFile.isFile()) {
			        		user.addFile(userFile.getName());
			        	}
			        }
			        
			        users.add(user);
			        log.info("User added: " + user.getName() + " " + user.getIp() + " " +  user.getPort());
			        mediator.addUser(user.getName(), user.getFiles());
			        
			    } finally {
			        br.close();
			    }
			}
		}
		
	}

}

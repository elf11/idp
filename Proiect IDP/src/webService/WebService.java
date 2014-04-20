package webService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.axis2.AxisFault;
import org.apache.log4j.*;

import registry.ClientRegistryStub;
import registry.ClientRegistryStub.AddUser;
import registry.ClientRegistryStub.GetClients;
import registry.ClientRegistryStub.GetClientsResponse;
import mediator.Mediator;
import mediator.User;

public class WebService {
	
	private Mediator mediator;
	private String path;
	private ClientRegistryStub service;
	
	Logger log = Logger.getLogger("WebService");
	
	public WebService(Mediator mediator, String path){
		this.mediator = mediator;
		this.path = path;

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
	 * @throws IOException 
	 */
	public void loadConfig() {
		File dir = new File(path);
		BufferedReader br = null;
		
		for (File f : dir.listFiles()) {
			if (f.isFile() && f.getName().equals(mediator.getUserName() + ".txt")) {
				try {
					String username = f.getName().substring(0, f.getName().length() - 4);
					br = new BufferedReader(new FileReader(f.getPath()));
			    
			        String ip = br.readLine().trim();
			        Integer port = Integer.parseInt(br.readLine().trim());
			        User user = new User(username, ip, port);
			        mediator.addUser(user);
			        
			        // reading the list of files for each user that it's to be connected
			        // to the application, the files reside in users/username_folder folder
			        File userDir = new File(path + "/" + username);
			        
			        for (File userFile : userDir.listFiles()) {
			        	if (userFile.isFile()) {
			        		user.addFile(userFile.getName());
			        	}
			        }
			        
			        log.info("User added: " + user.getName() + " " + user.getIp() + " " +  user.getPort());
			        
			        AddUser req = new AddUser();
			        req.setName(username);
			        req.setIp(ip);
			        req.setPort(port);
                    service.addUser(req);
                    
                    GetClients req1 = new GetClients();
                    GetClientsResponse res = service.getClients(req1);
                    System.out.println(res.get_return()[0].getIp());

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
				return;
			}
		}
		
	}

}

package junitTests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import mediator.Mediator;
import mediator.User;
import mediator.UserRegistrationException;

import org.apache.log4j.Logger;
import org.junit.Test;

import webService.WebService;

public class WebServiceTest {
	
	Mediator core;
	HashMap<String, User> users;
	HashMap<String, ArrayList<String>> files;
	WebService ws;
	String userName = "oana";
	Logger log = Logger.getLogger("WebServiceTest");

	@Test
	public void testWebService() throws IOException, UserRegistrationException {
		Mediator core = new Mediator(userName);
		ws = new WebService(core, "config");
		
		assert (ws) != null;
		
		ws.setTestTrue();
		ws.loadConfig();
		ws.getClients();
		ws.loadConfig();
		users = core.getUsers();
		ws.getFilesFromUser(userName);
	}

	@Test
	public void testGetClients() {
		assert(users.size() == 3);
		assert(users.get(0).getName().equals("user3") || users.get(0).getName().equals("andrei") || users.get(0).getName().equals("oana"));
		assert(users.get(1).getName().equals("user3") || users.get(1).getName().equals("andrei") || users.get(1).getName().equals("oana"));
		assert(users.get(2).getName().equals("user3") || users.get(2).getName().equals("andrei") || users.get(2).getName().equals("oana"));
	}
}

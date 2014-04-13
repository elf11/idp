package junitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import mediator.Mediator;
import mediator.User;

import org.junit.Test;

import webService.WebService;

public class WebServiceTest {
	
	Mediator core;
	Vector<User> users;
	WebService ws;
	private HashMap<String, Vector<String>> localUsers;

	@Test
	public void testWebService() throws IOException {
		Mediator core = new Mediator("oana");
		ws = new WebService(core, "config");
		
		assert (ws) != null;
		
		ws.loadConfig();
		users = ws.getUsers();
		localUsers = ws.getLocalUsers();
	}

	@Test
	public void testGetUsers() {
		assert(users.size() == 3);
		assert(users.get(0).getName().equals("user3") || users.get(0).getName().equals("andrei") || users.get(0).getName().equals("oana"));
		assert(users.get(1).getName().equals("user3") || users.get(1).getName().equals("andrei") || users.get(1).getName().equals("oana"));
		assert(users.get(2).getName().equals("user3") || users.get(2).getName().equals("andrei") || users.get(2).getName().equals("oana"));
	}

	@Test
	public void testLoadConfig() {
		assert(localUsers.get("andrei").contains("file1big.txt"));
		assert(localUsers.get("andrei").contains("file1large.txt"));
		assert(localUsers.get("andrei").contains("file1large.txt"));
		assert(localUsers.get("oana").contains("file2big.txt"));
		assert(localUsers.get("oana").contains("file2large.txt"));
		assert(localUsers.get("oana").contains("file2small.txt"));
		assert(localUsers.get("user3").contains("file3big.txt"));
		assert(localUsers.get("user3").contains("file3large.txt"));
		assert(localUsers.get("user3").contains("file3small.txt"));
	}

}

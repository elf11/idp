package mediator;

import java.util.Vector;

/**
 * Mock class for simulating users and file transfers 
 */
public class Mock {
	
	Mediator core;
	public Mock(Mediator core) {
		this.core = core;
	}

	final int TIMEOUT1 = 1500;
	final int TIMEOUT2 = 3000;
	/**
	 * Adds some users and then simulates download requests
	 */
	void run() throws InterruptedException {
		
		String file;
		int iteration = 0;
		Vector<String> files;
		
		Vector<String> files1 = new Vector<String>();
		files1.add("movie1.mp4");
		files1.add("so1.pdf");
		files1.add("idp1.pdf");
		
		Thread.sleep(TIMEOUT1);
		core.addUser("mihai", files1);
		
		Vector<String> files2 = new Vector<String>();
		files2.add("movie2.mp4");
		files2.add("so2.pdf");
		files2.add("idp2.pdf");
		
		Vector<String> files3 = new Vector<String>();
		files3.add("movie3.mp4");
		files3.add("so3.pdf");
		files3.add("idp3.pdf");
		
		while (true) {
			
			Thread.sleep(TIMEOUT1);
			core.addUser("ioana", files2);
			
			Thread.sleep(TIMEOUT1);
			core.addUser("daniel", files3);
			
			Thread.sleep(TIMEOUT1);
			files = core.getUsers().get(core.getUserName());
			file = files.get(iteration % files.size());
			core.newIncomingTransfer("mihai", file);
			
			Thread.sleep(TIMEOUT2);
			files = core.getUsers().get(core.getUserName());
			file = files.get((iteration + 1) % files.size());
			core.newIncomingTransfer("mihai", file);
			
			Thread.sleep(TIMEOUT2);
			core.removeUser("ioana");
			
			Thread.sleep(TIMEOUT2);
			files = core.getUsers().get(core.getUserName());
			file = files.get((iteration + 2) % files.size());
			core.newIncomingTransfer("daniel", file);
			
			Thread.sleep(TIMEOUT2);
			core.removeUser("daniel");
		}
	}

}

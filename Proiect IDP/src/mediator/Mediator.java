package mediator;

import java.util.HashSet;

import network.Listener;
import gui.*;

public class Mediator {

	GUI gui;
	Listener listener;
	public String currentUser;
	
	HashSet<Transfer> incomingFiles = new HashSet<Transfer>();
	HashSet<Transfer> outgoingFiles = new HashSet<Transfer>();
	
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
		listener.transfer(tr);
	}
	
	public void newOutgoingTransfer(String source, String fileName) {
		int id = gui.addTransfer(source, currentUser, fileName, ((float)45/100), false);
		Transfer tr = new Transfer((float) 0, id);
		outgoingFiles.add(tr);
		listener.transfer(tr);
	}

	public void updateTransfer(Transfer tr, Float i) {
		gui.updateProgress(tr.row, i);
	}
}

package mediator;

import java.util.HashSet;

import network.Network;
import gui.*;

public class Mediator {

	private GUI gui;
	private Network network;
	private String currentUser;
	
	HashSet<TransferInfo> incomingFiles = new HashSet<TransferInfo>();
	HashSet<TransferInfo> outgoingFiles = new HashSet<TransferInfo>();
	
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
		network = new Network(this);
		gui.start();
	}
	
	
	public void newIncomingTransfer(String dest, String fileName) {
		int size = 500;
		int id = gui.addTransfer(currentUser, dest, fileName, true);
		TransferInfo tr = new TransferInfo(dest, fileName, id, 0, size, this);
		incomingFiles.add(tr);
		network.startIncomingTransfer(tr);
	}
	
	public void newOutgoingTransfer(String source, String fileName) {
		int size = 500;
		int id = gui.addTransfer(source, currentUser, fileName, false);
		TransferInfo tr = new TransferInfo(source, fileName, id, 1, size, this);
		outgoingFiles.add(tr);
		network.startOutgoingTransfer(tr);
	}

	public void updateTransfer(int id, Float i) {
		gui.updateProgress(id, i);
	}
}

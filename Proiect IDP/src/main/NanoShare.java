package main;

import java.io.IOException;

import mediator.Mediator;

public class NanoShare {

	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		if (args.length != 1) {
			System.err.println("Introduceti username-ul pentru autentificare!");
			System.exit(-1);
		}
		try {
			new Mediator(args[0]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}

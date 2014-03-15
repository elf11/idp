package mediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;

import gui.GUI;

public class Main {
	
	GUI gui = new GUI();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		GUI.start();
		List<Integer> l = new ArrayList<Integer>();
		try {
//			(new NewWorker()).process(l);
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	private class NewWorker extends SwingWorker<Void, Integer> {
		public NewWorker() {
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
        protected void process(List<Integer> list) {
			//add users to the left panel
			Vector<String> files1 = new Vector<String>();
			files1.add("movie.mp4");
			files1.add("so.pdf");
			files1.add("idp.pdf");
			
			Vector<String> files2 = new Vector<String>();
			files1.add("movie1.mp4");
			files1.add("so1.pdf");
			files1.add("idp1.pdf");
			
			gui.addUser("mihai", files1);
			gui.addUser("ionut", files2);
		}
		
	}
}

package gui;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class StatusBar extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* set text and delete it after a certain period */
	public void setText(final String text) {
		super.setText(text);
		SwingWorker<Object, Object> clearItem = new SwingWorker<Object, Object>() {
			private int TIMEOUT = 3000;
			@Override
			protected Object doInBackground() throws Exception {
				Thread.sleep(TIMEOUT);
				if (StatusBar.this != null && StatusBar.this.getText().equals(text)) {
					StatusBar.this.setText("");
				}
				return null;
			}
		};
		clearItem.execute();
	}

}

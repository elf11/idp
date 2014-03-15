package network;

import java.awt.EventQueue;

import mediator.Mediator;
import mediator.Transfer;

public class Listener {

	private final Mediator mediator;

	public Listener(Mediator mediator) {
		this.mediator = mediator;
	}
	
	public void transfer(Transfer transferDetails) {
		final Transfer transfer = transferDetails;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(2000);
						mediator.updateTransfer(transfer, (float) 10);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}

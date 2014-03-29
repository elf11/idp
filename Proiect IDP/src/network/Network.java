package network;

import mediator.Mediator;
import mediator.TransferInfo;

/**
 * Class for implementing the  Network Component
 */
public class Network {

	final Mediator mediator;
	
	public Network(Mediator mediator) {
		this.mediator = mediator;
	}

	public void startIncomingTransfer(TransferInfo tr) {
		new IncomingTransfer(tr).execute();
	}

	public void startOutgoingTransfer(TransferInfo tr) {
		new OutgoingTransfer(tr).execute();
	}
}

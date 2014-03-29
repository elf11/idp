package network;

import mediator.TransferInfo;

/**
 * Class that implements sending a file
 */
public class OutgoingTransfer extends Transfer {

	public OutgoingTransfer(TransferInfo info) {
		super(info);
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		while (!info.isDone()) {
			Thread.sleep(250);
			publish(15);
		}
		return null;
	}
}

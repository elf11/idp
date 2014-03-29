package network;

import mediator.TransferInfo;

/**
 * Class that implements receiving a file
 *
 */
public class IncomingTransfer extends Transfer {

	public IncomingTransfer(TransferInfo info) {
		super(info);
	}

	@Override
	protected Integer doInBackground() throws Exception {
		while (!info.isDone()) {
			Thread.sleep(250);
			publish(25);
		}
		return null;
	}
}

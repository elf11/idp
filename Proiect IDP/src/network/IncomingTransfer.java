package network;

import mediator.TransferInfo;

public class IncomingTransfer extends Transfer {

	public IncomingTransfer(TransferInfo info) {
		super(info);
	}

	@Override
	protected Object doInBackground() throws Exception {
		while (!info.isDone()) {
			Thread.sleep(250);
			info.update(25);
		}
		return null;
	}
}

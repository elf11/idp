package network;

import mediator.TransferInfo;

public class OutgoingTransfer extends Transfer {

	public OutgoingTransfer(TransferInfo info) {
		super(info);
	}
	
	@Override
	protected Object doInBackground() throws Exception {
		while (!info.isDone()) {
			Thread.sleep(250);
			info.update(15);
		}
		return null;
	}
}

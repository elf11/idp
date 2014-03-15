package network;

import javax.swing.SwingWorker;

import mediator.TransferInfo;

public abstract class Transfer extends SwingWorker<Object, Object>{

	protected TransferInfo info;
	
	public Transfer(TransferInfo info) {
		this.info = info;
	}
}

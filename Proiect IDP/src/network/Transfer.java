package network;

import java.nio.channels.SocketChannel;

import mediator.TransferInfo;

/**
 * Class for holding specific network information about an ongoing transfer
 *
 */
public abstract class Transfer {

	protected TransferInfo info;
	
	public Transfer(TransferInfo info) {
		this.info = info;
	}
	
	abstract public void processData(SocketChannel socket);
	abstract public int getSelectionOp();

	public void setTransferInfo(TransferInfo info) {
		this.info = info;
	}
}

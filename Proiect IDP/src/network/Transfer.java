package network;

import java.nio.channels.SocketChannel;

import mediator.TransferInfo;

/**
 * Class for holding specific network information about an ongoing transfer
 *
 */
public abstract class Transfer {

	protected TransferInfo info;
	protected long lastTransferTime;
	protected long transfered;
	protected int lastSpeed;
	
	public Transfer(TransferInfo info) {
		this.info = info;
		lastTransferTime = System.currentTimeMillis();
	}
	
	abstract public void processData(SocketChannel socket);
	abstract public int getSelectionOp();

	public void setTransferInfo(TransferInfo info) {
		this.info = info;
	}
	
	/**
	 * Computes the speed of the transfer in bytes/sec. Only updated once
	 * per second.
	 */
	public int getSpeed(int transferSize) {
		long time = System.currentTimeMillis() - lastTransferTime;
		if (time > 1000) {
			lastTransferTime = System.currentTimeMillis();
			lastSpeed = (int) (transfered / time * 1000);
			transfered = 0;
		} else {
			transfered += transferSize;
		}
		return lastSpeed;
	}
}

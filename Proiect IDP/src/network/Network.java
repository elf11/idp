package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import mediator.Mediator;
import mediator.TransferInfo;

/**
 * Class for implementing the Network Component
 */
public class Network {


	final Mediator mediator;
	private Listener listener;
	
	public Network(Mediator mediator) {
		this.mediator = mediator;
		try {
			listener = new Listener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startIncomingTransfer(Transfer transfer, String dest, String fileName, long fileSize) {
		TransferInfo tr = mediator.newIncomingTransfer(dest, fileName, fileSize);
		transfer.setTransferInfo(tr);
	}

	public void startOutgoingTransfer(TransferInfo info, String fileName, String address, int port) {
		try {
			SocketChannel socket = SocketChannel.open();
			socket.connect(new InetSocketAddress(address, port));
			socket.configureBlocking(false);
			Transfer transfer = new OutgoingTransfer(info, fileName, address, port, socket);
			socket.register(listener.getSelector(), transfer.getSelectionOp(), transfer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

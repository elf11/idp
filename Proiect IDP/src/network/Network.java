package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import mediator.Mediator;
import mediator.TransferInfo;

/**
 * Class for implementing the Network Component
 */
public class Network {


	final Mediator mediator;
	private Listener listener;
	private static Logger log = Logger.getLogger("Network ");
	
	public Network(Mediator mediator) {
		this.mediator = mediator;
		try {
			listener = new Listener(this);
			log.info("Succesfully opened a new listener.");
		} catch (IOException e) {
			log.error("Couldn't open a new listener!");
			e.printStackTrace();
		}
	}
	
	public void startIncomingTransfer(Transfer transfer, String dest, String fileName, long fileSize) {
		log.info("Start new incomming transfer");
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
			log.info("Start new outgoing transfer");
		} catch (IOException e) {
			log.error("Failed to start a new outgoing transfer");
			e.printStackTrace();
		}
	}
}

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
	private String IP;
	private int port;
	private String PATH;
	private static Logger log = Logger.getLogger("Network ");
	
	
	public Network(Mediator mediator, String path, String IP, int port) {
		this.mediator = mediator;
		this.PATH = path;
		System.out.println(this.PATH);
		this.IP = IP;
		this.port = port;
		log.info("Started the network component");
		try {
			listener = new Listener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startIncomingTransfer(Transfer transfer, String dest, String fileName, long fileSize) {
		log.info("Start new incomming transfer from " + dest + " for file " + fileName);
		TransferInfo tr = mediator.newIncomingTransfer(dest, fileName, fileSize);
		transfer.setTransferInfo(tr);
	}

	public void startOutgoingTransfer(TransferInfo info, String fileName, String address, int port) {
		try {
			SocketChannel socket = SocketChannel.open();
			socket.connect(new InetSocketAddress(address, port));
			OutgoingTransfer transfer = new OutgoingTransfer(info, PATH, fileName);
			transfer.requestTransfer(socket);
			socket.configureBlocking(false);
			listener.registerSocket(socket, transfer);
			log.info("Start new outgoing transfer");
		} catch (IOException e) {
			log.error(e);
		}
	}

	public String getIp() {
		return IP;
	}
	
	public int getPort() {
		return port;
	}

	public String getPath() {
		return PATH;
	}
}

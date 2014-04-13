package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/** 
 * Class that implements the listening part of the network component
 */
public class Listener implements Runnable {

	private Selector selector;
	private ServerSocketChannel listener;
	private ExecutorService pool;
	private ReentrantLock selectorLock;
	private static Logger log = Logger.getLogger("Listener ");
	
	private Network network;
	
	public Listener(Network network) throws IOException {
		this.network = network;
		selector = Selector.open();
		selectorLock = new ReentrantLock();
		
		/* Open listener and register it with the selector*/
		listener = ServerSocketChannel.open();
		listener.socket().bind(new InetSocketAddress(network.getIp(), network.getPort()));
		listener.configureBlocking(false);
		listener.register(selector, SelectionKey.OP_ACCEPT, null);
		
		pool = Executors.newFixedThreadPool(5);
		pool.submit(this);
		log.info("Listener started with address: " + network.getIp() + " and port " + network.getPort());
		
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				/* Ensure the selector is not being modified */
				selectorLock.lock();
				selectorLock.unlock();
				selector.select();
				/* Iterate over the events */
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					// get current event and REMOVE it from the list!!!
					final SelectionKey key = it.next();
					it.remove();
					log.info("Succesfully removed the current event from the list " + it.toString());
					if (key.isAcceptable()) {
						/* Create a new connection that will be used to send a file */
						ServerSocketChannel listener= (ServerSocketChannel)key.channel();
						SocketChannel newConn = listener.accept();
						newConn.configureBlocking(false);
						newConn.register(selector, SelectionKey.OP_READ, new IncomingTransfer(network));
						selector.wakeup();
						log.info("Created a new connection to send a file");
					} else 	if (key.isReadable() || key.isWritable()) {
						/* Deregister the channel and dispatch the event for execution on the thread pool */
						key.channel().register(selector, 0, key.attachment());
						pool.submit(new Runnable() {
							@Override
							public void run() {
								Transfer transfer = (Transfer) key.attachment();
								transfer.processData((SocketChannel) key.channel());
								try {
									if (key.channel().isOpen()) {
										key.channel().register(selector, transfer.getSelectionOp(), transfer);
	log.info("Succesfully deregister the event and dispatched the event");
									selector.wakeup();
									}
								} catch (ClosedChannelException e) {
									e.printStackTrace();
								}
							}
						});
					}
				}
			} catch (IOException e) {
				log.error("Failed to deregister the event and to dispatch the event");
				log.error(e);
			}
		}
	}
	
	public Selector getSelector() {
		return selector;
	}

	/* Register a socket for selection, with the corresponding Transfer object */
	public void registerSocket(SocketChannel socket, Transfer transfer) {
		try {
			selectorLock.lock();
			selector.wakeup();
			socket.register(selector, transfer.getSelectionOp(), transfer);
			log.info("Registered a new socket with the selector");
		} catch (ClosedChannelException e) {
			log.error(e);
			try {
				socket.close();
			} catch (IOException e1) {
				log.error(e1);
			}
		} finally {
			selectorLock.unlock();
		}
	}
}

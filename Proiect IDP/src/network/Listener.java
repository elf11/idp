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

/** 
 * Class that implements the listening part of the network component
 */
public class Listener implements Runnable {

	private Selector selector;
	private ServerSocketChannel listener;
	private ExecutorService pool;
	
	public static final String IP = "127.0.0.1";
	public static final int PORT = 30000;
	
	private Network network;
	
	public Listener(Network network) throws IOException {
		this.network = network;
		selector = Selector.open();
		
		/* Open listener and register it with the selector*/
		listener = ServerSocketChannel.open();
		listener.socket().bind(new InetSocketAddress(IP, PORT));
		listener.configureBlocking(false);
		listener.register(selector, SelectionKey.OP_ACCEPT, null);
		
		pool = Executors.newFixedThreadPool(5);
		pool.submit(this);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				selector.select();
				/* Iterate over the events */
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					// get current event and REMOVE it from the list!!!
					final SelectionKey key = it.next();
					it.remove();
					if (key.isAcceptable()) {
						/* Create a new connection that will be used to send a file */
						ServerSocketChannel listener= (ServerSocketChannel)key.channel();
						SocketChannel newConn = listener.accept();
						newConn.configureBlocking(false);
						newConn.register(selector, SelectionKey.OP_READ, new IncomingTransfer(network));
						selector.wakeup();
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
				e.printStackTrace();
			}
		}
	}
	
	public Selector getSelector() {
		return selector;
	}
}

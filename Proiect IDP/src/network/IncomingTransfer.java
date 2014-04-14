package network;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

/**
 * Class that implements receiving a file
 *
 */
public class IncomingTransfer extends Transfer {

	private Network network;
	private boolean init = false;
	private MappedByteBuffer fileBuffer;
	private long fileSize;
	private String fileName;
	private RandomAccessFile file;
	private static Logger log = Logger.getLogger("IncomingTransfer ");
	
	public IncomingTransfer(Network network) {
		super(null);
		this.network = network;
		log.info("Initialized a new IncomingTransfer");
	}

	/** 
	 * Process the received data on the socket
	 */
	public void processData(SocketChannel socket) {
		if (init) {
			/* Write data to the file */
			try {
				int size = socket.write(fileBuffer);
				if (size < 0) {
					log.error("Socket closed due to an error!");
					socket.close();
					return;
				}
				info.update(size, getSpeed(size));
				if (info.isDone()) {
					/* Transfer is done. Close file and socket. */
					file.getChannel().close();
					file.close();
					socket.close();
					log.info("Successfully closed the file and the socket!");
				}
			} catch (IOException e) {
				log.error("Failed to close the file and the socket!");
				log.error(e);
			}
		} else {
			/* If the file is yet unknown, get the name and open it. */
			try {
				ByteBuffer buf = ByteBuffer.allocate(1024);
				
				/* Read data into the buffer. */
				socket.read(buf);
				buf.flip();
				
				/* Get the name of the file. */
				int len = buf.getInt();
				byte[] nameArray = new byte[len];
				buf.get(nameArray);
				fileName = new String(nameArray);
				
				len = buf.getInt();
				byte[] userArray = new byte[len];
				buf.get(userArray);
				String sourceUser = new String(userArray);
				
				
				/* Open the file and map it into memory */
				file = new RandomAccessFile(network.getPath() + fileName, "r");
				fileSize = file.getChannel().size();
				fileBuffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.getChannel().size());
				log.info("Succesfully read the name of the file and mapped it into the memory");
				
				/* Send the file size to the receiving user */
				buf.clear();
				buf.putLong(fileSize);
				buf.flip();
				socket.write(buf);
				log.info("Succesfully sent the file size to the receiving user");
				
				/* Register the transfer with the mediator */
				network.startIncomingTransfer(this, sourceUser, fileName, fileSize);
				init = true;
			} catch (IOException e) {
				log.error("Failed to open/read the data socket/file");
				log.error(e);
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			init = true;
		}
	}

	@Override
	public int getSelectionOp() {
		return SelectionKey.OP_WRITE;
	}
}


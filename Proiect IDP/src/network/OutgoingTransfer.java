package network;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import mediator.TransferInfo;

/**
 * Class that implements sending a file
 */
public class OutgoingTransfer extends Transfer {

	public OutgoingTransfer(TransferInfo info) {
		super(info);
	}

	private boolean init = true;
	private MappedByteBuffer fileBuffer;
	private String fileName;
	private RandomAccessFile file;
	private static Logger log = Logger.getLogger("OutgoingTransfer ");
	
	public OutgoingTransfer(TransferInfo tr, String fileName, String address, int port, SocketChannel socket) {
		super(tr);
		this.fileName = fileName;
		
		try {
			file = new RandomAccessFile(fileName + ".zip", "rw");
			requestTransfer(socket);
			log.info("Initialized a new outgoing transfer");
		} catch (FileNotFoundException e) {
			log.error("Failed to initialized a new outgoing transfer");
			e.printStackTrace();
		}
	}
	
	/**
	 * Request the file for transfer
	 */
	public void requestTransfer(SocketChannel socket) {
		ByteBuffer nameBuffer = ByteBuffer.allocate(1024);
		nameBuffer.clear();
		int size = fileName.length() + 4;
		nameBuffer.putInt(fileName.length());
		nameBuffer.put(fileName.getBytes());
		nameBuffer.flip();

		while(size > 0) {
			try {
				size -= socket.write(nameBuffer);
				log.info("Wrote on the outgoing socket!");
			} catch (IOException e) {
				log.error("Failed to wrote on the outgoing socket!");
				e.printStackTrace();
			}
		}
	}

	/** 
	 * Process the received data on the socket
	 */
	public void processData(SocketChannel socket) {
		try {
			if (init) {
				/* If this is the first packet, read the size first. */
				ByteBuffer sizeBuffer = ByteBuffer.allocate(8);
				socket.read(sizeBuffer);
				sizeBuffer.flip();
				long fileSize = sizeBuffer.getLong();
				info.setSize(fileSize);
				fileBuffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
				init = false;
			}

			/* Write data to the file */
			int size = socket.read(fileBuffer);
			info.update(size, 15000);
			if (info.isDone()) {
				/* Transfer is done. Close file and socket. */
				file.getChannel().close();
				file.close();
				socket.close();
				log.info("Closed the file and the socket!");
			}
			
		} catch (IOException e) {
			log.error("Failed to transfer data!");
			e.printStackTrace();
		}
	}

	@Override
	public int getSelectionOp() {
		return SelectionKey.OP_READ;
	}
}

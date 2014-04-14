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
	private RandomAccessFile file;
	private static Logger log = Logger.getLogger("OutgoingTransfer ");
	
	public OutgoingTransfer(TransferInfo tr, String path) {
		super(tr);
		
		try {
			file = new RandomAccessFile(path + tr.getFileName(), "rw");
		} catch (FileNotFoundException e) {
			log.error(e);
		}
	}

	
	/**
	 * Request the file for transfer
	 */
	public void requestTransfer(SocketChannel socket) {
		ByteBuffer nameBuffer = ByteBuffer.allocate(1024);
		nameBuffer.clear();
		int size = info.getFileName().length() + info.getUser().length() + 8;
		nameBuffer.putInt(info.getFileName().length());
		nameBuffer.put(info.getFileName().getBytes());
		nameBuffer.putInt(info.getUser().length());
		nameBuffer.put(info.getUser().getBytes());
		nameBuffer.flip();

		while(size > 0) {
			try {
				int trSize = socket.write(nameBuffer);
				if (trSize < 0) {
					log.error("Socket was closed due to an error!");
					socket.close();
					size = 0;
				}
				size -= trSize;
				log.info("Wrote on the outgoing socket!");
			} catch (IOException e) {
				log.error("Failed to wrote on the outgoing socket!");
				log.error(e);
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
				if (socket.read(sizeBuffer) < 0) {
					log.error("Socket closed due to an error!");
					socket.close();
					return;
				}
				sizeBuffer.flip();
				long fileSize = sizeBuffer.getLong();
				info.setSize(fileSize);
				fileBuffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
				init = false;
			}

			/* Write data to the file */
			int size = socket.read(fileBuffer);
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
				log.info("Closed the file and the socket!");
			}
			
		} catch (IOException e) {
			log.error("Failed to transfer data!");
			log.error(e);
		}
	}

	@Override
	public int getSelectionOp() {
		return SelectionKey.OP_READ;
	}
}

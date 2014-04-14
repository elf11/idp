package mediator;

import org.apache.log4j.Logger;

/**
 * Class that stores general information about a transfer that is in progress
 *
 */
public class TransferInfo {
	
	private String fileName;
	private String peer;
	private int type;
	private int id;
	private long totalSize, transferedSize;
	private Mediator mediator;
	private static Logger log = Logger.getLogger("TransferInfo ");
	
	public TransferInfo(String peer, String fileName, int id, int type, long totalSize, Mediator mediator) {
		this.fileName = fileName;
		this.peer = peer;
		this.type = type;
		this.id = id;
		this.totalSize = totalSize;
		this.mediator = mediator;
		log.info("Initialized a new TransferInfo");
	}
	
	public int getType() {
		return type;
	}
	
	public boolean isDone() {
		return transferedSize == totalSize;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getPeer() {
		return peer;
	}
	
	public int getId() {
		return id;
	}
	
	public Float getProgress() {
		return new Float((float)transferedSize / totalSize);
	}
	
	public String getUser() {
		return mediator.getUserName();
	}

	public void update(int size, int speed) {
		transferedSize += size;
		if (transferedSize > totalSize) {
			transferedSize = totalSize;
		}
		mediator.updateTransfer(id, getProgress(), speed);
	}

	public void setSize(long fileSize) {
		totalSize = fileSize;	
	}
}

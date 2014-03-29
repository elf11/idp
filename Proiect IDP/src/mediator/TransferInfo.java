package mediator;

/**
 * Class that stores general information about a transfer that is in progress
 * @author Andrei
 *
 */
public class TransferInfo {
	
	private String fileName;
	private String peer;
	private int type;
	private int id;
	private int totalSize, transferedSize;
	private Mediator mediator;
	
	public TransferInfo(String peer, String fileName, int id, int type, int totalSize, Mediator mediator) {
		this.fileName = fileName;
		this.peer = peer;
		this.type = type;
		this.id = id;
		this.totalSize = totalSize;
		this.mediator = mediator;
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

	public void update(int size, int speed) {
		transferedSize += size;
		if (transferedSize > totalSize) {
			transferedSize = totalSize;
		}
		mediator.updateTransfer(id, getProgress(), speed);
	}
}

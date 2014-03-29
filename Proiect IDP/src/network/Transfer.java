package network;

import java.util.List;

import javax.swing.SwingWorker;

import mediator.TransferInfo;

/**
 * Class for holding specific network information about an ongoing transfer
 *
 */
public abstract class Transfer extends SwingWorker<Integer, Integer>{

	protected TransferInfo info;
	
	public Transfer(TransferInfo info) {
		this.info = info;
	}
	
	@Override
	protected void process(List<Integer> chunks) {
		int dataTransfered = 0;
		for (Integer i : chunks) {
			dataTransfered += i;
		}
		int speed = (int) (1300000 + Math.random() * 400000);
		info.update(dataTransfered, speed);
		super.process(chunks);
	}
}

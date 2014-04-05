package mediator;

/**
 * Mock class for simulating users and file transfers 
 */
public class Mock {
	
	Mediator core;
	public Mock(Mediator core) {
		this.core = core;
	}
	
	/**
	 * Adds some users and then simulates download requests
	 */
	void run() throws InterruptedException {
		
		core.newOutgoingTransfer("abc", "test");
	}
}

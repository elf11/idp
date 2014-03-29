package gui;

/**
 * Class for storing the data that is displayed in a row
 */
public class RowData {

	private static int id_counter = 0;
	
	/* Column indexex */
	public final static int NUMCOLS = 6;
	public final static int SOURCE = 0;
	public final static int DEST = 1;
	public final static int NAME = 2;
	public final static int SPEED= 3;
	public final static int PROGRESS = 4;
	public final static int STATUS = 5;
	
	Object data[];
	private final int id;
	
	public RowData(String source, String dest, String name, Float progress, Status status) {
		data = new Object[NUMCOLS];
		data[SOURCE] = source;
		data[DEST] = dest;
		data[NAME] = name;
		data[SPEED] = "";
		data[PROGRESS] = progress;
		data[STATUS] = status;
		id = id_counter;
		id_counter++;
	}

	public Object getCol(int col) {
		return data[col];
	}

	public void set(int col, Object value) {
		data[col] = value;
	}
	
	public int getId() {
		return id;
	}

}

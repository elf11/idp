package gui;

public class RowData {

	public final static int NUMCOLS = 5;
	public final static int SOURCE = 0;
	public final static int DEST = 1;
	public final static int NAME = 2;
	public final static int PROGRESS = 3;
	public final static int STATUS = 4;
	
	Object data[];
	
	public RowData(String source, String dest, String name, Float progress, Status status) {
		data = new Object[NUMCOLS];
		data[SOURCE] = source;
		data[DEST] = dest;
		data[NAME] = name;
		data[PROGRESS] = progress;
		data[STATUS] = status;
	}

	public Object getCol(int col) {
		return data[col];
	}

	public void set(int col, Object value) {
		data[col] = value;
	}

}

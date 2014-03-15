package gui;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;


class TableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	public ProgressCellRender cell1 = new ProgressCellRender(0, 100);
	
    private String[] columnNames = {"Source", "Destination", "File Name", "Progress", "Status"};
    private Object[][] data = {{"Kathy", "Smith",
    	        "Snowboarding", new Integer(5), cell1},
    	       {"John", "Doe",
    	        "Rowing", new Integer(3), cell1},
    	       {"Sue", "Black",
    	        "Knitting", new Integer(2), cell1},
    	       {"Jane", "White",
    	        "Speed reading", new Integer(20), cell1},
    	       {"Joe", "Brown",
    	        "Pool", new Integer(10), cell1}};

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}

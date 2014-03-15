package gui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;


class TableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 2L;
	
    private final String[] columnNames = {"Source", "Destination", "File Name", "Progress", "Status"};
    private ArrayList<RowData> data = new ArrayList<RowData>();

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data.get(row).getCol(col);
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
       return false;
    }
    
    public void updateProgressBar(Float value, int row) {
    	data.get(row).set(RowData.PROGRESS, value);
    	
    	/* If the transfer is complete, change its status */
    	if ((float)data.get(row).getCol(RowData.PROGRESS) == 1.0) {
    		data.get(row).set(RowData.STATUS, Status.Completed);
    		fireTableCellUpdated(row, RowData.STATUS);
    	}
    		
    	fireTableCellUpdated(row, RowData.PROGRESS);
    }
    
    public void addRow(RowData newRow) {
    	data.add(newRow);
    	fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
}

enum Status {
	Sending, Receiving, Completed;
}

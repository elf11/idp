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

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data.get(row).set(col, value);
        fireTableCellUpdated(row, col);
    }
    
    public void updateProgressBar(Float value, int row) {
    	data.get(row).set(RowData.PROGRESS, value);
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

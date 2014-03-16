package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

class TableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 2L;
	
    private final String[] columnNames = {"Source", "Destination", "File Name", "Progress", "Status"};
    private ArrayList<RowData> data = new ArrayList<RowData>();
    
    /* stores the row of each RowData. A RowData element is identified by its ID */
    private HashMap<Integer, Integer> idMap = new HashMap<Integer, Integer>();

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
    
    public void updateProgressBar(Float value, int id) {
    	int row;
    	if (idMap.containsKey(id)) {
    		row = idMap.get(id);
    	}
    	else {
    		return;
    	}
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
    	idMap.put(newRow.getId(), data.size() - 1);
    	fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
    
    public void removeRow(int row) {
    	int id = -1;
    	/* decrease the row of all entries after the current one from map */
    	for(Entry<Integer, Integer> entry : idMap.entrySet()) {
    		if (entry.getValue() > row) {
    			entry.setValue(entry.getValue() - 1);
    		} else if (entry.getValue() == row) {
    			id = entry.getKey();
    		}
    	}
		if (id != -1)
			idMap.remove(id);
    	data.remove(row);
    	fireTableRowsDeleted(row, row);
    }
    
    public boolean isCompleted(int row) {
    	return (float)data.get(row).getCol(RowData.PROGRESS) == 1.0;
    }
}

enum Status {
	Sending, Receiving, Completed;
}

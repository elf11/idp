package gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

/**
 * This class implements a Table Model for the table containing data about ongoing transfers.
 */
class TableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 2L;
	
    private final String[] columnNames = {"Source", "Destination", "File Name", "Speed", "Progress", "Status"};
    private ArrayList<RowData> data = new ArrayList<RowData>();
    
    /* Stores the row of each RowData. A RowData element is identified by its ID */
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

    public boolean isCellEditable(int row, int col) {
       return false;
    }
    
    /**
     * Formats a given speed in bytes/s to an appropriate format
     */
    public String formatSpeed(int speed) {
    	
    	if (speed < 1024) {
    		return speed + " B/s";
    	} else if (speed > 1024 && speed < 1048576) {
    		return String.format("%.3g%n", (double)speed / 1024) + " KB/s";
    	} else {
    		return String.format("%.3g%n", (double)speed / 1048576) + " MB/s";
    	}	
    }
    
    /**
     * Updates the progress bar and the speed
     */
    public void updateProgress(Float value, int id, int speed) {
    	int row;
    	if (idMap.containsKey(id)) {
    		row = idMap.get(id);
    	}
    	else {
    		return;
    	}
    	data.get(row).set(RowData.PROGRESS, value);
    	data.get(row).set(RowData.SPEED, formatSpeed(speed));
    	
    	/* If the transfer is complete, change its status */
    	if ((float)data.get(row).getCol(RowData.PROGRESS) == 1.0) {
    		data.get(row).set(RowData.STATUS, Status.Completed);
    		data.get(row).set(RowData.SPEED, "");
    		fireTableCellUpdated(row, RowData.STATUS);
    	}
    		
		fireTableCellUpdated(row, RowData.SPEED);
    	fireTableCellUpdated(row, RowData.PROGRESS);
    }
    
    public void addRow(RowData newRow) {
    	data.add(newRow);
    	idMap.put(newRow.getId(), data.size() - 1);
    	fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }
    
    /**
     * Removes a completed row selected by the user
     */
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
    
    public RowData getById(int id) {
    	return data.get(idMap.get(id));
    }
    
    public boolean isCompleted(int row) {
    	return (float)data.get(row).getCol(RowData.PROGRESS) == 1.0;
    }
}

enum Status {
	Sending, Receiving, Completed;
}

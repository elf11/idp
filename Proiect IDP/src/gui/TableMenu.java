package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 * Class that implements a menu for operations on ongoing transfer
 */
public class TableMenu extends MouseAdapter {

	private JTable table;
	private TableModel model;
	
	public TableMenu(JTable table) {
		this.table = table;
		model = (TableModel) table.getModel();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
        int r = table.rowAtPoint(e.getPoint());
        if (r >= 0 && r < table.getRowCount()) {
            table.setRowSelectionInterval(r, r);
        } else {
            table.clearSelection();
        }

        int idx = table.getSelectedRow();
        if (idx < 0)
            return;

        if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
            JPopupMenu popup = createMenu(idx);
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

	private JPopupMenu createMenu(int i) {
		final int id = i;
		JPopupMenu menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("Remove Completed");
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.removeRow(id);
			}		
		});
		item.setEnabled(model.isCompleted(id));
		menu.add(item);
		
		return menu;
	}
}

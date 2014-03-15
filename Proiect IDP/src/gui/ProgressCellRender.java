package gui;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressCellRender extends JProgressBar implements TableCellRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTable t;
	
	public ProgressCellRender() {
		setStringPainted(true);
	}
	
	public ProgressCellRender(int min, int max) {
		super(min, max);
		setStringPainted(true);
	}

	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int progress = 45;
        System.out.println(value);
        t = table;
        if (value instanceof Float) {
            progress = Math.round(((Float) value) * 100f);
        }
        setValue(progress);
        return this;
    }
}

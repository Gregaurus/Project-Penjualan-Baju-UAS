import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class render implements TableCellRenderer{

	@Override
    public Component getTableCellRendererComponent(JTable tableToRender, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        TableColumn tb = tableToRender.getColumnModel().getColumn(1);
        tb.setMaxWidth(100);
        tb.setMinWidth(100);
        tableToRender.setRowHeight(100);
        return (Component) value;

    }
}
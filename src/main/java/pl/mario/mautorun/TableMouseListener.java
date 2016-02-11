
package pl.mario.mautorun;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

public class TableMouseListener extends MouseAdapter{
    
    private JTable table;
     
    public TableMouseListener(JTable table) {
        this.table = table;
    }
     
    @Override
    public void mousePressed(MouseEvent event) {
        // selects the row at which point the mouse is clicked
        Point point = event.getPoint();
        int currentRow = table.rowAtPoint(point);
        table.setRowSelectionInterval(currentRow, currentRow);
        TableActionListener actionListener = new TableActionListener(currentRow, table);
        JPopupMenu popupMenu = actionListener.getPopupMenu();
        if(popupMenu == null)
            return;
        table.setComponentPopupMenu(popupMenu);
        
    }

    
}

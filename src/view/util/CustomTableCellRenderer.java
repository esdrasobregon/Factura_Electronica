/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.util;

/**
 *
 * @author eobregon
 */
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    
    boolean alingToRigth;
    
    public CustomTableCellRenderer(boolean alingToRigth) {
        this.alingToRigth = alingToRigth;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (cellComponent instanceof JComponent) {
            JComponent jComponent = (JComponent) cellComponent;
            jComponent.setToolTipText(value.toString());
            jComponent.setAlignmentX(this.alingToRigth ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        }
        return cellComponent;
    }
}

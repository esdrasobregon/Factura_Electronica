/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.util;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author eobregon
 */
public class JTableCommonFunctions {

    public static void limpiarTabla(JTable tabla) {
        if (tabla != null && tabla.getModel() instanceof DefaultTableModel && tabla.getRowCount() > 0) {
            if (tabla.isEditing()) {
                tabla.getCellEditor().stopCellEditing();
            }

            DefaultTableModel tb = (DefaultTableModel) tabla.getModel();

            for (int i = tabla.getRowCount() - 1; i >= 0; i--) {
                tb.removeRow(i);
            }
        }
    }

    public static void hideColumn(JTable table, int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);
    }

    public static void showColumn(JTable table, int columnIndex, int defautlWidth, int maxWidth) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        //column.setMinWidth(width);
        column.setMaxWidth(maxWidth);
        column.setWidth(defautlWidth);
        column.setPreferredWidth(defautlWidth);
    }

    public static void alignTbHeadersToRigth(JTable tbReceips) {
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tbReceips.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
    }

    public static Object getCellValueByHeader(JTable table, String headerTitle, int rowIndex) {
        int columnIndex = -1;
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).toUpperCase().equals(headerTitle.toUpperCase())) {
                columnIndex = i;
                break;
            }
        }
        if (columnIndex != -1 && rowIndex < table.getRowCount()) {
            return table.getValueAt(rowIndex, columnIndex);
        }
        return null;

    }
}

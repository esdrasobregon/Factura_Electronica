/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import data.CrudProvContado.CrudProvedorContado;
import entitys.ProveedorContado.CuentaProveedorContado;
import entitys.ProveedorContado.TelefonoSinpeContado;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import logic.AppStaticValues;
import services.SimpleExcelWriter;
import view.util.JTableCommonFunctions;

/**
 *
 * @author eobregon
 */
public class ReporteAbonoSugeridoContado2 extends javax.swing.JPanel {

    /**
     * Creates new form ReporteAbonoSugeridoContado2
     */
    boolean loadingInfo = false;
    data.CRUDAbonoSugeridoContado crud = new data.CRUDAbonoSugeridoContado();
    ArrayList<entitys.AbonoSugeridoContado> listaAbonosContado = new ArrayList<entitys.AbonoSugeridoContado>();

    public ReporteAbonoSugeridoContado2() {
        initComponents();
        prepareGUI();
    }

    private void prepareGUI() {
        setTbListeners();

    }

    private void resumirInfoColumns() {
        if (!loadingInfo) {
            int[] selectedRows = tbReporte.getSelectedRows();
            double sumAbonoC = 0;
            double sumAbonoD = 0;
            double sumAbonoDC = 0;
            double montoOrCol = 0;
            double montoOrDol = 0;
            double montoDolEnCol = 0;
            this.txtResumen.setText("");
            for (int row : selectedRows) {
                int doc = Integer.parseInt(tbReporte.getValueAt(row, 0).toString());
                entitys.AbonoSugeridoContado ab = entitys.AbonoSugeridoContado.obtAbonoSugeridoContadoPorId(doc, listaAbonosContado);
                if (ab.getMoneda().equals("CRC")) {
                    sumAbonoC += ab.getAbono();
                    montoOrCol += ab.getAbonoColones();
                } else {
                    sumAbonoD += ab.getAbono();
                    montoOrDol += ab.getMontoOriginal();
                    montoDolEnCol += ab.getMontoOriginalColones();
                }
                sumAbonoDC += ab.getAbonoColones();
            }
            this.txtResumen.setText(
                    "Sm abonos: ₡" + AppStaticValues.numberFormater.format(sumAbonoC)
                    + "\tSm monto originales: ₡" + AppStaticValues.numberFormater.format(montoOrCol)
                    + "\nSm abonos: $" + AppStaticValues.numberFormater.format(sumAbonoD)
                    + " <--> ₡" + AppStaticValues.numberFormater.format(montoDolEnCol)
                    + "\tSm monto originales: $" + AppStaticValues.numberFormater.format(montoOrDol)
                    + "\nSm todos los abonos: ₡" + AppStaticValues.numberFormater.format(sumAbonoDC));

        }
    }

    private void setTbListeners() {
        tbReporte.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Your code here to handle table content change event
                if (!loadingInfo) {

                    int column = tbReporte.getSelectedColumn();
                    int row = tbReporte.getSelectedRow();
                    if (column == 14) {
                        int id = Integer.parseInt(tbReporte.getValueAt(row, 0).toString());
                        boolean res = crud.actSugeridoContadoRevisado(id);
                        if (res) {
                            loadAsyncInfo();
                        }

                        //actualizarAbono();
                    }
                }
            }
        });
        tbReporte.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    resumirInfoColumns();
                    setProveedorInfo();
                }
            }

        }
        );
        this.tbReporte.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tbReporte.rowAtPoint(e.getPoint());
                    tbReporte.addRowSelectionInterval(row, row);
                    //jPopupMenu1.show(tbReporte, e.getX(), e.getY());
                }
            }
        });
    }

    private void setProveedorInfo() {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    if (tbReporte.getSelectedRowCount() > 1) {
                        txtProveedor.setText("");
                        return;
                    }
                    int row = tbReporte.getSelectedRow();
                    int idProv = Integer.parseInt(tbReporte.getValueAt(row, 3).toString());
                    data.CrudProvContado.CrudProvedorContado crd = new CrudProvedorContado();
                    ArrayList<entitys.ProveedorContado.TelefonoSinpeContado> telefonos = crd.obtenerListaSinpeProveedorContado(idProv);
                    ArrayList<entitys.ProveedorContado.CuentaProveedorContado> cuentas = crd.obtenerListaCtaProveedorContado(idProv);
                    String result = "";
                    for (TelefonoSinpeContado t : telefonos) {
                        result += "SINPE: " + t.getNumero()
                                + " (" + (t.getEstado() == 1 ? "Activo" : "Inactivo") + ")\n";
                    }
                    for (CuentaProveedorContado c : cuentas) {
                        result += "Banco: " + c.getBanco() + "\tCuenta: " + c.getNumero()
                                + " (" + (c.getEstado() == 1 ? "Activo" : "Inactivo") + ")\n";
                    }
                    txtProveedor.setText(result);
                } catch (Exception e) {
                    txtProveedor.setText("");
                    System.err.println("setProveedorInfo() error " + e.getMessage());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbReporte = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResumen = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtProveedor = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        lbTbinfo = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbCia = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        btnExpExcel = new javax.swing.JButton();
        btnRefreshFilt = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Reporte Sugerido Pagos Contado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(7);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        tbReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idLinea", "Id", "Sociedad", "Proveedor", "Nombre Proveedor", "F Documento", "Cta Pres", "Cta Presupuesto", "Factura", "Moneda", "Saldo ₡", "Abono ₡", "Saldo $", "Abono $", "Revisado", "Forma Pago", "Adelanto", "F Solicitud"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbReporte.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbReporte.setShowVerticalLines(true);
        tbReporte.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbReporte);
        if (tbReporte.getColumnModel().getColumnCount() > 0) {
            tbReporte.getColumnModel().getColumn(0).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(0).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(1).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(1).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(1).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(3).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(3).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(3).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(4).setPreferredWidth(300);
            tbReporte.getColumnModel().getColumn(7).setPreferredWidth(300);
            tbReporte.getColumnModel().getColumn(17).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(17).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(17).setMaxWidth(0);
        }

        jPanel8.add(jScrollPane1);

        jSplitPane1.setLeftComponent(jPanel8);

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Resumen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txtResumen.setEditable(false);
        txtResumen.setColumns(20);
        txtResumen.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtResumen.setRows(5);
        jScrollPane2.setViewportView(txtResumen);

        jPanel12.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Informacioón Proveedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txtProveedor.setEditable(false);
        txtProveedor.setColumns(20);
        txtProveedor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtProveedor.setRows(5);
        jScrollPane3.setViewportView(txtProveedor);

        jPanel12.add(jScrollPane3, java.awt.BorderLayout.LINE_END);

        jPanel9.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel13.setPreferredSize(new java.awt.Dimension(1182, 25));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbTbinfo.setText("Filas: 0");
        jPanel13.add(lbTbinfo);

        jPanel9.add(jPanel13, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setRightComponent(jPanel9);

        jPanel6.add(jSplitPane1);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel7.setPreferredSize(new java.awt.Dimension(489, 30));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setText("CIA");
        jPanel10.add(jLabel1);

        cmbCia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "RYMSA", "CILT" }));
        cmbCia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCiaItemStateChanged(evt);
            }
        });
        jPanel10.add(cmbCia);

        jPanel7.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel2.setText("Moneda");
        jPanel11.add(jLabel2);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "CRC", "USD" }));
        cmbMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaItemStateChanged(evt);
            }
        });
        jPanel11.add(cmbMoneda);

        jPanel7.add(jPanel11);

        btnExpExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExpExcel.setToolTipText("Exportar a excel");
        btnExpExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpExcelActionPerformed(evt);
            }
        });
        jPanel7.add(btnExpExcel);

        btnRefreshFilt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refrescar2.png"))); // NOI18N
        btnRefreshFilt.setToolTipText("Refrescar información");
        btnRefreshFilt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshFiltActionPerformed(evt);
            }
        });
        jPanel7.add(btnRefreshFilt);

        jPanel1.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 30));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1002, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(400, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1002, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(20, 240));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(20, 240));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshFiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshFiltActionPerformed
        // TODO add your handling code here:
        loadAsyncInfo();

    }//GEN-LAST:event_btnRefreshFiltActionPerformed

    private void btnExpExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpExcelActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                guardarExcel();
            }
        });
        t.start();
    }//GEN-LAST:event_btnExpExcelActionPerformed

    private void cmbCiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCiaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadFiltAsyncInfo();
        }
    }//GEN-LAST:event_cmbCiaItemStateChanged
    private void loadFiltAsyncInfo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                JTableCommonFunctions.limpiarTabla(tbReporte);
                listaAbonosContado = crud.obtenerAbonoHistoricoCPContado_ViewConta(cmbMoneda.getSelectedItem().toString(), cmbCia.getSelectedItem().toString());
                DefaultTableModel model = (DefaultTableModel) tbReporte.getModel();
                listaAbonosContado.forEach(e -> {
                    addRow(model, e);
                });
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    private void cmbMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {

            loadFiltAsyncInfo();
        }
    }//GEN-LAST:event_cmbMonedaItemStateChanged
    private void guardarExcel() {

        SimpleExcelWriter sew = new SimpleExcelWriter();
        //sew.writeToExcell(this.tbConciliacionMarcas);

        boolean saved = sew.writeCtaContadoReportToExcel2("Reporte Sugerido Contado", this.listaAbonosContado);
        if (saved) {
            JOptionPane.showMessageDialog(null, "Se ha guardado el archivo");
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al guardar el archivo");
        }
    }

    private void loadAsyncInfo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                txtResumen.setText("");
                txtProveedor.setText("");
                cmbCia.setSelectedIndex(0);
                cmbMoneda.setSelectedIndex(0);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                JTableCommonFunctions.limpiarTabla(tbReporte);
                listaAbonosContado = crud.obtenerAbonoHistoricoCPContado_ViewConta();
                DefaultTableModel model = (DefaultTableModel) tbReporte.getModel();
                listaAbonosContado.forEach(e -> {
                    addRow(model, e);
                });
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                lbTbinfo.setText("Filas: " + tbReporte.getRowCount());
                loadingInfo = false;
            }

        };
        Thread t = new Thread(r);
        t.start();
    }

    private void addRow(DefaultTableModel model, entitys.AbonoSugeridoContado e) {
        double sumMontoCol = 0;
        double sumMontoDol = 0;
        double sumAbCol = 0;
        double sumAbdDol = 0;
        boolean montCr = (e.getMontoOriginal() == e.getAbono()) && (e.getMoneda().equals("CRC"));
        boolean abCRC = !(e.getMontoOriginal() == e.getAbono()) && e.getMoneda().equals("CRC");
        boolean montD0l = (e.getMontoOriginal() == e.getAbono()) && !(e.getMoneda().equals("CRC"));
        boolean abDol = !(e.getMontoOriginal() == e.getAbono()) && !e.getMoneda().equals("CRC");
        model.addRow(new Object[]{
            e.getIdAbonoSugeridoContado(),
            e.getId(),
            e.getSociedad(),
            e.getProveedor(),
            e.getNombreProveedor(),
            logic.AppStaticValues.dateFormat.format(e.getFechaDocumento()),
            e.getCtPresupuesto(),
            e.getDescCtaPres(),
            e.getDocumento(),
            e.getMoneda(),
            montCr
            ? logic.AppStaticValues.numberFormater.format(e.getAbono())
            : "",
            abCRC
            ? logic.AppStaticValues.numberFormater.format(e.getAbono())
            : "",
            montD0l
            ? logic.AppStaticValues.numberFormater.format(e.getAbono())
            : "",
            abDol
            ? logic.AppStaticValues.numberFormater.format(e.getAbono())
            : "",
            e.getRevisadoConta() == 1 ? true : false,
            e.getForma_pago(),
            e.getAdelanto() == 1 ? "Si" : "No",
            logic.AppStaticValues.dateFormat.format(e.getFechaSolicitud())
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExpExcel;
    private javax.swing.JButton btnRefreshFilt;
    private javax.swing.JComboBox<String> cmbCia;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbTbinfo;
    private javax.swing.JTable tbReporte;
    private javax.swing.JTextArea txtProveedor;
    private javax.swing.JTextArea txtResumen;
    // End of variables declaration//GEN-END:variables
}

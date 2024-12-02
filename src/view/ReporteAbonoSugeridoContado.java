/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import data.CrudProvContado.CrudProvedorContado;
import data.DataUser;
import entitys.HistoricoCP;
import entitys.ProveedorContado.CuentaProveedorContado;
import entitys.ProveedorContado.TelefonoSinpeContado;
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
public class ReporteAbonoSugeridoContado extends javax.swing.JPanel {

    ArrayList<HistoricoCP> listaHistoricos;
    data.CrudHistoricoContado crdc;
    private boolean loadingInfo = false;

    /**
     * Creates new form ReporteAbonoSugeridoContado
     */
    public ReporteAbonoSugeridoContado() {
        initComponents();
        this.crdc = new data.CrudHistoricoContado();
        prepareGui();
    }

    private void prepareGui() {
        loadInfo();
        setView();
        prepareListeners();
    }

    private void prepareListeners() {
        setTbHistoricoEvents();
    }

    private void loadInfo() {
        this.listaHistoricos = crdc.obtenerHistoricoCPContado_ViewConta("", "", "");

    }

    private void setView() {
        this.pnlProv.setEnabled(false);
        this.pnlProv.setVisible(false);
        loadTbReporte();
        setTbListeners();
    }

    private void setTbListeners() {
        tbReporte.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Your code here to handle table content change event
                if (!loadingInfo) {

                    int column = tbReporte.getSelectedColumn();
                    int row = tbReporte.getSelectedRow();
                    if (column == 11) {
                        actualizarAbono();
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

    }

    private void setTbHistoricoEvents() {
        this.tbReporte.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tbReporte.rowAtPoint(e.getPoint());
                    tbReporte.addRowSelectionInterval(row, row);
                    jPopupMenu1.show(tbReporte, e.getX(), e.getY());
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
                    int idProv = Integer.parseInt(tbReporte.getValueAt(row, 16).toString());
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
                int doc = Integer.parseInt(tbReporte.getValueAt(row, 13).toString());
                HistoricoCP ab = HistoricoCP.obtenerHistoricoPorId(doc, listaHistoricos);
                if (ab.getMONEDA().equals("CRC")) {
                    sumAbonoC += ab.getSaldo();
                    montoOrCol += ab.getMONTO();
                } else {
                    sumAbonoD += ab.getSaldo();
                    montoOrDol += ab.getMONTO();
                    montoDolEnCol += ab.getMonto_colones();
                }
                sumAbonoDC += ab.getMonto_colones();
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

    private void actualizarAbono() {
        try {
            int column = tbReporte.getSelectedColumn();
            int row = tbReporte.getSelectedRow();
            if (column == 11) {
                //actualizar
                int id = Integer.parseInt(tbReporte.getValueAt(row, 13).toString());
                HistoricoCP ab = HistoricoCP.obtenerHistoricoPorId(id, listaHistoricos);
                ab.setRevisadoConta(1);
                ab.setFecha_Revision_Conta(new java.util.Date());
                ab.setUsuarioRevision(DataUser.username);
                if (ab != null) {
                    boolean res = crdc.ActualizarHistoricoContadoConta(ab);
                    if (res) {
                        System.out.println("se h actualizado el documento " + ab.getDOCUMENTO());
                        afterUpdating();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se ha actualizado el abono");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha actualizado el abono");
                }
            }
        } catch (Exception ex) {
            System.out.println("view.ReporteAbonoSugerido.actualizarAbono() error " + ex.getMessage());
        }
    }

    private void afterUpdating() {
        this.loadingInfo = true;
        view.util.JTableCommonFunctions.limpiarTabla(tbReporte);
        createQuery();
        loadTbReporte();
        this.loadingInfo = false;
    }

    private void loadTbReporte() {
        DefaultTableModel model = (DefaultTableModel) this.tbReporte.getModel();
        double sumMontoCol = 0;
        double sumMontoDol = 0;
        double sumAbCol = 0;
        double sumAbdDol = 0;
        for (HistoricoCP e : listaHistoricos) {
            boolean abonoEqSaldo = e.getMONTO() == e.getAbono();
            sumMontoCol += e.getMONEDA().equals("CRC") ? e.getMONTO() : 0;
            sumAbCol += e.getMONEDA().equals("CRC") ? e.getSaldo() : 0;
            sumMontoDol += e.getMONEDA().equals("USD") ? e.getMONTO() : 0;
            sumAbdDol += e.getMONEDA().equals("USD") ? e.getSaldo() : 0;
            String sing = e.getMONEDA().equals("CRC") ? "₡" : "$";
            model.addRow(new Object[]{
                e.getTIPOPROV(),
                e.getMONEDA(),
                e.getCIA(),
                e.getNOMBRE(),
                e.getFECHA_DOCUMENTO(),
                e.getDesc_Cta_Pres(),
                e.getDOCUMENTO(),
                abonoEqSaldo ? e.getMONEDA().equals("CRC")
                ? //sing + 
                logic.AppStaticValues.numberFormater.format(e.getAbono()) : "" : "",
                abonoEqSaldo ? "" : e.getMONEDA().equals("CRC")
                ? //sing + 
                logic.AppStaticValues.numberFormater.format(e.getAbono()) : "",
                abonoEqSaldo ? e.getMONEDA().equals("USD")
                ? //sing + 
                logic.AppStaticValues.numberFormater.format(e.getAbono()) : "" : "",
                abonoEqSaldo ? "" : e.getMONEDA().equals("USD")
                ? //sing + 
                logic.AppStaticValues.numberFormater.format(e.getAbono()) : "",
                e.getRevisadoConta() == 1 ? true : false,
                e.getFecha_Creacion(),
                e.getId(),
                e.getCTA_PRESUPUESTO(),
                e.getNOMBRE(),
                e.getPROVEEDOR().trim(),
                e.getForma_Pago(),
                e.getAdelanto() == 1 ? "Si" : "No"
            });
        }
        this.lbTbinfo.setText("Filas: " + tbReporte.getRowCount()
                + "     Suma montos: ₡" + logic.AppStaticValues.numberFormater.format(sumMontoCol)
                + "     Suma abonos: ₡" + logic.AppStaticValues.numberFormater.format(sumAbCol)
                + "     Suma montos: $" + logic.AppStaticValues.numberFormater.format(sumMontoDol)
                + "     Suma abonos: $" + logic.AppStaticValues.numberFormater.format(sumAbdDol));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        nmOpenFiles = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbReporte = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbCia = new javax.swing.JComboBox<>();
        pnlProv = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        btnExpExcel = new javax.swing.JButton();
        btnRefreshFilt = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        lbTbinfo = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtResumen = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtProveedor = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();

        nmOpenFiles.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/attach-24.png"))); // NOI18N
        nmOpenFiles.setText("Abrir archivos adjuntos");
        nmOpenFiles.setToolTipText("Abrir archivos adjuntos");
        nmOpenFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nmOpenFilesActionPerformed(evt);
            }
        });
        jPopupMenu1.add(nmOpenFiles);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        tbReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Proveedor", "Moneda", "Sociedad", "Proveedor", "Fecha Documento", "Cta Presupuesto", "Factura", "Saldo ₡	", "Abono ₡	", "Saldo $", "Abono $", "Revisado", "FachaAbono", "Id", "Cta Pres", "Nombre proveedor", "#Proveedor", "Forma de pago", "Adelanto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbReporte.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbReporte.setShowGrid(true);
        tbReporte.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbReporte);
        if (tbReporte.getColumnModel().getColumnCount() > 0) {
            tbReporte.getColumnModel().getColumn(3).setPreferredWidth(300);
            tbReporte.getColumnModel().getColumn(5).setPreferredWidth(200);
            tbReporte.getColumnModel().getColumn(12).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(12).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(12).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(13).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(13).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(13).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(15).setPreferredWidth(300);
            tbReporte.getColumnModel().getColumn(16).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(16).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(16).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(17).setPreferredWidth(200);
        }

        jPanel4.add(jScrollPane2);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(645, 30));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel10.setLayout(flowLayout1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("CIA");
        jPanel10.add(jLabel1);

        cmbCia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "RYMSA", "CILT" }));
        cmbCia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCiaItemStateChanged(evt);
            }
        });
        jPanel10.add(cmbCia);

        jPanel8.add(jPanel10);

        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout2.setAlignOnBaseline(true);
        pnlProv.setLayout(flowLayout2);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Proveedor");
        pnlProv.add(jLabel2);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorItemStateChanged(evt);
            }
        });
        pnlProv.add(cmbProveedor);

        jPanel8.add(pnlProv);

        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout3.setAlignOnBaseline(true);
        jPanel12.setLayout(flowLayout3);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Moneda");
        jPanel12.add(jLabel3);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "CRC", "USD" }));
        cmbMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaItemStateChanged(evt);
            }
        });
        jPanel12.add(cmbMoneda);

        jPanel8.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        btnExpExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExpExcel.setToolTipText("Exportar a excel");
        btnExpExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpExcelActionPerformed(evt);
            }
        });
        jPanel13.add(btnExpExcel);

        btnRefreshFilt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refrescar2.png"))); // NOI18N
        btnRefreshFilt.setToolTipText("Refrescar la información");
        btnRefreshFilt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshFiltActionPerformed(evt);
            }
        });
        jPanel13.add(btnRefreshFilt);

        jPanel8.add(jPanel13);

        jPanel1.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        jPanel9.setPreferredSize(new java.awt.Dimension(645, 180));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel14.setPreferredSize(new java.awt.Dimension(0, 30));
        java.awt.FlowLayout flowLayout4 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout4.setAlignOnBaseline(true);
        jPanel14.setLayout(flowLayout4);

        lbTbinfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbTbinfo.setText("Filas");
        jPanel14.add(lbTbinfo);

        jPanel9.add(jPanel14, java.awt.BorderLayout.PAGE_START);

        jPanel15.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 0, new java.awt.Color(204, 204, 204)), "Resumen", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

        txtResumen.setEditable(false);
        txtResumen.setColumns(20);
        txtResumen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtResumen.setRows(5);
        jScrollPane1.setViewportView(txtResumen);

        jPanel15.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Información de proveedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(204, 204, 204))); // NOI18N
        jScrollPane3.setPreferredSize(new java.awt.Dimension(500, 86));

        txtProveedor.setEditable(false);
        txtProveedor.setColumns(20);
        txtProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtProveedor.setRows(5);
        jScrollPane3.setViewportView(txtProveedor);

        jPanel15.add(jScrollPane3, java.awt.BorderLayout.LINE_END);

        jPanel9.add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel9, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(665, 40));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1002, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(665, 40));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1002, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 511));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 140, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 139, Short.MAX_VALUE)))
        );

        add(jPanel5, java.awt.BorderLayout.LINE_END);

        jPanel7.setPreferredSize(new java.awt.Dimension(10, 511));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 379, Short.MAX_VALUE)
        );

        add(jPanel7, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshFiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshFiltActionPerformed
        // TODO add your handling code here:
        this.loadingInfo = true;
        this.cmbCia.setSelectedIndex(0);
        this.loadingInfo = false;
        refreshFilters();
    }//GEN-LAST:event_btnRefreshFiltActionPerformed

    private void cmbCiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCiaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            refreshFilters();

        }
    }//GEN-LAST:event_cmbCiaItemStateChanged

    private void cmbProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            refreshFilters();
            JOptionPane.showMessageDialog(null, "ejecutando cmb monEDAD");
        }
    }//GEN-LAST:event_cmbProveedorItemStateChanged

    private void cmbMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            refreshFilters();
        }
    }//GEN-LAST:event_cmbMonedaItemStateChanged

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

    private void nmOpenFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nmOpenFilesActionPerformed
        // TODO add your handling code here:
        int row = tbReporte.getSelectedRow();
        boolean flag = tbReporte.getSelectedRowCount() == 1;
        if (row > -1 && flag) {

            HistoricoCP h = HistoricoCP.obtenerHistoricoPorId((int) tbReporte.getValueAt(row, 13), listaHistoricos);
            String id = h.getTipo_Documento() + "_" + tbReporte.getValueAt(row, 13).toString() + "_" + h.getFECHA_DOCUMENTO();
            logic.util.FileHandler fh = new logic.util.FileHandler();
            //fh.openFilesBySerchinString(id, logic.AppStaticValues.archivosFacturasContado);
            boolean res = fh.getFilteredfiles(logic.AppStaticValues.archivosFacturasContado, id);
            if (!res) {
                JOptionPane.showMessageDialog(null, "No se han encontrado archivos asociados");
            }
        } else if (!flag) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione sólamenete una fila");
        }
    }//GEN-LAST:event_nmOpenFilesActionPerformed
    private void guardarExcel() {

        SimpleExcelWriter sew = new SimpleExcelWriter();
        //sew.writeToExcell(this.tbConciliacionMarcas);

        boolean saved = sew.writeCtaContadoReportToExcel("Reporte Sugerido Contado", this.listaHistoricos);
        if (saved) {
            JOptionPane.showMessageDialog(null, "Se ha guardado el archivo");
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al guardar el archivo");
        }
    }

    private void refreshFilters() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                JTableCommonFunctions.limpiarTabla(tbReporte);
                //cmbProveedor.removeAllItems();
                //cmbProveedor.addItem("Todos");
                //cmbMoneda.setSelectedIndex(0);
                createQuery();
                loadTbReporte();
                //loadCmbProveedores();
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void loadCmbProveedores() {
        ArrayList<String> lista = new ArrayList<>();
        this.listaHistoricos.forEach(e -> {
            String prov = e.getNOMBRE().replaceAll("-", "") + "-" + e.getPROVEEDOR();
            if (!lista.contains(prov)) {

                lista.add(prov);
            }
        });
        lista.forEach(e -> {
            this.cmbProveedor.addItem(e);
        });
    }

    private void createQuery() {

        String cia = this.cmbCia.getSelectedItem().toString();
        if (cia.equals("Todas")) {
            cia = "";
        }
        String prov = this.cmbProveedor.getSelectedItem().toString().isEmpty()
                ? ""
                : this.cmbProveedor.getSelectedItem().toString();
        String numP = prov.equals("Todos")
                ? ""
                : prov.substring(prov.indexOf("-") + 1, prov.length());
        String moneda = this.cmbMoneda.getSelectedItem().toString().equals("Todas")
                ? ""
                : this.cmbMoneda.getSelectedItem().toString();

        this.listaHistoricos = crdc.obtenerHistoricoCPContado_ViewConta(cia, moneda, numP);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExpExcel;
    private javax.swing.JButton btnRefreshFilt;
    private javax.swing.JComboBox<String> cmbCia;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbTbinfo;
    private javax.swing.JMenuItem nmOpenFiles;
    private javax.swing.JPanel pnlProv;
    private javax.swing.JTable tbReporte;
    private javax.swing.JTextArea txtProveedor;
    private javax.swing.JTextArea txtResumen;
    // End of variables declaration//GEN-END:variables

}

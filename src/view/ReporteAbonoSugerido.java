/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import entitys.AbonoSugerido;
import entitys.HistoricoCP;
import entitys.Presupuesto;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import logic.AppStaticValues;
import services.SimpleExcelWriter;
import view.util.JTableCommonFunctions;

/**
 *
 * @author eobregon
 */
public class ReporteAbonoSugerido extends javax.swing.JPanel {

    /**
     * Creates new form ReporteAbonoSugerido
     */
    private int currentWeek;
    private data.CrudAbonoSugerido crda;
    private ArrayList<AbonoSugerido> listaAbonoSug;
    private boolean loadingInfo = false;
    data.CRUDHistoricoCP crh;
    ArrayList<HistoricoCP> listaHistoricos;

    public ReporteAbonoSugerido() {

        initComponents();
        this.crda = new data.CrudAbonoSugerido();
        this.crh = new data.CRUDHistoricoCP();
        prepareGui();
    }

    private void prepareGui() {
        loadInfo();
        setView();
    }

    private void loadInfo() {
        this.listaAbonoSug = crda.getAbonoSugeridos("", "", "");

    }

    private void setView() {
        this.jProgressBar1.setVisible(false);
        this.pnlContred.setVisible(false);
        this.pnlContred.setEnabled(false);
        tbReporte.getColumnModel().getColumn(7).setCellRenderer(AppStaticValues.rightRenderer);
        tbReporte.getColumnModel().getColumn(8).setCellRenderer(AppStaticValues.rightRenderer);
        tbReporte.getColumnModel().getColumn(9).setCellRenderer(AppStaticValues.rightRenderer);
        tbReporte.getColumnModel().getColumn(10).setCellRenderer(AppStaticValues.rightRenderer);
        setTbListeners();
        loadTbReporte();
        addCellColorCode(tbReporte, 18);
        loadCmbProveedores();
    }

    private void loadCmbProveedores() {
        ArrayList<String> lista = new ArrayList<>();
        this.listaAbonoSug.forEach(e -> {
            String prov = e.getNombre_Proveedor().replaceAll("-", "") + "-" + e.getNumero_Proveedor();
            if (!lista.contains(prov)) {

                lista.add(prov);
            }
        });
        lista.forEach(e -> {
            this.cmbProveedor.addItem(e);
        });
    }

    private void setTbListeners() {
        tbReporte.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Your code here to handle table content change event
                if (!loadingInfo) {

                    int column = tbReporte.getSelectedColumn();
                    int row = tbReporte.getSelectedRow();
                    if (column == 12) {
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
                }
            }

        }
        );

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
                int doc = Integer.parseInt(tbReporte.getValueAt(row, 14).toString());
                AbonoSugerido ab = AbonoSugerido.obtenerAbonoPorId(doc, listaAbonoSug);
                if (ab.getMoneda().equals("CRC")) {
                    sumAbonoC += ab.getAbono();
                    montoOrCol += ab.getMonto_Original();
                } else {
                    sumAbonoD += ab.getAbono();
                    montoOrDol += ab.getMonto_Original();
                    montoDolEnCol += ab.getMonto_Colones();
                }
                sumAbonoDC += ab.getMonto_Colones();
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
            if (column == 12) {
                //actualizar
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String doc = tbReporte.getValueAt(row, 7).toString();
                String dateString = tbReporte.getValueAt(row, 13).toString();
                int id = Integer.parseInt(tbReporte.getValueAt(row, 14).toString());
                java.util.Date date = dateFormat.parse(dateString);
                AbonoSugerido ab = AbonoSugerido.obtenerAbonoFromList(doc, date, id, listaAbonoSug);
                String cuenta = tbReporte.getValueAt(row, 6).toString();
                ab.setCuenta_Presupuesto(cuenta);
                ab.setRevisadoConta(1);
                data.CrudAbonoSugerido crd = new data.CrudAbonoSugerido();
                if (ab != null) {
                    boolean res = crd.actualizarAbonoSugeridoConta(ab);
                    if (res) {
                        System.out.println("se h actualizado el documento " + ab.getDocumento());
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
        double sumSaldoCol = 0;
        double sumSaldDol = 0;
        double sumAbCol = 0;
        double sumAbdDol = 0;
        for (AbonoSugerido e : listaAbonoSug) {
            boolean abonoEqSaldo = e.getAbono() == e.getSaldo_Actuual();
            sumSaldoCol += e.getMoneda().equals("CRC") ? e.getSaldo_Actuual() : 0;
            sumAbCol += e.getMoneda().equals("CRC") ? e.getAbono() : 0;
            sumSaldDol += e.getMoneda().equals("USD") ? e.getSaldo_Actuual() : 0;
            sumAbdDol += e.getMoneda().equals("USD") ? e.getAbono() : 0;
            String sing = e.getMoneda().equals("CRC") ? "₡" : "$";
            model.addRow(new Object[]{
                e.getTipo_Proveedor(),
                e.getMoneda(),
                e.getCIA(),
                e.getNombre_Proveedor(),
                e.getFecha_Creacion(),
                e.getFecha_documento(),
                e.getDescripion_Cta_Presupuesto(),
                e.getDocumento(),
                abonoEqSaldo ? e.getMoneda().equals("CRC")
                ? logic.AppStaticValues.numberFormater.format(e.getSaldo_Actuual()) : "" : "",
                abonoEqSaldo ? "" : e.getMoneda().equals("CRC")
                ? logic.AppStaticValues.numberFormater.format(e.getAbono()) : "",
                abonoEqSaldo ? e.getMoneda().equals("USD")
                ? logic.AppStaticValues.numberFormater.format(e.getSaldo_Actuual()) : "" : "",
                abonoEqSaldo ? "" : e.getMoneda().equals("USD")
                ? logic.AppStaticValues.numberFormater.format(e.getAbono()) : "",
                e.getRevisadoConta() == 1 ? true : false,
                e.getFecha_Creacion(),
                e.getId(),
                e.getCuenta_Presupuesto(),
                e.getComentarios(),
                e.getDocumentoExactus()
            });
        }
        this.lbTbinfo.setText("Filas: " + tbReporte.getRowCount()
                //+ "     Suma saldos: ₡" + logic.AppStaticValues.numberFormater.format(sumSaldoCol)
                + "     Suma abonos: ₡" + logic.AppStaticValues.numberFormater.format(sumAbCol)
                + "     Suma saldos: $" + logic.AppStaticValues.numberFormater.format(sumSaldDol)
                + "     Suma abonos: $" + logic.AppStaticValues.numberFormater.format(sumAbdDol));

    }

    /**
     * this method sets the background color o a given jtable cell given the
     * value of the left cell -1 rgb(60, 63, 65), 0 red and >0 green
     */
    private void addCellColorCode(JTable table, int column) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        tableColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String v = (String) table.getValueAt(row, column - 1);
                if (v == null) {
                    c.setBackground(Color.red);
                    c.setToolTipText("Favor revizar documento en Exactus");
                } else {
                    //c.setBackground(Color.green);
                    c.setBackground(new Color(60, 63, 65));
                    c.setToolTipText("");
                }

                return c;
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        mnUpdate = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbReporte = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbCia = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        pnlContred = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbTipoPago = new javax.swing.JComboBox<>();
        btnExpEcxel = new javax.swing.JButton();
        btnRefTb = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResumen = new javax.swing.JTextArea();
        jPanel10 = new javax.swing.JPanel();
        lbTbinfo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        jMenu1.setText("jMenu1");

        mnUpdate.setText("jMenuItem1");
        jMenu1.add(mnUpdate);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Resumen detallado de pagos a proveedor ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        tbReporte.setAutoCreateRowSorter(true);
        tbReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Proveedor", "Moneda", "Sociedad", "Proveedor", "F. Solicitud", "Fecha Documento", "Cta Presupuesto", "Factura", "Saldo ₡", "Abono ₡", "Saldo $", "Abono $", "Revisado", "FechaAbono", "id", "Cta Pres", "Observaciones", "Documento Ex", "!"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbReporte.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbReporte.setFillsViewportHeight(true);
        tbReporte.setShowGrid(true);
        tbReporte.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbReporte);
        if (tbReporte.getColumnModel().getColumnCount() > 0) {
            tbReporte.getColumnModel().getColumn(3).setPreferredWidth(300);
            tbReporte.getColumnModel().getColumn(6).setPreferredWidth(300);
            tbReporte.getColumnModel().getColumn(8).setPreferredWidth(100);
            tbReporte.getColumnModel().getColumn(9).setPreferredWidth(100);
            tbReporte.getColumnModel().getColumn(10).setPreferredWidth(100);
            tbReporte.getColumnModel().getColumn(11).setPreferredWidth(100);
            tbReporte.getColumnModel().getColumn(13).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(13).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(13).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(14).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(14).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(14).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(15).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(15).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(15).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(17).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(17).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(17).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(18).setMinWidth(20);
            tbReporte.getColumnModel().getColumn(18).setPreferredWidth(20);
            tbReporte.getColumnModel().getColumn(18).setMaxWidth(20);
        }
        tbReporte.getTableHeader().setPreferredSize(new java.awt.Dimension(jScrollPane1.getWidth(),30));
        tbReporte.getTableHeader().setBackground(new java.awt.Color(102,102,102));
        tbReporte.getTableHeader().setForeground(new java.awt.Color(255,255,255));
        tbReporte.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14) {});

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(984, 40));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        jPanel12.setLayout(flowLayout1);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("CIA");
        jPanel12.add(jLabel3);

        cmbCia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "RYMSA", "CILT" }));
        cmbCia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCiaItemStateChanged(evt);
            }
        });
        cmbCia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCiaActionPerformed(evt);
            }
        });
        jPanel12.add(cmbCia);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Proveedor");
        jPanel12.add(jLabel4);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorItemStateChanged(evt);
            }
        });
        jPanel12.add(cmbProveedor);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Moneda");
        jPanel12.add(jLabel5);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "CRC", "USD" }));
        cmbMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaItemStateChanged(evt);
            }
        });
        jPanel12.add(cmbMoneda);

        jPanel6.add(jPanel12);

        pnlContred.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel2.setText("Tipo pago");
        pnlContred.add(jLabel2);

        cmbTipoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CREDITO", "CONTADO" }));
        cmbTipoPago.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoPagoItemStateChanged(evt);
            }
        });
        pnlContred.add(cmbTipoPago);

        jPanel6.add(pnlContred);

        btnExpEcxel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExpEcxel.setToolTipText("Exportar a excel");
        btnExpEcxel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpEcxelActionPerformed(evt);
            }
        });
        jPanel6.add(btnExpEcxel);

        btnRefTb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refrescar2.png"))); // NOI18N
        btnRefTb.setToolTipText("Refrescar filtros");
        btnRefTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefTbActionPerformed(evt);
            }
        });
        jPanel6.add(btnRefTb);

        jPanel1.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel8.setPreferredSize(new java.awt.Dimension(960, 100));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        txtResumen.setEditable(false);
        txtResumen.setColumns(20);
        txtResumen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtResumen.setRows(5);
        txtResumen.setText("\n");
        jScrollPane2.setViewportView(txtResumen);

        jPanel11.add(jScrollPane2);

        jPanel9.add(jPanel11);

        jPanel8.add(jPanel9, java.awt.BorderLayout.CENTER);

        lbTbinfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbTbinfo.setText("Filas 0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1018, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lbTbinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 1018, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(lbTbinfo)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel8.add(jPanel10, java.awt.BorderLayout.NORTH);

        jPanel1.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(1004, 50));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Reporte Abonos Sugeridos");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(867, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel2.add(jPanel7, java.awt.BorderLayout.CENTER);

        jProgressBar1.setOpaque(true);
        jProgressBar1.setPreferredSize(new java.awt.Dimension(146, 20));
        jProgressBar1.setStringPainted(true);
        jPanel2.add(jProgressBar1, java.awt.BorderLayout.PAGE_START);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(1004, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1030, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 577));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 577));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnExpEcxelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpEcxelActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                guardarExcel();
            }
        });
        t.start();
    }//GEN-LAST:event_btnExpEcxelActionPerformed

    private void btnRefTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefTbActionPerformed
        // TODO add your handling code here:
        //afterUpdating();
        this.loadingInfo = true;
        this.cmbCia.setSelectedIndex(0);
        this.loadingInfo = false;
        refreshFilters();
    }//GEN-LAST:event_btnRefTbActionPerformed

    private void cmbTipoPagoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoPagoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {

            loadingInfo = true;
            view.util.JTableCommonFunctions.limpiarTabla(tbReporte);
            String tp = cmbTipoPago.getSelectedItem().toString();
            if (tp.equalsIgnoreCase("Credito")) {
                //this.listaAbonoSug = crda.getAbonoSugeridos();
                loadTbReporte();
            } else {
                DefaultTableModel model = (DefaultTableModel) tbReporte.getModel();
                this.listaHistoricos = crh.obtenerHistoricoCPContado_View("");
                addTbRow();
            }
            loadingInfo = false;
        }
    }//GEN-LAST:event_cmbTipoPagoItemStateChanged

    private void cmbCiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCiaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            refreshFilters();

        }
    }//GEN-LAST:event_cmbCiaItemStateChanged
    private void refreshFilters() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                JTableCommonFunctions.limpiarTabla(tbReporte);
                jProgressBar1.setString("Cargando información...");
                jProgressBar1.setVisible(true);
                btnRefTb.setEnabled(false);
                cmbProveedor.removeAllItems();
                cmbProveedor.addItem("Todos");
                cmbMoneda.setSelectedIndex(0);
                createQuery();
                loadTbReporte();
                addCellColorCode(tbReporte, 17);
                loadCmbProveedores();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                btnRefTb.setEnabled(true);
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    private void cmbProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    loadingInfo = true;
                    JTableCommonFunctions.limpiarTabla(tbReporte);
                    createQuery();
                    loadTbReporte();
                    loadingInfo = false;
                }
            };
            Thread t = new Thread(r);
            t.start();

        }
    }//GEN-LAST:event_cmbProveedorItemStateChanged

    private void cmbMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    loadingInfo = true;
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    JTableCommonFunctions.limpiarTabla(tbReporte);
                    createQuery();
                    loadTbReporte();
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    loadingInfo = false;
                }
            };
            Thread t = new Thread(r);
            t.start();

        }
    }//GEN-LAST:event_cmbMonedaItemStateChanged

    private void cmbCiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCiaActionPerformed
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

        this.listaAbonoSug = crda.getAbonoSugeridos(cia, numP, moneda);

    }

    private void addTbRow() {
        /*DefaultTableModel model = (DefaultTableModel) this.tbReporte.getModel();
        double sumSaldoCol = 0;
        double sumSaldDol = 0;
        double sumAbCol = 0;
        double sumAbdDol = 0;
        for (HistoricoCP e : listaHistoricos) {
            boolean abonoEqSaldo = e.getMONTO()== e.getSaldo();
            sumSaldoCol += e.getMoneda().equals("CRC") ? e.getSaldo_Actuual() : 0;
            sumAbCol += e.getMoneda().equals("CRC") ? e.getAbono() : 0;
            sumSaldDol += e.getMoneda().equals("USD") ? e.getSaldo_Actuual() : 0;
            sumAbdDol += e.getMoneda().equals("USD") ? e.getAbono() : 0;
            String sing = e.getMoneda().equals("CRC") ? "₡" : "$";
            model.addRow(new Object[]{
                e.getTipo_Proveedor(),
                e.getMoneda(),
                e.getCIA(),
                e.getNombre_Proveedor(),
                e.getFecha_documento(),
                e.getDescripion_Cta_Presupuesto(),
                e.getDocumento(),
                abonoEqSaldo ? e.getMoneda().equals("CRC")
                ? sing + logic.AppStaticValues.numberFormater.format(e.getSaldo_Actuual()) : "" : "",
                abonoEqSaldo ? "" : e.getMoneda().equals("CRC")
                ? sing + logic.AppStaticValues.numberFormater.format(e.getAbono()) : "",
                abonoEqSaldo ? e.getMoneda().equals("USD")
                ? sing + logic.AppStaticValues.numberFormater.format(e.getSaldo_Actuual()) : "" : "",
                abonoEqSaldo ? "" : e.getMoneda().equals("USD")
                ? sing + logic.AppStaticValues.numberFormater.format(e.getAbono()) : "",
                e.getRevisadoConta() == 1 ? true : false,
                e.getFecha_Creacion(),
                e.getId(),
                e.getCuenta_Presupuesto()
            });
        this.lbTbinfo.setText("Filas: " + tbReporte.getRowCount()
                + "     Suma saldos: ₡" + logic.AppStaticValues.numberFormater.format(sumSaldoCol)
                + "     Suma abonos: ₡" + logic.AppStaticValues.numberFormater.format(sumAbCol)
                + "     Suma saldos: $" + logic.AppStaticValues.numberFormater.format(sumSaldDol)
                + "     Suma abonos: $" + logic.AppStaticValues.numberFormater.format(sumAbdDol));
        } */

    }

    private void guardarExcel() {

        SimpleExcelWriter sew = new SimpleExcelWriter();
        //sew.writeToExcell(this.tbConciliacionMarcas);

        boolean saved = sew.writeCtaCreditoReportToExcel("Reporte Sugerido Credito", this.listaAbonoSug);
        if (saved) {
            JOptionPane.showMessageDialog(null, "Se ha guardado el archivo");
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al guardar el archivo");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExpEcxel;
    private javax.swing.JButton btnRefTb;
    private javax.swing.JComboBox<String> cmbCia;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JComboBox<String> cmbTipoPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbTbinfo;
    private javax.swing.JMenuItem mnUpdate;
    private javax.swing.JPanel pnlContred;
    private javax.swing.JTable tbReporte;
    private javax.swing.JTextArea txtResumen;
    // End of variables declaration//GEN-END:variables

}

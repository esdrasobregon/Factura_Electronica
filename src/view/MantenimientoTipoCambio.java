/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import entitys.TipoCambio;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import logic.AppStaticValues;
import view.util.JTableCommonFunctions;

/**
 *
 * @author eobregon
 */
public class MantenimientoTipoCambio extends javax.swing.JPanel {

    /**
     * Creates new form MantenimientoTipoCambio
     */
    ArrayList<TipoCambio> listaTipoCambio;
    data.CRUDTipoCambio crtc;

    entitys.TipoCambio tc;
    view.util.CommonFreeLineChart graficoCompra;
    view.util.CommonFreeLineChart graficoVenta;
    boolean loadingInfo = false;

    public MantenimientoTipoCambio() {
        initComponents();
        this.crtc = new data.CRUDTipoCambio();
        prepareGUI();
    }

    private void prepareGUI() {
        loadInfo();
        setView();
        //showBarChart();
        //showLineChart();
    }

    private void loadInfo() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        java.util.Date inicio = cal.getTime();
        java.util.Date fin = new java.util.Date();
        //this.listaTipoCambio = crtc.getTipoCambioPorFechas(inicio, fin);
        this.jdtInicio.setDate(inicio);
        this.jdtFin.setDate(fin);
    }

    private ArrayList<TipoCambio> sortByDate(ArrayList<TipoCambio> persons) {
        ArrayList<TipoCambio> sortedList = new ArrayList<>(persons);
        Collections.sort(sortedList, Comparator.comparing(TipoCambio::getFecha));
        return sortedList;
    }

    private void getLineCharts() {
        this.graficoCompra = new view.util.CommonFreeLineChart(sortByDate(listaTipoCambio), 0);
        graficoCompra.showCompraLineChart(pnlGrf,
                this.lbInfoCompra,
                "Compra",
                "Fluctuación");

        this.graficoVenta = new view.util.CommonFreeLineChart(sortByDate(listaTipoCambio), 1);
        graficoVenta.showCompraLineChart(pnlGfVenta,
                this.lbVentaInfo,
                "Venta",
                "Fluctuación");

        repaint();
        validate();
    }

    private void addRowTipoCambio(DefaultTableModel model, TipoCambio e) {
        model.addRow(new Object[]{
            e.getFecha(), //rec.getFechaEmision(),
            AppStaticValues.numberFormater.format(e.getCompra()),
            AppStaticValues.numberFormater.format(e.getVenta()),
            AppStaticValues.numberFormater.format((e.getDiferencia()))

        });
    }

    private void setView() {

        DefaultTableModel model = (DefaultTableModel) this.tbTipoCambio.getModel();
        this.listaTipoCambio.forEach(e -> {
            addRowTipoCambio(model, e);
        });
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tbTipoCambio.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tbTipoCambio.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tbTipoCambio.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        IDateEditor dateEditor = dtTpC.getDateEditor();
        dateEditor = dtTpC.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
        dateEditor = jdtInicio.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
        dateEditor = jdtFin.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
    }

    private void setGraphsDefaultValues() {
        //compras
        this.cmbChrLbDeg.setSelectedItem("45");
        this.ckGridComp.setSelected(true);
        this.ckLineComp.setSelected(true);
        this.ckPuntosComp.setSelected(false);
        this.ckLbEjx.setSelected(true);
        this.ckLLineComp.setSelected(false);
        //ventas
        this.cmbChrLbDegVen.setSelectedItem("45");
        this.ckGridVenta.setSelected(true);
        this.ckLineVent.setSelected(true);
        this.ckPointVent.setSelected(false);
        this.ckLbEjxVent.setSelected(true);
        this.ckLLineVenta.setSelected(false);

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
        jPanel9 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lbInfoCompra = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        lbVentaInfo = new javax.swing.JTextArea();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCompra = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dtTpC = new com.toedter.calendar.JDateChooser();
        txtVenta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnGuardTC = new javax.swing.JButton();
        btnDeleteTC = new javax.swing.JButton();
        btnBuscarTC = new javax.swing.JButton();
        lbForTCError = new javax.swing.JLabel();
        pnlCompra = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        pnlMnGrfComp = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbChrLbDeg = new javax.swing.JComboBox<>();
        ckGridComp = new javax.swing.JCheckBox();
        ckLineComp = new javax.swing.JCheckBox();
        ckPuntosComp = new javax.swing.JCheckBox();
        ckLbEjx = new javax.swing.JCheckBox();
        ckLLineComp = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlGrf = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTipoCambio = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbTCProps = new javax.swing.JComboBox<>();
        cmbOrdeDate = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        pnlMnGrfComp1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cmbChrLbDegVen = new javax.swing.JComboBox<>();
        ckGridVenta = new javax.swing.JCheckBox();
        ckLineVent = new javax.swing.JCheckBox();
        ckPointVent = new javax.swing.JCheckBox();
        ckLbEjxVent = new javax.swing.JCheckBox();
        ckLLineVenta = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        pnlGfVenta = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jdtInicio = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jdtFin = new com.toedter.calendar.JDateChooser();
        btnBuscar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout(5, 0));

        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 5, 5));

        jPanel6.setPreferredSize(new java.awt.Dimension(923, 350));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel12.setPreferredSize(new java.awt.Dimension(509, 110));
        jPanel12.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        lbInfoCompra.setEditable(false);
        lbInfoCompra.setColumns(20);
        lbInfoCompra.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbInfoCompra.setRows(5);
        lbInfoCompra.setAutoscrolls(false);
        lbInfoCompra.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Compra", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jScrollPane4.setViewportView(lbInfoCompra);

        jPanel12.add(jScrollPane4);

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        lbVentaInfo.setEditable(false);
        lbVentaInfo.setColumns(20);
        lbVentaInfo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbVentaInfo.setRows(5);
        lbVentaInfo.setAutoscrolls(false);
        lbVentaInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jScrollPane5.setViewportView(lbVentaInfo);

        jPanel12.add(jScrollPane5);

        jScrollPane7.setViewportView(jPanel12);

        jPanel9.add(jScrollPane7, java.awt.BorderLayout.PAGE_START);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Formulario Tipo Cambio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(350, 170));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Compra ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Ingresa la fecha");

        dtTpC.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtTpCPropertyChange(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Venta");

        btnGuardTC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save-25.png"))); // NOI18N
        btnGuardTC.setToolTipText("Guardar cambios");
        btnGuardTC.setPreferredSize(new java.awt.Dimension(31, 30));
        btnGuardTC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardTCActionPerformed(evt);
            }
        });

        btnDeleteTC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_icon_25x25.png"))); // NOI18N
        btnDeleteTC.setToolTipText("Eliminar Tipo de Cambio");
        btnDeleteTC.setPreferredSize(new java.awt.Dimension(31, 30));
        btnDeleteTC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTCActionPerformed(evt);
            }
        });

        btnBuscarTC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBuscarTC.setToolTipText("Buscar");
        btnBuscarTC.setPreferredSize(new java.awt.Dimension(30, 30));
        btnBuscarTC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarTCActionPerformed(evt);
            }
        });

        lbForTCError.setBackground(new java.awt.Color(255, 255, 255));
        lbForTCError.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbForTCError.setForeground(new java.awt.Color(255, 0, 51));
        lbForTCError.setOpaque(true);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbForTCError))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(btnGuardTC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDeleteTC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtVenta)
                                    .addComponent(dtTpC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCompra, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarTC, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 25, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(dtTpC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarTC, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardTC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteTC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbForTCError)
                .addGap(65, 65, 65))
        );

        jPanel9.add(jPanel11, java.awt.BorderLayout.WEST);

        jPanel6.add(jPanel9);

        pnlCompra.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tipo Compra", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        pnlCompra.setLayout(new java.awt.BorderLayout());

        jPanel13.setPreferredSize(new java.awt.Dimension(554, 20));
        jPanel13.setLayout(new java.awt.BorderLayout(3, 0));

        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0);
        flowLayout2.setAlignOnBaseline(true);
        pnlMnGrfComp.setLayout(flowLayout2);

        jLabel7.setText("Grados");
        pnlMnGrfComp.add(jLabel7);

        cmbChrLbDeg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "45", "90" }));
        cmbChrLbDeg.setPreferredSize(new java.awt.Dimension(72, 20));
        cmbChrLbDeg.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbChrLbDegItemStateChanged(evt);
            }
        });
        pnlMnGrfComp.add(cmbChrLbDeg);

        ckGridComp.setSelected(true);
        ckGridComp.setText("Grid");
        ckGridComp.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckGridComp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckGridCompItemStateChanged(evt);
            }
        });
        pnlMnGrfComp.add(ckGridComp);

        ckLineComp.setSelected(true);
        ckLineComp.setText("Líneas");
        ckLineComp.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckLineComp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckLineCompItemStateChanged(evt);
            }
        });
        pnlMnGrfComp.add(ckLineComp);

        ckPuntosComp.setText("Puntos");
        ckPuntosComp.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckPuntosComp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckPuntosCompItemStateChanged(evt);
            }
        });
        pnlMnGrfComp.add(ckPuntosComp);

        ckLbEjx.setSelected(true);
        ckLbEjx.setText("Fechas Eje x");
        ckLbEjx.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckLbEjx.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckLbEjxItemStateChanged(evt);
            }
        });
        pnlMnGrfComp.add(ckLbEjx);

        ckLLineComp.setText("Linea media");
        ckLLineComp.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckLLineComp.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckLLineCompItemStateChanged(evt);
            }
        });
        pnlMnGrfComp.add(ckLLineComp);

        jPanel13.add(pnlMnGrfComp, java.awt.BorderLayout.CENTER);

        pnlCompra.add(jPanel13, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout pnlGrfLayout = new javax.swing.GroupLayout(pnlGrf);
        pnlGrf.setLayout(pnlGrfLayout);
        pnlGrfLayout.setHorizontalGroup(
            pnlGrfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1019, Short.MAX_VALUE)
        );
        pnlGrfLayout.setVerticalGroup(
            pnlGrfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(pnlGrf);

        pnlCompra.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel6.add(pnlCompra);

        jPanel1.add(jPanel6);

        jPanel3.setAutoscrolls(true);
        jPanel3.setPreferredSize(new java.awt.Dimension(1132, 350));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla Historico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel10.setLayout(new java.awt.BorderLayout());

        tbTipoCambio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "Compra", "Venta", "Diferencia"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbTipoCambio.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbTipoCambio);

        jPanel10.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel17.setPreferredSize(new java.awt.Dimension(811, 35));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        jPanel17.setLayout(flowLayout1);

        jLabel6.setText("Ordenar");
        jPanel17.add(jLabel6);
        jLabel6.getAccessibleContext().setAccessibleName("");

        cmbTCProps.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fecha", "Compra", "Venta", "Diferencia" }));
        cmbTCProps.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTCPropsItemStateChanged(evt);
            }
        });
        jPanel17.add(cmbTCProps);

        cmbOrdeDate.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ascendemte", "Descendente" }));
        cmbOrdeDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbOrdeDateItemStateChanged(evt);
            }
        });
        cmbOrdeDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbOrdeDatePropertyChange(evt);
            }
        });
        jPanel17.add(cmbOrdeDate);

        jPanel10.add(jPanel17, java.awt.BorderLayout.PAGE_START);

        jPanel7.setPreferredSize(new java.awt.Dimension(250, 217));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 207, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel7, java.awt.BorderLayout.EAST);

        jPanel14.setPreferredSize(new java.awt.Dimension(518, 10));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel14, java.awt.BorderLayout.PAGE_END);

        jPanel3.add(jPanel10);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tipo Venta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel16.setAutoscrolls(true);
        jPanel16.setPreferredSize(new java.awt.Dimension(498, 350));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jPanel18.setPreferredSize(new java.awt.Dimension(486, 25));
        jPanel18.setLayout(new java.awt.BorderLayout());

        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0);
        flowLayout3.setAlignOnBaseline(true);
        pnlMnGrfComp1.setLayout(flowLayout3);

        jLabel8.setText("Grados");
        pnlMnGrfComp1.add(jLabel8);

        cmbChrLbDegVen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "45", "90" }));
        cmbChrLbDegVen.setPreferredSize(new java.awt.Dimension(72, 20));
        cmbChrLbDegVen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbChrLbDegVenItemStateChanged(evt);
            }
        });
        pnlMnGrfComp1.add(cmbChrLbDegVen);

        ckGridVenta.setSelected(true);
        ckGridVenta.setText("Grid");
        ckGridVenta.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckGridVenta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckGridVentaItemStateChanged(evt);
            }
        });
        pnlMnGrfComp1.add(ckGridVenta);

        ckLineVent.setSelected(true);
        ckLineVent.setText("Líneas");
        ckLineVent.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckLineVent.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckLineVentItemStateChanged(evt);
            }
        });
        pnlMnGrfComp1.add(ckLineVent);

        ckPointVent.setText("Puntos");
        ckPointVent.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckPointVent.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckPointVentItemStateChanged(evt);
            }
        });
        pnlMnGrfComp1.add(ckPointVent);

        ckLbEjxVent.setSelected(true);
        ckLbEjxVent.setText("Fechas Eje x");
        ckLbEjxVent.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckLbEjxVent.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckLbEjxVentItemStateChanged(evt);
            }
        });
        pnlMnGrfComp1.add(ckLbEjxVent);

        ckLLineVenta.setText("Linea media");
        ckLLineVenta.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        ckLLineVenta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckLLineVentaItemStateChanged(evt);
            }
        });
        pnlMnGrfComp1.add(ckLLineVenta);

        jPanel18.add(pnlMnGrfComp1, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel18, java.awt.BorderLayout.PAGE_START);

        pnlGfVenta.setAutoscrolls(true);

        javax.swing.GroupLayout pnlGfVentaLayout = new javax.swing.GroupLayout(pnlGfVenta);
        pnlGfVenta.setLayout(pnlGfVentaLayout);
        pnlGfVentaLayout.setHorizontalGroup(
            pnlGfVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 775, Short.MAX_VALUE)
        );
        pnlGfVentaLayout.setVerticalGroup(
            pnlGfVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 271, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(pnlGfVenta);

        jPanel16.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel16);

        jPanel1.add(jPanel3);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setMinimumSize(new java.awt.Dimension(0, 20));
        jPanel2.setPreferredSize(new java.awt.Dimension(983, 30));
        java.awt.FlowLayout flowLayout5 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout5.setAlignOnBaseline(true);
        jPanel2.setLayout(flowLayout5);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Desde");
        jPanel2.add(jLabel4);

        jdtInicio.setDateFormatString("dd-MM-yyyy");
        jdtInicio.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel2.add(jdtInicio);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("hasta");
        jPanel2.add(jLabel5);

        jdtFin.setDateFormatString("dd-MM-yyyy");
        jdtFin.setPreferredSize(new java.awt.Dimension(120, 22));
        jdtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdtFinPropertyChange(evt);
            }
        });
        jPanel2.add(jdtFin);

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscar);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel5.setPreferredSize(new java.awt.Dimension(983, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1082, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.PAGE_END);

        jPanel8.setPreferredSize(new java.awt.Dimension(30, 440));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
        );

        add(jPanel8, java.awt.BorderLayout.LINE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 440));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardTCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardTCActionPerformed
        // TODO add your handling code here:

        try {
            double compra = Double.parseDouble(txtCompra.getText());
            double venta = Double.parseDouble(txtVenta.getText());
            this.tc = new TipoCambio();
            tc.setFecha(dtTpC.getDate());
            tc.setCompra(compra);
            tc.setVenta(venta);
            data.CRUDTipoCambio crud = new data.CRUDTipoCambio();
            boolean res = crud.addUpdateTipoCambio(tc);
            if (res) {
                JOptionPane.showMessageDialog(null, "Se han guardado los cambios para la fecha " + tc.getFecha());
                this.listaTipoCambio.add(tc);
                afterChangesTipo();
            } else {
                //JOptionPane.showMessageDialog(null, "Ha ocurrido un error agregando tipo de cambio para " + tc.getFecha());
                view.util.CustomMessages.showTemporalLabelMessage(this.lbForTCError, 3000, "Ha ocurrido un error agregando tipo de cambio para " + tc.getFecha());

            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Por favor asegurese de que la información es correcta");
            view.util.CustomMessages.showTemporalLabelMessage(this.lbForTCError, 3000, "Por favor asegurese de que la información es correcta");
            System.out.println("view.MantenimientoFacturaElectronica.btnGuardTCActionPerformed() error " + e.getMessage());
        }

    }//GEN-LAST:event_btnGuardTCActionPerformed
    private void afterChangesTipo() {
        obtenerListaTipoCambio();
        getLineCharts();
    }
    private void btnDeleteTCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTCActionPerformed
        // TODO add your handling code here:
        if (this.tc != null) {
            try {

                data.CRUDTipoCambio crud = new data.CRUDTipoCambio();
                boolean res = crud.eliminarTipoCambio(tc);
                if (res) {
                    afterChangesTipo();
                    this.txtCompra.setText("");
                    this.txtVenta.setText("");
                    JOptionPane.showMessageDialog(null, "Se ha eliminado tipo de cambio para " + tc.getFecha());
                    this.tc = null;

                } else {
                    //JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar tipo de cambio para " + tc.getFecha());
                    view.util.CustomMessages
                            .showTemporalLabelMessage(this.lbForTCError, 3000, "Ha ocurrido un error al eliminar tipo de cambio para " + tc.getFecha());

                }
            } catch (Exception e) {
                logic.AppLogger.appLogger.warning("view.MantenimientoTipoCambio.btnDeleteTCActionPerformed() error " + e.getMessage());
                view.util.CustomMessages
                        .showTemporalLabelMessage(this.lbForTCError, 3000, "view.MantenimientoTipoCambio.btnDeleteTCActionPerformed() error " + e.getMessage());

                System.out.println("view.MantenimientoFacturaElectronica.btnDeleteTCActionPerformed() error " + e.getMessage());
            }
        } else {
            view.util.CustomMessages
                    .showTemporalLabelMessage(this.lbForTCError, 3000, "El registro no existe");
        }
    }//GEN-LAST:event_btnDeleteTCActionPerformed

    private void dtTpCPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtTpCPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().contains("date")) {
            this.txtCompra.setText("");
            this.txtVenta.setText("");
            obtenerTipoCambio();
        }
    }//GEN-LAST:event_dtTpCPropertyChange

    private void btnBuscarTCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTCActionPerformed
        // TODO add your handling code here:
        this.txtCompra.setText("");
        this.txtVenta.setText("");
        obtenerTipoCambio();
    }//GEN-LAST:event_btnBuscarTCActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        this.loadingInfo = true;
        setGraphsDefaultValues();
        obtenerListaTipoCambio();
        getLineCharts();
        this.loadingInfo = false;
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void jdtFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdtFinPropertyChange
        // TODO add your handling code here:

        if (evt.getPropertyName().contains("date")) {
            this.loadingInfo = true;
            this.loadingInfo = true;
            setGraphsDefaultValues();
            obtenerListaTipoCambio();
            getLineCharts();
            this.loadingInfo = false;
        }

    }//GEN-LAST:event_jdtFinPropertyChange

    private void cmbOrdeDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbOrdeDatePropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbOrdeDatePropertyChange

    private void cmbOrdeDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbOrdeDateItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            handleOrdering();
        }
    }//GEN-LAST:event_cmbOrdeDateItemStateChanged

    private void cmbTCPropsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTCPropsItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            handleOrdering();
        }
    }//GEN-LAST:event_cmbTCPropsItemStateChanged

    private void cmbChrLbDegItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbChrLbDegItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            int degrees = Integer.parseInt(cmbChrLbDeg.getSelectedItem().toString());
            this.graficoCompra.setCategoriPlotUpPosition(degrees);
        }
    }//GEN-LAST:event_cmbChrLbDegItemStateChanged

    private void ckLbEjxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckLbEjxItemStateChanged
        // TODO add your handling code here:

        if (!loadingInfo) {
            this.graficoCompra.hideShowLbsAxisX(ckLbEjx.isSelected());
        }
    }//GEN-LAST:event_ckLbEjxItemStateChanged

    private void ckLLineCompItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckLLineCompItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoCompra.showMediaLineAxisX(ckLLineComp.isSelected());
        }
    }//GEN-LAST:event_ckLLineCompItemStateChanged

    private void cmbChrLbDegVenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbChrLbDegVenItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            int degrees = Integer.parseInt(cmbChrLbDegVen.getSelectedItem().toString());
            this.graficoVenta.setCategoriPlotUpPosition(degrees);
        }
    }//GEN-LAST:event_cmbChrLbDegVenItemStateChanged

    private void ckLbEjxVentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckLbEjxVentItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoVenta.hideShowLbsAxisX(ckLbEjxVent.isSelected());
        }
    }//GEN-LAST:event_ckLbEjxVentItemStateChanged

    private void ckLLineVentaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckLLineVentaItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoVenta.showMediaLineAxisX(ckLLineVenta.isSelected());
        }
    }//GEN-LAST:event_ckLLineVentaItemStateChanged

    private void ckPuntosCompItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckPuntosCompItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoCompra.showPoints(ckPuntosComp.isSelected());
        }


    }//GEN-LAST:event_ckPuntosCompItemStateChanged

    private void ckPointVentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckPointVentItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoVenta.showPoints(ckPointVent.isSelected());
        }

    }//GEN-LAST:event_ckPointVentItemStateChanged

    private void ckGridCompItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckGridCompItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoCompra.showGridLines(ckGridComp.isSelected());
        }
    }//GEN-LAST:event_ckGridCompItemStateChanged

    private void ckGridVentaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckGridVentaItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoVenta.showGridLines(ckGridVenta.isSelected());
        }
    }//GEN-LAST:event_ckGridVentaItemStateChanged

    private void ckLineCompItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckLineCompItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoCompra.showLines(ckLineComp.isSelected());
        }
    }//GEN-LAST:event_ckLineCompItemStateChanged

    private void ckLineVentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckLineVentItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            this.graficoVenta.showLines(ckLineVent.isSelected());
        }
    }//GEN-LAST:event_ckLineVentItemStateChanged
    private void handleOrdering() {
        JTableCommonFunctions.limpiarTabla(tbTipoCambio);
        DefaultTableModel model = (DefaultTableModel) this.tbTipoCambio.getModel();
        int selectedProperty = this.cmbTCProps.getSelectedIndex();

        switch (selectedProperty) {
            case 0:
                //fecha
                orderByDate();
                break;
            case 1:
                orderByCompra();
                break;
            case 2:
                orderByVenta();
                break;
            case 3:
                orderByDif();
                break;

        }
        this.listaTipoCambio.forEach(e -> {
            addRowTipoCambio(model, e);
        });
    }

    private void orderByDate() {
        int selectedOrderBy = this.cmbOrdeDate.getSelectedIndex();
        if (selectedOrderBy == 0) {
            Collections.sort(this.listaTipoCambio, Comparator.comparing(TipoCambio::getFecha));
        } else {
            Collections.sort(this.listaTipoCambio, Comparator.comparing(TipoCambio::getFecha).reversed());
        }

    }

    private void orderByCompra() {
        int selectedOrderBy = this.cmbOrdeDate.getSelectedIndex();
        if (selectedOrderBy == 0) {
            Collections.sort(this.listaTipoCambio, Comparator.comparing(TipoCambio::getCompra));
        } else {
            Collections.sort(this.listaTipoCambio, Comparator.comparing(TipoCambio::getCompra).reversed());
        }

    }

    private void orderByVenta() {
        int selectedOrderBy = this.cmbOrdeDate.getSelectedIndex();
        if (selectedOrderBy == 0) {
            Collections.sort(this.listaTipoCambio, Comparator.comparing(TipoCambio::getVenta));
        } else {
            Collections.sort(this.listaTipoCambio, Comparator.comparing(TipoCambio::getVenta).reversed());
        }

    }

    private void orderByDif() {
        int selectedOrderBy = this.cmbOrdeDate.getSelectedIndex();
        if (selectedOrderBy == 0) {
            Collections.sort(this.listaTipoCambio, Comparator.comparing(TipoCambio::getDiferencia));
        } else {
            Collections.sort(this.listaTipoCambio, Comparator.comparing(TipoCambio::getDiferencia).reversed());
        }

    }

    private void obtenerListaTipoCambio() {
        try {

            JTableCommonFunctions.limpiarTabla(tbTipoCambio);
            java.util.Date inicio = jdtInicio.getDate();
            java.util.Date fin = jdtFin.getDate();
            this.listaTipoCambio = crtc.getTipoCambioPorFechas(inicio, fin);
            handleOrdering();
        } catch (Exception e) {
            System.out.println("view.MantenimientoTipoCambio.obtenerListaTipoCambio() error " + e.getMessage());
        }
    }

    private void obtenerTipoCambio() {
        java.util.Date inicio = this.dtTpC.getDate();
        if (inicio != null) {
            data.CRUDTipoCambio crudtp = new data.CRUDTipoCambio();
            this.tc = crudtp.obtenerTipoCambioPorFecha(inicio);
            if (this.tc != null) {
                this.txtCompra.setText(this.tc.getCompra() + "");
                this.txtVenta.setText(this.tc.getVenta() + "");
            } else {
                view.util.CustomMessages.showTemporalLabelMessage(this.lbForTCError, 3000, "Registro no encontrado");

                //JOptionPane.showMessageDialog(null, "Registro no encontrado");
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarTC;
    private javax.swing.JButton btnDeleteTC;
    private javax.swing.JButton btnGuardTC;
    private javax.swing.JCheckBox ckGridComp;
    private javax.swing.JCheckBox ckGridVenta;
    private javax.swing.JCheckBox ckLLineComp;
    private javax.swing.JCheckBox ckLLineVenta;
    private javax.swing.JCheckBox ckLbEjx;
    private javax.swing.JCheckBox ckLbEjxVent;
    private javax.swing.JCheckBox ckLineComp;
    private javax.swing.JCheckBox ckLineVent;
    private javax.swing.JCheckBox ckPointVent;
    private javax.swing.JCheckBox ckPuntosComp;
    private javax.swing.JComboBox<String> cmbChrLbDeg;
    private javax.swing.JComboBox<String> cmbChrLbDegVen;
    private javax.swing.JComboBox<String> cmbOrdeDate;
    private javax.swing.JComboBox<String> cmbTCProps;
    private com.toedter.calendar.JDateChooser dtTpC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private com.toedter.calendar.JDateChooser jdtFin;
    private com.toedter.calendar.JDateChooser jdtInicio;
    private javax.swing.JLabel lbForTCError;
    private javax.swing.JTextArea lbInfoCompra;
    private javax.swing.JTextArea lbVentaInfo;
    private javax.swing.JPanel pnlCompra;
    private javax.swing.JPanel pnlGfVenta;
    private javax.swing.JPanel pnlGrf;
    private javax.swing.JPanel pnlMnGrfComp;
    private javax.swing.JPanel pnlMnGrfComp1;
    private javax.swing.JTable tbTipoCambio;
    private javax.swing.JTextField txtCompra;
    private javax.swing.JTextField txtVenta;
    // End of variables declaration//GEN-END:variables

}

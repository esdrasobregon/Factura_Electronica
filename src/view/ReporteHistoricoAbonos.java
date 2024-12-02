/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import entitys.AbonoSugerido;
import entitys.AbonoSugeridoContado;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import services.SimpleExcelWriter;
import view.util.JTableCommonFunctions;

/**
 *
 * @author eobregon
 */
public class ReporteHistoricoAbonos extends javax.swing.JPanel {

    /**
     * Creates new form ReporteHistoricoAbonos
     */
    boolean loadingInfo = false;
    data.CRUDAbonoSugeridoContado crud = new data.CRUDAbonoSugeridoContado();
    ArrayList<entitys.AbonoSugeridoContado> listaAbonosContado = new ArrayList<entitys.AbonoSugeridoContado>();
    private data.CrudAbonoSugerido crda;
    private ArrayList<AbonoSugerido> listaAbonoSug;

    public ReporteHistoricoAbonos() {
        initComponents();
        this.crda = new data.CrudAbonoSugerido();
        this.listaAbonoSug = new ArrayList<>();
        prepareGUI();
    }

    private void prepareGUI() {
        setView();
        setListeners();
    }

    private void setView() {
        //this.jPanel19.setVisible(false);
        //this.jPanel19.setEnabled(false);
        javax.swing.table.DefaultTableCellRenderer rightRenderer = new javax.swing.table.DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        this.tbReporte.getColumnModel().getColumn(12).setCellRenderer(rightRenderer);
        this.tbReporte.getColumnModel().getColumn(13).setCellRenderer(rightRenderer);
        this.tbReporte.getColumnModel().getColumn(14).setCellRenderer(rightRenderer);
        this.tbReporte.getColumnModel().getColumn(15).setCellRenderer(rightRenderer);
        this.tbReporte.getColumnModel().getColumn(16).setCellRenderer(rightRenderer);
        this.jProgressBar1.setVisible(false);
        setDateChooserLook();
    }

    private void setTbReporteRowShorter(int column) {
        tbReporte.getColumnModel().getColumn(column).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row) {
                if (value instanceof java.util.Date) {
                    value = logic.AppStaticValues.dateFormat.format((java.util.Date) value);
                }
                return super.getTableCellRendererComponent(tbReporte, value, isSelected, hasFocus, row, column);
            }
        });
    }

    private void setDateChooserLook() {
        IDateEditor dateEditor = dtInicio.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
        dateEditor = dtFin.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
    }

    private void setListeners() {

        tbReporte.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !loadingInfo) {
                    if (cmbContCred.getSelectedIndex() == 0) {
                        obtResumenCred();
                    } else {
                        obtResumenCCont();
                    }
                }
            }
        });

    }

    private void obtResumenCred() {
        lbTbinfo.setText("Filas: " + tbReporte.getRowCount() + "<<>> Total filas seleccionadas: " + tbReporte.getSelectedRows().length);

        if (tbReporte.getSelectedRows().length == 1) {
            System.out.println("Selected Row: " + tbReporte.getSelectedRow());
            showAsyncAbInffo();
        } else if (tbReporte.getSelectedRows().length > 1) {
            String filasSelec = "\t<<>> Filas seleccionadas: ";
            int[] listaIndices = tbReporte.getSelectedRows();
            ArrayList<AbonoSugerido> listaa = new ArrayList<AbonoSugerido>();
            for (int i = 0; i < listaIndices.length; i++) {
                filasSelec += (listaIndices[i] + 1) + ", ";
                int index = Integer.parseInt(tbReporte.getValueAt(listaIndices[i], 0).toString());
                listaa.add(AbonoSugerido.obtenerAbonoPorId(index, listaAbonoSug));
            }
            double mtoD = 0;
            double mtoPP = 0;
            double abono = 0;
            double mtoCol = 0;
            double mtSdoP = 0;
            double mtoDd = 0;
            double mtoPPd = 0;
            double abonod = 0;
            double mtoCold = 0;
            double mtSdoPd = 0;
            String resultd = "";
            for (AbonoSugerido ab : listaa) {
                if (ab.getMoneda().equals("CRC")) {
                    mtoD += ab.getMonto_Original();
                    mtoPP += ab.getSaldo_Restante();
                    abono += ab.getAbono();
                    mtoCol += ab.getMonto_Colones();
                    mtSdoP += ab.getSaldo_Restante();
                } else {
                    mtoDd += ab.getMonto_Original();
                    mtoPPd += ab.getSaldo_Restante();
                    abonod += ab.getAbono();
                    mtoCold += ab.getMonto_Colones();
                    mtSdoPd += ab.getSaldo_Restante();
                }
            }
            resultd = "Suma Monto documento: CRC " + logic.AppStaticValues.numberFormater.format(mtoD)
                    + "\tPendiente pago: CRC " + logic.AppStaticValues.numberFormater.format(mtoPP)
                    + "\tAbonos: CRC" + logic.AppStaticValues.numberFormater.format(abono) + "\n"
                    + "Suma Monto documento: USD " + logic.AppStaticValues.numberFormater.format(mtoDd)
                    + "\tPendiente pago: USD " + logic.AppStaticValues.numberFormater.format(mtoPPd)
                    + "\tAbonos: USD " + logic.AppStaticValues.numberFormater.format(abonod)
                    + "\tMonto abono colones: CRC " + logic.AppStaticValues.numberFormater.format(mtoCold)
                    + "\n---------------------Nota: esto es un historico, en los cálculos pueden incluirse documentos repetidos---------------------";
            txaBitAb.setText(resultd);
            lbTbinfo.setText(lbTbinfo.getText() + filasSelec);
        } else {
            txaBitAb.setText("");
        }
    }

    private void obtResumenCCont() {
        lbTbinfo.setText("Filas: " + tbReporte.getRowCount() + "<<>> Total filas seleccionadas: " + tbReporte.getSelectedRows().length);

        if (tbReporte.getSelectedRows().length == 1) {
            System.out.println("Selected Row: " + tbReporte.getSelectedRow());
            showAsyncAbInffo();
        } else if (tbReporte.getSelectedRows().length > 1) {
            String filasSelec = "\t<<>> Filas seleccionadas: ";
            int[] listaIndices = tbReporte.getSelectedRows();
            ArrayList<AbonoSugeridoContado> listaa = new ArrayList<>();
            for (int i = 0; i < listaIndices.length; i++) {
                filasSelec += (listaIndices[i] + 1) + ", ";
                int index = Integer.parseInt(view.util.JTableCommonFunctions.getCellValueByHeader(tbReporte, "ID", listaIndices[i]).toString());
                listaa.add(AbonoSugeridoContado.obtAbonoSugeridoContadoPorId(index, listaAbonosContado));
            }
            double mtoD = 0;
            double mtoPP = 0;
            double abono = 0;
            double mtoCol = 0;
            double mtSdoP = 0;
            double mtoDd = 0;
            double mtoPPd = 0;
            double abonod = 0;
            double mtoCold = 0;
            double mtSdoPd = 0;
            String resultd = "";
            for (AbonoSugeridoContado ab : listaa) {
                if (ab.getMoneda().equals("CRC")) {
                    mtoD += ab.getMontoOriginal();
                    mtoPP += ab.getSaldoActual();
                    abono += ab.getAbono();
                    mtoCol += ab.getMontoOriginalColones();
                    mtSdoP += ab.getSaldoActual();
                } else {
                    mtoDd += ab.getMontoOriginal();
                    mtoPPd += ab.getSaldoActual();
                    abonod += ab.getAbono();
                    mtoCold += ab.getMontoOriginalColones();
                    mtSdoPd += ab.getSaldoActual();
                }
            }
            resultd = "Suma Monto documento: CRC " + logic.AppStaticValues.numberFormater.format(mtoD)
                    + "\tPendiente pago: CRC " + logic.AppStaticValues.numberFormater.format(mtoPP)
                    + "\tAbonos: CRC" + logic.AppStaticValues.numberFormater.format(abono) + "\n"
                    + "Suma Monto documento: USD " + logic.AppStaticValues.numberFormater.format(mtoDd)
                    + "\tPendiente pago: USD " + logic.AppStaticValues.numberFormater.format(mtoPPd)
                    + "\tAbonos: USD " + logic.AppStaticValues.numberFormater.format(abonod)
                    + "\tMonto abono colones: CRC " + logic.AppStaticValues.numberFormater.format(mtoCold)
                    + "\n---------------------Nota: esto es un historico, en los cálculos pueden incluirse documentos repetidos---------------------";
            txaBitAb.setText(resultd);
            lbTbinfo.setText(lbTbinfo.getText() + filasSelec);
        } else {
            txaBitAb.setText("");
        }
    }

    private void showAsyncAbInffo() {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                int index = (int) tbReporte.getValueAt(tbReporte.getSelectedRow(), 0);
                ArrayList<AbonoSugerido> lista = crda.getHistoricoAbonoSugerido(index);
                String values = "";
                for (AbonoSugerido ab : lista) {
                    //values += ab.getNombre_Proveedor() + "\t";
                    //values += "Documento " + ab.getDocumento() + "\t";
                    //values += ab.getMoneda() + "\t";
                    values += "Creado " + logic.AppStaticValues.dateFormat.format(ab.getFecha_Creacion()) + "\t";
                    values += "Fecha del cambio " + logic.AppStaticValues.dateFormat.format(ab.getFecha_cambio()) + "\t";
                    values += "Monto original " + ab.getMoneda() + " " + logic.AppStaticValues.numberFormater.format(ab.getMonto_Original()) + "\t";
                    values += "Abono " + ab.getMoneda() + " " + logic.AppStaticValues.numberFormater.format(ab.getAbono()) + "\t";
                    values += "Saldo " + ab.getMoneda() + " " + logic.AppStaticValues.numberFormater.format(ab.getSaldo_Restante()) + "\t";
                    values += "Aprobado " + (ab.getAprobado() == 1 ? "Si" : "No") + "\t";
                    values += "Revisado conta " + (ab.getRevisadoConta() == 1 ? "Si" : "No") + "\t";
                    values += "Responsable " + ab.getUsuarioRevision() + "\n";

                }
                txaBitAb.setText(values);
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
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbOrderBy = new javax.swing.JComboBox<>();
        cmbAsCDesc = new javax.swing.JComboBox<>();
        btnExcel = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel17 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbReporte = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        lbTbinfo = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaBitAb = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel14 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbCia = new javax.swing.JComboBox<>();
        jPanel20 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cmbContCred = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbEstadoConta = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        dtInicio = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        dtFin = new com.toedter.calendar.JDateChooser();
        btnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Historico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(833, 35));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setText("Ordenar por");
        jPanel19.add(jLabel1);

        cmbOrderBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cia", "F. Solicitud", "F. Pago Conta", "F. Documento", "Documento", "Nombre Proveedor", "Cta Presupuesto", "Descripción Cta", "Moneda", "Monto Documento", "Monto pendiente pago", "Monto Abono", "Saldo restante" }));
        cmbOrderBy.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbOrderByItemStateChanged(evt);
            }
        });
        jPanel19.add(cmbOrderBy);

        cmbAsCDesc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "↑ Ascendente", "↓ Descendente" }));
        cmbAsCDesc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAsCDescItemStateChanged(evt);
            }
        });
        jPanel19.add(cmbAsCDesc);

        jPanel9.add(jPanel19);

        jPanel7.add(jPanel9);

        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExcel.setToolTipText("Exportar a excel");
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });
        jPanel7.add(btnExcel);

        jPanel1.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(7);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        tbReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "CIA", "F. Solicitud", "F. Pago Conta", "F. Documento", "Documento", "Numero Proveedor", "Tipo Proveedor", "Nombre Proveedor", "Cta Presupuesto", "Descripcion Cta", "Moneda", "Monto Documento", "Monto pendiente de pago", "Abono", "Monto colones", "Saldo restante", "Usuario", "Semana", "Aprobado", "Revisado conta", "Usuario conta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbReporte.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbReporte.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbReporte);
        if (tbReporte.getColumnModel().getColumnCount() > 0) {
            tbReporte.getColumnModel().getColumn(0).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(0).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(6).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(6).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(6).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(7).setMinWidth(0);
            tbReporte.getColumnModel().getColumn(7).setPreferredWidth(0);
            tbReporte.getColumnModel().getColumn(7).setMaxWidth(0);
            tbReporte.getColumnModel().getColumn(8).setPreferredWidth(250);
            tbReporte.getColumnModel().getColumn(10).setPreferredWidth(300);
        }

        jPanel6.add(jScrollPane1);

        jPanel17.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(833, 25));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbTbinfo.setText("Filas: 0");
        jPanel8.add(lbTbinfo);

        jPanel17.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jSplitPane1.setLeftComponent(jPanel17);

        jPanel18.setLayout(new java.awt.GridLayout(1, 0));

        txaBitAb.setEditable(false);
        txaBitAb.setColumns(20);
        txaBitAb.setRows(5);
        jScrollPane2.setViewportView(txaBitAb);

        jPanel18.add(jScrollPane2);

        jSplitPane1.setRightComponent(jPanel18);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setMinimumSize(new java.awt.Dimension(689, 100));
        jPanel2.setPreferredSize(new java.awt.Dimension(1033, 100));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jProgressBar1.setOpaque(true);
        jProgressBar1.setPreferredSize(new java.awt.Dimension(146, 20));
        jProgressBar1.setStringPainted(true);
        jPanel2.add(jProgressBar1, java.awt.BorderLayout.NORTH);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel5.setText("Cia");
        jPanel11.add(jLabel5);

        cmbCia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "RYMSA", "CILT" }));
        cmbCia.setToolTipText("");
        cmbCia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCiaItemStateChanged(evt);
            }
        });
        jPanel11.add(cmbCia);

        jPanel10.add(jPanel11);

        jLabel8.setText("Abonos");
        jPanel20.add(jLabel8);

        cmbContCred.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Crédito", "Contado" }));
        cmbContCred.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbContCredItemStateChanged(evt);
            }
        });
        jPanel20.add(cmbContCred);

        jPanel10.add(jPanel20);

        jLabel6.setText("Proveedor");
        jPanel12.add(jLabel6);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorItemStateChanged(evt);
            }
        });
        jPanel12.add(cmbProveedor);

        jPanel10.add(jPanel12);

        jLabel7.setText("Moneda");
        jPanel13.add(jLabel7);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "CRC", "USD" }));
        cmbMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaItemStateChanged(evt);
            }
        });
        jPanel13.add(cmbMoneda);

        jPanel10.add(jPanel13);

        jPanel14.add(jPanel10);

        jLabel2.setText("Estado");
        jPanel16.add(jLabel2);

        cmbEstadoConta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Sin revizar", "Revizadas" }));
        cmbEstadoConta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoContaItemStateChanged(evt);
            }
        });
        jPanel16.add(cmbEstadoConta);

        jPanel15.add(jPanel16);

        jLabel3.setText("Inicio");
        jPanel15.add(jLabel3);

        dtInicio.setDateFormatString("dd-MM-yyyy");
        dtInicio.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel15.add(dtInicio);

        jLabel4.setText("hasta");
        jPanel15.add(jLabel4);

        dtFin.setDateFormatString("dd-MM-yyyy");
        dtFin.setPreferredSize(new java.awt.Dimension(120, 22));
        dtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtFinPropertyChange(evt);
            }
        });
        jPanel15.add(dtFin);

        jPanel14.add(jPanel15);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnRefresh.setToolTipText("Cargar información");
        btnRefresh.setPreferredSize(new java.awt.Dimension(27, 27));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel14.add(btnRefresh);

        jPanel2.add(jPanel14, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(1033, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1112, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 554));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 554));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        refreshCreditFilters();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void cmbCiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCiaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {

            refreshCreditFilters();

        }
    }//GEN-LAST:event_cmbCiaItemStateChanged

    private void cmbProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncData(false);
        }
    }//GEN-LAST:event_cmbProveedorItemStateChanged
    private void loadAsyncData(boolean cambiarProveedores) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (dtInicio.getDate() == null || dtFin.getDate() == null) {
                    return;
                }
                loadingInfo = true;
                jProgressBar1.setVisible(true);
                jProgressBar1.setString("Cargando información...");
                txaBitAb.setText("");
                if (cambiarProveedores) {
                    cmbProveedor.removeAllItems();
                    cmbProveedor.addItem("Todos");
                }
                setEnableMenu(false);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                JTableCommonFunctions.limpiarTabla(tbReporte);
                if (cmbContCred.getSelectedIndex() == 0) {
                    prepararAbonos();
                    orderList();
                    loadTbReporte();
                    if (cambiarProveedores) {
                        loadCmbProveedores();
                    }
                } else {
                    prepararAbonosContado();
                    orderContaList();
                    loadTbReporteContado();
                    if (cambiarProveedores) {
                        loadCmbProveedoresContado();
                    }
                }

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                setEnableMenu(true);
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    private void cmbMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncData(false);
        }
    }//GEN-LAST:event_cmbMonedaItemStateChanged

    private void cmbEstadoContaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoContaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncData(false);
        }
    }//GEN-LAST:event_cmbEstadoContaItemStateChanged

    private void dtFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtFinPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().contains("date")) {
            refreshCreditFilters();
            //loadAsyncData();
        }
    }//GEN-LAST:event_dtFinPropertyChange

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                guardarExcel();
            }
        });
        t.start();
    }//GEN-LAST:event_btnExcelActionPerformed

    private void cmbOrderByItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbOrderByItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED && !loadingInfo) {
            loadFilters();
        }
    }//GEN-LAST:event_cmbOrderByItemStateChanged
    private void loadFilters() {
        loadingInfo = true;
        JTableCommonFunctions.limpiarTabla(tbReporte);
        if (cmbContCred.getSelectedIndex() == 0) {
            orderList();

            loadTbReporte();
        } else {
            orderContaList();

            loadTbReporteContado();
        }

        loadingInfo = false;
    }

    private void orderList() {
        int indexCmbOrd = this.cmbOrderBy.getSelectedIndex();
        int indexUpDown = this.cmbAsCDesc.getSelectedIndex();
        switch (indexCmbOrd) {
            case 0:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getCIA));
                } else {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getCIA).reversed());
                }
                break;
            case 1:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getFecha_Creacion));
                } else {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getFecha_Creacion).reversed());
                }
                break;
            case 2:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug, new java.util.Comparator<AbonoSugerido>() {
                        public int compare(AbonoSugerido p1, AbonoSugerido p2) {
                            if (p1.getFecha_Revision_Conta() == null) {
                                return 1;
                            }
                            if (p2.getFecha_Revision_Conta() == null) {
                                return -1;
                            }
                            return p1.getFecha_Revision_Conta().compareTo(p2.getFecha_Revision_Conta());
                        }
                    });
                } else {
                    java.util.Collections.sort(listaAbonoSug, new java.util.Comparator<AbonoSugerido>() {
                        public int compare(AbonoSugerido p1, AbonoSugerido p2) {
                            if (p1.getFecha_Revision_Conta() == null) {
                                return 1;
                            }
                            if (p2.getFecha_Revision_Conta() == null) {
                                return -1;
                            }
                            return p2.getFecha_Revision_Conta().compareTo(p1.getFecha_Revision_Conta());
                        }
                    });
                }
                break;
            case 3:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getFecha_documento));
                } else {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getFecha_documento).reversed());
                }
                break;
            case 4:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getDocumento));
                } else {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getDocumento).reversed());
                }
                break;
            case 5:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getNombre_Proveedor));
                } else {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getNombre_Proveedor).reversed());
                }
                break;
            case 6:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getCuenta_Presupuesto));
                } else {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getCuenta_Presupuesto).reversed());
                }
                break;
            case 7:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getDescripion_Cta_Presupuesto));
                } else {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getDescripion_Cta_Presupuesto).reversed());
                }
                break;
            case 8:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getMoneda));
                } else {
                    java.util.Collections.sort(listaAbonoSug,
                            java.util.Comparator.comparing(AbonoSugerido::getMoneda).reversed());
                }
                break;
            case 9:
                ordMontoMmoneda(indexCmbOrd, indexUpDown);
                break;
            case 10:
                ordMontoMmoneda(indexCmbOrd, indexUpDown);
                break;
            case 11:
                ordMontoMmoneda(indexCmbOrd, indexUpDown);
                break;
            case 12:
                ordMontoMmoneda(indexCmbOrd, indexUpDown);
                break;

        }
    }

    private void orderContaList() {
        int indexCmbOrd = this.cmbOrderBy.getSelectedIndex();
        int indexUpDown = this.cmbAsCDesc.getSelectedIndex();
        switch (indexCmbOrd) {
            case 0:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSociedad));
                } else {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSociedad));
                }
                break;
            case 1:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getFechaSolicitud));
                } else {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getFechaSolicitud).reversed());
                }
                break;
            case 2:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado, new java.util.Comparator<AbonoSugeridoContado>() {
                        public int compare(AbonoSugeridoContado p1, AbonoSugeridoContado p2) {
                            if (p1.getFechaRevisionConta() == null) {
                                return 1;
                            }
                            if (p2.getFechaRevisionConta() == null) {
                                return -1;
                            }
                            return p1.getFechaRevisionConta().compareTo(p2.getFechaRevisionConta());
                        }
                    });
                } else {
                    java.util.Collections.sort(listaAbonosContado, new java.util.Comparator<AbonoSugeridoContado>() {
                        public int compare(AbonoSugeridoContado p1, AbonoSugeridoContado p2) {
                            if (p1.getFechaRevisionConta() == null) {
                                return 1;
                            }
                            if (p2.getFechaRevisionConta() == null) {
                                return -1;
                            }
                            return p2.getFechaRevisionConta().compareTo(p1.getFechaRevisionConta());
                        }
                    });
                }
                break;
            case 3:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getFechaDocumento));
                } else {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getFechaDocumento).reversed());
                }
                break;
            case 4:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getDocumento));
                } else {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getDocumento).reversed());
                }
                break;
            case 5:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getNombreProveedor));
                } else {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getNombreProveedor).reversed());
                }
                break;
            case 6:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getCtPresupuesto));
                } else {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getCtPresupuesto).reversed());
                }
                break;
            case 7:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getDescCtaPres));
                } else {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getDescCtaPres).reversed());
                }
                break;
            case 8:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getMoneda));
                } else {
                    java.util.Collections.sort(listaAbonosContado,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getMoneda).reversed());
                }
                break;
            case 9:
                ordMontoContMmoneda(indexCmbOrd, indexUpDown);
                break;
            case 10:
                ordMontoContMmoneda(indexCmbOrd, indexUpDown);
                break;
            case 11:
                ordMontoContMmoneda(indexCmbOrd, indexUpDown);
                break;
            case 12:
                ordMontoContMmoneda(indexCmbOrd, indexUpDown);
                break;

        }
    }

    private void ordMontoMmoneda(int indexCmbOrd, int indexUpDown) {
        ArrayList<AbonoSugerido> listaCR = new ArrayList<AbonoSugerido>();
        ArrayList<AbonoSugerido> listaUsa = new ArrayList<AbonoSugerido>();
        while (!listaAbonoSug.isEmpty()) {
            AbonoSugerido a = listaAbonoSug.remove(0);
            if (a.getMoneda().equals("USD")) {
                listaUsa.add(a);
            } else {
                listaCR.add(a);
            }
        }
        switch (indexCmbOrd) {
            case 9:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugerido::getMonto_Original));
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugerido::getMonto_Original));
                } else {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugerido::getMonto_Original)
                                    .reversed());
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugerido::getMonto_Original)
                                    .reversed());
                }
                break;
            case 10:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugerido::getSaldo_Actuual));
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugerido::getSaldo_Actuual));
                } else {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugerido::getSaldo_Actuual)
                                    .reversed());
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugerido::getSaldo_Actuual)
                                    .reversed());
                }
                break;

            case 11:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugerido::getAbono));
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugerido::getAbono));
                } else {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugerido::getAbono)
                                    .reversed());
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugerido::getAbono)
                                    .reversed());
                }
                break;
            case 12:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugerido::getSaldo_Restante));
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugerido::getSaldo_Restante));
                } else {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugerido::getSaldo_Restante)
                                    .reversed());
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugerido::getSaldo_Restante)
                                    .reversed());
                }
                break;

        }
        while (!listaCR.isEmpty()) {
            listaAbonoSug.add(listaCR.remove(0));

        }
        while (!listaUsa.isEmpty()) {
            listaAbonoSug.add(listaUsa.remove(0));

        }
    }

    private void ordMontoContMmoneda(int indexCmbOrd, int indexUpDown) {
        ArrayList<AbonoSugeridoContado> listaCR = new ArrayList<>();
        ArrayList<AbonoSugeridoContado> listaUsa = new ArrayList<>();
        while (!listaAbonosContado.isEmpty()) {
            AbonoSugeridoContado a = listaAbonosContado.remove(0);
            if (a.getMoneda().equals("USD")) {
                listaUsa.add(a);
            } else {
                listaCR.add(a);
            }
        }
        switch (indexCmbOrd) {
            case 9:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getMontoOriginal));
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getMontoOriginal));
                } else {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getMontoOriginal)
                                    .reversed());
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getMontoOriginal)
                                    .reversed());
                }
                break;
            case 10:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSaldoActual));
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSaldoActual));
                } else {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSaldoActual)
                                    .reversed());
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSaldoActual)
                                    .reversed());
                }
                break;

            case 11:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getAbono));
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getAbono));
                } else {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getAbono)
                                    .reversed());
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getAbono)
                                    .reversed());
                }
                break;
            case 12:
                if (indexUpDown == 0) {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSaldoActual));
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSaldoActual));
                } else {
                    java.util.Collections.sort(listaCR,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSaldoActual)
                                    .reversed());
                    java.util.Collections.sort(listaUsa,
                            java.util.Comparator.comparing(AbonoSugeridoContado::getSaldoActual)
                                    .reversed());
                }
                break;

        }
        while (!listaCR.isEmpty()) {
            listaAbonosContado.add(listaCR.remove(0));

        }
        while (!listaUsa.isEmpty()) {
            listaAbonosContado.add(listaUsa.remove(0));

        }
    }
    private void cmbAsCDescItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAsCDescItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == evt.SELECTED && !loadingInfo) {
            loadFilters();
        }
    }//GEN-LAST:event_cmbAsCDescItemStateChanged

    private void cmbContCredItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbContCredItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncData(true);
        }
    }//GEN-LAST:event_cmbContCredItemStateChanged
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

    private void refreshCreditFilters() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (dtInicio.getDate() == null || dtFin.getDate() == null) {
                    return;
                }
                loadingInfo = true;
                jProgressBar1.setVisible(true);
                jProgressBar1.setString("Cargando información...");
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                JTableCommonFunctions.limpiarTabla(tbReporte);
                listaAbonoSug = new ArrayList<>();
                listaAbonosContado = new ArrayList<>();
                txaBitAb.setText("");
                cmbProveedor.removeAllItems();
                cmbProveedor.addItem("Todos");
                cmbMoneda.setSelectedIndex(0);
                cmbEstadoConta.setSelectedIndex(0);
                //cmbContCred.setSelectedIndex(0);
                setEnableMenu(false);
                if (cmbContCred.getSelectedIndex() == 0) {
                    prepararAbonos();
                    orderList();
                    loadTbReporte();
                    loadCmbProveedores();
                } else {
                    prepararAbonosContado();
                    orderContaList();
                    loadTbReporteContado();
                    loadCmbProveedoresContado();
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                setEnableMenu(true);
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void setEnableMenu(boolean enable) {
        this.cmbCia.setEnabled(enable);
        this.cmbProveedor.setEnabled(enable);
        this.cmbEstadoConta.setEnabled(enable);
        this.cmbMoneda.setEnabled(enable);
        this.dtInicio.setEnabled(enable);
        this.dtFin.setEnabled(enable);
        this.btnRefresh.setEnabled(enable);
        this.btnExcel.setEnabled(enable);
        this.cmbContCred.setEnabled(enable);
        setDateChooserLook();
    }

    private void loadTbReporte() {
        DefaultTableModel model = (DefaultTableModel) this.tbReporte.getModel();
        for (AbonoSugerido e : listaAbonoSug) {

            model.addRow(new Object[]{
                e.getId(),
                e.getCIA(),
                logic.AppStaticValues.dateBigFormat.format(e.getFecha_Solicitud()),
                e.getFecha_Revision_Conta() == null ? ""
                : logic.AppStaticValues.dateBigFormat.format(e.getFecha_Revision()),
                logic.AppStaticValues.dateFormat.format(e.getFecha_documento()),
                e.getDocumento(),
                e.getNumero_Proveedor(),
                e.getTipo_Proveedor(),
                e.getNombre_Proveedor().toUpperCase(),
                e.getCuenta_Presupuesto(),
                e.getDescripion_Cta_Presupuesto(),
                e.getMoneda(),
                logic.AppStaticValues.numberFormater.format(e.getMonto_Original()),
                logic.AppStaticValues.numberFormater.format(e.getSaldo_Actuual()),
                logic.AppStaticValues.numberFormater.format(e.getAbono()),
                logic.AppStaticValues.numberFormater.format(e.getMonto_Colones()),
                logic.AppStaticValues.numberFormater.format(e.getSaldo_Restante()),
                e.getUsuario(),
                e.getSemana(),
                e.getAprobado() == 1,
                e.getRevisadoConta() == 1,
                e.getUsuarioRevision(),});
        }
        this.lbTbinfo.setText("Filas: " + tbReporte.getRowCount());

    }

    private void loadTbReporteContado() {
        DefaultTableModel model = (DefaultTableModel) this.tbReporte.getModel();
        for (AbonoSugeridoContado e : listaAbonosContado) {

            model.addRow(new Object[]{
                e.getIdAbonoSugeridoContado(),
                e.getSociedad(),
                logic.AppStaticValues.dateBigFormat.format(e.getFechaRevisionConta()),
                e.getFechaRevisionConta() == null ? ""
                : logic.AppStaticValues.dateBigFormat.format(e.getFechaRevisionConta()),
                logic.AppStaticValues.dateFormat.format(e.getFechaDocumento()),
                e.getDocumento(),
                e.getProveedor(),
                "ND",
                e.getNombreProveedor().toUpperCase(),
                e.getCtPresupuesto(),
                e.getDescCtaPres(),
                e.getMoneda(),
                logic.AppStaticValues.numberFormater.format(e.getMontoOriginal()),
                logic.AppStaticValues.numberFormater.format(e.getSaldoActual()),
                logic.AppStaticValues.numberFormater.format(e.getAbono()),
                logic.AppStaticValues.numberFormater.format(e.getMontoOriginalColones()),
                logic.AppStaticValues.numberFormater.format(e.getSaldoActual()),
                e.getUsuario(),
                e.getSemana(),
                e.getAprobado() == 1,
                e.getRevisadoConta() == 1,
                e.getUsuarioRevision(),});
        }
        this.lbTbinfo.setText("Filas: " + tbReporte.getRowCount());

    }

    private void loadCmbProveedores() {
        ArrayList<String> lista = new ArrayList<>();
        this.listaAbonoSug.forEach(e -> {
            String prov = e.getNombre_Proveedor().replaceAll("-", "") + "-" + e.getNumero_Proveedor();
            if (!lista.contains(prov)) {

                lista.add(prov);
            }
        });
        java.util.Collections.sort(lista);
        lista.forEach(e -> {
            this.cmbProveedor.addItem(e);
        });
    }

    private void loadCmbProveedoresContado() {
        ArrayList<String> lista = new ArrayList<>();
        this.listaAbonosContado.forEach(e -> {
            String prov = e.getNombreProveedor().replaceAll("-", "") + "-" + e.getProveedor();
            if (!lista.contains(prov)) {

                lista.add(prov);
            }
        });
        java.util.Collections.sort(lista);
        lista.forEach(e -> {
            this.cmbProveedor.addItem(e);
        });
    }

    private void prepararAbonos() {

        boolean conta = cmbEstadoConta.getSelectedIndex() > 0;
        int estado = cmbEstadoConta.getSelectedIndex() - 1;
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

        this.listaAbonoSug = crda.getAbonoSugeridos(cia, numP, moneda, conta,
                estado, dtInicio.getDate(), dtFin.getDate());

    }

    private void prepararAbonosContado() {

        boolean conta = cmbEstadoConta.getSelectedIndex() > 0;
        int estado = cmbEstadoConta.getSelectedIndex() - 1;
        String cia = this.cmbCia.getSelectedItem().toString();
        if (cia.equals("Todas")) {
            cia = "";
        }
        String prov = this.cmbProveedor.getSelectedItem().toString().isEmpty()
                ? ""
                : this.cmbProveedor.getSelectedItem().toString();
        int numP = prov.equals("Todos")
                ? 0
                : Integer.parseInt(prov.substring(prov.indexOf("-") + 1, prov.length()));
        String moneda = this.cmbMoneda.getSelectedItem().toString().equals("Todas")
                ? ""
                : this.cmbMoneda.getSelectedItem().toString();
        this.listaAbonosContado = this.crud.obtAbonoHistoricoCPContadoConFilt(cia, numP, moneda, estado, dtInicio.getDate(), dtFin.getDate());

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JComboBox<String> cmbAsCDesc;
    private javax.swing.JComboBox<String> cmbCia;
    private javax.swing.JComboBox<String> cmbContCred;
    private javax.swing.JComboBox<String> cmbEstadoConta;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbOrderBy;
    private javax.swing.JComboBox<String> cmbProveedor;
    private com.toedter.calendar.JDateChooser dtFin;
    private com.toedter.calendar.JDateChooser dtInicio;
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
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
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
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbTbinfo;
    private javax.swing.JTable tbReporte;
    private javax.swing.JTextArea txaBitAb;
    // End of variables declaration//GEN-END:variables
}

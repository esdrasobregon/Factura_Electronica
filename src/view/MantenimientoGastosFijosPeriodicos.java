/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import data.DataUser;
import entitys.Departamento;
import entitys.GastosFijosPeriodicos;
import entitys.Presupuesto;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import services.SimpleExcelWriter;
import view.util.CustomMessages;
import view.util.JTableCommonFunctions;

/**
 *
 * @author eobregon
 */
public class MantenimientoGastosFijosPeriodicos extends javax.swing.JPanel {

    /**
     * Creates new form MantenimientoGastosFijosPeriodicos
     */
    ArrayList<Departamento> departamentosPropios;
    data.CrudGastosFijosPeriodicos crg;
    ArrayList<GastosFijosPeriodicos> listaGastos;
    boolean loadingInfo = false;
    int AdministradorGestionGastosPer;
    ArrayList<Presupuesto> listaPresupuesto;
    logic.CastingNumbers cNumbers;

    public MantenimientoGastosFijosPeriodicos() {
        initComponents();
    }

    MantenimientoGastosFijosPeriodicos(ArrayList<Departamento> departamentosPropios, int AdministradorGestionGastosPer) {
        initComponents();
        this.AdministradorGestionGastosPer = AdministradorGestionGastosPer;
        this.departamentosPropios = departamentosPropios;
        this.listaGastos = new ArrayList<>();
        this.crg = new data.CrudGastosFijosPeriodicos();
        this.listaPresupuesto = new ArrayList<>();
        cNumbers = new logic.CastingNumbers();
        prepareGUI();
    }

    private void setView() {
        this.btnSave.setEnabled(false);
        this.jProgressBar1.setVisible(false);
        this.jPanel10.setVisible(false);
        this.jPanel10.setEnabled(false);
        setDtChoosers();
        setTbEventListeners();
        prepareCmbCtas();
    }

    private void setDtChoosers() {
        IDateEditor dateEditor = dtSolicitud.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
        dateEditor = dtInicio.getDateEditor();
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

    private void setTbEventListeners() {
        this.tbRepPago.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (!loadingInfo) {
                    saveChanges();
                }
            }
        });
        this.tbRepPago.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                btnSave.setEnabled(false);
                btnAddRow.setEnabled(true);
                refreshForm();
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tbRepPago.rowAtPoint(e.getPoint());
                    tbRepPago.addRowSelectionInterval(row, row);
                    jPopupMenu1.show(tbRepPago, e.getX(), e.getY());
                }
            }
        });
        this.tbRepPago.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
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

        if (tbRepPago.getSelectedRowCount() > 0) {
            int[] selectedRows = tbRepPago.getSelectedRows();
            double sumaMontosCol = 0.0;
            double sumaApCol = 0.0;
            double sumaMontosDol = 0.0;
            double sumaApDol = 0.0;
            for (int row : selectedRows) {
                int id = (int) tbRepPago.getValueAt(row, 14);
                GastosFijosPeriodicos g = GastosFijosPeriodicos.getGastoById(id, listaGastos);
                if (g != null) {
                    if (g.getMoneda().equals("CRC")) {
                        sumaMontosCol += g.getMonto();
                        if (g.getEstado() == 1) {
                            sumaApCol += g.getMonto();
                        }
                    } else {
                        sumaMontosDol += g.getMonto();
                        if (g.getEstado() == 1) {
                            sumaApDol += g.getMonto();
                        }
                    }
                }
            }
            this.txaResumenes.setText("Suma de montos CRC " + logic.AppStaticValues.numberFormater.format(sumaMontosCol) + ""
                    + "\t Aprobados CRC " + logic.AppStaticValues.numberFormater.format(sumaApCol)
                    + "\nSuma de montos USD " + logic.AppStaticValues.numberFormater.format(sumaMontosDol)
                    + "\t Aprobados USD " + logic.AppStaticValues.numberFormater.format(sumaApDol));
        }

    }

    private void saveChanges() {

        int row = tbRepPago.getSelectedRow();
        if (row > -1) {
            try {

                this.loadingInfo = true;
                boolean aprobado = (boolean) tbRepPago.getValueAt(row, 12);
                boolean realizado = (boolean) tbRepPago.getValueAt(row, 13);
                int idGasto = (int) tbRepPago.getValueAt(row, 14);
                GastosFijosPeriodicos g = crg.obtenerGastosFijoPorId(idGasto);
                if ((g.getEstado() == 1 || aprobado) && this.AdministradorGestionGastosPer == 0) {
                    refreshForm();
                    btnAddRow.setEnabled(true);
                    btnSave.setEnabled(false);
                    loadGastos();
                    JOptionPane.showMessageDialog(null, "Usted no tiene permisos para aprobar o marcar registros como realizados");

                    return;
                }

                g.setEstado(aprobado ? 1 : 0);
                g.setRealizado(realizado);
                g.setObservaciones(tbRepPago.getValueAt(row, 11).toString());
                g.setProveedorActividad(tbRepPago.getValueAt(row, 2).toString());
                g.setMontoF(cNumbers.extractNumericChars(logic.AppStaticValues.numberFormater.format(g.getMonto())));
                boolean diff = g.getEstado() == 1 ? true : false;
                if (aprobado == diff) {
                    g.setUsuarioAutoriza(AdministradorGestionGastosPer == 1 ? DataUser.username : "");
                }
                boolean res = crg.actualizar(g);
                if (res) {

                    JOptionPane.showMessageDialog(null, "Se ha actualizado un registro");
                    loadGastos();
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al actualizar un registro");
                }
                this.loadingInfo = false;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error " + e.getMessage());
                System.err.println("view.MantenimientoGastosFijosPeriodicos.saveChanges() error " + e.getMessage());
            }
        }

    }

    private void prepareGUI() {
        this.loadingInfo = true;
        loadInfo();
        setView();
        this.loadingInfo = false;
    }

    private void loadInfo() {
        if (AdministradorGestionGastosPer == 1) {
            cmbFiltDepartamento.addItem("Todos");
        }
        this.departamentosPropios.forEach(e -> {
            cmbDepartamento.addItem(e.getDEPARTAMENTO() + "-" + e.getDescripcion().trim());
            cmbFiltDepartamento.addItem(e.getDEPARTAMENTO() + "-" + e.getDescripcion().trim());
        });
        String iddepartamento = this.cmbDepartamento.getItemAt(0).substring(0, 2);
        this.listaGastos = crg.obtenerGastosFijosPorIdDepartamento(iddepartamento);
        DefaultTableModel model = (DefaultTableModel) this.tbRepPago.getModel();
        this.listaGastos.forEach(e -> {
            addRowTb(e, model);
        });
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        Date firstDayOfYear = calendar.getTime();
        this.dtInicio.setDate(firstDayOfYear);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        Date lastDayOfYear = calendar.getTime();
        this.dtFin.setDate(lastDayOfYear);
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
        mnEliminar = new javax.swing.JMenuItem();
        mnEdit = new javax.swing.JMenuItem();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbRepPago = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        dtInicio = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        dtFin = new com.toedter.calendar.JDateChooser();
        btnCargarRegistros = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cmbFiltDepartamento = new javax.swing.JComboBox<>();
        jPanel20 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        btnRefrescar = new javax.swing.JButton();
        btnExportExcel = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txaResumenes = new javax.swing.JTextArea();
        jPanel25 = new javax.swing.JPanel();
        lbFilas = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dtSolicitud = new com.toedter.calendar.JDateChooser();
        jPanel14 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbFrecuencia = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtMonto = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtProvAct = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbDepartamento = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cmbCta = new javax.swing.JComboBox<>();
        jPanel23 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtObservaciones = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        btnAddRow = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();

        mnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_icon_25x25.png"))); // NOI18N
        mnEliminar.setText("Eliminar");
        mnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEliminarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnEliminar);

        mnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        mnEdit.setText("Editar registro");
        mnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEditActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnEdit);

        setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );

        add(jPanel7, java.awt.BorderLayout.LINE_START);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla gastos periódicos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        tbRepPago.setAutoCreateRowSorter(true);
        tbRepPago.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha Solicitud", "Fecha Creacion", "Proveedor <--> Actividad", "Moneda", "Monto", "Frecuencia", "Departamento", "Usuario solicitante", "Us Autoriza", "# Departamento", "Cta Presupuestaria", "Observaciones", "Aprobado", "Realizado", "Id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, true, false, false, false, false, false, false, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbRepPago.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbRepPago.setShowGrid(true);
        tbRepPago.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbRepPago);
        tbRepPago.getTableHeader().setPreferredSize(new java.awt.Dimension(jScrollPane1.getWidth(),30));
        tbRepPago.getTableHeader().setBackground(new java.awt.Color(102,102,102));
        tbRepPago.getTableHeader().setForeground(new java.awt.Color(255,255,255));
        tbRepPago.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14) {});
        if (tbRepPago.getColumnModel().getColumnCount() > 0) {
            tbRepPago.getColumnModel().getColumn(0).setPreferredWidth(100);
            tbRepPago.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbRepPago.getColumnModel().getColumn(2).setPreferredWidth(300);
            tbRepPago.getColumnModel().getColumn(3).setPreferredWidth(60);
            tbRepPago.getColumnModel().getColumn(4).setPreferredWidth(120);
            tbRepPago.getColumnModel().getColumn(5).setMinWidth(0);
            tbRepPago.getColumnModel().getColumn(5).setPreferredWidth(0);
            tbRepPago.getColumnModel().getColumn(5).setMaxWidth(0);
            tbRepPago.getColumnModel().getColumn(6).setPreferredWidth(250);
            tbRepPago.getColumnModel().getColumn(7).setPreferredWidth(150);
            tbRepPago.getColumnModel().getColumn(8).setMinWidth(0);
            tbRepPago.getColumnModel().getColumn(8).setPreferredWidth(0);
            tbRepPago.getColumnModel().getColumn(8).setMaxWidth(0);
            tbRepPago.getColumnModel().getColumn(9).setMinWidth(0);
            tbRepPago.getColumnModel().getColumn(9).setPreferredWidth(0);
            tbRepPago.getColumnModel().getColumn(9).setMaxWidth(0);
            tbRepPago.getColumnModel().getColumn(10).setPreferredWidth(200);
            tbRepPago.getColumnModel().getColumn(11).setPreferredWidth(200);
            tbRepPago.getColumnModel().getColumn(12).setPreferredWidth(100);
            tbRepPago.getColumnModel().getColumn(14).setMinWidth(0);
            tbRepPago.getColumnModel().getColumn(14).setPreferredWidth(0);
            tbRepPago.getColumnModel().getColumn(14).setMaxWidth(0);
        }
        tbRepPago.setRowHeight(25);

        jPanel3.add(jScrollPane1);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setPreferredSize(new java.awt.Dimension(940, 37));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout3.setAlignOnBaseline(true);
        jPanel4.setLayout(flowLayout3);

        jPanel19.setMinimumSize(new java.awt.Dimension(270, 25));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Inicio");
        jPanel19.add(jLabel8);

        dtInicio.setDateFormatString("dd-MM-yyyy");
        dtInicio.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel19.add(dtInicio);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Fin");
        jPanel19.add(jLabel9);

        dtFin.setDateFormatString("dd-MM-yyyy");
        dtFin.setPreferredSize(new java.awt.Dimension(120, 25));
        dtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtFinPropertyChange(evt);
            }
        });
        jPanel19.add(dtFin);

        btnCargarRegistros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnCargarRegistros.setToolTipText("Buscar registros");
        btnCargarRegistros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarRegistrosActionPerformed(evt);
            }
        });
        jPanel19.add(btnCargarRegistros);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel11.setToolTipText("Fechas de solicitud");
        jPanel19.add(jLabel11);

        jPanel4.add(jPanel19);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Estado");
        jPanel18.add(jLabel7);

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pendientes", "Aprobados" }));
        cmbEstado.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoItemStateChanged(evt);
            }
        });
        jPanel18.add(cmbEstado);

        jPanel4.add(jPanel18);
        jPanel4.add(jPanel21);

        java.awt.FlowLayout flowLayout6 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout6.setAlignOnBaseline(true);
        jPanel22.setLayout(flowLayout6);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Departamento");
        jPanel22.add(jLabel10);

        cmbFiltDepartamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFiltDepartamentoItemStateChanged(evt);
            }
        });
        jPanel22.add(cmbFiltDepartamento);

        jPanel4.add(jPanel22);

        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox1.setText("Realizados");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jPanel20.add(jCheckBox1);

        btnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnRefrescar.setToolTipText("Refrescar filtros");
        btnRefrescar.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });
        jPanel20.add(btnRefrescar);

        btnExportExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExportExcel.setToolTipText("Exportar a excel");
        btnExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportExcelActionPerformed(evt);
            }
        });
        jPanel20.add(btnExportExcel);

        jPanel4.add(jPanel20);

        jPanel1.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel6.setPreferredSize(new java.awt.Dimension(940, 100));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel24.setLayout(new java.awt.GridLayout(1, 0));

        txaResumenes.setEditable(false);
        txaResumenes.setColumns(20);
        txaResumenes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txaResumenes.setRows(5);
        jScrollPane2.setViewportView(txaResumenes);

        jPanel24.add(jScrollPane2);

        jPanel6.add(jPanel24, java.awt.BorderLayout.CENTER);

        jPanel25.setPreferredSize(new java.awt.Dimension(1363, 30));
        jPanel25.setLayout(new java.awt.GridLayout(1, 0));

        lbFilas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbFilas.setText("Filas: 0");
        jPanel25.add(lbFilas);

        jPanel6.add(jPanel25, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 140));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel9.setLayout(flowLayout1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Fecha");
        jPanel9.add(jLabel1);

        dtSolicitud.setDateFormatString("dd-MM-yyyy");
        dtSolicitud.setMinimumSize(new java.awt.Dimension(120, 25));
        dtSolicitud.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel9.add(dtSolicitud);

        jPanel16.add(jPanel9);

        java.awt.FlowLayout flowLayout5 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout5.setAlignOnBaseline(true);
        jPanel14.setLayout(flowLayout5);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Moneda");
        jPanel14.add(jLabel6);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CRC", "USD" }));
        jPanel14.add(cmbMoneda);

        jPanel16.add(jPanel14);

        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout2.setAlignOnBaseline(true);
        jPanel10.setLayout(flowLayout2);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Frecuencia");
        jPanel10.add(jLabel2);

        cmbFrecuencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semanal", "Mensual", "Anual" }));
        jPanel10.add(cmbFrecuencia);

        jPanel16.add(jPanel10);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Monto");
        jPanel11.add(jLabel3);

        txtMonto.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel11.add(txtMonto);

        jPanel16.add(jPanel11);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Proveedor o Actividad");
        jPanel12.add(jLabel4);

        txtProvAct.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel12.add(txtProvAct);

        jPanel16.add(jPanel12);

        java.awt.FlowLayout flowLayout4 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout4.setAlignOnBaseline(true);
        jPanel13.setLayout(flowLayout4);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Departamento");
        jPanel13.add(jLabel5);

        cmbDepartamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepartamentoItemStateChanged(evt);
            }
        });
        jPanel13.add(cmbDepartamento);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Cta presupuesto");
        jPanel13.add(jLabel13);

        cmbCta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCtaItemStateChanged(evt);
            }
        });
        jPanel13.add(cmbCta);

        jPanel16.add(jPanel13);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Observaciones");
        jPanel23.add(jLabel12);

        txtObservaciones.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel23.add(txtObservaciones);

        jPanel16.add(jPanel23);

        btnAddRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_icon_25x25_2.png"))); // NOI18N
        btnAddRow.setToolTipText("Agregar un gasto");
        btnAddRow.setPreferredSize(new java.awt.Dimension(30, 30));
        btnAddRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRowActionPerformed(evt);
            }
        });
        jPanel15.add(btnAddRow);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save-25.png"))); // NOI18N
        btnSave.setToolTipText("Guardar cambios");
        btnSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel15.add(btnSave);

        jPanel16.add(jPanel15);

        jPanel2.add(jPanel16, java.awt.BorderLayout.CENTER);

        jPanel17.setPreferredSize(new java.awt.Dimension(1211, 22));
        jPanel17.setLayout(new java.awt.GridLayout(1, 0));

        jProgressBar1.setOpaque(true);
        jProgressBar1.setStringPainted(true);
        jPanel17.add(jProgressBar1);

        jPanel2.add(jPanel17, java.awt.BorderLayout.PAGE_START);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel5.setPreferredSize(new java.awt.Dimension(400, 30));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1395, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 351, Short.MAX_VALUE)
        );

        add(jPanel8, java.awt.BorderLayout.LINE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRowActionPerformed
        // TODO add your handling code here:

        GastosFijosPeriodicos g = getGastosFromForm();
        if (g != null) {
            boolean res = crg.agregarAbonoSugerido(g);
            if (res) {
                //addRowTb(g, model);
                loadGastos();
                refreshForm();
                //afterAddEvents();
                JOptionPane.showMessageDialog(null, "Se ha agregado un registro");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar el registro");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor asegurese que la información es correcta y completa");
        }
    }//GEN-LAST:event_btnAddRowActionPerformed
    /*private void afterAddEvents() {
        this.loadingInfo = true;
        view.util.JTableCommonFunctions.limpiarTabla(tbRepPago);
        DefaultTableModel model = (DefaultTableModel) this.tbRepPago.getModel();
        String iddepartamento = this.cmbDepartamento.getSelectedItem().toString().substring(0, 2);
        this.listaGastos = crg.obtenerGastosFijosPorIdDepartamento(iddepartamento);
        this.listaGastos.forEach(e -> {
            addRowTb(e, model);
        });
        this.loadingInfo = false;
    }*/
    private void btnExportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportExcelActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                guardarExcel();
            }
        });
        t.start();
    }//GEN-LAST:event_btnExportExcelActionPerformed

    private void cmbDepartamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepartamentoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            prepareCmbCtas();
        }
    }//GEN-LAST:event_cmbDepartamentoItemStateChanged
    private void prepareCmbCtas() {
        String iddepartamento = this.cmbDepartamento.getSelectedItem().toString().substring(0, 2);
        data.CrudPresupuesto crudp = new data.CrudPresupuesto();
        listaPresupuesto = crudp.obtenerPresupuestoPorDep(iddepartamento);
        cmbCta.removeAllItems();
        listaPresupuesto.forEach(l -> {

            if (l.getCTAPRESUPUESTO().endsWith("00")) {
                cmbCta.addItem(l.getCTAPRESUPUESTO() + "-" + l.getCONCEPATOADETALLE().toUpperCase());
            } else {
                cmbCta.addItem("     " + l.getCTAPRESUPUESTO() + "-" + l.getCONCEPATOADETALLE().toUpperCase());
            }

        });
    }
    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        // TODO add your handling code here:
        this.loadingInfo = true;
        cmbEstado.setSelectedIndex(0);
        this.jCheckBox1.setSelected(false);
        loadGastos();
        this.loadingInfo = false;
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void cmbFiltDepartamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFiltDepartamentoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            /*view.util.JTableCommonFunctions.limpiarTabla(tbRepPago);

            String iddepartamento = this.cmbFiltDepartamento.getSelectedItem().toString().substring(0, 2);
            this.listaGastos = crg.obtenerGastosFijosPorIdDepartamento(iddepartamento);
            DefaultTableModel model = (DefaultTableModel) this.tbRepPago.getModel();
            this.listaGastos.forEach(e -> {
                addRowTb(e, model);
            });
            this.loadingInfo = false;*/
            loadGastos();
        }
    }//GEN-LAST:event_cmbFiltDepartamentoItemStateChanged

    private void dtFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtFinPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().contains("date")) {
            loadGastos();
        }
    }//GEN-LAST:event_dtFinPropertyChange

    private void cmbEstadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadGastos();
        }
    }//GEN-LAST:event_cmbEstadoItemStateChanged

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            loadGastos();
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void mnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEliminarActionPerformed
        // TODO add your handling code here:
        if (tbRepPago.getSelectedRows().length > 1) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione una sóla fila");
        } else {
            int row = tbRepPago.getSelectedRow();
            int idGasto = (int) tbRepPago.getValueAt(row, 14);

            GastosFijosPeriodicos g = crg.obtenerGastosFijoPorId(idGasto);
            if (g != null) {
                if (g.getEstado() == 1 && AdministradorGestionGastosPer == 0) {
                    JOptionPane.showMessageDialog(null, "No puede eliminar este registro porque fue aprobado por otro usuario");
                    return;
                }

            }
            boolean res = crg.eliminar(idGasto);
            if (res) {
                loadGastos();
                JOptionPane.showMessageDialog(null, "Se ha eliminado un registro");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar un registro");
            }

        }
    }//GEN-LAST:event_mnEliminarActionPerformed

    private void btnCargarRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarRegistrosActionPerformed
        // TODO add your handling code here:
        loadGastos();
    }//GEN-LAST:event_btnCargarRegistrosActionPerformed

    private void cmbCtaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCtaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCtaItemStateChanged

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        int row = tbRepPago.getSelectedRow();
        int idGasto = (int) tbRepPago.getValueAt(row, 14);

        GastosFijosPeriodicos g = crg.obtenerGastosFijoPorId(idGasto);
        if ((g.getEstado() == 1) && this.AdministradorGestionGastosPer == 0) {
            refreshForm();
            btnAddRow.setEnabled(true);
            btnSave.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Usted no tiene permisos para editar registros aprobados o realizados");

            return;
        }
        GastosFijosPeriodicos ga = getGastosFromForm();
        if (ga != null) {
            ga.setId(g.getId());
            boolean res = crg.actualizarFromForm(ga);
            if (res) {
                refreshForm();
                loadGastos();
                JOptionPane.showMessageDialog(null, "Se ha actualizado un registro");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error actualizando un registro");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor asegurese que la información es correcta y completa");
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void mnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEditActionPerformed
        // TODO add your handling code here:
        if (tbRepPago.getSelectedRows().length > 1) {
            btnAddRow.setEnabled(true);
            this.btnSave.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Por favor seleccione sólo una fila");
        } else {
            int row = tbRepPago.getSelectedRow();
            int idGasto = (int) tbRepPago.getValueAt(row, 14);
            GastosFijosPeriodicos g = crg.obtenerGastosFijoPorId(idGasto);
            if ((g.getEstado() == 1) && this.AdministradorGestionGastosPer == 0) {
                refreshForm();
                btnAddRow.setEnabled(true);
                btnSave.setEnabled(false);
                loadGastos();
                JOptionPane.showMessageDialog(null, "Usted no tiene permisos para editar registros aprobados");

                return;
            }
            this.btnSave.setEnabled(true);
            this.btnAddRow.setEnabled(false);
            String monto = logic.AppStaticValues.numberFormater.format(g.getMonto());
            this.txtMonto.setText(monto.replace(",", ""));
            this.txtObservaciones.setText(g.getObservaciones());
            this.txtProvAct.setText(g.getProveedorActividad());
            this.dtSolicitud.setDate(g.getFechaSolicitud());
            Departamento d = Departamento.getDepartamentoByCodDepa(departamentosPropios, g.getIdDepartamento());
            this.cmbDepartamento.setSelectedItem(d.getDEPARTAMENTO() + "-" + d.getDescripcion());
            this.cmbCta.setSelectedItem("     " + g.getCtaPresupuesto());
            this.cmbMoneda.setSelectedItem(g.getMoneda());
        }
    }//GEN-LAST:event_mnEditActionPerformed
    private void loadGastos() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                dtFin.setEnabled(false);
                btnCargarRegistros.setEnabled(false);
                btnAddRow.setEnabled(true);
                btnSave.setEnabled(false);
                try {
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    jProgressBar1.setVisible(true);
                    jProgressBar1.setString("Cargando...");
                    loadingInfo = true;
                    JTableCommonFunctions.limpiarTabla(tbRepPago);
                    java.util.Date inicio = dtInicio.getDate();
                    java.util.Date fin = dtFin.getDate();
                    String dep = cmbFiltDepartamento.getSelectedItem().toString().trim();
                    if (dep.equals("Todos")) {
                        dep = "";
                    }
                    int estado = cmbEstado.getSelectedIndex();
                    boolean realizado = jCheckBox1.isSelected();
                    listaGastos = crg.obtenerGastosFijosPorDepartamento(dep, inicio, fin, estado, realizado, AdministradorGestionGastosPer);
                    DefaultTableModel model = (DefaultTableModel) tbRepPago.getModel();
                    listaGastos.forEach(e -> {
                        addRowTb(e, model);
                    });
                    jProgressBar1.setString("Información cargada...");
                    CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    dtFin.setEnabled(true);
                    btnCargarRegistros.setEnabled(true);
                    lbFilas.setText("Filas: " + tbRepPago.getRowCount());
                    loadingInfo = false;
                } catch (Exception e) {
                    jProgressBar1.setString("La carga de información ha fallado");
                    CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    JOptionPane.showMessageDialog(null, "A ocurrido un error, asegurese de que la información es correcta");
                }
                dtFin.setEnabled(true);
                setDtChoosers();
                btnCargarRegistros.setEnabled(true);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void guardarExcel() {
        SimpleExcelWriter sew = new SimpleExcelWriter();
        //sew.writeToExcell(this.tbConciliacionMarcas);

        boolean saved = sew.writeJtableExcelFile(tbRepPago, "Reporte");
        if (saved) {
            JOptionPane.showMessageDialog(null, "Se ha guardado el archivo");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha guardado el archivo");
        }
    }

    private void refreshForm() {
        this.txtMonto.setText("0");
        this.txtProvAct.setText("");
        this.txtObservaciones.setText("");

        //this.cmbDepartamento.setSelectedIndex(0);
        //this.cmbFrecuencia.setSelectedIndex(0);
        //this.cmbMoneda.setSelectedIndex(0);
    }

    private GastosFijosPeriodicos getGastosFromForm() {
        GastosFijosPeriodicos g = null;
        try {
            java.util.Date date = dtSolicitud.getDate();
            String cta = cmbCta.getSelectedItem().toString();
            if (date != null && !txtProvAct.getText().isEmpty()
                    && !cta.substring(0, 11).endsWith("00")) {
                String codepa = cmbDepartamento.getSelectedItem().toString().substring(0, 2);
                Departamento d = Departamento.getDepartamentoByCodDepa(departamentosPropios, codepa);
                g = new GastosFijosPeriodicos();
                g.setFechaSolicitud(date);
                g.setFechaCreacion(new java.util.Date());
                g.setDepartamento(cmbDepartamento.getSelectedItem().toString());
                g.setMonto(Double.parseDouble(txtMonto.getText().replace(",", "")));
                g.setMontoF(txtMonto.getText().replaceAll(",", ""));
                g.setMoneda(cmbMoneda.getSelectedItem().toString());
                g.setFecuencia(cmbFrecuencia.getSelectedItem().toString());
                g.setProveedorActividad(txtProvAct.getText());
                g.setUsuarioCreador(DataUser.username);
                g.setUsuarioAutoriza("");
                g.setIdDepartamento(d.getDEPARTAMENTO());
                g.setObservaciones(txtObservaciones.getText());
                g.setCtaPresupuesto(cta);
                g.setEstado(0);
                g.setRealizado(false);
            }
        } catch (Exception e) {
            System.err.println("view.MantenimientoGastosFijosPeriodicos.getGastosFromForm() error " + e.getMessage());
            g = null;
        }
        return g;
    }

    private void addRowTb(GastosFijosPeriodicos g, DefaultTableModel model) {
        try {
            boolean estado = g.getEstado() == 0 ? false : true;
            model.addRow(new Object[]{
                g.getFechaSolicitud(),
                g.getFechaCreacion(),
                g.getProveedorActividad(),
                g.getMoneda(),
                logic.AppStaticValues.numberFormater.format(g.getMonto()),
                g.getFecuencia(),
                g.getDepartamento(),
                g.getUsuarioCreador(),
                g.getUsuarioAutoriza(),
                g.getIdDepartamento(),
                g.getCtaPresupuesto(),
                g.getObservaciones(),
                estado,
                g.isRealizado(),
                g.getId()
            });
        } catch (Exception e) {
            System.out.println("view.MantenimientoGastosFijosPeriodicos.addRowTb() error " + e.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddRow;
    private javax.swing.JButton btnCargarRegistros;
    private javax.swing.JButton btnExportExcel;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbCta;
    private javax.swing.JComboBox<String> cmbDepartamento;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbFiltDepartamento;
    private javax.swing.JComboBox<String> cmbFrecuencia;
    private javax.swing.JComboBox<String> cmbMoneda;
    private com.toedter.calendar.JDateChooser dtFin;
    private com.toedter.calendar.JDateChooser dtInicio;
    private com.toedter.calendar.JDateChooser dtSolicitud;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbFilas;
    private javax.swing.JMenuItem mnEdit;
    private javax.swing.JMenuItem mnEliminar;
    private javax.swing.JTable tbRepPago;
    private javax.swing.JTextArea txaResumenes;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextField txtObservaciones;
    private javax.swing.JTextField txtProvAct;
    // End of variables declaration//GEN-END:variables

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import data.CrudAbonoSugerido;
import entitys.AbonoSugerido;
import entitys.Departamento;
import entitys.Presupuesto;
import entitys.ProveedorContado.CuentaProveedorContado;
import entitys.SubtiposExactus;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import logic.AppLogger;
import services.SimpleExcelWriter;
import view.util.CustomMessages;

/**
 *
 * @author eobregon
 */
public class MantenimientoCuentasExactus extends javax.swing.JFrame {

    /**
     * Creates new form MantenimientoSubtiposExactus
     */
    Runnable guardarExcel;
    ArrayList<Presupuesto> listaPresupuesto;
    ArrayList<SubtiposExactus> listaSubtipos;
    ArrayList<SubtiposExactus> listaSubtiposFilt;
    ArrayList<SubtiposExactus> listaCambiosSubtipos;
    ArrayList<Departamento> listaDepartamentos;
    ArrayList<String> proveedores;
    ArrayList<String> tipos;
    String selectedsubtipo = "";
    boolean loadingInfo = false;
    int option = -1;
    Icon presentationIcon;
    data.CrudPresupuesto crpre;

    public MantenimientoCuentasExactus() {
        initComponents();
    }

    MantenimientoCuentasExactus(int option, ArrayList<Presupuesto> listaPresupuesto, ArrayList<Departamento> listaDepartamentos) {
        initComponents();
        this.option = option;
        this.listaPresupuesto = listaPresupuesto;
        this.listaDepartamentos = listaDepartamentos;
        this.listaSubtipos = new ArrayList<>();
        this.listaSubtiposFilt = new ArrayList<>();
        this.listaCambiosSubtipos = new ArrayList<>();
        this.setLocationRelativeTo(null);
        this.crpre = new data.CrudPresupuesto();
        prepareCommonInfo();
        prepareGUI();
    }

    /**
     * this method calls database to load all the user common needed data
     */
    private void prepareCommonInfo() {
        this.listaDepartamentos.forEach(e -> {
            this.cmbDepartamento.addItem(e.getDescripcion());
        });
    }

    /**
     * this method sets the tittle for this form, so the tittle matches the
     * purpose of the form
     */
    private void setTbTitle(String title) {
        center.setBorder(javax.swing.BorderFactory
                .createTitledBorder(javax.swing.BorderFactory
                        .createMatteBorder(2, 2, 2, 2,
                                new java.awt.Color(204, 204, 204)), title,
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new java.awt.Font("Segoe UI", 1, 14))); // NOI18N

    }

    /**
     * this method handles the process to initialized the info, shows it, and
     * the behavior of the GUI like event, all of this in the right order
     */
    private void prepareGUI() {
        setView();
        this.guardarExcel = new Runnable() {
            @Override
            public void run() {
                guardarExcel();
            }
        };
        setTxtAsientoEvent();
        tbMntCuentasEvents();
    }

    private void guardarExcel() {
        this.jProgressBar1.setString("Guardando excel...");
        this.jProgressBar1.setVisible(true);
        SimpleExcelWriter sew = new SimpleExcelWriter();
        //sew.writeToExcell(this.tbConciliacionMarcas);

        boolean saved = sew.writeJtableExcelFile(tbMntCuentas, "Reporte");
        if (saved) {
            this.jProgressBar1.setString("Excel guardadado correctamente");
            CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        } else {
            this.jProgressBar1.setString("Proceso cancelado");
            //this.jpbLoading.setVisible(false);
            CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        }
    }

    /**
     * this method prepares the event for the txtAsiento textfield, witch will
     * serch for the matching register in the info loaded
     */
    private void setTxtAsientoEvent() {
        txtAsiento.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarSubtipos();
                }
            }

        });
        txtProveedor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarSubtiposPorProveedor();
                }
            }

        });
    }

    private void setDateChooserLook() {
        IDateEditor dateEditor = jdtInicio.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
        dateEditor = jdFin.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
    }

    /**
     * this method prepared all the set ups for the GUI before the user gets to
     * interact with it
     */
    private void setView() {

        this.btnDeleteRows.setVisible(false);
        setDateChooserLook();
        chooseOptionCase();

        this.jProgressBar1.setVisible(false);
        this.btnHistory.setEnabled(false);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        tbMntCuentas.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        tbMntCuentas.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
        tbUndo.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
        tbUndo.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
    }

    /**
     * this method sets up the different options for uses of this form, witch
     * are:
     *
     * 1) for Mantenimiento Cuentas por Pagar
     *
     * 2) for Mantenimiento Movimientos Bancarios
     *
     * 3) for Mantenimiento Contabilidad General
     */
    private void chooseOptionCase() {
        this.cmbTipo.setVisible(false);
        this.lbTipo.setVisible(false);
        this.txtProveedor.setVisible(false);
        this.lbtxProv.setVisible(false);
        this.btnBuscProv.setVisible(false);
        String title = "";
        switch (option) {
            case 1:
                this.presentationIcon = new javax.swing.ImageIcon(getClass().getResource("/images/icons8-money-25.png"));
                title = "Mantenimiento Cuentas por Pagar";
                break;
            case 2:
                tbMntCuentas.getColumnModel().getColumn(9).setMinWidth(0);
                tbMntCuentas.getColumnModel().getColumn(9).setMaxWidth(0);
                tbMntCuentas.getColumnModel().getColumn(9).setWidth(0);
                tbMntCuentas.getColumnModel().getColumn(9).setPreferredWidth(0);
                this.presentationIcon = new javax.swing.ImageIcon(getClass().getResource("/images/icons8-bank-25.png"));
                title = "Mantenimiento Movimientos Bancarios";
                this.cmbTipo.setVisible(true);
                this.lbTipo.setVisible(true);
                this.txtProveedor.setVisible(true);
                this.lbtxProv.setVisible(true);
                this.btnBuscProv.setVisible(true);
                break;
            case 3:
                tbMntCuentas.getColumnModel().getColumn(9).setMinWidth(0);
                tbMntCuentas.getColumnModel().getColumn(9).setMaxWidth(0);
                tbMntCuentas.getColumnModel().getColumn(9).setWidth(0);
                tbMntCuentas.getColumnModel().getColumn(9).setPreferredWidth(0);
                this.presentationIcon = new javax.swing.ImageIcon(getClass().getResource("/images/icons8-accounting-25.png"));
                title = "Mantenimiento Contabilidad General";
                break;

        }

        this.setTitle(title);
        setTbTitle("Tabla " + title);
        lbTitle.setIcon(presentationIcon);
        this.lbTitle.setText(title);

    }

    /**
     * this method sets up the behavior of the tbMntCuentas table events
     */
    private void tbMntCuentasEvents() {
        this.tbMntCuentas.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int row = tbMntCuentas.rowAtPoint(e.getPoint());
                if (row >= 0 && row < tbMntCuentas.getRowCount()) {
                    tbMntCuentas.setRowSelectionInterval(row, row);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        //int row = jTable1.rowAtPoint(e.getPoint());
                        jPopupMenu1.show(tbMntCuentas, e.getX(), e.getY());
                    }
                } else {
                    tbMntCuentas.clearSelection();
                }

            }
        });
        this.tbMntCuentas.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    boolean selected = !(Boolean) tbMntCuentas.getValueAt(row, 12);
                    tbMntCuentas.setValueAt(selected, row, 12);
                }
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

        jDialog1 = new javax.swing.JDialog();
        jPanel14 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbUndo = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        btnDeleteTbUndo = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        lbTbUndoInfo = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        mnFiltProv = new javax.swing.JMenuItem();
        center = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbMntCuentas = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        lbTbSubTipos = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnDeleteRows = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cmbDepartamento = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbCuentaPresupesto = new javax.swing.JComboBox<>();
        btnSaveGroup = new javax.swing.JButton();
        btnHistory = new javax.swing.JButton();
        btnExportExcel = new javax.swing.JButton();
        north = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jdtInicio = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jdFin = new com.toedter.calendar.JDateChooser();
        btnBuscar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbCIA = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cmbProveedores = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        lbTipo = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        chkSinSub = new javax.swing.JCheckBox();
        btnRefresh = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtAsiento = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        lbtxProv = new javax.swing.JLabel();
        txtProveedor = new javax.swing.JTextField();
        btnBuscProv = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        down = new javax.swing.JPanel();
        rigth = new javax.swing.JPanel();
        left = new javax.swing.JPanel();

        jDialog1.setTitle("Historial de cambios");
        jDialog1.setMinimumSize(new java.awt.Dimension(800, 298));
        jDialog1.setModal(true);

        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.GridLayout(1, 0));

        tbUndo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sociedad", "Tipo", "Subtipo", "Subtipo anterior", "Subtipo actual", "Proveedor", "Documento", "Fecha", "Aplicacion", "Monto", "Monto $", "Asiento", "Idrow"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbUndo.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbUndo);
        if (tbUndo.getColumnModel().getColumnCount() > 0) {
            tbUndo.getColumnModel().getColumn(0).setMinWidth(60);
            tbUndo.getColumnModel().getColumn(0).setPreferredWidth(60);
            tbUndo.getColumnModel().getColumn(0).setMaxWidth(60);
            tbUndo.getColumnModel().getColumn(1).setMinWidth(40);
            tbUndo.getColumnModel().getColumn(1).setPreferredWidth(50);
            tbUndo.getColumnModel().getColumn(1).setMaxWidth(50);
            tbUndo.getColumnModel().getColumn(2).setMinWidth(0);
            tbUndo.getColumnModel().getColumn(2).setPreferredWidth(0);
            tbUndo.getColumnModel().getColumn(2).setMaxWidth(0);
            tbUndo.getColumnModel().getColumn(3).setMinWidth(100);
            tbUndo.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbUndo.getColumnModel().getColumn(3).setMaxWidth(70);
            tbUndo.getColumnModel().getColumn(4).setMinWidth(100);
            tbUndo.getColumnModel().getColumn(4).setPreferredWidth(100);
            tbUndo.getColumnModel().getColumn(4).setMaxWidth(100);
            tbUndo.getColumnModel().getColumn(6).setMinWidth(60);
            tbUndo.getColumnModel().getColumn(6).setPreferredWidth(70);
            tbUndo.getColumnModel().getColumn(6).setMaxWidth(70);
            tbUndo.getColumnModel().getColumn(7).setMinWidth(60);
            tbUndo.getColumnModel().getColumn(7).setPreferredWidth(70);
            tbUndo.getColumnModel().getColumn(7).setMaxWidth(70);
            tbUndo.getColumnModel().getColumn(9).setMinWidth(60);
            tbUndo.getColumnModel().getColumn(9).setPreferredWidth(70);
            tbUndo.getColumnModel().getColumn(9).setMaxWidth(90);
            tbUndo.getColumnModel().getColumn(10).setMinWidth(60);
            tbUndo.getColumnModel().getColumn(10).setPreferredWidth(80);
            tbUndo.getColumnModel().getColumn(10).setMaxWidth(90);
            tbUndo.getColumnModel().getColumn(11).setMinWidth(60);
            tbUndo.getColumnModel().getColumn(11).setPreferredWidth(70);
            tbUndo.getColumnModel().getColumn(11).setMaxWidth(100);
            tbUndo.getColumnModel().getColumn(12).setMinWidth(0);
            tbUndo.getColumnModel().getColumn(12).setPreferredWidth(0);
            tbUndo.getColumnModel().getColumn(12).setMaxWidth(0);
        }

        jPanel19.add(jScrollPane2);

        jPanel14.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel20.setPreferredSize(new java.awt.Dimension(542, 35));

        btnDeleteTbUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-undo-25.png"))); // NOI18N
        btnDeleteTbUndo.setToolTipText("Deshacer los cambios en base de datos");
        btnDeleteTbUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTbUndoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDeleteTbUndo)
                .addContainerGap(609, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(btnDeleteTbUndo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel20, java.awt.BorderLayout.PAGE_START);

        jPanel21.setPreferredSize(new java.awt.Dimension(542, 20));
        jPanel21.setLayout(new java.awt.GridLayout(1, 0));

        lbTbUndoInfo.setText("Filas: 0");
        jPanel21.add(lbTbUndoInfo);

        jPanel14.add(jPanel21, java.awt.BorderLayout.PAGE_END);

        jDialog1.getContentPane().add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel15.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jPanel15.setPreferredSize(new java.awt.Dimension(491, 35));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Seleccione y elimine filas para deshacer cambios");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 666, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addGap(0, 170, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addGap(0, 170, Short.MAX_VALUE)))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addGap(0, 7, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addGap(0, 7, Short.MAX_VALUE)))
        );

        jDialog1.getContentPane().add(jPanel15, java.awt.BorderLayout.PAGE_START);

        jPanel16.setPreferredSize(new java.awt.Dimension(491, 20));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 666, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel16, java.awt.BorderLayout.PAGE_END);

        jPanel17.setPreferredSize(new java.awt.Dimension(10, 258));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel17, java.awt.BorderLayout.LINE_END);

        jPanel18.setPreferredSize(new java.awt.Dimension(10, 258));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel18, java.awt.BorderLayout.LINE_START);

        mnFiltProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/filer_icon_png_25x25.png"))); // NOI18N
        mnFiltProv.setText("Filtrar Proveedor");
        mnFiltProv.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mnFiltProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFiltProvActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnFiltProv);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mantenimiento de subtipos");
        setMinimumSize(new java.awt.Dimension(1282, 400));

        center.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla Mantenimiento Subtipos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        center.setPreferredSize(new java.awt.Dimension(984, 100));
        center.setLayout(new java.awt.BorderLayout(10, 5));

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        tbMntCuentas.setAutoCreateRowSorter(true);
        tbMntCuentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SOCIEDAD", "TIPO", "SUBTIPO", "Cta Presup", "PROVEEDOR", "DOCUMENTO", "FECHA", "APLICACION", "MONTO", "DOLARES", "ASIENTO", "idRow", "ASIGNAR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbMntCuentas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbMntCuentas.setShowHorizontalLines(true);
        tbMntCuentas.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbMntCuentas);
        if (tbMntCuentas.getColumnModel().getColumnCount() > 0) {
            tbMntCuentas.getColumnModel().getColumn(0).setMinWidth(80);
            tbMntCuentas.getColumnModel().getColumn(0).setPreferredWidth(80);
            tbMntCuentas.getColumnModel().getColumn(0).setMaxWidth(80);
            tbMntCuentas.getColumnModel().getColumn(1).setMinWidth(40);
            tbMntCuentas.getColumnModel().getColumn(1).setPreferredWidth(40);
            tbMntCuentas.getColumnModel().getColumn(1).setMaxWidth(40);
            tbMntCuentas.getColumnModel().getColumn(2).setMinWidth(0);
            tbMntCuentas.getColumnModel().getColumn(2).setPreferredWidth(0);
            tbMntCuentas.getColumnModel().getColumn(2).setMaxWidth(0);
            tbMntCuentas.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbMntCuentas.getColumnModel().getColumn(4).setPreferredWidth(300);
            tbMntCuentas.getColumnModel().getColumn(5).setMinWidth(90);
            tbMntCuentas.getColumnModel().getColumn(5).setPreferredWidth(90);
            tbMntCuentas.getColumnModel().getColumn(5).setMaxWidth(90);
            tbMntCuentas.getColumnModel().getColumn(6).setMinWidth(50);
            tbMntCuentas.getColumnModel().getColumn(6).setPreferredWidth(70);
            tbMntCuentas.getColumnModel().getColumn(6).setMaxWidth(70);
            tbMntCuentas.getColumnModel().getColumn(7).setPreferredWidth(250);
            tbMntCuentas.getColumnModel().getColumn(8).setPreferredWidth(90);
            tbMntCuentas.getColumnModel().getColumn(9).setPreferredWidth(90);
            tbMntCuentas.getColumnModel().getColumn(10).setPreferredWidth(60);
            tbMntCuentas.getColumnModel().getColumn(11).setMinWidth(0);
            tbMntCuentas.getColumnModel().getColumn(11).setPreferredWidth(0);
            tbMntCuentas.getColumnModel().getColumn(11).setMaxWidth(0);
        }

        jPanel6.add(jScrollPane1);

        center.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(674, 20));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        lbTbSubTipos.setText(" Total de filas: 0");
        jPanel8.add(lbTbSubTipos);

        center.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jPanel9.setPreferredSize(new java.awt.Dimension(972, 35));
        java.awt.FlowLayout flowLayout4 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout4.setAlignOnBaseline(true);
        jPanel9.setLayout(flowLayout4);

        btnDeleteRows.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_icon_25x25.png"))); // NOI18N
        btnDeleteRows.setToolTipText("Eliminar las filas seleccionadas");
        btnDeleteRows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteRowsActionPerformed(evt);
            }
        });
        jPanel9.add(btnDeleteRows);

        jLabel4.setText("Departamento");
        jPanel9.add(jLabel4);

        cmbDepartamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepartamentoItemStateChanged(evt);
            }
        });
        jPanel9.add(cmbDepartamento);

        jLabel5.setText("Subtipo");
        jPanel9.add(jLabel5);

        cmbCuentaPresupesto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCuentaPresupestoItemStateChanged(evt);
            }
        });
        cmbCuentaPresupesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCuentaPresupestoActionPerformed(evt);
            }
        });
        jPanel9.add(cmbCuentaPresupesto);

        btnSaveGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save-25.png"))); // NOI18N
        btnSaveGroup.setToolTipText("Aplica  la cuenta  a todos los registros seleccionados");
        btnSaveGroup.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSaveGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveGroupActionPerformed(evt);
            }
        });
        jPanel9.add(btnSaveGroup);

        btnHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-history-25.png"))); // NOI18N
        btnHistory.setToolTipText("Historial de cambios");
        btnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistoryActionPerformed(evt);
            }
        });
        jPanel9.add(btnHistory);

        btnExportExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExportExcel.setToolTipText("Exportar a Excel");
        btnExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportExcelActionPerformed(evt);
            }
        });
        jPanel9.add(btnExportExcel);

        center.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        north.setMinimumSize(new java.awt.Dimension(900, 140));
        north.setPreferredSize(new java.awt.Dimension(984, 150));
        north.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(700, 120));
        jPanel7.setRequestFocusEnabled(false);
        jPanel7.setLayout(new java.awt.BorderLayout());

        java.awt.FlowLayout flowLayout5 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout5.setAlignOnBaseline(true);
        jPanel12.setLayout(flowLayout5);

        jLabel1.setText("Fecha inicio");
        jPanel12.add(jLabel1);

        jdtInicio.setDateFormatString("dd-MM-yyyy");
        jdtInicio.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel12.add(jdtInicio);

        jLabel2.setText("Fecha final");
        jPanel12.add(jLabel2);

        jdFin.setDateFormatString("dd-MM-yyyy");
        jdFin.setPreferredSize(new java.awt.Dimension(120, 22));
        jdFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdFinPropertyChange(evt);
            }
        });
        jPanel12.add(jdFin);

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBuscar.setToolTipText("Buscar registros en el rango escogido");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel12.add(btnBuscar);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel9.setToolTipText("Esta acción elimina el historial de cambios actual");
        jPanel12.add(jLabel9);

        jPanel7.add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel11.setMinimumSize(new java.awt.Dimension(900, 60));
        jPanel11.setPreferredSize(new java.awt.Dimension(900, 90));
        jPanel11.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        jPanel1.setLayout(flowLayout1);

        jLabel3.setText("CIA");
        jPanel1.add(jLabel3);

        cmbCIA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TODAS", "RYMSA", "CILT", "IRASA", "KATRA", "OPYLOG", "TURINTEL" }));
        cmbCIA.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCIAItemStateChanged(evt);
            }
        });
        jPanel1.add(cmbCIA);

        jLabel7.setText("Proveedor");
        jPanel1.add(jLabel7);

        cmbProveedores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas" }));
        cmbProveedores.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedoresItemStateChanged(evt);
            }
        });
        jPanel1.add(cmbProveedores);

        jLabel10.setText("Moneda");
        jPanel1.add(jLabel10);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TODAS", "CRC", "USD" }));
        cmbMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaItemStateChanged(evt);
            }
        });
        jPanel1.add(cmbMoneda);

        lbTipo.setText("Tipo");
        jPanel1.add(lbTipo);

        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas" }));
        cmbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoItemStateChanged(evt);
            }
        });
        jPanel1.add(cmbTipo);

        chkSinSub.setText("Mostrar sin Subtipo");
        chkSinSub.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkSinSubItemStateChanged(evt);
            }
        });
        jPanel1.add(chkSinSub);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refrescar2.png"))); // NOI18N
        btnRefresh.setToolTipText("Refrezca los filtros y la tabla");
        btnRefresh.setMaximumSize(new java.awt.Dimension(36, 30));
        btnRefresh.setMinimumSize(new java.awt.Dimension(30, 30));
        btnRefresh.setPreferredSize(new java.awt.Dimension(30, 30));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel1.add(btnRefresh);

        jPanel11.add(jPanel1);

        jPanel10.setPreferredSize(new java.awt.Dimension(400, 35));
        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout2.setAlignOnBaseline(true);
        jPanel10.setLayout(flowLayout2);

        jLabel6.setText("Ingrese Asiento");
        jPanel10.add(jLabel6);

        txtAsiento.setMinimumSize(new java.awt.Dimension(100, 22));
        txtAsiento.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel10.add(txtAsiento);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_list_29x29.png"))); // NOI18N
        jButton1.setToolTipText("Buscar por Asiento");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton1);

        lbtxProv.setText("Ingrese Proveedor");
        jPanel10.add(lbtxProv);

        txtProveedor.setMinimumSize(new java.awt.Dimension(100, 22));
        txtProveedor.setPreferredSize(new java.awt.Dimension(250, 25));
        jPanel10.add(txtProveedor);

        btnBuscProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_list_29x29.png"))); // NOI18N
        btnBuscProv.setToolTipText("Buscar por Asiento");
        btnBuscProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscProvActionPerformed(evt);
            }
        });
        jPanel10.add(btnBuscProv);

        jPanel11.add(jPanel10);

        jPanel7.add(jPanel11, java.awt.BorderLayout.CENTER);

        north.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel13.setPreferredSize(new java.awt.Dimension(1255, 45));
        jPanel13.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-accounting-25.png"))); // NOI18N
        lbTitle.setText("Title");
        lbTitle.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        lbTitle.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel13.add(lbTitle);

        jProgressBar1.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jProgressBar1.setOpaque(true);
        jProgressBar1.setPreferredSize(new java.awt.Dimension(146, 20));
        jProgressBar1.setString("");
        jProgressBar1.setStringPainted(true);
        jPanel13.add(jProgressBar1);

        north.add(jPanel13, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        down.setPreferredSize(new java.awt.Dimension(694, 20));

        javax.swing.GroupLayout downLayout = new javax.swing.GroupLayout(down);
        down.setLayout(downLayout);
        downLayout.setHorizontalGroup(
            downLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1216, Short.MAX_VALUE)
        );
        downLayout.setVerticalGroup(
            downLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        getContentPane().add(down, java.awt.BorderLayout.PAGE_END);

        rigth.setPreferredSize(new java.awt.Dimension(10, 310));

        javax.swing.GroupLayout rigthLayout = new javax.swing.GroupLayout(rigth);
        rigth.setLayout(rigthLayout);
        rigthLayout.setHorizontalGroup(
            rigthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        rigthLayout.setVerticalGroup(
            rigthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        getContentPane().add(rigth, java.awt.BorderLayout.LINE_END);

        left.setPreferredSize(new java.awt.Dimension(10, 310));

        javax.swing.GroupLayout leftLayout = new javax.swing.GroupLayout(left);
        left.setLayout(leftLayout);
        leftLayout.setHorizontalGroup(
            leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        leftLayout.setVerticalGroup(
            leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        getContentPane().add(left, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

        if (option == 2) {
            //loadAsyncSubtiposInfo();
            serchAsyncCB();
        } else {
            refreshInfoCP();
        }

    }//GEN-LAST:event_btnBuscarActionPerformed
    private void serchAsyncCB() {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                loadSubtiposInfoCB();
                if (!listaSubtipos.isEmpty()) {

                    cmbProveedores.removeAllItems();
                    loadCmbProveedores();
                    loadTipos();
                    //filtrarSubtipos();
                    loadTbSubtipos();

                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    private void jdFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdFinPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().contains("date") && !loadingInfo) {
            if (option == 2) {
                serchAsyncCB();

            } else {
                refreshInfoCP();
            }

        }
    }//GEN-LAST:event_jdFinPropertyChange
    private void prepareToShowProgressBar(int min, int max, int value, String message) {
        jProgressBar1.setVisible(true);
        jProgressBar1.setMinimum(min);
        jProgressBar1.setMaximum(max);
        jProgressBar1.setValue(value);
        jProgressBar1.setString(message);
    }
    private void cmbDepartamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepartamentoItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            cmbCuentaPresupesto.removeAllItems();
            String selectedItem = cmbDepartamento.getSelectedItem().toString();
            Departamento d = Departamento.getDepartamento(listaDepartamentos, selectedItem);
            if (d != null) {
                ArrayList<Presupuesto> cta = getSubCuentaPresupuesto(d);

                cta.forEach(e -> {
                    if (e.getCTAPRESUPUESTO().endsWith("00")) {
                        cmbCuentaPresupesto.addItem(e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE().toUpperCase());
                    } else {
                        cmbCuentaPresupesto.addItem("     " + e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE().toUpperCase());
                    }
                });
            }
        }
    }//GEN-LAST:event_cmbDepartamentoItemStateChanged
    /**
     * this method first refresh all controls associated to handle information,
     * then loads the new info from database, and finally put that info on the
     * GUI this is an async method
     */
    private void loadAsyncSubtiposInfo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                jdFin.setEnabled(false);
                btnBuscar.setEnabled(false);
                cmbProveedores.removeAllItems();
                cmbTipo.removeAllItems();
                view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
                prepareToShowProgressBar(0, 0, 0, "Cargando información...");
                chkSinSub.setSelected(false);
                obtenerSubtipos();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                btnBuscar.setEnabled(true);
                jdFin.setEnabled(true);
                setDateChooserLook();
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    /**
     * this method first refresh all controls associated to handle information,
     * then loads the new info from database, and finally put that info on the
     * GUI this is an async method
     */
    private void loadSubtiposInfoCB() {

        jdFin.setEnabled(false);
        btnBuscar.setEnabled(false);
        //cmbProveedores.removeAllItems();
        //cmbTipo.removeAllItems();
        view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        prepareToShowProgressBar(0, 0, 0, "Cargando información...");
        //chkSinSub.setSelected(false);
        selectOptionSubtipos();
        btnBuscar.setEnabled(true);
        jdFin.setEnabled(true);
        setDateChooserLook();

    }

    /**
     * this method first refresh all controls associated to handle information,
     * then loads the new info from database, and finally put that info on the
     * GUI this is an async method
     */
    private void loadSubtiposInfo() {

        jdFin.setEnabled(false);
        btnBuscar.setEnabled(false);
        //cmbTipo.removeAllItems();
        view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        prepareToShowProgressBar(0, 0, 0, "Cargando información...");
        //chkSinSub.setSelected(false);
        selectOptionSubtipos();

        btnBuscar.setEnabled(true);
        jdFin.setEnabled(true);
        setDateChooserLook();

    }
    private void cmbCuentaPresupestoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCuentaPresupestoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                String cuenta = this.cmbCuentaPresupesto.getSelectedItem().toString().trim().substring(3, 5);
                if (cuenta.equals("99")) {
                    this.selectedsubtipo = this.cmbCuentaPresupesto.getSelectedItem().toString().trim().substring(0, 11);
                } else {
                    this.selectedsubtipo = this.cmbCuentaPresupesto.getSelectedItem().toString().trim().substring(3, 11);
                }
                SubtiposExactus sbe = SubtiposExactus.getSubtipoPorDesc(this.selectedsubtipo, this.listaSubtipos);
                if (sbe != null) {
                    System.err.println("subselected " + this.selectedsubtipo + " index " + sbe.getSUBTIPO());

                }
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_cmbCuentaPresupestoItemStateChanged

    private void cmbCuentaPresupestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCuentaPresupestoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbCuentaPresupestoActionPerformed

    private void cmbCIAItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCIAItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            if (option == 2) {
                anteriorMetodo();
            } else {
                refreshInfoCP();
            }
        }
    }//GEN-LAST:event_cmbCIAItemStateChanged
    private void refreshInfoCP() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
                cmbProveedores.removeAllItems();
                cmbProveedores.addItem("Todas");
                cmbMoneda.setSelectedIndex(0);
                chkSinSub.setSelected(false);
                loadSubtiposInfo();
                if (!listaSubtipos.isEmpty()) {

                    loadCmbProveedores();
                    //loadTipos();
                    loadTbSubtipos();
                }
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                loadingInfo = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void anteriorMetodo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                loadingInfo = true;
                cmbProveedores.removeAllItems();
                cmbProveedores.addItem("Todas");
                cmbTipo.removeAllItems();
                cmbTipo.addItem("Todas");
                cmbMoneda.setSelectedIndex(0);

                loadSubtiposInfoCB();
                if (!listaSubtipos.isEmpty()) {

                    //JOptionPane.showMessageDialog(null, "subtipos " + listaSubtipos.size());
                    cmbProveedores.removeAllItems();
                    loadCmbProveedores();
                    loadTipos();
                    //filtrarSubtipos();
                    loadTbSubtipos();
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
        /*loadingInfo = true;
        view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        this.cmbProveedores.removeAllItems();
        loadCIAProveedores();
        loadingInfo = false;
        filtrarSubtipos();
        loadAsyncSubtiposInfo();
        loadTbSubtipos();
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);*/
    }
    private void chkSinSubItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkSinSubItemStateChanged
        // TODO add your handling code here:
        if (!loadingInfo) {
            if (option == 2) {
                chSinSubAnteriorMth();
            } else {
                loadAsyncFiltInfo();
            }
        }
    }//GEN-LAST:event_chkSinSubItemStateChanged
    private void chSinSubAnteriorMth() {

        /*view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        filtrarSubtipos();
        loadTbSubtipos();
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);*/
        loadingInfo = true;
        this.cmbTipo.removeAllItems();
        this.cmbTipo.addItem("Todas");
        this.cmbMoneda.setSelectedIndex(0);
        this.cmbProveedores.setSelectedIndex(0);
        //this.chkSinSub.setSelected(false);
        this.cmbCIA.setSelectedIndex(0);
        loadingInfo = false;
        serchAsyncCB();
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        buscarSubtipos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:

        if (option == 2) {
            anteriorRefresInfo();
        } else {
            this.loadingInfo = true;
            this.cmbCIA.setSelectedIndex(0);
            loadingInfo = false;
            refreshInfoCP();
        }
    }//GEN-LAST:event_btnRefreshActionPerformed
    private void anteriorRefresInfo() {
        loadingInfo = true;
        this.cmbTipo.removeAllItems();
        this.cmbTipo.addItem("Todas");
        this.cmbMoneda.setSelectedIndex(0);
        this.cmbProveedores.setSelectedIndex(0);
        this.chkSinSub.setSelected(false);
        this.cmbCIA.setSelectedIndex(0);
        loadingInfo = false;
        serchAsyncCB();

        /*loadingInfo = true;
        view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        this.cmbCIA.setSelectedIndex(0);
        this.cmbMoneda.setSelectedIndex(0);
        this.cmbProveedores.removeAllItems();
        this.cmbTipo.removeAllItems();
        this.txtAsiento.setText("");
        this.txtProveedor.setText("");
        this.chkSinSub.setSelected(false);
        loadCmbProveedores();
        loadTipos();
        loadingInfo = false;
        filtrarSubtipos();
        loadTbSubtipos();
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);*/
    }

    private boolean saveRow(int row) {
        boolean res = false;
        String rowindx = tbMntCuentas.getValueAt(row, 11).toString();
        SubtiposExactus s = SubtiposExactus.getSubtipoPorIdRow(rowindx, listaSubtipos);
        if (s != null) {
            logChange(s);
            res = chooseOptionToUpdate(s);
            if (res) {

                String anteriorSubtipo = s.getDESCRIPCION();

                SubtiposExactus sub = SubtiposExactus.getSubtipoFromOther(s, selectedsubtipo, anteriorSubtipo);
                //Presupuesto newCta = crpre.obtenerPresupuestoPorCta((s.getSociedad().equals("CILT") ? "01-" : "02-") +selectedsubtipo);
                //Presupuesto ant = crpre.obtenerPresupuestoPorCta((s.getSociedad().equals("CILT") ? "01-" : "02-") + s.getDESCRIPCION());
                Presupuesto newCta = crpre.obtenerPresupuestoPorCtaNoCia(selectedsubtipo);
                Presupuesto.getPresupuestoGen(rowindx, listaPresupuesto);
                this.listaCambiosSubtipos.add(sub);
                actualizarAbonos(s,selectedsubtipo, newCta.getCONCEPATOADETALLE());
                s.setDESCRIPCION(this.selectedsubtipo);
                //actualizarAbonos(s, actual.getDESCRIPCION(), sub);
                //JOptionPane.showMessageDialog(null, "Se ha actualizado el documento " + s.getDOCUMENTO() + " correctamente");
            } else {
                JOptionPane.showMessageDialog(null, "No se ha actualizado el registro");
            }
        }else{JOptionPane.showMessageDialog(null, "No se ha encontrado el subtipo");}
        return res;
    }

    private void actualizarAbonos(SubtiposExactus s,String nCta, String nDescCta) {
        //actualizar abonos sugeridos tomando la cuenta de presupuesto, nombre de proveedor, sociedad, monto y numero de documento
        AbonoSugerido ab = new AbonoSugerido();
        ab.setCuenta_Presupuesto(nCta);
        ab.setCIA(s.getSociedad());
        ab.setNombre_Proveedor(s.getNombre_Proveedor());
        ab.setDocumento(s.getDOCUMENTO());
        ab.setDescripion_Cta_Presupuesto(nDescCta);
        ab.setMonto_Original(s.getMONTO());

        CrudAbonoSugerido cruda = new CrudAbonoSugerido();
        boolean res = cruda.actualizarAbonoSugeridoCambioCuenta(ab, s.getDESCRIPCION());
        if (res) {
            System.out.println("view.MantenimientoCuentasExactus.actualizarAbonos() se han actualizado los abonos ");
        } else {
            JOptionPane.showMessageDialog(null, "no se ha actualizado los abonos");
        }
    }

    private void logChange(SubtiposExactus s) {
        String cambio = "rowindex " + s.getIdRow()
                + "\n indice subtipo " + s.getSUBTIPO()
                + "\n descripcion subtipo " + s.getDESCRIPCION()
                + "\n documento " + s.getDOCUMENTO()
                + "\n CIA " + s.getSociedad()
                + "\n asiento " + s.getASIENTO()
                + "\n nuevo subtipo " + this.selectedsubtipo;
        AppLogger.appLogger.log(Level.WARNING, cambio);
    }

    /**
     * this method receives SubtiposExactus object, choose between the options
     * that this form is currently use for, then updates the register on
     * database
     *
     * @param s is the SubtiposExactus object to be updated
     * @return a boolean true value in a success event, else false
     */
    private boolean chooseOptionToUpdate(SubtiposExactus s) {
        boolean res = false;
        data.CRUDSubtiposExactus cs = new data.CRUDSubtiposExactus();
        switch (this.option) {
            case 1:
                res = cs.actualizarSubtiposCP(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), this.selectedsubtipo);

                break;
            case 2:
                res = cs.actualizarSubtiposCB(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), this.selectedsubtipo);
                break;
            case 3:
                res = cs.actualizarSubtiposCG(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), this.selectedsubtipo);
                break;

        }
        return res;
    }

    /**
     * this method receives SubtiposExactus object, choose between the options
     * that this form is currently use for, then updates the register on
     * database
     *
     * @param s is the SubtiposExactus object to be updated
     * @return a boolean true value in a success event, else false
     */
    private boolean undoUpdate(SubtiposExactus s) {
        boolean res = false;

        if (s != null) {
            logChange(s);

            res = chooseOptionToUpdate(s);

            if (!res) {
                JOptionPane.showMessageDialog(null, "No se ha actualizado el registro");
            }
        }
        return res;
    }

    private void cmbProveedoresItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedoresItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            if (option == 2) {
                //cmbProveedoresAnteriorMeth();
                loadFiltInfo();
            } else {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        loadAsyncFiltInfo();
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        }
    }//GEN-LAST:event_cmbProveedoresItemStateChanged
    private void cmbProveedoresAnteriorMeth() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                cmbTipo.removeAllItems();
                cmbTipo.addItem("Todas");

                loadSubtiposInfoCB();
                if (!listaSubtipos.isEmpty()) {

                    cmbTipo.removeAllItems();
                    loadTipos();
                    //filtrarSubtipos();
                    loadTbSubtipos();
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
        /*view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        filtrarSubtipos();
        loadTbSubtipos();
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);*/
    }
    private void btnDeleteRowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteRowsActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tbMntCuentas.getModel();
        int[] rows = tbMntCuentas.getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            model.removeRow(rows[i]);
        }
        this.btnHistory.setEnabled(this.listaCambiosSubtipos.size() > 0 ? true : false);
        this.lbTbSubTipos.setText("Total de filas: " + tbMntCuentas.getRowCount());
    }//GEN-LAST:event_btnDeleteRowsActionPerformed

    private void btnSaveGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveGroupActionPerformed
        // TODO add your handling code here:
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea ejecutar este cambio de subtipo?", "Sistema Facturacion", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.NO_OPTION) {
            //JOptionPane.showMessageDialog(rootPane, "Operacion cancelada.");
            this.jProgressBar1.setString("Proceso cancelado");
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            return;
        }
        saveChangesTbMantCuentas();
        this.btnHistory.setEnabled(this.listaCambiosSubtipos.size() > 0 ? true : false);
    }//GEN-LAST:event_btnSaveGroupActionPerformed
    private void setUpJprogressbar(int max) {
        this.jProgressBar1.setVisible(true);
        this.jProgressBar1.setValue(0);
        this.jProgressBar1.setMaximum(max);
    }

    private void saveChangesTbMantCuentas() {
        if (!this.selectedsubtipo.endsWith("00")) {
            int count = tbMntCuentas.getRowCount();
            setUpJprogressbar(count - 1);
            for (int i = 0; i < count; i++) {
                boolean selected = (boolean) tbMntCuentas.getValueAt(i, 12);
                if (selected) {
                    boolean res = saveRow(i);
                    if (res) {
                        tbMntCuentas.setValueAt(selectedsubtipo, i, 3);
                        tbMntCuentas.setValueAt(false, i, 12);
                    }
                }
                this.jProgressBar1.setValue(i + 1);
            }

            this.jProgressBar1.setString("Proceso completo");
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        } else {
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            JOptionPane.showMessageDialog(null, "Subtipo es una cuenta general");
        }
    }
    private void btnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistoryActionPerformed
        // TODO add your handling code here:
        view.util.JTableCommonFunctions.limpiarTabla(tbUndo);
        DefaultTableModel model = (DefaultTableModel) this.tbUndo.getModel();
        this.listaCambiosSubtipos.forEach(e -> {
            addRowSubtipoTbUndo(e, model);
        });
        this.lbTbUndoInfo.setText("Filas: " + tbUndo.getRowCount() + "  Cambios: " + this.listaCambiosSubtipos.size());
        this.jDialog1.setLocationRelativeTo(this);
        this.jDialog1.setVisible(true);
    }//GEN-LAST:event_btnHistoryActionPerformed

    private void btnDeleteTbUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTbUndoActionPerformed
        // TODO add your handling code here:
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea ejecutar este cambio de subtipo?", "Sistema Facturacion", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.NO_OPTION) {
            //JOptionPane.showMessageDialog(rootPane, "Operacion cancelada.");
            this.jProgressBar1.setString("Proceso cancelado");
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tbUndo.getModel();
        undoChanges();
        setViewAfterUndoChanges(model);
    }//GEN-LAST:event_btnDeleteTbUndoActionPerformed
    private void undoChanges() {
        int[] rows = tbUndo.getSelectedRows();

        for (int i : rows) {
            String idrow = tbUndo.getValueAt(i, 12).toString();
            System.out.println("idrow " + idrow);
            SubtiposExactus s = SubtiposExactus.getSubtipoPorIdRow(idrow, listaCambiosSubtipos);
            this.selectedsubtipo = s.getanteriorSubtipo();
            if (undoUpdate(s)) {
                listaCambiosSubtipos.remove(s);
                SubtiposExactus sub = SubtiposExactus.getSubtipoPorIdRow(idrow, listaSubtipos);
                sub.setDESCRIPCION(selectedsubtipo);

            }

        }
    }

    private void setViewAfterUndoChanges(DefaultTableModel model) {
        view.util.JTableCommonFunctions.limpiarTabla(tbUndo);
        this.listaCambiosSubtipos.forEach(e -> {
            addRowSubtipoTbUndo(e, model);
        });
        view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        filtrarSubtipos();
        loadTbSubtipos();
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        this.btnHistory.setEnabled(this.listaCambiosSubtipos.size() > 0 ? true : false);
        String cuenta = this.cmbCuentaPresupesto.getSelectedItem().toString().trim();
        if (cuenta.startsWith("02-99")) {
            this.selectedsubtipo = this.cmbCuentaPresupesto.getSelectedItem().toString().trim().substring(0, 11);
        } else {
            this.selectedsubtipo = this.cmbCuentaPresupesto.getSelectedItem().toString().trim().substring(3, 11);
        }
        this.lbTbUndoInfo.setText("Filas: " + this.tbUndo.getRowCount() + "  Cambios: " + listaCambiosSubtipos.size());
    }
    private void mnFiltProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnFiltProvActionPerformed
        // TODO add your handling code here:
        int row = tbMntCuentas.getSelectedRow();
        if (row > -1) {
            String rowindx = tbMntCuentas.getValueAt(row, 11).toString();
            SubtiposExactus s = SubtiposExactus.getSubtipoPorIdRow(rowindx, listaSubtipos);
            cmbProveedores.setSelectedItem(s.getNombre_Proveedor());
        }
    }//GEN-LAST:event_mnFiltProvActionPerformed

    private void cmbMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            if (option == 2) {
                loadFiltInfo();
                //cmbMonedaAnteriorMeth();
            } else {
                loadAsyncFiltInfo();
            }
        }
    }//GEN-LAST:event_cmbMonedaItemStateChanged
    private void loadAsyncFiltInfo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);

                loadSubtiposInfo();
                if (!listaSubtipos.isEmpty()) {
                    loadTbSubtipos();
                }
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void loadFiltInfo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                cmbTipo.removeAllItems();
                cmbTipo.addItem("Todas");

                loadSubtiposInfoCB();
                if (!listaSubtipos.isEmpty()) {

                    cmbTipo.removeAllItems();
                    loadTipos();
                    //filtrarSubtipos();
                    loadTbSubtipos();
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void cmbMonedaAnteriorMeth() {

        view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        filtrarSubtipos();
        loadTbSubtipos();
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
    }
    private void cmbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            /*view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        filtrarSubtipos();
        loadTbSubtipos();
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);*/
            filtBycmbTipo();
        }
    }//GEN-LAST:event_cmbTipoItemStateChanged
    private void filtBycmbTipo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                loadSubtiposInfoCB();
                if (!listaSubtipos.isEmpty()) {

                    loadTbSubtipos();
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    private void btnBuscProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscProvActionPerformed
        // TODO add your handling code here:
        buscarSubtiposPorProveedor();
    }//GEN-LAST:event_btnBuscProvActionPerformed

    private void btnExportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportExcelActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(this.guardarExcel);
        t.start();
    }//GEN-LAST:event_btnExportExcelActionPerformed
    /**
     * this method use the typed text in txtAsiento, so to look for the
     * registers that its asiento property match with it, then show the result
     * on the tbMntCuentas
     */
    private void buscarSubtipos() {
        this.txtProveedor.setText("");
        view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        String asiento = this.txtAsiento.getText();
        if (asiento != null && !asiento.equals("")) {
            this.listaSubtiposFilt = SubtiposExactus.getSubtipoPorAsiento(asiento, listaSubtipos);
            loadTbSubtipos(this.listaSubtiposFilt);
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        } else {
            filtBycmbTipo();
            //JOptionPane.showMessageDialog(null, "Valor " + asiento + " no es correcto");
        }
    }

    /**
     * this method use the typed text in txtProveedor, so to look for the
     * registers that its proveedor property match with it, then show the result
     * on the tbMntCuentas
     */
    private void buscarSubtiposPorProveedor() {
        this.txtAsiento.setText("");
        view.util.JTableCommonFunctions.limpiarTabla(tbMntCuentas);
        String prov = this.txtProveedor.getText();
        if (prov != null && !prov.equals("")) {
            this.listaSubtiposFilt = SubtiposExactus.getSubtipoPorProvedor(prov, listaSubtipos);
            loadTbSubtipos(this.listaSubtiposFilt);
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        } else {
            filtBycmbTipo();
            //JOptionPane.showMessageDialog(null, "Valor " + prov + " no es correcto");
        }
    }

    /**
     * this function receives a departamento object, then takes the
     */
    private ArrayList<Presupuesto> getSubCuentaPresupuesto(Departamento d) {
        /*ArrayList<Presupuesto> ctaP = new ArrayList<>();
        for (int i = 0; i < listaPresupuesto.size(); i++) {
            Presupuesto pres = listaPresupuesto.get(i);
            String iddepartamento = pres.getCODDEPA();// pres.getCONCEPATOADETALLE().substring(3, 5);
            if (iddepartamento.equalsIgnoreCase(d.getDEPARTAMENTO())) {
                ctaP.add(pres);
            }

        }*/
        ArrayList<Presupuesto> ctaP = crpre.obtenerPresupuestoPorDep(d.getDEPARTAMENTO());
        return ctaP;
        //return getCmbCuentas(ctaP);
    }

    /**
     * this method calls database to obtain the information that is requested by
     * the user, then it puts the founded information to the GUI
     */
    private void obtenerSubtipos() {
        java.util.Date inicio = jdtInicio.getDate();
        java.util.Date fin = jdFin.getDate();
        if (inicio != null && fin != null) {
            this.listaCambiosSubtipos = new ArrayList<>();
            this.btnHistory.setEnabled(false);
            data.CRUDSubtiposExactus cex = new data.CRUDSubtiposExactus();

            switch (option) {
                case 1:
                    this.listaSubtipos = cex.obtenerSubtiposCPPorFechas(inicio, fin);// cuentas por pagar
                    break;
                case 2:
                    this.listaSubtipos = cex.obtenerSubtiposCBPorFechas(inicio, fin); //cuentas bancarias
                    break;
                case 3:
                    this.listaSubtipos = cex.obtenerSubtiposCGPorFechas(inicio, fin); //cuentas contabilidad
                    break;

            }
            if (!this.listaSubtipos.isEmpty()) {

                //JOptionPane.showMessageDialog(null, "subtipos " + listaSubtipos.size());
                cmbProveedores.removeAllItems();
                loadCmbProveedores();
                loadTipos();
                filtrarSubtipos();
                loadTbSubtipos();
            }
        }
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        this.lbTbSubTipos.setText("Total de filas: " + tbMntCuentas.getRowCount());
    }

    /**
     * this method calls database to obtain the information that is requested by
     * the user, then it puts the founded information to the GUI
     */
    private void selectOptionSubtipos() {

        this.txtAsiento.setText("");
        this.txtProveedor.setText("");
        String cia = this.cmbCIA.getSelectedItem().toString();
        if (cia.equals("TODAS")) {
            cia = "";
        }
        String prov = this.cmbProveedores.getSelectedItem().toString();
        if (prov.equals("Todas")) {
            prov = "";
        }
        String moneda = this.cmbMoneda.getSelectedItem().toString();
        if (moneda.equals("TODAS")) {
            moneda = "";
        }
        String tipo = this.cmbTipo.getSelectedItem().toString();
        if (tipo.equals("Todas")) {
            tipo = "";
        }
        boolean sinsubt = this.chkSinSub.isSelected();
        java.util.Date inicio = jdtInicio.getDate();
        java.util.Date fin = jdFin.getDate();
        if (inicio != null && fin != null) {
            this.listaCambiosSubtipos = new ArrayList<>();
            this.btnHistory.setEnabled(false);
            data.CRUDSubtiposExactus cex = new data.CRUDSubtiposExactus();

            switch (option) {
                case 1:
                    this.listaSubtipos = cex.obtenerSubtiposCP(inicio, fin, cia, prov, moneda, sinsubt);// cuentas por pagar

                    break;
                case 2:
                    this.listaSubtipos = cex.obtenerSubtiposCB(inicio, fin, cia, prov, moneda, tipo, sinsubt); //cuentas bancarias
                    break;
                case 3:
                    this.listaSubtipos = cex.obtenerSubtiposCGPorFechas(inicio, fin); //cuentas contabilidad
                    break;

            }

        }
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        this.lbTbSubTipos.setText("Total de filas: " + tbMntCuentas.getRowCount());
    }

    /**
     * this method loads all proveedores to the cmbProveedores, the proveedores
     * item can't be more than one time in the cmbProveedores
     */
    /*private void loadCmbProveedores() {
        this.proveedores = new ArrayList<>();

        this.listaSubtipos.forEach(e -> {

            if (!proveedores.contains(e.getNombre_Proveedor())) {
                proveedores.add(e.getNombre_Proveedor());

            }
        });
        Collections.sort(proveedores);
        cmbProveedores.addItem("Todas");
        proveedores.forEach(e -> {
            cmbProveedores.addItem(e);
        });
    }*/
    /**
     * this method loads all proveedores to the cmbProveedores, the proveedores
     * item can't be more than one time in the cmbProveedores
     */
    private void loadCmbProveedores() {
        this.proveedores = new ArrayList<>();
        cmbProveedores.removeAllItems();
        this.listaSubtipos.forEach(e -> {

            if (!proveedores.contains(e.getNombre_Proveedor())) {
                proveedores.add(e.getNombre_Proveedor());

            }
        });
        Collections.sort(proveedores);
        cmbProveedores.addItem("Todas");
        proveedores.forEach(e -> {
            cmbProveedores.addItem(e);
        });
    }

    /**
     * this method loads all Tipos to the cmbTipos, the tipo item can't be more
     * than one time in the cmbTipos
     */
    private void loadTipos() {
        this.tipos = new ArrayList<>();

        this.listaSubtipos.forEach(e -> {

            if (!tipos.contains(e.getTIPO())) {
                tipos.add(e.getTIPO());

            }
        });
        Collections.sort(tipos);
        if (cmbTipo.getItemAt(0) == null) {
            cmbTipo.addItem("Todas");
        }
        tipos.forEach(e -> {
            cmbTipo.addItem(e);
        });
    }

    /**
     * this method loads all proveedores, this when match the selected value on
     * the cmbCIA, to the cmbProveedores, the proveedores item can't be more
     * than one time in the cmbProveedores
     */
    private void loadCIAProveedores() {
        this.proveedores = new ArrayList<>();
        String cia = this.cmbCIA.getSelectedItem().toString();
        this.listaSubtipos.forEach(e -> {

            if (!proveedores.contains(e.getNombre_Proveedor())) {
                if (e.getSociedad().toUpperCase().contains(cia)) {
                    proveedores.add(e.getNombre_Proveedor());
                } else if (cia.equalsIgnoreCase("todas")) {
                    proveedores.add(e.getNombre_Proveedor());
                }

            }
        });
        Collections.sort(proveedores);
        cmbProveedores.addItem("Todas");
        proveedores.forEach(e -> {
            cmbProveedores.addItem(e);
        });
    }

    /**
     * this function receives a ArrayList<SubtiposExactus> list, then loads a
     * ArrayList<SubtiposExactus> response list, with the first list object when
     * the object description is a number after deleting the '-' chars from it
     *
     * @param lista is a ArrayList<SubtiposExactus> list
     * @return a filtered ArrayList<SubtiposExactus>
     */
    private ArrayList<SubtiposExactus> obtenerSinSubt(ArrayList<SubtiposExactus> lista) {
        ArrayList<SubtiposExactus> res = new ArrayList<>();
        lista.forEach(e -> {
            String subtipodesc = e.getDESCRIPCION().replace("-", "");
            if (!isNumber(subtipodesc)) {
                res.add(e);
            }

        });
        return res;
    }

    /**
     * this function receives a ArrayList<SubtiposExactus> list, and a string
     * currency, then loads a ArrayList<SubtiposExactus> response list, with the
     * first list object when the property sociedad is equals to the parameter
     * cia
     *
     * @param lista is a ArrayList<SubtiposExactus> list
     * @param cia is a string containing the filtering parameter
     * @return a filtered ArrayList<SubtiposExactus>
     */
    private ArrayList<SubtiposExactus> obtnerlistaPorCIA(ArrayList<SubtiposExactus> lista, String cia) {

        ArrayList<SubtiposExactus> res = new ArrayList<>();
        lista.forEach(e -> {
            if (e.getSociedad().toUpperCase().contains(cia)) {
                res.add(e);
            }

        });
        return res;
    }

    /**
     * this function receives a ArrayList<SubtiposExactus> list, and a string
     * currency, then loads a ArrayList<SubtiposExactus> response list, with the
     * first list object when the property proveedor is equals to the parameter
     * proveedor
     *
     * @param lista is a ArrayList<SubtiposExactus> list
     * @param proveedor is a string containing the filtering parameter
     * @return a filtered ArrayList<SubtiposExactus>
     */
    private ArrayList<SubtiposExactus> obtnerlistaPorProveedor(ArrayList<SubtiposExactus> lista, String proveedor) {

        ArrayList<SubtiposExactus> res = new ArrayList<>();
        lista.forEach(e -> {
            if (e.getNombre_Proveedor().toUpperCase().contains(proveedor)) {
                res.add(e);
            }

        });
        return res;
    }

    /**
     * this function receives a ArrayList<SubtiposExactus> list, and a string
     * currency, then loads a ArrayList<SubtiposExactus> response list, with the
     * first list object when the property moneda is equals to the parameter
     * moneda
     *
     * @param lista is a ArrayList<SubtiposExactus> list
     * @param moneda is a string containing the filtering parameter
     * @return a filtered ArrayList<SubtiposExactus>
     */
    private ArrayList<SubtiposExactus> obtnerlistaPorMoneda(ArrayList<SubtiposExactus> lista, String moneda) {

        ArrayList<SubtiposExactus> res = new ArrayList<>();
        lista.forEach(e -> {
            if (e.getMoneda().equalsIgnoreCase(moneda)) {
                res.add(e);
            }

        });
        return res;
    }

    /**
     * this function receives a ArrayList<SubtiposExactus> list, and a string
     * tipo, then loads a ArrayList<SubtiposExactus> response list, with the
     * first list object when the property tipo is equals to the parameter tipo
     *
     * @param lista is a ArrayList<SubtiposExactus> list
     * @param tipo is a string containing the filtering parameter
     * @return a filtered ArrayList<SubtiposExactus>
     */
    private ArrayList<SubtiposExactus> obtnerlistaPorTipo(ArrayList<SubtiposExactus> lista, String tipo) {

        ArrayList<SubtiposExactus> res = new ArrayList<>();
        lista.forEach(e -> {
            if (e.getTIPO().equalsIgnoreCase(tipo)) {
                res.add(e);
            }

        });
        return res;
    }

    /**
     * this method handles the process of filtering the information to show on
     * the table
     */
    private void filtrarSubtipos() {
        try {
            this.listaSubtiposFilt = new ArrayList<>();

            String cia = this.cmbCIA.getSelectedItem().toString().toUpperCase().trim();
            String proveedor = this.cmbProveedores.getSelectedItem().toString().toUpperCase().trim();
            String moneda = this.cmbMoneda.getSelectedItem().toString().toUpperCase().trim();
            String tipo = this.cmbTipo.getSelectedItem().toString().toUpperCase().trim();
            ArrayList<SubtiposExactus> lista = this.listaSubtipos;
            boolean chkSinSub = this.chkSinSub.isSelected();
            if (!cia.equalsIgnoreCase("TODAS")) {
                lista = obtnerlistaPorCIA(lista, cia);
            }
            if (!proveedor.equalsIgnoreCase("Todas")) {
                lista = obtnerlistaPorProveedor(lista, proveedor);
            }
            if (!moneda.equalsIgnoreCase("Todas")) {
                lista = obtnerlistaPorMoneda(lista, moneda);
            }
            if (option == 2) {
                if (!tipo.equalsIgnoreCase("Todas")) {
                    lista = obtnerlistaPorTipo(lista, tipo);
                }
            }
            if (chkSinSub) {
                lista = obtenerSinSubt(lista);
            }
            this.listaSubtiposFilt = lista;
        } catch (Exception e) {
            System.err.println("view.MantenimientoSubtiposExactus.filtrarSubtipos() error " + e.getMessage());
        }
    }

    private void loadTbSubtipos() {
        prepareToShowProgressBar(0, listaSubtipos.size(), 0, "Cargando información...");
        DefaultTableModel model = (DefaultTableModel) this.tbMntCuentas.getModel();
        int count = 0;
        for (SubtiposExactus e : listaSubtipos) {
            addRowTbSubtipo(e, model);
            jProgressBar1.setValue(count + 1);
            count++;
        }

        this.lbTbSubTipos.setText("Total de filas: " + tbMntCuentas.getRowCount());
    }

    private void loadTbSubtipos(ArrayList<SubtiposExactus> lista) {
        prepareToShowProgressBar(0, lista.size(), 0, "Cargando información...");
        DefaultTableModel model = (DefaultTableModel) this.tbMntCuentas.getModel();
        int count = 0;
        for (SubtiposExactus e : lista) {
            addRowTbSubtipo(e, model);
            jProgressBar1.setValue(count + 1);
            count++;
        }

        this.lbTbSubTipos.setText("Total de filas: " + tbMntCuentas.getRowCount());
    }

    /**
     * this function receives a string like number, if the content of the string
     * is in fact a number returns a true boolean value, else a false
     *
     * @param stringNumber is a string containing a number
     * @return a Boolean value
     */
    private boolean isNumber(String stringNumber) {
        boolean number = false;
        try {
            Integer.parseInt(stringNumber);
            number = true;
        } catch (Exception e) {
            System.err.println("view.MantenimientoCuentasExactus.isNumber() error " + e.getMessage());
        }
        return number;
    }

    /**
     * this method add a row to the table model received as a parameter, this
     * with the information given in the SubtiposExactus rec also receive as a
     * parameter
     */
    private void addRowTbSubtipo(SubtiposExactus rec, DefaultTableModel model) {
        model.addRow(new Object[]{
            rec.getSociedad(),
            rec.getTIPO(),
            rec.getSUBTIPO(),
            rec.getDESCRIPCION(),
            rec.getNombre_Proveedor(),
            rec.getDOCUMENTO(),
            rec.getFECHA_DOCUMENTO(),
            rec.getAPLICACION(),
            logic.AppStaticValues.numberFormater.format(rec.getMONTO()),
            logic.AppStaticValues.numberFormater.format(rec.getMONTO_DOLAR()),
            rec.getASIENTO(),
            rec.getIdRow(),
            false
        });
    }

    /**
     * this method add a row to the table model received as a parameter, this
     * with the information given in the SubtiposExactus rec also receive as a
     * parameter
     */
    private void addRowSubtipoTbUndo(SubtiposExactus rec, DefaultTableModel model) {
        model.addRow(new Object[]{
            rec.getSociedad(),
            rec.getTIPO(),
            rec.getSUBTIPO(),
            rec.getanteriorSubtipo(),
            rec.getDESCRIPCION(),
            rec.getNombre_Proveedor(),
            rec.getDOCUMENTO(),
            rec.getFECHA_DOCUMENTO(),
            rec.getAPLICACION(),
            logic.AppStaticValues.numberFormater.format(rec.getMONTO()),
            logic.AppStaticValues.numberFormater.format(rec.getMONTO_DOLAR()),
            rec.getASIENTO(),
            rec.getIdRow()
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MantenimientoCuentasExactus.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoCuentasExactus.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoCuentasExactus.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoCuentasExactus.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MantenimientoCuentasExactus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscProv;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnDeleteRows;
    private javax.swing.JButton btnDeleteTbUndo;
    private javax.swing.JButton btnExportExcel;
    private javax.swing.JButton btnHistory;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSaveGroup;
    private javax.swing.JPanel center;
    private javax.swing.JCheckBox chkSinSub;
    private javax.swing.JComboBox<String> cmbCIA;
    private javax.swing.JComboBox<String> cmbCuentaPresupesto;
    private javax.swing.JComboBox<String> cmbDepartamento;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbProveedores;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JPanel down;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser jdFin;
    private com.toedter.calendar.JDateChooser jdtInicio;
    private javax.swing.JLabel lbTbSubTipos;
    private javax.swing.JLabel lbTbUndoInfo;
    private javax.swing.JLabel lbTipo;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbtxProv;
    private javax.swing.JPanel left;
    private javax.swing.JMenuItem mnFiltProv;
    private javax.swing.JPanel north;
    private javax.swing.JPanel rigth;
    private javax.swing.JTable tbMntCuentas;
    private javax.swing.JTable tbUndo;
    private javax.swing.JTextField txtAsiento;
    private javax.swing.JTextField txtProveedor;
    // End of variables declaration//GEN-END:variables

}

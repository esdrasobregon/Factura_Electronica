package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import data.DataUser;
import entitys.AbonoSugeridoContado;
import entitys.Departamento;
import entitys.HistoricoCP;
import entitys.Presupuesto;
import entitys.ProveedorContado.ProveedorContado;
import entitys.TipoCambio;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import logic.AppStaticValues;
import logic.util.FileHandler;
import view.util.JTableCommonFunctions;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
/**
 *
 * @author eobregon
 */
public class MantHistoricoCPContado extends javax.swing.JPanel {

    /**
     * Creates new form MantHistoricoCPContado
     */
    ArrayList<Departamento> listaDepartamentos;
    ArrayList<Presupuesto> listaPresupuesto;
    ArrayList<HistoricoCP> listaHistoricos;
    boolean loadingInfo = false;
    data.CrudPresupuesto crudp;
    data.CRUDHistoricoCP crh;
    data.CrudHistoricoContado crhc;
    private TipoCambio tipoc;
    private int currentWeek;
    HistoricoCP currentHcp;
    ArrayList<entitys.ProveedorContado.ProveedorContado> listaProv;
    data.CrudProvContado.CrudProvedorContado crd;
    File selectedFile;
    data.CRUDAbonoSugeridoContado crdL = new data.CRUDAbonoSugeridoContado();

    public MantHistoricoCPContado(ArrayList<Departamento> listaDepartamentos, ArrayList<Presupuesto> listaPresupuesto) {
        initComponents();
        this.listaDepartamentos = listaDepartamentos;
        this.crudp = new data.CrudPresupuesto();
        this.crh = new data.CRUDHistoricoCP();
        this.crhc = new data.CrudHistoricoContado();
        crd = new data.CrudProvContado.CrudProvedorContado();
        prepareGUI();
    }

    private void prepareGUI() {
        loadInfo();
        setView();

    }

    private void loadInfo() {
        this.listaHistoricos = crh.obtenerHistoricoCPContado_View("");
        obtTipoCambio();
        refreshInfo();

    }

    private void setView() {
        /*this.btnProveedorContado.setVisible(false);
        this.btnProveedorContado.setEnabled(false);*/
        jProgressBar1.setVisible(false);
        this.mnEdit.setEnabled(false);
        this.btnSave.setVisible(false);
        this.btnSave.setEnabled(false);
        setDatesView();
        java.util.Date date = new java.util.Date();
        this.currentWeek = logic.CommonDateFunctions.getWeekOfTheYear(date);
        this.pnlVen.setVisible(false);
        this.pnlTP.setVisible(false);
        this.loadingInfo = true;
        this.listaDepartamentos.forEach(e -> {
            if (!e.getDescripcion().equals("Ingresos")) {
                cmbDepartamento.addItem(e.getDEPARTAMENTO() + "-" + e.getDescripcion());
            }
        });
        loadCtas();
        this.txtNumProv.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {
                    getProveedor();
                }
            }
        });
        this.txtNomProv.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {
                    txtBuscarNombre.setText(txtNomProv.getText());
                    jDialog1.setLocationRelativeTo(null);
                    jDialog1.setVisible(true);
                }
            }
        });
        this.txtMonto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                checkTC();
                String moneda = cmbMoneda.getSelectedItem().toString();
                if ((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB)) {
                    if (moneda.equals("CRC")) {
                        txtMontoC.setText(txtMonto.getText());
                    } else {
                        try {
                            if (tipoc == null) {
                                obtTipoCambio();
                                if (tipoc == null) {
                                    JOptionPane.showMessageDialog(null, "El tipo de cambio no esta listo", "Atención", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }
                            double saldo = Double.parseDouble(txtMonto.getText());
                            double res = tipoc.getCompra() * saldo;
                            txtMontoC.setText(String.format("%.2f", res).replace(",", "."));
                        } catch (Exception ex) {
                            txtMontoC.setText("");
                        }
                    }

                }
            }
        });
        this.txtAbono.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                checkTC();
                String moneda = cmbMoneda.getSelectedItem().toString();
                if ((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB)) {

                    if (moneda.equals("CRC")) {
                        txtAbonoC.setText(txtAbono.getText());
                    } else {
                        try {
                            if (tipoc == null) {
                                obtTipoCambio();
                                if (tipoc == null) {
                                    JOptionPane.showMessageDialog(null, "El tipo de cambio no esta listo", "Atención", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                            }
                            double saldo = Double.parseDouble(txtAbono.getText());
                            double res = saldo * tipoc.getCompra();
                            txtAbonoC.setText(String.format("%.2f", res).replace(",", "."));
                        } catch (Exception ex) {
                            txtAbonoC.setText("");
                        }

                    }
                }
            }
        });
        txtNomProv.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    getProveedor();
                }
            }
        });
        this.tbHistoricoCP.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    resumirInfoColumns();
                }
            }
        }
        );
        this.txtBuscarNombre.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                serchRegisters();

            }

            public void removeUpdate(DocumentEvent e) {
                serchRegisters();
            }

            public void insertUpdate(DocumentEvent e) {
                serchRegisters();
            }

        });
        setTbProveedoresEvents();
        setTbHistoricoEvents();
        this.loadingInfo = false;
    }

    private void setTbProveedoresEvents() {
        tbProveedor.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                int row = tbProveedor.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && row > -1 && !loadingInfo) {
                    //JOptionPane.showMessageDialog(null, "fila seleccionada");
                    String id = tbProveedor.getValueAt(row, 0).toString();
                    String nombre = (String) tbProveedor.getValueAt(row, 1);
                    txtNumProv.setText(id);
                    txtNomProv.setText(nombre);
                    jDialog1.setVisible(false);
                }

            }

        });
    }

    private void serchRegisters() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                listaProv = crd.obtenerListaProveedorContado(txtBuscarNombre.getText());
                DefaultTableModel model = (DefaultTableModel) tbProveedor.getModel();
                JTableCommonFunctions.limpiarTabla(tbProveedor);
                listaProv.forEach(e -> {
                    addRowProveedor(model, e);
                });
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
        //JOptionPane.showConfirmDialog(rootPane, "INFO " + txtBuscarNombre.getText());
    }

    private void addRowProveedor(DefaultTableModel model, ProveedorContado e) {
        model.addRow(new Object[]{
            e.getIdProveedorContado(),
            e.getNombre(),
            e.getCodigo(),
            e.getCedulaJuridica(),
            e.getTotalCuentas(),
            e.getSinpes()
        });
    }

    private void setTbHistoricoEvents() {
        this.tbHistoricoCP.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (tbHistoricoCP.getSelectedRowCount() > 1) {
                        mnEdit.setEnabled(false);
                        mnAbrirAdjuntos.setEnabled(false);
                        //mnAgAb.setEnabled(false);
                        mnAbCant.setEnabled(false);
                    } else {
                        mnEdit.setEnabled(true);
                        mnAbrirAdjuntos.setEnabled(true);
                        //mnAgAb.setEnabled(true);
                        mnAbCant.setEnabled(true);
                    }
                    int row = tbHistoricoCP.rowAtPoint(e.getPoint());
                    tbHistoricoCP.addRowSelectionInterval(row, row);
                    jPopupMenu1.show(tbHistoricoCP, e.getX(), e.getY());
                } else {
                    currentHcp = null;
                    btnSave.setVisible(false);
                    btnSave.setEnabled(false);
                    btnAdd.setEnabled(true);

                    btnAtth.setText("");
                    refreshForm();
                }
            }
        });
    }

    private void getProveedor() {
        /*data.CrudProveedor crp = new data.CrudProveedor();
        String numProv = this.txtNumProv.getText();
        String cia = this.cmbCiaForm.getSelectedItem().toString();
        Proveedor p = crp.getProveedor(cia + "-" + numProv);
        if (p.getNombre() != null && !p.getNombre().isEmpty()) {
            this.txtNomProv.setText(p.getNombre());
        } else {
            this.txtNomProv.setText("");
        }*/
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                try {
                    int id = Integer.parseInt(txtNumProv.getText());
                    entitys.ProveedorContado.ProveedorContado p = crd.obtenerListaProveedorContadoPorId(id);
                    JTableCommonFunctions.limpiarTabla(tbProveedor);
                    if (p == null) {
                        txtNomProv.setText("");
                        JOptionPane.showMessageDialog(null, "No existe ese número de proveedor", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        txtNomProv.setText(p.getNombre());
                    }
                } catch (Exception e) {
                    txtNomProv.setText("");
                    System.out.println(".run() error " + e.getMessage());
                }
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();

    }

    private void checkTC() {
        if (tipoc == null) {
            data.CRUDTipoCambio cr = new data.CRUDTipoCambio();
            tipoc = cr.obtenerTipoCambioPorFecha(new java.util.Date());
        }
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
        mnEdit = new javax.swing.JMenuItem();
        mnAbrirAdjuntos = new javax.swing.JMenuItem();
        mnAgAb = new javax.swing.JMenuItem();
        mnAbCant = new javax.swing.JMenuItem();
        mnDeleteAb = new javax.swing.JMenuItem();
        jDialog1 = new javax.swing.JDialog();
        jPanel33 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbProveedor = new javax.swing.JTable();
        jPanel34 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtBuscarNombre = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jDialog2 = new javax.swing.JDialog();
        jPanel43 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbCiaForm = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dtFechDoc = new com.toedter.calendar.JDateChooser();
        pnlVen = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        dtVencimiento = new com.toedter.calendar.JDateChooser();
        jPanel16 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtNumProv = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtNomProv = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        btnProveedorContado = new javax.swing.JButton();
        pnlTP = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbTipoProv = new javax.swing.JComboBox<>();
        jPanel39 = new javax.swing.JPanel();
        pnlMoneda = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtMonto = new javax.swing.JTextField();
        pnlPagar = new javax.swing.JPanel();
        javax.swing.JLabel Saldo = new javax.swing.JLabel();
        txtAbono = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        javax.swing.JLabel Saldo1 = new javax.swing.JLabel();
        txtMontoC = new javax.swing.JTextField();
        pnlPagarColones = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtAbonoC = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbDepartamento = new javax.swing.JComboBox<>();
        jPanel40 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbCuentaPres = new javax.swing.JComboBox<>();
        jPanel28 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        cmbTipoDocumento = new javax.swing.JComboBox<>();
        jPanel42 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        cmbFormaPago = new javax.swing.JComboBox<>();
        jPanel27 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtNotas = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        ckAdelanto = new javax.swing.JCheckBox();
        btnAtth = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jPanel48 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblineasPC = new javax.swing.JTable();
        jPanel52 = new javax.swing.JPanel();
        btnDeleteAbono = new javax.swing.JButton();
        jPanel53 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        btnAbrirForm = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel55 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        cmbFiltroSaldo = new javax.swing.JComboBox<>();
        pnlSplit = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbHistoricoCP = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        lbTbInfo = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResumen = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel54 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        lbInicio = new javax.swing.JLabel();
        dtFechaIni = new com.toedter.calendar.JDateChooser();
        Inicio1 = new javax.swing.JLabel();
        dtFin = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cmbFiltCia = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        mnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        mnEdit.setText("Editar");
        mnEdit.setToolTipText("Editar el documento");
        mnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEditActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnEdit);

        mnAbrirAdjuntos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/attach-24.png"))); // NOI18N
        mnAbrirAdjuntos.setText("Abrir archivos adjuntos");
        mnAbrirAdjuntos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAbrirAdjuntosActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnAbrirAdjuntos);

        mnAgAb.setText("Abonar el saldo");
        mnAgAb.setToolTipText("Aplicar abonos al saldo total");
        mnAgAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAgAbActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnAgAb);

        mnAbCant.setText("Abonar (ingresar cantidad)");
        mnAbCant.setToolTipText("Ingresar un monto para abonar");
        mnAbCant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAbCantActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnAbCant);

        mnDeleteAb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_round_icon_20x20.png"))); // NOI18N
        mnDeleteAb.setText("Eliminar abono");
        mnDeleteAb.setToolTipText("Elimina abonos (sin revisar por contabilidad)");
        mnDeleteAb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteAbActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnDeleteAb);

        jDialog1.setTitle("Buscar proveedor");
        jDialog1.setMinimumSize(new java.awt.Dimension(912, 408));
        jDialog1.setModal(true);

        jPanel33.setLayout(new java.awt.GridLayout(1, 0));

        tbProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "Nombre", "Codigo", "Cedula juridica", "Cuentas", "SINPE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbProveedor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbProveedor.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbProveedor);
        if (tbProveedor.getColumnModel().getColumnCount() > 0) {
            tbProveedor.getColumnModel().getColumn(0).setMinWidth(0);
            tbProveedor.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbProveedor.getColumnModel().getColumn(0).setMaxWidth(0);
            tbProveedor.getColumnModel().getColumn(1).setPreferredWidth(300);
            tbProveedor.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbProveedor.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbProveedor.getColumnModel().getColumn(4).setPreferredWidth(100);
            tbProveedor.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        jPanel33.add(jScrollPane3);

        jDialog1.getContentPane().add(jPanel33, java.awt.BorderLayout.CENTER);

        jPanel34.setPreferredSize(new java.awt.Dimension(912, 30));
        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Ingrese nombre proveedor");
        jPanel34.add(jLabel17);

        txtBuscarNombre.setPreferredSize(new java.awt.Dimension(300, 25));
        jPanel34.add(txtBuscarNombre);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel34.add(jButton1);

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel18.setToolTipText("Doble click en la fila para seleccionar");
        jPanel34.add(jLabel18);

        jDialog1.getContentPane().add(jPanel34, java.awt.BorderLayout.PAGE_START);

        jPanel35.setPreferredSize(new java.awt.Dimension(912, 30));

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 912, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel35, java.awt.BorderLayout.PAGE_END);

        jPanel36.setPreferredSize(new java.awt.Dimension(10, 231));

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel36, java.awt.BorderLayout.LINE_END);

        jPanel37.setPreferredSize(new java.awt.Dimension(10, 231));

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel37, java.awt.BorderLayout.LINE_START);

        jDialog2.setTitle("Formulario de pagos de contado");
        jDialog2.setMinimumSize(new java.awt.Dimension(1084, 600));
        jDialog2.getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        jPanel43.setMinimumSize(new java.awt.Dimension(900, 78));
        jPanel43.setPreferredSize(new java.awt.Dimension(900, 360));
        jPanel43.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Favor ingrese la información", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel7.setMaximumSize(new java.awt.Dimension(700, 32767));
        jPanel7.setPreferredSize(new java.awt.Dimension(700, 260));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel7.setLayout(flowLayout1);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("CIA");
        jLabel7.setToolTipText("");
        jPanel8.add(jLabel7);

        cmbCiaForm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RYMSA", "CILT", "TURINTEL" }));
        cmbCiaForm.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCiaFormItemStateChanged(evt);
            }
        });
        jPanel8.add(cmbCiaForm);

        jPanel7.add(jPanel8);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Documento");
        jPanel9.add(jLabel12);

        txtDocumento.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel9.add(txtDocumento);

        jPanel7.add(jPanel9);
        jPanel7.add(jPanel10);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Fecha documento");
        jPanel22.add(jLabel5);

        dtFechDoc.setDateFormatString("dd-MM-yyyy");
        dtFechDoc.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel22.add(dtFechDoc);

        jPanel20.add(jPanel22);

        jPanel7.add(jPanel20);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Vencimiento");
        pnlVen.add(jLabel6);

        dtVencimiento.setDateFormatString("dd-MM-yyyy");
        dtVencimiento.setPreferredSize(new java.awt.Dimension(120, 25));
        pnlVen.add(dtVencimiento);

        jPanel7.add(pnlVen);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("N proveedor");
        jPanel38.add(jLabel8);

        txtNumProv.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel38.add(txtNumProv);

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel20.setToolTipText("Ingrese el Id del proveedor, luego enter");
        jPanel38.add(jLabel20);

        jPanel16.add(jPanel38);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Nombre proveedor");
        jPanel15.add(jLabel9);

        txtNomProv.setPreferredSize(new java.awt.Dimension(300, 25));
        jPanel15.add(txtNomProv);

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel19.setToolTipText("Pulse enter para escoger proveedor");
        jPanel15.add(jLabel19);

        jPanel16.add(jPanel15);

        btnProveedorContado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/provider-25.png"))); // NOI18N
        btnProveedorContado.setToolTipText("Abrir mantenimiento de proveedores");
        btnProveedorContado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorContadoActionPerformed(evt);
            }
        });
        jPanel16.add(btnProveedorContado);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Tipo prov");
        pnlTP.add(jLabel1);

        cmbTipoProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LOC", "CONS" }));
        pnlTP.add(cmbTipoProv);

        jPanel16.add(pnlTP);

        jPanel7.add(jPanel16);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Moneda");
        pnlMoneda.add(jLabel2);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CRC", "USD" }));
        cmbMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaItemStateChanged(evt);
            }
        });
        pnlMoneda.add(cmbMoneda);

        jPanel39.add(pnlMoneda);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Monto del documento");
        jPanel14.add(jLabel11);

        txtMonto.setToolTipText("Pulse techa enter para llenar monto en colones");
        txtMonto.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel14.add(txtMonto);

        jPanel39.add(jPanel14);

        Saldo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Saldo.setText("Pagar");
        pnlPagar.add(Saldo);

        txtAbono.setPreferredSize(new java.awt.Dimension(100, 25));
        pnlPagar.add(txtAbono);

        jPanel39.add(pnlPagar);

        Saldo1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Saldo1.setText("Monto ₡");
        jPanel12.add(Saldo1);

        txtMontoC.setToolTipText("Pulse techa enter para llenar monto en colones");
        txtMontoC.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel12.add(txtMontoC);

        jPanel39.add(jPanel12);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Pagar ₡");
        pnlPagarColones.add(jLabel13);

        txtAbonoC.setPreferredSize(new java.awt.Dimension(100, 25));
        pnlPagarColones.add(txtAbonoC);

        jPanel39.add(pnlPagarColones);

        jPanel7.add(jPanel39);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Departamento");
        jPanel18.add(jLabel3);

        cmbDepartamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepartamentoItemStateChanged(evt);
            }
        });
        jPanel18.add(cmbDepartamento);

        jPanel41.add(jPanel18);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Cta presupuesto");
        jPanel40.add(jLabel4);

        jPanel40.add(cmbCuentaPres);

        jPanel41.add(jPanel40);

        jPanel17.add(jPanel41);

        jPanel7.add(jPanel17);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Tipo documento");
        jPanel28.add(jLabel14);

        cmbTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Factura", "Proforma" }));
        cmbTipoDocumento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoDocumentoItemStateChanged(evt);
            }
        });
        jPanel28.add(cmbTipoDocumento);

        jPanel7.add(jPanel28);

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Forma de pago");
        jPanel42.add(jLabel21);

        cmbFormaPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiempo real", "T+1" }));
        cmbFormaPago.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFormaPagoItemStateChanged(evt);
            }
        });
        jPanel42.add(cmbFormaPago);

        jPanel7.add(jPanel42);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Notas");
        jPanel27.add(jLabel10);

        txtNotas.setPreferredSize(new java.awt.Dimension(300, 25));
        jPanel27.add(txtNotas);

        jPanel7.add(jPanel27);

        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout3.setAlignOnBaseline(true);
        jPanel31.setLayout(flowLayout3);

        ckAdelanto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ckAdelanto.setText("Adelanto");
        jPanel31.add(ckAdelanto);

        btnAtth.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAtth.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/attach-24.png"))); // NOI18N
        btnAtth.setToolTipText("Agregar un archivo");
        btnAtth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtthActionPerformed(evt);
            }
        });
        jPanel31.add(btnAtth);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_icon_25x25_2.png"))); // NOI18N
        btnAdd.setToolTipText("Agregar");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel31.add(btnAdd);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save-25.png"))); // NOI18N
        btnSave.setToolTipText("Guardar cambios");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel31.add(btnSave);

        jPanel7.add(jPanel31);

        jPanel43.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reporte de abonos ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel48.setPreferredSize(new java.awt.Dimension(1012, 200));
        jPanel48.setLayout(new java.awt.BorderLayout());

        jPanel51.setLayout(new java.awt.GridLayout(1, 0));

        tblineasPC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdLinea", "Id", "Proveedor", "Documento", "Moneda", "Monto original", "Monto original ₡", "Abono", "Abono ₡", "Saldo", "Saldo ₡", "Solicitado", "Usuario", "Rev contabilidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblineasPC.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblineasPC.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tblineasPC);
        if (tblineasPC.getColumnModel().getColumnCount() > 0) {
            tblineasPC.getColumnModel().getColumn(0).setMinWidth(0);
            tblineasPC.getColumnModel().getColumn(0).setPreferredWidth(0);
            tblineasPC.getColumnModel().getColumn(0).setMaxWidth(0);
            tblineasPC.getColumnModel().getColumn(1).setMinWidth(0);
            tblineasPC.getColumnModel().getColumn(1).setPreferredWidth(0);
            tblineasPC.getColumnModel().getColumn(1).setMaxWidth(0);
            tblineasPC.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblineasPC.getColumnModel().getColumn(6).setPreferredWidth(120);
        }

        jPanel51.add(jScrollPane4);

        jPanel48.add(jPanel51, java.awt.BorderLayout.CENTER);

        jPanel52.setPreferredSize(new java.awt.Dimension(1054, 35));
        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        btnDeleteAbono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_icon_25x25.png"))); // NOI18N
        btnDeleteAbono.setPreferredSize(new java.awt.Dimension(27, 27));
        btnDeleteAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAbonoActionPerformed(evt);
            }
        });
        jPanel52.add(btnDeleteAbono);

        jPanel48.add(jPanel52, java.awt.BorderLayout.PAGE_START);

        jPanel53.setPreferredSize(new java.awt.Dimension(1054, 25));

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1034, Short.MAX_VALUE)
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        jPanel48.add(jPanel53, java.awt.BorderLayout.PAGE_END);

        jPanel43.add(jPanel48, java.awt.BorderLayout.SOUTH);

        jPanel49.setPreferredSize(new java.awt.Dimension(1012, 10));

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1044, Short.MAX_VALUE)
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel43.add(jPanel49, java.awt.BorderLayout.NORTH);

        jDialog2.getContentPane().add(jPanel43, java.awt.BorderLayout.CENTER);

        jPanel44.setPreferredSize(new java.awt.Dimension(1084, 15));

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1084, Short.MAX_VALUE)
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        jDialog2.getContentPane().add(jPanel44, java.awt.BorderLayout.PAGE_START);

        jPanel45.setPreferredSize(new java.awt.Dimension(1084, 15));

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1084, Short.MAX_VALUE)
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        jDialog2.getContentPane().add(jPanel45, java.awt.BorderLayout.PAGE_END);

        jPanel46.setPreferredSize(new java.awt.Dimension(15, 330));

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        jDialog2.getContentPane().add(jPanel46, java.awt.BorderLayout.LINE_END);

        jPanel47.setPreferredSize(new java.awt.Dimension(15, 330));

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        jDialog2.getContentPane().add(jPanel47, java.awt.BorderLayout.LINE_START);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(700, 2147483647));
        jPanel1.setMinimumSize(new java.awt.Dimension(700, 58));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla documentos contado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel21.setPreferredSize(new java.awt.Dimension(1132, 40));
        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout2.setAlignOnBaseline(true);
        jPanel21.setLayout(flowLayout2);

        btnAbrirForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_icon_25x25_2.png"))); // NOI18N
        btnAbrirForm.setToolTipText("Agregar documento");
        btnAbrirForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirFormActionPerformed(evt);
            }
        });
        jPanel50.add(btnAbrirForm);

        jPanel21.add(jPanel50);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_icon_25x25.png"))); // NOI18N
        btnDelete.setToolTipText("Eliminar registro seleccionado");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel21.add(btnDelete);

        jLabel22.setText("Mostrando");
        jPanel55.add(jLabel22);

        cmbFiltroSaldo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Sin saldo pendiente", "Con saldo pendiente", "Abono pendiente" }));
        cmbFiltroSaldo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFiltroSaldoItemStateChanged(evt);
            }
        });
        jPanel55.add(cmbFiltroSaldo);

        jPanel21.add(jPanel55);

        jPanel6.add(jPanel21, java.awt.BorderLayout.PAGE_START);

        pnlSplit.setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel23.setLayout(new java.awt.GridLayout(1, 0));

        tbHistoricoCP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CIA", "TIPOPROV", "PROVEEDOR", "NOMBRE PR", "NOTAS", "DOCUMENTO", "FECHA DOC", "VENCIMIENTO", "DIAS MORA", "TIPO MORA", "MONEDA", "MONTO", "SALDO", "MONTO ₡", "SALDO ₡", "Abono", "CTA PRESUPUESTO", "APLICACION", "EMBARQUE", "CONTA CRED", "CREADOR", "FECHA CREACION", "Tipo documento", "FORMA PAGO", "Adelanto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbHistoricoCP.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbHistoricoCP.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbHistoricoCP);
        if (tbHistoricoCP.getColumnModel().getColumnCount() > 0) {
            tbHistoricoCP.getColumnModel().getColumn(0).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(0).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(2).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(2).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(2).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(3).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(3).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(3).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(4).setPreferredWidth(300);
            tbHistoricoCP.getColumnModel().getColumn(8).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(8).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(8).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(9).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(9).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(9).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(10).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(10).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(10).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(18).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(18).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(18).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(19).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(19).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(19).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(20).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(20).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(20).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(21).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(21).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(21).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(22).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(22).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(22).setMaxWidth(0);
        }

        jPanel23.add(jScrollPane1);

        jSplitPane1.setLeftComponent(jPanel23);

        jPanel24.setPreferredSize(new java.awt.Dimension(1120, 140));
        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel25.setPreferredSize(new java.awt.Dimension(0, 20));
        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbTbInfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbTbInfo.setText("Filas: 0");
        jPanel25.add(lbTbInfo);

        jPanel24.add(jPanel25, java.awt.BorderLayout.PAGE_START);

        jPanel26.setPreferredSize(new java.awt.Dimension(1120, 60));
        jPanel26.setLayout(new java.awt.GridLayout(1, 0));

        txtResumen.setEditable(false);
        txtResumen.setColumns(20);
        txtResumen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtResumen.setRows(5);
        txtResumen.setText("\n");
        jScrollPane2.setViewportView(txtResumen);

        jPanel26.add(jScrollPane2);

        jPanel24.add(jPanel26, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel24);

        pnlSplit.add(jSplitPane1);

        jPanel6.add(pnlSplit, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 60));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jProgressBar1.setOpaque(true);
        jProgressBar1.setPreferredSize(new java.awt.Dimension(146, 22));
        jProgressBar1.setStringPainted(true);
        jPanel2.add(jProgressBar1, java.awt.BorderLayout.NORTH);

        jPanel54.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbInicio.setText("Inicio");
        jPanel32.add(lbInicio);

        dtFechaIni.setDateFormatString("dd-MM-yyyy");
        dtFechaIni.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel32.add(dtFechaIni);

        Inicio1.setText("Fin");
        jPanel32.add(Inicio1);

        dtFin.setDateFormatString("dd-MM-yyyy");
        dtFin.setPreferredSize(new java.awt.Dimension(120, 22));
        dtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dtFinPropertyChange(evt);
            }
        });
        jPanel32.add(dtFin);

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel16.setToolTipText("Fechas de creación de los registros");
        jPanel32.add(jLabel16);

        jPanel29.add(jPanel32);

        jPanel54.add(jPanel29);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("CIA");
        jLabel15.setToolTipText("");
        jPanel30.add(jLabel15);

        cmbFiltCia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "RYMSA", "CILT", "TURINTEL" }));
        cmbFiltCia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFiltCiaItemStateChanged(evt);
            }
        });
        jPanel30.add(cmbFiltCia);

        jPanel54.add(jPanel30);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refrescar2.png"))); // NOI18N
        btnRefresh.setToolTipText("Refrescar la tabla");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel54.add(btnRefresh);

        jPanel2.add(jPanel54, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(400, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 925, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(20, 220));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(20, 220));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 470, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        HistoricoCP h = getHistoricoCPFromForm();
        if (h != null) {
            //verified that the document doesn't exists
            entitys.HistoricoCP p = crhc.obtenerHistoricoCPContado(h);
            if (p == null) {
                saveHAndAttachements(h);
            } else {
                JOptionPane.showMessageDialog(null, "El documento ya existe para ese proveedor", "Aviso importante", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Por favor asegurese de que la información es correcta", "Aviso importante", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnAddActionPerformed
    private void saveHAndAttachements(HistoricoCP h) {
        int res = crhc.agregarHistoricoContado2(h);

        if (res > 0) {
            h.setId(res);
            entitys.AbonoSugeridoContado ab = getNewAbonoSugeridoContado(h);
            boolean res1 = crdL.agAbonoSugeridoContado(ab);
            if (selectedFile != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String newName = h.getTipo_Documento() + "_" + res + "_" + sdf.format(h.getFECHA_DOCUMENTO());
                FileHandler fh = new FileHandler();
                boolean filesaved = fh.saveFile(this.selectedFile, newName);
                if (filesaved) {
                    jProgressBar1.setVisible(true);
                    jProgressBar1.setString("Proceso completo...");
                    view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al guardar el archivo adjunto");
                }

            }
            refreshInfo();

        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar el registro", "Aviso importante", JOptionPane.ERROR_MESSAGE);
        }
    }

    private entitys.AbonoSugeridoContado getNewAbonoSugeridoContado(entitys.HistoricoCP h) {
        entitys.AbonoSugeridoContado a = new entitys.AbonoSugeridoContado();

        try {
            if (h != null) {
                double abonoC = Double.parseDouble(txtAbonoC.getText());
                double abono = Double.parseDouble(txtAbono.getText());
                a.setDocumento(h.getDOCUMENTO());
                a.setAbono(abono);
                a.setSaldoActual(h.getSaldo());
                a.setSaldoActualColones(h.getSaldo_colones());
                a.setAbonoColones(abonoC);
                a.setId(h.getId());
                a.setMoneda(h.getMONEDA());
                a.setSemana(this.currentWeek);
                a.setForma_pago(h.getForma_Pago());
                a.setProveedor(Integer.parseInt(h.getPROVEEDOR()));
                a.setMontoOriginal(h.getMONTO());
                a.setMontoOriginalColones(h.getMonto_colones());
            }
        } catch (Exception e) {
            System.err.println("view.MantHistoricoCPContado.getNewAbonoSugeridoContado() error " + e.getMessage());
            return null;
        }

        return a;
    }

    private void savedAttachements(HistoricoCP h) {

        if (selectedFile != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            FileHandler fh = new FileHandler();
            String newName = h.getTipo_Documento() + "_" + h.getId() + "_" + sdf.format(h.getFECHA_DOCUMENTO());
            String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."), selectedFile.getName().length());
            newName = fh.getFileNewIndexedName(new java.io.File(logic.AppStaticValues.archivosFacturasContado + newName + extension));
            if (newName == null) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al guardar el archivo adjunto");
            } else {
                boolean filesaved = fh.saveFile(this.selectedFile, newName);
                if (!filesaved) {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al guardar el archivo adjunto");
                }
            }
        }
        //JOptionPane.showMessageDialog(null,"nuevo idlinea "+res);
        refreshInfo();

    }

    private void refreshInfo() {
        try {
            this.loadingInfo = true;
            refreshForm();
            cmbFiltroSaldo.setSelectedIndex(0);
            view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);
            DefaultTableModel model = (DefaultTableModel) tbHistoricoCP.getModel();
            String cia = this.cmbFiltCia.getSelectedItem().toString();
            java.util.Date ini = dtFechaIni.getDate();
            java.util.Date fin = dtFin.getDate();
            this.listaHistoricos = crhc.obtenerHistoricoCPContado_View(cia.equals("Todas") ? "" : cia, ini, fin);
            for (HistoricoCP e : listaHistoricos) {
                addTbRow(model, e);
            }
            this.lbTbInfo.setText("Filas: " + tbHistoricoCP.getRowCount());
            loadingInfo = false;
        } catch (Exception e) {
            System.out.println("view.MantHistoricoCPContado.refreshInfo() error " + e.getMessage());
        }
    }

    private void obtTipoCambio() {
        data.CRUDTipoCambio cr = new data.CRUDTipoCambio();
        this.tipoc = cr.getTipoCambioPorFecha(new java.util.Date());
    }

    private void resumirInfoColumns() {
        if (!loadingInfo) {
            int[] selectedRows = tbHistoricoCP.getSelectedRows();
            double sumSaldCol = 0;
            double sumMontOriCol = 0;
            double sumMontCol = 0;
            double sumSaldDol = 0;
            double sumMontOriDol = 0;
            double sumMontOriDolCol = 0;
            double ttlAb = 0;
            double ttlAbDol = 0;
            double ttTodosSaldos = 0;
            this.txtResumen.setText("");
            for (int row : selectedRows) {
                int doc = (int) JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "id", row);
                HistoricoCP h = HistoricoCP.obtenerHistoricoPorId(doc, listaHistoricos);
                if (h.getMONEDA().equals("CRC")) {
                    sumSaldCol += h.getSaldo();
                    sumMontOriCol += h.getMONTO();
                    ttlAb += h.getAbono();
                } else {
                    sumSaldDol += h.getSaldo();
                    sumMontOriDol += h.getMONTO();
                    sumMontOriDolCol += h.getMonto_colones();
                    ttlAbDol += h.getAbono();
                }
                sumMontCol += h.getMonto_colones();
                ttTodosSaldos += h.getSaldo_colones();
                /*for (AbonoSugerido s : h.getSugeridos()) {
                    if (s.getSemana() == currentWeek && s.getAprobado() == 1) {
                        ttlAb += s.getMonto_Colones();
                    } else if (s.getSemana() == currentWeek && s.getAprobado() == 0) {
                        ttlAbSinap += s.getMonto_Colones();
                    }
                }*/
            }
            this.txtResumen.setText(
                    "Sm montos: ₡" + AppStaticValues.numberFormater.format(sumMontOriCol)
                    + "\tSm saldos: ₡" + AppStaticValues.numberFormater.format(sumSaldCol)
                    + "\nSm montos: $" + AppStaticValues.numberFormater.format(sumMontOriDol)
                    + "\tSm saldos: $" + AppStaticValues.numberFormater.format(sumSaldDol)
                    + "\nSm Abonos: ₡" + AppStaticValues.numberFormater.format(ttlAb)
                    + "\tSm Abonos: $" + AppStaticValues.numberFormater.format(ttlAbDol)
                    + "\nSm montos en $ en ₡" + AppStaticValues.numberFormater.format(sumMontOriDolCol)
                    + "\nSm todos los montos: ₡" + AppStaticValues.numberFormater.format(sumMontCol)
                    + "\nSm todos los saldos: ₡" + AppStaticValues.numberFormater.format(ttTodosSaldos));

            /*String resumen = "Total aprobados: ₡"
                    + AppStaticValues.numberFormater.format(ttlAb)
                    + "\nTotal sin aprobar: ₡"
                    + AppStaticValues.numberFormater.format(ttlAbSinap)
                    + "\nTotal saldos - abonos aprobados: ₡"
                    + AppStaticValues.numberFormater.format(ttTodosSaldos - ttlAb);
            this.txtResumen.setText(resumen);*/
        }
    }

    private HistoricoCP getHistoricoCPFromForm() {
        if (txtDocumento.getText().isEmpty()
                || txtNumProv.getText().isEmpty()
                || txtNomProv.getText().isEmpty()) {
            return null;
        }
        if (cmbMoneda.getSelectedIndex() == 1 && this.tipoc == null) {
            obtTipoCambio();
            if (this.tipoc == null) {
                JOptionPane.showMessageDialog(null, "El tipo de cambio del día no esta listo", "Atenció", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }
        }
        HistoricoCP h = new HistoricoCP();
        try {
            int id = Integer.parseInt(txtNumProv.getText());
            entitys.ProveedorContado.ProveedorContado p = crd.obtenerListaProveedorContadoPorId(id);
            if (p == null) {
                return null;
            }
            double monto = Double.parseDouble(this.txtMonto.getText());
            double abono = Double.parseDouble(txtAbono.getText());
            double montoC = Double.parseDouble(this.txtMontoC.getText());
            double abonoC = Double.parseDouble(txtAbonoC.getText());
            double montoColones = Double.parseDouble(txtMontoC.getText());

            if (monto < abono || montoC < abonoC) {
                return null;
            }
            String cta = this.cmbCuentaPres.getSelectedItem().toString().trim();
            h.setCIA(this.cmbCiaForm.getSelectedItem().toString());
            h.setTIPOPROV("LOC");
            h.setMONEDA(this.cmbMoneda.getSelectedItem().toString());
            h.setPROVEEDOR(this.txtNumProv.getText());
            h.setNOMBRE(p.getNombre());
            h.setDOCUMENTO(this.txtDocumento.getText());
            h.setFECHA_DOCUMENTO(this.dtFechDoc.getDate());
            h.setFECHA_VENCE(new java.util.Date());
            h.setNotas(txtNotas.getText());
            h.setDIAS_CREDITO(0);
            h.setESTAD_MORA("Sin vencer");
            h.setSaldo_colones(montoC - abonoC);
            h.setMONTO(monto);
            h.setSaldo(monto - abono);
            h.setMonto_colones(montoColones);
            h.setDesc_Cta_Pres(cta.substring(12, cta.length()));
            h.setCTA_PRESUPUESTO(cta.substring(0, 11));
            h.setAplicacion("");
            h.setTipo_Documento(this.cmbTipoDocumento.getSelectedItem().toString());
            h.setCONTA_CRED("CONTADO");
            h.setCreador(DataUser.username);
            h.setFecha_Creacion(new java.util.Date());
            h.setForma_Pago(cmbFormaPago.getSelectedItem().toString());
            h.setId(0);
            h.setAdelanto(ckAdelanto.isSelected() ? 1 : 0);
            if (h.getFECHA_DOCUMENTO() == null) {
                return null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error " + e.getMessage());
            return null;
        }
        return h;
    }

    private HistoricoCP getEditHistoricoCPFromForm() {
        if (txtDocumento.getText().isEmpty()
                || txtNumProv.getText().isEmpty()
                || txtNomProv.getText().isEmpty()) {
            return null;
        }
        HistoricoCP h = new HistoricoCP();
        try {
            int id = Integer.parseInt(txtNumProv.getText());
            entitys.ProveedorContado.ProveedorContado p = crd.obtenerListaProveedorContadoPorId(id);
            if (p == null) {
                return null;
            }
            double monto = Double.parseDouble(this.txtMonto.getText());
            double montoC = Double.parseDouble(this.txtMontoC.getText());

            String cta = this.cmbCuentaPres.getSelectedItem().toString().trim();
            h.setCIA(this.cmbCiaForm.getSelectedItem().toString());
            h.setTIPOPROV("LOC");
            h.setPROVEEDOR(this.txtNumProv.getText());
            h.setNOMBRE(p.getNombre());
            h.setDOCUMENTO(this.txtDocumento.getText());
            h.setFECHA_DOCUMENTO(this.dtFechDoc.getDate());
            h.setFECHA_VENCE(new java.util.Date());
            h.setNotas(txtNotas.getText());
            h.setDIAS_CREDITO(0);
            h.setESTAD_MORA("Sin vencer");
            h.setMONTO(monto);
            h.setMonto_colones(montoC);
            h.setDesc_Cta_Pres(cta.substring(12, cta.length()));
            h.setCTA_PRESUPUESTO(cta.substring(0, 11));
            h.setAplicacion("");
            h.setTipo_Documento(this.cmbTipoDocumento.getSelectedItem().toString());
            h.setCONTA_CRED("CONTADO");
            h.setCreador(DataUser.username);
            h.setFecha_Creacion(new java.util.Date());
            h.setForma_Pago(cmbFormaPago.getSelectedItem().toString());
            h.setId(0);
            h.setAdelanto(ckAdelanto.isSelected() ? 1 : 0);
            if (h.getFECHA_DOCUMENTO() == null) {
                return null;
            }
        } catch (Exception e) {
            System.out.println("view.MantHistoricoCPContado.getEditHistoricoCPFromForm() error " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error " + e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return h;
    }

    private void refreshForm() {

        this.txtNumProv.setText("");
        this.txtNomProv.setText("");
        this.txtDocumento.setText("");
        this.txtMonto.setText("");
        this.txtAbono.setText("");
        txtMontoC.setText("");
        txtAbonoC.setText("");
        this.txtNotas.setText("");
        this.cmbMoneda.setSelectedIndex(0);
        this.cmbTipoDocumento.setSelectedIndex(0);
        this.ckAdelanto.setSelected(false);
        this.selectedFile = null;
        this.btnAtth.setText("");
    }

    private void addTbRow(DefaultTableModel model, HistoricoCP h) {
        String signC = "₡";
        String singD = "$";
        String moneda = h.getMONEDA().equals("CRC") ? signC : singD;
        Presupuesto cta = Presupuesto.getPresupuesto(h.getCTA_PRESUPUESTO(), listaPresupuesto);

        model.addRow(new Object[]{
            h.getId(),
            h.getCIA(),
            h.getTIPOPROV(),
            h.getPROVEEDOR(),
            h.getNOMBRE(),
            h.getNotas(),
            h.getDOCUMENTO(),
            logic.AppStaticValues.dateFormat.format(h.getFECHA_DOCUMENTO()),
            h.getFECHA_VENCE(),
            h.getDIAS_CREDITO(),
            h.getESTAD_MORA(),
            h.getMONEDA(),
            moneda + AppStaticValues.numberFormater.format(h.getMONTO()),
            moneda + AppStaticValues.numberFormater.format(h.getSaldo()),
            "₡" + AppStaticValues.numberFormater.format(h.getMonto_colones()),
            "₡" + AppStaticValues.numberFormater.format(h.getSaldo_colones()),
            h.getAbono(),
            h.getCTA_PRESUPUESTO(),
            cta == null ? "" : cta.getCONCEPATOADETALLE(),//h.getAplicacion(),
            "embarque",
            h.getCONTA_CRED(),
            h.getCreador(),
            h.getFecha_Creacion(),
            h.getTipo_Documento(),
            h.getForma_Pago(),
            h.getAdelanto() == 1 ? "Si" : "No"

        });
    }
    private void cmbDepartamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepartamentoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadCtas();
        }

    }//GEN-LAST:event_cmbDepartamentoItemStateChanged

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:

        if (tbHistoricoCP.getSelectedRowCount() == 1) {
            int row = tbHistoricoCP.getSelectedRow();
            int id = (int) JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "id", row);
            String documento = (String) JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "documento", row);
            int proveedor = Integer.parseInt(JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "proveedor", row).toString());
            ArrayList<entitys.AbonoSugeridoContado> lista = this.crdL.obtenerLineasAbonoHistoricoCPContado(proveedor, documento);
            for (AbonoSugeridoContado a1 : lista) {
                if (a1.getRevisadoConta() == 1) {
                    JOptionPane.showMessageDialog(null, "El documento tiene abonos revisados por contabilidad", "No se puede eliminar el documento", JOptionPane.WARNING_MESSAGE);
                    return;
                };

            }
            boolean lineas = lista.size() > 0;
            if (lineas) {
                String[] options = {"Continuar", "Cancelar"};
                int choice = JOptionPane.showOptionDialog(null, "El documento tiene abonos sugeridos, desea eliminarlos?", "Escoja una opción",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (choice == 0) {
                    System.out.println("view.MantHistoricoCPContado.mnAgAbActionPerformed You selected Option 1");

                } else if (choice == 1) {
                    JOptionPane.showMessageDialog(null, "Proceso cancelado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, "Proceso cancelado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            //deleting lines
            for (AbonoSugeridoContado a1 : lista) {
                crdL.elimSugeridoContado(a1.getIdAbonoSugeridoContado());
            }
            //deleting main document
            boolean res = crh.deleteHistoricoCPContado(id);
            if (res) {
                refreshInfo();
                JOptionPane.showMessageDialog(null, "Se ha eliminado el registro", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar el registro", "Aviso", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione sólo una fila", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnDeleteActionPerformed

    private void cmbCiaFormItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCiaFormItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbCiaFormItemStateChanged

    private void cmbTipoDocumentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoDocumentoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTipoDocumentoItemStateChanged

    private void dtFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dtFinPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().contains("date")) {
            refreshInfo();
        }
    }//GEN-LAST:event_dtFinPropertyChange

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        refreshInfo();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        HistoricoCP h = getEditHistoricoCPFromForm();
        if (h != null && this.currentHcp != null) {
            if (!h.getDOCUMENTO().equals(this.currentHcp.getDOCUMENTO())
                    || !h.getPROVEEDOR().equals(this.currentHcp.getPROVEEDOR())) {
                HistoricoCP hcp = this.crh.obtHistoricoCPContadPorProvDoc(h.getPROVEEDOR(), h.getDOCUMENTO());
                if (hcp != null) {
                    JOptionPane.showMessageDialog(null, "No se puede actualizar el documento con esa información porque ya existe", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            ArrayList<entitys.AbonoSugeridoContado> lista
                    = this.crdL.obtenerLineasAbonoHistoricoCPContado(Integer.parseInt(this.currentHcp.getPROVEEDOR()), this.currentHcp.getDOCUMENTO());
            double sumAbonos = 0;
            double sumAbonoC = 0;
            boolean abonosR = false;
            boolean abonosSinR = true;
            for (AbonoSugeridoContado a : lista) {
                sumAbonos += a.getAbono();
                sumAbonoC += a.getAbonoColones();
                if (!abonosR && a.getRevisadoConta() == 1) {
                    abonosR = true;
                }
                if (a.getRevisadoConta() == 1) {
                }
            }
            if (sumAbonos > h.getMONTO()) {
                JOptionPane.showMessageDialog(null, "La suma de abonos actuales es mayor al monto\n"
                        + "Se recomenda corregir los abonos antes", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            h.setId(this.currentHcp.getId());
            h.setSaldo(h.getMONTO() - sumAbonos);
            h.setSaldo_colones(h.getMonto_colones() - sumAbonoC);
            //actualizar

            boolean res = crhc.ActualizarHistoricoContado(h);
            if (res) {

                if (!h.getDOCUMENTO().equals(this.currentHcp.getDOCUMENTO())
                        || !h.getForma_Pago().equals(this.currentHcp.getForma_Pago())
                        || !h.getPROVEEDOR().equals(this.currentHcp.getPROVEEDOR())
                        || !h.getCIA().equals(this.currentHcp.getCIA())
                        || h.getMONTO() != this.currentHcp.getMONTO()) {

                    //actualizar lineas
                    double saldo = h.getMONTO();
                    double saldo_colones = h.getMonto_colones();
                    for (AbonoSugeridoContado a : lista) {
                        a.setDocumento(h.getDOCUMENTO());
                        a.setForma_pago(h.getForma_Pago());
                        a.setProveedor(Integer.parseInt(h.getPROVEEDOR()));
                        a.setSaldoActual(saldo - a.getAbono());
                        a.setSaldoActualColones(saldo_colones - a.getAbonoColones());
                        saldo -= a.getAbono();
                        saldo_colones -= a.getAbonoColones();
                        boolean flag = this.crdL.actSugeridoContado(a);
                        if (!flag) {
                            JOptionPane.showMessageDialog(null, "Han ocurrido errores al actualizar los abonos de la factura", "Aviso", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                jDialog2.setVisible(false);
                savedAttachements(this.currentHcp);
                this.currentHcp = null;
                setFormBtnMenu(false, false, true, true);
                refreshInfo();
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al actualizar el registro id " + currentHcp.getId());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al revisar el formulario", "Aviso", JOptionPane.WARNING_MESSAGE);
            this.currentHcp = null;
            refreshForm();

        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void mnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEditActionPerformed
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(null, "Función sin implementar");
        loadingInfo = true;
        int[] selectedRows = this.tbHistoricoCP.getSelectedRows();
        //refreshInfo();
        if (selectedRows.length == 1) {
            int row = selectedRows[0];
            int id = (int) JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "id", row);
            this.currentHcp = HistoricoCP.obtenerHistoricoPorId(id, listaHistoricos);
            //this.btnSave.setEnabled(this.currentHcp.getSaldo()>0);
            ArrayList<entitys.AbonoSugeridoContado> lista
                    = this.crdL.obtenerLineasAbonoHistoricoCPContado(Integer.parseInt(this.currentHcp.getPROVEEDOR()), this.currentHcp.getDOCUMENTO());
            if (loadFormToEdit()) {
                //this.btnAtth.setEnabled(false);
                this.btnAdd.setEnabled(false);
                this.btnSave.setVisible(true);
                this.btnSave.setEnabled(true);
                this.pnlPagar.setVisible(false);
                this.pnlMoneda.setVisible(false);
                this.pnlPagarColones.setVisible(false);
            }
            JTableCommonFunctions.limpiarTabla(tblineasPC);
            DefaultTableModel model = (DefaultTableModel) tblineasPC.getModel();
            lista.forEach(e -> {
                model.addRow(new Object[]{
                    e.getIdAbonoSugeridoContado(),
                    e.getId(),
                    e.getProveedor(),
                    e.getDocumento(),
                    e.getMoneda(),
                    logic.AppStaticValues.numberFormater.format(e.getMontoOriginal()),
                    logic.AppStaticValues.numberFormater.format(e.getMontoOriginalColones()),
                    logic.AppStaticValues.numberFormater.format(e.getAbono()),
                    logic.AppStaticValues.numberFormater.format(e.getAbonoColones()),
                    logic.AppStaticValues.numberFormater.format(e.getSaldoActual()),
                    logic.AppStaticValues.numberFormater.format(e.getSaldoActualColones()),
                    logic.AppStaticValues.dateFormat.format(e.getFechaSolicitud()),
                    e.getUsuario(),
                    e.getRevisadoConta() == 1

                });
            });

            this.jDialog2.setLocationRelativeTo(this);
            this.jDialog2.setVisible(true);

        } else {
            this.currentHcp = null;
            this.btnAdd.setEnabled(true);
            this.btnSave.setVisible(false);
            this.btnSave.setEnabled(false);
            this.ckAdelanto.setSelected(false);
            this.pnlPagar.setVisible(false);
            this.pnlPagarColones.setVisible(false);
            refreshInfo();
            JOptionPane.showMessageDialog(null, "Para editar seleccione sólamente una fila");
        }
        loadingInfo = false;

    }//GEN-LAST:event_mnEditActionPerformed

    private void cmbFiltCiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFiltCiaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            refreshInfo();
        }
    }//GEN-LAST:event_cmbFiltCiaItemStateChanged

    private void btnProveedorContadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedorContadoActionPerformed
        // TODO add your handling code here:
        MantenientoProveedoreContado mpc = new MantenientoProveedoreContado();
        mpc.setVisible(true);
    }//GEN-LAST:event_btnProveedorContadoActionPerformed

    private void cmbFormaPagoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFormaPagoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbFormaPagoItemStateChanged

    private void btnAtthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtthActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Seleccione un archivo");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF, JPEG, JPG Files", "pdf", "jpeg", "jpg"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            this.selectedFile = fileChooser.getSelectedFile();
            this.btnAtth.setText("1");

        } else {
            this.selectedFile = null;
            this.btnAtth.setText("");
            System.out.println("No file selected.");
        }
    }//GEN-LAST:event_btnAtthActionPerformed

    private void mnAbrirAdjuntosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAbrirAdjuntosActionPerformed
        // TODO add your handling code here:
        int row = tbHistoricoCP.getSelectedRow();
        boolean flag = tbHistoricoCP.getSelectedRowCount() == 1;
        if (row > -1 && flag) {
            int id = (int) JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "ID", row);
            this.currentHcp = HistoricoCP.obtenerHistoricoPorId(id, listaHistoricos);
            logic.util.FileHandler fh = new logic.util.FileHandler();
            String stringId = this.currentHcp.getTipo_Documento() + "_" + id + "_" + currentHcp.getFECHA_DOCUMENTO();
            boolean res = fh.getFilteredfiles(logic.AppStaticValues.archivosFacturasContado, stringId);
            if (!res) {
                JOptionPane.showMessageDialog(null, "No se han encontrado archivos asociados");
            }
        } else if (!flag) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione sólamente una fila");
        }

    }//GEN-LAST:event_mnAbrirAdjuntosActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void mnAgAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAgAbActionPerformed
        // TODO add your handling code here:
        String[] options = {"Continuar", "Cancelar"};
        int choice = JOptionPane.showOptionDialog(null, "Este proceso pagará el total de los saldos, quiere continuar?", "Escoja una opción",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            System.out.println("view.MantHistoricoCPContado.mnAgAbActionPerformed You selected Option 1");

        } else if (choice == 1) {
            JOptionPane.showMessageDialog(null, "Proceso cancelado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(null, "Proceso cancelado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        agAsyncAbono();


    }//GEN-LAST:event_mnAgAbActionPerformed

    private void mnAbCantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAbCantActionPerformed
        // TODO add your handling code here:
        if (tbHistoricoCP.getSelectedRowCount() == 1) {
            String input = JOptionPane.showInputDialog("Ingrese la cantidad");
            System.out.println("La cantidad es: " + input);
            try {
                double abono = Double.parseDouble(input);
                if (abono < 0) {
                    JOptionPane.showMessageDialog(null, "El abono no puede ser negativo", "Aviso inportante", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int row = tbHistoricoCP.getSelectedRow();
                int id = (int) JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "id", row);
                HistoricoCP h = crhc.obtenerHistoricoCPContadoById(id);
                if (h == null) {
                    JOptionPane.showMessageDialog(null, "No se ha encontrado el documento", "Aviso inportante", JOptionPane.WARNING_MESSAGE);
                    return;
                } else {
                    AbonoSugeridoContado ab = crdL.obtenerAbonoContado_ViewContaAp((Integer.parseInt(h.getPROVEEDOR())), id, h.getDOCUMENTO());
                    if (h.getSaldo() == 0 || ab != null) {

                        if (ab != null) {
                            actAb(ab, h, input);
                            return;
                        }
                        JOptionPane.showMessageDialog(null, "El documento no tiene saldo pendiente",
                                "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        return;

                    }

                    if (h.getSaldo() < abono) {
                        JOptionPane.showMessageDialog(null, "El abono no puede ser mayor al saldo", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    AbonoSugeridoContado a = getAbonoParaHisto(h);
                    a.setAbono(Double.parseDouble(input));
                    a.setSaldoActual(h.getSaldo() - a.getAbono());
                    if (h.getMONEDA().equals("CRC")) {
                        a.setAbonoColones(a.getAbono());
                        a.setSaldoActualColones(h.getSaldo_colones() - a.getAbono());
                    } else {
                        a.setSaldoActualColones(h.getSaldo_colones() - (a.getAbono() * this.tipoc.getCompra()));
                        a.setAbonoColones(a.getAbono() * this.tipoc.getCompra());
                    }
                    boolean res = crdL.agAbonoSugeridoContado(a);
                    if (res) {
                        JOptionPane.showMessageDialog(null, "Se ha solicitado un abono al documento " + h.getDOCUMENTO(),
                                "Aviso inportante", JOptionPane.INFORMATION_MESSAGE);
                        refreshInfo();
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocrrido un error al guardar el registro",
                                "Aviso inportante", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error " + e.getMessage(), "Atención", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione una fila", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_mnAbCantActionPerformed
    private void actAb(AbonoSugeridoContado a, HistoricoCP h, String input) {
        String[] options = {"Continuar", "Cancelar"};
        int choice = JOptionPane.showOptionDialog(null, "El documento " + a.getDocumento() + " ya tiene un abono pendiente, desea actualizarlo?", "Escoja una opción",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            System.out.println("view.MantHistoricoCPContado.mnAgAbActionPerformed You selected Option 1");

        } else if (choice == 1) {
            JOptionPane.showMessageDialog(null, "Proceso cancelado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(null, "Proceso cancelado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        double abono = Double.parseDouble(input);
        h.setSaldo(h.getSaldo() + a.getAbono());
        if (h.getMONEDA().equals("CRC")) {
            h.setSaldo_colones(h.getSaldo());
        } else {
            h.setSaldo_colones(h.getSaldo() * this.tipoc.getCompra());
        }
        if (h.getSaldo() < abono) {
            JOptionPane.showMessageDialog(null, "El abono no puede ser mayor al saldo", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        a.setAbono(abono);
        a.setSaldoActual(h.getSaldo() - a.getAbono());
        if (h.getMONEDA().equals("CRC")) {
            a.setAbonoColones(a.getAbono());
            a.setSaldoActualColones(h.getSaldo() - a.getAbono());
        } else {
            a.setSaldoActualColones(h.getSaldo_colones() - (a.getAbono() * this.tipoc.getCompra()));
            a.setAbonoColones(a.getAbono() * this.tipoc.getCompra());
        }
        boolean res = crdL.actSugeridoContado(a);
        if (res) {
            refreshInfo();
            JOptionPane.showMessageDialog(null, "Se ha actualizado un abono al documento " + h.getDOCUMENTO(),
                    "Aviso inportante", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocrrido un error al guardar el registro",
                    "Aviso inportante", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btnAbrirFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbrirFormActionPerformed
        // TODO add your handling code here:
        view.util.JTableCommonFunctions.limpiarTabla(tblineasPC);
        refreshForm();
        this.btnAdd.setEnabled(true);
        this.btnSave.setEnabled(false);
        this.pnlPagar.setVisible(true);
        this.pnlPagarColones.setVisible(true);
        this.pnlMoneda.setVisible(true);
        this.jDialog2.setLocationRelativeTo(this);
        this.jDialog2.setVisible(true);
    }//GEN-LAST:event_btnAbrirFormActionPerformed

    private void cmbFiltroSaldoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFiltroSaldoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);
            DefaultTableModel model = (DefaultTableModel) tbHistoricoCP.getModel();
            int index = cmbFiltroSaldo.getSelectedIndex();
            switch (index) {
                case 0:
                    for (HistoricoCP e : listaHistoricos) {

                        addTbRow(model, e);

                    }
                    break;
                case 1:
                    for (HistoricoCP e : listaHistoricos) {

                        if (e.getSaldo() == 0) {
                            addTbRow(model, e);
                        }
                    }
                    break;
                case 2:
                    for (HistoricoCP e : listaHistoricos) {

                        if (e.getSaldo() > 0) {
                            addTbRow(model, e);
                        }
                    }
                    break;
                case 3:
                    for (HistoricoCP e : listaHistoricos) {

                        if (e.getAbono() > 0) {
                            addTbRow(model, e);
                        }
                    }
                    break;
            }
        }
        this.lbTbInfo.setText("Filas: " + tbHistoricoCP.getRowCount());
    }//GEN-LAST:event_cmbFiltroSaldoItemStateChanged

    private void btnDeleteAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAbonoActionPerformed
        // TODO add your handling code here:
        int row = tblineasPC.getSelectedRow();
        if (row > -1) {
            int idlinea = Integer.parseInt(tblineasPC.getValueAt(row, 0).toString());
            int id = Integer.parseInt(tblineasPC.getValueAt(row, 1).toString());
            String documento = tblineasPC.getValueAt(row, 3).toString();
            int proveedor = Integer.parseInt(tblineasPC.getValueAt(row, 2).toString());
            ArrayList<entitys.AbonoSugeridoContado> lista = this.crdL.obtenerLineasAbonoHistoricoCPContado(proveedor, documento);
            entitys.AbonoSugeridoContado ab = AbonoSugeridoContado.obtAbonoSugeridoContadoPorId(idlinea, lista);
            if (ab.getRevisadoConta() == 1) {
                JOptionPane.showMessageDialog(null, "No se puede eliminar este abono porque ya fue revisado por contabilidad", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                if (lista.size() == 1) {
                    String[] options = {"Continuar", "Cancelar"};
                    int choice = JOptionPane.showOptionDialog(null, "Este documento solo tiene un abono, \nsi eliminar el abono se eliminará todo el documento\ndesea continuar"
                            + "?", "Escoja una opción",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                    if (choice == 0) {
                        System.out.println("view.MantHistoricoCPContado.mnAgAbActionPerformed You selected Option 1");
                        boolean res = this.crdL.elimSugeridoContado(idlinea);
                        if (res) {
                            boolean res1 = this.crh.deleteHistoricoCPContado(id);
                            if (res1) {
                                JOptionPane.showMessageDialog(null, "El documento se ha eliminado correctamente!", "Aviso", JOptionPane.INFORMATION_MESSAGE);

                            } else {
                                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar el registro", "Aviso", JOptionPane.ERROR_MESSAGE);
                            }
                            JTableCommonFunctions.limpiarTabla(tblineasPC);
                            refreshForm();
                            refreshInfo();
                            jDialog2.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar el abono!", "Aviso", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (choice == 1) {
                        JOptionPane.showMessageDialog(null, "Proceso cancelado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "Proceso cancelado", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } else {
                    boolean res = this.crdL.elimSugeridoContado(idlinea);
                    if (res) {
                        JOptionPane.showMessageDialog(null, "El abono se ha eliminado correctamente!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        JTableCommonFunctions.limpiarTabla(tblineasPC);
                        refreshForm();
                        refreshInfo();
                        jDialog2.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar el abono!", "Aviso", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        }

    }//GEN-LAST:event_btnDeleteAbonoActionPerformed

    private void cmbMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            if (cmbMoneda.getSelectedIndex() == 1 && this.tipoc == null) {
                obtTipoCambio();
                if (this.tipoc == null) {
                    JOptionPane.showMessageDialog(null, "El tipo de cambio no esta listo", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    loadingInfo = true;
                    cmbMoneda.setSelectedIndex(0);
                    loadingInfo = false;
                }
            }
        }

    }//GEN-LAST:event_cmbMonedaItemStateChanged

    private void mnDeleteAbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeleteAbActionPerformed
        // TODO add your handling code here:
        deleteAsyncAb();

    }//GEN-LAST:event_mnDeleteAbActionPerformed

    private void agAsyncAbono() {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                jProgressBar1.setVisible(true);
                jProgressBar1.setMaximum(tbHistoricoCP.getSelectedRows().length);
                setEnableMenu(false);
                for (int i = 0; i < tbHistoricoCP.getSelectedRows().length; i++) {
                    int row = tbHistoricoCP.getSelectedRows()[i];
                    int id = (int) JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "id", row);
                    System.out.println("selected id " + id);
                    HistoricoCP h = crhc.obtenerHistoricoCPContadoById(id);
                    if (h == null) {
                        JOptionPane.showMessageDialog(null, "No se ha encontrado el documento", "Aviso inportante", JOptionPane.WARNING_MESSAGE);
                    } else {

                        if (h.getSaldo() == 0) {
                            JOptionPane.showMessageDialog(null, "El documento " + h.getDOCUMENTO() + " no tiene saldo pendiente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            jProgressBar1.setValue(i + 1);
                            continue;
                        }
                        AbonoSugeridoContado ab = crdL.obtenerAbonoContado_ViewContaAp((Integer.parseInt(h.getPROVEEDOR())), id, h.getDOCUMENTO());
                        if (ab != null) {
                            JOptionPane.showMessageDialog(null, "El documento " + ab.getDocumento() + " ya tiene un abono pendiente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            jProgressBar1.setValue(i + 1);
                            continue;
                        }
                        AbonoSugeridoContado a = getAbonoParaHisto(h);

                        boolean res1 = crdL.agAbonoSugeridoContado(a);
                        if (!res1) {
                            JOptionPane.showMessageDialog(null, "Ha ocrrido un error al guardar el registro",
                                    "Aviso inportante", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    jProgressBar1.setValue(i + 1);
                }

                refreshInfo();
                setEnableMenu(true);
                jProgressBar1.setString("Proceso completo...");
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            }

        };
        Thread t = new Thread(r);
        t.start();
    }

    private void deleteAsyncAb() {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                jProgressBar1.setVisible(true);
                jProgressBar1.setMaximum(tbHistoricoCP.getSelectedRows().length);
                setEnableMenu(false);
                for (int i = 0; i < tbHistoricoCP.getSelectedRows().length; i++) {
                    int row = tbHistoricoCP.getSelectedRows()[i];
                    int id = (int) JTableCommonFunctions.getCellValueByHeader(tbHistoricoCP, "id", row);
                    System.out.println("selected id " + id);
                    HistoricoCP h = crhc.obtenerHistoricoCPContadoById(id);
                    if (h == null) {
                        JOptionPane.showMessageDialog(null, "No se ha encontrado el documento", "Aviso inportante", JOptionPane.WARNING_MESSAGE);
                    } else {

                        boolean res = crdL.elimSugeridoContadoPendiente(id);
                    }
                    jProgressBar1.setValue(i + 1);
                }

                refreshInfo();
                setEnableMenu(true);
                jProgressBar1.setString("Proceso completo...");
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            }

        };
        Thread t = new Thread(r);
        t.start();
    }

    private void setEnableMenu(boolean enable) {
        this.dtFechaIni.setEnabled(enable);
        this.dtFin.setEnabled(enable);
        this.btnRefresh.setEnabled(enable);
        this.cmbFiltCia.setEnabled(enable);
        setDatesView();
    }

    private AbonoSugeridoContado getAbonoParaHisto(HistoricoCP h) {
        AbonoSugeridoContado a = new AbonoSugeridoContado();
        a.setAbono(h.getSaldo());
        a.setMoneda(h.getMONEDA());
        a.setDocumento(h.getDOCUMENTO());
        if (h.getMONEDA().equals("CRC")) {
            a.setAbonoColones(h.getSaldo());
        } else {
            a.setAbonoColones(h.getSaldo() * this.tipoc.getCompra());
        }
        a.setId(h.getId());
        a.setSaldoActual(0);
        a.setSaldoActualColones(0);
        a.setSemana(this.currentWeek);
        a.setProveedor(Integer.parseInt(h.getPROVEEDOR()));
        a.setForma_pago(h.getForma_Pago());
        return a;
    }

    private boolean doesFileExist() {
        boolean res = false;
        logic.util.FileHandler fh = new logic.util.FileHandler();
        String id = logic.AppStaticValues.archivosFacturasContado + this.currentHcp.getTipo_Documento() + "_" + currentHcp.getId() + "_" + currentHcp.getFECHA_DOCUMENTO() + ".pdf";
        System.out.println("view.MantHistoricoCPContado.doesFileExist() lookin for " + id);
        res = fh.doesFileExist(id);

        return res;
    }

    private boolean loadFormToEdit() {
        boolean res = false;
        try {
            String dep = this.currentHcp.getCTA_PRESUPUESTO().substring(3, 5);

            Departamento d = Departamento.getDepartamentoByCodDepa(listaDepartamentos, dep);
            this.cmbDepartamento.setSelectedItem(dep + "-" + d.getDescripcion());
            loadCtas();
            String cta = "   " + currentHcp.getCTA_PRESUPUESTO().trim()
                    + "-" + currentHcp.getDesc_Cta_Pres().trim();
            this.cmbCuentaPres.setSelectedItem(cta);
            this.cmbMoneda.setSelectedItem(currentHcp.getMONEDA());
            this.txtDocumento.setText(currentHcp.getDOCUMENTO());
            this.dtFechDoc.setDate(currentHcp.getFECHA_DOCUMENTO());
            this.txtNumProv.setText(currentHcp.getPROVEEDOR());
            this.txtNomProv.setText(currentHcp.getNOMBRE());
            this.txtMonto.setText(String.format("%.2f",
                    this.currentHcp.getMONTO())
                    .replace(",", "."));
            this.txtMontoC.setText(String.format("%.2f",
                    this.currentHcp.getMonto_colones())
                    .replace(",", "."));
            this.txtAbono.setText(String.format("%.2f",
                    this.currentHcp.getSaldo())
                    .replace(",", "."));
            this.txtAbonoC.setText(String.format("%.2f",
                    this.currentHcp.getSaldo_colones())
                    .replace(",", "."));
            this.txtNotas.setText(this.currentHcp.getNotas());
            this.cmbTipoDocumento.setSelectedItem(currentHcp.getTipo_Documento());
            this.cmbCiaForm.setSelectedItem(currentHcp.getCIA());
            this.cmbFormaPago.setSelectedItem(currentHcp.getForma_Pago());
            this.ckAdelanto.setSelected(currentHcp.getAdelanto() == 1);
            FileHandler fh = new FileHandler();
            String id = this.currentHcp.getTipo_Documento() + "_" + currentHcp.getId() + "_" + currentHcp.getFECHA_DOCUMENTO();
            int count = fh.countMatchingfiles(logic.AppStaticValues.archivosFacturasContado, id);
            this.btnAtth.setText(count > 0 ? count + "" : "");
            res = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error " + e.getMessage());
            return false;
        }
        return res;
    }

    private void loadCtas() {

        this.cmbCuentaPres.removeAllItems();
        String selected = cmbDepartamento.getSelectedItem().toString();
        String depCod = selected.substring(0, selected.indexOf("-"));
        //load cuentas
        this.listaPresupuesto = crudp.obtenerPresupuestoPorDep(depCod);
        if (!this.listaPresupuesto.isEmpty()) {
            this.listaPresupuesto.forEach(e -> {

                if (e.getCODCONCEPTO().equals("00")) {
                    String cta = e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE();
                    this.cmbCuentaPres.addItem(cta);
                } else {
                    String cta = "   " + e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE();
                    this.cmbCuentaPres.addItem(cta);
                }
            });
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Inicio1;
    private javax.swing.JButton btnAbrirForm;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAtth;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteAbono;
    private javax.swing.JButton btnProveedorContado;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox ckAdelanto;
    private javax.swing.JComboBox<String> cmbCiaForm;
    private javax.swing.JComboBox<String> cmbCuentaPres;
    private javax.swing.JComboBox<String> cmbDepartamento;
    private javax.swing.JComboBox<String> cmbFiltCia;
    private javax.swing.JComboBox<String> cmbFiltroSaldo;
    private javax.swing.JComboBox<String> cmbFormaPago;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbTipoDocumento;
    private javax.swing.JComboBox<String> cmbTipoProv;
    private com.toedter.calendar.JDateChooser dtFechDoc;
    private com.toedter.calendar.JDateChooser dtFechaIni;
    private com.toedter.calendar.JDateChooser dtFin;
    private com.toedter.calendar.JDateChooser dtVencimiento;
    private javax.swing.JButton jButton1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbInicio;
    private javax.swing.JLabel lbTbInfo;
    private javax.swing.JMenuItem mnAbCant;
    private javax.swing.JMenuItem mnAbrirAdjuntos;
    private javax.swing.JMenuItem mnAgAb;
    private javax.swing.JMenuItem mnDeleteAb;
    private javax.swing.JMenuItem mnEdit;
    private javax.swing.JPanel pnlMoneda;
    private javax.swing.JPanel pnlPagar;
    private javax.swing.JPanel pnlPagarColones;
    private javax.swing.JPanel pnlSplit;
    private javax.swing.JPanel pnlTP;
    private javax.swing.JPanel pnlVen;
    private javax.swing.JTable tbHistoricoCP;
    private javax.swing.JTable tbProveedor;
    private javax.swing.JTable tblineasPC;
    private javax.swing.JTextField txtAbono;
    private javax.swing.JTextField txtAbonoC;
    private javax.swing.JTextField txtBuscarNombre;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextField txtMontoC;
    private javax.swing.JTextField txtNomProv;
    private javax.swing.JTextField txtNotas;
    private javax.swing.JTextField txtNumProv;
    private javax.swing.JTextArea txtResumen;
    // End of variables declaration//GEN-END:variables

    private void setDatesView() {
        IDateEditor dateEditor = dtFechDoc.getDateEditor();
        if (dateEditor instanceof JTextFieldDateEditor) {
            JTextFieldDateEditor txtFld = (JTextFieldDateEditor) dateEditor;
            txtFld.setBackground(Color.WHITE);

        }
        dateEditor = dtFechaIni.getDateEditor();
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

    private void setFormBtnMenu(boolean savev, boolean saveE, boolean adde, boolean addv) {
        this.btnSave.setVisible(savev);
        this.btnSave.setEnabled(saveE);
        this.btnAdd.setEnabled(adde);
        this.btnAdd.setVisible(addv);
    }

}

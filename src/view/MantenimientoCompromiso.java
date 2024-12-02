/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import data.DataUser;
import entitys.CompromisosProveedor;
import entitys.Proveedor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import services.SimpleExcelWriter;
import view.util.CustomMessages;
import view.util.JTableCommonFunctions;

/**
 *
 * @author eobregon
 */
public class MantenimientoCompromiso extends javax.swing.JPanel {

    /**
     * Creates new form MantenimientoCompromiso
     */
    private boolean loadingInfo;
    ArrayList<entitys.Proveedor> listaProv;
    ArrayList<entitys.CompromisosProveedor> listaComp;
    data.CrudProveedor crd;
    private Proveedor currentProv;
    private CompromisosProveedor currentCompromiso;

    public MantenimientoCompromiso() {
        initComponents();
        this.crd = new data.CrudProveedor();

        prepareGUI();
    }

    private void prepareGUI() {
        getCommonInfo();
        setListeners();
        setView();
    }

    private void getCommonInfo() {
        listaProv = crd.getProveedores();
        loadAsyncCompromisoInfo();
    }

    private void setDatesView() {
        IDateEditor dateEditor = jdtInicio.getDateEditor();
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

    private void setView() {
        setDatesView();
        jPanel20.setVisible(false);
        this.btnSave.setEnabled(false);
    }

    private void setListeners() {
        this.txtBuscarProveedor.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                serchRegisters(txtBuscarProveedor.getText());

            }

            public void removeUpdate(DocumentEvent e) {
                serchRegisters(txtBuscarProveedor.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                serchRegisters(txtBuscarProveedor.getText());
            }

        });
        // Add double click event
        tbProveedor.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tbProveedor.getSelectedRow();

                    if (row != -1) {
                        currentProv = Proveedor.obtProvPorCiaId(listaProv, tbProveedor.getValueAt(row, 0).toString());
                        txtProveedor.setText(currentProv.getNombre());
                        jDialog1.setVisible(false);
                    } else {
                        currentProv = null;
                    }
                }
            }
        });
        this.tbCompromiso.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tbCompromiso.rowAtPoint(e.getPoint());
                    tbCompromiso.addRowSelectionInterval(row, row);
                    jPopupMenu1.show(tbCompromiso, e.getX(), e.getY());
                } else {
                    currentProv = null;
                    currentCompromiso = null;
                    btnSave.setEnabled(false);
                    btnAdd.setEnabled(true);
                    refreshForm();
                }
            }
        });

    }

    private void serchRegisters(String text) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                if (!loadingInfo) {
                    loadingInfo = true;
                    DefaultTableModel model = (DefaultTableModel) tbProveedor.getModel();
                    JTableCommonFunctions.limpiarTabla(tbProveedor);
                    listaProv.forEach(e -> {
                        if (e.getNombre().toUpperCase().contains(text.toUpperCase())) {
                            addRowProveedor(model, e);
                        }
                    });
                    lbTbModalInfo.setText("Filas: " + tbProveedor.getRowCount());
                    loadingInfo = false;
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        //JOptionPane.showConfirmDialog(rootPane, "INFO " + txtBuscarNombre.getText());
    }

    private void addRowProveedor(DefaultTableModel model, entitys.Proveedor e) {
        model.addRow(new Object[]{
            e.getCIA_PROV(),
            e.getProveedor(),
            e.getNombre()
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
        jPanel27 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProveedor = new javax.swing.JTable();
        jPanel32 = new javax.swing.JPanel();
        lbTbModalInfo = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtBuscarProveedor = new javax.swing.JTextField();
        btnModalSerch = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        mnEdit = new javax.swing.JMenuItem();
        mnDelete = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCompromiso = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnExportExcel = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        lbFilas = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jdtInicio = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jdtFin = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cmbMonedaFilt = new javax.swing.JComboBox<>();
        jPanel22 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cmbEstadoFilt = new javax.swing.JComboBox<>();
        jPanel23 = new javax.swing.JPanel();
        btnRefreshFilt = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        lbResponsable = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtProveedor = new javax.swing.JTextField();
        btnSerchProv = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtMonto = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbPeriodo = new javax.swing.JComboBox<>();
        jPanel16 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbEstadoForm = new javax.swing.JComboBox<>();
        jPanel17 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtObservaciones = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        jDialog1.setTitle("Lista de proveedores");
        jDialog1.setMinimumSize(new java.awt.Dimension(600, 400));
        jDialog1.setModal(true);
        jDialog1.setPreferredSize(new java.awt.Dimension(600, 400));

        jPanel27.setLayout(new java.awt.BorderLayout());

        tbProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cia", "ID", "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbProveedor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbProveedor.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbProveedor);
        if (tbProveedor.getColumnModel().getColumnCount() > 0) {
            tbProveedor.getColumnModel().getColumn(1).setMinWidth(0);
            tbProveedor.getColumnModel().getColumn(1).setPreferredWidth(0);
            tbProveedor.getColumnModel().getColumn(2).setPreferredWidth(300);
        }

        jPanel27.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel32.setPreferredSize(new java.awt.Dimension(460, 25));
        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lbTbModalInfo.setText("Filas: 0");
        jPanel32.add(lbTbModalInfo);

        jPanel27.add(jPanel32, java.awt.BorderLayout.PAGE_END);

        jPanel33.setPreferredSize(new java.awt.Dimension(460, 35));
        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel13.setText("Nombre del proveedor");
        jPanel33.add(jLabel13);

        txtBuscarProveedor.setPreferredSize(new java.awt.Dimension(300, 26));
        jPanel33.add(txtBuscarProveedor);

        btnModalSerch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnModalSerch.setToolTipText("Buscar");
        jPanel33.add(btnModalSerch);

        jPanel27.add(jPanel33, java.awt.BorderLayout.NORTH);

        jDialog1.getContentPane().add(jPanel27, java.awt.BorderLayout.CENTER);

        jPanel28.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Dele doble click en la fila para esconger el proveedor");
        jPanel28.add(jLabel12);

        jDialog1.getContentPane().add(jPanel28, java.awt.BorderLayout.PAGE_START);

        jPanel29.setPreferredSize(new java.awt.Dimension(400, 10));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel29, java.awt.BorderLayout.PAGE_END);

        jPanel30.setPreferredSize(new java.awt.Dimension(10, 280));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel30, java.awt.BorderLayout.LINE_END);

        jPanel31.setPreferredSize(new java.awt.Dimension(10, 280));

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel31, java.awt.BorderLayout.LINE_START);

        mnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        mnEdit.setText("Editar fila");
        mnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnEditActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnEdit);

        mnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_round_icon_20x20.png"))); // NOI18N
        mnDelete.setText("Eliminar registro");
        mnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnDelete);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(153, 153, 153)), "Tabla compromisos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel24.setLayout(new java.awt.GridLayout(1, 0));

        tbCompromiso.setAutoCreateRowSorter(true);
        tbCompromiso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CIA", "Fecha creación", "Responsable", "Poveedor", "Moneda", "Monto", "Periodo", "Estado", "Observaciones", "ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCompromiso.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbCompromiso.setShowHorizontalLines(true);
        tbCompromiso.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbCompromiso);
        if (tbCompromiso.getColumnModel().getColumnCount() > 0) {
            tbCompromiso.getColumnModel().getColumn(0).setMinWidth(0);
            tbCompromiso.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbCompromiso.getColumnModel().getColumn(0).setMaxWidth(0);
            tbCompromiso.getColumnModel().getColumn(3).setPreferredWidth(300);
            tbCompromiso.getColumnModel().getColumn(5).setPreferredWidth(120);
            tbCompromiso.getColumnModel().getColumn(8).setPreferredWidth(400);
            tbCompromiso.getColumnModel().getColumn(9).setMinWidth(0);
            tbCompromiso.getColumnModel().getColumn(9).setPreferredWidth(0);
            tbCompromiso.getColumnModel().getColumn(9).setMaxWidth(0);
        }

        jPanel24.add(jScrollPane1);

        jPanel11.add(jPanel24, java.awt.BorderLayout.CENTER);

        jPanel25.setPreferredSize(new java.awt.Dimension(953, 33));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel25.setLayout(flowLayout1);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_round_icon_20x20.png"))); // NOI18N
        btnDelete.setToolTipText("Eliminar fila seleccionada");
        btnDelete.setPreferredSize(new java.awt.Dimension(28, 28));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel25.add(btnDelete);

        btnExportExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExportExcel.setToolTipText("Exportar a excel");
        btnExportExcel.setPreferredSize(new java.awt.Dimension(28, 28));
        btnExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportExcelActionPerformed(evt);
            }
        });
        jPanel25.add(btnExportExcel);

        jPanel11.add(jPanel25, java.awt.BorderLayout.PAGE_START);

        jPanel26.setPreferredSize(new java.awt.Dimension(953, 25));
        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lbFilas.setText("Filas");
        jPanel26.add(lbFilas);

        jPanel11.add(jPanel26, java.awt.BorderLayout.PAGE_END);

        jPanel6.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel12.setPreferredSize(new java.awt.Dimension(1052, 50));
        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel7.setText("Desde");
        jPanel20.add(jLabel7);

        jdtInicio.setDateFormatString("dd-MM-yyyy");
        jPanel20.add(jdtInicio);

        jLabel8.setText("hasta");
        jPanel20.add(jLabel8);

        jdtFin.setDateFormatString("dd-MM-yyyy");
        jdtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdtFinPropertyChange(evt);
            }
        });
        jPanel20.add(jdtFin);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel11.setToolTipText("Fecha de creación de los registros");
        jLabel11.setOpaque(true);
        jPanel20.add(jLabel11);

        jPanel12.add(jPanel20);

        jLabel9.setText("Moneda");
        jPanel21.add(jLabel9);

        cmbMonedaFilt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "CRC", "USD" }));
        cmbMonedaFilt.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaFiltItemStateChanged(evt);
            }
        });
        jPanel21.add(cmbMonedaFilt);

        jPanel12.add(jPanel21);

        jLabel10.setText("Estado");
        jPanel22.add(jLabel10);

        cmbEstadoFilt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));
        cmbEstadoFilt.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoFiltItemStateChanged(evt);
            }
        });
        jPanel22.add(cmbEstadoFilt);

        jPanel12.add(jPanel22);

        btnRefreshFilt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnRefreshFilt.setToolTipText("Refrescar filtros");
        btnRefreshFilt.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRefreshFilt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshFiltActionPerformed(evt);
            }
        });
        jPanel23.add(btnRefreshFilt);

        jPanel12.add(jPanel23);

        jPanel6.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)), "Formulario compromisos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel7.setPreferredSize(new java.awt.Dimension(1062, 110));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbResponsable.setText("Responsable");
        jPanel18.add(lbResponsable);

        jPanel13.add(jPanel18);

        jLabel1.setText("Proveedor");
        jLabel1.setToolTipText("");
        jPanel13.add(jLabel1);

        txtProveedor.setToolTipText("Pulse enter para buscar");
        txtProveedor.setPreferredSize(new java.awt.Dimension(350, 26));
        txtProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProveedorActionPerformed(evt);
            }
        });
        txtProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProveedorKeyPressed(evt);
            }
        });
        jPanel13.add(txtProveedor);

        btnSerchProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnSerchProv.setToolTipText("Buscar por descripción");
        btnSerchProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSerchProvActionPerformed(evt);
            }
        });
        jPanel13.add(btnSerchProv);

        jPanel7.add(jPanel13);

        jLabel3.setText("Moneda");
        jPanel14.add(jLabel3);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CRC", "USD" }));
        jPanel14.add(cmbMoneda);

        jLabel2.setText("Monto");
        jPanel14.add(jLabel2);

        txtMonto.setPreferredSize(new java.awt.Dimension(110, 22));
        jPanel14.add(txtMonto);

        jPanel7.add(jPanel14);

        jLabel4.setText("Periodo");
        jPanel15.add(jLabel4);

        cmbPeriodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semanal", "Mensual" }));
        jPanel15.add(cmbPeriodo);

        jPanel7.add(jPanel15);

        jLabel5.setText("Estado");
        jPanel16.add(jLabel5);

        cmbEstadoForm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));
        jPanel16.add(cmbEstadoForm);

        jPanel7.add(jPanel16);

        jLabel6.setText("Observaciones");
        jLabel6.setToolTipText("");
        jPanel17.add(jLabel6);

        txtObservaciones.setToolTipText("Buscar por número proveedor");
        txtObservaciones.setPreferredSize(new java.awt.Dimension(350, 22));
        jPanel17.add(txtObservaciones);

        jPanel7.add(jPanel17);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_icon_25x25_2.png"))); // NOI18N
        btnAdd.setToolTipText("Agregar nuevo registro");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel19.add(btnAdd);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save-25.png"))); // NOI18N
        btnSave.setToolTipText("Guardar cambios");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel19.add(btnSave);

        jPanel7.add(jPanel19);

        jPanel1.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel8.setPreferredSize(new java.awt.Dimension(1062, 10));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 987, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jPanel9.setPreferredSize(new java.awt.Dimension(5, 336));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel9, java.awt.BorderLayout.LINE_END);

        jPanel10.setPreferredSize(new java.awt.Dimension(5, 336));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel10, java.awt.BorderLayout.LINE_START);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(1102, 20));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jProgressBar1.setOpaque(true);
        jProgressBar1.setStringPainted(true);
        jPanel2.add(jProgressBar1);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(1102, 1));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1027, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(20, 456));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(20, 456));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshFiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshFiltActionPerformed
        // TODO add your handling code here:
        loadingInfo = true;
        this.cmbMonedaFilt.setSelectedIndex(0);
        this.cmbEstadoFilt.setSelectedIndex(0);
        loadingInfo = false;
        loadAsyncCompromisoInfo();

    }//GEN-LAST:event_btnRefreshFiltActionPerformed

    private void txtProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProveedorKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == evt.VK_ENTER && !loadingInfo) {
            this.txtBuscarProveedor.setText(txtProveedor.getText());
            this.jDialog1.setLocationRelativeTo(null);
            this.jDialog1.setVisible(true);

        }
    }//GEN-LAST:event_txtProveedorKeyPressed

    private void txtProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProveedorActionPerformed

    private void btnSerchProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSerchProvActionPerformed
        // TODO add your handling code here:
        this.txtBuscarProveedor.setText(txtProveedor.getText());
        this.jDialog1.setVisible(true);
    }//GEN-LAST:event_btnSerchProvActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        data.CrudCompromisoProveedor cr = new data.CrudCompromisoProveedor();
        entitys.CompromisosProveedor c = getCompromisoFromForm();
        if (c != null) {
            boolean res = cr.agregarCompromisosProveedor(c);
            if (res) {
                refreshInfo();
                JOptionPane.showMessageDialog(null, "Se ha agregado un registro nuevo");
                this.currentProv = null;
                refreshForm();
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error agregando un registro nuevo");
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void jdtFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdtFinPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().contains("date")) {
            loadAsyncCompromisoInfo();
        }
    }//GEN-LAST:event_jdtFinPropertyChange

    private void mnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnEditActionPerformed
        // TODO add your handling code here:

        if (tbCompromiso.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione una sóla fila");
        } else {
            int row = tbCompromiso.getSelectedRow();
            if (row > -1) {

                int id = Integer.parseInt(tbCompromiso.getValueAt(row, 9).toString());
                data.CrudCompromisoProveedor cr = new data.CrudCompromisoProveedor();
                this.currentCompromiso = cr.obtenerCompromisosProveedorPorId(id);
                if (this.currentCompromiso != null) {
                    this.btnAdd.setEnabled(false);
                    this.btnSave.setEnabled(true);
                    loadFormFromComp();
                } else {
                    this.btnAdd.setEnabled(true);
                    this.currentCompromiso = null;
                    this.btnSave.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_mnEditActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        data.CrudCompromisoProveedor cr = new data.CrudCompromisoProveedor();
        entitys.CompromisosProveedor c = getCompromisoFromForm();
        if (c != null && this.currentCompromiso != null) {
            c.setId(this.currentCompromiso.getId());
            boolean res = cr.actCompromisosProveedor(c);
            if (res) {
                refreshInfo();
                JOptionPane.showMessageDialog(null, "Se ha actualizado un registro nuevo");
                this.currentProv = null;
                refreshForm();
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error actualizando un registro");
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        deleteRegister();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void mnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeleteActionPerformed
        // TODO add your handling code here:
        deleteRegister();
    }//GEN-LAST:event_mnDeleteActionPerformed

    private void cmbMonedaFiltItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaFiltItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncCompromisoInfo();
        }
    }//GEN-LAST:event_cmbMonedaFiltItemStateChanged

    private void cmbEstadoFiltItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoFiltItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncCompromisoInfo();
        }
    }//GEN-LAST:event_cmbEstadoFiltItemStateChanged

    private void btnExportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportExcelActionPerformed
        // TODO add your handling code here:
        if (tbCompromiso.getRowCount() > 0) {
            SimpleExcelWriter se = new SimpleExcelWriter();
            boolean res = se.writeJtableExcelFile(tbCompromiso, "Reporte de compromisos");
            if (res) {
                this.jProgressBar1.setVisible(true);
                this.jProgressBar1.setString("Excel guardadado correctamente");
                CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay elementos para el reporte");
        }
    }//GEN-LAST:event_btnExportExcelActionPerformed
    private void deleteRegister() {
        int row = tbCompromiso.getSelectedRow();
        int countSelected = tbCompromiso.getSelectedRowCount();
        if (row > -1 && countSelected == 1) {
            int id = (int) tbCompromiso.getValueAt(row, 9);
            data.CrudCompromisoProveedor cr = new data.CrudCompromisoProveedor();
            boolean res = cr.deleteCompromisosProveedor(id);
            if (res) {
                refreshInfo();
                JOptionPane.showMessageDialog(null, "Se ha eliminado un registro");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar un registro");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un elemento");
        }
    }

    private void loadFormFromComp() {

        this.txtMonto.setText(String.format("%.2f", this.currentCompromiso.getMonto()));
        this.txtObservaciones.setText(this.currentCompromiso.getObservaciones());
        this.txtProveedor.setText(this.currentCompromiso.getNombreProveedor());
        this.cmbMoneda.setSelectedItem(this.currentCompromiso.getMoneda());
        this.cmbPeriodo.setSelectedIndex(this.currentCompromiso.getPeriodo());
        this.cmbEstadoForm.setSelectedItem(this.currentCompromiso.getEstado() == 1 ? "Activo" : "Inactivo");
    }

    private void refreshInfo() {
        loadAsyncCompromisoInfo();
    }

    private void loadAsyncCompromisoInfo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                enableFilts(false);
                jProgressBar1.setVisible(true);
                jProgressBar1.setValue(jProgressBar1.getMaximum());
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                jProgressBar1.setString("Cargando información...");
                JTableCommonFunctions.limpiarTabla(tbCompromiso);
                data.CrudCompromisoProveedor cr = new data.CrudCompromisoProveedor();
                /*java.util.Date inicio = jdtInicio.getDate();
                java.util.Date fin = jdtFin.getDate();
                if (inicio != null && fin != null) {
                    listaComp = cr.obtenerCompromisosProveedorPPorFechas(inicio, fin, activo, moneda);
                }*/
                int activo = cmbEstadoFilt.getSelectedIndex() == 0 ? 1 : 0;
                String moneda = cmbMonedaFilt.getSelectedItem().toString();
                listaComp = cr.obtenerCompromisosProveedor(activo, moneda);
                DefaultTableModel model = (DefaultTableModel) tbCompromiso.getModel();

                listaComp.forEach(e -> {
                    addRow(model, e);
                });
                lbFilas.setText("Filas: " + tbCompromiso.getRowCount());
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                jProgressBar1.setString("Información cargada...");
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                enableFilts(true);
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void enableFilts(boolean enable) {
        this.btnDelete.setEnabled(enable);
        this.cmbEstadoFilt.setEnabled(enable);
        this.cmbMonedaFilt.setEnabled(enable);
        this.tbCompromiso.setEnabled(enable);
    }

    private void refreshForm() {
        this.txtProveedor.setText("");
        this.txtMonto.setText("");
        this.txtObservaciones.setText("");
        this.cmbMoneda.setSelectedIndex(0);
        this.cmbEstadoForm.setSelectedIndex(0);
        this.cmbPeriodo.setSelectedIndex(0);
    }

    private void addRow(DefaultTableModel model, entitys.CompromisosProveedor c) {

        model.addRow(new Object[]{
            c.getCia_prov(),
            logic.AppStaticValues.dateFormat.format(c.getFechaCreacion()),
            c.getResponsable(),
            c.getNombreProveedor(),
            c.getMoneda(),
            String.format("%.2f", c.getMonto()),
            c.getPeriodo() == 0 ? "Semanal" : "Mensual",
            c.getEstado() == 0 ? "Inactivo" : "Activo",
            c.getObservaciones(),
            c.getId()
        });

    }

    private entitys.CompromisosProveedor getCompromisoFromForm() {
        entitys.CompromisosProveedor res = new entitys.CompromisosProveedor();
        try {
            res.setMonto(Double.parseDouble(this.txtMonto.getText()));
            res.setNombreProveedor(txtProveedor.getText());
            res.setCia_prov(this.currentProv == null ? "ND" : this.currentProv.getCIA_PROV());
            res.setProveedor(this.currentProv == null ? "ND" : this.currentProv.getProveedor());
            res.setFechaCreacion(new java.util.Date());
            res.setResponsable(DataUser.username);
            res.setObservaciones(txtObservaciones.getText());
            res.setMoneda(this.cmbMoneda.getSelectedItem().toString());
            int estado = this.cmbEstadoForm.getSelectedIndex() == 0 ? 1 : 0;
            res.setEstado(estado);
            int periodo = this.cmbPeriodo.getSelectedIndex();
            res.setPeriodo(periodo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error " + e.getMessage() + "!\nAsegurese de que la información es correcta");
            return null;
        }
        return res;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExportExcel;
    private javax.swing.JButton btnModalSerch;
    private javax.swing.JButton btnRefreshFilt;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSerchProv;
    private javax.swing.JComboBox<String> cmbEstadoFilt;
    private javax.swing.JComboBox<String> cmbEstadoForm;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbMonedaFilt;
    private javax.swing.JComboBox<String> cmbPeriodo;
    private javax.swing.JDialog jDialog1;
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
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
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
    private com.toedter.calendar.JDateChooser jdtFin;
    private com.toedter.calendar.JDateChooser jdtInicio;
    private javax.swing.JLabel lbFilas;
    private javax.swing.JLabel lbResponsable;
    private javax.swing.JLabel lbTbModalInfo;
    private javax.swing.JMenuItem mnDelete;
    private javax.swing.JMenuItem mnEdit;
    private javax.swing.JTable tbCompromiso;
    private javax.swing.JTable tbProveedor;
    private javax.swing.JTextField txtBuscarProveedor;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextField txtObservaciones;
    private javax.swing.JTextField txtProveedor;
    // End of variables declaration//GEN-END:variables
}

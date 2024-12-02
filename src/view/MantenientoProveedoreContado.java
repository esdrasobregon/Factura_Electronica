/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import data.CrudProvContado.CrudProvedorContado;
import data.DataUser;
import entitys.ProveedorContado.CuentaProveedorContado;
import entitys.ProveedorContado.ProveedorContado;
import entitys.ProveedorContado.TelefonoSinpeContado;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import view.util.JTableCommonFunctions;

/**
 *
 * @author eobregon
 */
public class MantenientoProveedoreContado extends javax.swing.JFrame {

    /**
     * Creates new form MantenientoProveedoreContado
     */
    ArrayList<entitys.ProveedorContado.ProveedorContado> listaProv;
    ProveedorContado currentProv;
    data.CrudProvContado.CrudProvedorContado crd;
    boolean loadingInfo = false;

    public MantenientoProveedoreContado() {
        initComponents();
        this.crd = new CrudProvedorContado();
        prepareGUI();
    }

    private void prepareGUI() {
        loadInfo();
        setView();
    }

    private void loadInfo() {
        this.listaProv = crd.obtenerListaProveedorContado("");
    }

    private void setView() {
        tbProveedor.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(getCmbAcInact()));
        tbCuentas.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(getCmbAcInact()));
        tbSinpes.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(getCmbAcInact()));
        DefaultTableModel model = (DefaultTableModel) this.tbListProv.getModel();
        this.listaProv.forEach(e -> {
            addRowProveedor(model, e);
        });
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
        this.tbListProv.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarInformacionProv();
                }
            }
        }
        );
        serTbProveedorEventListeners();
        serTbCtasEventListeners();
        serTbSinpesEventListeners();
        this.setLocationRelativeTo(null);
    }

    private void serTbProveedorEventListeners() {
        tbProveedor.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        tbProveedor.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your action here
                updateAddTbProveedor();
            }
        });
    }

    private void serTbCtasEventListeners() {
        tbCuentas.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        tbCuentas.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your action here
                addTbCta();
            }
        });
    }

    private void serTbSinpesEventListeners() {
        tbSinpes.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        tbSinpes.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add your action here
                addTbTele();
            }
        });
    }

    private void mostrarInformacionProv() {
        int row = tbListProv.getSelectedRow();
        try {
            loadingInfo = true;
            JTableCommonFunctions.limpiarTabla(tbCuentas);
            JTableCommonFunctions.limpiarTabla(tbSinpes);
            JTableCommonFunctions.limpiarTabla(tbProveedor);
            int id = (int) tbListProv.getValueAt(row, 0);
            ProveedorContado prov = crd.obtenerListaProveedorContadoPorId(id);
            DefaultTableModel model = (DefaultTableModel) tbProveedor.getModel();
            DefaultTableModel modelCuentas = (DefaultTableModel) tbCuentas.getModel();
            DefaultTableModel modelsinpe = (DefaultTableModel) tbSinpes.getModel();
            ArrayList<entitys.ProveedorContado.TelefonoSinpeContado> telefonos = crd.obtenerListaSinpeProveedorContado(id);
            ArrayList<entitys.ProveedorContado.CuentaProveedorContado> cuentas = crd.obtenerListaCtaProveedorContado(id);
            prov.setCuentas(cuentas);
            prov.setTelefonos(telefonos);
            prov.getTelefonos().forEach(e -> {
                addRowTbSinpes(modelsinpe, e);
            });
            prov.getCuentas().forEach(e -> {
                addRowTbCuentas(modelCuentas, e);
            });
            addRowProveedor(model, prov);
            loadingInfo = false;
        } catch (Exception e) {
        }
    }

    private void serchRegisters() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                listaProv = crd.obtenerListaProveedorContado(txtBuscarNombre.getText());
                DefaultTableModel model = (DefaultTableModel) tbListProv.getModel();
                JTableCommonFunctions.limpiarTabla(tbListProv);
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

    private JComboBox getCmbAcInact() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Activo");
        comboBox.addItem("Inactivo");
        return comboBox;
    }

    private void addRowProveedor(DefaultTableModel model, ProveedorContado e) {
        model.addRow(new Object[]{
            e.getIdProveedorContado(),
            e.getNombre(),
            e.getCodigo(),
            e.getCedulaJuridica(),
            e.getEstado() == 1 ? "Activo" : "Inactivo",
            e.getTotalCuentas(),
            e.getSinpes(),});
    }

    private void addRowTbCuentas(DefaultTableModel model, entitys.ProveedorContado.CuentaProveedorContado e) {
        model.addRow(new Object[]{
            e.getIdProveedorContado(),
            e.getIdCuentaContado(),
            e.getBanco(),
            e.getNumero(),
            e.getCreado(),
            e.getEstado() == 1 ? "Activo" : "Inactivo"
        });
    }

    private void addRowTbSinpes(DefaultTableModel model, entitys.ProveedorContado.TelefonoSinpeContado e) {
        model.addRow(new Object[]{
            e.getProveedorContado_idProveedorContado(),
            e.getIdTelefonoSinpeContado(),
            e.getNumero(),
            e.getCreado(),
            e.getEstado() == 1 ? "Activo" : "Inactivo"});
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        center = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProveedor = new javax.swing.JTable();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCuentas = new javax.swing.JTable();
        jPanel33 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbSinpes = new javax.swing.JTable();
        jPanel31 = new javax.swing.JPanel();
        btnAddProvTb = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        first = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarNombre = new javax.swing.JTextField();
        btnBuscarProv = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbListProv = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        south = new javax.swing.JPanel();
        rigth = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mantenimiento de proveedores contado");
        setMinimumSize(new java.awt.Dimension(800, 400));
        setPreferredSize(new java.awt.Dimension(800, 500));

        center.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla proveedores contado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        center.setLayout(new java.awt.BorderLayout());

        jPanel30.setPreferredSize(new java.awt.Dimension(1194, 20));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1011, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        center.add(jPanel30, java.awt.BorderLayout.PAGE_END);

        jPanel28.setLayout(new java.awt.GridLayout(1, 0));

        tbProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdProveedor", "Nombre", "Código", "Cédula juridica", "Estado", "Cuentas", "Sinpes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbProveedor.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbProveedor.setShowHorizontalLines(true);
        tbProveedor.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbProveedor);
        if (tbProveedor.getColumnModel().getColumnCount() > 0) {
            tbProveedor.getColumnModel().getColumn(0).setMinWidth(0);
            tbProveedor.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbProveedor.getColumnModel().getColumn(0).setMaxWidth(0);
            tbProveedor.getColumnModel().getColumn(1).setPreferredWidth(300);
            tbProveedor.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbProveedor.getColumnModel().getColumn(3).setPreferredWidth(200);
        }

        jPanel28.add(jScrollPane1);

        jTabbedPane1.addTab("Proveedor", jPanel28);

        jPanel32.setLayout(new java.awt.GridLayout(1, 0));

        tbCuentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idProveedor", "idCuenta", "Banco", "Cuenta", "Creado", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCuentas.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbCuentas.setShowHorizontalLines(true);
        tbCuentas.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbCuentas);
        if (tbCuentas.getColumnModel().getColumnCount() > 0) {
            tbCuentas.getColumnModel().getColumn(0).setMinWidth(0);
            tbCuentas.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbCuentas.getColumnModel().getColumn(0).setMaxWidth(0);
            tbCuentas.getColumnModel().getColumn(1).setMinWidth(0);
            tbCuentas.getColumnModel().getColumn(1).setPreferredWidth(0);
            tbCuentas.getColumnModel().getColumn(1).setMaxWidth(0);
            tbCuentas.getColumnModel().getColumn(2).setPreferredWidth(200);
            tbCuentas.getColumnModel().getColumn(3).setMinWidth(200);
            tbCuentas.getColumnModel().getColumn(3).setPreferredWidth(200);
            tbCuentas.getColumnModel().getColumn(4).setPreferredWidth(100);
            tbCuentas.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        jPanel32.add(jScrollPane2);

        jTabbedPane1.addTab("Cuentas", jPanel32);

        jPanel33.setLayout(new java.awt.GridLayout(1, 0));

        tbSinpes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idProveedor", "id", "Número", "Creado", "Estado"
            }
        ));
        tbSinpes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbSinpes.setShowHorizontalLines(true);
        tbSinpes.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tbSinpes);
        if (tbSinpes.getColumnModel().getColumnCount() > 0) {
            tbSinpes.getColumnModel().getColumn(0).setMinWidth(0);
            tbSinpes.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbSinpes.getColumnModel().getColumn(0).setMaxWidth(0);
            tbSinpes.getColumnModel().getColumn(1).setMinWidth(0);
            tbSinpes.getColumnModel().getColumn(1).setPreferredWidth(0);
            tbSinpes.getColumnModel().getColumn(1).setMaxWidth(0);
            tbSinpes.getColumnModel().getColumn(2).setPreferredWidth(120);
            tbSinpes.getColumnModel().getColumn(3).setPreferredWidth(120);
            tbSinpes.getColumnModel().getColumn(4).setPreferredWidth(120);
        }

        jPanel33.add(jScrollPane3);

        jTabbedPane1.addTab("Telefonos SINPE", jPanel33);

        center.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnAddProvTb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_icon_25x25_2.png"))); // NOI18N
        btnAddProvTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProvTbActionPerformed(evt);
            }
        });
        jPanel31.add(btnAddProvTb);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_icon_25x25.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel31.add(btnDelete);

        center.add(jPanel31, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        first.setPreferredSize(new java.awt.Dimension(800, 200));
        first.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(979, 60));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setText("Ingrese el nombre");
        jPanel1.add(jLabel1);

        txtBuscarNombre.setPreferredSize(new java.awt.Dimension(300, 25));
        txtBuscarNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarNombreActionPerformed(evt);
            }
        });
        jPanel1.add(txtBuscarNombre);

        btnBuscarProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBuscarProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProvActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarProv);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel2.setToolTipText("Seleccione un resultado para editar");
        jPanel1.add(jLabel2);

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setPreferredSize(new java.awt.Dimension(1063, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1063, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        first.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel35.setPreferredSize(new java.awt.Dimension(1132, 150));
        jPanel35.setLayout(new java.awt.BorderLayout(0, 5));

        jScrollPane4.setBorder(null);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(452, 120));

        tbListProv.setBackground(new java.awt.Color(60, 63, 65));
        tbListProv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idProveedor", "Nombre", "Codigo", "cedula", "estado", "cuentas", "sinpes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbListProv.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbListProv.setFillsViewportHeight(true);
        jScrollPane4.setViewportView(tbListProv);
        if (tbListProv.getColumnModel().getColumnCount() > 0) {
            tbListProv.getColumnModel().getColumn(0).setMinWidth(0);
            tbListProv.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbListProv.getColumnModel().getColumn(0).setMaxWidth(0);
            tbListProv.getColumnModel().getColumn(1).setMinWidth(400);
            tbListProv.getColumnModel().getColumn(1).setPreferredWidth(400);
            tbListProv.getColumnModel().getColumn(2).setMinWidth(0);
            tbListProv.getColumnModel().getColumn(2).setPreferredWidth(0);
            tbListProv.getColumnModel().getColumn(2).setMaxWidth(0);
            tbListProv.getColumnModel().getColumn(3).setMinWidth(0);
            tbListProv.getColumnModel().getColumn(3).setPreferredWidth(0);
            tbListProv.getColumnModel().getColumn(3).setMaxWidth(0);
            tbListProv.getColumnModel().getColumn(4).setMinWidth(0);
            tbListProv.getColumnModel().getColumn(4).setPreferredWidth(0);
            tbListProv.getColumnModel().getColumn(4).setMaxWidth(0);
            tbListProv.getColumnModel().getColumn(5).setMinWidth(0);
            tbListProv.getColumnModel().getColumn(5).setPreferredWidth(0);
            tbListProv.getColumnModel().getColumn(5).setMaxWidth(0);
            tbListProv.getColumnModel().getColumn(6).setMinWidth(0);
            tbListProv.getColumnModel().getColumn(6).setPreferredWidth(0);
            tbListProv.getColumnModel().getColumn(6).setMaxWidth(0);
        }

        jPanel35.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel4.setPreferredSize(new java.awt.Dimension(400, 140));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        jPanel35.add(jPanel4, java.awt.BorderLayout.EAST);

        jPanel6.setPreferredSize(new java.awt.Dimension(20, 140));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        jPanel35.add(jPanel6, java.awt.BorderLayout.WEST);

        first.add(jPanel35, java.awt.BorderLayout.CENTER);

        getContentPane().add(first, java.awt.BorderLayout.PAGE_START);

        south.setPreferredSize(new java.awt.Dimension(694, 30));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1063, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        rigth.setPreferredSize(new java.awt.Dimension(20, 500));

        javax.swing.GroupLayout rigthLayout = new javax.swing.GroupLayout(rigth);
        rigth.setLayout(rigthLayout);
        rigthLayout.setHorizontalGroup(
            rigthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        rigthLayout.setVerticalGroup(
            rigthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        getContentPane().add(rigth, java.awt.BorderLayout.EAST);

        jPanel5.setPreferredSize(new java.awt.Dimension(20, 427));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel5, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addTbProveedor() {
        int row = tbProveedor.getSelectedRow();
        try {
            entitys.ProveedorContado.ProveedorContado p = new ProveedorContado();
            p.setCedulaJuridica((String) tbProveedor.getValueAt(row, 3));
            p.setCodigo((String) tbProveedor.getValueAt(row, 2));
            p.setNombre((String) tbProveedor.getValueAt(row, 1));
            String estado = (String) tbProveedor.getValueAt(row, 4);
            p.setEstado(estado.equalsIgnoreCase("Activo") ? 1 : 0);
            p.setFechaCreacion(new java.util.Date());
            p.setUltimaModificacion(new java.util.Date());
            p.setUsuarioCreador(DataUser.username);

            data.CrudProvContado.CrudProvedorContado cr = new CrudProvedorContado();
            boolean res = cr.agregarProveedorContado(p);
            if (res) {
                entitys.ProveedorContado.ProveedorContado result = crd.obtenerListaProveedorCont(p);
                if (result != null) {
                    JTableCommonFunctions.limpiarTabla(tbProveedor);
                    DefaultTableModel model = (DefaultTableModel) tbProveedor.getModel();
                    addRowProveedor(model, result);
                    JOptionPane.showMessageDialog(null, "Se a agregado un proveedor de contado");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar un proveedor de contado");
            }
        } catch (Exception e) {
            System.out.println("view.MantenientoProveedoreContado.addTbProveedor()");
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error " + e.getMessage());
        }
    }

    private void updateProv(int id) {
        int row = tbProveedor.getSelectedRow();
        try {
            entitys.ProveedorContado.ProveedorContado p = crd.obtenerListaProveedorContadoPorId(id);
            p.setCedulaJuridica((String) tbProveedor.getValueAt(row, 3));
            p.setCodigo((String) tbProveedor.getValueAt(row, 2));
            p.setNombre((String) tbProveedor.getValueAt(row, 1));
            String estado = (String) tbProveedor.getValueAt(row, 4);
            p.setEstado(estado.equalsIgnoreCase("Activo") ? 1 : 0);
            p.setUltimaModificacion(new java.util.Date());

            boolean res = crd.actualizarProveedorContado(p);
            if (res) {

                JOptionPane.showMessageDialog(null, "Se a actualizado un proveedor de contado");

            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al actualizar un proveedor de contado");
            }
        } catch (Exception e) {
            System.out.println("view.MantenientoProveedoreContado.addTbProveedor()");
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error " + e.getMessage());
        }
    }

    private void updateAddTbProveedor() {
        int idProv = getIdProv();
        if (idProv == -1) {
            addTbProveedor();
        } else {
            //update
            updateProv(idProv);
        }
    }

    private int getIdProv() {

        try {
            int idProv = (int) tbProveedor.getValueAt(0, 0);
            return idProv;
        } catch (Exception e) {
            return -1;
        }
    }

    private void addTbCta() {
        int row = tbCuentas.getSelectedRow();
        int idCta = getCtaId(row);
        if (idCta == -1) {
            agregarCuenta();

        } else {
            //actualizar
            actualizarCuenta();
        }

    }

    private void agregarCuenta() {
        int idProv = (int) tbProveedor.getValueAt(0, 0);
        entitys.ProveedorContado.CuentaProveedorContado cta = new CuentaProveedorContado();
        int rowCta = tbCuentas.getSelectedRow();
        cta.setCreado(new java.util.Date());
        cta.setBanco((String) tbCuentas.getValueAt(rowCta, 2));
        cta.setNumero((String) tbCuentas.getValueAt(rowCta, 3));
        String estado = (String) tbCuentas.getValueAt(rowCta, 5);
        cta.setEstado(estado.equalsIgnoreCase("Activo") ? 1 : 0);
        cta.setIdProveedorContado(idProv);
        boolean res = crd.agregarCtaProveedorContado(cta);
        if (res) {
            entitys.ProveedorContado.CuentaProveedorContado result = crd.obtenerCtaProveedor(cta);
            if (result != null) {
                tbCuentas.setValueAt(result.getIdProveedorContado(), rowCta, 0);
                tbCuentas.setValueAt(result.getIdCuentaContado(), rowCta, 1);
                tbCuentas.setValueAt(result.getBanco(), rowCta, 2);
                tbCuentas.setValueAt(result.getNumero(), rowCta, 3);
                tbCuentas.setValueAt(cta.getCreado(), rowCta, 4);
                tbCuentas.setValueAt(result.getEstado() == 1 ? "Activo" : "Inactivo", rowCta, 5);
            }
            JOptionPane.showMessageDialog(null, "Se ha agregado una cuenta");
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error agregando una cuenta");
        }
    }

    private void actualizarCuenta() {
        int row = tbCuentas.getSelectedRow();
        int idCta = getCtaId(row);
        int idProv = (int) tbProveedor.getValueAt(0, 0);
        entitys.ProveedorContado.CuentaProveedorContado cta = crd.obtenerListaCtaProveedorContadoPorId(idCta);
        int rowCta = tbCuentas.getSelectedRow();
        cta.setCreado(new java.util.Date());
        cta.setBanco((String) tbCuentas.getValueAt(rowCta, 2));
        cta.setNumero((String) tbCuentas.getValueAt(rowCta, 3));
        String estado = (String) tbCuentas.getValueAt(rowCta, 5);
        cta.setEstado(estado.equalsIgnoreCase("Activo") ? 1 : 0);
        cta.setIdProveedorContado(idProv);
        boolean res = crd.actualizarCtaProveedorContado(cta);
        if (res) {

            JOptionPane.showMessageDialog(null, "Se ha actualizado la cuenta");
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error actualizar la cuenta");
        }
    }

    private int getTeleId(int row) {
        try {
            return (int) tbSinpes.getValueAt(row, 1);
        } catch (Exception e) {
        }
        return -1;
    }

    private int getCtaId(int row) {
        try {
            return (int) tbCuentas.getValueAt(row, 1);
        } catch (Exception e) {
        }
        return -1;
    }

    private void addTbTele() {
        int row = tbSinpes.getSelectedRow();
        int idSinpe = getTeleId(row);
        if (idSinpe == -1) {
            agregarTelefono(row);
        } else {
            //actualizar
            actualizarTelefono(row);
        }

    }

    private void actualizarTelefono(int row) {
        try {

            int idTele = (int) tbSinpes.getValueAt(row, 1);
            entitys.ProveedorContado.TelefonoSinpeContado tel = crd.obtenerListaSinpeProveedorPorid(idTele);

            tel.setCreado(new java.util.Date());
            String estado = tbSinpes.getValueAt(row, 4).toString();
            tel.setEstado(estado.equalsIgnoreCase("Activo") ? 1 : 0);
            tel.setNumero(tbSinpes.getValueAt(row, 2).toString());

            boolean res = crd.actualizarSinpeProveedorContado(tel);
            if (res) {
                JOptionPane.showMessageDialog(null, "Se ha actualizado la cuenta SINPE");

            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error actualizando la cuenta SINPE");
            }
        } catch (Exception e) {
        }

    }

    private void agregarTelefono(int row) {
        entitys.ProveedorContado.TelefonoSinpeContado tel = new TelefonoSinpeContado();

        int idProv = (int) tbProveedor.getValueAt(0, 0);
        tel.setCreado(new java.util.Date());
        String estado = tbSinpes.getValueAt(row, 4).toString();
        tel.setEstado(estado.equalsIgnoreCase("Activo") ? 1 : 0);
        tel.setNumero(tbSinpes.getValueAt(row, 2).toString());
        tel.setProveedorContado_idProveedorContado(idProv);

        boolean res = crd.agregarSinpeProveedorContado(tel);
        if (res) {
            entitys.ProveedorContado.TelefonoSinpeContado t = crd.obtenerListaSinpeProveedorContado(tel);
            if (t != null) {
                tbSinpes.setValueAt(t.getProveedorContado_idProveedorContado(), row, 0);
                tbSinpes.setValueAt(t.getIdTelefonoSinpeContado(), row, 1);
                tbSinpes.setValueAt(t.getNumero(), row, 2);
                tbSinpes.setValueAt(t.getCreado(), row, 3);
                tbSinpes.setValueAt(t.getEstado() == 1 ? "Activo" : "Inactivo", row, 4);
                JOptionPane.showMessageDialog(null, "Se ha agregado una cuenta SINPE");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error agregando una cuenta SINPE");
        }
    }
    private void btnAddProvTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProvTbActionPerformed
        // TODO add your handling code here:
        prepararProcesoAgregar();
    }//GEN-LAST:event_btnAddProvTbActionPerformed

    private void txtBuscarNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarNombreActionPerformed

    private void btnBuscarProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProvActionPerformed
        // TODO add your handling code here:
        serchRegisters();

    }//GEN-LAST:event_btnBuscarProvActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        prepararProcesoEliminar();
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void prepararProcesoAgregar() {

        int tab = jTabbedPane1.getSelectedIndex();
        switch (tab) {
            case 0:
                handleAddProveedor();
                break;
            case 1:
                addNewRowCuenta();
                break;
            case 2:
                addNewRowTel();
                break;

        }

    }

    private void prepararProcesoEliminar() {

        int tab = jTabbedPane1.getSelectedIndex();
        switch (tab) {
            case 0:
                deleteProveedor();
                break;
            case 1:
                deleteCta();
                break;
            case 2:
                deleteTel();
                break;

        }

    }

    private void deleteTel() {
        try {
            int row = tbSinpes.getSelectedRow();
            int idTel = (int) tbSinpes.getValueAt(row, 1);
            boolean res = crd.deleteTelefono(idTel);
            if (res) {
                DefaultTableModel model = (DefaultTableModel) tbSinpes.getModel();
                if (tbSinpes.isEditing()) {
                    tbSinpes.getCellEditor().stopCellEditing();
                }
                model.removeRow(row);
                JOptionPane.showMessageDialog(null, "Se ha eliminado una cuenta SINPE");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar una cuenta SINPE");
            }
        } catch (Exception e) {
        }
    }

    private void deleteProveedor() {
        try {

            int idProv = (int) tbProveedor.getValueAt(0, 0);

            //boolean res = crd.deleteProveedorContado(idTel);
            if (crd.deleteTelefonoByIdProv(idProv)
                    && crd.deleteCtaContadoByIdProv(idProv)
                    && crd.deleteProveedorContado(idProv)) {
                JTableCommonFunctions.limpiarTabla(tbCuentas);
                JTableCommonFunctions.limpiarTabla(tbProveedor);
                JTableCommonFunctions.limpiarTabla(tbSinpes);
                JOptionPane.showMessageDialog(null, "Se ha eliminado un proveedor de contado");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar el proveedor");
            }
        } catch (Exception e) {
        }
    }

    private void deleteCta() {
        try {
            int row = tbCuentas.getSelectedRow();
            int idCta = (int) tbCuentas.getValueAt(row, 1);
            boolean res = crd.deleteCtaContado(idCta);
            if (res) {
                DefaultTableModel model = (DefaultTableModel) tbCuentas.getModel();
                if (tbCuentas.isEditing()) {
                    tbCuentas.getCellEditor().stopCellEditing();
                }
                model.removeRow(row);
                JOptionPane.showMessageDialog(null, "Se ha eliminado la cuenta");
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al eliminar la cuenta");
            }
        } catch (Exception e) {
        }
    }

    private void addNewRowTel() {
        try {
            int idprov = (int) tbProveedor.getValueAt(0, 0);
            DefaultTableModel modelT = (DefaultTableModel) tbSinpes.getModel();
            modelT.addRow(new Object[]{idprov});
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar una fila " + e.getMessage());
        }
    }

    private void handleAddProveedor() {
        loadingInfo = true;
        JTableCommonFunctions.limpiarTabla(tbProveedor);
        JTableCommonFunctions.limpiarTabla(tbCuentas);
        JTableCommonFunctions.limpiarTabla(tbSinpes);
        DefaultTableModel model = (DefaultTableModel) tbProveedor.getModel();
        model.addRow(new Object[]{});
        this.loadingInfo = false;
    }

    private void addNewRowCuenta() {
        try {
            int idprov = (int) tbProveedor.getValueAt(0, 0);
            DefaultTableModel modelC = (DefaultTableModel) tbCuentas.getModel();
            modelC.addRow(new Object[]{idprov});
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar cuenta " + e.getMessage());
        }
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
            java.util.logging.Logger.getLogger(MantenientoProveedoreContado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenientoProveedoreContado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenientoProveedoreContado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenientoProveedoreContado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MantenientoProveedoreContado().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddProvTb;
    private javax.swing.JButton btnBuscarProv;
    private javax.swing.JButton btnDelete;
    private javax.swing.JPanel center;
    private javax.swing.JPanel first;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel rigth;
    private javax.swing.JPanel south;
    private javax.swing.JTable tbCuentas;
    private javax.swing.JTable tbListProv;
    private javax.swing.JTable tbProveedor;
    private javax.swing.JTable tbSinpes;
    private javax.swing.JTextField txtBuscarNombre;
    // End of variables declaration//GEN-END:variables
}

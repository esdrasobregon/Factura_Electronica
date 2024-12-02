/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import entitys.Departamento;
import entitys.UsuariosPresupuesto;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author eobregon
 */
public class MantenimientoUsuarios extends javax.swing.JPanel {

    /**
     * Creates new form MantenimientoUsuarios
     */
    ArrayList<UsuariosPresupuesto> usuarios;
    data.CRUDUsuariosPresupuesto crup;
    ArrayList<Departamento> listaDepartamentos;
    boolean loadingInfo = false;

    public MantenimientoUsuarios(ArrayList<Departamento> listaDepartamentos) {
        initComponents();
        this.crup = new data.CRUDUsuariosPresupuesto();
        this.listaDepartamentos = listaDepartamentos;
        //this.listaDepartamentos.removeLast();
        prepareGUI();
    }

    private void prepareGUI() {
        loadInfo();
        setView();
        setListeners();
    }

    private void setListeners() {
        tbUsuarios.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {

                // Your code here to handle table content change event
                if (!loadingInfo && !ckAdd.isSelected()) {
                    loadingInfo = true;
                    int row = tbUsuarios.getSelectedRow();
                    int id = (int) tbUsuarios.getValueAt(row, 20);
                    UsuariosPresupuesto us = crup.obtenerUsuariosPorId(id);
                    UsuariosPresupuesto usF = obtUsDeTb();
                    usF.setId(us.getId());
                    boolean res = crup.addUpdate(usF);
                    if (res) {
                        jProgressBar1.setVisible(true);
                        removeUsuarioDeLista(id);
                        usuarios.add(usF);
                        jProgressBar1.setString("Información guardada...");
                        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                        //JOptionPane.showMessageDialog(null, "Se ha actualizado el usuario " + us.getDETA_USER());

                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error");
                    }
                    loadingInfo = false;

                }
            }
        });
        tbUsuarios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !loadingInfo && !ckAdd.isSelected()) {
                    int row = tbUsuarios.getSelectedRow();
                    int id = (int) tbUsuarios.getValueAt(row, 20);
                    UsuariosPresupuesto us = crup.obtenerUsuariosPorId(id);
                    if (us != null) {
                        lbUsuarioActual.setText("Usuario: " + us.getDETA_USER().trim() + " (" + us.getCOD_USER() + ")");
                    }

                }
            }
        }
        );
        txtBusqueda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuario();

            }
        });
        tbUsuarios.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {

                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    int column = tbUsuarios.getSelectedColumn();
                    int x = mouseEvent.getX();
                    int y = mouseEvent.getY();
                    int row = tbUsuarios.getSelectedRow();
                    if (row >= 0 && column >= 0) {
                        // Perform actions for right-click on a cell
                        int rowat = tbUsuarios.rowAtPoint(new Point(x, y));
                        tbUsuarios.columnAtPoint(new Point(x, y));
                        tbUsuarios.setRowSelectionInterval(rowat, rowat);
                        System.out.println("Right-clicked on Row: " + row + ", Column: " + column);
                        popUpTbUssuario.show(tbUsuarios, x, y);
                    }
                }
            }

        });
    }

    private void buscarUsuario() {
        loadingInfo = true;
        String buscar = txtBusqueda.getText().toUpperCase();
        ArrayList<UsuariosPresupuesto> lista = UsuariosPresupuesto.getUsuariosPorNombreYUsuario(buscar, usuarios);
        view.util.JTableCommonFunctions.limpiarTabla(tbUsuarios);
        DefaultTableModel model = (DefaultTableModel) tbUsuarios.getModel();
        lista.forEach(element -> {
            addRow(element, model);
        });
        this.lbTbInfo.setText("Filas: " + tbUsuarios.getRowCount());
        loadingInfo = false;
    }

    private void filtrarUsuario() {
        loadingInfo = true;
        int row = tbUsuarios.getSelectedRow();
        if (row > -1) {
            String buscar = tbUsuarios.getValueAt(row, 2).toString();
            ArrayList<UsuariosPresupuesto> lista = UsuariosPresupuesto.getUsuariosPorNombre(buscar, usuarios);
            view.util.JTableCommonFunctions.limpiarTabla(tbUsuarios);
            DefaultTableModel model = (DefaultTableModel) tbUsuarios.getModel();
            lista.forEach(element -> {
                addRow(element, model);
            });
            this.lbTbInfo.setText("Filas: " + tbUsuarios.getRowCount());
            loadingInfo = false;
        }
    }

    private UsuariosPresupuesto obtUsDeTb() {
        int row = tbUsuarios.getSelectedRow();
        UsuariosPresupuesto us = null;
        if (row > -1) {
            us = new UsuariosPresupuesto();

            String acceso = (boolean) tbUsuarios.getValueAt(row, 7) ? "S" : "N";
            int desglose = (boolean) tbUsuarios.getValueAt(row, 8) ? 1 : 0;
            int exactus = (boolean) tbUsuarios.getValueAt(row, 9) ? 1 : 0;
            int tipo = (boolean) tbUsuarios.getValueAt(row, 10) ? 1 : 0;
            int cp = (boolean) tbUsuarios.getValueAt(row, 11) ? 1 : 0;
            int cb = (boolean) tbUsuarios.getValueAt(row, 12) ? 1 : 0;
            int subt = (boolean) tbUsuarios.getValueAt(row, 13) ? 1 : 0;
            int mantPg = (boolean) tbUsuarios.getValueAt(row, 14) ? 1 : 0;
            int reportes = (boolean) tbUsuarios.getValueAt(row, 15) ? 1 : 0;
            int mantcpcontado = (boolean) tbUsuarios.getValueAt(row, 16) ? 1 : 0;
            int afa = (boolean) tbUsuarios.getValueAt(row, 17) ? 1 : 0;
            int gp = (boolean) tbUsuarios.getValueAt(row, 18) ? 1 : 0;
            int admUs = (boolean) tbUsuarios.getValueAt(row, 19) ? 1 : 0;
            int admComp = (boolean) tbUsuarios.getValueAt(row, 21) ? 1 : 0;
            int repPa = (boolean) tbUsuarios.getValueAt(row, 22) ? 1 : 0;
            int repHA = (boolean) tbUsuarios.getValueAt(row, 23) ? 1 : 0;
            us.setCOD_CIA("3101086415");
            us.setDETA_CIA("CILT 3101086415");
            us.setCOD_USER((String) tbUsuarios.getValueAt(row, 2));
            us.setDETA_USER((String) tbUsuarios.getValueAt(row, 3));
            us.setCOD_DEPA((String) tbUsuarios.getValueAt(row, 4));
            us.setDETA_DEPA((String) tbUsuarios.getValueAt(row, 5));
            us.setActivo((boolean) tbUsuarios.getValueAt(row, 6));
            us.setACCESO(acceso);
            us.setUsuarioConta(desglose);
            us.setExactus(exactus);
            us.setExactus_TC(tipo);
            us.setExactus_CP(cp);
            us.setExactus_CB(cb);
            us.setExactus_Subtipos(subt);
            us.setMantenimientoPagos(mantPg);
            us.setReportePagos(reportes);
            us.setHistoricoCP(mantcpcontado);
            us.setAdminFactSub(afa);
            us.setAdministradorGestionGastosPer(gp);
            us.setAdministracionUsuarios(admUs);
            us.setMantenimientoCompromisos(admComp);
            us.setReportePagoAplicados(repPa);
            us.setHistoricoAbonos(repHA);
        } else {
            return null;
        }
        return us;
    }

    private int getId() {
        int row = tbUsuarios.getSelectedRow();
        int id = -1;
        if (row > -1) {
            try {
                id = (int) tbUsuarios.getValueAt(row, 20);
            } catch (Exception e) {
                System.out.println("view.MantenimientoUsuarios.getId() error " + e.getMessage());
            }
        } else {
        }
        return id;
    }

    private void loadInfo() {
        loadingInfo = true;
        this.usuarios = crup.obtenerUsuarios();
        DefaultTableModel model = (DefaultTableModel) tbUsuarios.getModel();
        this.usuarios.forEach(e -> {
            try {
                addRow(e, model);
            } catch (Exception ex) {
                System.out.println("view.MantenimientoUsuarios.loadInfo() error id " + e.getId());
            }
        });
        this.lbTbInfo.setText("Filas: " + tbUsuarios.getRowCount());
        loadingInfo = false;
    }

    private void addRow(UsuariosPresupuesto e, DefaultTableModel model) {
        Departamento d = Departamento.getDepartamentoByCodDepa(listaDepartamentos, e.getCOD_DEPA());
        model.addRow(new Object[]{
            e.getCOD_CIA(),
            e.getDETA_CIA(),
            e.getCOD_USER(),
            e.getDETA_USER(),
            e.getCOD_DEPA(),
            d.getDescripcion(),//e.getDETA_DEPA(),
            e.isActivo(),
            e.getACCESO().equals("S") ? true : false,
            e.getUsuarioConta() == 0 ? false : true,
            e.getExactus() == 0 ? false : true,
            e.getExactus_TC() == 0 ? false : true,
            e.getExactus_CP() == 0 ? false : true,
            e.getExactus_CB() == 0 ? false : true,
            e.getExactus_Subtipos() == 0 ? false : true,
            e.getMantenimientoPagos() == 0 ? false : true,
            e.getReportePagos() == 0 ? false : true,
            e.getHistoricoCP() == 0 ? false : true,
            e.getAdminFactSub() == 0 ? false : true,
            e.getAdministradorGestionGastosPer() == 0 ? false : true,
            e.getAdministracionUsuarios() == 0 ? false : true,
            e.getId(),
            e.getMantenimientoCompromisos() == 0 ? false : true,
            e.getReportePagoAplicados() == 0 ? false : true,
            e.getHistoricoAbonos() == 0 ? false : true
        });

    }

    private void setView() {
        this.btnAgregarUs.setEnabled(false);
        this.jProgressBar1.setVisible(false);
        tbUsuarios.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(getCmbDepartamentos()));

    }

    public javax.swing.JComboBox getCmbDepartamentos() {
        javax.swing.JComboBox cmb = new javax.swing.JComboBox();

        cmb.addItem("");
        for (Departamento d : listaDepartamentos) {
            cmb.addItem(d.getDescripcion());
        }
        cmb.setSelectedItem("");

        cmb.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {

                //your code
            }
        }
        );
        return cmb;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popUpTbUssuario = new javax.swing.JPopupMenu();
        mnFilt = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsuarios = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        lbUsuarioActual = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        btnBsucarUs = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnrefresh = new javax.swing.JButton();
        ckAdd = new javax.swing.JCheckBox();
        btnAgregarUs = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lbTbInfo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();

        mnFilt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/filer_icon_png_25x25.png"))); // NOI18N
        mnFilt.setText("Mostrar solo este usuario");
        mnFilt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnFiltActionPerformed(evt);
            }
        });
        popUpTbUssuario.add(mnFilt);

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        tbUsuarios.setAutoCreateRowSorter(true);
        tbUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "COD_CIA", "DETA_CIA", "Usuario", "Nombre usuario", "COD_DEPA", "Departamento", "ACTIVO", "ACCESO", "Desgose facturas", "Exactus", "Tipo cambio", "Cuentas por pagar", "Control bancario", "Subtipos contabilidad", "Mantenimiento pagos", "Reporte Pagos (crédito y contado)", "Mantenimiento CP Contado", "Administracion de facturas asignadas", "Gastos periódicos", "Administrar usuarios", "id", "Mantenimiento compromisos", "Reporte pagos aplicados", "Hist. Abonos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true, true, true, false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbUsuarios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbUsuarios.setShowGrid(true);
        tbUsuarios.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbUsuarios);
        if (tbUsuarios.getColumnModel().getColumnCount() > 0) {
            tbUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
            tbUsuarios.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
            tbUsuarios.getColumnModel().getColumn(1).setMinWidth(0);
            tbUsuarios.getColumnModel().getColumn(1).setPreferredWidth(0);
            tbUsuarios.getColumnModel().getColumn(1).setMaxWidth(0);
            tbUsuarios.getColumnModel().getColumn(3).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(4).setMinWidth(0);
            tbUsuarios.getColumnModel().getColumn(4).setPreferredWidth(0);
            tbUsuarios.getColumnModel().getColumn(4).setMaxWidth(0);
            tbUsuarios.getColumnModel().getColumn(5).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(8).setPreferredWidth(130);
            tbUsuarios.getColumnModel().getColumn(10).setPreferredWidth(120);
            tbUsuarios.getColumnModel().getColumn(11).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(12).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(13).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(14).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(15).setPreferredWidth(300);
            tbUsuarios.getColumnModel().getColumn(16).setPreferredWidth(300);
            tbUsuarios.getColumnModel().getColumn(17).setPreferredWidth(300);
            tbUsuarios.getColumnModel().getColumn(18).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(19).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(20).setMinWidth(0);
            tbUsuarios.getColumnModel().getColumn(20).setPreferredWidth(0);
            tbUsuarios.getColumnModel().getColumn(20).setMaxWidth(0);
            tbUsuarios.getColumnModel().getColumn(21).setPreferredWidth(200);
            tbUsuarios.getColumnModel().getColumn(22).setPreferredWidth(200);
        }
        //tbUsuarios.getTableHeader().setPreferredSize(new java.awt.Dimension(jScrollPane2.getWidth(),30));
        tbUsuarios.getTableHeader().setBackground(new java.awt.Color(102,102,102));
        tbUsuarios.getTableHeader().setForeground(new java.awt.Color(255,255,255));
        //tbReceips.getTableHeader().setForeground(new java.awt.Color(201,201,201));
        tbUsuarios.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14) {});
        tbUsuarios.setBackground(new java.awt.Color(51, 51, 51));
        tbUsuarios.setRowHeight(25);
        tbUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        tbUsuarios.setForeground(new java.awt.Color(153, 153, 153));
        tbUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jPanel1.add(jScrollPane1);

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel7.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbUsuarioActual.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel9.add(lbUsuarioActual);

        jPanel7.add(jPanel9);

        jPanel10.setPreferredSize(new java.awt.Dimension(548, 40));
        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout2.setAlignOnBaseline(true);
        jPanel10.setLayout(flowLayout2);

        jLabel1.setText("Usuario");
        jPanel10.add(jLabel1);

        txtBusqueda.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtBusqueda.setPreferredSize(new java.awt.Dimension(300, 25));
        jPanel10.add(txtBusqueda);

        btnBsucarUs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBsucarUs.setToolTipText("Buscar por nombr o usuario");
        btnBsucarUs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBsucarUsActionPerformed(evt);
            }
        });
        jPanel10.add(btnBsucarUs);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_round_icon_20x20.png"))); // NOI18N
        btnDelete.setToolTipText("Eliminar usuario");
        btnDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel10.add(btnDelete);

        btnrefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnrefresh.setToolTipText("Refrescar información");
        btnrefresh.setPreferredSize(new java.awt.Dimension(30, 30));
        btnrefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrefreshActionPerformed(evt);
            }
        });
        jPanel10.add(btnrefresh);

        ckAdd.setText("Agregar");
        ckAdd.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckAddItemStateChanged(evt);
            }
        });
        jPanel10.add(ckAdd);

        btnAgregarUs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_icon_25x25_2.png"))); // NOI18N
        btnAgregarUs.setToolTipText("Agregar usuario");
        btnAgregarUs.setPreferredSize(new java.awt.Dimension(30, 30));
        btnAgregarUs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarUsActionPerformed(evt);
            }
        });
        jPanel10.add(btnAgregarUs);

        jPanel7.add(jPanel10);

        jPanel2.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel8.setPreferredSize(new java.awt.Dimension(805, 30));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel8.setLayout(flowLayout1);

        lbTbInfo.setText("Filas: 0");
        jPanel8.add(lbTbInfo);

        jPanel2.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setPreferredSize(new java.awt.Dimension(835, 15));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jProgressBar1.setOpaque(true);
        jProgressBar1.setString("");
        jProgressBar1.setStringPainted(true);
        jPanel3.add(jProgressBar1);

        add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(835, 10));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1006, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(15, 361));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_END);

        jPanel6.setPreferredSize(new java.awt.Dimension(15, 361));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );

        add(jPanel6, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBsucarUsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBsucarUsActionPerformed
        // TODO add your handling code here:
        buscarUsuario();
    }//GEN-LAST:event_btnBsucarUsActionPerformed

    private void btnrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrefreshActionPerformed
        // TODO add your handling code here:
        loadAsyncInfo();
    }//GEN-LAST:event_btnrefreshActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        int row = tbUsuarios.getSelectedRow();
        if (row > -1) {
            int id = (int) tbUsuarios.getValueAt(row, 20);
            boolean res = crup.delete(id);
            if (res) {
                this.loadingInfo = true;
                removeUsuarioDeLista(id);
                DefaultTableModel model = (DefaultTableModel) tbUsuarios.getModel();
                model.removeRow(row);
                JOptionPane.showMessageDialog(null, "Se ha eliminado el usuario");
                this.loadingInfo = false;
            } else {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error eliminando el usuario");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Favor seleccione una fila");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void removeUsuarioDeLista(int id) {

        int count = 0;
        boolean found = false;
        while (count < usuarios.size() && !found) {
            UsuariosPresupuesto us = usuarios.get(count);
            if (us.getId() == id) {
                usuarios.remove(us);
                found = true;
            }
            count++;
        }

    }
    private void btnAgregarUsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarUsActionPerformed
        // TODO add your handling code here:
        int row = tbUsuarios.getSelectedRow();
        if (row > -1) {
            UsuariosPresupuesto us = obtUsDeTb();
            UsuariosPresupuesto usdb = this.crup.obtenerUsuariosPorUsuarioYDep(us.getCOD_USER(), us.getCOD_DEPA());
            if (usdb == null) {
                boolean res = crup.add(us);
                if (res) {
                    DefaultTableModel model = (DefaultTableModel) tbUsuarios.getModel();
                    model.removeRow(row);
                    usuarios = crup.obtenerUsuarios();
                    JOptionPane.showMessageDialog(null, "Usuario agregado con exitosamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar el nuevo usuario");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El usuario " + us.getCOD_USER() + " ya existe para el departamento " + us.getCOD_DEPA());

            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay filas seleccionadas");
        }


    }//GEN-LAST:event_btnAgregarUsActionPerformed

    private void ckAddItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckAddItemStateChanged
        // TODO add your handling code here:
        if (ckAdd.isSelected()) {
            this.loadingInfo = true;
            lbUsuarioActual.setText("Agregando usuarios");
            view.util.JTableCommonFunctions.limpiarTabla(tbUsuarios);
            txtBusqueda.setEnabled(false);
            this.btnBsucarUs.setEnabled(false);
            this.btnAgregarUs.setEnabled(true);
            this.btnDelete.setEnabled(false);
            this.btnrefresh.setEnabled(false);
            prepareNewUser();
            this.loadingInfo = false;
        } else {
            loadAsyncInfo();
            this.btnAgregarUs.setEnabled(false);
            this.btnDelete.setEnabled(true);
            txtBusqueda.setEnabled(true);
            this.btnBsucarUs.setEnabled(true);
            this.btnrefresh.setEnabled(true);
            lbUsuarioActual.setText("");
        }

    }//GEN-LAST:event_ckAddItemStateChanged

    private void mnFiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnFiltActionPerformed
        // TODO add your handling code here:
        filtrarUsuario();
    }//GEN-LAST:event_mnFiltActionPerformed
    private void prepareNewUser() {
        DefaultTableModel model = (DefaultTableModel) tbUsuarios.getModel();
        for (int i = 0; i < listaDepartamentos.size() - 1; i++) {
            Departamento e = listaDepartamentos.get(i);
            model.addRow(new Object[]{
                "3101086415",
                "CILT 3101086415",
                "usuario",
                "Nombre de usuario",
                e.getDEPARTAMENTO(),
                e.getDescripcion(),//e.getDETA_DEPA(),
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                -1,
                false,
                false,
                false
            });
        }
    }

    private void loadAsyncInfo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                jProgressBar1.setVisible(true);
                jProgressBar1.setValue(0);
                txtBusqueda.setText("");
                loadingInfo = true;
                view.util.JTableCommonFunctions.limpiarTabla(tbUsuarios);
                usuarios = crup.obtenerUsuarios();
                DefaultTableModel model = (DefaultTableModel) tbUsuarios.getModel();
                jProgressBar1.setMaximum(usuarios.size());
                usuarios.forEach(e -> {
                    try {
                        addRow(e, model);
                        jProgressBar1.setValue(jProgressBar1.getValue() + 1);
                    } catch (Exception ex) {
                        System.out.println("view.MantenimientoUsuarios.loadInfo() error id " + e.getId());
                    }
                });
                lbTbInfo.setText("Filas: " + tbUsuarios.getRowCount());
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarUs;
    private javax.swing.JButton btnBsucarUs;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnrefresh;
    private javax.swing.JCheckBox ckAdd;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
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
    private javax.swing.JLabel lbTbInfo;
    private javax.swing.JLabel lbUsuarioActual;
    private javax.swing.JMenuItem mnFilt;
    private javax.swing.JPopupMenu popUpTbUssuario;
    private javax.swing.JTable tbUsuarios;
    private javax.swing.JTextField txtBusqueda;
    // End of variables declaration//GEN-END:variables
}

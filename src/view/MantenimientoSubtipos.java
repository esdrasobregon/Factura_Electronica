/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import entitys.Departamento;
import entitys.Presupuesto;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Year;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import view.util.JTableCommonFunctions;

/**
 *
 * @author eobregon
 */
public class MantenimientoSubtipos extends javax.swing.JPanel {

    /**
     * Creates new form MantenimientoSubtipos
     */
    ArrayList<Departamento> listaDepartamentos;
    ArrayList<Presupuesto> listaPresupuesto;
    private boolean loadingInfo;
    ArrayList<Presupuesto> listPresFilt;

    public MantenimientoSubtipos(ArrayList<Departamento> listaDepartamentos, ArrayList<Presupuesto> listaPresupuesto) {
        initComponents();
        this.listaDepartamentos = listaDepartamentos;
        this.listaPresupuesto = listaPresupuesto;
        prepareGUI();
    }

    private void prepareGUI() {
        prepareCommonInfo();
        setView();
        setTbEventsListeners();
        filtrarTbMantSubt();
    }

    private void setTbEventsListeners() {
        this.jTable1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    agregarActualizar();
                }
            }
        });
    }

    private void agregarActualizar() {
        int row = jTable1.getSelectedRow();
        String concep = jTable1.getValueAt(row, 4).toString();

        if (concep == null || concep.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No puede dejar un campo vacío");
        } else {
            String cta = jTable1.getValueAt(row, 5).toString();
            Presupuesto p = Presupuesto.getPresupuestoGen(cta, listaPresupuesto);
            if (p == null) {
                String lastCta = jTable1.getValueAt(row - 1, 5).toString();
                Presupuesto lastPres = Presupuesto.getPresupuestoGen(lastCta, listaPresupuesto);
                Presupuesto newPresupuesto = getTbPresupuesto(row);
                agregarPresupuesto(lastPres, newPresupuesto);
            } else {
                actualizarPresupuesto(p, row);
            }
        }
    }

    private void agregarPresupuesto(Presupuesto lastPres, Presupuesto newPresupuesto) {
        newPresupuesto.setCIA(lastPres.getCIA());
        newPresupuesto.setCODDEPA(lastPres.getCODDEPA());
        newPresupuesto.setCODAREA(lastPres.getCODAREA());
        newPresupuesto.setPERIODO(lastPres.getPERIODO());
        newPresupuesto.setCODCONCEPTO(newPresupuesto.getCTAPRESUPUESTO().substring(9));
        //JOptionPane.showMessageDialog(null, "Concepto " + newPresupuesto.getCODCONCEPTO());
        data.CrudPresupuesto crp = new data.CrudPresupuesto();
        boolean res = crp.agregarPresupuesto(newPresupuesto);
        if (res) {
            JOptionPane.showMessageDialog(null, "Se ha agregado "
                    + newPresupuesto.getCTAPRESUPUESTO() + "-" + newPresupuesto.getCONCEPATOADETALLE());
            listaPresupuesto.add(newPresupuesto);
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el proceso");
        }
    }

    private void actualizarPresupuesto(Presupuesto p, int row) {

        //p.setCODCONCEPTO(p.getCTAPRESUPUESTO().substring(9));
        p.setCONCEPTOADETALLE(jTable1.getValueAt(row, 4).toString());
        //JOptionPane.showMessageDialog(null, "Concepto " + newPresupuesto.getCODCONCEPTO());
        data.CrudPresupuesto crp = new data.CrudPresupuesto();
        boolean res = crp.actualizarPresupuesto(p);
        if (res) {
            JOptionPane.showMessageDialog(null, "Se ha actualizado "
                    + p.getCTAPRESUPUESTO() + "-" + p.getCONCEPATOADETALLE());
            //listaPresupuesto.add(newPresupuesto);
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido realizar el proceso");
        }
    }

    private void prepareCommonInfo() {
        this.listaDepartamentos.forEach(e -> {
            this.cmbDepartamentos.addItem(e.getDescripcion());
            this.cmbDepCG.addItem(e.getDescripcion());
        });
        prepareCmbSubtipo();

    }

    private void setView() {
        DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        this.listaPresupuesto.forEach(e -> {
            addRowSubTipo(model, e);
        });
    }

    private void addRowSubTipo(DefaultTableModel model, Presupuesto e) {

        model.addRow(new Object[]{
            e.getCIA(),
            e.getPERIODO(),
            e.getDEPTADETALLE(),
            e.getAREADETALLE(),
            e.getCONCEPATOADETALLE(),
            e.getCTAPRESUPUESTO()

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
        jPanel9 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbCIA = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtPeriodo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbDepCG = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtAreadetalle = new javax.swing.JTextField();
        txtCGResult = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnGuardCtaGen = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbDepartamentos = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cmbCuentaPresupesto = new javax.swing.JComboBox<>();
        btnAgregarFila = new javax.swing.JButton();
        cmbCtaGen = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lbTbSubtiposInfo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        jDialog1.setTitle("Formulario Cuenta General");
        jDialog1.setMinimumSize(new java.awt.Dimension(650, 350));
        jDialog1.setModal(true);

        jPanel9.setPreferredSize(new java.awt.Dimension(622, 200));

        jPanel14.setPreferredSize(new java.awt.Dimension(600, 170));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("CIA");

        cmbCIA.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01-CILT", "02-TURINTEL" }));
        cmbCIA.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCIAItemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Periodo");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Departamento");

        cmbDepCG.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepCGItemStateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Detalle Cuenta");

        txtCGResult.setEditable(false);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Subtipo resultante");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCIA, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtAreadetalle, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cmbDepCG, 0, 478, Short.MAX_VALUE))
                    .addComponent(txtCGResult, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(120, 120, 120))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cmbCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDepCG, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAreadetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCGResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnGuardCtaGen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save-25.png"))); // NOI18N
        btnGuardCtaGen.setText("Guardar");
        btnGuardCtaGen.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnGuardCtaGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardCtaGenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardCtaGen, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGuardCtaGen, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );

        jDialog1.getContentPane().add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel10.setPreferredSize(new java.awt.Dimension(400, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Formulario Cuenta General");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addContainerGap(455, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog1.getContentPane().add(jPanel10, java.awt.BorderLayout.PAGE_START);

        jPanel11.setPreferredSize(new java.awt.Dimension(400, 10));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel11, java.awt.BorderLayout.PAGE_END);

        jPanel12.setPreferredSize(new java.awt.Dimension(10, 240));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel12, java.awt.BorderLayout.LINE_END);

        jPanel13.setPreferredSize(new java.awt.Dimension(10, 240));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel13, java.awt.BorderLayout.LINE_START);

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla Mantenimiento Subtipos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CIA", "Periodo", "Departamento", "Area", "Concepto", "Subtipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(70);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(70);
            jTable1.getColumnModel().getColumn(1).setMinWidth(70);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(2).setMinWidth(90);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(200);
            jTable1.getColumnModel().getColumn(5).setMinWidth(100);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        jPanel6.add(jScrollPane1);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel7.setPreferredSize(new java.awt.Dimension(360, 35));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel7.setLayout(flowLayout1);

        jLabel1.setText("Departamento");
        jPanel7.add(jLabel1);

        cmbDepartamentos.setPreferredSize(new java.awt.Dimension(150, 30));
        cmbDepartamentos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepartamentosItemStateChanged(evt);
            }
        });
        cmbDepartamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDepartamentosActionPerformed(evt);
            }
        });
        cmbDepartamentos.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbDepartamentosPropertyChange(evt);
            }
        });
        jPanel7.add(cmbDepartamentos);

        jLabel2.setText("Subtipos");
        jPanel7.add(jLabel2);

        cmbCuentaPresupesto.setPreferredSize(new java.awt.Dimension(300, 30));
        cmbCuentaPresupesto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCuentaPresupestoItemStateChanged(evt);
            }
        });
        jPanel7.add(cmbCuentaPresupesto);

        btnAgregarFila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_icon_25x25_2.png"))); // NOI18N
        btnAgregarFila.setToolTipText("Agregar un subtipo");
        btnAgregarFila.setPreferredSize(new java.awt.Dimension(31, 30));
        btnAgregarFila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarFilaActionPerformed(evt);
            }
        });
        jPanel7.add(btnAgregarFila);

        cmbCtaGen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_icon_25x25_2.png"))); // NOI18N
        cmbCtaGen.setText("Cuenta General");
        cmbCtaGen.setToolTipText("Agregar una Cuenta General");
        cmbCtaGen.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        cmbCtaGen.setPreferredSize(new java.awt.Dimension(160, 30));
        cmbCtaGen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCtaGenActionPerformed(evt);
            }
        });
        jPanel7.add(cmbCtaGen);

        jPanel1.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel8.setPreferredSize(new java.awt.Dimension(360, 30));

        lbTbSubtiposInfo.setText("Filas");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTbSubtiposInfo)
                .addContainerGap(923, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTbSubtiposInfo)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 5));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 992, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(400, 5));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 992, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(20, 240));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 612, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(20, 240));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 612, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbDepartamentosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepartamentosItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            prepareCmbSubtipo();
            this.lbTbSubtiposInfo.setText("Total de subtipos: " + this.listaPresupuesto.size()
                    + " | Filas: " + this.jTable1.getRowCount());
        }
    }//GEN-LAST:event_cmbDepartamentosItemStateChanged
    private void prepareCmbSubtipo() {
        loadingInfo = true;
        JTableCommonFunctions.limpiarTabla(jTable1);
        cmbCuentaPresupesto.removeAllItems();
        String selectedItem = cmbDepartamentos.getSelectedItem().toString();
        Departamento d = Departamento.getDepartamento(listaDepartamentos, selectedItem);
        if (d != null) {
            this.listPresFilt = getSubCuentaPresupuesto(d);

            listPresFilt.forEach(e -> {
                if (e.getCTAPRESUPUESTO().endsWith("00")) {
                    cmbCuentaPresupesto.addItem(e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE().toUpperCase());
                }
            });
        }
        this.lbTbSubtiposInfo.setText("Total de subtipos: " + this.listaPresupuesto.size()
                + " | Filas: " + this.jTable1.getRowCount());
        loadingInfo = false;

        filtrarTbMantSubt();
    }
    private void cmbCuentaPresupestoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCuentaPresupestoItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            filtrarTbMantSubt();
            this.lbTbSubtiposInfo.setText("Total de subtipos: " + this.listaPresupuesto.size()
                    + " Filas: " + this.jTable1.getRowCount());
        }
    }//GEN-LAST:event_cmbCuentaPresupestoItemStateChanged

    private void cmbDepartamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDepartamentosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDepartamentosActionPerformed

    private void btnAgregarFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarFilaActionPerformed
        // TODO add your handling code here:
        int size = this.jTable1.getRowCount();
        if (size > 0) {
            String lastConcepto = jTable1.getValueAt(size - 1, 4).toString();

            if (!lastConcepto.equals("")) {
                DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
                //copiar ultimo subtipo
                Presupuesto e = getTbPresupuesto(size - 1);
                e.setCONCEPTOADETALLE("");
                String lastsubtipo = jTable1.getValueAt(size - 1, 5).toString();
                String last = lastsubtipo.substring(9, 11);
                String first = lastsubtipo.substring(0, 9);
                int num = Integer.parseInt(last) + 1;
                String subtipo = first + num;
                if (num < 10) {
                    subtipo = first + "0" + num;
                }
                e.setCTAPRESUPUESTO(subtipo);
                addRowSubTipo(model, e);
            } else {
                JOptionPane.showMessageDialog(null, "No puede dejar un campo vacío");
            }
            this.lbTbSubtiposInfo.setText("Total de subtipos: " + this.listaPresupuesto.size()
                    + " | Filas: " + this.jTable1.getRowCount());
        }
    }//GEN-LAST:event_btnAgregarFilaActionPerformed

    private void cmbDepartamentosPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbDepartamentosPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDepartamentosPropertyChange

    private void cmbCtaGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCtaGenActionPerformed
        // TODO add your handling code here:
        String selectedDep = this.cmbDepartamentos.getSelectedItem().toString();
        this.cmbDepCG.setSelectedItem(selectedDep);
        this.txtPeriodo.setText(Year.now().getValue() + "");
        this.jDialog1.setLocationRelativeTo(this);
        this.jDialog1.setVisible(true);
    }//GEN-LAST:event_cmbCtaGenActionPerformed

    private void btnGuardCtaGenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardCtaGenActionPerformed
        // TODO add your handling code here:
        Presupuesto p = new Presupuesto();
        String subtipo = this.txtCGResult.getText();
        p.setCIA(subtipo.substring(0, 2));
        p.setCODDEPA(subtipo.substring(3, 5));
        p.setCODAREA(subtipo.substring(6, 8));
        p.setCODCONCEPTO("00");
        p.setDEPTADETALLE(cmbDepCG.getSelectedItem().toString());
        p.setAREADETALLE(txtAreadetalle.getText());
        p.setCONCEPTOADETALLE(txtAreadetalle.getText());
        p.setCTAPRESUPUESTO(subtipo);
        p.setPERIODO(this.txtPeriodo.getText());
        data.CrudPresupuesto crp = new data.CrudPresupuesto();
        boolean res = crp.agregarPresupuesto(p);
        if (res) {
            JOptionPane.showMessageDialog(null, "Se ha agregado una Cuenta General");
            this.listaPresupuesto.add(p);
            this.txtCGResult.setText("");
            this.txtAreadetalle.setText("");
            jDialog1.setVisible(false);

        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar una Cuenta General");
        }

        //this.txtCGResult.setText(subtipo + "     " + p.getCIA() + "-" + p.getCODDEPA() + "-" + p.getCODAREA() + "-" + p.getCODCONCEPTO());

    }//GEN-LAST:event_btnGuardCtaGenActionPerformed

    private void cmbDepCGItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepCGItemStateChanged
        // TODO add your handling code here:
        filtrarCtaGen();
    }//GEN-LAST:event_cmbDepCGItemStateChanged

    private void cmbCIAItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCIAItemStateChanged
        // TODO add your handling code here:
        filtrarCtaGen();
    }//GEN-LAST:event_cmbCIAItemStateChanged
    private void filtrarCtaGen() {
        try {
            String selectedItem = cmbDepCG.getSelectedItem().toString();
            Departamento d = Departamento.getDepartamento(listaDepartamentos, selectedItem);
            String ciaSelected = cmbCIA.getSelectedItem().toString().substring(0, 3);
            if (d != null) {

                ArrayList<Presupuesto> list = getSubCuentaPresupuesto(d);

                Presupuesto e = list.get(list.size() - 1);
                int cond = Integer.parseInt(e.getCODAREA()) + 1;
                String codarea = cond < 10 ? "0" + cond : cond + "";
                this.txtCGResult.setText(ciaSelected + d.getDEPARTAMENTO() + "-" + codarea + "-00");
            }
        } catch (Exception e) {
            System.out.println("view.MantenimientoSubtipos.cmbDepCGItemStateChanged() error " + e.getMessage());
        }
    }

    private Presupuesto getTbPresupuesto(int index) {
        Presupuesto e = new Presupuesto();
        e.setCIA(jTable1.getValueAt(index, 0).toString());
        e.setPERIODO(jTable1.getValueAt(index, 1).toString());
        e.setDEPTADETALLE(jTable1.getValueAt(index, 2).toString());
        e.setAREADETALLE(jTable1.getValueAt(index, 3).toString());
        e.setCONCEPTOADETALLE(jTable1.getValueAt(index, 4).toString());
        e.setCTAPRESUPUESTO(jTable1.getValueAt(index, 5).toString());
        return e;
    }

    private void filtrarTbMantSubt() {
        try {
            loadingInfo = true;
            String sub = cmbCuentaPresupesto.getSelectedItem().toString().substring(0, 9);
            this.listPresFilt = filtrarCtaPresPorSub(listaPresupuesto, sub);
            JTableCommonFunctions.limpiarTabla(jTable1);
            DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
            this.listPresFilt.forEach(e -> {
                addRowSubTipo(model, e);
            });
            loadingInfo = false;
        } catch (Exception e) {
            System.out.println("view.MantenimientoSubtipos.filtrarTbMantSubt() error "+e.getMessage());
        }
    }

    private ArrayList<Presupuesto> getSubCuentaPresupuesto(Departamento d) {
        ArrayList<Presupuesto> ctaP = new ArrayList<>();
        for (int i = 0; i < listaPresupuesto.size(); i++) {
            Presupuesto pres = listaPresupuesto.get(i);
            String iddepartamento = pres.getCODDEPA();// pres.getCONCEPATOADETALLE().substring(3, 5);
            if (iddepartamento.equalsIgnoreCase(d.getDEPARTAMENTO())) {
                ctaP.add(pres);
            }

        }
        return ctaP;
        //return getCmbCuentas(ctaP);
    }

    private ArrayList<Presupuesto> filtrarCtaPresPorSub(ArrayList<Presupuesto> lista, String subtipo) {
        ArrayList<Presupuesto> ctaP = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            Presupuesto pres = lista.get(i);
            String subt = pres.getCTAPRESUPUESTO();// pres.getCONCEPATOADETALLE().substring(3, 5);
            if (subt.contains(subtipo)) {
                ctaP.add(pres);
            }

        }
        return ctaP;
        //return getCmbCuentas(ctaP);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarFila;
    private javax.swing.JButton btnGuardCtaGen;
    private javax.swing.JComboBox<String> cmbCIA;
    private javax.swing.JButton cmbCtaGen;
    private javax.swing.JComboBox<String> cmbCuentaPresupesto;
    private javax.swing.JComboBox<String> cmbDepCG;
    private javax.swing.JComboBox<String> cmbDepartamentos;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbTbSubtiposInfo;
    private javax.swing.JTextField txtAreadetalle;
    private javax.swing.JTextField txtCGResult;
    private javax.swing.JTextField txtPeriodo;
    // End of variables declaration//GEN-END:variables
}

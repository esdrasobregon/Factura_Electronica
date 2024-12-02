/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**
 *
 * @author eobregon
 */
import data.DataUser;
import entitys.Departamento;
import entitys.Presupuesto;
import entitys.Receips;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import view.util.CustomMessages;

public class OpcionesSeleccion extends JDialog {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ArrayList<Departamento> departamentosPropios;
    ArrayList<Presupuesto> listaPresupuesto;
    private ArrayList<Receips> filteredReceipList;

    /**
     * Creates new form OpcionesSeleccion
     */
    public OpcionesSeleccion() {
        initComponents();
    }

    public OpcionesSeleccion(ArrayList<Receips> filteredReceipList, ArrayList<Departamento> departamentosPropios, ArrayList<Presupuesto> listaPresupuesto) {
        initComponents();
        setModal(true);
        this.filteredReceipList = filteredReceipList;
        this.departamentosPropios = departamentosPropios;
        this.listaPresupuesto = listaPresupuesto;
        this.setLocationRelativeTo(null);
        /*addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // Do nothing
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                System.out.println(".windowLostFocus()");
                dispose();
            }
        });*/
        this.lbError.setVisible(false);
        loadCmbDep();

        loadTb();
        this.lbTbInfo.setText("Filas a ser asignadas " + this.jTable2.getRowCount());

    }

    public OpcionesSeleccion(ArrayList<Receips> filteredReceipList, ArrayList<Departamento> departamentosPropios) {
        initComponents();
        setModal(true);
        this.filteredReceipList = filteredReceipList;
        this.departamentosPropios = departamentosPropios;
        this.listaPresupuesto = listaPresupuesto;
        this.setLocationRelativeTo(null);

        this.lbError.setVisible(false);
        loadCmbDep();

        loadTb();
        this.lbTbInfo.setText("Filas a ser asignadas " + this.jTable2.getRowCount());
        //loadCtas();
        cmbDeps.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    loadCtas();
                }
            }
        });

    }

    private void loadCtas() {

        this.cmbCtaPres.removeAllItems();
        String selected = cmbDeps.getSelectedItem().toString();
        Departamento d = Departamento.getDepartamento(departamentosPropios, selected);
        //load cuentas
        data.CrudPresupuesto crudp = new data.CrudPresupuesto();
        this.listaPresupuesto = crudp.obtenerPresupuestoPorDep(d.getDEPARTAMENTO());
        if (!this.listaPresupuesto.isEmpty()) {
            this.listaPresupuesto.forEach(e -> {

                if (e.getCODCONCEPTO().equals("00")) {
                    String cta = e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE();
                    this.cmbCtaPres.addItem(cta);
                } else {
                    String cta = "   " + e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE();
                    this.cmbCtaPres.addItem(cta);
                }
            });
        }
    }

    private void loadCmbDep() {
        this.cmbDeps.addItem("");
        this.departamentosPropios.forEach(e -> {

            this.cmbDeps.addItem(e.getDescripcion());
        });

        cmbDeps.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {
                try {
                    String desc = cmbDeps.getSelectedItem().toString();
                    Departamento d = Departamento.getDepartamento(departamentosPropios, desc);
                    //JOptionPane.showMessageDialog(null, "todos las facturas se asignaran a la misma cuenta de presupuesto");
                    ArrayList<Presupuesto> ctap = getSubCuentaPresupuesto(d);
                    loadCmbCtapre(ctap);
                } catch (Exception e) {
                    System.err.println(".itemStateChanged() error " + e.getMessage());

                }
            }
        }
        );
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

    private void loadCmbCtapre(ArrayList<Presupuesto> ctas) {
        cmbCtaPres.removeAllItems();
        cmbCtaPres.addItem("");
        for (Presupuesto d : ctas) {
            if (d.getCTAPRESUPUESTO().endsWith("00")) {
                cmbCtaPres.addItem(d.getCTAPRESUPUESTO() + "-" + d.getCONCEPATOADETALLE().toUpperCase());
            } else {
                cmbCtaPres.addItem("       " + d.getCTAPRESUPUESTO() + "-" + d.getCONCEPATOADETALLE());
            }

        }

    }

    private void loadTb() {
        DefaultTableModel model = (DefaultTableModel) this.jTable2.getModel();
        this.filteredReceipList.forEach(rec -> {
            String totalComprobante = logic.AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalComprobante());

            model.addRow(new Object[]{
                rec.getReceptor().getNombre(),
                rec.getEmisor().getNombre(),
                rec.getNumeroConsecutivo(),
                dateFormat.format(rec.getFechaEmision()),
                totalComprobante,
                rec.getClave()
            });
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

        center = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbDeps = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbCtaPres = new javax.swing.JComboBox<>();
        lbError = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        lbTbInfo = new javax.swing.JLabel();
        north = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        south = new javax.swing.JPanel();
        east = new javax.swing.JPanel();
        west = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Asignar Facturas Masivamente");
        setAlwaysOnTop(true);
        setPreferredSize(new java.awt.Dimension(800, 450));

        center.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(153, 153, 153)), "Formulario Asignacón", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 140));

        btnGuardar.setText("Guardar");
        btnGuardar.setToolTipText("Guarda los cambios para todas las filas");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel1.setText("Departamentos");
        jPanel4.add(jLabel1);

        jPanel4.add(cmbDeps);

        jLabel2.setText("Cuenta de Presupuesto");
        jPanel5.add(jLabel2);

        jPanel5.add(cmbCtaPres);

        lbError.setBackground(new java.awt.Color(204, 204, 204));
        lbError.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbError.setForeground(new java.awt.Color(255, 0, 0));
        lbError.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(81, 81, 81)
                        .addComponent(lbError))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 451, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(lbError))
                .addGap(12, 12, 12))
        );

        center.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(153, 153, 153)), "Asignación masiva", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout(5, 5));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Compañia", "Proveedor", "Factura #", "Fecha", "Monto", "clave"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(300);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTable2.getColumnModel().getColumn(5).setMinWidth(0);
            jTable2.getColumnModel().getColumn(5).setPreferredWidth(0);
            jTable2.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel3.setPreferredSize(new java.awt.Dimension(702, 40));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        jPanel3.setLayout(flowLayout1);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_icon_25x25.png"))); // NOI18N
        btnDelete.setToolTipText("Eliminar filas");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel3.add(btnDelete);

        lbTbInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        lbTbInfo.setToolTipText("Todas las filas serán asignadas a la misma cuenta");
        lbTbInfo.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel3.add(lbTbInfo);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        center.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(center, java.awt.BorderLayout.CENTER);

        north.setPreferredSize(new java.awt.Dimension(400, 10));
        north.setLayout(new java.awt.GridLayout(1, 0));
        north.add(jProgressBar1);

        getContentPane().add(north, java.awt.BorderLayout.NORTH);

        south.setPreferredSize(new java.awt.Dimension(400, 10));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 721, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        getContentPane().add(south, java.awt.BorderLayout.SOUTH);

        east.setPreferredSize(new java.awt.Dimension(10, 0));

        javax.swing.GroupLayout eastLayout = new javax.swing.GroupLayout(east);
        east.setLayout(eastLayout);
        eastLayout.setHorizontalGroup(
            eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        eastLayout.setVerticalGroup(
            eastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        getContentPane().add(east, java.awt.BorderLayout.LINE_END);

        west.setPreferredSize(new java.awt.Dimension(10, 0));

        javax.swing.GroupLayout westLayout = new javax.swing.GroupLayout(west);
        west.setLayout(westLayout);
        westLayout.setHorizontalGroup(
            westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        westLayout.setVerticalGroup(
            westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        getContentPane().add(west, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) this.jTable2.getModel();
        int[] rows = jTable2.getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            model.removeRow(rows[i]);
        }
        this.lbTbInfo.setText("Filas a ser asignadas " + this.jTable2.getRowCount());
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        int rows = this.jTable2.getRowCount();
        this.jProgressBar1.setMaximum(rows);
        this.jProgressBar1.setValue(0);
        try {
            String cmbCtaP = this.cmbCtaPres.getSelectedItem().toString().trim();
            String cta = cmbCtaP.trim().substring(0, 11);
            String ctaGeneral = cmbCtaP.trim().substring(0, 9) + "00";
            if (cmbCtaP.isEmpty() || ctaGeneral.isEmpty() || cta.endsWith("00")) {
                view.util.CustomMessages.showTemporalLabelMessage(lbError, 3000, "Información incompleta, o cuenta incorrecta, intente nuevamente");
            } else {
                Presupuesto prex = Presupuesto.getPresupuestoGen(ctaGeneral, this.listaPresupuesto);

                String dep = cmbDeps.getSelectedItem().toString();
                Departamento d = Departamento.getDepartamento(departamentosPropios, dep);
                for (int i = 0; i < rows; i++) {
                    String clave = jTable2.getValueAt(i, 5).toString();
                    Receips r = Receips.getReceipByClave(clave, filteredReceipList);
                    System.out.println("rec " + r.getNumeroConsecutivo() + " total " + r.getResumenFactura().getTotalComprobante());
                    save(d, r, i, ctaGeneral + "-" + prex.getCONCEPATOADETALLE(), cmbCtaP);
                }
                this.dispose();
            }
        } catch (Exception e) {
            System.err.println("view.OpcionesSeleccion.btnGuardarActionPerformed() error " + e.getMessage());
            view.util.CustomMessages.showTemporalLabelMessage(lbError, 3000, "Información incompleta, intente nuevamente");
        }
    }//GEN-LAST:event_btnGuardarActionPerformed
    private void save(Departamento d, Receips r, int i, String cuentaGeneral, String cuenta) {
        Receips oldR = getOldR(r);
        r.setIdDepartamento(Integer.parseInt(d.getDEPARTAMENTO()));
        r.setCuentaGeneral(cuentaGeneral);
        r.setCuentaPresupuesto(cuenta == null ? "" : cuenta);
        r.setAprobadoDirector(1);
        r.setRechazado(0);
        //String user = r.getAprobadoDirector() ==1? DataUser.username : "SA";
        r.setPropietario(DataUser.username);
        //r.setPropietario( System. getenv("USERNAME").toUpperCase());
        boolean res = data.CrudFacturaElectronica.addUpdateFacturaElectronica(r);
        if (res) {
            System.out.println("Factura fila " + i + " guardada");
            //setUpAfterSaveProgressBar("Registro guardado correctamente");
            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
        } else {
            rollbackReceip(oldR, r);
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error, intente nuevamente");
        }

    }

    private void setUpAfterSaveProgressBar(String message) {
        this.jProgressBar1.setVisible(true);
        //jProgressBar1.setMaximum(1);
        //this.jProgressBar1.setValue(1);
        this.jProgressBar1.setString(message);
        CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
    }

    /**
     * this method restore all the values that a user can update of a receipt,
     * so use it when an attempt of update fails
     *
     */
    private void rollbackReceip(Receips oldR, Receips r) {
        r.setCajaChica(oldR.esCajaChica());
        r.setIdDepartamento(oldR.getIdDepartamento());
        r.setAprobadoDirector(oldR.getAprobadoDirector());
        r.setRechazado(oldR.getRechazado());
        r.setCuentaGeneral(oldR.getCuentaGeneral());
        r.setCuentaPresupuesto(oldR.getCuentaPresupuesto());
        r.setExactus(oldR.getExactus());
        r.setPropietario(oldR.getPropietario());
    }

    private Receips getOldR(Receips r) {
        Receips oldR = new Receips();
        oldR.setCuentaGeneral(r.getCuentaGeneral());
        oldR.setCuentaPresupuesto(r.getCuentaPresupuesto());
        oldR.setExactus(r.getExactus());
        oldR.setIdDepartamento(r.getIdDepartamento());
        oldR.setCajaChica(r.esCajaChica());
        oldR.setPropietario(r.getPropietario());
        oldR.setAprobadoDirector(r.getAprobadoDirector());
        oldR.setRechazado(r.getRechazado());
        return oldR;
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
            java.util.logging.Logger.getLogger(OpcionesSeleccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OpcionesSeleccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OpcionesSeleccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OpcionesSeleccion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OpcionesSeleccion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JPanel center;
    private javax.swing.JComboBox<String> cmbCtaPres;
    private javax.swing.JComboBox<String> cmbDeps;
    private javax.swing.JPanel east;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lbError;
    private javax.swing.JLabel lbTbInfo;
    private javax.swing.JPanel north;
    private javax.swing.JPanel south;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import data.DataUser;
import entitys.Departamento;
import entitys.Presupuesto;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import services.VersionHandler;

/**
 *
 * @author eobregon
 */
public class MainApplication extends javax.swing.JFrame {

    /**
     * Creates new form MainApplication
     */
    private ArrayList<entitys.UsuariosPresupuesto> listaAccesos;
    ArrayList<Departamento> listaDepartamentos;
    ArrayList<Departamento> departamentosPropios;
    VersionHandler vh = new VersionHandler();
    ArrayList<Presupuesto> listaPresupuesto;
    MantenimientoPagos mp;

    public MainApplication() {
        initComponents();
    }

    public MainApplication(ArrayList<entitys.UsuariosPresupuesto> listaAccesos) {
        initComponents();
        setLocationRelativeTo(null);
        this.lbVersion.setText(" Versión: " + logic.AppStaticValues.appVersion + " Usuario: " + DataUser.username);
        this.listaAccesos = listaAccesos;
        prepareGUI();
    }

    private void setSubTipoUser() {

        boolean exactusAccess = this.listaAccesos.get(0).getExactus() == 1;
        boolean esContabilidad = this.listaAccesos.get(0).getUsuarioConta() == 1;
        boolean esAdmin = this.listaAccesos.get(0).getAdministracionUsuarios() == 1;
        entitys.UsuariosPresupuesto element = listaAccesos.get(0);
        if (element.getExactus() == 1) {
            exactusAccess = true;
            if (element.getExactus_CP() == 1) {
                MantenimientoCuentasExactus mantCP = new MantenimientoCuentasExactus(1, listaPresupuesto, listaDepartamentos);
                tbPresupuesto.addTab("Cuentas por Pagar", mantCP.getContentPane());
            }
            if (element.getExactus_CB() == 1) {
                Departamento din = new Departamento();
                din.setDEPARTAMENTO("99");
                din.setDescripcion("Ingresos");
                din.setJEFE("");
                listaDepartamentos.add(din);
                MantenimientoCuentasExactus mantCB = new MantenimientoCuentasExactus(2, listaPresupuesto, listaDepartamentos);
                tbPresupuesto.addTab("Control Bancario", mantCB.getContentPane());
            }
        }
        if (element.getExactus_TC() == 1) {
            MantenimientoTipoCambio mtc = new MantenimientoTipoCambio();
            tbPresupuesto.addTab("Mantenimiento Tipo Cambio", mtc);
        }

        if (element.getExactus_Subtipos() == 1) {
            MantenimientoSubtipos mtc = new MantenimientoSubtipos(listaDepartamentos, listaPresupuesto);
            tbPresupuesto.addTab("Subtipos Contabilidad", mtc);
        }
        if (!exactusAccess) {
            this.pnlMain.remove(pnlExactus);

        } else if (!this.listaAccesos.isEmpty()) {
            if (this.listaAccesos.get(0).getMantenimientoPagos() == 1) {
                this.mp = new MantenimientoPagos(listaPresupuesto);
                tabExactus.addTab("Mantenimiento de Pagos", this.mp);
            }
            if (this.listaAccesos.get(0).getHistoricoCP() == 1) {
                MantHistoricoCPContado rep = new MantHistoricoCPContado(this.listaDepartamentos, listaPresupuesto);
                tabExactus.addTab("Mantenimiento CP Contado", rep);
            }
            System.out.println("es un usuario de conta");
            if (this.listaAccesos.get(0).getReportePagos() == 1) {
                ReporteAbonoSugerido rep = new ReporteAbonoSugerido();
                tabExactus.addTab("Reporte Sugerido Pagos (Credito)", rep);
            }
           /* if (this.listaAccesos.get(0).getReportePagos() == 1) {
                ReporteAbonoSugeridoContado rep = new ReporteAbonoSugeridoContado();
                tabExactus.addTab("Reporte Sugerido Pagos (Contado)", rep);
            }*/
            if (this.listaAccesos.get(0).getReportePagos() == 1) {
                view.ReporteAbonoSugeridoContado2 rep = new ReporteAbonoSugeridoContado2();
                tabExactus.addTab("Reporte Sugerido Pagos (Contado)", rep);
            }

            if (listaAccesos.get(0).getAdminFactSub() == 1) {
                this.tbPresupuesto.add("Administracion de facturas asignadas", new AdministracionSubtiposFacturas());
            }
            if (listaAccesos.get(0).getMantenimientoCompromisos() == 1) {
                this.tabExactus.add("Compromisos", new MantenimientoCompromiso());
            }
            if (this.listaAccesos.get(0).getReportePagoAplicados() == 1) {
                ReportePagosRealizados rep = new ReportePagosRealizados();
                tabExactus.addTab("Reporte Pagos Realizados", rep);
            }
            if (this.listaAccesos.get(0).getHistoricoAbonos() == 1) {
                ReporteHistoricoAbonos rep = new ReporteHistoricoAbonos();
                tabExactus.addTab("Historico Abonos", rep);
            }

        }

        if (!esContabilidad) {
            this.pnlMain.remove(pnlDesglose);

        }
        if (esAdmin) {
            pnlAdminUs.add(new MantenimientoUsuarios(listaDepartamentos));

        } else {
            this.pnlMain.remove(pnlAdminUs);
        }

    }

    private void prepareGUI() {
        loadInfo();
        setSubTipoUser();
        setView();
    }

    private void loadInfo() {
        data.CrudPresupuesto crudp = new data.CrudPresupuesto();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        this.listaPresupuesto = crudp.obtenerPresupuestoPorPeriodo(year + "");
        getProductionCommonInfo();
        checkInternetConnectionScheduler();
        checkUpdatesScheduler();
    }

    /**
     * this method check if there is internet connection, so the program can
     * keep tabs on it, and therefore warns the user if there is not, with the
     * change of the connection icon on lbCheckConnection
     */
    private void checkInternetConnectionScheduler() {
        Timer databaseSyncTimer = new Timer();
        databaseSyncTimer.scheduleAtFixedRate(
                new TimerTask() {
            public void run() {
                boolean connection = services.InternetFunctions.getInternetConnection();
                lbCheckConnection.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                        connection ? "/images/browser.png"
                                : "/images/no-internet.png")));
                lbCheckConnection.setToolTipText(connection ? "Acceso a internet" : "Sin acceso a internet");
            }
        }, 0, 3000);
    }

    /**
     * this method check if there is a new app version, so the program can keep
     * tabs on it, and therefore warns the user if there is an update available
     */
    private void checkUpdatesScheduler() {
        Timer databaseSyncTimer = new Timer();
        databaseSyncTimer.scheduleAtFixedRate(
                new TimerTask() {
            public void run() {
                String newVersion = vh.getCurrentAppVersion();
                if (!newVersion.equalsIgnoreCase(logic.AppStaticValues.appVersion)) {

                    System.out.println("New version found...");
                    btnNotifications.setVisible(true);
                    btnNotifications.setText("1");
                    btnNotifications.setToolTipText("Nueva versión encontrada, click para mas opciones");

                } else {
                    btnNotifications.setVisible(false);
                }
            }
        }, 0, 3000);
    }

    private void setView() {
        this.pnlAsignacion.add("Cuentas de Factura", new MantenimientoCuentaFact(this.listaAccesos));
        this.pnlDesglose.add("Desglose Facturas", new MantenimientoAsientosConta());
        this.tbPresupuesto.add("Gastos Periodicos", new MantenimientoGastosFijosPeriodicos(this.departamentosPropios, listaAccesos.get(0).getAdministradorGestionGastosPer()));
        tabExactus.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabExactus.getSelectedComponent() == mp && mp != null) {
                    mp.moduleOnUse = true;
                    System.out.println(".stateChanged() mantenimiento pagos seleccionado");
                } else if (mp != null) {
                    mp.moduleOnUse = false;
                    System.out.println(".stateChanged() mantenimiento pagos no seleccionado");
                }
            }
        });
    }

    private void getProductionCommonInfo() {

        //this.listaAsientos = crAsientos.obtenerAsientos();
        data.CRUDUsuariosPresupuesto cr = new data.CRUDUsuariosPresupuesto();

        data.crudDepartamento crd = new data.crudDepartamento();
        this.listaDepartamentos = crd.getSqlDepartamentos();

        if (!listaDepartamentos.isEmpty()) {
            this.departamentosPropios = new ArrayList<>();
            listaAccesos.forEach(e -> {
                if (e.getACCESO().equalsIgnoreCase("S")) {
                    Departamento d = Departamento.getDepartamentoByCodDepa(listaDepartamentos, e.getCOD_DEPA());
                    //System.err.println("departamento " + e.getDETA_DEPA() + " acceso " + e.getACCESO() + " CODEPA " + e.getCOD_DEPA());
                    if (d != null) {

                        departamentosPropios.add(d);
                        System.out.println("departamamento propio " + d.getDescripcion());

                    }
                }
            });

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

        jPanel1 = new javax.swing.JPanel();
        pnlMain = new javax.swing.JTabbedPane();
        pnlAsignacion = new javax.swing.JPanel();
        pnlDesglose = new javax.swing.JPanel();
        pnlExactus = new javax.swing.JPanel();
        tabExactus = new javax.swing.JTabbedPane();
        pnlPresupuestoexactus = new javax.swing.JPanel();
        tbPresupuesto = new javax.swing.JTabbedPane();
        pnlAdminUs = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lbVersion = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btnManual = new javax.swing.JButton();
        btnNotifications = new javax.swing.JButton();
        lbCheckConnection = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mantenimiento Facturas Electronicas");
        setMinimumSize(new java.awt.Dimension(1246, 500));
        setPreferredSize(new java.awt.Dimension(1246, 800));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        pnlMain.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        pnlAsignacion.setLayout(new java.awt.GridLayout(1, 0));
        pnlMain.addTab("Asingnación de facturas", pnlAsignacion);

        pnlDesglose.setLayout(new java.awt.GridLayout(1, 0));
        pnlMain.addTab("Desglose de facturas", pnlDesglose);

        pnlExactus.setLayout(new java.awt.GridLayout(1, 0));
        pnlExactus.add(tabExactus);

        pnlMain.addTab("Pago a proveedor", pnlExactus);

        pnlPresupuestoexactus.setLayout(new java.awt.GridLayout(1, 0));
        pnlPresupuestoexactus.add(tbPresupuesto);

        pnlMain.addTab("Presupuesto (Exactus)", pnlPresupuestoexactus);

        pnlAdminUs.setLayout(new java.awt.GridLayout(1, 0));
        pnlMain.addTab("Administrar usuarios", pnlAdminUs);

        jPanel1.add(pnlMain);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(718, 10));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 886, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(718, 30));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbVersion.setText("Version");
        jPanel6.add(lbVersion);

        jPanel3.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        btnManual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/manual-30.png"))); // NOI18N
        btnManual.setToolTipText("Abrir manual del programa");
        btnManual.setPreferredSize(new java.awt.Dimension(36, 30));
        btnManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManualActionPerformed(evt);
            }
        });
        jPanel7.add(btnManual);

        btnNotifications.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/notifications-25.png"))); // NOI18N
        btnNotifications.setToolTipText("Se ha encontrado una nueva actualización");
        btnNotifications.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificationsActionPerformed(evt);
            }
        });
        jPanel7.add(btnNotifications);

        lbCheckConnection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/browser.png"))); // NOI18N
        jPanel7.add(lbCheckConnection);

        jPanel3.add(jPanel7);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 418));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 418));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel5, java.awt.BorderLayout.LINE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManualActionPerformed
        // TODO add your handling code here:
        openManual();
    }//GEN-LAST:event_btnManualActionPerformed

    private void btnNotificationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificationsActionPerformed
        // TODO add your handling code here:
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Se ha encontrado una nueva versión\n"
                + "Desea realizar la actualización inmediatamente?", "Sistema Facturacion", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.NO_OPTION) {
            return;
        }
        this.dispose();
        UpdateAppInterface up = new UpdateAppInterface();
        up.setVisible(true);
    }//GEN-LAST:event_btnNotificationsActionPerformed
    private void openManual() {
        try {

            String path = logic.AppStaticValues.productionAppPath + "\\" + logic.AppStaticValues.appManual;
            File pdfFile = new File(path);
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("PDF file not found.");
                JOptionPane.showMessageDialog(null, "No se ha encontrado el archivo...");
            }

        } catch (Exception e) {
            System.err.println("view.MantenimientoFacturaElectronica.openManual() error " + e.getMessage());
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
            java.util.logging.Logger.getLogger(MainApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainApplication.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainApplication().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnManual;
    private javax.swing.JButton btnNotifications;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lbCheckConnection;
    private javax.swing.JLabel lbVersion;
    private javax.swing.JPanel pnlAdminUs;
    private javax.swing.JPanel pnlAsignacion;
    private javax.swing.JPanel pnlDesglose;
    private javax.swing.JPanel pnlExactus;
    private javax.swing.JTabbedPane pnlMain;
    private javax.swing.JPanel pnlPresupuestoexactus;
    private javax.swing.JTabbedPane tabExactus;
    private javax.swing.JTabbedPane tbPresupuesto;
    // End of variables declaration//GEN-END:variables
}

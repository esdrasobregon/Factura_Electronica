/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import data.DataUser;
import entitys.AsientoFactura;
import entitys.CorreoExcluidoFE;
import entitys.Departamento;
import entitys.Impuesto;
import entitys.LineaDetalle;
import entitys.Presupuesto;
import entitys.Receips;
import entitys.Sociedad;
import entitys.Usuario;
import entitys.UsuariosPresupuesto;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import logic.AppStaticValues;
import org.apache.poi.ss.formula.ptg.AddPtg;
import services.FileHandler;
import services.SimpleExcelWriter;
import services.VersionHandler;
import view.util.CustomMessages;
import static view.util.JTableCommonFunctions.limpiarTabla;
import view.util.LookAndFeel;
import view.util.botonComun;

/**
 *
 * @author eobregon
 */
public class MantenimientoFacturaElectronica extends javax.swing.JFrame {

    /**
     * Creates new form ReceiptMaintenance
     */
    boolean loadingInfo = false;
    ArrayList<Receips> receipList;
    Usuario usuarioActual;
    ArrayList<Departamento> listaDepartamentos;
    ArrayList<Departamento> departamentosPropios;
    boolean infoCargada = false;
    data.CrudPresupuesto crudp;
    ArrayList<Presupuesto> listaPresupuesto;
    int registradas = 0;
    ArrayList<Sociedad> sociedades;
    data.CrudAsiento crAsientos;
    ArrayList<AsientoFactura> listaAsientos;
    ArrayList<CorreoExcluidoFE> listaCorreosOmitidos;
    Action openXMLCom;
    boolean filtrandoPorEmisor = true;
    private ArrayList<Receips> filteredReceipList;
    private ArrayList<entitys.UsuariosPresupuesto> listaAccesos;
    private boolean esContabilidad;
    entitys.TipoCambio tc;
    VersionHandler vh = new VersionHandler();
    boolean contaRemoved = false;
    boolean exactusRemoved = false;

    public MantenimientoFacturaElectronica(Receips receipt) {
        initComponents();
        this.receipList = new ArrayList<>();
        receipList.add(receipt);
        showReceipDetails(receipt);

    }

    public MantenimientoFacturaElectronica(ArrayList<entitys.UsuariosPresupuesto> listaAccesos) {
        initComponents();
        this.crudp = new data.CrudPresupuesto();
        this.usuarioActual = new Usuario();
        this.usuarioActual.setNombreUsuario(System.getProperty("user.name").toUpperCase());
        System.out.println("usuario " + this.usuarioActual.getNombreUsuario());
        setLocationRelativeTo(null);
        setUpProgressBar("Cargando...", 5);
        this.listaAccesos = listaAccesos;

        prepareGUI();

        setFocusable(true);
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not needed for this example
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
                    // Handle Ctrl+X combination
                    System.out.println("Ctrl+X pressed");
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
                    loadingInfo = true;
                    //receipList = FileHandler.readReceipNotaCreditoFiles(getDirectory());
                    receipList = FileHandler.readReceipFiles(getDirectory());
                    if (receipList != null) {
                        filtrarFacturasPorEstado();
                    }
                    loadingInfo = false;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not needed for this example
            }
        });

    }

    /**
     * this method takes the selected receipt, and then loads the xml file
     * associated to it, to call this method type Ctrl+Shift+u keyboard
     * combination
     */
    private void openXMLFile() {
        openXMLCom = new AbstractAction("Show Message") {
            @Override
            public void actionPerformed(ActionEvent e) {

                openXmlFromSelectedRow();
            }
        };

        openXMLCom.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));

        JComponent content = (JComponent) this.getContentPane();
        content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put((KeyStroke) openXMLCom.getValue(Action.ACCELERATOR_KEY), "showMessage");
        content.getActionMap().put("showMessage", openXMLCom);
    }

    private void setSubTipoUser() {

        esContabilidad = false;
        boolean exactusAccess = false;
        int count = 0;
        while (count < this.listaAccesos.size() && !esContabilidad) {
            if (this.listaAccesos.get(count).getUsuarioConta() == 1) {
                esContabilidad = true;
                System.out.println("es un usuario de conta");
            }
            count++;
        }
        count = 0;
        while (count < this.listaAccesos.size() && !exactusAccess) {
            UsuariosPresupuesto element = this.listaAccesos.get(count);
            if (element.getExactus() == 1) {
                exactusAccess = true;
                if (element.getExactus_CP() == 1) {
                    MantenimientoCuentasExactus mantCP = new MantenimientoCuentasExactus(1, listaPresupuesto, listaDepartamentos);
                    tabExactus.addTab("Cuentas por Pagar", mantCP.getContentPane());
                }
                if (element.getExactus_CB() == 1) {
                    Departamento din = new Departamento();
                    din.setDEPARTAMENTO("99");
                    din.setDescripcion("Ingresos");
                    din.setJEFE("");
                    listaDepartamentos.add(din);
                    MantenimientoCuentasExactus mantCB = new MantenimientoCuentasExactus(2, listaPresupuesto, listaDepartamentos);
                    tabExactus.addTab("Control Bancario", mantCB.getContentPane());
                }
                if (element.getExactus_TC() == 1) {
                    MantenimientoTipoCambio mtc = new MantenimientoTipoCambio();
                    tabExactus.addTab("Mantenimiento Tipo Cambio", mtc);
                }

                if (element.getExactus_Subtipos() == 1) {
                    MantenimientoSubtipos mtc = new MantenimientoSubtipos(listaDepartamentos, listaPresupuesto);
                    tabExactus.addTab("Subtipos Contabilidad", mtc);
                }

                /*MantenimientoSubtiposExactus mantCG = new MantenimientoSubtiposExactus(3, listaPresupuesto, listaDepartamentos);
                tabExactus.addTab("Contabilidad General", mantCG.getContentPane());*/
                System.out.println("es un usuario de conta");
            }
            count++;
        }
        if (!this.listaAccesos.isEmpty()) {
            if (this.listaAccesos.get(0).getMantenimientoPagos() == 1) {
                MantenimientoPagos mp = new MantenimientoPagos(listaPresupuesto);
                tabExactus.addTab("Mantenimiento de Pagos", mp);
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
            if (this.listaAccesos.get(0).getReportePagos() == 1) {
                ReporteAbonoSugeridoContado rep = new ReporteAbonoSugeridoContado();
                tabExactus.addTab("Reporte Sugerido Pagos (Contado)", rep);
            }
            if (listaAccesos.get(0).getAdminFactSub() == 1) {
                this.tabExactus.add("Administracion de facturas asignadas", new AdministracionSubtiposFacturas());
            }
        }

        if (!esContabilidad) {
            this.jTabbedPane1.remove(pnlIvaMantenance);
            contaRemoved = true;
        }

        if (!exactusAccess) {
            this.jTabbedPane1.remove(pnlExactus);
            exactusRemoved = true;
        }

        this.jTabbedPane1.add("Cuentas de Factura", new MantenimientoCuentaFact(this.listaAccesos));
        this.jTabbedPane1.add("Desglose Facturas", new MantenimientoAsientosConta());
        this.jTabbedPane1.add("Gastos Periodicos", new MantenimientoGastosFijosPeriodicos(this.departamentosPropios, listaAccesos.get(0).getAdministradorGestionGastosPer()));

    }

    private void openXmlFromSelectedRow() {
        int tab = jTabbedPane1.getSelectedIndex();
        int row = tab == 0
                ? tbMntFact.getSelectedRow()
                : tbReceips.getSelectedRow();
        if (row > -1) {
            try {
                String clave = tab == 0
                        ? tbMntFact.getValueAt(row, 16).toString()
                        : tbReceips.getValueAt(row, 13).toString();
                Receips r = Receips.getReceipByClave(clave, receipList);
                String path = logic.AppStaticValues.respaldoArchivosGuardados + "\\" + r.getNombreArchivo();
                File pdfFile = new File(path);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("PDF file not found.");
                    JOptionPane.showMessageDialog(null, "No se ha encontrado el archivo...");
                }
            } catch (Exception ex) {
                System.out.println(".actionPerformed() error " + ex.getMessage());
            }
        }
    }

    /**
     * this method takes the selected receipt, and then loads the xml file
     * associated to it, to call this method type Ctrl+Shift+x keyboard
     * combination
     */
    private void showReceipDetails(Receips receipt) {
        //setTbReceiptEvent();
        commonListeners();
        commonViewSettings();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.btnAddReceips.setVisible(false);
        this.btnCorreosOmitidos.setVisible(false);
        this.btnSettings.setVisible(false);
        this.pnlMenuTbReceip.setVisible(false);
        this.jProgressBar1.setVisible(false);
        this.jTabbedPane1.remove(0);
        this.setTitle("Detalles de la factura " + receipt.getNumeroConsecutivo());
        settTbReceipDetailsMode();
        //limpiarTabla(tbReceips);
        Receips rec = this.receipList.get(0);
        //jTabbedPane1.setSelectedIndex(1);
        this.pnlReceipHeadings.setVisible(false);
        view.util.JTableCommonFunctions.alignTbHeadersToRigth(tbReceips);
        view.util.JTableCommonFunctions.showColumn(this.tbReceips, 2, 300, 350);
        view.util.JTableCommonFunctions.showColumn(this.tbReceips, 3, 350, 350);
        view.util.JTableCommonFunctions.showColumn(this.tbReceips, 4, 100, 100);
        view.util.JTableCommonFunctions.showColumn(this.tbReceips, 5, 100, 100);
        view.util.JTableCommonFunctions.showColumn(this.tbReceips, 6, 100, 100);
        view.util.JTableCommonFunctions.showColumn(this.tbReceips, 7, 100, 100);
        view.util.JTableCommonFunctions.showColumn(this.tbReceips, 11, 100, 120);
        view.util.JTableCommonFunctions.hideColumn(tbReceips, 8);
        view.util.JTableCommonFunctions.hideColumn(tbReceips, 9);
        view.util.JTableCommonFunctions.hideColumn(tbReceips, 10);
        view.util.JTableCommonFunctions.hideColumn(tbReceips, 1);
        loadTbReceps(rec, true);

        ShowDetails(this.receipList);
    }

    private void settTbReceipDetailsMode() {
        tbReceips.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Fecha", "N Factura", "Proveedor", "Producto/Servicio", "% Impuesto", "Total Imp Det", "Valor Bruto", "Total otros", "Impuesto total", "Total Factura", "Bienes", "EXACTUS"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    }

    private void prepareGUI() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                jProgressBar1.setMinimum(0);
                jProgressBar1.setMaximum(7);
                jProgressBar1.setString("Cargando, por favor espere...");
                getProductionCommonInfo();
                jProgressBar1.setValue(1);
                commonListeners();
                receipList = new ArrayList<>();
                setTbReceiptEvent();
                jProgressBar1.setValue(2);
                openReceipDetails();
                jProgressBar1.setValue(4);
                commonViewSettings();
                addTbMantenimientoFacturasEvents();
                jProgressBar1.setValue(5);
                openXMLFile();
                checkInternetConnectionScheduler();
                checkUpdatesScheduler();
                jProgressBar1.setValue(6);
                CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                quemarSociedades();
                setSubTipoUser();
                jProgressBar1.setValue(7);

            }
        };
        Thread t = new Thread(r);
        t.start();
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

    private void getProductionCommonInfo() {
        int value = jProgressBar1.getValue();
        this.listaPresupuesto = crudp.obtenerPresupuestoPorPeriodo("2024");
        this.sociedades = new ArrayList<>();
        this.crAsientos = new data.CrudAsiento();
        //this.listaAsientos = crAsientos.obtenerAsientos();
        data.CRUDUsuariosPresupuesto cr = new data.CRUDUsuariosPresupuesto();

        data.crudDepartamento crd = new data.crudDepartamento();
        this.listaDepartamentos = crd.getSqlDepartamentos();

        if (!listaDepartamentos.isEmpty()) {
            infoCargada = true;
            this.departamentosPropios = new ArrayList<>();
            loadingInfo = true;
            this.cmbDepConj.addItem("");
            listaAccesos.forEach(e -> {
                if (e.getACCESO().equalsIgnoreCase("S")) {
                    Departamento d = Departamento.getDepartamentoByCodDepa(listaDepartamentos, e.getCOD_DEPA());
                    System.err.println("departamento " + e.getDETA_DEPA() + " acceso " + e.getACCESO() + " CODEPA " + e.getCOD_DEPA());
                    if (d != null) {

                        departamentosPropios.add(d);
                        this.cmbDepConj.addItem(d.getDescripcion());
                        System.out.println("departamamento propio " + d.getDescripcion());

                    }
                }
            });
            loadingInfo = false;
            tbMntFact.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(getCmbDepartamentos()));
            tbMntFact.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(getCmbCuentaPresupuesto()));

        }
        jProgressBar1.setValue(value + 2);
    }

    /**
     * this function returns a jcombobox containing the days of the week. this
     * jcombobox has a event itemStateChanged listenter, witch will mark the
     * selected day in green on the associated day on the tbPlanilla cell
     */
    public javax.swing.JComboBox getCmbDepartamentos() {
        javax.swing.JComboBox cmb = new javax.swing.JComboBox();

        if (infoCargada) {
            cmb.addItem("");
            for (Departamento d : listaDepartamentos) {
                cmb.addItem(d.getDescripcion());
            }
            cmb.setSelectedItem("");
        }

        cmb.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent arg0) {

                if (arg0.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
                    try {

                        System.out.println("getCmbDepartamentos.itemStateChanged()");
                        int row = tbMntFact.getSelectedRow();
                        String clave = tbMntFact.getValueAt(row, 16).toString();
                        Receips r = Receips.getReceipByClave(clave, receipList);
                        if ((facturaLibre(r) //&& r.getExactus().equals("")
                                )) {
                            getCuenataSeleccionada(cmb, r, row);
                        } else {
                            restaurarReceipRowTbMntFact(r, row);
                            JOptionPane.showMessageDialog(null, "Usted no puede asignar presupuestos a este departamento");

                        }

                    } catch (Exception e) {
                        System.out.println("getCmbDepartamentos.itemStateChanged() error " + e.getMessage());
                    }
                }

            }
        }
        );
        return cmb;

    }

    private void getCuenataSeleccionada(javax.swing.JComboBox cmb, Receips r, int rowSelected) {
        try {
            String selectedItem = cmb.getSelectedItem().toString();
            Departamento d = Departamento.getDepartamento(departamentosPropios, selectedItem);
            if (d != null) {
                ArrayList<Presupuesto> ctap = getSubCuentaPresupuesto(d);
                javax.swing.JComboBox cmbCtaP = getCmbCuentas(ctap);
                String message = "Escoja su cuenta " + d.getDescripcion();// ctap.get(0).getCTAPRESUPUESTO() + "-" + ctap.get(0).getCONCEPATOADETALLE();
                JOptionPane.showMessageDialog(null, cmbCtaP, message, JOptionPane.QUESTION_MESSAGE);
                String itemSelected = cmbCtaP.getSelectedItem().toString();

                if (itemSelected == null || itemSelected.isEmpty()) {
                    //restaurarReceipRowTbMntFact(r, rowSelected);
                    refreshTbMntFacAfterFailAsing();
                } else {
                    String selected = itemSelected.substring(0, 11);
                    if (selected.endsWith("00")) {

                        //restaurarReceipRowTbMntFact(r, rowSelected);
                        refreshTbMntFacAfterFailAsing();
                        JOptionPane.showMessageDialog(null, "La cuenta seleccionada no es correcta");
                    } else {
                        String ctaGeneral = cmbCtaP.getSelectedItem().toString().trim().substring(0, 9) + "00";
                        Presupuesto prex = Presupuesto.getPresupuestoGen(ctaGeneral, this.listaPresupuesto);
                        tbMntFact.setValueAt(ctaGeneral + "-" + prex.getCONCEPATOADETALLE(), rowSelected, 7);
                        tbMntFact.setValueAt(cmbCtaP.getSelectedItem().toString().trim(), rowSelected, 8);
                    }
                }
            } else {

                if (!selectedItem.equals("")) {
                    JOptionPane.showMessageDialog(null, "Usted no tiene acceso a este departamento");
                    refreshTbMntFacAfterFailAsing();
                } else {
                    restaurarReceipRowTbMntFact(r, rowSelected);
                }
            }
        } catch (Exception e) {
            restaurarReceipRowTbMntFact(r, rowSelected);
            System.out.println("view.ReceiptMaintenance.getCuenataSeleccionada() error " + e.getMessage());
        }
    }

    private void refreshTbMntFacAfterFailAsing() {
        loadingInfo = true;
        filtrandoPorEmisor = false;
        handleFilter();
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

    public javax.swing.JComboBox getCmbCuentas(ArrayList<Presupuesto> ctaP) {
        javax.swing.JComboBox cmb = new javax.swing.JComboBox();

        cmb.addItem("");
        for (Presupuesto d : ctaP) {
            if (d.getCTAPRESUPUESTO().endsWith("00")) {
                cmb.addItem(d.getCTAPRESUPUESTO() + "-" + d.getCONCEPATOADETALLE().toUpperCase());
            } else {
                cmb.addItem("       " + d.getCTAPRESUPUESTO() + "-" + d.getCONCEPATOADETALLE());
            }

        }
        cmb.setSelectedItem("");

        return cmb;

    }

    public javax.swing.JComboBox getCmbCuentaPresupuesto() {
        javax.swing.JComboBox cmb = new javax.swing.JComboBox();

        if (infoCargada) {
            cmb.addItem("");
            for (Presupuesto d : listaPresupuesto) {

                String item = d.getCONCEPATOADETALLE().trim() + "-" + d.getAREADETALLE().trim();
                //int id = Integer.parseInt(item.substring(0, 2));
                cmb.addItem(item);
            }
            cmb.setSelectedIndex(0);
        }

        cmb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {

            }
        });
        return cmb;

    }

    /**
     * this method sets the default tbReceipt item selected event
     */
    private void setTbReceiptEvent() {
        tbReceips.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                //System.out.println(tbReceips.getValueAt(tbReceips.getSelectedRow(), 0).toString());
                try {
                    if (!loadingInfo) {
                        System.out.println(".valueChanged()");
                        String NumeroConsecutivo = tbReceips.getValueAt(tbReceips.getSelectedRow(), 1).toString();
                        Receips r = Receips.getReceipsFromNumeroConsecutivo(receipList, NumeroConsecutivo);
                        boolean bien = (boolean) tbReceips.getValueAt(tbReceips.getSelectedRow(), 10);
                        if (r != null) {
                            r.setBienes(bien);
                        }
                        showReceiptDetails();
                    }
                } catch (Exception e) {
                    System.out.println(".valueChanged() error " + e.getMessage());
                }

            }
        });
    }

    /**
     * this method shows a message to the user about some receipt details, like
     * the receipt creation date and total lines
     */
    private void showReceiptDetails() {
        try {
            String NumeroConsecutivo = tbReceips.getValueAt(tbReceips.getSelectedRow(), 1).toString();
            Receips r = Receips.getReceipsFromNumeroConsecutivo(this.receipList, NumeroConsecutivo);

            if (r != null) {
                this.lbDetFactura.setText("Fecha -> " + r.getFechaEmision().toString()
                        + ", Detalles -> " + r.getListLineaDetalle().size() + " Mostrando -> "
                        + tbReceips.getRowCount() + " filas "
                        + "de " + this.receipList.size() + " documentos");//logic.CommonDateFunctions.getDateFromString(r.getFechaEmision()).toString() + ", Detalles -> " + r.getListLineaDetalle().size());
            }
        } catch (Exception e) {
        }
    }

    private void showTbReceipsDetails() {

        if (receipList != null) {
            this.lbDetFactura.setText(" Mostrando -> "
                    + tbReceips.getRowCount() + " filas "
                    + "de " + this.receipList.size() + " documentos");
        }
    }

    private void showTbReceipsDetails(ArrayList<Receips> lista) {

        if (lista != null) {
            this.lbTbMantResumen.setText(" Mostrando -> "
                    + tbMntFact.getRowCount() + " filas "
                    + "de " + this.receipList.size() + " documentos");
        }
    }

    /**
     * this method set the default look and feel of the form components
     */
    private void commonViewSettings() {

        chbConj.setVisible(false);
        this.toolbConjunto.setVisible(false);
        this.btnSave.setVisible(false);
        //this.jPanel11.setVisible(false);
        this.lbVersion.setText(" Versión: " + logic.AppStaticValues.appVersion + " Usuario: " + DataUser.username);
        AppStaticValues.rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tbMntFact.getColumnModel().getColumn(6).setCellRenderer(new view.util.CustomTableCellRenderer(false));
        tbMntFact.getColumnModel().getColumn(7).setCellRenderer(new view.util.CustomTableCellRenderer(false));
        tbMntFact.getColumnModel().getColumn(8).setCellRenderer(new view.util.CustomTableCellRenderer(false));
        tbMntFact.getColumnModel().getColumn(8).setCellRenderer(new view.util.CustomTableCellRenderer(false));
        tbMntFact.getColumnModel().getColumn(4).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(5).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(6).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(7).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(8).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(9).setCellRenderer(AppStaticValues.rightRenderer);
        this.btnSettings.setVisible(false);
        tbMntFact.getColumnModel().getColumn(4).setCellRenderer(new view.util.CustomTableCellRenderer(true));
        botonComun.mouseHoverListener(btnManual);
        botonComun.mouseHoverListener(btnNotifications);
        btnNotifications.setVisible(false);
        botonComun.mouseHoverListener(btnAddReceips);
        botonComun.mouseHoverListener(btnExcel);
        botonComun.mouseHoverListener(btnCorreosOmitidos);
        botonComun.mouseHoverListener(this.btnRefreshReceips);
        botonComun.mouseHoverListener(btnSave);
        botonComun.mouseHoverListener(btnHideRow);
        botonComun.mouseHoverListener(btnCalculator);
        botonComun.mouseHoverListener(btnRefresh);
        botonComun.mouseHoverListener(btnSettings);
        botonComun.mouseHoverListener(btnBuscarFactura);
        botonComun.mouseHoverListener(this.btnOpenPdf);
        //botonComun.mouseHoverListener(this.btnAsingGroup);
        this.btnAddReceips.setVisible(false);
        this.btnCorreosOmitidos.setVisible(false);
        txtTotalCompras.setCaretColor(txtTotalCompras.getForeground());
        txtTotatIVA.setCaretColor(txtTotatIVA.getForeground());
        txtTotalComprasPlusIV.setCaretColor(txtTotalComprasPlusIV.getForeground());
        txtTotalComprasPlusIVPlusOtros.setCaretColor(txtTotalComprasPlusIVPlusOtros.getForeground());
        txtComp1p.setCaretColor(txtComp1p.getForeground());
        txtComp4p.setCaretColor(txtComp4p.getForeground());
        txtComp8P.setCaretColor(txtComp8P.getForeground());
        txtComp13P.setCaretColor(txtComp13P.getForeground());
        txtIm1p.setCaretColor(txtIm1p.getForeground());
        txtIm4p.setCaretColor(txtIm4p.getForeground());
        txtIm8P.setCaretColor(txtIm8P.getForeground());
        txtIm13P.setCaretColor(txtIm13P.getForeground());
        this.jToolBar1.setFloatable(false);
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

        setTabPanelEventListener();

    }

    private void setTabPanelEventListener() {
        jTabbedPane1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = jTabbedPane1.getSelectedIndex();
                System.out.println("Tab changed to index: " + selectedIndex);
                try {
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            int option = jTabbedPane1.getSelectedIndex();
                            if (option < 2) {
                                view.util.CommonPanel cp = new view.util.CommonPanel();
                                cp.handleResizePanel(up, 633, 110);
                            } else {
                                view.util.CommonPanel cp = new view.util.CommonPanel();
                                cp.handleResizePanel(up, 600, 0);
                            }
                            revalidate();
                        }

                    };
                    Thread t = new Thread(r);
                    t.start();
                } catch (Exception ex) {
                    System.out.println("jTabbedPane1.addChangeListener error " + ex.getMessage());
                }
            }
        });
    }

    /**
     * this method sets the common listeners for the components in the form
     */
    private void commonListeners() {
        this.tbReceips.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tme) {
                try {
                    int row = tbReceips.getSelectedRow();
                    int column = tbReceips.getSelectedColumn();
                    System.out.println("tbreceip.tableChanged() row " + row + " column " + column);
                    if (!loadingInfo && row > -1 && column == 10) {

                        System.out.println("tbreceip.valueChanged()");
                        //guardarExactus(row, column);
                        String NumeroConsecutivo = tbReceips.getValueAt(row, 1).toString();
                        if (NumeroConsecutivo != null) {
                            Receips r = Receips.getReceipsFromNumeroConsecutivo(receipList, NumeroConsecutivo);
                            boolean bien = (boolean) tbReceips.getValueAt(row, 10);
                            if (r != null) {
                                r.setBienes(bien);
                                ShowDetails(receipList);
                            }
                        }

                    }
                } catch (Exception e) {
                    System.out.println("commonListeners error " + e.getMessage());
                }
            }
        });
        this.cmbTipoFactura.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                if (arg0.getStateChange() == ItemEvent.SELECTED) {
                    //Do Something
                    ShowDetails(filteredReceipList);
                }

            }
        });
        this.cmbMoneda.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                if (arg0.getStateChange() == ItemEvent.SELECTED) {
                    //Do Something
                    ShowDetails(filteredReceipList);
                }

            }
        });
        this.txtBuscarConsecutivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFactura();
            }
        });

    }

    private void guardarExactus(int row, int column) {
        System.out.println("view.ReceiptMaintenance.guardarExactus() row " + row + " column " + column);
        if (column == 11 && !loadingInfo && !this.receipList.isEmpty()) {
            String clave = tbReceips.getValueAt(row, 13).toString();
            String asiento = tbReceips.getValueAt(row, column).toString().replaceAll("\\s", "");
            String exactus = "";
            Receips r = Receips.getReceipByClave(clave, receipList);
            boolean flag = !asiento.equals(r.getExactus());
            if (r != null && flag) {
                exactus = r.getExactus();
                r.setExactus(asiento);
                boolean res = data.CrudFacturaElectronica.addUpdateFacturaElectronica(r);
                if (res) {
                    //this.registradas += r.getExactus().isEmpty() ? -1 : 1;
                    System.out.println("factura numero " + r.getNumeroConsecutivo() + " guardada");
                    mostrarResumenEstadoFacturas();
                } else {
                    r.setExactus(exactus);
                    JOptionPane.showMessageDialog(null, "No se han podido guardar los cambios, favor revice su conexión");
                    System.out.println("factura numero " + r.getNumeroConsecutivo() + " no se ha podido guardar");
                }
            } else {
                System.out.println("receipt null or exactus does not have change...");
            }

        } else {
            System.out.println("view.ReceiptMaintenance.guardarExactus() column " + column);
        }
    }

    /**
     * this method adds a double chick function for the tbReceipt so when a row
     * is double clicked it opens a new instance of this form on read only
     * configuration, whit the receipt selected details
     */
    private void openReceipDetails() {
        this.tbReceips.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if it's a double click event
                JTable table = (JTable) e.getSource();
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (e.getClickCount() == 2 && table.getSelectedRow() > -1) {
                    //JOptionPane.showMessageDialog(null, "fila seleccionada");
                    String clave = tbReceips.getValueAt(row, 13).toString();
                    Receips r = Receips.getReceipByClave(clave, receipList);
                    MantenimientoFacturaElectronica redt = new MantenimientoFacturaElectronica(r);
                    redt.setVisible(true);
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tbReceips.rowAtPoint(e.getPoint());
                    tbReceips.addRowSelectionInterval(row, row);
                    popUpTbReceips.show(tbReceips, e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * this method adds a double chick function for the tbReceipt so when a row
     * is double clicked it opens a new instance of this form on read only
     * configuration, whit the receipt selected details
     */
    private void addTbMantenimientoFacturasEvents() {
        tbMntFact.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                int row = tbMntFact.getSelectedRow();
                if (mouseEvent.getClickCount() == 2 && row > -1 && !loadingInfo) {
                    //JOptionPane.showMessageDialog(null, "fila seleccionada");
                    String clave = tbMntFact.getValueAt(row, 2).toString();

                    Receips r = Receips.getReceipByClave(clave, receipList);

                    MantenimientoFacturaElectronica redt = new MantenimientoFacturaElectronica(r);
                    redt.setVisible(true);
                }
                if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                    int column = tbMntFact.getSelectedColumn();
                    int x = mouseEvent.getX();
                    int y = mouseEvent.getY();

                    if (row >= 0 && column >= 0) {
                        // Perform actions for right-click on a cell
                        int rowat = tbMntFact.rowAtPoint(new Point(x, y));
                        tbMntFact.columnAtPoint(new Point(x, y));
                        tbMntFact.setRowSelectionInterval(rowat, rowat);
                        System.out.println("Right-clicked on Row: " + row + ", Column: " + column);
                        popUpTbReceips.show(tbMntFact, x, y);
                    }
                }
            }

        });
        tbMntFact.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("tbmantenimiento .valueChanged()");
                if (!loadingInfo) {
                    int row = tbMntFact.getSelectedRow();
                    setTxtAreaDetalles(row);
                }
            }
        });
        tbMntFact.getModel().addTableModelListener(
                new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                int row = tbMntFact.getSelectedRow();
                int column = tbMntFact.getSelectedColumn();
                //new
                boolean chkconj = chbConj.isSelected();
                if (!loadingInfo && row > -1 && !chkconj) {
                    String clave = tbMntFact.getValueAt(row, 16).toString();
                    Receips r = Receips.getReceipByClave(clave, receipList);
                    if (facturaLibre(r)) {
                        if (column > 8) {
                            //JOptionPane.showMessageDialog(null, "fila " + selectedRow + " columna " + selectedColumn + " ha cambiado");
                            saveRowTbMantenimientoFacturas(row, r);
                        }
                    } else {
                        Departamento d = Departamento.getDepartamentoPorStringId(listaDepartamentos, r.getIdDepartamento());
                        loadingInfo = true;
                        tbMntFact.setValueAt(d == null ? "" : d.getDescripcion(), row, 6);
                        tbMntFact.setValueAt(r.getCuentaGeneral(), row, 7);
                        tbMntFact.setValueAt(r.getCuentaPresupuesto(), row, 8);
                        tbMntFact.setValueAt(r.getAprobadoDirector() == 1 ? true : false, row, 9);
                        tbMntFact.setValueAt(r.getRechazado() == 1 ? true : false, row, 10);
                        tbMntFact.setValueAt(r.esCajaChica() == 1 ? true : false, row, 13);
                        loadingInfo = false;
                        JOptionPane.showMessageDialog(null, "Esta factura fue asignada por " + r.getPropietario());
                    }
                    //aprobarRechazar(row, column);
                }

            }
        });

    }

    private boolean facturaLibre(Receips r) {
        boolean res = false;
        if (r.getPropietario().equals("SA")
                || r.getPropietario().equalsIgnoreCase(DataUser.username)) {
            res = true;
        }
        return res;
    }

    private void setTxtAreaDetalles(int row) {
        if (!loadingInfo) {
            try {
                String clave = tbMntFact.getValueAt(row, 16).toString();
                Receips r = Receips.getReceipByClave(clave, receipList);
                jtxtfDetalles.setText("");
                ArrayList<LineaDetalle> lista = r.getListLineaDetalle();
                String lineas = "";
                for (LineaDetalle l : lista) {
                    lineas += "\n" + l.getDetalle()
                            + " |Total: "
                            + r.getResumenFactura().getCodigoMoneda().getCodigoMoneda()
                            + " " + l.getMontoTotalLinea();
                }
                jtxtfDetalles.setText(lineas.toUpperCase());
            } catch (IndexOutOfBoundsException e) {
                System.err.println("view.MantenimientoFacturaElectronica.setTxtAreaDetalles() error " + e.getMessage());
            }
        }
    }

    /**
     * this method loads the user selected directory xml files, so this info be
     * show to the user on the GUI
     */
    private void loadReceips() {
        //FileHandler fh = new FileHandler();
        if (!receipList.isEmpty()) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            this.loadingInfo = true;
            limpiarTabla(tbReceips);
            clearInfo();
            //this.receipList = new ArrayList<>();
            // this.lbFolderAbierto.setToolTipText(path);
            readReceipFiles();

            ShowDetails(filteredReceipList);
            this.loadingInfo = false;
            lbDetFactura.setText("Filas " + this.tbReceips.getRowCount());
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * this method loads the receips registers from database, then checks for
     * new asientos references, updates asientos and finally show the info to
     * the GUI. loadinginfo value must be set on true
     */
    private void handleLoadReceips() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                setUpProgressBar("Cargando facturas...", 100);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                java.util.Date inicio = new java.util.Date(jdtInicio.getDate().getTime());
                java.util.Date fin = new java.util.Date(jdtFin.getDate().getTime());

                data.CrudCorreoExcluidoFE ce = new data.CrudCorreoExcluidoFE();
                listaCorreosOmitidos = ce.obtenerCorreosExclidosPorFecha(inicio, fin);
                crAsientos = new data.CrudAsiento();
                listaAsientos = crAsientos.obtenerAsientos(inicio, fin);
                //listaAsientos = crAsientos.obtenerAsientos();
                //JOptionPane.showMessageDialog(null, "asientos "+listaAsientos.size());
                receipList = data.CrudFacturaElectronica.obtenerFacturasPorFecha(inicio, fin);
                jProgressBar1.setValue(50);//readReceipFiles();
                setUpProgressBar("Cargando facturas...", receipList.size());
                checkXMLAndAsientos();
                filteredReceipList = receipList;
                loadCmbprovedores(filteredReceipList);
                if (jTabbedPane1.getSelectedIndex() == 0) {

                    filtrarTbMantenimientoFacturas();
                } else {
                    filtrarFacturasPorEstado();
                    ShowDetails(filteredReceipList);
                }

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                mostrarResumenEstadoFacturas();
                addCellColorCode(tbMntFact, 15);
                loadingInfo = false;
                filtrandoPorEmisor = true;

                //renameFiles1();
                CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    /**
     * this method checks the xml files associated to the receip register, so it
     * can load the info details to it. then checks the asientos list to update
     * db asientos info
     */
    private void checkXMLAndAsientos() {

        ArrayList<Receips> lista = new ArrayList<>();
        for (Receips r : receipList) {
            Receips rec = services.FileHandler.getJsonStringFromFile(logic.AppStaticValues.respaldoArchivosGuardados + r.getNombreArchivo());

            r.setListLineaDetalle(rec.getListLineaDetalle());
            String asiento = AsientoFactura.getAsientoPorCosecutivo(r, listaAsientos, "FAC");
            if (!r.getExactus().equalsIgnoreCase(asiento)
                    && !asiento.equals("") && r.getExactus().equals("")) {

                lista.add(r);
                r.setExactus(asiento);

            }

            jProgressBar1.setValue(jProgressBar1.getValue() + 1);

        }
        guardarAsientos(lista);
    }

    private void guardarAsientos(ArrayList<Receips> lista) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (Receips r : lista) {

                    boolean res = data.CrudFacturaElectronica.UpdateFacturaElectronicaAsientos(r);
                    if (res) {
                        System.out.println("asiento guardado, factura numero " + r.getNumeroConsecutivo());
                    } else {
                        System.out.println("asiento no se ha guardado, factura numero " + r.getNumeroConsecutivo());
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void mostrarResumenEstadoFacturas() {
        this.registradas = 0;
        for (Receips r : receipList) {
            this.registradas += r.getExactus().isEmpty() ? 0 : 1;
        }
        lbTotalFacturas.setText("Total facturas: " + receipList.size());
        lbPendientes.setText("Pendientes: " + (receipList.size() - registradas));
        lbRegistradas.setText("Registradas: " + registradas);
        if (this.listaCorreosOmitidos.size() > 0) {
            this.btnCorreosOmitidos.setVisible(true);
            this.btnCorreosOmitidos.setText("" + this.listaCorreosOmitidos.size());
        } else {
            this.btnCorreosOmitidos.setVisible(false);
        }
    }

    /**
     * this method loads the receip info to the tbreceip so it can be accessible
     * to the user
     */
    private void loadTbReceps(Receips rec, boolean showEmptyDetails) {

        DefaultTableModel model = (DefaultTableModel) this.tbReceips.getModel();

        rec.getListLineaDetalle().forEach(e -> {
            String formattedDate = AppStaticValues.dateFormat.format(rec.getFechaEmision());
            Impuesto imp = e.getImpuesto();
            if ((imp.getTarifa() > 0 || showEmptyDetails)) {
                model.addRow(new Object[]{
                    formattedDate, //rec.getFechaEmision(),
                    rec.getNumeroConsecutivo(),
                    rec.getEmisor().getNombre(),
                    e.getDetalle(),//rec.getDetalleServicio().getDetalle(),
                    imp == null ? 0 : imp.getTarifa(),
                    AppStaticValues.numberFormater.format(imp == null ? 0 : imp.getMonto()),
                    AppStaticValues.numberFormater.format(e.getMontoTotalLinea() - (imp == null ? 0 : imp.getMonto())),//rec.getResumenFactura().getTotalVentaNeta(),
                    AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalOtrosCargos()),
                    AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalImpuesto()),
                    AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalComprobante()),
                    rec.isBienes(),
                    rec.getExactus(),
                    rec.getClave()
                });
            }
        });

    }

    /**
     * this method loops the receipts list and check all the registers, this
     * must have all the conditions selected by the user to be shown
     */
    private void checkLoadTbReceps(Receips rec, boolean showEmptyDetails) {

        DefaultTableModel model = (DefaultTableModel) this.tbReceips.getModel();

        rec.getListLineaDetalle().forEach(e -> {
            boolean existe = numeroConsecutivoExiste(rec.getNumeroConsecutivo());
            if (!existe || showEmptyDetails) {
                String codmo = rec.getResumenFactura().getCodigoMoneda().getCodigoMoneda() + " ";
                String formattedDate = AppStaticValues.dateFormat.format(rec.getFechaEmision());

                addRowTbReceips(model, formattedDate, rec, e, codmo);
            }
        });

    }

    private void addRowTbReceips(DefaultTableModel model, String formattedDate, Receips rec, LineaDetalle e, String codmo) {
        Impuesto imp = e.getImpuesto();
        model.addRow(new Object[]{
            formattedDate, //rec.getFechaEmision(),
            rec.getNumeroConsecutivo(),
            rec.getEmisor().getNombre().toUpperCase(),
            e.getDetalle(),//rec.getDetalleServicio().getDetalle(),
            imp == null ? 0 : imp.getTarifa(),
            codmo + AppStaticValues.numberFormater.format(imp == null ? 0 : imp.getMonto()),
            codmo + AppStaticValues.numberFormater.format(e.getMontoTotalLinea() - (imp == null ? 0 : imp.getMonto())),//rec.getResumenFactura().getTotalVentaNeta(),
            codmo + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalOtrosCargos()),
            codmo + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalImpuesto()),
            codmo + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalComprobante()),
            rec.isBienes(),
            rec.getExactus(),
            codmo + AppStaticValues.numberFormater.format(rec.getDifereniciaXmlExactus()),
            rec.getClave()
        });

    }

    private void loadTbRecepsPorCIA(Receips rec, Sociedad soc) {

        String cedRec = rec.getReceptor().getIdentificacion().substring(3, rec.getReceptor().getIdentificacion().length());
        if (soc.getCedula().equalsIgnoreCase(cedRec)) {
            DefaultTableModel model = (DefaultTableModel) this.tbReceips.getModel();
            String codmo = rec.getResumenFactura().getCodigoMoneda().getCodigoMoneda() + " ";
            String formattedDate = AppStaticValues.dateFormat.format(rec.getFechaEmision());
            rec.getListLineaDetalle().forEach(e -> {
                boolean existe = numeroConsecutivoExiste(rec.getNumeroConsecutivo());
                if (!existe) {

                    addRowTbReceips(model, formattedDate, rec, e, codmo);
                }

            });

        }

    }

    /**
     * this method loads the receipt object to the tbReceips this method does
     * not a filtering process
     */
    private void loadTbReceptsWithoutFilter(Receips rec) {

        DefaultTableModel model = (DefaultTableModel) this.tbReceips.getModel();

        rec.getListLineaDetalle().forEach(e -> {
            boolean existe = numeroConsecutivoExiste(rec.getNumeroConsecutivo());
            if (!existe) {
                String codmo = rec.getResumenFactura().getCodigoMoneda().getCodigoMoneda() + " ";
                String formattedDate = AppStaticValues.dateFormat.format(rec.getFechaEmision());
                Impuesto imp = e.getImpuesto();
                model.addRow(new Object[]{
                    formattedDate, //rec.getFechaEmision(),
                    rec.getNumeroConsecutivo(),
                    rec.getEmisor().getNombre().toUpperCase(),
                    e.getDetalle(),//rec.getDetalleServicio().getDetalle(),
                    imp == null ? 0 : imp.getTarifa(),
                    codmo + AppStaticValues.numberFormater.format(imp == null ? 0 : imp.getMonto()),
                    codmo + AppStaticValues.numberFormater.format(e.getMontoTotalLinea() - (imp == null ? 0 : imp.getMonto())),//rec.getResumenFactura().getTotalVentaNeta(),
                    codmo + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalOtrosCargos()),
                    codmo + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalImpuesto()),
                    codmo + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalComprobante()),
                    rec.isBienes(),
                    rec.getExactus(),
                    codmo + AppStaticValues.numberFormater.format(rec.getDifereniciaXmlExactus()),
                    rec.getClave()
                });
            }
        });

    }

    /**
     * this method loops receiplist to show on the tbMntFact, the registers that
     * fulfilled the user filters selected required
     */
    private void loadAsingTbMantenimientoFacturas(Receips rec) {

        String totalComprobante = AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalComprobante());
        DefaultTableModel model = (DefaultTableModel) this.tbMntFact.getModel();
        try {
            Departamento d = Departamento.getDepartamentoPorStringId(listaDepartamentos, rec.getIdDepartamento());
            String descripcionDep = "";
            if (d != null) {
                descripcionDep = d.getDescripcion();
            }
            addRowTbMantenimiento(model, rec, totalComprobante, descripcionDep);
        } catch (Exception e) {
            System.out.println("view.ReceiptMaintenance.loadAsingTbReceps() error " + e.getMessage());
        }
    }

    /**
     * this method deletes all tbreceips selected rows
     */
    private void deleteRows() {
        int row = this.tbReceips.getSelectedRow();

        while (row > -1) {
            try {
                this.tbReceips.getCellEditor().stopCellEditing();
            } catch (Exception e) {
            }
            DefaultTableModel model = (DefaultTableModel) this.tbReceips.getModel();
            model.removeRow(row);
            row = this.tbReceips.getSelectedRow();
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

        popUpTbReceips = new javax.swing.JPopupMenu();
        menuAbrirPDF = new javax.swing.JMenuItem();
        mnuUpdateExactus = new javax.swing.JMenuItem();
        mnCopiarNFac = new javax.swing.JMenuItem();
        mnPropietario = new javax.swing.JMenuItem();
        down = new javax.swing.JPanel();
        lbVersion = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        btnManual = new javax.swing.JButton();
        lbCheckConnection = new javax.swing.JLabel();
        btnNotifications = new javax.swing.JButton();
        up = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        pnlReceipHeadings = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jdtInicio = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jdtFin = new com.toedter.calendar.JDateChooser();
        btnRefreshReceips = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        lbTotalFacturas = new javax.swing.JLabel();
        lbPendientes = new javax.swing.JLabel();
        lbRegistradas = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnCorreosOmitidos = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtBuscarConsecutivo = new javax.swing.JTextField();
        btnBuscarFactura = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        cmbSociedades = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        cmbEstadoRegistro = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        left = new javax.swing.JPanel();
        rigth = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pnlAsignacionesFacturas = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane(this.tbMntFact,javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tbMntFact = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        cmbEstadoAsignacion = new javax.swing.JComboBox<>();
        btnAsingGroup = new javax.swing.JButton();
        chbConj = new javax.swing.JCheckBox();
        toolbConjunto = new javax.swing.JToolBar();
        jPanel21 = new javax.swing.JPanel();
        cmbDepConj = new javax.swing.JComboBox<>();
        cmbCuentaConj = new javax.swing.JComboBox<>();
        btnSaveConj = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtxtfDetalles = new javax.swing.JTextArea();
        jPanel19 = new javax.swing.JPanel();
        lbTbMantResumen = new javax.swing.JLabel();
        btnExpCont = new javax.swing.JButton();
        jPanel20 = new javax.swing.JPanel();
        blSinCuentaPres = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        pnlIvaMantenance = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbReceips = new javax.swing.JTable();
        pnlMenuTbReceip = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnHideRow = new javax.swing.JButton();
        btnAddReceips = new javax.swing.JButton();
        btnOpenPdf = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnExcel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lbDetFactura = new javax.swing.JLabel();
        pnlResumen = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pnlTotales = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTotalCompras = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTotatIVA = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTotalComprasPlusIV = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTotalComprasPlusIVPlusOtros = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtComp1p = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtComp4p = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtComp8P = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtComp13P = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtIm1p = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtIm4p = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtIm8P = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtIm13P = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        cmbTipoFactura = new javax.swing.JComboBox<>();
        cmbMoneda = new javax.swing.JComboBox<>();
        btnCalculator = new javax.swing.JButton();
        btnSettings = new javax.swing.JButton();
        pnlExactus = new javax.swing.JPanel();
        tabExactus = new javax.swing.JTabbedPane();

        menuAbrirPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf .png"))); // NOI18N
        menuAbrirPDF.setText("Abrir PDF");
        menuAbrirPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbrirPDFActionPerformed(evt);
            }
        });
        popUpTbReceips.add(menuAbrirPDF);

        mnuUpdateExactus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        mnuUpdateExactus.setText("Editar asiento");
        mnuUpdateExactus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuUpdateExactusActionPerformed(evt);
            }
        });
        popUpTbReceips.add(mnuUpdateExactus);

        mnCopiarNFac.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file.png"))); // NOI18N
        mnCopiarNFac.setText("Copiar número de factura");
        mnCopiarNFac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnCopiarNFacActionPerformed(evt);
            }
        });
        popUpTbReceips.add(mnCopiarNFac);

        mnPropietario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/owner.png"))); // NOI18N
        mnPropietario.setText("Asignada por");
        mnPropietario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPropietarioActionPerformed(evt);
            }
        });
        popUpTbReceips.add(mnPropietario);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mantenimiento Facturas Electronicas");
        setBackground(new java.awt.Color(255, 204, 51));
        setMinimumSize(new java.awt.Dimension(1246, 500));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        down.setBackground(new java.awt.Color(60, 63, 66));
        down.setMinimumSize(new java.awt.Dimension(600, 20));
        down.setPreferredSize(new java.awt.Dimension(600, 33));
        down.setLayout(new java.awt.GridLayout(1, 0));

        lbVersion.setText(" Version");
        lbVersion.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        down.add(lbVersion);

        java.awt.FlowLayout flowLayout5 = new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0);
        flowLayout5.setAlignOnBaseline(true);
        jPanel22.setLayout(flowLayout5);

        btnManual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/manual-30.png"))); // NOI18N
        btnManual.setToolTipText("Abrir manual");
        btnManual.setBorderPainted(false);
        btnManual.setContentAreaFilled(false);
        btnManual.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnManual.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManualActionPerformed(evt);
            }
        });
        jPanel22.add(btnManual);

        lbCheckConnection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/browser.png"))); // NOI18N
        lbCheckConnection.setToolTipText("Acceso a internet");
        jPanel22.add(lbCheckConnection);

        btnNotifications.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnNotifications.setForeground(new java.awt.Color(204, 0, 51));
        btnNotifications.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/notifications-25.png"))); // NOI18N
        btnNotifications.setToolTipText("No hay nuevas notificaciones");
        btnNotifications.setBorderPainted(false);
        btnNotifications.setContentAreaFilled(false);
        btnNotifications.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNotifications.setIconTextGap(0);
        btnNotifications.setPreferredSize(new java.awt.Dimension(70, 30));
        btnNotifications.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotificationsActionPerformed(evt);
            }
        });
        jPanel22.add(btnNotifications);

        down.add(jPanel22);

        getContentPane().add(down, java.awt.BorderLayout.SOUTH);

        up.setBackground(new java.awt.Color(60, 63, 66));
        up.setPreferredSize(new java.awt.Dimension(633, 110));
        up.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel15.setPreferredSize(new java.awt.Dimension(400, 30));
        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT);
        flowLayout2.setAlignOnBaseline(true);
        jPanel15.setLayout(flowLayout2);

        jProgressBar1.setBackground(new java.awt.Color(0, 102, 102));
        jProgressBar1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jProgressBar1.setOpaque(true);
        jProgressBar1.setPreferredSize(new java.awt.Dimension(350, 20));
        jProgressBar1.setStringPainted(true);
        jPanel15.add(jProgressBar1);

        up.add(jPanel15, java.awt.BorderLayout.PAGE_START);

        pnlReceipHeadings.setMinimumSize(new java.awt.Dimension(757, 30));
        pnlReceipHeadings.setPreferredSize(new java.awt.Dimension(1069, 30));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5);
        flowLayout1.setAlignOnBaseline(true);
        pnlReceipHeadings.setLayout(flowLayout1);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Fecha inicio");
        pnlReceipHeadings.add(jLabel15);

        jdtInicio.setDateFormatString("dd-MM-yyyy");
        jdtInicio.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jdtInicio.setPreferredSize(new java.awt.Dimension(120, 22));
        pnlReceipHeadings.add(jdtInicio);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Fecha final");
        pnlReceipHeadings.add(jLabel16);

        jdtFin.setDateFormatString("dd-MM-yyyy");
        jdtFin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jdtFin.setPreferredSize(new java.awt.Dimension(120, 22));
        jdtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdtFinPropertyChange(evt);
            }
        });
        pnlReceipHeadings.add(jdtFin);

        btnRefreshReceips.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-start-30.png"))); // NOI18N
        btnRefreshReceips.setToolTipText("Volver a cargar facturas");
        btnRefreshReceips.setBorderPainted(false);
        btnRefreshReceips.setContentAreaFilled(false);
        btnRefreshReceips.setPreferredSize(new java.awt.Dimension(36, 22));
        btnRefreshReceips.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnRefreshReceipsItemStateChanged(evt);
            }
        });
        btnRefreshReceips.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshReceipsActionPerformed(evt);
            }
        });
        pnlReceipHeadings.add(btnRefreshReceips);

        jPanel18.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 204, 204)));
        jPanel18.setPreferredSize(new java.awt.Dimension(600, 35));
        jPanel18.setLayout(new java.awt.GridLayout(1, 0));

        lbTotalFacturas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbTotalFacturas.setText("Total facturas");
        jPanel18.add(lbTotalFacturas);

        lbPendientes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbPendientes.setText("Pendientes");
        jPanel18.add(lbPendientes);

        lbRegistradas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbRegistradas.setText("Registradas");
        jPanel18.add(lbRegistradas);

        jPanel4.setLayout(new java.awt.BorderLayout());

        btnCorreosOmitidos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCorreosOmitidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/failed (3).png"))); // NOI18N
        btnCorreosOmitidos.setToolTipText("Correos incompatilbles");
        btnCorreosOmitidos.setBorderPainted(false);
        btnCorreosOmitidos.setContentAreaFilled(false);
        btnCorreosOmitidos.setMargin(new java.awt.Insets(2, 0, 0, 14));
        btnCorreosOmitidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorreosOmitidosActionPerformed(evt);
            }
        });
        jPanel4.add(btnCorreosOmitidos, java.awt.BorderLayout.WEST);

        jPanel18.add(jPanel4);

        pnlReceipHeadings.add(jPanel18);

        up.add(pnlReceipHeadings, java.awt.BorderLayout.CENTER);

        jPanel16.setPreferredSize(new java.awt.Dimension(1272, 35));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5);
        flowLayout3.setAlignOnBaseline(true);
        jPanel16.setLayout(flowLayout3);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Factura #");
        jPanel16.add(jLabel17);

        txtBuscarConsecutivo.setToolTipText("Buscar por número de factura");
        txtBuscarConsecutivo.setPreferredSize(new java.awt.Dimension(150, 22));
        jPanel16.add(txtBuscarConsecutivo);

        btnBuscarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBuscarFactura.setToolTipText("Buscar factura por número");
        btnBuscarFactura.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBuscarFactura.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBuscarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFacturaActionPerformed(evt);
            }
        });
        jPanel16.add(btnBuscarFactura);

        jLabel14.setText("CIA");
        jPanel16.add(jLabel14);

        cmbSociedades.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbSociedades.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar todas", "Cedulas desconocidas", "RYMSA 3101724817", "CILT 3101086411", "IRASA 3101119637", "KATRA 3101119531", "OPYLOG 3101466557", "TURINTEL 3101468003" }));
        cmbSociedades.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSociedadesItemStateChanged(evt);
            }
        });
        cmbSociedades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSociedadesActionPerformed(evt);
            }
        });
        jPanel16.add(cmbSociedades);

        jLabel18.setText("Proveedor");
        jPanel16.add(jLabel18);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorItemStateChanged(evt);
            }
        });
        jPanel16.add(cmbProveedor);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Estado en Exactus");
        jPanel16.add(jLabel20);

        cmbEstadoRegistro.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbEstadoRegistro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Pendientes", "Registradas" }));
        cmbEstadoRegistro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoRegistroItemStateChanged(evt);
            }
        });
        jPanel16.add(cmbEstadoRegistro);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnRefresh.setToolTipText("Volver a cargar tabla");
        btnRefresh.setBorderPainted(false);
        btnRefresh.setContentAreaFilled(false);
        btnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefresh.setPreferredSize(new java.awt.Dimension(36, 30));
        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel16.add(btnRefresh);

        up.add(jPanel16, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(up, java.awt.BorderLayout.NORTH);

        left.setBackground(new java.awt.Color(60, 63, 66));
        left.setPreferredSize(new java.awt.Dimension(5, 445));

        javax.swing.GroupLayout leftLayout = new javax.swing.GroupLayout(left);
        left.setLayout(leftLayout);
        leftLayout.setHorizontalGroup(
            leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        leftLayout.setVerticalGroup(
            leftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
        );

        getContentPane().add(left, java.awt.BorderLayout.WEST);

        rigth.setBackground(new java.awt.Color(60, 63, 66));
        rigth.setPreferredSize(new java.awt.Dimension(5, 445));

        javax.swing.GroupLayout rigthLayout = new javax.swing.GroupLayout(rigth);
        rigth.setLayout(rigthLayout);
        rigthLayout.setHorizontalGroup(
            rigthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        rigthLayout.setVerticalGroup(
            rigthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
        );

        getContentPane().add(rigth, java.awt.BorderLayout.EAST);

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        pnlAsignacionesFacturas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla asignaciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        pnlAsignacionesFacturas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        pnlAsignacionesFacturas.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tbMntFact.setAutoCreateRowSorter(true);
        tbMntFact.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "COMPAÑIA", "PROVEEDOR", "FACTURA #", "FECHA", "MONTO", "MONEDA", "DEPARTAMENTO", "CTA Madre", "CTA Detalle", "APROBADO", "RECHAZADO", "ESTADO", "EXACTUS", "Cja Ch", "subtipo", "", "clave"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, true, true, true, true, true, true, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbMntFact.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbMntFact.setFillsViewportHeight(true);
        tbMntFact.setShowHorizontalLines(true);
        tbMntFact.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tbMntFact);
        if (tbMntFact.getColumnModel().getColumnCount() > 0) {
            tbMntFact.getColumnModel().getColumn(0).setPreferredWidth(250);
            tbMntFact.getColumnModel().getColumn(1).setPreferredWidth(250);
            tbMntFact.getColumnModel().getColumn(2).setPreferredWidth(160);
            tbMntFact.getColumnModel().getColumn(3).setMinWidth(90);
            tbMntFact.getColumnModel().getColumn(3).setPreferredWidth(90);
            tbMntFact.getColumnModel().getColumn(3).setMaxWidth(100);
            tbMntFact.getColumnModel().getColumn(4).setMinWidth(80);
            tbMntFact.getColumnModel().getColumn(4).setPreferredWidth(90);
            tbMntFact.getColumnModel().getColumn(4).setMaxWidth(90);
            tbMntFact.getColumnModel().getColumn(5).setMinWidth(70);
            tbMntFact.getColumnModel().getColumn(5).setPreferredWidth(70);
            tbMntFact.getColumnModel().getColumn(5).setMaxWidth(80);
            tbMntFact.getColumnModel().getColumn(6).setPreferredWidth(130);
            tbMntFact.getColumnModel().getColumn(7).setPreferredWidth(100);
            tbMntFact.getColumnModel().getColumn(8).setMinWidth(70);
            tbMntFact.getColumnModel().getColumn(8).setPreferredWidth(100);
            tbMntFact.getColumnModel().getColumn(9).setMinWidth(70);
            tbMntFact.getColumnModel().getColumn(9).setPreferredWidth(90);
            tbMntFact.getColumnModel().getColumn(9).setMaxWidth(90);
            tbMntFact.getColumnModel().getColumn(10).setPreferredWidth(95);
            tbMntFact.getColumnModel().getColumn(11).setMinWidth(0);
            tbMntFact.getColumnModel().getColumn(11).setPreferredWidth(0);
            tbMntFact.getColumnModel().getColumn(11).setMaxWidth(0);
            tbMntFact.getColumnModel().getColumn(12).setMinWidth(0);
            tbMntFact.getColumnModel().getColumn(12).setPreferredWidth(0);
            tbMntFact.getColumnModel().getColumn(12).setMaxWidth(0);
            tbMntFact.getColumnModel().getColumn(13).setMinWidth(50);
            tbMntFact.getColumnModel().getColumn(13).setPreferredWidth(55);
            tbMntFact.getColumnModel().getColumn(14).setMinWidth(0);
            tbMntFact.getColumnModel().getColumn(14).setPreferredWidth(0);
            tbMntFact.getColumnModel().getColumn(14).setMaxWidth(0);
            tbMntFact.getColumnModel().getColumn(15).setMinWidth(10);
            tbMntFact.getColumnModel().getColumn(15).setPreferredWidth(10);
            tbMntFact.getColumnModel().getColumn(15).setMaxWidth(10);
            tbMntFact.getColumnModel().getColumn(16).setMinWidth(0);
            tbMntFact.getColumnModel().getColumn(16).setPreferredWidth(0);
            tbMntFact.getColumnModel().getColumn(16).setMaxWidth(0);
        }
        tbMntFact.getTableHeader().setPreferredSize(new java.awt.Dimension(jScrollPane2.getWidth(),30));
        tbMntFact.getTableHeader().setBackground(new java.awt.Color(102,102,102));
        tbMntFact.getTableHeader().setForeground(new java.awt.Color(255,255,255));
        //tbReceips.getTableHeader().setForeground(new java.awt.Color(201,201,201));
        tbMntFact.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14) {});
        tbMntFact.setBackground(new java.awt.Color(51, 51, 51));
        tbMntFact.setRowHeight(25);
        tbMntFact.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        tbMntFact.setForeground(new java.awt.Color(153, 153, 153));
        tbMntFact.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.util.JTableCommonFunctions.alignTbHeadersToRigth(tbMntFact);

        jScrollPane2.setPreferredSize(new Dimension(160, 200));

        jPanel9.add(jScrollPane2);

        pnlAsignacionesFacturas.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel11.setMinimumSize(new java.awt.Dimension(49, 0));
        jPanel11.setPreferredSize(new java.awt.Dimension(10, 40));
        java.awt.FlowLayout flowLayout4 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5);
        flowLayout4.setAlignOnBaseline(true);
        jPanel11.setLayout(flowLayout4);

        jToolBar2.setRollover(true);
        jToolBar2.setPreferredSize(new java.awt.Dimension(400, 35));

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save_round_icon_25x25.png"))); // NOI18N
        btnSave.setToolTipText("Guardar cambios");
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        btnSave.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                btnSavePropertyChange(evt);
            }
        });
        jToolBar2.add(btnSave);

        jLabel19.setText("Estado asignación   ");
        jToolBar2.add(jLabel19);

        cmbEstadoAsignacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Asignadas", "Sin asignar" }));
        cmbEstadoAsignacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoAsignacionItemStateChanged(evt);
            }
        });
        cmbEstadoAsignacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoAsignacionActionPerformed(evt);
            }
        });
        jToolBar2.add(cmbEstadoAsignacion);

        btnAsingGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        btnAsingGroup.setText("Asignar en conjunto");
        btnAsingGroup.setToolTipText("Asignar grupalmente (excepto los asignados)");
        btnAsingGroup.setFocusable(false);
        btnAsingGroup.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnAsingGroup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAsingGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsingGroupActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAsingGroup);

        jPanel11.add(jToolBar2);

        chbConj.setText("Asignar en conjunto");
        chbConj.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chbConjItemStateChanged(evt);
            }
        });
        jPanel11.add(chbConj);

        toolbConjunto.setRollover(true);
        toolbConjunto.setMargin(new java.awt.Insets(0, 5, 0, 5));
        toolbConjunto.setMinimumSize(new java.awt.Dimension(400, 41));
        toolbConjunto.setPreferredSize(new java.awt.Dimension(500, 34));

        jPanel21.setMinimumSize(new java.awt.Dimension(400, 37));
        jPanel21.setPreferredSize(new java.awt.Dimension(400, 30));
        jPanel21.setLayout(new java.awt.BorderLayout(5, 0));

        cmbDepConj.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepConjItemStateChanged(evt);
            }
        });
        jPanel21.add(cmbDepConj, java.awt.BorderLayout.WEST);

        jPanel21.add(cmbCuentaConj, java.awt.BorderLayout.CENTER);

        toolbConjunto.add(jPanel21);

        btnSaveConj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save-25.png"))); // NOI18N
        btnSaveConj.setToolTipText("");
        btnSaveConj.setFocusable(false);
        btnSaveConj.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSaveConj.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveConj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveConjActionPerformed(evt);
            }
        });
        toolbConjunto.add(btnSaveConj);

        jPanel11.add(toolbConjunto);

        pnlAsignacionesFacturas.add(jPanel11, java.awt.BorderLayout.PAGE_START);

        jPanel12.setPreferredSize(new java.awt.Dimension(10, 100));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jtxtfDetalles.setEditable(false);
        jtxtfDetalles.setColumns(20);
        jtxtfDetalles.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jtxtfDetalles.setRows(5);
        jtxtfDetalles.setText("\n");
        jScrollPane3.setViewportView(jtxtfDetalles);

        jPanel12.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel19.setMinimumSize(new java.awt.Dimension(300, 30));
        jPanel19.setPreferredSize(new java.awt.Dimension(300, 25));
        jPanel19.setLayout(new java.awt.BorderLayout());

        lbTbMantResumen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbTbMantResumen.setText("Total de filas -> 0");
        lbTbMantResumen.setMaximumSize(new java.awt.Dimension(600, 20));
        lbTbMantResumen.setPreferredSize(new java.awt.Dimension(600, 20));
        jPanel19.add(lbTbMantResumen, java.awt.BorderLayout.LINE_START);

        btnExpCont.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-move-grabber-20.png"))); // NOI18N
        btnExpCont.setToolTipText("Expandir <-> Contraer");
        btnExpCont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpContActionPerformed(evt);
            }
        });
        jPanel19.add(btnExpCont, java.awt.BorderLayout.LINE_END);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        blSinCuentaPres.setBackground(new java.awt.Color(255, 0, 0));
        blSinCuentaPres.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        blSinCuentaPres.setText(" Sin Cta Presupuesto ");
        blSinCuentaPres.setToolTipText("Asiento asignado sin cuenta de presupuesto");
        blSinCuentaPres.setOpaque(true);
        jPanel20.add(blSinCuentaPres);

        jPanel19.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel19, java.awt.BorderLayout.PAGE_START);

        pnlAsignacionesFacturas.add(jPanel12, java.awt.BorderLayout.PAGE_END);

        jPanel13.setPreferredSize(new java.awt.Dimension(5, 324));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );

        pnlAsignacionesFacturas.add(jPanel13, java.awt.BorderLayout.LINE_END);

        jPanel14.setPreferredSize(new java.awt.Dimension(5, 324));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );

        pnlAsignacionesFacturas.add(jPanel14, java.awt.BorderLayout.LINE_START);

        jTabbedPane1.addTab("Asignaciones de Facturas", pnlAsignacionesFacturas);

        pnlIvaMantenance.setBackground(new java.awt.Color(51, 51, 51));
        pnlIvaMantenance.setLayout(new java.awt.BorderLayout(5, 10));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 2, true), "Tabla Detalles Facturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(153, 153, 153))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout(5, 5));

        jScrollPane1.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tbReceips.setAutoCreateRowSorter(true);
        tbReceips.setBackground(new java.awt.Color(51, 51, 51));
        tbReceips.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tbReceips.setForeground(new java.awt.Color(153, 153, 153));
        tbReceips.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fecha", "N Factura", "Proveedor", "Producto/Servicio", "% Impuesto", "Total Imp Det", "Valor Bruto", "Total otros", "Impuesto total", "Total Factura", "Bienes", "EXACTUS", "Diferencia", "clave"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbReceips.setFillsViewportHeight(true);
        tbReceips.setGridColor(new java.awt.Color(51, 51, 51));
        tbReceips.setRowHeight(25);
        tbReceips.setSelectionBackground(new java.awt.Color(51, 0, 153));
        tbReceips.setShowGrid(false);
        tbReceips.setShowVerticalLines(true);
        tbReceips.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbReceips);
        //tbReceips.setForeground(new java.awt.Color(205,205,205));
        if (tbReceips.getColumnModel().getColumnCount() > 0) {
            tbReceips.getColumnModel().getColumn(0).setMinWidth(80);
            tbReceips.getColumnModel().getColumn(0).setPreferredWidth(90);
            tbReceips.getColumnModel().getColumn(0).setMaxWidth(90);
            tbReceips.getColumnModel().getColumn(1).setMinWidth(180);
            tbReceips.getColumnModel().getColumn(1).setPreferredWidth(180);
            tbReceips.getColumnModel().getColumn(1).setMaxWidth(200);
            tbReceips.getColumnModel().getColumn(2).setMinWidth(120);
            tbReceips.getColumnModel().getColumn(2).setPreferredWidth(170);
            tbReceips.getColumnModel().getColumn(3).setMinWidth(0);
            tbReceips.getColumnModel().getColumn(3).setPreferredWidth(0);
            tbReceips.getColumnModel().getColumn(3).setMaxWidth(0);
            tbReceips.getColumnModel().getColumn(4).setMinWidth(0);
            tbReceips.getColumnModel().getColumn(4).setPreferredWidth(0);
            tbReceips.getColumnModel().getColumn(4).setMaxWidth(0);
            tbReceips.getColumnModel().getColumn(5).setMinWidth(0);
            tbReceips.getColumnModel().getColumn(5).setPreferredWidth(0);
            tbReceips.getColumnModel().getColumn(5).setMaxWidth(0);
            tbReceips.getColumnModel().getColumn(6).setMinWidth(0);
            tbReceips.getColumnModel().getColumn(6).setPreferredWidth(0);
            tbReceips.getColumnModel().getColumn(6).setMaxWidth(0);
            tbReceips.getColumnModel().getColumn(7).setMinWidth(0);
            tbReceips.getColumnModel().getColumn(7).setPreferredWidth(0);
            tbReceips.getColumnModel().getColumn(7).setMaxWidth(0);
            tbReceips.getColumnModel().getColumn(8).setMinWidth(130);
            tbReceips.getColumnModel().getColumn(8).setPreferredWidth(130);
            tbReceips.getColumnModel().getColumn(8).setMaxWidth(150);
            tbReceips.getColumnModel().getColumn(9).setMinWidth(130);
            tbReceips.getColumnModel().getColumn(9).setPreferredWidth(130);
            tbReceips.getColumnModel().getColumn(9).setMaxWidth(130);
            tbReceips.getColumnModel().getColumn(10).setMinWidth(60);
            tbReceips.getColumnModel().getColumn(10).setPreferredWidth(60);
            tbReceips.getColumnModel().getColumn(10).setMaxWidth(60);
            tbReceips.getColumnModel().getColumn(11).setMinWidth(150);
            tbReceips.getColumnModel().getColumn(11).setPreferredWidth(180);
            tbReceips.getColumnModel().getColumn(11).setMaxWidth(180);
            tbReceips.getColumnModel().getColumn(12).setMinWidth(90);
            tbReceips.getColumnModel().getColumn(12).setPreferredWidth(90);
            tbReceips.getColumnModel().getColumn(12).setMaxWidth(90);
            tbReceips.getColumnModel().getColumn(13).setMinWidth(0);
            tbReceips.getColumnModel().getColumn(13).setPreferredWidth(0);
            tbReceips.getColumnModel().getColumn(13).setMaxWidth(0);
        }
        tbReceips.getTableHeader().setPreferredSize(new java.awt.Dimension(jScrollPane1.getWidth(),30));
        tbReceips.getTableHeader().setBackground(new java.awt.Color(102,102,102));
        tbReceips.getTableHeader().setForeground(new java.awt.Color(0,0,0));
        //tbReceips.getTableHeader().setForeground(new java.awt.Color(201,201,201));
        tbReceips.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14) {
        });
        //view.util.JTableCommonFunctions.alignTbHeadersToRigth(tbReceips);
        tbReceips.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnlMenuTbReceip.setBackground(new java.awt.Color(51, 51, 51));
        pnlMenuTbReceip.setPreferredSize(new java.awt.Dimension(540, 30));
        pnlMenuTbReceip.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(51, 51, 51));
        jPanel8.setPreferredSize(new java.awt.Dimension(300, 30));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        pnlMenuTbReceip.add(jPanel8, java.awt.BorderLayout.CENTER);

        jToolBar1.setBackground(new java.awt.Color(51, 51, 51));
        jToolBar1.setRollover(true);

        btnHideRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hide file.png"))); // NOI18N
        btnHideRow.setToolTipText("Ocultar fila seleccionada");
        btnHideRow.setBorderPainted(false);
        btnHideRow.setContentAreaFilled(false);
        btnHideRow.setFocusable(false);
        btnHideRow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnHideRow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnHideRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHideRowActionPerformed(evt);
            }
        });
        jToolBar1.add(btnHideRow);

        btnAddReceips.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/open-folder .png"))); // NOI18N
        btnAddReceips.setToolTipText("Seleccione carperta de facturas");
        btnAddReceips.setBorderPainted(false);
        btnAddReceips.setContentAreaFilled(false);
        btnAddReceips.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddReceips.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddReceips.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddReceipsActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddReceips);

        btnOpenPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf .png"))); // NOI18N
        btnOpenPdf.setToolTipText("Abrir PDF de la fila seleccionada");
        btnOpenPdf.setBorderPainted(false);
        btnOpenPdf.setContentAreaFilled(false);
        btnOpenPdf.setFocusable(false);
        btnOpenPdf.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenPdf.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenPdfActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenPdf);
        jToolBar1.add(jSeparator1);

        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExcel.setToolTipText("Exportar a excel");
        btnExcel.setBorderPainted(false);
        btnExcel.setContentAreaFilled(false);
        btnExcel.setFocusable(false);
        btnExcel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExcel);

        pnlMenuTbReceip.add(jToolBar1, java.awt.BorderLayout.WEST);

        jPanel1.add(pnlMenuTbReceip, java.awt.BorderLayout.PAGE_START);

        pnlIvaMantenance.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setPreferredSize(new java.awt.Dimension(514, 20));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        lbDetFactura.setBackground(new java.awt.Color(51, 51, 51));
        lbDetFactura.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbDetFactura.setForeground(new java.awt.Color(153, 153, 153));
        lbDetFactura.setOpaque(true);
        jPanel3.add(lbDetFactura);

        pnlIvaMantenance.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        pnlResumen.setBackground(new java.awt.Color(51, 51, 51));
        pnlResumen.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(102, 102, 102)), "Resumen de facturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(153, 153, 153))); // NOI18N
        pnlResumen.setForeground(new java.awt.Color(204, 204, 204));
        pnlResumen.setLayout(new java.awt.BorderLayout(5, 5));

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setForeground(new java.awt.Color(204, 204, 204));
        jPanel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 3, 20, 10));

        pnlTotales.setBackground(new java.awt.Color(51, 51, 51));
        pnlTotales.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(102, 102, 102)));
        pnlTotales.setLayout(new java.awt.GridLayout(4, 2, 5, 10));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Total compras");
        pnlTotales.add(jLabel1);

        txtTotalCompras.setEditable(false);
        txtTotalCompras.setBackground(new java.awt.Color(60, 63, 66));
        txtTotalCompras.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalCompras.setForeground(new java.awt.Color(204, 204, 204));
        txtTotalCompras.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalCompras.setText("0.0");
        txtTotalCompras.setOpaque(true);
        pnlTotales.add(txtTotalCompras);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText("Total IV");
        pnlTotales.add(jLabel2);

        txtTotatIVA.setEditable(false);
        txtTotatIVA.setBackground(new java.awt.Color(60, 63, 66));
        txtTotatIVA.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotatIVA.setForeground(new java.awt.Color(204, 204, 204));
        txtTotatIVA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotatIVA.setText("0.0");
        txtTotatIVA.setOpaque(true);
        pnlTotales.add(txtTotatIVA);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Total compras + IV");
        pnlTotales.add(jLabel3);

        txtTotalComprasPlusIV.setEditable(false);
        txtTotalComprasPlusIV.setBackground(new java.awt.Color(60, 63, 66));
        txtTotalComprasPlusIV.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalComprasPlusIV.setForeground(new java.awt.Color(204, 204, 204));
        txtTotalComprasPlusIV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalComprasPlusIV.setText("0.0");
        txtTotalComprasPlusIV.setOpaque(true);
        pnlTotales.add(txtTotalComprasPlusIV);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText("Total compras + IV + otros");
        pnlTotales.add(jLabel4);

        txtTotalComprasPlusIVPlusOtros.setEditable(false);
        txtTotalComprasPlusIVPlusOtros.setBackground(new java.awt.Color(60, 63, 66));
        txtTotalComprasPlusIVPlusOtros.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalComprasPlusIVPlusOtros.setForeground(new java.awt.Color(204, 204, 204));
        txtTotalComprasPlusIVPlusOtros.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalComprasPlusIVPlusOtros.setText("0.0");
        txtTotalComprasPlusIVPlusOtros.setOpaque(true);
        pnlTotales.add(txtTotalComprasPlusIVPlusOtros);

        jPanel2.add(pnlTotales);

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(102, 102, 102)));
        jPanel6.setLayout(new java.awt.GridLayout(4, 2, 5, 10));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(153, 153, 153));
        jLabel9.setText("Compras IV 1%");
        jPanel6.add(jLabel9);

        txtComp1p.setEditable(false);
        txtComp1p.setBackground(new java.awt.Color(60, 63, 66));
        txtComp1p.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtComp1p.setForeground(new java.awt.Color(204, 204, 204));
        txtComp1p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp1p.setText("0.0");
        jPanel6.add(txtComp1p);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(153, 153, 153));
        jLabel10.setText("Compras IV 4%");
        jPanel6.add(jLabel10);

        txtComp4p.setEditable(false);
        txtComp4p.setBackground(new java.awt.Color(60, 63, 66));
        txtComp4p.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtComp4p.setForeground(new java.awt.Color(204, 204, 204));
        txtComp4p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp4p.setText("0.0");
        jPanel6.add(txtComp4p);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 153, 153));
        jLabel11.setText("Compras IV 8%");
        jPanel6.add(jLabel11);

        txtComp8P.setEditable(false);
        txtComp8P.setBackground(new java.awt.Color(60, 63, 66));
        txtComp8P.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtComp8P.setForeground(new java.awt.Color(204, 204, 204));
        txtComp8P.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp8P.setText("0.0");
        jPanel6.add(txtComp8P);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(153, 153, 153));
        jLabel12.setText("Compras IV 13%");
        jPanel6.add(jLabel12);

        txtComp13P.setEditable(false);
        txtComp13P.setBackground(new java.awt.Color(60, 63, 66));
        txtComp13P.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtComp13P.setForeground(new java.awt.Color(204, 204, 204));
        txtComp13P.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp13P.setText("0.0");
        jPanel6.add(txtComp13P);

        jPanel2.add(jPanel6);

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(102, 102, 102)));
        jPanel5.setLayout(new java.awt.GridLayout(4, 2, 5, 10));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 153, 153));
        jLabel5.setText("Total impuesto 1%");
        jPanel5.add(jLabel5);

        txtIm1p.setEditable(false);
        txtIm1p.setBackground(new java.awt.Color(60, 63, 66));
        txtIm1p.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtIm1p.setForeground(new java.awt.Color(204, 204, 204));
        txtIm1p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm1p.setText("0.0");
        jPanel5.add(txtIm1p);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 153, 153));
        jLabel8.setText("Total impuesto 4%");
        jPanel5.add(jLabel8);

        txtIm4p.setEditable(false);
        txtIm4p.setBackground(new java.awt.Color(60, 63, 66));
        txtIm4p.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtIm4p.setForeground(new java.awt.Color(204, 204, 204));
        txtIm4p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm4p.setText("0.0");
        jPanel5.add(txtIm4p);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 153, 153));
        jLabel6.setText("Total impuesto 8%");
        jPanel5.add(jLabel6);

        txtIm8P.setEditable(false);
        txtIm8P.setBackground(new java.awt.Color(60, 63, 66));
        txtIm8P.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtIm8P.setForeground(new java.awt.Color(204, 204, 204));
        txtIm8P.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm8P.setText("0.0");
        jPanel5.add(txtIm8P);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 153, 153));
        jLabel7.setText("Total impuesto 13%");
        jPanel5.add(jLabel7);

        txtIm13P.setEditable(false);
        txtIm13P.setBackground(new java.awt.Color(60, 63, 66));
        txtIm13P.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtIm13P.setForeground(new java.awt.Color(204, 204, 204));
        txtIm13P.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm13P.setText("0.0");
        jPanel5.add(txtIm13P);

        jPanel2.add(jPanel5);

        jPanel17.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 134, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel17);

        pnlResumen.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel10.setBackground(new java.awt.Color(51, 51, 51));
        jPanel10.setPreferredSize(new java.awt.Dimension(1198, 40));
        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel7.setBackground(new java.awt.Color(51, 51, 51));
        jPanel7.setMaximumSize(new java.awt.Dimension(300, 32));
        jPanel7.setMinimumSize(new java.awt.Dimension(300, 22));
        jPanel7.setPreferredSize(new java.awt.Dimension(300, 20));
        jPanel7.setLayout(new java.awt.GridLayout(1, 0, 5, 5));

        jLabel13.setBackground(new java.awt.Color(51, 51, 51));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 153, 153));
        jLabel13.setText("Mostrar");
        jLabel13.setOpaque(true);
        jLabel13.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel7.add(jLabel13);

        cmbTipoFactura.setBackground(new java.awt.Color(102, 102, 102));
        cmbTipoFactura.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbTipoFactura.setForeground(new java.awt.Color(0, 0, 0));
        cmbTipoFactura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bienes", "Servicios" }));
        cmbTipoFactura.setBorder(null);
        cmbTipoFactura.setMaximumSize(new java.awt.Dimension(79, 22));
        cmbTipoFactura.setOpaque(true);
        jPanel7.add(cmbTipoFactura);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CRC", "USD" }));
        jPanel7.add(cmbMoneda);

        jPanel10.add(jPanel7);

        btnCalculator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/calculator_25x25.png"))); // NOI18N
        btnCalculator.setToolTipText("Abrir calculadora");
        btnCalculator.setBorderPainted(false);
        btnCalculator.setContentAreaFilled(false);
        btnCalculator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculatorActionPerformed(evt);
            }
        });
        jPanel10.add(btnCalculator);

        btnSettings.setBackground(new java.awt.Color(51, 51, 51));
        btnSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settings.png"))); // NOI18N
        btnSettings.setBorderPainted(false);
        btnSettings.setContentAreaFilled(false);
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
            }
        });
        jPanel10.add(btnSettings);

        pnlResumen.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        pnlIvaMantenance.add(pnlResumen, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Desglose Facturas", pnlIvaMantenance);

        pnlExactus.setLayout(new java.awt.GridLayout(1, 0));
        pnlExactus.add(tabExactus);

        jTabbedPane1.addTab("Exactus", pnlExactus);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddReceipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddReceipsActionPerformed
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(null, "your directory "+);
        this.receipList = FileHandler.readReceipFiles(getDirectory());
        //this.receipList = FileHandler.readReceipNotaCreditoFiles(getDirectory());
        if (this.receipList != null) {
            filtrarFacturasPorEstado();
        }


    }//GEN-LAST:event_btnAddReceipsActionPerformed

    private void btnHideRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHideRowActionPerformed
        // TODO add your handling code here:

        deleteRows();
    }//GEN-LAST:event_btnHideRowActionPerformed

    private void btnCalculatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculatorActionPerformed
        // TODO add your handling code here:
        try {
            Process p = Runtime.getRuntime().exec("calc.exe");
            p.waitFor();
            p.destroy();

        } catch (Exception e) {
            System.out.println("view.ReceiptMaintenance.btnCalculatorActionPerformed() error " + e.getMessage());;
        }
    }//GEN-LAST:event_btnCalculatorActionPerformed
    /**
     * this function ask the user to select a directory, then returns the string
     * directory selected path
     *
     * @return a string directory path
     */
    private String getDirectory() {
        String file = "";
        JFileChooser fchoose = new JFileChooser();
        int option = fchoose.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            file = fchoose.getSelectedFile().getParentFile().getPath();

        }
        return file;
    }
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        //filtrarFacturasPorEstado();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                refrescarFiltrosTbMnt();
            }
        };
        Thread t = new Thread(r);
        t.start();

    }//GEN-LAST:event_btnRefreshActionPerformed
    /**
     * this method refresh all the GUI and information filters, use it when
     * loading new information or the information is change
     */
    private void refrescarFiltrosTbMnt() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        loadingInfo = true;
        filtrandoPorEmisor = false;
        limpiarTabla(tbReceips);
        limpiarTabla(tbMntFact);
        cmbEstadoRegistro.setSelectedIndex(0);
        cmbSociedades.setSelectedIndex(0);
        cmbEstadoAsignacion.setSelectedIndex(0);
        cmbProveedor.removeAllItems();
        filteredReceipList = receipList;
        loadCmbprovedores(filteredReceipList);
        if (jTabbedPane1.getSelectedIndex() == 0) {
            showSelectedReceips();
            setUpProgressBar("Cargando facturas...", receipList.size());

        } else {
            filtrarFacturasPorEstado();//loadTbReceips();
            ShowDetails(filteredReceipList);
        }
        jProgressBar1.setString("Proceso completo...");
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);

        showManteniminetoFacturasInfo();
        addCellColorCode(tbMntFact, 15);
        filtrandoPorEmisor = true;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        loadingInfo = false;
    }
    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        // TODO add your handling code here:
        LookAndFeel l = new LookAndFeel();
        l.setIndex();
        boolean done = l.setTheme();
        if (done) {
            MantenimientoFacturaElectronica mant = new MantenimientoFacturaElectronica(this.listaAccesos);
            mant.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnSettingsActionPerformed

    private void jdtFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdtFinPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().contains("date")) {
            loadingInfo = true;
            filtrandoPorEmisor = false;
            resetContainers();
            this.registradas = 0;
            cargarFacturas();
        }
    }//GEN-LAST:event_jdtFinPropertyChange

    /**
     * this method checks the selected dates, if the dates are right handles the
     * loading and showing information to the GUI
     */
    private void cargarFacturas() {
        try {
            java.util.Date date = new java.util.Date(this.jdtInicio.getDate().getTime());
            if (date == null) {
                JOptionPane.showMessageDialog(null, "Error en las fechas seleccionadas");
            } else {
                java.util.Date endDate = new java.util.Date(this.jdtFin.getDate().getTime());
                if (date.before(endDate)) {

                    handleLoadReceips();
                    //loadingInfo = false;

                } else {
                    JOptionPane.showMessageDialog(null, "Error en las fechas seleccionadas");
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en las fechas seleccionadas");
            System.out.println("view.ReceiptMaintenance.jDateChooser2PropertyChange() error " + e.getMessage());
        }
    }

    private void showManteniminetoFacturasInfo() {
        this.lbTbMantResumen.setText("Total de filas -> " + tbMntFact.getRowCount()
                + " Total de facturas ->" + receipList.size());
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        Runnable r = new Runnable() {
            @Override
            public void run() {
                saveTbMantenimientoFacturas();
            }
        };
        Thread t = new Thread(r);
        t.start();


    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSavePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_btnSavePropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSavePropertyChange

    private void cmbSociedadesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSociedadesItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    loadingInfo = true;
                    filtrandoPorEmisor = false;
                    view.util.JTableCommonFunctions.limpiarTabla(tbReceips);
                    limpiarTabla(tbMntFact);
                    //cambioRecApr = true;
                    cmbEstadoAsignacion.setSelectedIndex(0);
                    filteredReceipList = getReceipsFilterBySoc();
                    loadCmbprovedores(filteredReceipList);
                    if (jTabbedPane1.getSelectedIndex() == 0) {
                        loadFilteredListTbMantenimiento(filteredReceipList);
                    } else {
                        loadFilteredListTbReceips(filteredReceipList);
                        ShowDetails(filteredReceipList);
                    }
                    filtrandoPorEmisor = true;
                    showManteniminetoFacturasInfo();
                    showTbReceipsDetails();
                    addCellColorCode(tbMntFact, 15);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    loadingInfo = false;
                }
            };
            Thread t = new Thread(r);
            t.start();
        }


    }//GEN-LAST:event_cmbSociedadesItemStateChanged

    private void btnBuscarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarFacturaActionPerformed
        // TODO add your handling code here:

        buscarFactura();

    }//GEN-LAST:event_btnBuscarFacturaActionPerformed

    private void cmbEstadoRegistroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoRegistroItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadingInfo = true;
            filtrandoPorEmisor = false;
            handleFilter();

        }
    }//GEN-LAST:event_cmbEstadoRegistroItemStateChanged
    private void filtrarFacturasPorEstado() {

        try {
            int selectedTab = this.jTabbedPane1.getSelectedIndex();
            for (int i = 0; i < receipList.size(); i++) {
                Receips r = receipList.get(i);
                int index = cmbEstadoRegistro.getSelectedIndex();
                switch (index) {
                    case 0:
                        if (selectedTab == 0) {
                            loadAsingTbMantenimientoFacturas(r);
                        } else {
                            checkLoadTbReceps(r, false);
                        }
                        break;
                    case 1:
                        if (selectedTab == 0 && r.getExactus().trim().isEmpty()) {
                            loadAsingTbMantenimientoFacturas(r);
                        } else if (r.getExactus().trim().isEmpty()) {
                            checkLoadTbReceps(r, false);
                        }
                        break;
                    case 2:
                        if (selectedTab == 0 && !r.getExactus().trim().isEmpty()) {
                            loadAsingTbMantenimientoFacturas(r);
                        } else if (!r.getExactus().trim().isEmpty()) {
                            checkLoadTbReceps(r, false);
                        }
                        break;
                }

            }
            showManteniminetoFacturasInfo();

        } catch (Exception e) {
            System.out.println("view.ReceiptMaintenance.filtrarFacturasPorEstado() error " + e.getMessage());
        }

    }
    private void btnRefreshReceipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshReceipsActionPerformed
        // TODO add your handling code here:
        java.util.Date inicio = this.jdtInicio.getDate();
        java.util.Date fin = this.jdtFin.getDate();
        if (inicio != null && fin != null) {
            loadingInfo = true;
            filtrandoPorEmisor = false;
            resetContainers();
            cargarFacturas();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor revice las fechas ingresadas...");
        }
    }//GEN-LAST:event_btnRefreshReceipsActionPerformed

    private void btnCorreosOmitidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorreosOmitidosActionPerformed
        // TODO add your handling code here:
        if (listaCorreosOmitidos != null && !listaCorreosOmitidos.isEmpty()) {
            String rango = "desde "
                    + AppStaticValues.dateFormat.format(this.jdtInicio.getDate())
                    + " hasta " + AppStaticValues.dateFormat.format(this.jdtFin.getDate());
            MantenimientoCorreosOmitidos mco = new MantenimientoCorreosOmitidos(rango, listaCorreosOmitidos);
            mco.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "La lista está vacía");
        }

    }//GEN-LAST:event_btnCorreosOmitidosActionPerformed

    private void menuAbrirPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbrirPDFActionPerformed
        // TODO add your handling code here:
        int index = jTabbedPane1.getSelectedIndex();
        openTbReceipPDF(index);
    }//GEN-LAST:event_menuAbrirPDFActionPerformed

    private void btnOpenPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenPdfActionPerformed
        // TODO add your handling code here:
        int index = jTabbedPane1.getSelectedIndex();
        openTbReceipPDF(index);
    }//GEN-LAST:event_btnOpenPdfActionPerformed

    private void mnuUpdateExactusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuUpdateExactusActionPerformed
        // TODO add your handling code here:
        int row = tbReceips.getSelectedRow();
        if (row > -1) {
            String clave = tbReceips.getValueAt(row, 13).toString();
            Receips r = Receips.getReceipByClave(clave, receipList);

            String input = JOptionPane.showInputDialog("Ingrese asiento para factura " + r.getNumeroConsecutivo());
            if (input != null && !input.isEmpty()) {
                logic.AppLogger.appLogger.info("Cabiando asiento para el documento " + r.getNumeroConsecutivo()
                        + " asiento viejo " + r.getExactus());
                r.setExactus(input);

                boolean res = data.CrudFacturaElectronica.UpdateFacturaElectronicaAsientos(r);
                if (res) {
                    logic.AppLogger.appLogger.info("Cabiando asiento para el documento "
                            + r.getNumeroConsecutivo() + " asiento nuevo " + r.getExactus());
                    tbReceips.setValueAt(input, row, 11);
                    JOptionPane.showMessageDialog(null, "Factura " + r.getNumeroConsecutivo() + " actualizada correctamente");
                    mostrarResumenEstadoFacturas();
                } else {
                    JOptionPane.showMessageDialog(null, "Factura " + r.getNumeroConsecutivo() + " no se ha actualizado...");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Nuevo asiento no es válido");
            }

        }

    }//GEN-LAST:event_mnuUpdateExactusActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        // TODO add your handling code here:
        //JOptionPane.showMessageDialog(null, "selected tab index "+jTabbedPane1.getSelectedIndex());
        if (jTabbedPane1.getSelectedIndex() == 0) {
            mnuUpdateExactus.setVisible(false);
        } else {
            mnuUpdateExactus.setVisible(true);
        }
        if (jTabbedPane1.getSelectedIndex() > 1 || (jTabbedPane1.getSelectedIndex() > 0 && contaRemoved)) {
            this.up.setVisible(false);
        } else {
            this.up.setVisible(true);
        }

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void mnCopiarNFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCopiarNFacActionPerformed
        // TODO add your handling code here:
        try {
            int tab = this.jTabbedPane1.getSelectedIndex();

            int row = tab == 0
                    ? this.tbMntFact.getSelectedRow()
                    : this.tbReceips.getSelectedRow();

            if (row > -1) {

                String consecutivo = tab == 0
                        ? this.tbMntFact.getValueAt(row, 2).toString()
                        : this.tbReceips.getValueAt(row, 1).toString();
                StringSelection selection = new StringSelection(consecutivo);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                System.out.println("consecutivo copiado: " + consecutivo);
            }
        } catch (Exception e) {
            System.out.println("view.ReceiptMaintenance.mnCopiarNFacActionPerformed() error " + e.getMessage());
        }
    }//GEN-LAST:event_mnCopiarNFacActionPerformed

    private void cmbSociedadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSociedadesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSociedadesActionPerformed

    private void cmbProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorItemStateChanged
        // TODO add your handling code here:

        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            if (filtrandoPorEmisor) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        setCursor(new Cursor(Cursor.WAIT_CURSOR));
                        System.out.println("view.MantenimientoFacturaElectronica.cmbProveedorItemStateChanged()");
                        loadingInfo = true;
                        limpiarTabla(tbReceips);
                        limpiarTabla(tbMntFact);
                        cmbEstadoAsignacion.setSelectedIndex(0);
                        filteredReceipList = getReceipsFilterBySoc();
                        //filtered by state
                        filteredReceipList = getReceipsFiteredByState(filteredReceipList, cmbEstadoRegistro.getSelectedIndex());
                        //loadCmbProveedores(l);
                        //filtered by provee
                        filteredReceipList = getReceipsFiteredByProv(filteredReceipList);
                        if (jTabbedPane1.getSelectedIndex() == 0) {
                            loadFilteredListTbMantenimiento(filteredReceipList);
                        } else {
                            loadFilteredListTbReceips(filteredReceipList);
                            ShowDetails(filteredReceipList);
                        }

                        filtrandoPorEmisor = true;
                        showTbReceipsDetails();
                        addCellColorCode(tbMntFact, 15);
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        loadingInfo = false;
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }

            //   handleFilter();
        }
    }//GEN-LAST:event_cmbProveedorItemStateChanged

    private void btnRefreshReceipsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnRefreshReceipsItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshReceipsItemStateChanged

    private void btnExpContActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpContActionPerformed
        // TODO add your handling code here:
        try {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (jPanel12.getSize().getHeight() == 100) {
                        view.util.CommonPanel cp = new view.util.CommonPanel();
                        cp.handleResizePanel(jPanel12, 600, 300);
                    } else {
                        view.util.CommonPanel cp = new view.util.CommonPanel();
                        cp.handleResizePanel(jPanel12, 600, 100);
                    }
                    revalidate();
                }

            };
            Thread t = new Thread(r);
            t.start();
        } catch (Exception e) {
            System.out.println("view.MantenimientoFacturaElectronica.btnExpContActionPerformed() error " + e.getMessage());
        }

    }//GEN-LAST:event_btnExpContActionPerformed

    private void mnPropietarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPropietarioActionPerformed
        // TODO add your handling code here:
        try {
            int tab = this.jTabbedPane1.getSelectedIndex();

            int row = tab == 0
                    ? this.tbMntFact.getSelectedRow()
                    : this.tbReceips.getSelectedRow();

            if (row > -1) {

                String clave = tab == 0
                        ? this.tbMntFact.getValueAt(row, 16).toString()
                        : this.tbReceips.getValueAt(row, 13).toString();
                Receips r = Receips.getReceipByClave(clave, receipList);
                if (r.getPropietario().equals("SA")) {
                    JOptionPane.showMessageDialog(null, "Esta factura no está asignada");

                } else {
                    JOptionPane.showMessageDialog(null, "Esta factura fue asiganda por " + r.getPropietario());
                }
            }
        } catch (Exception e) {
            System.out.println("view.MantenimientoFacturaElectronica.mnPropietarioActionPerformed() error " + e);
        }
    }//GEN-LAST:event_mnPropietarioActionPerformed

    private void btnAsingGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsingGroupActionPerformed
        // TODO add your handling code here:

        //JOptionPane.showMessageDialog(null, "Todas las facturas en la tabla se asignaran a la misma cuenta");
        try {
            ArrayList<Receips> lista = new ArrayList<>();
            filteredReceipList.forEach(e -> {
                if (e.getCuentaPresupuesto().equalsIgnoreCase("")) {
                    lista.add(e);
                }
            });
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay facturas que asignar");
            } else {
                OpcionesSeleccion op = new OpcionesSeleccion(lista, departamentosPropios, this.listaPresupuesto);
                op.setVisible(true);
                System.out.println("view.MantenimientoFacturaElectronica.btnAsingGroupActionPerformed()");
                refreshTbMntFacAfterFailAsing();
            }
        } catch (Exception e) {
            System.out.println("view.MantenimientoFacturaElectronica.btnAsingGroupActionPerformed() error " + e.getMessage());
        }


    }//GEN-LAST:event_btnAsingGroupActionPerformed

    private void btnManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManualActionPerformed
        // TODO add your handling code here:
        openManual();
    }//GEN-LAST:event_btnManualActionPerformed

    private void cmbEstadoAsignacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoAsignacionItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadingInfo = true;
            filtrandoPorEmisor = false;
            ArrayList<Receips> lista = getReceipsFiteredByEstadoAsignacion(filteredReceipList);
            limpiarTabla(tbMntFact);

            loadFilteredListTbMantenimiento(lista);
            showTbReceipsDetails(lista);
            addCellColorCode(tbMntFact, 15);
            loadingInfo = false;
            filtrandoPorEmisor = true;
            //handleFilter();

        }
    }//GEN-LAST:event_cmbEstadoAsignacionItemStateChanged

    private void cmbEstadoAsignacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadoAsignacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEstadoAsignacionActionPerformed

    private void chbConjItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chbConjItemStateChanged
        // TODO add your handling code here:
        Runnable r = new Runnable() {
            @Override
            public void run() {
                refrescarFiltrosTbMnt();
                boolean select = chbConj.isSelected();
                toolbConjunto.setVisible(select);
                if (select) {
                    tbMntFact.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Multi-Row Selection
                } else {
                    tbMntFact.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Single-Row Selection
                }
            }
        };
        Thread t = new Thread(r);
        t.start();

    }//GEN-LAST:event_chbConjItemStateChanged

    private void cmbDepConjItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepConjItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            cmbCuentaConj.removeAllItems();
            String selectedItem = cmbDepConj.getSelectedItem().toString();
            Departamento d = Departamento.getDepartamento(departamentosPropios, selectedItem);
            if (d != null) {
                ArrayList<Presupuesto> cta = getSubCuentaPresupuesto(d);

                cta.forEach(e -> {
                    if (e.getCTAPRESUPUESTO().endsWith("00")) {
                        cmbCuentaConj.addItem(e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE().toUpperCase());
                    } else {
                        cmbCuentaConj.addItem("     " + e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE().toUpperCase());
                    }
                });
            }
        }
    }//GEN-LAST:event_cmbDepConjItemStateChanged

    private void btnSaveConjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveConjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveConjActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        logic.AppLogger.appLogger.info("*************************************ending session*********************************");

    }//GEN-LAST:event_formWindowClosing

    private void btnNotificationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotificationsActionPerformed
        // TODO add your handling code here:
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Se ha encontrado una nueva versión\n"
                + "Desea realizar la actualización inmediatamente?", "Sistema Facturacion", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.NO_OPTION) {
            //JOptionPane.showMessageDialog(rootPane, "Operacion cancelada.");
            this.jProgressBar1.setString("Actualización cancelada");
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            return;
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FacturaElectronica.update();      }
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_btnNotificationsActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                guardarExcel();
                loadingInfo = false;
            }
        });
        t.start();
    }//GEN-LAST:event_btnExcelActionPerformed
    private void guardarExcel() {
        this.jProgressBar1.setString("Guardando excel...");
        this.jProgressBar1.setVisible(true);
        SimpleExcelWriter sew = new SimpleExcelWriter();
        //sew.writeToExcell(this.tbConciliacionMarcas);

        boolean saved = sew.writeJtableExcelFile(tbReceips, "Reporte");
        if (saved) {
            this.jProgressBar1.setString("Excel guardadado correctamente");
            CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        } else {
            this.jProgressBar1.setString("Proceso cancelado");
            //this.jpbLoading.setVisible(false);
            CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        }
    }

    private ArrayList<Receips> getReceipsFiteredByEstadoAsignacion(ArrayList<Receips> lista) {
        ArrayList<Receips> l = new ArrayList<>();
        try {

            int indexSelected = cmbEstadoAsignacion.getSelectedIndex();
            if (indexSelected == 0) {
                return lista;
            } else {
                for (Receips r : lista) {
                    String cdR = r.getCuentaPresupuesto();
                    if (cdR.equals("") && indexSelected == 2) {
                        l.add(r);
                    } else if (!cdR.equals("") && indexSelected == 1) {
                        l.add(r);
                    }
                }

            }
        } catch (Exception e) {
            System.err.println("view.MantenimientoFacturaElectronica.getReceipsFiteredByEstadoAsignacion() error " + e.getMessage());
        }
        return l;
    }

    /**
     * the loadingifo must be set to false, and filtrando to flase
     */
    private void handleFilter() {

        Runnable runna = new Runnable() {
            @Override
            public void run() {

                if (loadingInfo) {
                    limpiarTabla(tbReceips);
                    limpiarTabla(tbMntFact);
                    //cambioRecApr = true;
                    showFilteredReceipsTbMantenimiento();
                    showTbReceipsDetails();
                    addCellColorCode(tbMntFact, 15);
                    filtrandoPorEmisor = true;
                    loadingInfo = false;
                }
            }
        };
        Thread t = new Thread(runna);
        t.start();
    }

    private void openTbReceipPDF(int indexTabSelected) {
        try {
            int row = indexTabSelected == 0
                    ? tbMntFact.getSelectedRow()
                    : this.tbReceips.getSelectedRow();
            if (row > -1) {
                String clave = indexTabSelected == 0
                        ? tbMntFact.getValueAt(row, 16).toString()
                        : tbReceips.getValueAt(row, 13).toString();
                Receips r = Receips.getReceipByClave(clave, receipList);
                if (r != null) {
                    String path = logic.AppStaticValues.respaldoArchivosGuardados + "\\" + r.getPDFAsociated();
                    File pdfFile = new File(path);
                    if (pdfFile.exists()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        System.out.println("PDF file not found.");
                        JOptionPane.showMessageDialog(null, "No se ha encontrado el archivo...");
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "Por favor seleccione un registro de la tabla");
            }
        } catch (Exception e) {
            System.out.println("view.ReceiptMaintenance.openReceipPDF() error " + e.getMessage());
        }
    }

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

    private void buscarFactura() {
        loadingInfo = true;
        limpiarTabla(tbReceips);
        limpiarTabla(tbMntFact);
        Runnable runna = new Runnable() {
            @Override
            public void run() {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                String nIngresado = txtBuscarConsecutivo.getText();
                ArrayList<Receips> res = Receips.getSubReceipListFromListByNumeroConsecutivo(nIngresado, receipList);
                if (res.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No se ha encontrado...");
                } else {

                    res.forEach(r -> {

                        if (r != null) {
                            System.out.println("resultado de busqueda factura " + r.getNumeroConsecutivo());
                            /*r.getListLineaDetalle().forEach(e -> {
                                System.out.println("detalle monto total " + e.getImpuesto().getTarifa());

                            });*/
                            checkLoadTbReceps(r, false);
                            loadAsingTbMantenimientoFacturas(r);
                        }
                    });
                    ShowDetails(filteredReceipList);
                    lbDetFactura.setText("Filas -> " + tbReceips.getRowCount() + " Total facturas cargadas " + receipList.size());
                    loadingInfo = false;
                }
                addCellColorCode(tbMntFact, 15);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                showManteniminetoFacturasInfo();
            }
        };
        Thread t = new Thread(runna);
        t.start();
    }

    private void filtrarTbMantenimientoFacturas() {
        setUpProgressBar("Cargando facturas...", receipList.size());
        showFilteredReceipsTbMantenimiento();

        jProgressBar1.setString("Proceso completo...");
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        loadCmbprovedores(filteredReceipList);
        showManteniminetoFacturasInfo();

    }

    private ArrayList<Receips> getReceipsFiteredByState(ArrayList<Receips> lista, int stateIndex) {
        ArrayList<Receips> res = new ArrayList<>();
        if (stateIndex == 0) {
            return lista;
        } else {
            lista.forEach(e -> {
                if (e.getExactus().equals("") && stateIndex == 1) {
                    res.add(e);
                } else if (!e.getExactus().equals("") && stateIndex == 2) {
                    res.add(e);
                }
            });

        }

        return res;
    }

    public ArrayList<Receips> getReceipsWithNoCIA() {
        ArrayList<Receips> l = new ArrayList<>();
        ArrayList<String> socs = getSociedadesList();
        for (Receips r : this.receipList) {
            int startIndex = 2;
            boolean found = false;
            while (startIndex < socs.size() && !found) {
                String soc = socs.get(startIndex);
                int length = soc.length();
                //int cedLength = 10;
                int init = length - 10;
                String cedCia = soc.substring(init, length);
                String cedRec = r.getReceptor().getIdentificacion().substring(3, r.getReceptor().getIdentificacion().length());
                if (cedCia.equals(cedRec)) {
                    found = true;
                }
                startIndex++;
            }
            if (!found) {
                l.add(r);
            }
        }
        return l;
    }

    /**
     * this method sets the background color o a given jtable cell given the
     * value of the left cell -1 rgb(60, 63, 65), 0 red and >0 green
     */
    public void addCellColorCode(JTable table, int column) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        tableColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int v = (int) table.getValueAt(row, column - 1);
                if (v == 0) {
                    c.setBackground(Color.red);
                    c.setToolTipText("Se ha asiganado asiento sin cuenta de presupuesto");
                } else if (v > 0) {
                    //c.setBackground(Color.green);
                    c.setBackground(new Color(60, 63, 65));
                    c.setToolTipText("Cuenta de presupuesto correcta");
                } else {
                    c.setBackground(new Color(60, 63, 65));
                    c.setToolTipText("No se ha aignado asiento contable");
                }

                return c;
            }
        });
    }

    private ArrayList<Receips> getReceipsFilterBySoc() {
        ArrayList<Receips> l = new ArrayList<>();
        if (cmbSociedades.getSelectedIndex() < 2) {
            if (cmbSociedades.getSelectedIndex() == 0) {
                return this.receipList;
            } else {
                return getReceipsWithNoCIA();
            }
        } else {
            String desc = cmbSociedades.getSelectedItem().toString();
            Sociedad soc = Sociedad.obtenerSociedadPorDesc(desc, sociedades);
            this.receipList.forEach(e -> {
                String cedRec = e.getReceptor().getIdentificacion().substring(3,
                        e.getReceptor().getIdentificacion().length());
                if (soc.getCedula().equalsIgnoreCase(cedRec)) {
                    l.add(e);
                }
            });
        }
        return l;
    }

    private boolean showAllReceips() {
        boolean showAll = cmbSociedades.getSelectedIndex() == 0
                && cmbEstadoRegistro.getSelectedIndex() == 0
                && cmbProveedor.getSelectedIndex() == 0;
        return showAll;
    }

    private void showSelectedReceips() {
        setUpProgressBar("Por favor espere...", receipList.size());
        this.receipList.forEach(e -> {

            loadAsingTbMantenimientoFacturas(e);

            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
        });
        jProgressBar1.setString("Proceso completo");
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
    }

    private void loadFilteredListTbMantenimiento(ArrayList<Receips> l) {
        setUpProgressBar("Por favor espere...", l.size());
        l.forEach(e -> {

            loadAsingTbMantenimientoFacturas(e);

            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
        });
        jProgressBar1.setString("Proceso completo");
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
    }

    private void loadFilteredListTbReceips(ArrayList<Receips> l) {
        setUpProgressBar("Por favor espere...", l.size());
        l.forEach(e -> {

            checkLoadTbReceps(e, false);

            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
        });
        jProgressBar1.setString("Proceso completo");
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
    }

    private ArrayList<String> getSociedadesList() {

        ArrayList<String> l = new ArrayList<>();
        l.add("Mostrar todas");
        l.add("Cedulas desconocidas");
        l.add("RYMSA 3101724817");
        l.add("CILT 3101086411");
        l.add("IRASA 3101119637");
        l.add("KATRA 3101119531");
        l.add("OPILOG 3101466557");
        l.add("TURINTEL 3101468003");
        return l;
    }

    private void showFilteredReceipsTbMantenimiento() {
        boolean showAll = showAllReceips();
        //this.loadingInfo = false;
        //limpiarTabla(tbMantenimientoFacturas);

        if (showAll) {
            setUpProgressBar("Cargando facturas...", receipList.size());
            filteredReceipList = receipList;
            if (this.jTabbedPane1.getSelectedIndex() == 0) {
                loadFilteredListTbMantenimiento(filteredReceipList);
            } else {
                loadFilteredListTbReceips(filteredReceipList);
            }
            jProgressBar1.setString("Proceso completo...");
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        } else {

            //filtered by asocc
            this.filteredReceipList = getReceipsFilterBySoc();
            //filtered by state
            filteredReceipList = getReceipsFiteredByState(filteredReceipList, cmbEstadoRegistro.getSelectedIndex());
            //loadCmbProveedores(l);
            //filtered by provee
            filteredReceipList = getReceipsFiteredByProv(filteredReceipList);
            setUpProgressBar("Cargando facturas...", filteredReceipList.size());
            if (this.jTabbedPane1.getSelectedIndex() == 0) {
                loadFilteredListTbMantenimiento(filteredReceipList);
            } else {
                loadFilteredListTbReceips(filteredReceipList);
            }

            jProgressBar1.setString("Proceso completo...");
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        }

        showManteniminetoFacturasInfo();
    }

    private ArrayList<Receips> getReceipsFiteredByProv(ArrayList<Receips> lista) {
        ArrayList<Receips> l = new ArrayList<>();
        try {

            int indexSelected = cmbProveedor.getSelectedIndex();
            if (indexSelected == 0) {
                return lista;
            } else {
                String cdp = cmbProveedor.getItemAt(indexSelected).toString();
                for (Receips r : lista) {
                    String cdR = r.getEmisor().getIdentificacion();
                    if (cdp.contains(cdR)) {
                        l.add(r);
                    }
                }

            }
        } catch (Exception e) {
        }
        return l;
    }

    private void resetContainers() {
        limpiarTabla(tbReceips);
        limpiarTabla(tbMntFact);
        this.cmbEstadoRegistro.setSelectedIndex(0);
        this.cmbSociedades.setSelectedIndex(0);
        cmbProveedor.removeAllItems();
        cmbProveedor.addItem("Todos");
    }

    private void loadCmbprovedores(ArrayList<Receips> l) {

        cmbProveedor.removeAllItems();
        cmbProveedor.addItem("Todos");
        ArrayList<String> lista = new ArrayList<>();
        for (Receips r : l) {
            int emisorCedLength = r.getEmisor().getNombre().length();
            String emi = emisorCedLength < 40
                    ? r.getEmisor().getNombre()
                    : r.getEmisor().getNombre().substring(0, 30);
            String proveedor = emi + "->" + r.getEmisor().getIdentificacion();

            if (!lista.contains(proveedor.toUpperCase())) {
                //cmbProveedor.addItem(proveedor);
                lista.add(proveedor.toUpperCase());

            }

        }
        lista.sort(Comparator.naturalOrder());
        lista.forEach(e -> {
            cmbProveedor.addItem(e);
        });
    }

    private void quemarSociedades() {
        sociedades = new ArrayList<>();
        Sociedad soc = new Sociedad();
        soc.setActivo(1);
        soc.setNombre("CILT");
        soc.setCedula("3101086411");
        sociedades.add(soc);
        Sociedad soc2 = new Sociedad();
        soc2.setActivo(1);
        soc2.setNombre("RYMSA");
        soc2.setCedula("3101724817");
        sociedades.add(soc2);
        Sociedad soc3 = new Sociedad();
        soc3.setActivo(1);
        soc3.setNombre("IRASA");
        soc3.setCedula("3101119637");
        sociedades.add(soc3);
        Sociedad soc4 = new Sociedad();
        soc4.setActivo(1);
        soc4.setNombre("KATRA");
        soc4.setCedula("3101119531");
        sociedades.add(soc4);
        Sociedad soc5 = new Sociedad();
        soc5.setActivo(1);
        soc5.setNombre("OPILOG");
        soc5.setCedula("3101466557");
        sociedades.add(soc5);
        Sociedad soc6 = new Sociedad();
        soc6.setActivo(1);
        soc6.setNombre("TURINTEL");
        soc6.setCedula("3101468003");
        sociedades.add(soc6);
    }

    private void filtrarTbReceipsPorCIA() {

        String desc = cmbSociedades.getSelectedItem().toString();
        Sociedad soc = Sociedad.obtenerSociedadPorDesc(desc, sociedades);
        limpiarTabla(tbReceips);
        setUpProgressBar("Cargando facturas...", receipList.size());
        ArrayList<Receips> lista = new ArrayList<>();
        this.receipList.forEach(e -> {
            if (soc != null) {
                loadTbRecepsPorCIA(e, soc);
                String cedRec = e.getReceptor().getIdentificacion().substring(3, e.getReceptor().getIdentificacion().length());
                if (soc.getCedula().equalsIgnoreCase(cedRec)) {
                    lista.add(e);
                }

            } else {
                loadTbReceptsWithoutFilter(e);
                lista.add(e);
            }
            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
        });
        ShowDetails(lista);
        jProgressBar1.setString("Proceso completo...");
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        showManteniminetoFacturasInfo();
    }

    private void checkLoadTbMantenimieto(Receips rec, Sociedad soc) {

        String cedRec = rec.getReceptor().getIdentificacion().substring(3, rec.getReceptor().getIdentificacion().length());
        if (soc.getCedula().equalsIgnoreCase(cedRec)) {
            String totalComprobante = AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalComprobante());
            DefaultTableModel model = (DefaultTableModel) this.tbMntFact.getModel();
            try {
                Departamento d = Departamento.getDepartamentoPorStringId(listaDepartamentos, rec.getIdDepartamento());
                String descripcionDep = "";
                if (d != null) {
                    descripcionDep = d.getDescripcion();
                }
                addRowTbMantenimiento(model, rec, totalComprobante, descripcionDep);
            } catch (Exception e) {
                System.out.println("view.ReceiptMaintenance.loadAsingTbReceps() error " + e.getMessage());
            }
        }
    }

    private void addRowTbMantenimiento(DefaultTableModel model, Receips rec, String totalComprobante, String descripcionDep) {
        model.addRow(new Object[]{
            rec.getReceptor().getNombre(),
            rec.getEmisor().getNombre(),
            rec.getNumeroConsecutivo(),
            AppStaticValues.dateFormat.format(rec.getFechaEmision()),
            totalComprobante,
            rec.getResumenFactura().getCodigoMoneda().getCodigoMoneda(), //getCmbDepartamentos()//"Detalles " + rec.getListLineaDetalle().size()
            descripcionDep,
            rec.getCuentaGeneral(),
            rec.getCuentaPresupuesto(),
            rec.getAprobadoDirector() == 1 ? true : false,
            rec.getRechazado() == 1 ? true : false,
            rec.getEstado(),
            rec.getExactus(),
            rec.esCajaChica() == 1 ? true : false,
            rec.getSubTipoAsiento(),
            "",
            rec.getClave()
        });
    }

    private void saveTbMantenimientoFacturas() {
        try {
            tbMntFact.getCellEditor().stopCellEditing();
        } catch (Exception e) {
        }
        int size = this.tbMntFact.getRowCount();
        setUpProgressBar("Guardando cambios...", size - 1);
        for (int i = 0; i < size; i++) {
            saveRowTbMantenimientoFacturas(i);
        }
        this.jProgressBar1.setString("Proceso completado correctamente");
        CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
    }

    private void saveRowTbMantenimientoFacturas(int i) {
        System.out.println("guardando fila " + i);
        String clave = tbMntFact.getValueAt(i, 16).toString();
        Receips r = Receips.getReceipByClave(clave, this.receipList);
        String dep = tbMntFact.getValueAt(i, 6).toString();
        String cm = tbMntFact.getValueAt(i, 7).toString();
        String cp = tbMntFact.getValueAt(i, 8).toString();
        Departamento d = null;
        if (!dep.isEmpty() && !cm.equals("") && !cp.equals("")) {
            d = Departamento.getDepartamento(listaDepartamentos, dep);
            if (d != null) {

                save(d, r, i);
            } else {
                this.infoCargada = false;
                restaurarReceipRowTbMntFact(r, i);
                JOptionPane.showMessageDialog(null, "No se puede aprobar o rechazar una factura sin asignarle las cuentas de presupuesto");
                System.out.println("view.ReceiptMaintenance.saveTbMantenimientoFacturas() no se ha actualizado " + i);
                this.infoCargada = true;
            }

        } else {
            restaurarReceipRowTbMntFact(r, i);
            JOptionPane.showMessageDialog(null, "No se ha actualizado la fila " + i + " por falta información");
            System.out.println("no se ha actualizado la fila " + i + " por falta información");
        }
        this.jProgressBar1.setValue(i);
    }

    /**
     * this method restore the values for a receipt register in to the tb.
     *
     * @param i is the tb row
     * @param r is the receipt register to restore
     */
    private void restaurarReceipRowTbMntFact(Receips r, int i) {
        loadingInfo = true;
        Departamento d = Departamento.getDepartamentoPorStringId(listaDepartamentos, r.getIdDepartamento());
        tbMntFact.setValueAt(d == null ? "" : d.getDescripcion(), i, 6);
        tbMntFact.setValueAt(r.getCuentaGeneral(), i, 7);
        tbMntFact.setValueAt(r.getCuentaPresupuesto(), i, 8);
        tbMntFact.setValueAt(r.getAprobadoDirector() == 1 ? true : false, i, 9);
        tbMntFact.setValueAt(r.getRechazado() == 1 ? true : false, i, 10);
        tbMntFact.setValueAt(r.esCajaChica() == 1 ? true : false, i, 13);
        loadingInfo = false;
    }

    private void saveRowTbMantenimientoFacturas(int i, Receips r) {
        System.out.println("guardando fila " + i);
        String dep = tbMntFact.getValueAt(i, 6).toString();
        String cm = tbMntFact.getValueAt(i, 7).toString();
        String cp = tbMntFact.getValueAt(i, 8).toString();
        Departamento d = null;
        if (dep != null && !dep.isEmpty() && !cm.equals("") && !cp.equals("")) {
            d = Departamento.getDepartamento(listaDepartamentos, dep);
            if (d != null) {
                boolean ap = (boolean) this.tbMntFact.getValueAt(i, 9);
                boolean rec = (boolean) this.tbMntFact.getValueAt(i, 10);
                if (!ap && !rec) {
                    int input = JOptionPane.showConfirmDialog(null, "Este proceso va a liberar la factura para otros usuarios,"
                            + "\ndesea continuar?");
                    if (input == 0) {
                        liberarFactura(r, i);
                    } else {
                        restaurarReceipRowTbMntFact(r, i);
                        JOptionPane.showMessageDialog(null, "Se ha cancelado la operación");

                    }

                } else {
                    save(d, r, i);
                }

            } else {
                this.infoCargada = false;
                tbMntFact.setValueAt("", i, 6);
                JOptionPane.showMessageDialog(null, "No se puede aprobar o rechazar una factura sin asignarle las cuentas de presupuesto");
                System.out.println("view.ReceiptMaintenance.saveTbMantenimientoFacturas() no se ha actualizado " + i);
                this.infoCargada = true;
            }

        } else {

            restaurarReceipRowTbMntFact(r, i);
            JOptionPane.showMessageDialog(null, "No se ha actualizado la fila " + i + " por falta información");
            System.out.println("no se ha actualizado la fila " + i + " por falta información");
        }
        this.jProgressBar1.setValue(i);
    }

    private void setUpProgressBar(String message, int maxvalue) {
        this.jProgressBar1.setMaximum(maxvalue);
        this.jProgressBar1.setVisible(true);
        this.jProgressBar1.setValue(0);
        this.jProgressBar1.setString(message);
    }

    private void setUpAfterSaveProgressBar(String message) {
        this.jProgressBar1.setVisible(true);
        jProgressBar1.setMaximum(1);
        this.jProgressBar1.setValue(1);
        this.jProgressBar1.setString(message);
        CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
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

    private void save(Departamento d, Receips r, int i) {
        Receips oldR = getOldR(r);
        String cuentaGeneral = tbMntFact.getValueAt(i, 7).toString();
        String cuenta = tbMntFact.getValueAt(i, 8).toString();
        String Exactus = tbMntFact.getValueAt(i, 12).toString();
        boolean aprobado = (Boolean) tbMntFact.getValueAt(i, 9);
        boolean rechazado = (Boolean) tbMntFact.getValueAt(i, 10);
        int cajaChica = (Boolean) tbMntFact.getValueAt(i, 13) ? 1 : 0;

        if (!cuentaGeneral.equalsIgnoreCase(r.getCuentaGeneral())
                || !cuenta.equalsIgnoreCase(r.getCuentaPresupuesto())
                || !Exactus.equalsIgnoreCase(r.getExactus())
                || aprobado != (oldR.getAprobadoDirector() == 1 ? true : false)
                || rechazado != (oldR.getRechazado() == 1 ? true : false)
                || oldR.esCajaChica() != cajaChica) {
            r.setIdDepartamento(Integer.parseInt(d.getDEPARTAMENTO()));
            r.setCuentaGeneral(cuentaGeneral);
            r.setCuentaPresupuesto(cuenta == null ? "" : cuenta);
            r.setExactus(Exactus == null ? "" : Exactus);
            r.setCajaChica(cajaChica);
            r.setAprobadoDirector(aprobado ? 1 : 0);
            r.setRechazado(rechazado ? 1 : 0);
            //String user = r.getAprobadoDirector() ==1? DataUser.username : "SA";
            r.setPropietario(DataUser.username);
            //r.setPropietario( System. getenv("USERNAME").toUpperCase());
            boolean res = data.CrudFacturaElectronica.addUpdateFacturaElectronica(r);
            if (res) {
                System.out.println("Factura fila " + i + " guardada");
                setUpAfterSaveProgressBar("Registro guardado correctamente");
            } else {
                rollbackReceip(oldR, r);
                restaurarReceipRowTbMntFact(r, i);
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error, intente nuevamente");
            }
        } else {
            setUpAfterSaveProgressBar("No hay cambios");
            System.out.println("no hay cambios en fila " + i);
        }
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

    private void liberarFactura(Receips r, int i) {

        r.setPropietario("SA");
        r.setAprobadoDirector(0);
        r.setRechazado(0);
        r.setCajaChica(0);
        r.setIdDepartamento(0);
        r.setCuentaGeneral("");
        r.setCuentaPresupuesto("");

        boolean res = data.CrudFacturaElectronica.addUpdateFacturaElectronica(r);
        if (res) {
            restaurarReceipRowTbMntFact(r, i);
            setUpAfterSaveProgressBar("Factura " + r.getNumeroConsecutivo() + " liberada");
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error, intente en otro momento");
        }

    }

    /**
     * this method refresh all calculated info shown to the user on a past
     * interaction, so the info does not show older and deprecated info
     */
    private void clearInfo() {
        this.txtTotatIVA.setText("");
        this.txtTotalCompras.setText("");
        this.txtTotalComprasPlusIV.setText("");
        this.txtTotalComprasPlusIVPlusOtros.setText("");
        txtComp1p.setText("");
        txtComp4p.setText("");
        txtComp8P.setText("");
        txtComp13P.setText("");
        txtIm1p.setText("");
        txtIm4p.setText("");
        txtIm8P.setText("");
        txtIm13P.setText("");
    }

    /**
     * this method resumes all the steps to refresh previously loaded and
     * filtered info
     */
    private void ShowDetails(ArrayList<Receips> lista) {
        System.out.println("view.ReceiptMaintenance.ShowDetails()");
        int tipoFac = this.cmbTipoFactura.getSelectedIndex();
        boolean tipo = tipoFac == 0 ? true : false;
        showGeneralTottalsListReceips(tipo, lista); //showTottalsTbReceips(tipo);
        showDetailCompras(tipo, lista);
        showDetailIV(tipo, lista);

    }

    private void showDetailCompras(boolean tipo, ArrayList<Receips> lista) {
        double compras13 = 0.0;
        double compras1 = 0.0;
        double compras4 = 0.0;
        double compras8 = 0.0;
        String moneda = this.cmbMoneda.getSelectedItem().toString();
        if (lista != null) {
            for (Receips r : lista) {
                System.out.println("view.MantenimientoFacturaElectronica.showDetailCompras() clave " + r.getClave());
                String CodigoMoneda = r.getResumenFactura().getCodigoMoneda().getCodigoMoneda();
                if (CodigoMoneda.equalsIgnoreCase("ND")) {
                    CodigoMoneda = "CRC";
                }
                boolean monedaCorrecta = CodigoMoneda.equalsIgnoreCase(moneda);

                if (tipo == r.isBienes() && monedaCorrecta) {
                    ArrayList<LineaDetalle> LineaDetalles = r.getListLineaDetalle();
                    for (LineaDetalle l : LineaDetalles) {
                        entitys.Impuesto imp = l.getImpuesto();
                        if (imp != null) {
                            String porcentage = imp.getTarifa() + "";
                            switch (porcentage) {
                                case "1.0":
                                    compras1 += (l.getMontoTotal() - l.getDescuento().getMontoDescuento());
                                    break;
                                case "4.0":
                                    compras4 += (l.getMontoTotal() - l.getDescuento().getMontoDescuento());
                                    break;
                                case "13.0":
                                    compras13 += (l.getMontoTotal() - l.getDescuento().getMontoDescuento());
                                    break;
                                case "8.0":
                                    compras8 += (l.getMontoTotal() - l.getDescuento().getMontoDescuento());
                                    break;
                            }
                        }

                    }
                }
            }
        }

        txtComp1p.setText(AppStaticValues.numberFormater.format(compras1));
        txtComp4p.setText(AppStaticValues.numberFormater.format(compras4));
        txtComp8P.setText(AppStaticValues.numberFormater.format(compras8));
        txtComp13P.setText(AppStaticValues.numberFormater.format(compras13));

    }

    /**
     * this method makes some calculations taking the loaded table tbreceipt
     * info, so it calculates the totals for every tax ("impuestos") percentage,
     * and then loads the results to the GUI
     */
    private void showDetailIV(boolean tipo, ArrayList<Receips> lista) {
        double imp13 = 0.0;
        double imp1 = 0.0;
        double imp4 = 0.0;
        double imp8 = 0.0;
        if (lista != null) {
            String moneda = this.cmbMoneda.getSelectedItem().toString();

            for (Receips r : lista) {
                String codmo = r.getResumenFactura().getCodigoMoneda().getCodigoMoneda();
                if (codmo.equalsIgnoreCase("ND")) {
                    codmo = "CRC";
                }
                boolean monedaCorrecta = codmo.equalsIgnoreCase(moneda);

                if (tipo == r.isBienes() && monedaCorrecta) {
                    ArrayList<LineaDetalle> LineaDetalles = r.getListLineaDetalle();

                    for (LineaDetalle l : LineaDetalles) {
                        Impuesto imp = l.getImpuesto();
                        if (imp != null) {
                            String porcentage = imp.getTarifa() + "";
                            switch (porcentage) {
                                case "1.0":
                                    imp1 += imp.getMonto();// Double.parseDouble(tbReceips.getValueAt(i, 5).toString());
                                    break;
                                case "4.0":
                                    imp4 += imp.getMonto();// += Double.parseDouble(tbReceips.getValueAt(i, 5).toString());
                                    break;
                                case "13.0":
                                    imp13 += imp.getMonto();// += Double.parseDouble(tbReceips.getValueAt(i, 5).toString());
                                    break;
                                case "8.0":
                                    imp8 += imp.getMonto();// += Double.parseDouble(tbReceips.getValueAt(i, 5).toString());
                                    break;
                            }
                        }
                    }
                }
            }
        }
        txtIm1p.setText(AppStaticValues.numberFormater.format(imp1));
        txtIm4p.setText(AppStaticValues.numberFormater.format(imp4));
        txtIm8P.setText(AppStaticValues.numberFormater.format(imp8));
        txtIm13P.setText(AppStaticValues.numberFormater.format(imp13));
    }

    /**
     * this method makes some calculations taking the loaded table tbreceipt
     * info, so it calculates the final totals for every tax summary
     * ("impuestos"), purchases ("compras") and other percentage, and then loads
     * the results to the GUI
     */
    private void showTottalsTbReceips(boolean tipo, ArrayList<Receips> lista) {
        int rows = this.tbReceips.getRowCount();
        double totalCompras = 0.0;
        double totalIV = 0.0;
        double totalComprasPlusIV = 0.0;
        double otros = 0.0;
        for (int i = 0; i < rows; i++) {
            if (tipo == (boolean) (this.tbReceips.getValueAt(i, 10))) {
                totalCompras += Double.parseDouble(tbReceips.getValueAt(i, 5).toString());
                totalIV += Double.parseDouble(tbReceips.getValueAt(i, 4).toString());
                otros += Double.parseDouble(tbReceips.getValueAt(i, 6).toString());
            }
        }

        totalComprasPlusIV = totalCompras + totalIV;
        this.txtTotatIVA.setText(String.format("###,###,###.2f", totalIV));
        String ttformated = AppStaticValues.numberFormater.format("" + totalCompras);
        //this.txtTotalCompras.setText(String.format("%.2f", totalCompras));
        this.txtTotalCompras.setText(ttformated);
        this.txtTotalComprasPlusIV.setText(String.format("%.2f", totalComprasPlusIV));
        double totalComprasPlusOtros = totalComprasPlusIV + otros;
        this.txtTotalComprasPlusIVPlusOtros.setText(String.format("%.2f", totalComprasPlusOtros));
    }

    private void showGeneralTottalsListReceips(boolean tipo) {
        System.out.println("view.ReceiptMaintenance.showGeneralTottalsListReceips()");
        double totalCompras = 0.0;
        double totalIV = 0.0;
        double totalComprasPlusIV = 0.0;
        double otros = 0.0;
        String moneda = this.cmbMoneda.getSelectedItem().toString();

        if (this.receipList != null) {
            for (Receips r : receipList) {
                String codmo = r.getResumenFactura().getCodigoMoneda().getCodigoMoneda();
                if (codmo.equalsIgnoreCase("ND")) {
                    codmo = "CRC";
                }
                boolean monedaCorrecta = codmo.equalsIgnoreCase(moneda);
                if (tipo == r.isBienes() && monedaCorrecta) {
                    for (LineaDetalle detalle : r.getListLineaDetalle()) {
                        totalCompras += detalle.getSubTotal();
                        totalIV += detalle.getImpuesto().getMonto();

                    }
                    otros += r.getResumenFactura().getTotalOtrosCargos();
                }

            }
        }

        totalComprasPlusIV = totalCompras + totalIV;
        //this.txtTotatIVA.setText(String.format("%.2f", totalIV));
        this.txtTotatIVA.setText(AppStaticValues.numberFormater.format(totalIV));
        this.txtTotalCompras.setText(AppStaticValues.numberFormater.format(totalCompras));
        this.txtTotalComprasPlusIV.setText(AppStaticValues.numberFormater.format(totalComprasPlusIV));
        double totalComprasPlusOtros = totalComprasPlusIV + otros;
        this.txtTotalComprasPlusIVPlusOtros.setText(AppStaticValues.numberFormater.format(totalComprasPlusOtros));
    }

    private void showGeneralTottalsListReceips(boolean tipo, ArrayList<Receips> lista) {
        System.out.println("view.ReceiptMaintenance.showGeneralTottalsListReceips()");
        double totalCompras = 0.0;
        double totalIV = 0.0;
        double totalComprasPlusIV = 0.0;
        double otros = 0.0;
        String moneda = this.cmbMoneda.getSelectedItem().toString();

        if (lista != null) {
            for (Receips r : lista) {
                try {
                    String codmo = r.getResumenFactura().getCodigoMoneda().getCodigoMoneda();
                    if (codmo.equalsIgnoreCase("ND")) {
                        codmo = "CRC";
                    }
                    boolean monedaCorrecta = codmo.equalsIgnoreCase(moneda);
                    if (tipo == r.isBienes() && monedaCorrecta) {
                        for (LineaDetalle detalle : r.getListLineaDetalle()) {
                            totalCompras += detalle.getSubTotal();
                            Impuesto imp = detalle.getImpuesto();
                            if (imp != null) {
                                totalIV += imp.getMonto();
                            }

                        }
                        otros += r.getResumenFactura().getTotalOtrosCargos();
                    }
                } catch (Exception e) {
                    System.out.println("view.MantenimientoFacturaElectronica.showGeneralTottalsListReceips() error " + e.getMessage() + r.getClave());
                }

            }
        }

        totalComprasPlusIV = totalCompras + totalIV;
        //this.txtTotatIVA.setText(String.format("%.2f", totalIV));
        this.txtTotatIVA.setText(AppStaticValues.numberFormater.format(totalIV));
        this.txtTotalCompras.setText(AppStaticValues.numberFormater.format(totalCompras));
        this.txtTotalComprasPlusIV.setText(AppStaticValues.numberFormater.format(totalComprasPlusIV));
        double totalComprasPlusOtros = totalComprasPlusIV + otros;
        this.txtTotalComprasPlusIVPlusOtros.setText(AppStaticValues.numberFormater.format(totalComprasPlusOtros));
    }

    /**
     * this method permits the user to choose the folder receipt location from
     * where the receipts can be load
     */
    private void readReceipFiles(String path) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && (listOfFiles[i].getName().contains(".xml") || listOfFiles[i].getName().contains(".XML"))) {
                System.out.println("File " + listOfFiles[i].getName());
                //ReceipFromXML r = new ReceipFromXML(path + "\\" + listOfFiles[i].getName());
                receipList = services.FileHandler.readReceipFiles(path);//Receips.getReceipsFromJson(path + "\\" + listOfFiles[i].getName());

            }
        }

    }

    private void readReceipFiles() {

        for (int i = 0; i < this.receipList.size(); i++) {
            Receips r = receipList.get(i);
            String path = logic.AppStaticValues.respaldoArchivosGuardados + "\\" + r.getNombreArchivo();
            System.out.println("archivo " + path);
            Receips rec = services.FileHandler.getJsonStringFromFile(path);//Receips.getReceipsFromJson(path + "\\" + listOfFiles[i].getName());
            checkLoadTbReceps(rec, false);
        }

    }

    private boolean numeroConsecutivoExiste(String numeroConsecutivo) {
        boolean existe = false;
        try {
            int count = 0;
            int size = tbReceips.getRowCount();
            while (!existe && count < size) {
                String claveTb = tbReceips.getValueAt(count, 1).toString();
                if (numeroConsecutivo.equalsIgnoreCase(claveTb)) {
                    existe = true;
                    System.out.println("view.ReceiptMaintenance.numeroConsecutivoExiste() consecutivo existe " + numeroConsecutivo);
                }
                count++;
            }
        } catch (Exception e) {
            System.out.println("view.MantenimientoFacturaElectronica.numeroConsecutivoExiste() error " + e.getMessage());
        }
        return existe;
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
            java.util.logging.Logger.getLogger(MantenimientoFacturaElectronica.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MantenimientoFacturaElectronica.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MantenimientoFacturaElectronica.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MantenimientoFacturaElectronica.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ArrayList<entitys.UsuariosPresupuesto> lista = new ArrayList<>();
                new MantenimientoFacturaElectronica(lista).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel blSinCuentaPres;
    private javax.swing.JButton btnAddReceips;
    private javax.swing.JButton btnAsingGroup;
    private javax.swing.JButton btnBuscarFactura;
    private javax.swing.JButton btnCalculator;
    private javax.swing.JButton btnCorreosOmitidos;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnExpCont;
    private javax.swing.JButton btnHideRow;
    private javax.swing.JButton btnManual;
    private javax.swing.JButton btnNotifications;
    private javax.swing.JButton btnOpenPdf;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRefreshReceips;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSaveConj;
    private javax.swing.JButton btnSettings;
    private javax.swing.JCheckBox chbConj;
    private javax.swing.JComboBox<String> cmbCuentaConj;
    private javax.swing.JComboBox<String> cmbDepConj;
    private javax.swing.JComboBox<String> cmbEstadoAsignacion;
    private javax.swing.JComboBox<String> cmbEstadoRegistro;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JComboBox<String> cmbSociedades;
    private javax.swing.JComboBox<String> cmbTipoFactura;
    private javax.swing.JPanel down;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private com.toedter.calendar.JDateChooser jdtFin;
    private com.toedter.calendar.JDateChooser jdtInicio;
    private javax.swing.JTextArea jtxtfDetalles;
    private javax.swing.JLabel lbCheckConnection;
    private javax.swing.JLabel lbDetFactura;
    private javax.swing.JLabel lbPendientes;
    private javax.swing.JLabel lbRegistradas;
    private javax.swing.JLabel lbTbMantResumen;
    private javax.swing.JLabel lbTotalFacturas;
    private javax.swing.JLabel lbVersion;
    private javax.swing.JPanel left;
    private javax.swing.JMenuItem menuAbrirPDF;
    private javax.swing.JMenuItem mnCopiarNFac;
    private javax.swing.JMenuItem mnPropietario;
    private javax.swing.JMenuItem mnuUpdateExactus;
    private javax.swing.JPanel pnlAsignacionesFacturas;
    private javax.swing.JPanel pnlExactus;
    private javax.swing.JPanel pnlIvaMantenance;
    private javax.swing.JPanel pnlMenuTbReceip;
    private javax.swing.JPanel pnlReceipHeadings;
    private javax.swing.JPanel pnlResumen;
    private javax.swing.JPanel pnlTotales;
    private javax.swing.JPopupMenu popUpTbReceips;
    private javax.swing.JPanel rigth;
    private javax.swing.JTabbedPane tabExactus;
    private javax.swing.JTable tbMntFact;
    private javax.swing.JTable tbReceips;
    private javax.swing.JToolBar toolbConjunto;
    private javax.swing.JTextField txtBuscarConsecutivo;
    private javax.swing.JTextField txtComp13P;
    private javax.swing.JTextField txtComp1p;
    private javax.swing.JTextField txtComp4p;
    private javax.swing.JTextField txtComp8P;
    private javax.swing.JTextField txtIm13P;
    private javax.swing.JTextField txtIm1p;
    private javax.swing.JTextField txtIm4p;
    private javax.swing.JTextField txtIm8P;
    private javax.swing.JTextField txtTotalCompras;
    private javax.swing.JTextField txtTotalComprasPlusIV;
    private javax.swing.JTextField txtTotalComprasPlusIVPlusOtros;
    private javax.swing.JTextField txtTotatIVA;
    private javax.swing.JPanel up;
    // End of variables declaration//GEN-END:variables

}

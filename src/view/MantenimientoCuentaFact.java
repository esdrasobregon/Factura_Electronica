/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import data.CrudAsiento;
import data.DataUser;
import entitys.AsientoFactura;
import entitys.CorreoExcluidoFE;
import entitys.Departamento;
import entitys.LineaDetalle;
import entitys.Presupuesto;
import entitys.Receips;
import entitys.Sociedad;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import logic.AppStaticValues;
import static view.util.JTableCommonFunctions.limpiarTabla;

/**
 *
 * @author eobregon
 */
public class MantenimientoCuentaFact extends javax.swing.JPanel {

    boolean loadingInfo = false;
    ArrayList<Receips> receipList;
    ArrayList<Departamento> listaDepartamentos;
    ArrayList<Departamento> departamentosPropios;
    data.CrudPresupuesto crudp;
    ArrayList<Presupuesto> listaPresupuesto;
    int registradas = 0;
    ArrayList<Sociedad> sociedades;
    data.CrudAsiento crAsientos;
    ArrayList<AsientoFactura> listaAsientos;
    ArrayList<CorreoExcluidoFE> listaCorreosOmitidos;
    Action openXMLCom;
    private ArrayList<entitys.UsuariosPresupuesto> listaAccesos;

    /**
     * Creates new form MantenimientoCuentaFact
     */
    public MantenimientoCuentaFact(ArrayList<entitys.UsuariosPresupuesto> accesos) {
        initComponents();
        this.listaAccesos = accesos;
        crAsientos = new CrudAsiento();
        this.receipList = new ArrayList<>();
        prepareGUI();
    }

    private void prepareGUI() {
        loadInfo();
        setView();
        setListeners();
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

                    /*Receips r = Receips.getReceipByClave(clave, receipList);
                    MantenimientoFacturaElectronica redt = new MantenimientoFacturaElectronica(r);
                    redt.setVisible(true);*/
                    openTbReceipPDF();

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

                    setTxtAreaDetalles();
                }
            }
        });
        tbMntFact.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent evt) {
                int row = tbMntFact.getSelectedRow();
                int column = tbMntFact.getSelectedColumn();
                //new
                //boolean chkconj = chbConj.isSelected();
                if (!loadingInfo && row > -1) {
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
                        refreshTbMntFacAfterFailAsing();
                        JOptionPane.showMessageDialog(null, "Se ha cancelado la operación");

                    }

                } else {
                    save(d, r, i);
                }

            } else {

                tbMntFact.setValueAt("", i, 6);
                JOptionPane.showMessageDialog(null, "No se puede aprobar o rechazar una factura sin asignarle las cuentas de presupuesto");
                System.out.println("view.ReceiptMaintenance.saveTbMantenimientoFacturas() no se ha actualizado " + i);

            }

        } else {

            refreshTbMntFacAfterFailAsing();
            JOptionPane.showMessageDialog(null, "No se ha actualizado la fila " + i + " por falta información");
            System.out.println("no se ha actualizado la fila " + i + " por falta información");
        }
        this.jProgressBar1.setValue(i);
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
                jProgressBar1.setValue(0);
                jProgressBar1.setVisible(true);
                jProgressBar1.setString("Cambios correctamente guardados...");
                view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            } else {
                //rollbackReceip(oldR, r);
                //restaurarReceipRowTbMntFact(r, i);
                refreshTbMntFacAfterFailAsing();
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error, intente nuevamente");
            }
        } else {
            //setUpAfterSaveProgressBar("No hay cambios");
            System.out.println("no hay cambios en fila " + i);
        }
    }

    private void openTbReceipPDF() {
        try {
            int row = tbMntFact.getSelectedRow();
            String clave = tbMntFact.getValueAt(row, 16).toString();
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

        } catch (Exception e) {
            System.out.println("view.MantenimientoCuentaFact.openTbReceipPDF() error " + e.getMessage());
        }
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
            jProgressBar1.setValue(0);
            jProgressBar1.setVisible(true);
            jProgressBar1.setString("Cambios correctamente guardados...");
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            refreshTbMntFacAfterFailAsing();
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error, intente en otro momento");
        }

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

        JComponent content = (JComponent) this;
        content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put((KeyStroke) openXMLCom.getValue(Action.ACCELERATOR_KEY), "showMessage");
        content.getActionMap().put("showMessage", openXMLCom);
    }

    private void openXmlFromSelectedRow() {
        int row = tbMntFact.getSelectedRow();
        if (row > -1) {
            try {
                String clave = tbMntFact.getValueAt(row, 16).toString();
                Receips r = Receips.getReceipByClave(clave, receipList);
                String path = logic.AppStaticValues.respaldoArchivosGuardados + "\\" + r.getNombreArchivo();
                File pdfFile = new File(path);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("xml file not found.");
                    JOptionPane.showMessageDialog(null, "No se ha encontrado el archivo...");
                }
            } catch (Exception ex) {
                System.out.println(".actionPerformed() error " + ex.getMessage());
            }
        }
    }

    private void setTxtAreaDetalles() {
        if (!loadingInfo) {

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    try {
                        int row = tbMntFact.getSelectedRow();
                        if (tbMntFact.getSelectedRows().length < 2) {
                            String clave = tbMntFact.getValueAt(row, 16).toString();
                            Receips r = Receips.getReceipByClave(clave, receipList);
                            jtxtfDetalles.setText("");

                            data.CRUDLineaDetalle ln = new data.CRUDLineaDetalle();
                            ArrayList<LineaDetalle> lista = ln.obtenerLineas(clave);
                            /*Receips rec = services.FileHandler.getJsonStringFromFile(logic.AppStaticValues.respaldoArchivosGuardados + r.getNombreArchivo());
                            ArrayList<LineaDetalle> lista = rec.getListLineaDetalle();*/
                            String lineas = "";
                            double montoTotalLinea = 0;
                            for (LineaDetalle l : lista) {
                                montoTotalLinea += l.getMontoTotalLinea();
                                lineas += "\nLínea " + l.getNumeroLinea()
                                        + " " + l.getDetalle()
                                        + " |Total: "
                                        + r.getResumenFactura().getCodigoMoneda().getCodigoMoneda()
                                        + " " + AppStaticValues.numberFormater.format(l.getMontoTotalLinea());
                            }
                            lineas += "\nMonto Total: "
                                    + r.getResumenFactura().getCodigoMoneda().getCodigoMoneda()
                                    + " " + AppStaticValues.numberFormater.format(montoTotalLinea);
                            jtxtfDetalles.setText(lineas.toUpperCase());
                        } else {
                            jtxtfDetalles.setText("");
                        }
                    } catch (Exception e) {
                        System.err.println("view.MantenimientoCuentaFact.setTxtAreaDetalles() error " + e.getMessage());
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();

        }
    }

    private void setView() {
        this.btnCorreosOmitidos.setVisible(false);
        this.jProgressBar1.setVisible(true);
        setDateChooserLook();
        tbMntFact.getColumnModel().getColumn(4).setCellRenderer(logic.AppStaticValues.rightRenderer);
    }

    private void setDateChooserLook() {
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

    private void setListeners() {
        this.txtBuscarConsecutivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFacturas();
            }
        });
        addTbMantenimientoFacturasEvents();
        openXMLFile();
    }

    private void loadInfo() {
        Sociedad s = new Sociedad();
        sociedades = s.quemarSociedades();
        data.crudDepartamento crd = new data.crudDepartamento();
        this.listaDepartamentos = crd.getSqlDepartamentos();

        if (!listaDepartamentos.isEmpty()) {

            this.departamentosPropios = new ArrayList<>();
            loadingInfo = true;
            listaAccesos.forEach(e -> {
                if (e.getACCESO().equalsIgnoreCase("S")) {
                    Departamento d = Departamento.getDepartamentoByCodDepa(listaDepartamentos, e.getCOD_DEPA());
                    System.err.println("departamento " + e.getDETA_DEPA() + " acceso " + e.getACCESO() + " CODEPA " + e.getCOD_DEPA());
                    if (d != null) {

                        departamentosPropios.add(d);
                        System.out.println("departamamento propio " + d.getDescripcion());

                    }
                }
            });
            loadingInfo = false;
            tbMntFact.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(getCmbDepartamentos()));
            tbMntFact.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(getCmbCuentaPresupuesto()));

        }
    }

    public javax.swing.JComboBox getCmbCuentaPresupuesto() {
        javax.swing.JComboBox cmb = new javax.swing.JComboBox();
        int row = tbMntFact.getSelectedRow();

        try {
            String depDesc = tbMntFact.getValueAt(row, 6).toString();
            Departamento dp = Departamento.getDepartamento(listaDepartamentos, depDesc);
            cmb.addItem("");
            data.CrudPresupuesto crp = new data.CrudPresupuesto();
            ArrayList<Presupuesto> lista = crp.obtenerPresupuestoPorDep(dp.getDEPARTAMENTO());
            for (Presupuesto d : lista) {

                String item = d.getCONCEPATOADETALLE().trim() + "-" + d.getAREADETALLE().trim();
                //int id = Integer.parseInt(item.substring(0, 2));
                cmb.addItem(item);
            }
            cmb.setSelectedIndex(0);

            cmb.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent arg0) {

                }
            });
        } catch (Exception e) {
            System.err.println("view.MantenimientoCuentaFact.getCmbCuentaPresupuesto() error " + e.getMessage());
        }
        return cmb;

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
                            refreshTbMntFacAfterFailAsing();
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

    private void getCuenataSeleccionada(javax.swing.JComboBox cmb, Receips r, int rowSelected) {
        try {
            String selectedItem = cmb.getSelectedItem().toString();
            Departamento d = Departamento.getDepartamento(departamentosPropios, selectedItem);
            if (d != null) {
                data.CrudPresupuesto crp = new data.CrudPresupuesto();
                ArrayList<Presupuesto> ctap = crp.obtenerPresupuestoPorDep(d.getDEPARTAMENTO());
                javax.swing.JComboBox cmbCtaP = getCmbCuentas(ctap);
                String message = "Escoja su cuenta " + d.getDescripcion();// ctap.get(0).getCTAPRESUPUESTO() + "-" + ctap.get(0).getCONCEPATOADETALLE();
                JOptionPane.showMessageDialog(null, cmbCtaP, message, JOptionPane.QUESTION_MESSAGE);
                String itemSelected = cmbCtaP.getSelectedItem().toString();

                if (itemSelected == null || itemSelected.isEmpty()) {
                    refreshTbMntFacAfterFailAsing();
                } else {
                    String selected = itemSelected.substring(0, 11);
                    if (selected.endsWith("00")) {

                        refreshTbMntFacAfterFailAsing();
                        JOptionPane.showMessageDialog(null, "La cuenta seleccionada no es correcta");
                    } else {
                        String ctaGeneral = cmbCtaP.getSelectedItem().toString().trim().substring(0, 9) + "00";
                        Presupuesto prex = Presupuesto.getPresupuestoGen(ctaGeneral, ctap);
                        tbMntFact.setValueAt(ctaGeneral + "-" + prex.getCONCEPATOADETALLE(), rowSelected, 7);
                        tbMntFact.setValueAt(cmbCtaP.getSelectedItem().toString().trim(), rowSelected, 8);
                    }
                }
            } else {

                if (!selectedItem.equals("")) {
                    JOptionPane.showMessageDialog(null, "Usted no tiene acceso a este departamento");
                    refreshTbMntFacAfterFailAsing();
                } else {
                    refreshTbMntFacAfterFailAsing();
                }
            }
        } catch (Exception e) {
            refreshTbMntFacAfterFailAsing();
            System.out.println("view.ReceiptMaintenance.getCuenataSeleccionada() error " + e.getMessage());
        }
    }

    private void refreshTbMntFacAfterFailAsing() {
        loadingInfo = true;
        int row = tbMntFact.getSelectedRow();
        String clave = tbMntFact.getValueAt(row, 16).toString();
        Receips r = Receips.getReceipByClave(clave, receipList);
        Departamento d = Departamento.getDepartamentoPorStringId(listaDepartamentos, r.getIdDepartamento());
        String descripcionDep = "";
        if (d != null) {
            descripcionDep = d.getDescripcion();
        }
        tbMntFact.setValueAt(descripcionDep, row, 6);
        tbMntFact.setValueAt(r.getCuentaGeneral(), row, 7);
        tbMntFact.setValueAt(r.getCuentaPresupuesto(), row, 8);
        tbMntFact.setValueAt((r.getAprobadoDirector() == 1 ? true : false), row, 9);
        tbMntFact.setValueAt((r.getRechazado() == 1 ? true : false), row, 10);
        tbMntFact.setValueAt((r.esCajaChica() == 1 ? true : false), row, 13);
        loadingInfo = false;
    }

    private boolean facturaLibre(Receips r) {
        boolean res = false;
        if (r.getPropietario().equals("SA")
                || r.getPropietario().equalsIgnoreCase(DataUser.username)) {
            res = true;
        }
        return res;
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
        mnCopiarNFac = new javax.swing.JMenuItem();
        mnPropietario = new javax.swing.JMenuItem();
        center = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBuscarConsecutivo = new javax.swing.JTextField();
        btnBuscarFactura = new javax.swing.JButton();
        btnAsingGroup = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbMntFact = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtxtfDetalles = new javax.swing.JTextArea();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        lbTbMantResumen = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        blSinCuentaPres = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jdtInicio = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jdtFin = new com.toedter.calendar.JDateChooser();
        btnRefreshReceips = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lbTotalFacturas = new javax.swing.JLabel();
        lbPendientes = new javax.swing.JLabel();
        lbRegistradas = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btnCorreosOmitidos = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbSociedades = new javax.swing.JComboBox<>();
        jPanel20 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jPanel21 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbEstadoRegistro = new javax.swing.JComboBox<>();
        jPanel17 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbEstadoAsignacion = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

        menuAbrirPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf .png"))); // NOI18N
        menuAbrirPDF.setText("Abrir pdf");
        menuAbrirPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbrirPDFActionPerformed(evt);
            }
        });
        popUpTbReceips.add(menuAbrirPDF);

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

        setLayout(new java.awt.BorderLayout());

        center.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla asignaciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        center.setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(868, 40));
        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Factura #");
        jPanel18.add(jLabel3);

        txtBuscarConsecutivo.setPreferredSize(new java.awt.Dimension(150, 22));
        jPanel18.add(txtBuscarConsecutivo);

        btnBuscarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBuscarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFacturaActionPerformed(evt);
            }
        });
        jPanel18.add(btnBuscarFactura);

        jPanel11.add(jPanel18);

        btnAsingGroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        btnAsingGroup.setText("Asingnar en conjunto");
        btnAsingGroup.setToolTipText("Asignar grupalmente (excepto los asignados)");
        btnAsingGroup.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnAsingGroup.setPreferredSize(new java.awt.Dimension(180, 33));
        btnAsingGroup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAsingGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsingGroupActionPerformed(evt);
            }
        });
        jPanel11.add(btnAsingGroup);

        center.add(jPanel11, java.awt.BorderLayout.PAGE_START);

        jPanel22.setLayout(new java.awt.GridLayout(1, 0));

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

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
                false, false, false, false, false, false, true, false, false, true, true, false, false, true, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbMntFact.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbMntFact.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbMntFact.setShowHorizontalLines(true);
        tbMntFact.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbMntFact);
        if (tbMntFact.getColumnModel().getColumnCount() > 0) {
            tbMntFact.getColumnModel().getColumn(0).setPreferredWidth(300);
            tbMntFact.getColumnModel().getColumn(1).setPreferredWidth(300);
            tbMntFact.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbMntFact.getColumnModel().getColumn(4).setPreferredWidth(150);
            tbMntFact.getColumnModel().getColumn(6).setPreferredWidth(120);
            tbMntFact.getColumnModel().getColumn(7).setPreferredWidth(120);
            tbMntFact.getColumnModel().getColumn(8).setPreferredWidth(120);
            tbMntFact.getColumnModel().getColumn(11).setMinWidth(0);
            tbMntFact.getColumnModel().getColumn(11).setPreferredWidth(0);
            tbMntFact.getColumnModel().getColumn(11).setMaxWidth(0);
            tbMntFact.getColumnModel().getColumn(12).setMinWidth(0);
            tbMntFact.getColumnModel().getColumn(12).setPreferredWidth(0);
            tbMntFact.getColumnModel().getColumn(12).setMaxWidth(0);
            tbMntFact.getColumnModel().getColumn(14).setMinWidth(0);
            tbMntFact.getColumnModel().getColumn(14).setPreferredWidth(0);
            tbMntFact.getColumnModel().getColumn(14).setMaxWidth(0);
            tbMntFact.getColumnModel().getColumn(15).setMinWidth(20);
            tbMntFact.getColumnModel().getColumn(15).setPreferredWidth(20);
            tbMntFact.getColumnModel().getColumn(15).setMaxWidth(20);
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

        jPanel10.add(jScrollPane1);

        jSplitPane1.setLeftComponent(jPanel10);

        jPanel12.setPreferredSize(new java.awt.Dimension(856, 150));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel13.setPreferredSize(new java.awt.Dimension(856, 100));
        jPanel13.setLayout(new java.awt.GridLayout(1, 0));

        jtxtfDetalles.setEditable(false);
        jtxtfDetalles.setColumns(20);
        jtxtfDetalles.setRows(5);
        jScrollPane2.setViewportView(jtxtfDetalles);

        jPanel13.add(jScrollPane2);

        jPanel12.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel14.setPreferredSize(new java.awt.Dimension(856, 37));
        jPanel14.setLayout(new java.awt.GridLayout(1, 0));

        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout3.setAlignOnBaseline(true);
        jPanel15.setLayout(flowLayout3);

        lbTbMantResumen.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbTbMantResumen.setText("Filas");
        jPanel15.add(lbTbMantResumen);

        jPanel14.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 2));

        blSinCuentaPres.setBackground(new java.awt.Color(255, 51, 51));
        blSinCuentaPres.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        blSinCuentaPres.setText(" Sin Cta Presupuesto ");
        blSinCuentaPres.setToolTipText("Fue asignada sin una cuenta de presupuesto");
        blSinCuentaPres.setOpaque(true);
        jPanel16.add(blSinCuentaPres);

        jPanel14.add(jPanel16);

        jPanel12.add(jPanel14, java.awt.BorderLayout.NORTH);

        jSplitPane1.setRightComponent(jPanel12);

        jPanel22.add(jSplitPane1);

        center.add(jPanel22, java.awt.BorderLayout.CENTER);

        add(center, java.awt.BorderLayout.CENTER);

        jPanel2.setPreferredSize(new java.awt.Dimension(798, 140));
        jPanel2.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel7.setPreferredSize(new java.awt.Dimension(1125, 15));
        jPanel7.setLayout(new java.awt.GridLayout(1, 0));

        jProgressBar1.setOpaque(true);
        jProgressBar1.setStringPainted(true);
        jPanel7.add(jProgressBar1);

        jPanel2.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel1.setMinimumSize(new java.awt.Dimension(740, 20));
        jPanel1.setPreferredSize(new java.awt.Dimension(816, 35));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel1.setLayout(flowLayout1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Fecha incio");
        jPanel1.add(jLabel1);

        jdtInicio.setDateFormatString("dd-MM-yyyy");
        jdtInicio.setPreferredSize(new java.awt.Dimension(120, 22));
        jPanel1.add(jdtInicio);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Fecha final");
        jPanel1.add(jLabel2);

        jdtFin.setDateFormatString("dd-MM-yyyy");
        jdtFin.setPreferredSize(new java.awt.Dimension(120, 22));
        jdtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdtFinPropertyChange(evt);
            }
        });
        jPanel1.add(jdtFin);

        btnRefreshReceips.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-start-30.png"))); // NOI18N
        btnRefreshReceips.setToolTipText("Volver a cargar facturas");
        btnRefreshReceips.setPreferredSize(new java.awt.Dimension(36, 30));
        btnRefreshReceips.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshReceipsActionPerformed(evt);
            }
        });
        jPanel1.add(btnRefreshReceips);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 0));

        lbTotalFacturas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbTotalFacturas.setText("Total facturas");
        jPanel8.add(lbTotalFacturas);

        lbPendientes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbPendientes.setText("Pendientes");
        jPanel8.add(lbPendientes);

        lbRegistradas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbRegistradas.setText("Registradas");
        jPanel8.add(lbRegistradas);

        jPanel1.add(jPanel8);

        jPanel9.setMinimumSize(new java.awt.Dimension(200, 100));
        jPanel9.setPreferredSize(new java.awt.Dimension(200, 40));
        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0);
        flowLayout2.setAlignOnBaseline(true);
        jPanel9.setLayout(flowLayout2);

        btnCorreosOmitidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/failed (3).png"))); // NOI18N
        btnCorreosOmitidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorreosOmitidosActionPerformed(evt);
            }
        });
        jPanel9.add(btnCorreosOmitidos);

        jPanel1.add(jPanel9);

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(724, 70));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("CIA");
        jPanel19.add(jLabel4);

        cmbSociedades.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar todas", "Cedulas desconocidas", "RYMSA 3101724817", "CILT 3101086411", "IRASA 3101119637", "KATRA 3101119531", "OPYLOG 3101466557", "TURINTEL 3101468003" }));
        cmbSociedades.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSociedadesItemStateChanged(evt);
            }
        });
        jPanel19.add(cmbSociedades);

        jPanel6.add(jPanel19);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Proveedor");
        jPanel20.add(jLabel5);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorItemStateChanged(evt);
            }
        });
        jPanel20.add(cmbProveedor);

        jPanel6.add(jPanel20);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Estado Exactus");
        jPanel21.add(jLabel6);

        cmbEstadoRegistro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Pendientes", "Registradas" }));
        cmbEstadoRegistro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoRegistroItemStateChanged(evt);
            }
        });
        jPanel21.add(cmbEstadoRegistro);

        jPanel6.add(jPanel21);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Estado asignación   ");
        jPanel17.add(jLabel7);

        cmbEstadoAsignacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Asignadas", "Sin asignar" }));
        cmbEstadoAsignacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoAsignacionItemStateChanged(evt);
            }
        });
        jPanel17.add(cmbEstadoAsignacion);

        jPanel6.add(jPanel17);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnRefresh.setToolTipText("Refrescar filtros");
        btnRefresh.setPreferredSize(new java.awt.Dimension(36, 25));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel6.add(btnRefresh);

        jPanel2.add(jPanel6, java.awt.BorderLayout.SOUTH);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(798, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1155, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 474, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 474, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jdtFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdtFinPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().contains("date")) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    java.util.Date inicio = jdtInicio.getDate();
                    java.util.Date fin = jdtFin.getDate();
                    if (inicio != null && fin != null) {
                        loadReceipts();
                        loadingInfo = true;
                        cmbProveedor.removeAllItems();
                        loadCmbProveedores();
                        loadingInfo = false;
                    }

                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }//GEN-LAST:event_jdtFinPropertyChange

    private void cmbSociedadesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSociedadesItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {

            try {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        java.util.Date inicio = jdtInicio.getDate();
                        java.util.Date fin = jdtFin.getDate();
                        if (inicio != null && fin != null) {
                            loadingInfo = true;
                            cmbProveedor.setSelectedIndex(0);
                            loadingInfo = false;
                            loadReceipts();
                            loadingInfo = true;
                            cmbProveedor.removeAllItems();
                            loadCmbProveedores();
                            loadingInfo = false;
                        }

                    }
                };
                Thread t = new Thread(r);
                t.start();
            } catch (Exception e) {
                System.err.println("view.MantenimientoCuentaFact.cmbSociedadesItemStateChanged() error " + e.getMessage());
            }
        }
    }//GEN-LAST:event_cmbSociedadesItemStateChanged

    private void cmbProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncReceips();

        }
    }//GEN-LAST:event_cmbProveedorItemStateChanged
    private void loadAsyncReceips() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                java.util.Date inicio = jdtInicio.getDate();
                java.util.Date fin = jdtFin.getDate();
                if (inicio != null && fin != null) {
                    loadReceipts();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    private void cmbEstadoRegistroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoRegistroItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncReceips();
        }
    }//GEN-LAST:event_cmbEstadoRegistroItemStateChanged

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        try {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    java.util.Date inicio = jdtInicio.getDate();
                    java.util.Date fin = jdtFin.getDate();
                    if (inicio != null && fin != null) {
                        loadingInfo = true;
                        cmbEstadoAsignacion.setSelectedIndex(0);
                        txtBuscarConsecutivo.setText("");
                        cmbEstadoRegistro.setSelectedIndex(0);
                        cmbSociedades.setSelectedIndex(0);
                        cmbProveedor.setSelectedIndex(0);
                        loadingInfo = false;
                        loadReceipts();
                        loadingInfo = true;
                        cmbProveedor.removeAllItems();
                        loadCmbProveedores();
                        loadingInfo = false;
                    }

                }

            };
            Thread t = new Thread(r);
            t.start();

        } catch (Exception e) {
            System.err.println("view.MantenimientoCuentaFact.btnRefreshActionPerformed() error " + e.getMessage());
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBuscarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarFacturaActionPerformed
        // TODO add your handling code here:
        buscarFacturas();
    }//GEN-LAST:event_btnBuscarFacturaActionPerformed

    private void btnAsingGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsingGroupActionPerformed
        // TODO add your handling code here:
        try {
            ArrayList<Receips> lista = new ArrayList<>();
            receipList.forEach(e -> {
                if (e.getCuentaPresupuesto().equalsIgnoreCase("")) {
                    lista.add(e);
                }
            });
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hay facturas que asignar");
            } else {
                OpcionesSeleccion op = new OpcionesSeleccion(lista, departamentosPropios);
                op.setVisible(true);
                loadAsyncReceips();
            }
        } catch (Exception e) {
            System.err.println("view.MantenimientoFacturaElectronica.btnAsingGroupActionPerformed() error " + e.getMessage());
        }
    }//GEN-LAST:event_btnAsingGroupActionPerformed

    private void cmbEstadoAsignacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoAsignacionItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    java.util.Date inicio = jdtInicio.getDate();
                    java.util.Date fin = jdtFin.getDate();
                    if (inicio != null && fin != null) {
                        loadReceipts();
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }//GEN-LAST:event_cmbEstadoAsignacionItemStateChanged

    private void btnRefreshReceipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshReceipsActionPerformed
        // TODO add your handling code here:
        Runnable r = new Runnable() {
            @Override
            public void run() {
                java.util.Date inicio = jdtInicio.getDate();
                java.util.Date fin = jdtFin.getDate();
                if (inicio != null && fin != null) {
                    loadReceipts();
                    loadingInfo = true;
                    cmbProveedor.removeAllItems();
                    loadCmbProveedores();
                    loadingInfo = false;
                }

            }
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_btnRefreshReceipsActionPerformed

    private void menuAbrirPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbrirPDFActionPerformed
        // TODO add your handling code here:
        openTbReceipPDF();
    }//GEN-LAST:event_menuAbrirPDFActionPerformed

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

    private void mnCopiarNFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCopiarNFacActionPerformed
        // TODO add your handling code here:
        try {

            int row = this.tbMntFact.getSelectedRow();

            if (row > -1) {

                String consecutivo = this.tbMntFact.getValueAt(row, 2).toString();
                StringSelection selection = new StringSelection(consecutivo);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                System.out.println("consecutivo copiado: " + consecutivo);
            }
        } catch (Exception e) {
            System.out.println("view.ReceiptMaintenance.mnCopiarNFacActionPerformed() error " + e.getMessage());
        }
    }//GEN-LAST:event_mnCopiarNFacActionPerformed

    private void mnPropietarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPropietarioActionPerformed
        // TODO add your handling code here:
        try {

            int row = this.tbMntFact.getSelectedRow();

            if (row > -1) {

                String clave = this.tbMntFact.getValueAt(row, 16).toString();
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

    /**
     * this method sets the background color o a given jtable cell given the
     * value of the left cell -1 rgb(60, 63, 65), 0 red and >0 green
     */
    private void addCellColorCode(JTable table, int column) {
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

    /**
     * this method checks the xml files associated to the receip register, so it
     * can load the info details to it. then checks the asientos list to update
     * db asientos info
     */
    private void checkAsientos() {

        ArrayList<Receips> lista = new ArrayList<>();
        for (Receips r : receipList) {

            String asiento = AsientoFactura.getAsientoPorCosecutivo(r, listaAsientos, "FAC");
            if (!r.getExactus().equalsIgnoreCase(asiento)
                    && !asiento.equals("") && r.getExactus().equals("")) {

                lista.add(r);
                r.setExactus(asiento);

            }

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

    private void buscarFacturas() {
        String consecutivo = this.txtBuscarConsecutivo.getText();
        if (consecutivo.isEmpty()) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    java.util.Date inicio = jdtInicio.getDate();
                    java.util.Date fin = jdtFin.getDate();
                    if (inicio != null && fin != null) {
                        loadReceipts();
                    }
                }
            };
            Thread t = new Thread(r);
            t.start();
        } else {
            ArrayList<Receips> lista = Receips.getLisReceipsFromNumeroConsecutivo(receipList, consecutivo);
            loadingInfo = true;
            limpiarTabla(tbMntFact);
            DefaultTableModel model = (DefaultTableModel) this.tbMntFact.getModel();
            lista.forEach(e -> {
                addRowTbMant(model, e);
            });
            this.lbTbMantResumen.setText("Filas: " + this.tbMntFact.getRowCount());
            loadingInfo = false;
        }
    }

    private void loadReceipts() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.jdtFin.setEnabled(false);
        this.btnRefreshReceips.setEnabled(false);
        try {
            loadingInfo = true;
            this.jProgressBar1.setVisible(true);
            this.jProgressBar1.setString("Cargando...");
            this.jProgressBar1.setMaximum(100);
            this.jProgressBar1.setValue(50);
            limpiarTabla(tbMntFact);
            java.util.Date inicio = this.jdtInicio.getDate();
            java.util.Date fin = this.jdtFin.getDate();
            String cia = this.cmbSociedades.getSelectedItem().toString();
            Sociedad soc = Sociedad.obtenerSociedadPorDesc(cia, sociedades);
            String prov = cmbProveedor.getSelectedItem().toString();
            String provCed = "";
            if (!prov.equals("Todos")) {
                provCed = prov.substring(prov.indexOf("->") + 2, prov.length());
            }
            int registrado = cmbEstadoRegistro.getSelectedIndex();
            int asignadas = cmbEstadoAsignacion.getSelectedIndex();
            //cmbProveedor.removeAllItems();
            boolean ciaDesconocida = cia.equalsIgnoreCase("Cedulas desconocidas") ? true : false;
            receipList = data.CrudFacturaElectronica.obtenerFacturaElectronicaView(
                    soc == null ? "" : soc.getCedula(),
                    provCed, registrado, asignadas, inicio, fin, ciaDesconocida);
            data.CrudCorreoExcluidoFE ce = new data.CrudCorreoExcluidoFE();
            listaCorreosOmitidos = ce.obtenerCorreosExclidosPorFecha(inicio, fin);
            listaAsientos = crAsientos.obtenerAsientos(inicio, fin);
            checkAsientos();
            showReceiptsStats();
            this.lbTbMantResumen.setText("Filas: " + tbMntFact.getRowCount());
            loadingInfo = false;
        } catch (Exception e) {
            System.err.println("view.MantenimientoCuentaFact.loadReceipts() error " + e.getMessage());
        }

        this.jdtFin.setEnabled(true);
        this.btnRefreshReceips.setEnabled(true);
        setDateChooserLook();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }

    private void showReceiptsStats() {
        this.registradas = 0;
        this.jProgressBar1.setVisible(true);
        this.jProgressBar1.setString("Cargando...");
        this.jProgressBar1.setValue(0);
        this.jProgressBar1.setMaximum(receipList.size());

        DefaultTableModel model = (DefaultTableModel) this.tbMntFact.getModel();
        receipList.forEach(e -> {
            this.registradas += e.getExactus().isEmpty() ? 0 : 1;
            addRowTbMant(model, e);
            int value = jProgressBar1.getValue() + 1;
            jProgressBar1.setValue(value);
        });
        lbTotalFacturas.setText("Total facturas: " + receipList.size());
        lbPendientes.setText("Pendientes: " + (receipList.size() - registradas));
        lbRegistradas.setText("Registradas: " + registradas);
        if (this.listaCorreosOmitidos.size() > 0) {
            this.btnCorreosOmitidos.setVisible(true);
            this.btnCorreosOmitidos.setText("" + this.listaCorreosOmitidos.size());
        } else {
            this.btnCorreosOmitidos.setVisible(false);
        }
        addCellColorCode(tbMntFact, 15);
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
    }

    private void loadCmbProveedores() {
        try {
            ArrayList<String> list = new ArrayList<>();
            cmbProveedor.addItem("Todos");
            this.receipList.forEach(e -> {
                String prov = e.getEmisor().getNombre() + "->" + e.getEmisor().getIdentificacion();
                if (!list.contains(prov)) {
                    list.add(prov);
                }
            });
            Collections.sort(list);
            list.forEach(e -> {
                cmbProveedor.addItem(e);
            });
        } catch (Exception e) {
            System.err.println("view.MantenimientoCuentaFact.loadCmbProveedores() error " + e.getMessage());
        }
    }

    private void addRowTbMant(DefaultTableModel model, Receips e) {

        Departamento d = Departamento.getDepartamentoPorStringId(listaDepartamentos, e.getIdDepartamento());
        String descripcionDep = "";
        if (d != null) {
            descripcionDep = d.getDescripcion();
        }
        String monto = logic.AppStaticValues.numberFormater.format(e.getResumenFactura().getTotalComprobante());
        model.addRow(new Object[]{
            e.getCia().equals("")?e.getReceptor().getNombre():e.getCia(),
            e.getEmisor().getNombre(),
            e.getNumeroConsecutivo(),
            AppStaticValues.dateFormat.format(e.getFechaEmision()),
            monto,
            e.getResumenFactura().getCodigoMoneda().getCodigoMoneda(),
            descripcionDep,
            e.getCuentaGeneral(),
            e.getCuentaPresupuesto(),
            e.getAprobadoDirector() == 1 ? true : false,
            e.getRechazado() == 1 ? true : false,
            e.getEstado(),
            e.getExactus(),
            e.esCajaChica() == 1 ? true : false,
            e.getSubTipoAsiento(),
            "",
            e.getClave()
        });

    }

    private void setUpProgressBar(String message, int maxvalue) {
        this.jProgressBar1.setMaximum(maxvalue);
        this.jProgressBar1.setVisible(true);
        this.jProgressBar1.setValue(0);
        this.jProgressBar1.setString(message);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel blSinCuentaPres;
    private javax.swing.JButton btnAsingGroup;
    private javax.swing.JButton btnBuscarFactura;
    private javax.swing.JButton btnCorreosOmitidos;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRefreshReceips;
    private javax.swing.JPanel center;
    private javax.swing.JComboBox<String> cmbEstadoAsignacion;
    private javax.swing.JComboBox<String> cmbEstadoRegistro;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JComboBox<String> cmbSociedades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JSplitPane jSplitPane1;
    private com.toedter.calendar.JDateChooser jdtFin;
    private com.toedter.calendar.JDateChooser jdtInicio;
    private javax.swing.JTextArea jtxtfDetalles;
    private javax.swing.JLabel lbPendientes;
    private javax.swing.JLabel lbRegistradas;
    private javax.swing.JLabel lbTbMantResumen;
    private javax.swing.JLabel lbTotalFacturas;
    private javax.swing.JMenuItem menuAbrirPDF;
    private javax.swing.JMenuItem mnCopiarNFac;
    private javax.swing.JMenuItem mnPropietario;
    private javax.swing.JPopupMenu popUpTbReceips;
    private javax.swing.JTable tbMntFact;
    private javax.swing.JTextField txtBuscarConsecutivo;
    // End of variables declaration//GEN-END:variables
}

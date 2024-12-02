/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import data.CRUDHistoricoCP;
import data.DataUser;
import entitys.AbonoSugerido;
import entitys.DetallesHistoricoPagos;
import entitys.PagoProv;
import entitys.HistoricoCP;
import entitys.Presupuesto;
import entitys.Proveedor;
import entitys.SumasTransacciones;
import entitys.TipoCambio;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import logic.AppStaticValues;
import services.SimpleExcelWriter;
import view.util.CustomMessages;

/**
 *
 * @author eobregon
 */
public class MantenimientoPagos extends javax.swing.JPanel {

    /**
     * Creates new form MantenimientoPagos
     */
    ArrayList<entitys.CompraProv> listaCompras;
    ArrayList<Presupuesto> listaPresupuesto;
    ArrayList<entitys.HistoricoCP> listaHistoricoCP;
    ArrayList<entitys.PagoProv> listaPagos;
    CRUDHistoricoCP chcp;
    data.CrudAbonoSugerido crab;
    private boolean loadingInfo = false;
    private int currentWeek;
    private double presupColones = 0;
    private TipoCambio tipoc;
    data.CrudProveedor crp;
    double saldoTotal = 0, saldoVencido, saldoSinVencer, saldoAvencer7dias;
    int facturasVen = 0;
    public boolean moduleOnUse = true;

    public MantenimientoPagos(ArrayList<Presupuesto> listaPresupuesto) {
        initComponents();
        this.chcp = new CRUDHistoricoCP();
        this.listaPresupuesto = listaPresupuesto;
        this.crab = new data.CrudAbonoSugerido();
        this.crp = new data.CrudProveedor();
        prepareGUI();
    }

    private void prepareGUI() {
        loadInfo();
        setView();

    }

    /**
     * this method check if there is internet connection, so the program can
     * keep tabs on it, and therefore warns the user if there is not, with the
     * change of the connection icon on lbCheckConnection
     */
    private void checkAbonosScheduler() {
        Timer databaseSyncTimer = new Timer();
        databaseSyncTimer.scheduleAtFixedRate(
                new TimerTask() {
            public void run() {
                if (!moduleOnUse) {
                    return;
                }
                if (tipoc == null) {
                    prepareTc();
                }
                // Example java.util.Date, replace with your actual Date
                java.util.Date utilDate = new java.util.Date(); // Current date

                // Convert java.util.Date to LocalDate
                LocalDate givenDate = utilDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                // Get the start of the week (Monday)
                LocalDate startOfWeek = givenDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                // Get the end of the week (Sunday)
                LocalDate endOfWeek = givenDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                // Convert LocalDate back to java.util.Date
                Date startOfWeekDate = convertToDateViaInstant(startOfWeek);
                Date endOfWeekDate = convertToDateViaInstant(endOfWeek);
                //JOptionPane.showMessageDialog(null,"fecha "+date);
                double colones = crab.getAbonosSum(startOfWeekDate, endOfWeekDate);
                //data.CRUDHistoricoCP crdhis = new data.CRUDHistoricoCP();
                //ArrayList<entitys.SumasTransacciones> listaContado = crdhis.obtenerSumaHistoricoCPContado_View(startOfWeekDate, endOfWeekDate);
               data.CRUDAbonoSugeridoContado crdhis = new data.CRUDAbonoSugeridoContado();
                ArrayList<entitys.SumasTransacciones> listaContado = crdhis.obtenerSumaAbonoHistoricoCPContado_View();

                double contado = 0;
                for (SumasTransacciones e : listaContado) {
                    contado += e.getSumSaldoColones();
                }
                double restCol = presupColones - colones - contado;

                if (restCol <= 0) {
                    lbRestPreCol.setForeground(new Color(255, 0, 0));
                } else {
                    lbRestPreCol.setForeground(new Color(187, 187, 187));
                }
                if (tipoc == null) {
                    lbRestPreCol.setText("Restante: ₡" + logic.AppStaticValues.numberFormater.format(restCol)
                            + " No disponible el tipo de cambio");
                } else {
                    lbRestPreCol.setText("Restante: ₡" + logic.AppStaticValues.numberFormater.format(restCol)
                            + "   Compra: ₡" + logic.AppStaticValues.numberFormater.format(tipoc.getCompra())
                            + " Venta: ₡" + logic.AppStaticValues.numberFormater.format(tipoc.getVenta()));
                }

                lbresumenAbon.setText("Total abonos: ₡"
                        + logic.AppStaticValues.numberFormater.format(colones));
            }
        }, 0, 6000);
    }

    public Date convertToDateViaInstant(LocalDate localDate) {
        return java.util.Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void loadInfo() {
        this.loadingInfo = true;
        //this.proveedores = crp.getProveedores();
        cmbProveedor.removeAllItems();
        listaHistoricoCP = chcp.getcp_cilt_rymsaPlusAbonoSugerido(
                new java.util.Date(), new java.util.Date(),
                "", "", "", "", "", "");//this.listaHistoricoCP =chcp.obtenerHistoricoCPPorFechas(new java.util.Date(), new java.util.Date());
        loadTbHistoricoCP(listaHistoricoCP);
        Collections.sort(listaHistoricoCP, Comparator.comparing(HistoricoCP::getNOMBRE));
        loadCmbProveedor(listaHistoricoCP);
        lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount() + "\t |Facturas vencidas " + facturasVen);
        addCellColorCode(tbHistoricoCP, 17);
        prepareTc();
        loadingInfo = false;
    }

    private void prepareTc() {
        data.CRUDTipoCambio tc = new data.CRUDTipoCambio();
        ArrayList<TipoCambio> lista = tc.getTipoCambioPorFechas(new Date(), new Date());
        if (!lista.isEmpty()) {
            this.tipoc = lista.get(0);
        }
    }

    private void loadCmbProveedor(ArrayList<entitys.HistoricoCP> lista) {
        ArrayList<String> listaN = new ArrayList<>();
        this.cmbProveedor.removeAllItems();
        this.cmbProveedor.addItem("Todos");
        Collections.sort(lista, Comparator.comparing(entitys.HistoricoCP::getNOMBRE));
        lista.forEach(e -> {
            String prov = e.getNOMBRE().replace("-", "") + "-" + e.getPROVEEDOR();
            if (!listaN.contains(prov)) {

                listaN.add(prov);
            }
        });
        listaN.forEach(e -> {
            this.cmbProveedor.addItem(e);
        });
    }

    private void setUpProgessBar(String message, int min, int max, int value) {

        this.jProgressBar1.setMinimum(min);
        this.jProgressBar1.setMaximum(max);
        this.jProgressBar1.setString(message);
        this.jProgressBar1.setVisible(true);
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
            double ttlAbSinap = 0;
            double ttTodosSaldos = 0;
            this.txtDetMontSel.setText("");
            for (int row : selectedRows) {
                String doc = tbHistoricoCP.getValueAt(row, 6).toString();
                String cia = tbHistoricoCP.getValueAt(row, 0).toString();
                String numProv = tbHistoricoCP.getValueAt(row, 2).toString();
                HistoricoCP h = HistoricoCP.obtenerHistoricoPorDoc(doc, cia, numProv, listaHistoricoCP);
                if (h.getMONEDA().equals("CRC")) {
                    sumSaldCol += h.getSaldo();
                    sumMontOriCol += h.getMONTO();

                } else {
                    sumSaldDol += h.getSaldo();
                    sumMontOriDol += h.getMONTO();
                    sumMontOriDolCol += h.getMonto_colones();
                }
                sumMontCol += h.getMonto_colones();
                ttTodosSaldos += h.getSaldo_colones();
                for (AbonoSugerido s : h.getSugeridos()) {
                    if (s.getSemana() == currentWeek && s.getAprobado() == 1) {
                        ttlAb += s.getMonto_Colones();
                    } else if (s.getSemana() == currentWeek && s.getAprobado() == 0) {
                        ttlAbSinap += s.getMonto_Colones();
                    }
                }
            }
            this.txtDetMontSel.setText(
                    "Sm montos: ₡" + AppStaticValues.numberFormater.format(sumMontOriCol)
                    + "\tSm saldos: ₡" + AppStaticValues.numberFormater.format(sumSaldCol)
                    + "\nSm montos: $" + AppStaticValues.numberFormater.format(sumMontOriDol)
                    + "\tSm saldos: $" + AppStaticValues.numberFormater.format(sumSaldDol)
                    + "\nSm montos de $ a ₡" + AppStaticValues.numberFormater.format(sumMontOriDolCol)
                    + "\nSm todos los montos: ₡" + AppStaticValues.numberFormater.format(sumMontCol)
                    + "\nSm todos los saldos: ₡" + AppStaticValues.numberFormater.format(ttTodosSaldos));

            String resumen = "Total aprobados: ₡"
                    + AppStaticValues.numberFormater.format(ttlAb)
                    + "\nTotal sin aprobar: ₡"
                    + AppStaticValues.numberFormater.format(ttlAbSinap)
                    + "\nTotal saldos - abonos aprobados: ₡"
                    + AppStaticValues.numberFormater.format(ttTodosSaldos - ttlAb);
            this.txtDetAbon.setText(resumen);

        }
    }

    private double getSaldoRestante(HistoricoCP h) {
        double saldoRestante = 0;

        for (AbonoSugerido s : h.getSugeridos()) {
            saldoRestante += s.getSaldo_Restante();
        }

        return saldoRestante;
    }

    private void loadTbHistoricoCP(ArrayList<entitys.HistoricoCP> lista) {
        setUpProgessBar("Cargando información...", 0, lista.size(), 0);
        DefaultTableModel model = (DefaultTableModel) this.tbHistoricoCP.getModel();
        this.saldoSinVencer = 0;
        this.saldoTotal = 0;
        this.saldoVencido = 0;
        this.facturasVen = 0;
        Collections.sort(lista, Comparator.comparing(entitys.HistoricoCP::getFECHA_DOCUMENTO));
        lista.forEach(e -> {
            int semana = logic.CommonDateFunctions.getWeekOfTheYear(e.getFECHA_DOCUMENTO());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(e.getFECHA_DOCUMENTO());
            int year = calendar.get(Calendar.YEAR);
            facturasVen += !e.getESTAD_MORA().equalsIgnoreCase("SIN VENCER") ? 1 : 0;
            double saldoReal = e.getSaldo_colones();
            this.saldoSinVencer += e.getESTAD_MORA().equalsIgnoreCase("SIN VENCER") ? saldoReal : 0;
            this.saldoVencido += e.getESTAD_MORA().equalsIgnoreCase("SIN VENCER") ? 0 : saldoReal;
            this.saldoTotal += saldoReal;
            String sing = e.getMONEDA().equals("CRC") ? "₡" : "$";
            AbonoSugerido ab = AbonoSugerido.obtenerAbonoMismaSemana(e.getDOCUMENTO(), currentWeek, e.getSugeridos());

            Double saldoAbonos = e.getMONTO() - entitys.AbonoSugerido.getSaldoFinal(e.getSugeridos());
            Presupuesto p = Presupuesto.getPresupuesto(e.getCTA_PRESUPUESTO(), listaPresupuesto);
            String cta = p == null ? "ND" : p.getCONCEPATOADETALLE();
            model.addRow(new Object[]{
                e.getCIA(),
                e.getTIPOPROV(),
                e.getPROVEEDOR(),
                e.getNOMBRE(),
                e.getFECHA_DOCUMENTO(),//logic.AppStaticValues.dateFormat.format(e.getFECHA_DOCUMENTO()),
                year + "-" + semana,
                e.getDOCUMENTO(),
                e.getESTAD_MORA(),
                e.getCTA_PRESUPUESTO(),
                cta,//"descripcion cuenta presupuesto",
                e.getMONEDA(),
                sing + logic.AppStaticValues.numberFormater.format(e.getMONTO()),
                sing + logic.AppStaticValues.numberFormater.format(e.getSaldo()),
                sing + logic.AppStaticValues.numberFormater.format(saldoAbonos),
                "₡" + logic.AppStaticValues.numberFormater.format(e.getMonto_colones()),
                ab == null ? "" : sing + logic.AppStaticValues.numberFormater.format(ab.getAbono()),
                ab == null ? false : ab.getAprobado() == 1,
                e.getCONTA_CRED(),
                "",
                ab == null ? "" : ab.getId(),
                e.getComentario(),
                e.getCredito_proveedor()
            });

            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
        });
        calcVence7dias();
        this.txtProvDet.setText("Sld total: ₡" + AppStaticValues.numberFormater.format(this.saldoTotal)
                + "\nSld vencido: ₡" + AppStaticValues.numberFormater.format(this.saldoVencido)
                + "\nSld sin ven: ₡" + AppStaticValues.numberFormater.format(this.saldoSinVencer)
                + "\nVence 7 d: ₡" + AppStaticValues.numberFormater.format(this.saldoAvencer7dias)
        );

        addCellColorCode(tbHistoricoCP, 17);
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);

    }

    private void calcVence7dias() {
        this.saldoAvencer7dias = 0;
        java.util.Date originalDate = new java.util.Date();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(originalDate);
        calendar.add(java.util.Calendar.DAY_OF_YEAR, -1);
        Date initDate = calendar.getTime();
        calendar.setTime(originalDate);
        calendar.add(java.util.Calendar.DAY_OF_YEAR, 8);
        Date limitDate = calendar.getTime();
        System.out.println("view.MantenimientoPagos.calcVence7dias() desde " + initDate.toString() + " hasta " + limitDate.toString());

        for (HistoricoCP h : listaHistoricoCP) {
            if (h.getFECHA_VENCE().after(initDate) && h.getFECHA_VENCE().before(limitDate)) {
                this.saldoAvencer7dias += h.getSaldo_colones();
            }
        }
    }

    private ArrayList<entitys.HistoricoCP> prepareTbToImportToExcel(ArrayList<entitys.HistoricoCP> lista) {
        setUpProgessBar("Preparando información...", 0, lista.size(), 0);
        DefaultTableModel model = (DefaultTableModel) this.tbHistoricoCP.getModel();
        ArrayList<entitys.HistoricoCP> result = new ArrayList<>();
        lista.forEach(e -> {
            int semana = logic.CommonDateFunctions.getWeekOfTheYear(e.getFECHA_DOCUMENTO());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(e.getFECHA_DOCUMENTO());
            int year = calendar.get(Calendar.YEAR);
            String sing = e.getMONEDA().equals("CRC") ? "₡" : "$";
            AbonoSugerido ab = AbonoSugerido.obtenerAbonoMismaSemana(e.getDOCUMENTO(), currentWeek, e.getSugeridos());
            Double saldoAbonos = e.getMONTO() - entitys.AbonoSugerido.getSaldoFinal(e.getSugeridos());
            Presupuesto p = Presupuesto.getPresupuesto(e.getCTA_PRESUPUESTO(), listaPresupuesto);
            String cta = p == null ? "ND" : p.getCONCEPATOADETALLE();
            if (ab != null && ab.getAprobado() == 1) {
                model.addRow(new Object[]{
                    e.getCIA(),
                    e.getTIPOPROV(),
                    e.getPROVEEDOR(),
                    e.getNOMBRE(),
                    e.getFECHA_DOCUMENTO(),
                    year + "-" + semana,
                    e.getDOCUMENTO(),
                    e.getESTAD_MORA(),
                    e.getCTA_PRESUPUESTO(),
                    cta,//"descripcion cuenta presupuesto",
                    e.getMONEDA(),
                    e.getMONTO(),//sing + logic.AppStaticValues.numberFormater.format(e.getMONTO()),
                    e.getSaldo(),//sing + logic.AppStaticValues.numberFormater.format(e.getSaldo()),
                    saldoAbonos,//sing + logic.AppStaticValues.numberFormater.format(saldoAbonos),
                    e.getMonto_colones(),//"₡" + logic.AppStaticValues.numberFormater.format(e.getMonto_colones()),
                    ab == null ? "" : ab.getAbono(),//sing + logic.AppStaticValues.numberFormater.format(ab.getAbono()),
                    ab == null ? false : ab.getAprobado() == 1,
                    e.getCONTA_CRED(),
                    "",
                    ab == null ? "" : ab.getId(),
                    e.getComentario()

                });
                result.add(e);
            }

            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
        });
        addCellColorCode(tbHistoricoCP, 17);
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        return result;
    }

    private void setView() {
        java.util.Date date = new java.util.Date();
        this.currentWeek = logic.CommonDateFunctions.getWeekOfTheYear(date);
        this.lbCurrentWeek.setText("Semana " + this.currentWeek);

        tbHistoricoCP.getColumnModel().getColumn(10).setCellRenderer(AppStaticValues.rightRenderer);
        tbHistoricoCP.getColumnModel().getColumn(11).setCellRenderer(AppStaticValues.rightRenderer);
        tbHistoricoCP.getColumnModel().getColumn(12).setCellRenderer(AppStaticValues.rightRenderer);

        tbHistoricoCP.getColumnModel().getColumn(13).setCellRenderer(AppStaticValues.rightRenderer);
        tbHistoricoCP.getColumnModel().getColumn(14).setCellRenderer(AppStaticValues.rightRenderer);
        //tbHistoricoCP.getColumnModel().getColumn(15).setCellRenderer(AppStaticValues.rightRenderer);
        tbHistoricoCP.getColumnModel().getColumn(17).setCellRenderer(AppStaticValues.rightRenderer);
        this.txtBuscarProvedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRegPorNomProv();
            }
        });
        setPresupListener();
        setTbHistoricoListeners();
        checkAbonosScheduler();
    }

    private void buscarRegPorNomProv() {
        loadingInfo = true;
        String nombreProv = txtBuscarProvedor.getText().toUpperCase();
        if (nombreProv.equals("")) {
            createQuery();
        } else {
            view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);
            ArrayList<entitys.HistoricoCP> lista = HistoricoCP.getListaFitPorNombreProv(nombreProv, listaHistoricoCP);
            loadTbHistoricoCP(lista);
            lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount());

        }
        loadingInfo = false;
    }

    /**
     * this method sets all event listeners for the JTable tbHistoricoCP
     */
    private void setTbHistoricoListeners() {
        this.tbHistoricoCP.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // Your code here to handle table content change event
                if (!loadingInfo) {

                    int column = tbHistoricoCP.getSelectedColumn();
                    int row = tbHistoricoCP.getSelectedRow();
                    if (column == 15) {
                        validarAgrActAbono();
                    } else if (column == 16) {
                        actAbonoApr();
                    }

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
        this.tbHistoricoCP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if it's a double click event
                JTable table = (JTable) e.getSource();
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (e.getClickCount() == 2 && table.getSelectedRow() > -1) {
                    //JOptionPane.showMessageDialog(null, "fila seleccionada "+row);

                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tbHistoricoCP.rowAtPoint(e.getPoint());
                    tbHistoricoCP.addRowSelectionInterval(row, row);
                    jPopupMenu1.show(tbHistoricoCP, e.getX(), e.getY());
                }
            }
        });

    }

    /**
     * this method saves the AbonoSugerido registers to the database
     */
    private void updateAbono(AbonoSugerido ab, HistoricoCP h, int row) {
        if (ab != null) {
            entitys.AbonoSugerido newab = checkAmountsGetAbono(h);
            if (newab != null) {
                newab.setId(ab.getId());
                newab.setFecha_Creacion(ab.getFecha_Creacion());
                if (ab.getMoneda().equals("CRC")) {
                    newab.setMonto_Colones(newab.getAbono());
                } else {
                    newab.setMonto_Colones(newab.getAbono() * this.tipoc.getCompra());
                }
                boolean res = crab.actualizarAbonoSugerido(newab);
                if (res) {

                    ab = crab.getAbonoSugeridos(currentWeek, ab.getDocumento(), ab.getNumero_Proveedor(), ab.getCIA());
                    AbonoSugerido a = AbonoSugerido.obtenerAbonoPorId(ab.getId(), h.getSugeridos());
                    h.getSugeridos().remove(a);
                    h.getSugeridos().add(ab);
                    refreshTB();
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al actializar el abono al documento " + ab.getDocumento());
                }
            }
        }
    }

    /**
     * this method checks and validates the typed information to be save to the
     * database
     */
    private void validarAgrActAbono() {
        int row = tbHistoricoCP.getSelectedRow();
        String doc = tbHistoricoCP.getValueAt(row, 6).toString();
        String cia = tbHistoricoCP.getValueAt(row, 0).toString();
        String numProv = tbHistoricoCP.getValueAt(row, 2).toString();
        HistoricoCP h = HistoricoCP.obtenerHistoricoPorDoc(doc, cia, numProv, listaHistoricoCP);
        boolean week = AbonoSugerido.tieneAbonoMismaSemana(doc, this.currentWeek, h.getSugeridos());
        //AbonoSugerido ab = AbonoSugerido.obtenerAbonoMismaSemana(doc, currentWeek, h.getSugeridos());
        AbonoSugerido ab = crab.getAbonoSugeridos(currentWeek, doc, numProv, cia);
        if (week) {
            String[] options = {"Actualizar", "Cancelar"};
            int choice = JOptionPane.showOptionDialog(null, "Ya tiene un abono para esta semana "
                    + "\nFavor escoja", "Escoja una opción",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                //actualizar 
                Double saldoAbonos = h.getMONTO() - (entitys.AbonoSugerido.getSaldoFinal(h.getSugeridos()));
                if (saldoAbonos >= 0) {
                    //actualizar
                    updateAbono(ab, h, row);
                } else if (saldoAbonos < 0) {
                    String[] options2 = {"Continuar", "Cancelar"};
                    choice = JOptionPane.showOptionDialog(null, "El total de abonos es mayor al saldo, desea continuar? "
                            + "\nFavor escoja", "Escoja una opción",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options2, options2[0]);

                    if (choice == 0) {
                        updateAbono(ab, h, row);
                    } else {
                        refreshTB();
                    }
                } else {
                    refreshTB();
                    JOptionPane.showMessageDialog(null, "La suma de abonos es igual o mayor a la totalidad del monto");
                }

            }
            refreshTB();
            return;
        }
        validarParaAgregarAbono(h, ab, row);

    }

    private void actAbonoApr() {
        this.jProgressBar1.setString("Guardadado cambios...");
        CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        int row = tbHistoricoCP.getSelectedRow();
        String doc = tbHistoricoCP.getValueAt(row, 6).toString();
        String cia = tbHistoricoCP.getValueAt(row, 0).toString();
        String numProv = tbHistoricoCP.getValueAt(row, 2).toString();
        HistoricoCP h = HistoricoCP.obtenerHistoricoPorDoc(doc, cia, numProv, listaHistoricoCP);
        boolean week = AbonoSugerido.tieneAbonoMismaSemana(doc, this.currentWeek, h.getSugeridos());
        AbonoSugerido ab = AbonoSugerido.obtenerAbonoMismaSemana(doc, currentWeek, h.getSugeridos());

        if (ab != null) {
            int ap = ab.getAprobado();
            if (ap == 1) {
                ab.setAprobado(0);
            } else {
                ab.setAprobado(1);
            }
            boolean res = crab.actualizarAbonoSugerido(ab);
            if (res) {
                refreshTB();

            } else {
                ab.setAprobado(ap);
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al actializar el abono al documento " + ab.getDocumento());
            }
        } else {
            refreshTB();
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error, por favor asegurese de que la informacion es correcta");
        }

    }

    /**
     * this method saves a new AbonoSugerido register to the database
     */
    private void agregar(AbonoSugerido ab, HistoricoCP h) {
        ab = checkAmountsGetAbono(h);
        if (ab != null) {
            boolean res = crab.agregarAbonoSugerido(ab);
            if (res) {
                ab = crab.getAbonoSugeridos(ab.getSemana(), ab.getDocumento(), ab.getNumero_Proveedor(), ab.getCIA());
                if (ab != null) {
                    h.getSugeridos().add(ab);
                }
                refreshTB();
                //JOptionPane.showMessageDialog(null, "Se ha agregado una sugerencia de abono a la factura " + ab.getDocumento());
            } else {
                refreshTB();
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar un abono al documento " + h.getDOCUMENTO());
            }
        }
    }

    /**
     * this method checks and validates the new information to be save to the
     * database
     */
    private void validarParaAgregarAbono(HistoricoCP h, AbonoSugerido ab, int row) {
        Double saldoAbonos = h.getMONTO() - (entitys.AbonoSugerido.getSaldoFinal(h.getSugeridos()) + getAbono(row, h));
        if (saldoAbonos >= 0) {
            agregar(ab, h);
        } else if (saldoAbonos < 0) {
            String[] options = {"Continuar", "Cancelar"};
            int choice = JOptionPane.showOptionDialog(null, "El total de abonos es mayor al saldo, desea continuar? "
                    + "\nFavor escoja", "Escoja una opción",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == 0) {
                agregar(ab, h);
            } else {
                refreshTB();
            }
        } else {
            refreshTB();
            JOptionPane.showMessageDialog(null, "La suma de abonos es igual o mayor a la totalidad del monto");
        }
    }

    /**
     * this function checks if the resulting AbonoSugerido' amount are logic, so
     * ask the user if the process must continue even if the amounts are not
     * correct
     *
     * @param h is HistoricoCP object from witch the calks must be carried
     * @returns a AbonoSugerido object if the conditions are accepted, null if
     * they are not
     */
    private AbonoSugerido checkAmountsGetAbono(HistoricoCP h) {
        int row = tbHistoricoCP.getSelectedRow();
        double abono = getAbono(row, h);
        entitys.AbonoSugerido ab = new AbonoSugerido();
        double saldo = h.getSaldo();
        if (abono > -1 && abono <= saldo) {
            ab = getAbonoFromTb(ab, h, row, saldo, abono);
        } else if (abono > saldo) {
            String[] options = {"Continuar", "Cancelar"};
            int choice = JOptionPane.showOptionDialog(null, "El total de abonos es mayor al saldo, desea continuar? "
                    + "\nFavor escoja", "Escoja una opción",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                ab = getAbonoFromTb(ab, h, row, saldo, abono);
            } else {
                refreshTB();
                return null;
            }
        } else {
            refreshTB();
            JOptionPane.showMessageDialog(null,
                    "El abono no puede ser menor a cero o mayor al saldo total de " + h.getSaldo());
            return null;
        }

        return ab;
    }

    private AbonoSugerido getAbonoFromTb(entitys.AbonoSugerido ab, HistoricoCP h, int row, double saldo, double abono) {
        boolean ap = (boolean) tbHistoricoCP.getValueAt(row, 16);
        ab.setCIA(tbHistoricoCP.getValueAt(row, 0).toString());
        ab.setDocumento(tbHistoricoCP.getValueAt(row, 6).toString());
        ab.setCuenta_Presupuesto(tbHistoricoCP.getValueAt(row, 8).toString());
        ab.setFecha_Creacion(new java.util.Date());
        ab.setNumero_Proveedor(tbHistoricoCP.getValueAt(row, 2).toString());
        ab.setMonto_Original(h.getMONTO());
        ab.setSaldo_Actuual(saldo);
        ab.setAbono(abono);
        ab.setSaldo_Restante(ab.getSaldo_Actuual() - abono);
        ab.setAbono_Total(abono == ab.getSaldo_Actuual());
        ab.setMoneda(h.getMONEDA());
        ab.setUsuario(DataUser.username);
        ab.setNombre_Proveedor(h.getNOMBRE());
        ab.setFecha_documento(h.getFECHA_DOCUMENTO());
        ab.setTipo_Proveedor(h.getCONTA_CRED());
        ab.setSemana(this.currentWeek);
        ab.setDescripion_Cta_Presupuesto(tbHistoricoCP.getValueAt(row, 9).toString());
        ab.setAprobado(ap ? 1 : 0);
        ab.setMonto_Colones(abono);
        if (h.getMONEDA().equals("USD") && this.tipoc == null) {
            JOptionPane.showMessageDialog(null, "No puede hacer abonos en dolares hasta que establecer el tipo de cambio del día");
            return null;
        } else if (h.getMONEDA().equals("USD")) {
            double monto_colones = (ab.getAbono() * this.tipoc.getVenta());
            ab.setMonto_Colones(monto_colones);
        }
        return ab;
    }

    private double getAbono(int row, HistoricoCP h) {
        double abono = -1;
        try {
            String content = tbHistoricoCP.getValueAt(row, 15).toString().replace(",", "");
            content = content.replace("₡", "");
            content = content.replace("$", "");
            content.replace("$", "");
            if (content.equals("*")) {

                abono = h.getSaldo();
            } else {
                abono = Double.parseDouble(content);
            }
        } catch (Exception e) {
            System.out.println("view.MantenimientoPagos.getAbono() error " + e.getMessage());
        }
        return abono;
    }

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
                boolean v = (boolean) table.getValueAt(row, 12).toString().equals(table.getValueAt(row, 12).toString());
                if (!v) {
                    c.setBackground(Color.YELLOW);
                    c.setToolTipText("Abono pendiente");
                } else {
                    //c.setBackground(Color.green);
                    c.setBackground(new Color(70, 73, 75));
                    c.setToolTipText("Abono correcto");
                }

                return c;
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        mnAbonar = new javax.swing.JMenuItem();
        menuAbAp = new javax.swing.JMenuItem();
        mnDeleteAbono = new javax.swing.JMenuItem();
        mnObservacion = new javax.swing.JMenuItem();
        jDialog1 = new javax.swing.JDialog();
        jPanel26 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tbAbSinAp = new javax.swing.JTable();
        jPanel32 = new javax.swing.JPanel();
        btnRefAbSinAp = new javax.swing.JButton();
        btnApAbSAp = new javax.swing.JButton();
        btnDeleteAbSAp = new javax.swing.JButton();
        btnExAbSAp = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        lbInTbAbSAp = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        center = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbCia = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbMora = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbTipoProv = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        jPanel23 = new javax.swing.JPanel();
        ckResumen = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        btnRefFilt = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel24 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbHistoricoCP = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtBuscarProvedor = new javax.swing.JTextField();
        btnBuscarProv = new javax.swing.JButton();
        btnExpExc = new javax.swing.JButton();
        lbContApSinAp = new javax.swing.JLabel();
        btnPendientes = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lbTbHistoCpInfo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDetMontSel = new javax.swing.JTextArea();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtDetAbon = new javax.swing.JTextArea();
        jPanel19 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtProvDet = new javax.swing.JTextArea();
        jPanel20 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtCompras = new javax.swing.JTextArea();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtPagos = new javax.swing.JTextArea();
        north = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        lbCurrentWeek = new javax.swing.JLabel();
        lbresumenAbon = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPresupuestoCol = new javax.swing.JTextField();
        lbRestPreCol = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        west = new javax.swing.JPanel();
        south = new javax.swing.JPanel();

        mnAbonar.setText("Abonar");
        mnAbonar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnAbonarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnAbonar);

        menuAbAp.setText("Abonar y aprobar");
        menuAbAp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbApActionPerformed(evt);
            }
        });
        jPopupMenu1.add(menuAbAp);

        mnDeleteAbono.setText("Eliminar abonos");
        mnDeleteAbono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDeleteAbonoActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnDeleteAbono);

        mnObservacion.setText("Agregar observación");
        mnObservacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnObservacionActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnObservacion);

        jDialog1.setMinimumSize(new java.awt.Dimension(700, 455));
        jDialog1.setModal(true);

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla abonos sin aprobar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel26.setToolTipText("");
        jPanel26.setLayout(new java.awt.BorderLayout());

        jPanel31.setLayout(new java.awt.GridLayout(1, 0));

        tbAbSinAp.setAutoCreateRowSorter(true);
        tbAbSinAp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Sociedad", "Usuario", "Fecha de creación", "Fecha de documento", "Documento", "Proveedor #", "Proveedor", "Cuenta de presupuesto", "Descripción cuenta", "Moneda", "Monto original", "Abono", "Aprobar"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbAbSinAp.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbAbSinAp.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(tbAbSinAp);
        if (tbAbSinAp.getColumnModel().getColumnCount() > 0) {
            tbAbSinAp.getColumnModel().getColumn(0).setMinWidth(0);
            tbAbSinAp.getColumnModel().getColumn(0).setPreferredWidth(0);
            tbAbSinAp.getColumnModel().getColumn(0).setMaxWidth(0);
            tbAbSinAp.getColumnModel().getColumn(2).setMinWidth(0);
            tbAbSinAp.getColumnModel().getColumn(2).setPreferredWidth(0);
            tbAbSinAp.getColumnModel().getColumn(2).setMaxWidth(0);
            tbAbSinAp.getColumnModel().getColumn(6).setMinWidth(0);
            tbAbSinAp.getColumnModel().getColumn(6).setPreferredWidth(0);
            tbAbSinAp.getColumnModel().getColumn(6).setMaxWidth(0);
            tbAbSinAp.getColumnModel().getColumn(7).setPreferredWidth(200);
            tbAbSinAp.getColumnModel().getColumn(9).setPreferredWidth(200);
            tbAbSinAp.getColumnModel().getColumn(13).setMinWidth(0);
            tbAbSinAp.getColumnModel().getColumn(13).setPreferredWidth(0);
            tbAbSinAp.getColumnModel().getColumn(13).setMaxWidth(0);
        }

        jPanel31.add(jScrollPane7);

        jPanel26.add(jPanel31, java.awt.BorderLayout.CENTER);

        jPanel32.setPreferredSize(new java.awt.Dimension(771, 40));
        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 3));

        btnRefAbSinAp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnRefAbSinAp.setToolTipText("Refrescar");
        btnRefAbSinAp.setPreferredSize(new java.awt.Dimension(36, 30));
        btnRefAbSinAp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefAbSinApActionPerformed(evt);
            }
        });
        jPanel32.add(btnRefAbSinAp);

        btnApAbSAp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-facebook-like-30.png"))); // NOI18N
        btnApAbSAp.setToolTipText("Aprobar selección");
        btnApAbSAp.setPreferredSize(new java.awt.Dimension(36, 32));
        btnApAbSAp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApAbSApActionPerformed(evt);
            }
        });
        jPanel32.add(btnApAbSAp);

        btnDeleteAbSAp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_icon_25x25.png"))); // NOI18N
        btnDeleteAbSAp.setToolTipText("Eliminar selección");
        btnDeleteAbSAp.setPreferredSize(new java.awt.Dimension(36, 30));
        btnDeleteAbSAp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAbSApActionPerformed(evt);
            }
        });
        jPanel32.add(btnDeleteAbSAp);

        btnExAbSAp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExAbSAp.setToolTipText("Exportar a excel");
        btnExAbSAp.setPreferredSize(new java.awt.Dimension(36, 30));
        btnExAbSAp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExAbSApActionPerformed(evt);
            }
        });
        jPanel32.add(btnExAbSAp);

        jPanel26.add(jPanel32, java.awt.BorderLayout.PAGE_START);

        jPanel33.setPreferredSize(new java.awt.Dimension(771, 25));
        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbInTbAbSAp.setText("Filas: 0");
        jPanel33.add(lbInTbAbSAp);

        jPanel26.add(jPanel33, java.awt.BorderLayout.PAGE_END);

        jDialog1.getContentPane().add(jPanel26, java.awt.BorderLayout.CENTER);

        jPanel27.setPreferredSize(new java.awt.Dimension(791, 30));
        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Abonos sin aprobar");
        jPanel27.add(jLabel10);

        jDialog1.getContentPane().add(jPanel27, java.awt.BorderLayout.PAGE_START);

        jPanel28.setPreferredSize(new java.awt.Dimension(791, 10));

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel28, java.awt.BorderLayout.PAGE_END);

        jPanel29.setPreferredSize(new java.awt.Dimension(10, 435));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel29, java.awt.BorderLayout.LINE_END);

        jPanel30.setPreferredSize(new java.awt.Dimension(10, 435));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 415, Short.MAX_VALUE)
        );

        jDialog1.getContentPane().add(jPanel30, java.awt.BorderLayout.LINE_START);

        setLayout(new java.awt.BorderLayout(0, 5));

        center.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla Historico Pagos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        center.setLayout(new java.awt.BorderLayout());

        jPanel5.setPreferredSize(new java.awt.Dimension(790, 70));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel5.setLayout(flowLayout1);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("CIA");
        jPanel8.add(jLabel5);

        cmbCia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "RYMSA", "CILT" }));
        cmbCia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCiaItemStateChanged(evt);
            }
        });
        jPanel8.add(cmbCia);

        jPanel5.add(jPanel8);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Proveedor");
        jPanel7.add(jLabel3);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorItemStateChanged(evt);
            }
        });
        cmbProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbProveedorActionPerformed(evt);
            }
        });
        jPanel7.add(cmbProveedor);

        jPanel5.add(jPanel7);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Mora");
        jPanel9.add(jLabel4);

        cmbMora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Sin vencer", "0 a 30", "31 a 60", "61 a 90", "91 a 120", "MAS DE 120" }));
        cmbMora.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMoraItemStateChanged(evt);
            }
        });
        jPanel9.add(cmbMora);

        jPanel5.add(jPanel9);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Tipo Prov");
        jPanel10.add(jLabel6);

        cmbTipoProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "LOC", "CONS" }));
        cmbTipoProv.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoProvItemStateChanged(evt);
            }
        });
        jPanel10.add(cmbTipoProv);

        jPanel5.add(jPanel10);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Moneda");
        jPanel11.add(jLabel7);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "CRC", "USD" }));
        cmbMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaItemStateChanged(evt);
            }
        });
        jPanel11.add(cmbMoneda);

        jPanel5.add(jPanel11);

        jPanel23.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        ckResumen.setText("Resumen");
        ckResumen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ckResumenItemStateChanged(evt);
            }
        });
        jPanel23.add(ckResumen);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel9.setToolTipText("Muestra abonos de la semana (sin filtros)");
        jPanel23.add(jLabel9);

        jPanel5.add(jPanel23);

        btnRefFilt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refrescar2.png"))); // NOI18N
        btnRefFilt.setToolTipText("Refrescar filtros");
        btnRefFilt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefFiltActionPerformed(evt);
            }
        });
        jPanel5.add(btnRefFilt);

        center.add(jPanel5, java.awt.BorderLayout.NORTH);

        jSplitPane2.setDividerLocation(300);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel24.setPreferredSize(new java.awt.Dimension(600, 100));
        jPanel24.setLayout(new java.awt.GridLayout(1, 0));

        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        tbHistoricoCP.setAutoCreateRowSorter(true);
        tbHistoricoCP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CIA", "Tipo Prov", "Proveedor", "Nombre", "Fecha Documento", "Semana", "Documento", "Tipo mora", "Cta Presupuesto", "Descripcion Cta Presupuesto", "Moneda", "Monto", "Saldo", "Sld Sug", "Monto ₡", "Abonar", "Aprobar", "Condición V", "!", "id", "Observaciones", "Crédito Proveedor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, true, true, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbHistoricoCP.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbHistoricoCP.setColumnSelectionAllowed(true);
        tbHistoricoCP.setFillsViewportHeight(true);
        tbHistoricoCP.setShowGrid(true);
        tbHistoricoCP.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbHistoricoCP);
        tbHistoricoCP.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (tbHistoricoCP.getColumnModel().getColumnCount() > 0) {
            tbHistoricoCP.getColumnModel().getColumn(0).setPreferredWidth(80);
            tbHistoricoCP.getColumnModel().getColumn(1).setPreferredWidth(80);
            tbHistoricoCP.getColumnModel().getColumn(2).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(2).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(2).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(3).setPreferredWidth(300);
            tbHistoricoCP.getColumnModel().getColumn(4).setPreferredWidth(90);
            tbHistoricoCP.getColumnModel().getColumn(6).setPreferredWidth(90);
            tbHistoricoCP.getColumnModel().getColumn(9).setPreferredWidth(300);
            tbHistoricoCP.getColumnModel().getColumn(10).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(10).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(10).setMaxWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(15).setPreferredWidth(80);
            tbHistoricoCP.getColumnModel().getColumn(18).setMinWidth(10);
            tbHistoricoCP.getColumnModel().getColumn(18).setPreferredWidth(10);
            tbHistoricoCP.getColumnModel().getColumn(18).setMaxWidth(10);
            tbHistoricoCP.getColumnModel().getColumn(19).setMinWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(19).setPreferredWidth(0);
            tbHistoricoCP.getColumnModel().getColumn(19).setMaxWidth(0);
        }
        tbHistoricoCP.getTableHeader().setPreferredSize(new java.awt.Dimension(jScrollPane1.getWidth(),30));
        tbHistoricoCP.getTableHeader().setBackground(new java.awt.Color(102,102,102));
        tbHistoricoCP.getTableHeader().setForeground(new java.awt.Color(255,255,255));
        tbHistoricoCP.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14) {});

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel22.setPreferredSize(new java.awt.Dimension(1126, 40));
        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Proveedor");
        jPanel22.add(jLabel8);

        txtBuscarProvedor.setPreferredSize(new java.awt.Dimension(300, 25));
        jPanel22.add(txtBuscarProvedor);

        btnBuscarProv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBuscarProv.setToolTipText("Buscar registros por nombre de proveedor");
        btnBuscarProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProvActionPerformed(evt);
            }
        });
        jPanel22.add(btnBuscarProv);

        btnExpExc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExpExc.setToolTipText("Exportar a excel");
        btnExpExc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpExcActionPerformed(evt);
            }
        });
        jPanel22.add(btnExpExc);

        lbContApSinAp.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jPanel22.add(lbContApSinAp);

        btnPendientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/file.png"))); // NOI18N
        btnPendientes.setToolTipText("Ver pendientes");
        btnPendientes.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPendientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendientesActionPerformed(evt);
            }
        });
        jPanel22.add(btnPendientes);

        jPanel4.add(jPanel22, java.awt.BorderLayout.PAGE_START);

        jPanel24.add(jPanel4);

        jSplitPane2.setLeftComponent(jPanel24);

        jPanel25.setPreferredSize(new java.awt.Dimension(600, 100));
        jPanel25.setLayout(new java.awt.GridLayout(1, 0));

        jPanel6.setPreferredSize(new java.awt.Dimension(790, 160));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.BorderLayout());

        lbTbHistoCpInfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbTbHistoCpInfo.setText("Filas");
        jPanel12.add(lbTbHistoCpInfo, java.awt.BorderLayout.CENTER);

        jLabel1.setBackground(new java.awt.Color(255, 255, 51));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("  Abono Pendiente  ");
        jLabel1.setOpaque(true);
        jPanel12.add(jLabel1, java.awt.BorderLayout.EAST);

        jPanel6.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel15.setPreferredSize(new java.awt.Dimension(484, 150));
        jPanel15.setLayout(new java.awt.GridLayout(1, 0));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles montos seleccionados"));
        jPanel14.setLayout(new java.awt.GridLayout(1, 0));

        txtDetMontSel.setEditable(false);
        txtDetMontSel.setColumns(20);
        txtDetMontSel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDetMontSel.setRows(5);
        jScrollPane2.setViewportView(txtDetMontSel);

        jPanel14.add(jScrollPane2);

        jPanel15.add(jPanel14);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles abonos seleccionado"));
        jPanel16.setPreferredSize(new java.awt.Dimension(525, 40));
        jPanel16.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane4.setBorder(null);

        txtDetAbon.setEditable(false);
        txtDetAbon.setColumns(20);
        txtDetAbon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDetAbon.setRows(5);
        jScrollPane4.setViewportView(txtDetAbon);

        jPanel16.add(jScrollPane4);

        jPanel15.add(jPanel16);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles proveedor"));
        jPanel19.setLayout(new java.awt.GridLayout(1, 0));

        jPanel21.setLayout(new java.awt.GridLayout(1, 1));

        jScrollPane3.setBorder(null);

        txtProvDet.setEditable(false);
        txtProvDet.setColumns(20);
        txtProvDet.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtProvDet.setRows(5);
        jScrollPane3.setViewportView(txtProvDet);

        jPanel21.add(jScrollPane3);

        jPanel19.add(jPanel21);

        jPanel20.setLayout(new java.awt.GridLayout(2, 1));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Compras"));
        jPanel17.setPreferredSize(new java.awt.Dimension(220, 84));
        jPanel17.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtCompras.setEditable(false);
        txtCompras.setColumns(20);
        txtCompras.setRows(5);
        jScrollPane5.setViewportView(txtCompras);

        jPanel17.add(jScrollPane5);

        jPanel20.add(jPanel17);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Pagos"));
        jPanel18.setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtPagos.setEditable(false);
        txtPagos.setColumns(20);
        txtPagos.setRows(5);
        jScrollPane6.setViewportView(txtPagos);

        jPanel18.add(jScrollPane6);

        jPanel20.add(jPanel18);

        jPanel19.add(jPanel20);

        jPanel15.add(jPanel19);

        jPanel6.add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel6);

        jSplitPane2.setRightComponent(jPanel25);

        center.add(jSplitPane2, java.awt.BorderLayout.CENTER);

        add(center, java.awt.BorderLayout.CENTER);

        north.setPreferredSize(new java.awt.Dimension(400, 50));
        north.setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(1016, 17));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jProgressBar1.setOpaque(true);
        jProgressBar1.setStringPainted(true);
        jPanel1.add(jProgressBar1);

        north.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 0);
        flowLayout3.setAlignOnBaseline(true);
        jPanel2.setLayout(flowLayout3);

        lbCurrentWeek.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbCurrentWeek.setText("Total abonado: ");
        jPanel2.add(lbCurrentWeek);

        lbresumenAbon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbresumenAbon.setText("Semana ");
        jPanel2.add(lbresumenAbon);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Presupuesto ₡");
        jPanel13.add(jLabel2);

        txtPresupuestoCol.setPreferredSize(new java.awt.Dimension(100, 22));
        jPanel13.add(txtPresupuestoCol);

        lbRestPreCol.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbRestPreCol.setText("Restante");
        jPanel13.add(lbRestPreCol);

        jPanel2.add(jPanel13);

        north.add(jPanel2, java.awt.BorderLayout.CENTER);

        add(north, java.awt.BorderLayout.NORTH);

        jPanel3.setPreferredSize(new java.awt.Dimension(20, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.EAST);

        west.setPreferredSize(new java.awt.Dimension(20, 30));

        javax.swing.GroupLayout westLayout = new javax.swing.GroupLayout(west);
        west.setLayout(westLayout);
        westLayout.setHorizontalGroup(
            westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        westLayout.setVerticalGroup(
            westLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
        );

        add(west, java.awt.BorderLayout.LINE_START);

        south.setPreferredSize(new java.awt.Dimension(20, 0));

        javax.swing.GroupLayout southLayout = new javax.swing.GroupLayout(south);
        south.setLayout(southLayout);
        southLayout.setHorizontalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1078, Short.MAX_VALUE)
        );
        southLayout.setVerticalGroup(
            southLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(south, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            createQuery();
            getProviderdetails();
        }

    }//GEN-LAST:event_cmbProveedorItemStateChanged
    private void getProviderdetails() {
//provider details
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    txtProvDet.setText("");
                    String selected = cmbProveedor.getSelectedItem().toString();
                    String prov = selected.equals("Todos")
                            ? "" : selected.substring(selected.indexOf("-") + 1, selected.length());

                    if (!prov.isEmpty()) {
                        String nombreProv = selected.substring(0, selected.indexOf("-"));
                        java.util.Date in = logic.CommonDateFunctions.getLastWeekStartDate();
                        java.util.Date end = logic.CommonDateFunctions.getLastWeekEndDate();
                        String cia = !cmbCia.getSelectedItem().toString().equals("Todas")
                                ? cmbCia.getSelectedItem().toString() : "";
                        Proveedor p = crp.getProveedor(cia + "-" + prov);

                        if (p != null) {
                            listaPagos = chcp.obtenerResuPagProvedor(prov, cia, in, end, nombreProv);
                            listaCompras = chcp.obtenerResuComProvedor(prov, cia, in, end, nombreProv);
                            if (!listaPagos.isEmpty()) {
                                String pag = "";
                                for (PagoProv c : listaPagos) {
                                    if (c.getMoneda().equals("CRC")) {
                                        pag += "Monto: ₡" + AppStaticValues.numberFormater.format(c.getMonto()) + "\n";
                                    } else {
                                        pag += "Monto: $" + AppStaticValues.numberFormater.format(c.getMonto()) + "\n";
                                    }
                                }
                                txtPagos.setText(pag);
                            } else {
                                txtPagos.setText("");
                            }
                            if (!listaCompras.isEmpty()) {
                                String pag = "";
                                for (entitys.CompraProv c : listaCompras) {
                                    if (c.getMoneda().equals("CRC")) {
                                        pag += "Monto ₡" + AppStaticValues.numberFormater.format(c.getMonto()) + "\n";
                                    } else {
                                        pag += "Monto: $" + AppStaticValues.numberFormater.format(c.getMonto()) + "\n";
                                    }
                                }
                                txtCompras.setText(pag);
                            } else {
                                txtCompras.setText("");
                            }

                        } else {
                            //this.txtProvDet.setText("");
                            txtPagos.setText("");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("view.MantenimientoPagos.getProviderdetails() error " + e.getMessage());
                }
            }
        };
        Thread t = new Thread(r);
        t.start();

    }

    private void createQueryRS() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                loadingInfo = true;
                ckResumen.setEnabled(false);
                view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);
                listaHistoricoCP = chcp.getcp_cilt_rymsaPlusAbonoSugeridoResumenSemanal(currentWeek);
                loadTbHistoricoCP(listaHistoricoCP);

                lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount() + "\t |Facturas vencidas " + facturasVen);
                ckResumen.setEnabled(true);
                loadingInfo = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        Thread t = new Thread(r);
        t.start();

    }

    private void createQuery() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                loadingInfo = true;
                //enableMenu(false);
                String p = cmbProveedor.getSelectedItem().toString();
                String nombreProv = "";
                if (!p.isEmpty() && !p.equals("Todos")) {
                    int length = p.indexOf("-");
                    nombreProv = p.substring(0, p.indexOf("-"));
                    p = p.substring(length + 1, p.length());
                }
                setUpProgessBar("Cargando", 0, 0, 0);

                String prov = !p.equals("Todos")
                        ? p : "";

                String mora = !cmbMora.getSelectedItem().toString().equals("Todas") ? cmbMora.getSelectedItem().toString() : "";

                String cia = !cmbCia.getSelectedItem().toString().equals("Todas") ? cmbCia.getSelectedItem().toString() : "";

                String tipo = !cmbTipoProv.getSelectedItem().toString().equals("Todas") ? cmbTipoProv.getSelectedItem().toString() : "";
                String moneda = !cmbMoneda.getSelectedItem().toString().equals("Todas") ? cmbMoneda.getSelectedItem().toString() : "";
                txtBuscarProvedor.setText("");
                view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);

                listaHistoricoCP = chcp.getcp_cilt_rymsaPlusAbonoSugerido(
                        new java.util.Date(), new java.util.Date(),
                        cia, prov, mora, moneda, tipo, nombreProv);
                loadTbHistoricoCP(listaHistoricoCP);

                lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount() + "\t |Facturas vencidas " + facturasVen);
                //enableMenu(true);
                loadingInfo = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        Thread t = new Thread(r);
        t.start();

    }

    /**
     * this method refresh lbTbHistoCpInfo JTable information
     *
     */
    private void refreshTB() {
        this.loadingInfo = true;
        txtBuscarProvedor.setText("");
        view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);
        loadTbHistoricoCP(listaHistoricoCP);
        lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount() + "\t | Facturas vencidas " + facturasVen);
        this.loadingInfo = false;
    }


    private void cmbMoraItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMoraItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            //handleTbHistoCPFilters();
            createQuery();
        }
    }//GEN-LAST:event_cmbMoraItemStateChanged

    private void cmbCiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCiaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            //handleTbHistoCPFilters();
            int index = cmbCia.getSelectedIndex();

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    //enableMenu(false);
                    if (index == 0) {

                        refreshInfo();

                    } else {

                        load();
                    }
                    //enableMenu(true);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            };
            Thread t = new Thread(r);
            t.start();

        }
    }//GEN-LAST:event_cmbCiaItemStateChanged
    private void refreshInfo() {
        loadingInfo = true;
        txtDetAbon.setText("");
        txtDetMontSel.setText("");
        String p = cmbProveedor.getSelectedItem().toString();
        String nombreProv = "";
        if (!p.isEmpty() && !p.equals("Todos")) {
            int length = p.indexOf("-");
            nombreProv = p.substring(0, length);
            p = p.substring(length, p.length());
        }
        setUpProgessBar("Cargando", 0, 0, 0);

        String prov = !p.equals("Todos")
                ? p : "";

        String mora = !cmbMora.getSelectedItem().toString().equals("Todas") ? cmbMora.getSelectedItem().toString() : "";

        String cia = !cmbCia.getSelectedItem().toString().equals("Todas") ? cmbCia.getSelectedItem().toString() : "";
        if (cia.equals("")) {
            prov = "";
        }
        String tipo = !cmbTipoProv.getSelectedItem().toString().equals("Todas") ? cmbTipoProv.getSelectedItem().toString() : "";
        String moneda = !cmbMoneda.getSelectedItem().toString().equals("Todas") ? cmbMoneda.getSelectedItem().toString() : "";

        view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);
        txtBuscarProvedor.setText("");
        listaHistoricoCP = chcp.getcp_cilt_rymsaPlusAbonoSugerido(
                new java.util.Date(), new java.util.Date(),
                cia, prov, mora, moneda, tipo, nombreProv);
        loadTbHistoricoCP(listaHistoricoCP);

        lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount() + "\t |Facturas vencidas " + facturasVen);
        getProviderdetails();

        loadCmbProveedor(listaHistoricoCP);
        loadingInfo = false;
    }

    private void load() {
        loadingInfo = true;
        txtDetMontSel.setText("");
        txtDetAbon.setText("");
        txtBuscarProvedor.setText("");
        view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);
        cmbProveedor.removeAllItems();
        String cia = cmbCia.getSelectedItem().toString();
        if (cia.equals("Todas")) {
            cia = "";
        }
        listaHistoricoCP = chcp.getcp_cilt_rymsaPlusAbonoSugerido(
                new java.util.Date(), new java.util.Date(),
                cia, "", "", "", "", "");
        loadTbHistoricoCP(listaHistoricoCP);
        loadCmbProveedor(listaHistoricoCP);
        lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount() + "\t |Facturas vencidas " + facturasVen);
        addCellColorCode(tbHistoricoCP, 17);
        //prepareTc();
        loadingInfo = false;
    }
    private void btnRefFiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefFiltActionPerformed
        // TODO add your handling code here:

        Runnable r = new Runnable() {
            @Override
            public void run() {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                loadingInfo = true;
                cmbCia.setSelectedIndex(0);
                cmbMora.setSelectedIndex(0);
                cmbTipoProv.setSelectedIndex(0);
                cmbMoneda.setSelectedIndex(0);
                cmbProveedor.setSelectedIndex(0);
                ckResumen.setSelected(false);

                //enableMenu(false);
                loadingInfo = false;
                refreshInfo();
                //enableMenu(true);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_btnRefFiltActionPerformed
//    private void enableMenu(boolean enable) {
//        btnRefFilt.setEnabled(enable);
//        cmbCia.setEnabled(enable);
//        cmbMora.setEnabled(enable);
//        cmbTipoProv.setEnabled(enable);
//        cmbMoneda.setEnabled(enable);
//        cmbProveedor.setEnabled(enable);
//        ckResumen.setEnabled(enable);
//    }
    private void cmbTipoProvItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoProvItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            //handleTbHistoCPFilters();
            createQuery();
        }
    }//GEN-LAST:event_cmbTipoProvItemStateChanged

    private void cmbMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            //handleTbHistoCPFilters();
            createQuery();
        }
    }//GEN-LAST:event_cmbMonedaItemStateChanged

    private void cmbProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbProveedorActionPerformed

    private void btnExpExcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpExcActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                txtBuscarProvedor.setText("");
                view.util.JTableCommonFunctions.limpiarTabla(tbHistoricoCP);
                ArrayList<entitys.HistoricoCP> res = prepareTbToImportToExcel(listaHistoricoCP);
                lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount());
                guardarExcel(res);
                refreshTB();
                loadingInfo = false;
            }
        });
        t.start();
    }//GEN-LAST:event_btnExpExcActionPerformed

    private void menuAbApActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbApActionPerformed
        // TODO add your handling code here:
        Runnable r = new Runnable() {
            @Override
            public void run() {
                agregarVariosAbonos(true);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_menuAbApActionPerformed

    private void mnDeleteAbonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDeleteAbonoActionPerformed
        // TODO add your handling code here:
        deleteAbonos();
    }//GEN-LAST:event_mnDeleteAbonoActionPerformed

    private void mnAbonarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnAbonarActionPerformed
        // TODO add your handling code here:
        Runnable r = new Runnable() {
            @Override
            public void run() {
                agregarVariosAbonos(false);
            }
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_mnAbonarActionPerformed

    private void mnObservacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnObservacionActionPerformed
        // TODO add your handling code here:
        int[] selectedRows = tbHistoricoCP.getSelectedRows();
        if (selectedRows.length > 1) {
            JOptionPane.showMessageDialog(null, "No se puede comentar más de un registro a la vez");
            return;
        }
        int row = this.tbHistoricoCP.getSelectedRow();
        String doc = tbHistoricoCP.getValueAt(row, 6).toString();
        String cia = tbHistoricoCP.getValueAt(row, 0).toString();
        String numProv = tbHistoricoCP.getValueAt(row, 2).toString();
        data.CrudDetallesHistoricoPagos crdet = new data.CrudDetallesHistoricoPagos();
        DetallesHistoricoPagos det = crdet.getDetalles_Historico_Pagos_Credito(cia, numProv, doc);
        String info = JOptionPane.showInputDialog("Ingrese el comentario", det == null ? "" : det.getObservacion());
        if (info != null) {

            if (det == null) {
                agregarComentario(row, info);
            } else {
                actualizarComentario(row, info, det);
            }

        }

    }//GEN-LAST:event_mnObservacionActionPerformed

    private void btnBuscarProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProvActionPerformed
        // TODO add your handling code here:
        buscarRegPorNomProv();
    }//GEN-LAST:event_btnBuscarProvActionPerformed

    private void ckResumenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ckResumenItemStateChanged
        // TODO add your handling code here:
        if (ckResumen.isSelected()) {
            this.loadingInfo = true;
            cmbCia.setSelectedIndex(0);
            cmbMora.setSelectedIndex(0);
            cmbTipoProv.setSelectedIndex(0);
            cmbMoneda.setSelectedIndex(0);
            cmbProveedor.setSelectedIndex(0);
            this.cmbCia.setEnabled(false);
            this.cmbMoneda.setEnabled(false);
            this.cmbMora.setEnabled(false);
            this.cmbTipoProv.setEnabled(false);
            this.cmbProveedor.setEnabled(false);
            this.loadingInfo = false;
            createQueryRS();
        } else {
            this.loadingInfo = true;
            this.cmbCia.setEnabled(true);
            this.cmbMoneda.setEnabled(true);
            this.cmbMora.setEnabled(true);
            this.cmbTipoProv.setEnabled(true);
            this.cmbProveedor.setEnabled(true);
            this.loadingInfo = false;
            createQuery();
        }
    }//GEN-LAST:event_ckResumenItemStateChanged

    private void btnPendientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPendientesActionPerformed
        // TODO add your handling code here:
        loadAsyncAbSAp();
        this.jDialog1.setVisible(true);
    }//GEN-LAST:event_btnPendientesActionPerformed

    private void btnRefAbSinApActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefAbSinApActionPerformed
        // TODO add your handling code here:
        loadAsyncAbSAp();
    }//GEN-LAST:event_btnRefAbSinApActionPerformed
    private void addRowSAp(DefaultTableModel model, AbonoSugerido e) {
        model.addRow(new Object[]{
            e.getId(),
            e.getCIA(),
            e.getUsuario(),
            logic.AppStaticValues.dateFormat.format(e.getFecha_Creacion()),
            logic.AppStaticValues.dateFormat.format(e.getFecha_documento()),
            e.getDocumento(),
            e.getNumero_Proveedor(),
            e.getNombre_Proveedor(),
            e.getCuenta_Presupuesto(),
            e.getDescripion_Cta_Presupuesto(),
            e.getMoneda(),
            e.getMonto_Original(),
            e.getAbono(),
            e.getAprobado() == 1 ? true : false
        });
    }

    private void loadAsyncAbSAp() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                view.util.JTableCommonFunctions.limpiarTabla(tbAbSinAp);
                ArrayList<AbonoSugerido> lista = crab.getAbonoSugeridosSinAp();
                DefaultTableModel model = (DefaultTableModel) tbAbSinAp.getModel();
                lista.forEach(e -> {
                    addRowSAp(model, e);
                });
                lbInTbAbSAp.setText("Filas: " + tbAbSinAp.getRowCount());
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                loadingInfo = false;
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
    private void btnDeleteAbSApActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAbSApActionPerformed
        // TODO add your handling code here:
        int[] selectedRows = tbAbSinAp.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione al menos una fila");
            return;
        }
        for (int row : selectedRows) {
            int id = (int) tbAbSinAp.getValueAt(row, 0);
            System.out.println("deleting " + row + " id " + id);
            boolean res = crab.eliminarAbonoSugeridos(id);
            if (res) {
                System.out.println("register deleted!");
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar el id " + id);
            }
        }
        loadAsyncAbSAp();
    }//GEN-LAST:event_btnDeleteAbSApActionPerformed

    private void btnApAbSApActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApAbSApActionPerformed
        // TODO add your handling code here:
        int[] selectedRows = tbAbSinAp.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione al menos una fila");
            return;
        }
        for (int row : selectedRows) {
            int id = (int) tbAbSinAp.getValueAt(row, 0);
            System.out.println("deleting " + row + " id " + id);
            boolean res = crab.actualizarAbonoSugeridoSinAp(id, currentWeek);
            if (res) {
                System.out.println("register updated!");
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrió un error al actualizar el id " + id);
            }
        }
        loadAsyncAbSAp();
    }//GEN-LAST:event_btnApAbSApActionPerformed

    private void btnExAbSApActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExAbSApActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                loadingInfo = true;
                guardarExcel();
                loadingInfo = false;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        t.start();
    }//GEN-LAST:event_btnExAbSApActionPerformed
    private void guardarExcel() {
        this.jProgressBar1.setString("Guardando excel...");
        this.jProgressBar1.setVisible(true);
        SimpleExcelWriter sew = new SimpleExcelWriter();
        //sew.writeToExcell(this.tbConciliacionMarcas);

        boolean saved = sew.writeJtableExcelFile(tbAbSinAp, "Reporte");
        if (saved) {
            this.jProgressBar1.setString("Excel guardadado correctamente");
            CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        } else {
            this.jProgressBar1.setString("Proceso cancelado");
            //this.jpbLoading.setVisible(false);
            CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        }
    }

    private void actualizarComentario(int row, String info, DetallesHistoricoPagos det) {
        data.CrudDetallesHistoricoPagos crdet = new data.CrudDetallesHistoricoPagos();
        String ob = det.getObservacion();
        det.setObservacion(info);
        det.setULTIMA_ACTUALIZACIO(new java.util.Date());
        boolean res = crdet.actualizarDetalles_Historico_Pagos_Credito(det);
        if (res) {
            //JOptionPane.showMessageDialog(null, "Se ha actualizado un comentario a la factura " + det.getFACTURA());
            tbHistoricoCP.setValueAt(info, row, 20);
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al actualizar comentario a la factura " + det.getFACTURA());
        }
    }

    private void agregarComentario(int row, String info) {

        data.CrudDetallesHistoricoPagos crdet = new data.CrudDetallesHistoricoPagos();
        entitys.DetallesHistoricoPagos det = new entitys.DetallesHistoricoPagos();
        det.setFECHA(new java.util.Date());
        det.setULTIMA_ACTUALIZACIO(new java.util.Date());
        det.setUSUARIO(DataUser.username);
        det.setFACTURA(tbHistoricoCP.getValueAt(row, 6).toString());
        det.setCIA(tbHistoricoCP.getValueAt(row, 0).toString());
        det.setNOMBRE_PROVEEDOR(tbHistoricoCP.getValueAt(row, 3).toString());
        det.setNUM_PROVEEDOR(tbHistoricoCP.getValueAt(row, 2).toString());
        det.setObservacion(info);
        boolean res = crdet.agregarDetallesHistoricoPagos(det);
        if (res) {
            tbHistoricoCP.setValueAt(info, row, 20);
        } else {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar un comentario a la factura " + det.getFACTURA());
        }
    }

    /**
     * this method adds an AbonoSugerido registers to the database, to every row
     * selected to the table tbHistoricoCP, this when the there are not a
     * register for the same document and week
     */
    private void agregarVariosAbonos(boolean aprobar) {
        int[] selectedRows = tbHistoricoCP.getSelectedRows();
        this.jProgressBar1.setVisible(true);
        this.loadingInfo = true;
        for (int row : selectedRows) {
            System.out.println("selected row " + tbHistoricoCP.getValueAt(row, 6).toString());
            String doc = tbHistoricoCP.getValueAt(row, 6).toString();
            String cia = tbHistoricoCP.getValueAt(row, 0).toString();
            String numProv = tbHistoricoCP.getValueAt(row, 2).toString();
            HistoricoCP h = HistoricoCP.obtenerHistoricoPorDoc(doc, cia, numProv, listaHistoricoCP);
            if (h.getSugeridos().isEmpty()) {
                AbonoSugerido a = getAbonoFromForm(h, row);
                if (a != null) {
                    a.setAprobado(aprobar ? 1 : 0);
                    boolean res = crab.agregarAbonoSugerido(a);
                    if (res) {
                        AbonoSugerido ab = crab.getAbonoSugeridos(a.getSemana(), a.getDocumento(), a.getNumero_Proveedor(), a.getCIA());
                        if (ab != null) {
                            h.getSugeridos().add(ab);
                            jProgressBar1.setString("Abono agregado para " + h.getDOCUMENTO());
                        }

                    } else {
                        jProgressBar1.setString("Error agregando abono para " + h.getDOCUMENTO());
                    }
                }
            } else {
                //agregar un abono al saldo
                double saldo = getSaldoRestante(h);
                if (saldo > 0) {
                    //AbonoSugerido ab = AbonoSugerido.obtenerAbonoMismaSemana(doc, currentWeek, h.getSugeridos());
                    AbonoSugerido ab = crab.getAbonoSugeridos(currentWeek, doc, numProv, cia);

                    if (ab == null) {
                        AbonoSugerido a = getAbonoFromForm(h, row);
                        a.setAprobado(aprobar ? 1 : 0);
                        a.setAbono(saldo);
                        a.setSaldo_Actuual(saldo);

                        boolean res = crab.agregarAbonoSugerido(a);
                        if (res) {
                            a = crab.getAbonoSugeridos(a.getSemana(), a.getDocumento(), a.getNumero_Proveedor(), a.getCIA());
                            if (a != null) {
                                h.getSugeridos().add(a);
                                jProgressBar1.setString("Abono agregado para " + h.getDOCUMENTO());
                            }

                        } else {
                            jProgressBar1.setString("Error agregando abono para " + h.getDOCUMENTO());
                        }
                    }

                }
            }
        }
        refreshTB();
        jProgressBar1.setVisible(false);
        loadingInfo = false;
    }

    private void deleteAbonos() {
        int[] selectedRows = tbHistoricoCP.getSelectedRows();
        this.jProgressBar1.setVisible(true);
        this.loadingInfo = true;
        for (int row : selectedRows) {
            System.out.println("selected row " + tbHistoricoCP.getValueAt(row, 6).toString());
            String doc = tbHistoricoCP.getValueAt(row, 6).toString();
            String cia = tbHistoricoCP.getValueAt(row, 0).toString();
            String numProv = tbHistoricoCP.getValueAt(row, 2).toString();
            HistoricoCP h = HistoricoCP.obtenerHistoricoPorDoc(doc, cia, numProv, listaHistoricoCP);
            //AbonoSugerido ab = AbonoSugerido.obtenerAbonoMismaSemana(doc, currentWeek, h.getSugeridos());
            AbonoSugerido ab = crab.getAbonoSugeridos(currentWeek, doc, numProv, cia);
            if (ab != null) {
                boolean res = crab.eliminarAbonoSugeridos(ab.getId());
                if (res) {
                    AbonoSugerido a = AbonoSugerido.obtenerAbonoPorId(ab.getId(), h.getSugeridos());
                    h.getSugeridos().remove(a);

                } else {
                    jProgressBar1.setString("Error eliminando abono para " + h.getDOCUMENTO());
                }
            }
        }
        refreshTB();
        jProgressBar1.setVisible(false);
        loadingInfo = false;
    }

    private AbonoSugerido getAbonoFromForm(HistoricoCP h, int row) {
        AbonoSugerido ab = new AbonoSugerido();
        ab.setFecha_documento(h.getFECHA_DOCUMENTO());
        ab.setMonto_Original(saldoTotal);
        ab.setDocumento(h.getDOCUMENTO());
        ab.setCIA(h.getCIA());
        ab.setCuenta_Presupuesto(h.getCTA_PRESUPUESTO());
        ab.setNumero_Proveedor(h.getPROVEEDOR());
        ab.setMoneda(h.getMONEDA());
        ab.setUsuario(DataUser.username);
        ab.setNombre_Proveedor(h.getNOMBRE());
        ab.setTipo_Proveedor(h.getCONTA_CRED());
        ab.setDescripion_Cta_Presupuesto(tbHistoricoCP.getValueAt(row, 9).toString());
        ab.setSemana(this.currentWeek);
        ab.setAprobado(1);
        ab.setMonto_Original(h.getMONTO());
        ab.setSaldo_Actuual(h.getSaldo());
        ab.setAbono(h.getSaldo());
        ab.setSaldo_Restante(0);

        ab.setMonto_Colones(h.getSaldo());
        if (h.getMONEDA().equals("USD") && this.tipoc == null) {
            JOptionPane.showMessageDialog(null, "No puede hacer abonos en dolares hasta que establecer el tipo de cambio del día");
            return null;
        } else if (h.getMONEDA().equals("USD")) {
            double monto_colones = (ab.getAbono() * this.tipoc.getVenta());
            ab.setMonto_Colones(monto_colones);
        }
        ab.setAbono_Total(true);
        ab.setFecha_Creacion(new java.util.Date());
        return ab;
    }

    private void guardarExcel(ArrayList<entitys.HistoricoCP> lista) {
        this.jProgressBar1.setString("Guardando excel...");
        this.jProgressBar1.setVisible(true);
        SimpleExcelWriter sew = new SimpleExcelWriter();

        boolean saved = sew.writeHistoricoCPToExcel("Reporte", lista, currentWeek, listaPresupuesto);
        if (saved) {
            this.jProgressBar1.setString("Excel guardadado correctamente");
            CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        } else {
            this.jProgressBar1.setString("Proceso cancelado");
            //this.jpbLoading.setVisible(false);
            CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
        }
        lbTbHistoCpInfo.setText("Filas: " + tbHistoricoCP.getRowCount());
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApAbSAp;
    private javax.swing.JButton btnBuscarProv;
    private javax.swing.JButton btnDeleteAbSAp;
    private javax.swing.JButton btnExAbSAp;
    private javax.swing.JButton btnExpExc;
    private javax.swing.JButton btnPendientes;
    private javax.swing.JButton btnRefAbSinAp;
    private javax.swing.JButton btnRefFilt;
    private javax.swing.JPanel center;
    private javax.swing.JCheckBox ckResumen;
    private javax.swing.JComboBox<String> cmbCia;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbMora;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JComboBox<String> cmbTipoProv;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JLabel lbContApSinAp;
    private javax.swing.JLabel lbCurrentWeek;
    private javax.swing.JLabel lbInTbAbSAp;
    private javax.swing.JLabel lbRestPreCol;
    private javax.swing.JLabel lbTbHistoCpInfo;
    private javax.swing.JLabel lbresumenAbon;
    private javax.swing.JMenuItem menuAbAp;
    private javax.swing.JMenuItem mnAbonar;
    private javax.swing.JMenuItem mnDeleteAbono;
    private javax.swing.JMenuItem mnObservacion;
    private javax.swing.JPanel north;
    private javax.swing.JPanel south;
    private javax.swing.JTable tbAbSinAp;
    private javax.swing.JTable tbHistoricoCP;
    private javax.swing.JTextField txtBuscarProvedor;
    private javax.swing.JTextArea txtCompras;
    private javax.swing.JTextArea txtDetAbon;
    private javax.swing.JTextArea txtDetMontSel;
    private javax.swing.JTextArea txtPagos;
    private javax.swing.JTextField txtPresupuestoCol;
    private javax.swing.JTextArea txtProvDet;
    private javax.swing.JPanel west;
    // End of variables declaration//GEN-END:variables

    private void setPresupListener() {
        this.txtPresupuestoCol.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        presupColones = Double.parseDouble(txtPresupuestoCol.getText());
                        txtPresupuestoCol.setText(logic.AppStaticValues.numberFormater.format(presupColones));
                    } catch (Exception ex) {
                        System.out.println(".keyPressed()");
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error " + ex.getMessage());
                    }
                }
            }
        });
    }

}

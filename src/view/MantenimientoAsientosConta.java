/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE;
import entitys.AsientoFactura;
import entitys.CorreoExcluidoFE;
import entitys.Departamento;
import entitys.DetallesFacturas.DetallesLineasFactura;
import entitys.DetallesFacturas.ResumenTotalesDetallesFacturaE;
import entitys.Presupuesto;
import entitys.Receips;
import entitys.Sociedad;
import entitys.Usuario;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import logic.AppStaticValues;
import services.SimpleExcelWriter;
import services.VersionHandler;
import view.util.CustomMessages;
import static view.util.JTableCommonFunctions.limpiarTabla;

/**
 *
 * @author eobregon
 */
public class MantenimientoAsientosConta extends javax.swing.JPanel {

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
    entitys.TipoCambio tc;
    VersionHandler vh = new VersionHandler();
    ArrayList<ResumenTotalesDetallesFacturaE> listaResumenDetallesFactura;

    /**
     * Creates new form MantenimientoAsientosConta
     */
    public MantenimientoAsientosConta() {
        initComponents();
        this.crAsientos = new data.CrudAsiento();
        this.listaResumenDetallesFactura = new ArrayList<ResumenTotalesDetallesFacturaE>();
        prepareGUI();
    }

    private void prepareGUI() {
        loadInfo();
        setView();
        setListeners();
    }

    private void loadInfo() {
        Sociedad s = new Sociedad();
        sociedades = s.quemarSociedades();

    }

    private void setView() {
        jPanel29.setVisible(false);
        btnCorreosOmitidos.setVisible(false);
        //AppStaticValues.rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tbReceips.getColumnModel().getColumn(5).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(6).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(7).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(8).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(9).setCellRenderer(AppStaticValues.rightRenderer);
        tbReceips.getColumnModel().getColumn(12).setCellRenderer(AppStaticValues.rightRenderer);
        this.jProgressBar1.setVisible(false);
        setDateChooserLook();
        openReceipDetails();
        openXMLFile();
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
        openXMLFile();
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
                    /*String clave = tbReceips.getValueAt(row, 13).toString();
                    Receips r = Receips.getReceipByClave(clave, receipList);
                    MantenimientoFacturaElectronica redt = new MantenimientoFacturaElectronica(r);
                    redt.setVisible(true);*/
                    openTbReceipPDF();
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
        int row = tbReceips.getSelectedRow();
        if (row > -1) {
            try {
                String clave = tbReceips.getValueAt(row, 13).toString();
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
            limpiarTabla(tbReceips);
            loadTbReceps(lista);
            this.lbTbMantResumen.setText("Filas: " + this.tbReceips.getRowCount());
            loadingInfo = false;
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
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cmbTipoFactura = new javax.swing.JComboBox<>();
        btnCalculator = new javax.swing.JButton();
        btnUpdown = new javax.swing.JButton();
        lbDetResu = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtTotalCompras = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTotatIVA = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtTotalComprasPlusIV = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtTotalComprasPlusIVPlusOtros = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        txtComp0p = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        txtComp05p = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtIm05p = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtComp1p = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtIm1p = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtComp4p = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtIm4p = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtComp8P = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtIm8P = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtComp13P = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtIm13P = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        lbTbMantResumen = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbReceips = new javax.swing.JTable();
        jPanel25 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtBuscarConsecutivo = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        btnHideRow = new javax.swing.JButton();
        btnOpenPdf = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        north = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel7 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jdtInicio = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jdtFin = new com.toedter.calendar.JDateChooser();
        btnCargarFacturas = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        lbTotalFacturas = new javax.swing.JLabel();
        lbPendientes = new javax.swing.JLabel();
        lbRegistradas = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        btnCorreosOmitidos = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cmbSociedades = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cmbEstadoRegistro = new javax.swing.JComboBox<>();
        jPanel26 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        cmbEstadoAsignacion = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cmbMoneda = new javax.swing.JComboBox<>();
        btnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();

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
        mnCopiarNFac.setText("Copiar número factura");
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

        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel6.setPreferredSize(new java.awt.Dimension(1011, 32));
        jPanel6.setLayout(new java.awt.BorderLayout());

        java.awt.FlowLayout flowLayout5 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout5.setAlignOnBaseline(true);
        jPanel16.setLayout(flowLayout5);

        jPanel29.setEnabled(false);

        jLabel10.setText("Mostrar");
        jPanel29.add(jLabel10);

        cmbTipoFactura.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bienes", "Servicios" }));
        jPanel29.add(cmbTipoFactura);

        jPanel16.add(jPanel29);

        btnCalculator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/calculator_25x25.png"))); // NOI18N
        btnCalculator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculatorActionPerformed(evt);
            }
        });
        jPanel16.add(btnCalculator);

        btnUpdown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/up_down.png"))); // NOI18N
        btnUpdown.setToolTipText("Ocultar o mostrar resumen");
        btnUpdown.setPreferredSize(new java.awt.Dimension(30, 27));
        btnUpdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdownActionPerformed(evt);
            }
        });
        jPanel16.add(btnUpdown);

        lbDetResu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbDetResu.setText("Detalles de facturas en colones");
        jPanel16.add(lbDetResu);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-info-30.png"))); // NOI18N
        jLabel4.setToolTipText("Si selecciona todas las monedas los detalles son de facturas en colones");
        jPanel16.add(jLabel4);

        jPanel6.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resumen de facturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(204, 204, 204))); // NOI18N
        jPanel18.setMaximumSize(new java.awt.Dimension(32767, 150));
        jPanel18.setMinimumSize(new java.awt.Dimension(800, 300));
        jPanel18.setPreferredSize(new java.awt.Dimension(900, 400));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel27.setMaximumSize(new java.awt.Dimension(600, 32767));
        jPanel27.setPreferredSize(new java.awt.Dimension(300, 100));
        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Total compras                      ");
        jPanel27.add(jLabel11);

        txtTotalCompras.setEditable(false);
        txtTotalCompras.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTotalCompras.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalCompras.setText("0.0");
        txtTotalCompras.setToolTipText("");
        txtTotalCompras.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel27.add(txtTotalCompras);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Total IV                                  ");
        jPanel27.add(jLabel14);

        txtTotatIVA.setEditable(false);
        txtTotatIVA.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTotatIVA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotatIVA.setText("0.0");
        txtTotatIVA.setToolTipText("");
        txtTotatIVA.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel27.add(txtTotatIVA);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Total compras + IV              ");
        jPanel27.add(jLabel17);

        txtTotalComprasPlusIV.setEditable(false);
        txtTotalComprasPlusIV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTotalComprasPlusIV.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalComprasPlusIV.setText("0.0");
        txtTotalComprasPlusIV.setToolTipText("");
        txtTotalComprasPlusIV.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel27.add(txtTotalComprasPlusIV);

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Total compras + IV + otros");
        jPanel27.add(jLabel20);

        txtTotalComprasPlusIVPlusOtros.setEditable(false);
        txtTotalComprasPlusIVPlusOtros.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTotalComprasPlusIVPlusOtros.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotalComprasPlusIVPlusOtros.setText("0.0");
        txtTotalComprasPlusIVPlusOtros.setToolTipText("");
        txtTotalComprasPlusIVPlusOtros.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel27.add(txtTotalComprasPlusIVPlusOtros);

        jPanel18.add(jPanel27, java.awt.BorderLayout.WEST);

        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel31.setPreferredSize(new java.awt.Dimension(1100, 25));
        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 0));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("Compras IV 0%   ");
        jPanel31.add(jLabel28);

        txtComp0p.setEditable(false);
        txtComp0p.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtComp0p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp0p.setText("0.0");
        txtComp0p.setToolTipText("");
        txtComp0p.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel31.add(txtComp0p);

        jPanel28.add(jPanel31);

        jPanel30.setPreferredSize(new java.awt.Dimension(1100, 25));
        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 0));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("Compras IV 0.5%");
        jPanel30.add(jLabel26);

        txtComp05p.setEditable(false);
        txtComp05p.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtComp05p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp05p.setText("0.0");
        txtComp05p.setToolTipText("");
        txtComp05p.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel30.add(txtComp05p);

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Total impuesto 0.5%");
        jPanel30.add(jLabel27);

        txtIm05p.setEditable(false);
        txtIm05p.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtIm05p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm05p.setText("0.0");
        txtIm05p.setToolTipText("");
        txtIm05p.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel30.add(txtIm05p);

        jPanel28.add(jPanel30);

        jPanel17.setPreferredSize(new java.awt.Dimension(1100, 30));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel17.setLayout(flowLayout1);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Compras IV 1%   ");
        jPanel17.add(jLabel12);

        txtComp1p.setEditable(false);
        txtComp1p.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtComp1p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp1p.setText("0.0");
        txtComp1p.setToolTipText("");
        txtComp1p.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel17.add(txtComp1p);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Total impuesto 1%   ");
        jPanel17.add(jLabel13);

        txtIm1p.setEditable(false);
        txtIm1p.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtIm1p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm1p.setText("0.0");
        txtIm1p.setToolTipText("");
        txtIm1p.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel17.add(txtIm1p);

        jPanel28.add(jPanel17);

        jPanel19.setPreferredSize(new java.awt.Dimension(1100, 25));
        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 0));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Compras IV 4%   ");
        jPanel19.add(jLabel15);

        txtComp4p.setEditable(false);
        txtComp4p.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtComp4p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp4p.setText("0.0");
        txtComp4p.setToolTipText("");
        txtComp4p.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel19.add(txtComp4p);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Total impuesto 4%   ");
        jPanel19.add(jLabel16);

        txtIm4p.setEditable(false);
        txtIm4p.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtIm4p.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm4p.setText("0.0");
        txtIm4p.setToolTipText("");
        txtIm4p.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel19.add(txtIm4p);

        jPanel28.add(jPanel19);

        jPanel20.setPreferredSize(new java.awt.Dimension(1100, 25));
        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 0));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Compras IV 8%   ");
        jPanel20.add(jLabel18);

        txtComp8P.setEditable(false);
        txtComp8P.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtComp8P.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp8P.setText("0.0");
        txtComp8P.setToolTipText("");
        txtComp8P.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel20.add(txtComp8P);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Total impuesto 8%   ");
        jPanel20.add(jLabel19);

        txtIm8P.setEditable(false);
        txtIm8P.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtIm8P.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm8P.setText("0.0");
        txtIm8P.setToolTipText("");
        txtIm8P.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel20.add(txtIm8P);

        jPanel28.add(jPanel20);

        jPanel21.setPreferredSize(new java.awt.Dimension(1100, 25));
        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 0));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Compras IV 13%");
        jPanel21.add(jLabel21);

        txtComp13P.setEditable(false);
        txtComp13P.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtComp13P.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtComp13P.setText("0.0");
        txtComp13P.setToolTipText("");
        txtComp13P.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel21.add(txtComp13P);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Total impuesto 13% ");
        jPanel21.add(jLabel22);

        txtIm13P.setEditable(false);
        txtIm13P.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtIm13P.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtIm13P.setText("0.0");
        txtIm13P.setToolTipText("");
        txtIm13P.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel21.add(txtIm13P);

        jPanel28.add(jPanel21);

        jPanel18.add(jPanel28, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla Detalles Facturas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel8.setPreferredSize(new java.awt.Dimension(1244, 500));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel24.setPreferredSize(new java.awt.Dimension(1249, 20));
        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        lbTbMantResumen.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbTbMantResumen.setText("Filas");
        jPanel24.add(lbTbMantResumen);

        jPanel8.add(jPanel24, java.awt.BorderLayout.PAGE_END);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(900, 300));
        jScrollPane1.setViewportView(null);

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
                false, false, false, false, false, false, false, false, false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbReceips.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbReceips.setShowVerticalLines(true);
        tbReceips.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbReceips);
        if (tbReceips.getColumnModel().getColumnCount() > 0) {
            tbReceips.getColumnModel().getColumn(1).setPreferredWidth(180);
            tbReceips.getColumnModel().getColumn(2).setPreferredWidth(400);
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
            tbReceips.getColumnModel().getColumn(8).setPreferredWidth(140);
            tbReceips.getColumnModel().getColumn(9).setPreferredWidth(140);
            tbReceips.getColumnModel().getColumn(10).setMinWidth(0);
            tbReceips.getColumnModel().getColumn(10).setPreferredWidth(0);
            tbReceips.getColumnModel().getColumn(10).setMaxWidth(0);
            tbReceips.getColumnModel().getColumn(11).setPreferredWidth(140);
            tbReceips.getColumnModel().getColumn(12).setPreferredWidth(140);
            tbReceips.getColumnModel().getColumn(13).setMinWidth(0);
            tbReceips.getColumnModel().getColumn(13).setPreferredWidth(0);
            tbReceips.getColumnModel().getColumn(13).setMaxWidth(0);
        }
        tbReceips.setBackground(new java.awt.Color(51, 51, 51));

        tbReceips.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        tbReceips.setForeground(new java.awt.Color(153, 153, 153));
        tbReceips.getTableHeader().setPreferredSize(new java.awt.Dimension(jScrollPane1.getWidth(),30));
        tbReceips.getTableHeader().setBackground(new java.awt.Color(102,102,102));
        tbReceips.getTableHeader().setForeground(new java.awt.Color(0,0,0));
        //tbReceips.getTableHeader().setForeground(new java.awt.Color(201,201,201));
        tbReceips.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14) {
        });
        //view.util.JTableCommonFunctions.alignTbHeadersToRigth(tbReceips);
        tbReceips.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbReceips.getTableHeader().setForeground(new java.awt.Color(255,255,255));

        jPanel8.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel25.setPreferredSize(new java.awt.Dimension(1249, 40));
        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jPanel12.setMinimumSize(new java.awt.Dimension(161, 25));
        jPanel12.setPreferredSize(new java.awt.Dimension(230, 30));
        java.awt.FlowLayout flowLayout4 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout4.setAlignOnBaseline(true);
        jPanel12.setLayout(flowLayout4);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Factura #");
        jPanel12.add(jLabel6);

        txtBuscarConsecutivo.setPreferredSize(new java.awt.Dimension(130, 25));
        jPanel12.add(txtBuscarConsecutivo);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        jPanel12.add(jButton3);

        jPanel25.add(jPanel12);

        btnHideRow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hide file.png"))); // NOI18N
        btnHideRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHideRowActionPerformed(evt);
            }
        });
        jPanel25.add(btnHideRow);

        btnOpenPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pdf .png"))); // NOI18N
        btnOpenPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenPdfActionPerformed(evt);
            }
        });
        jPanel25.add(btnOpenPdf);

        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });
        jPanel25.add(btnExcel);

        jPanel8.add(jPanel25, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(jPanel8, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        north.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(204, 204, 204)));
        north.setPreferredSize(new java.awt.Dimension(400, 160));
        north.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 20));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jProgressBar1.setOpaque(true);
        jProgressBar1.setStringPainted(true);
        jPanel2.add(jProgressBar1);

        north.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel7.setMinimumSize(new java.awt.Dimension(312, 100));
        jPanel7.setPreferredSize(new java.awt.Dimension(1100, 130));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel23.setMinimumSize(new java.awt.Dimension(1400, 49));
        jPanel23.setPreferredSize(new java.awt.Dimension(1400, 50));
        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 0);
        flowLayout2.setAlignOnBaseline(true);
        jPanel23.setLayout(flowLayout2);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Fecha inicio");
        jPanel9.add(jLabel1);

        jdtInicio.setDateFormatString("dd-MM-yyyy");
        jdtInicio.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel9.add(jdtInicio);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Fecha final");
        jPanel9.add(jLabel2);

        jdtFin.setDateFormatString("dd-MM-yyyy");
        jdtFin.setPreferredSize(new java.awt.Dimension(120, 25));
        jdtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdtFinPropertyChange(evt);
            }
        });
        jPanel9.add(jdtFin);

        btnCargarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-start-30.png"))); // NOI18N
        btnCargarFacturas.setToolTipText("Cargar información");
        btnCargarFacturas.setMinimumSize(new java.awt.Dimension(36, 33));
        btnCargarFacturas.setPreferredSize(new java.awt.Dimension(36, 33));
        btnCargarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarFacturasActionPerformed(evt);
            }
        });
        jPanel9.add(btnCargarFacturas);

        jPanel23.add(jPanel9);

        jPanel10.setPreferredSize(new java.awt.Dimension(500, 30));
        java.awt.FlowLayout flowLayout6 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 0);
        flowLayout6.setAlignOnBaseline(true);
        jPanel10.setLayout(flowLayout6);

        lbTotalFacturas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbTotalFacturas.setText("Total factura");
        jPanel10.add(lbTotalFacturas);

        lbPendientes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbPendientes.setText("Pendientes");
        jPanel10.add(lbPendientes);

        lbRegistradas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbRegistradas.setText("Registradas");
        jPanel10.add(lbRegistradas);

        jPanel23.add(jPanel10);

        jPanel11.setMinimumSize(new java.awt.Dimension(100, 49));
        jPanel11.setPreferredSize(new java.awt.Dimension(60, 45));
        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnCorreosOmitidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/failed (3).png"))); // NOI18N
        btnCorreosOmitidos.setToolTipText("Correos omitidos");
        btnCorreosOmitidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorreosOmitidosActionPerformed(evt);
            }
        });
        jPanel11.add(btnCorreosOmitidos);

        jPanel23.add(jPanel11);

        jPanel7.add(jPanel23, java.awt.BorderLayout.PAGE_START);

        jPanel22.setPreferredSize(new java.awt.Dimension(556, 80));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout3.setAlignOnBaseline(true);
        jPanel22.setLayout(flowLayout3);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("CIA");
        jPanel13.add(jLabel7);

        cmbSociedades.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar todas", "Cedulas desconocidas", "RYMSA 3101724817", "CILT 3101086411", "IRASA 3101119637", "KATRA 3101119531", "OPYLOG 3101466557", "TURINTEL 3101468003" }));
        cmbSociedades.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSociedadesItemStateChanged(evt);
            }
        });
        jPanel13.add(cmbSociedades);

        jPanel22.add(jPanel13);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Proveedor");
        jPanel15.add(jLabel9);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorItemStateChanged(evt);
            }
        });
        jPanel15.add(cmbProveedor);

        jPanel22.add(jPanel15);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Estado en Exactus");
        jPanel14.add(jLabel8);

        cmbEstadoRegistro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Pendientes", "Registradas" }));
        cmbEstadoRegistro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoRegistroItemStateChanged(evt);
            }
        });
        jPanel14.add(cmbEstadoRegistro);

        jPanel22.add(jPanel14);

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Estado asiganación");
        jPanel26.add(jLabel23);

        cmbEstadoAsignacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "Asignadas", "Sin asignar" }));
        cmbEstadoAsignacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEstadoAsignacionItemStateChanged(evt);
            }
        });
        jPanel26.add(cmbEstadoAsignacion);

        jPanel22.add(jPanel26);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Moneda");
        jPanel22.add(jLabel3);

        cmbMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "CRC", "USD" }));
        cmbMoneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMonedaItemStateChanged(evt);
            }
        });
        jPanel22.add(cmbMoneda);

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnRefresh.setToolTipText("Refrescar fltros");
        btnRefresh.setPreferredSize(new java.awt.Dimension(25, 25));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel22.add(btnRefresh);

        jPanel7.add(jPanel22, java.awt.BorderLayout.CENTER);

        north.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(north, java.awt.BorderLayout.PAGE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(400, 20));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1174, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 260));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 425, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 260));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 425, Short.MAX_VALUE)
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

    private void cmbEstadoRegistroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEstadoRegistroItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadAsyncReceips();
        }
    }//GEN-LAST:event_cmbEstadoRegistroItemStateChanged

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
                        cmbMoneda.setSelectedIndex(0);
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
            System.out.println("view.MantenimientoAsientosConta.btnRefreshActionPerformed() error " + e.getMessage());
        }
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void cmbMonedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMonedaItemStateChanged
        // TODO add your handling code here:
        try {
            /*java.util.Date inicio = this.jdtInicio.getDate();
            java.util.Date fin = this.jdtFin.getDate();
            loadDetalles(inicio, fin);*/
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
        } catch (Exception e) {
            System.err.println("view.MantenimientoAsientosConta.cmbMonedaItemStateChanged() error " + e.getMessage());
        }


    }//GEN-LAST:event_cmbMonedaItemStateChanged

    private void btnCargarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarFacturasActionPerformed
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
    }//GEN-LAST:event_btnCargarFacturasActionPerformed

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

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
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
    }//GEN-LAST:event_btnExcelActionPerformed

    private void btnOpenPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenPdfActionPerformed
        // TODO add your handling code here:

        openTbReceipPDF();
    }//GEN-LAST:event_btnOpenPdfActionPerformed

    private void btnHideRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHideRowActionPerformed
        // TODO add your handling code here:
        deleteRows();
    }//GEN-LAST:event_btnHideRowActionPerformed

    private void menuAbrirPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAbrirPDFActionPerformed
        // TODO add your handling code here:
        openTbReceipPDF();
    }//GEN-LAST:event_menuAbrirPDFActionPerformed

    private void mnCopiarNFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnCopiarNFacActionPerformed
        // TODO add your handling code here:
        try {
            int row = this.tbReceips.getSelectedRow();

            if (row > -1) {

                String consecutivo = this.tbReceips.getValueAt(row, 1).toString();
                StringSelection selection = new StringSelection(consecutivo);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                System.out.println("consecutivo copiado: " + consecutivo);
            }
        } catch (Exception e) {
            System.err.println("view.MantenimientoAsientosConta.mnCopiarNFacActionPerformed() error " + e.getMessage());
        }
    }//GEN-LAST:event_mnCopiarNFacActionPerformed

    private void mnPropietarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPropietarioActionPerformed
        // TODO add your handling code here:
        try {

            int row = this.tbReceips.getSelectedRow();

            if (row > -1) {

                String clave = this.tbReceips.getValueAt(row, 13).toString();
                Receips r = Receips.getReceipByClave(clave, receipList);
                if (r.getPropietario().equals("SA")) {
                    JOptionPane.showMessageDialog(null, "Esta factura no está asignada");

                } else {
                    JOptionPane.showMessageDialog(null, "Esta factura fue asiganda por " + r.getPropietario());
                }
            }
        } catch (Exception e) {
            System.out.println("view.MantenimientoAsientosConta.mnPropietarioActionPerformed() error " + e.getMessage());
        }
    }//GEN-LAST:event_mnPropietarioActionPerformed

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

    private void btnCalculatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculatorActionPerformed
        // TODO add your handling code here:
        try {
            Process p = Runtime.getRuntime().exec("calc.exe");
            p.waitFor();
            p.destroy();

        } catch (Exception e) {
            System.out.println("view.MantenimientoAsientosConta.btnCalculatorActionPerformed() error " + e.getMessage());
        }
    }//GEN-LAST:event_btnCalculatorActionPerformed

    private void btnUpdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdownActionPerformed
        // TODO add your handling code here:
        java.awt.Dimension dimention = jPanel6.getSize();
        if (dimention.getHeight() > 32) {
            java.awt.Dimension d = new java.awt.Dimension(1011, 32);
            jPanel6.setPreferredSize(d);
            System.out.println("view.MantenimientoAsientosConta.btnUpdownActionPerformed() height changed");
            jPanel6.revalidate();
            jPanel6.repaint();
        } else {
            java.awt.Dimension d = new java.awt.Dimension(1011, 250);
            jPanel6.setPreferredSize(d);
            System.out.println("view.MantenimientoAsientosConta.btnUpdownActionPerformed() height changed");
            jPanel6.revalidate();
            jPanel6.repaint();
        }
    }//GEN-LAST:event_btnUpdownActionPerformed
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

    private void openTbReceipPDF() {
        try {
            int row = this.tbReceips.getSelectedRow();
            if (row > -1) {
                String clave = tbReceips.getValueAt(row, 13).toString();
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
            System.out.println("view.MantenimientoAsientosConta.openTbReceipPDF() error " + e.getMessage());

        }
    }

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

    private void loadReceipts() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        this.jdtFin.setEnabled(false);
        this.btnCargarFacturas.setEnabled(false);
        try {
            loadingInfo = true;
            this.jProgressBar1.setVisible(true);
            this.jProgressBar1.setString("Cargando...");
            this.jProgressBar1.setMaximum(100);
            this.jProgressBar1.setValue(50);
            this.txtBuscarConsecutivo.setText("");
            limpiarTabla(tbReceips);
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
            String moneda = cmbMoneda.getSelectedItem().toString();
            if (moneda.equals("Todas")) {
                moneda = "";
            }
            //cmbProveedor.removeAllItems();
            boolean ciaDesconocida = cia.equalsIgnoreCase("Cedulas desconocidas") ? true : false;
            receipList = data.CrudFacturaElectronica.obtenerFacturaElectronicaView(
                    soc == null ? "" : soc.getCedula(),
                    provCed, registrado, asignadas, inicio, fin, ciaDesconocida, moneda);
            data.CrudCorreoExcluidoFE ce = new data.CrudCorreoExcluidoFE();
            listaCorreosOmitidos = ce.obtenerCorreosExclidosPorFecha(inicio, fin);
            listaAsientos = crAsientos.obtenerAsientos(inicio, fin);
            checkAsientos();
            loadTbReceps(this.receipList);
            this.lbTbMantResumen.setText("Filas: " + tbReceips.getRowCount());
            loadDetalles(soc == null ? "" : soc.getCedula(), provCed, registradas, asignadas, inicio, fin, ciaDesconocida);
            mostrarResumenEstadoFacturas();
            loadingInfo = false;

        } catch (Exception e) {
            System.err.println("view.MantenimientoCuentaFact.loadReceipts() error " + e.getMessage());
        }
        this.jdtFin.setEnabled(true);
        this.btnCargarFacturas.setEnabled(true);
        setDateChooserLook();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }

    private void loadDetalles(String cedCia, String cedProv, int registradas, int asignadas, java.util.Date fechaInicio, java.util.Date fechaFin, boolean ciaDesconocidas) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (cmbMoneda.getSelectedItem().toString().equals("USD")) {
                    lbDetResu.setText("Detalles de facturas en dólares");
                } else {
                    lbDetResu.setText("Detalles de facturas en colones");
                }
                txtTotalCompras.setText("0.0");
                txtTotatIVA.setText("0.0");
                txtTotalComprasPlusIV.setText("0.0");
                txtTotalComprasPlusIVPlusOtros.setText("0.0");
                String moneda = cmbMoneda.getSelectedItem().toString();
                double ttCompras = 0;
                double ttComprasIV = 0;
                double ttImpuesto = 0;
                double ttComprobante = 0;
                String exactus = cmbEstadoRegistro.getSelectedItem().toString();
                CrudResumenTotalesDetallesFacturaE cr = new CrudResumenTotalesDetallesFacturaE();
                listaResumenDetallesFactura = cr.ObtResumenTotalesFacturaElectronicaPorFecha(cedCia, cedProv, registradas, asignadas, fechaInicio, fechaFin, ciaDesconocidas, moneda, exactus.equals("Todas"));
                ArrayList<DetallesLineasFactura> resumenDetallesLineas = cr.ObtResumenTotalesDetallesLineasFacturaPorFecha(cedCia, cedProv, registradas, asignadas, fechaInicio, fechaFin, ciaDesconocidas, moneda, exactus.equals("Todas"));
                setResumenCompras(resumenDetallesLineas);
                setResumenImpuestos(resumenDetallesLineas);
                for (ResumenTotalesDetallesFacturaE re : listaResumenDetallesFactura) {
                    if (moneda.equals("USD") && re.getCodigoMoneda().equals("USD")) {
                        ttCompras += re.getTotalMercanciasGravadas() + re.getTotalMercanciasExentas();
                        ttComprasIV += ttCompras + re.getTotalImpuesto();
                        ttImpuesto += re.getTotalImpuesto();
                        ttComprobante += re.getTotalComprobante();
                        txtTotalCompras.setText(AppStaticValues.numberFormater.format(ttCompras));
                        txtTotatIVA.setText(AppStaticValues.numberFormater.format(ttImpuesto));
                        txtTotalComprasPlusIV.setText(AppStaticValues.numberFormater.format(ttComprasIV));
                        txtTotalComprasPlusIVPlusOtros.setText(AppStaticValues.numberFormater.format(ttComprobante));
                    } else if (!moneda.equals("USD")) {
                        ttCompras += re.getTotalMercanciasGravadas() + re.getTotalMercanciasExentas();
                        ttComprasIV += ttCompras + re.getTotalImpuesto();
                        ttImpuesto += re.getTotalImpuesto();
                        ttComprobante += re.getTotalComprobante();
                        txtTotalCompras.setText(AppStaticValues.numberFormater.format(ttCompras));
                        txtTotatIVA.setText(AppStaticValues.numberFormater.format(ttImpuesto));
                        txtTotalComprasPlusIV.setText(AppStaticValues.numberFormater.format(ttComprasIV));
                        txtTotalComprasPlusIVPlusOtros.setText(AppStaticValues.numberFormater.format(ttComprobante));
                    }
                }

            }
        };
        Thread t = new Thread(r);
        t.start();

    }

    private void setResumenCompras(ArrayList<DetallesLineasFactura> resumenDetallesLineas) {
        String moneda = cmbMoneda.getSelectedItem().toString();
        txtComp1p.setText("0.0");
        txtComp4p.setText("0.0");
        txtComp8P.setText("0.0");
        txtComp13P.setText("0.0");
        double c0p = 0.0;
        double c05p = 0.0;
        double c1p = 0.0;
        double c4p = 0.0;
        double c8p = 0.0;
        double c13p = 0.0;
        for (DetallesLineasFactura e : resumenDetallesLineas) {

            if (moneda.equals("USD") && e.getCodigomoneda().equals("USD")) {
                if (e.getTarifaimp() == 0.0) {
                    c0p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 0.5) {
                    c05p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 1.0) {
                    c1p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 4.0) {
                    c4p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 8.0) {
                    c8p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 13.0) {
                    c13p += e.getSumaMontoTotalLineas();
                }
            } else if (!moneda.equals("USD")) {
                if (e.getTarifaimp() == 0.0) {
                    c0p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 0.5) {
                    c05p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 1.0) {
                    c1p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 4.0) {
                    c4p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 8.0) {
                    c8p += e.getSumaMontoTotalLineas();
                }
                if (e.getTarifaimp() == 13.0) {
                    c13p += e.getSumaMontoTotalLineas();
                }
            }
            /*JOptionPane.showMessageDialog(null, "total facturas " + e.getTotalLineas() + "\n"
                    + "sumaMontoTotalLineas " + e.getSumaMontoTotalLineas() + "\n"
                    + "sumaMontoTotalDesc " + e.getSumaMontoTotalDesc() + "\n"
                    + "sumaSubTotal " + e.getSumaSubTotal() + "\n"
                    + "sumaMontoImpuestoNeto " + e.getSumaMontoImpuestoNeto() + "\n"
                    + "calcImpuestoNeto " + e.getCalcImpuestoNeto() + "\n"
                    + "tarifaimp " + e.getTarifaimp() + "\n"
                    + "codigomoneda " + e.getCodigomoneda());*/
        }
        txtComp0p.setText(logic.AppStaticValues.numberFormater.format(c0p));
        txtComp05p.setText(logic.AppStaticValues.numberFormater.format(c05p));
        txtComp1p.setText(logic.AppStaticValues.numberFormater.format(c1p));
        txtComp4p.setText(logic.AppStaticValues.numberFormater.format(c4p));
        txtComp8P.setText(logic.AppStaticValues.numberFormater.format(c8p));
        txtComp13P.setText(logic.AppStaticValues.numberFormater.format(c13p));

    }

    private void setResumenImpuestos(ArrayList<DetallesLineasFactura> resumenDetallesLineas) {
        String moneda = cmbMoneda.getSelectedItem().toString();
        txtIm1p.setText("0.0");
        txtIm4p.setText("0.0");
        txtIm8P.setText("0.0");
        txtIm13P.setText("0.0");
        double i05p = 0.0;
        double i1p = 0.0;
        double i4p = 0.0;
        double i8p = 0.0;
        double i13p = 0.0;
        for (DetallesLineasFactura e : resumenDetallesLineas) {

            if (moneda.equals("USD") && e.getCodigomoneda().equals("USD")) {
                if (e.getTarifaimp() == 0.5) {
                    i05p += e.getSumaMontoImpuestoNeto();
                }
                if (e.getTarifaimp() == 1.0) {
                    i1p += e.getSumaMontoImpuestoNeto();
                }
                if (e.getTarifaimp() == 4.0) {
                    i4p += e.getSumaMontoImpuestoNeto();
                }
                if (e.getTarifaimp() == 8.0) {
                    i8p += e.getSumaMontoImpuestoNeto();
                }
                if (e.getTarifaimp() == 13.0) {
                    i13p += e.getSumaMontoImpuestoNeto();
                }
            } else {
                if (e.getTarifaimp() == 0.5) {
                    i05p += e.getSumaMontoImpuestoNeto();
                }
                if (e.getTarifaimp() == 1.0) {
                    i1p += e.getSumaMontoImpuestoNeto();
                }
                if (e.getTarifaimp() == 4.0) {
                    i4p += e.getSumaMontoImpuestoNeto();
                }
                if (e.getTarifaimp() == 8.0) {
                    i8p += e.getSumaMontoImpuestoNeto();
                }
                if (e.getTarifaimp() == 13.0) {
                    i13p += e.getSumaMontoImpuestoNeto();
                }
            }
            /*JOptionPane.showMessageDialog(null, "total facturas " + e.getTotalLineas() + "\n"
                    + "sumaMontoTotalLineas " + e.getSumaMontoTotalLineas() + "\n"
                    + "sumaMontoTotalDesc " + e.getSumaMontoTotalDesc() + "\n"
                    + "sumaSubTotal " + e.getSumaSubTotal() + "\n"
                    + "sumaMontoImpuestoNeto " + e.getSumaMontoImpuestoNeto() + "\n"
                    + "calcImpuestoNeto " + e.getCalcImpuestoNeto() + "\n"
                    + "tarifaimp " + e.getTarifaimp() + "\n"
                    + "codigomoneda " + e.getCodigomoneda());*/
        }
        txtIm05p.setText(logic.AppStaticValues.numberFormater.format(i05p));
        txtIm1p.setText(logic.AppStaticValues.numberFormater.format(i1p));
        txtIm4p.setText(logic.AppStaticValues.numberFormater.format(i4p));
        txtIm8P.setText(logic.AppStaticValues.numberFormater.format(i8p));
        txtIm13P.setText(logic.AppStaticValues.numberFormater.format(i13p));

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
        data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE crdetalle = new CrudResumenTotalesDetallesFacturaE();
        boolean res = crdetalle.updateLinesExactus();
        if (res) {
            System.out.println("el campo exactus de las lineas se ha actualizado");
        } else {
            System.out.println("el campo exactus de las lineas se ha actualizado");
        }
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

    /**
     * this method loads the receip info to the tbreceip so it can be accessible
     * to the user
     */
    private void loadTbReceps(ArrayList<Receips> lista) {

        DefaultTableModel model = (DefaultTableModel) this.tbReceips.getModel();
        int max = receipList.size();
        this.jProgressBar1.setValue(0);
        this.jProgressBar1.setMaximum(max);
        this.jProgressBar1.setString("Cargando elementos...");

        lista.forEach(rec -> {

            String formattedDate = AppStaticValues.dateFormat.format(rec.getFechaEmision());
            //Impuesto imp = //e.getImpuesto();
            model.addRow(new Object[]{
                formattedDate, //rec.getFechaEmision(),
                rec.getNumeroConsecutivo(),
                rec.getEmisor().getNombre(),
                "",//e.getDetalle(),//rec.getDetalleServicio().getDetalle(),
                "",//imp == null ? 0 : imp.getTarifa(),
                "",//AppStaticValues.numberFormater.format(imp == null ? 0 : imp.getMonto()),
                "",//AppStaticValues.numberFormater.format(e.getMontoTotalLinea() - (imp == null ? 0 : imp.getMonto())),//rec.getResumenFactura().getTotalVentaNeta(),
                rec.getResumenFactura().getCodigoMoneda().getCodigoMoneda()
                + " " + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalOtrosCargos()),
                rec.getResumenFactura().getCodigoMoneda().getCodigoMoneda()
                + " " + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalImpuesto()),
                rec.getResumenFactura().getCodigoMoneda().getCodigoMoneda()
                + " " + AppStaticValues.numberFormater.format(rec.getResumenFactura().getTotalComprobante()),
                rec.isBienes(),
                rec.getExactus(),
                rec.getResumenFactura().getCodigoMoneda().getCodigoMoneda()
                + " " + AppStaticValues.numberFormater.format(rec.getDifereniciaXmlExactus()),
                rec.getClave()
            });
            jProgressBar1.setValue(jProgressBar1.getValue() + 1);
        });
        CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalculator;
    private javax.swing.JButton btnCargarFacturas;
    private javax.swing.JButton btnCorreosOmitidos;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnHideRow;
    private javax.swing.JButton btnOpenPdf;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnUpdown;
    private javax.swing.JComboBox<String> cmbEstadoAsignacion;
    private javax.swing.JComboBox<String> cmbEstadoRegistro;
    private javax.swing.JComboBox<String> cmbMoneda;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JComboBox<String> cmbSociedades;
    private javax.swing.JComboBox<String> cmbTipoFactura;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdtFin;
    private com.toedter.calendar.JDateChooser jdtInicio;
    private javax.swing.JLabel lbDetResu;
    private javax.swing.JLabel lbPendientes;
    private javax.swing.JLabel lbRegistradas;
    private javax.swing.JLabel lbTbMantResumen;
    private javax.swing.JLabel lbTotalFacturas;
    private javax.swing.JMenuItem menuAbrirPDF;
    private javax.swing.JMenuItem mnCopiarNFac;
    private javax.swing.JMenuItem mnPropietario;
    private javax.swing.JMenuItem mnuUpdateExactus;
    private javax.swing.JPanel north;
    private javax.swing.JPopupMenu popUpTbReceips;
    private javax.swing.JTable tbReceips;
    private javax.swing.JTextField txtBuscarConsecutivo;
    private javax.swing.JTextField txtComp05p;
    private javax.swing.JTextField txtComp0p;
    private javax.swing.JTextField txtComp13P;
    private javax.swing.JTextField txtComp1p;
    private javax.swing.JTextField txtComp4p;
    private javax.swing.JTextField txtComp8P;
    private javax.swing.JTextField txtIm05p;
    private javax.swing.JTextField txtIm13P;
    private javax.swing.JTextField txtIm1p;
    private javax.swing.JTextField txtIm4p;
    private javax.swing.JTextField txtIm8P;
    private javax.swing.JTextField txtTotalCompras;
    private javax.swing.JTextField txtTotalComprasPlusIV;
    private javax.swing.JTextField txtTotalComprasPlusIVPlusOtros;
    private javax.swing.JTextField txtTotatIVA;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JTextFieldDateEditor;
import data.CrudPresupuesto;
import entitys.Departamento;
import entitys.Presupuesto;
import entitys.Sociedad;
import entitys.SubtiposExactus;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import logic.AppLogger;
import logic.AppStaticValues;
import static logic.AppStaticValues.numberFormater;
import services.SimpleExcelWriter;
import static view.util.JTableCommonFunctions.limpiarTabla;

/**
 *
 * @author eobregon
 */
public class AdministracionSubtiposFacturas extends javax.swing.JPanel {

    /**
     * Creates new form AdministracionSubtiposFacturas
     */
    ArrayList<Departamento> departamentos;
    ArrayList<Sociedad> sociedades;
    ArrayList<Presupuesto> listaPresupuesto;
    ArrayList<SubtiposExactus> listaSubtipos;
    boolean loadingInfo = false;
    data.CrudPresupuesto crudp;

    public AdministracionSubtiposFacturas() {
        initComponents();
        this.crudp = new CrudPresupuesto();
        this.listaSubtipos = new ArrayList<>();
        prepareGui();
    }

    private void prepareGui() {
        loadInfo();
        setListeners();
        setView();
    }

    private void setListeners() {

        tbMantSiubt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = tbMantSiubt.getSelectedRow();
                    if (selectedRow != -1) {
                        //prepareChanges();
                        // Your code logic here for the selected row
                    }
                }
            }
        });
        tbMantSiubt.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tbMantSiubt.rowAtPoint(e.getPoint());
                    tbMantSiubt.addRowSelectionInterval(row, row);
                    jPopupMenu1.show(tbMantSiubt, e.getX(), e.getY());
                }
            }
        });
        this.txtBuscarConsecutivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFacturas();
            }
        });
    }

    private void buscarFacturas() {
        String factura = txtBuscarConsecutivo.getText();
        if (!factura.isEmpty()) {
            ArrayList<SubtiposExactus> lista = SubtiposExactus.getSubListTipoPorFactura(factura, listaSubtipos);
            loadingInfo = true;
            limpiarTabla(tbMantSiubt);
            DefaultTableModel model = (DefaultTableModel) tbMantSiubt.getModel();

            for (SubtiposExactus e : lista) {
                addRowTbSubtipo(e, model);
            }
            lbTbMantResumen.setText("Filas: " + tbMantSiubt.getRowCount());
            loadingInfo = false;
        } else {
            Runnable r = new Runnable() {
                @Override
                public void run() {

                    obtenerSubtipos();

                }
            };
            Thread t = new Thread(r);
            t.start();
        }

    }

    private void loadInfo() {
        loadingInfo = true;
        Sociedad s = new Sociedad();
        this.sociedades = s.quemarSociedades();
        data.crudDepartamento crd = new data.crudDepartamento();

        this.departamentos = crd.getSqlDepartamentos();
        Departamento din = new Departamento();
        din.setDEPARTAMENTO("99");
        din.setDescripcion("Ingresos");
        din.setJEFE("");
        departamentos.add(din);
        loadingInfo = false;
    }

    private void prepareChanges() {
        if (!loadingInfo) {
            try {
                loadingInfo = true;
                cmbCta.removeAllItems();
                int row = tbMantSiubt.getSelectedRow();
                String cta = tbMantSiubt.getValueAt(row, 3).toString();
                String depDesc = cta.length() == 11 ? cta.substring(3, 5) : cta.substring(0, 2);
                Departamento d = Departamento.getDepartamentoByCodDepa(departamentos, depDesc);
                listaPresupuesto = crudp.obtenerPresupuestoPorDep(d.getDEPARTAMENTO());
                Presupuesto p = new Presupuesto();
                p = p.getPresupuestoPorCtaGeneral(cta.length() == 11 ? cta : "01-" + cta, listaPresupuesto);
                if (p == null && cta.startsWith("99-02-")) {
                    p = new Presupuesto();
                    p = p.getPresupuestoPorCtaGeneral(cta.length() == 11 ? cta : "02-" + cta, listaPresupuesto);
                }
                if (p != null) {
                    listaPresupuesto.forEach(l -> {

                        if (l.getCTAPRESUPUESTO().endsWith("00")) {
                            cmbCta.addItem(l.getCTAPRESUPUESTO() + "-" + l.getCONCEPATOADETALLE().toUpperCase());
                        } else {
                            cmbCta.addItem("     " + l.getCTAPRESUPUESTO() + "-" + l.getCONCEPATOADETALLE().toUpperCase());
                        }

                    });
                    cmbCta.setSelectedItem("     " + p.getCTAPRESUPUESTO() + "-" + p.getCONCEPATOADETALLE().toUpperCase());
                    cmbDep.setSelectedItem(d.getDescripcion());
                }
                loadingInfo = false;
            } catch (Exception e) {
                System.out.println("view.AdministracionSubtiposFacturas.prepareChanges() error " + e.getMessage());
            }
        }
    }

    private void setView() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        tbMantSiubt.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
        tbMantSiubt.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
        setDateChooserBackground();
        this.jProgressBar1.setVisible(false);
        tbMantSiubt.getColumnModel().getColumn(5).setCellRenderer(AppStaticValues.rightRenderer);
        loadingInfo = true;
        this.departamentos.forEach(e -> {
            this.cmbDepartamentos.addItem(e.getDescripcion());
            this.cmbDep.addItem(e.getDescripcion());
        });
        loadingInfo = false;
        setTbListeners();
    }

    private void setDateChooserBackground() {
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

    private void setTbListeners() {
        this.tbMantSiubt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if it's a double click event
                JTable table = (JTable) e.getSource();
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);

                if (e.getClickCount() == 2 && table.getSelectedRow() > -1) {
                    //JOptionPane.showMessageDialog(null, "fila seleccionada");

                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }
        });
    }

    /**
     * this function returns a jcombobox containing the days of the week. this
     * jcombobox has a event itemStateChanged listenter, witch will mark the
     * selected day in green on the associated day on the tbPlanilla cell
     */
    private void getCmbCuentas(ArrayList<Presupuesto> ctaP) {
        cmbCta.removeAllItems();
        for (Presupuesto d : ctaP) {
            if (d.getCTAPRESUPUESTO().endsWith("00")) {
                cmbCta.addItem(d.getCTAPRESUPUESTO() + "-" + d.getCONCEPATOADETALLE().toUpperCase());
            } else {
                cmbCta.addItem("       " + d.getCTAPRESUPUESTO() + "-" + d.getCONCEPATOADETALLE());
            }

        }
        cmbCta.setSelectedItem("");

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
        mnDefault = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbMantSiubt = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        btnExcel = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbDep = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cmbCta = new javax.swing.JComboBox<>();
        btnSaveChanges = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        lbTbMantResumen = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jdtInicio = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jdtFin = new com.toedter.calendar.JDateChooser();
        btnCargarFacturas = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cmbSociedades = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jPanel19 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbDepartamentos = new javax.swing.JComboBox<>();
        jPanel20 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cmbFiltCuenta = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        btnRefrescarFiltros = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtBuscarConsecutivo = new javax.swing.JTextField();
        btnBuscarNumero = new javax.swing.JButton();

        mnDefault.setText("Asignar factura por defecto");
        mnDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDefaultActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mnDefault);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(204, 204, 204)), "Tabla Administración de Cuentas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        tbMantSiubt.setAutoCreateRowSorter(true);
        tbMantSiubt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SOCIEDAD", "TIPO", "SUBTIPO", "Cta Presup", "PROVEEDOR", "DOCUMENTO", "FECHA", "APLICACION", "MONEDA", "MONTO", "DOLARES", "ASIENTO", "idRow", "Asignar", "TipoCuenta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbMantSiubt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbMantSiubt.setShowGrid(true);
        tbMantSiubt.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbMantSiubt);
        if (tbMantSiubt.getColumnModel().getColumnCount() > 0) {
            tbMantSiubt.getColumnModel().getColumn(0).setPreferredWidth(100);
            tbMantSiubt.getColumnModel().getColumn(1).setPreferredWidth(60);
            tbMantSiubt.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbMantSiubt.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbMantSiubt.getColumnModel().getColumn(4).setPreferredWidth(300);
            tbMantSiubt.getColumnModel().getColumn(5).setPreferredWidth(120);
            tbMantSiubt.getColumnModel().getColumn(6).setPreferredWidth(90);
            tbMantSiubt.getColumnModel().getColumn(7).setPreferredWidth(300);
            tbMantSiubt.getColumnModel().getColumn(9).setPreferredWidth(100);
            tbMantSiubt.getColumnModel().getColumn(10).setPreferredWidth(120);
            tbMantSiubt.getColumnModel().getColumn(11).setPreferredWidth(100);
            tbMantSiubt.getColumnModel().getColumn(12).setMinWidth(0);
            tbMantSiubt.getColumnModel().getColumn(12).setPreferredWidth(0);
            tbMantSiubt.getColumnModel().getColumn(12).setMaxWidth(0);
            tbMantSiubt.getColumnModel().getColumn(14).setMinWidth(0);
            tbMantSiubt.getColumnModel().getColumn(14).setPreferredWidth(0);
            tbMantSiubt.getColumnModel().getColumn(14).setMaxWidth(0);
        }
        tbMantSiubt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jPanel11.add(jScrollPane1);

        jPanel1.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel12.setPreferredSize(new java.awt.Dimension(970, 35));
        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout2.setAlignOnBaseline(true);
        jPanel12.setLayout(flowLayout2);

        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/excel_icon_20x20.png"))); // NOI18N
        btnExcel.setToolTipText("Exportar tabla a excel");
        btnExcel.setMaximumSize(new java.awt.Dimension(26, 30));
        btnExcel.setMinimumSize(new java.awt.Dimension(26, 30));
        btnExcel.setPreferredSize(new java.awt.Dimension(30, 30));
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });
        jPanel12.add(btnExcel);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Departamento");
        jPanel10.add(jLabel6);

        cmbDep.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Factura" }));
        cmbDep.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepItemStateChanged(evt);
            }
        });
        jPanel10.add(cmbDep);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Cuenta");
        jPanel10.add(jLabel7);

        cmbCta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Factura", "Dejar en blanco" }));
        jPanel10.add(cmbCta);

        btnSaveChanges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-save-25.png"))); // NOI18N
        btnSaveChanges.setToolTipText("Guardar cambios");
        btnSaveChanges.setPreferredSize(new java.awt.Dimension(31, 27));
        btnSaveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveChangesActionPerformed(evt);
            }
        });
        jPanel10.add(btnSaveChanges);

        jPanel12.add(jPanel10);

        jPanel1.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel13.setPreferredSize(new java.awt.Dimension(1026, 30));
        java.awt.FlowLayout flowLayout4 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout4.setAlignOnBaseline(true);
        jPanel13.setLayout(flowLayout4);

        lbTbMantResumen.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lbTbMantResumen.setText("Filas: 0");
        jPanel13.add(lbTbMantResumen);

        jPanel1.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setPreferredSize(new java.awt.Dimension(400, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1324, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(10, 100));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );

        add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setPreferredSize(new java.awt.Dimension(10, 100));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 448, Short.MAX_VALUE)
        );

        add(jPanel5, java.awt.BorderLayout.LINE_START);

        jPanel15.setPreferredSize(new java.awt.Dimension(1058, 120));
        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(1022, 80));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout1.setAlignOnBaseline(true);
        jPanel2.setLayout(flowLayout1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Inicio");
        jPanel6.add(jLabel1);

        jdtInicio.setDateFormatString("dd-MM-yyyy");
        jdtInicio.setPreferredSize(new java.awt.Dimension(120, 25));
        jPanel6.add(jdtInicio);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Fin");
        jPanel6.add(jLabel2);

        jdtFin.setDateFormatString("dd-MM-yyyy");
        jdtFin.setPreferredSize(new java.awt.Dimension(120, 25));
        jdtFin.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdtFinPropertyChange(evt);
            }
        });
        jPanel6.add(jdtFin);

        btnCargarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-start-30.png"))); // NOI18N
        btnCargarFacturas.setToolTipText("Cargar facturas");
        btnCargarFacturas.setPreferredSize(new java.awt.Dimension(30, 30));
        btnCargarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarFacturasActionPerformed(evt);
            }
        });
        jPanel6.add(btnCargarFacturas);

        jPanel2.add(jPanel6);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Cia");
        jPanel7.add(jLabel3);

        cmbSociedades.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todas", "RYMSA", "CILT", "IRASA", "KATRA", "OPILOG", "TURINTEL" }));
        cmbSociedades.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSociedadesItemStateChanged(evt);
            }
        });
        jPanel7.add(cmbSociedades);

        jPanel2.add(jPanel7);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Proveedor");
        jPanel9.add(jLabel5);

        cmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbProveedor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbProveedorItemStateChanged(evt);
            }
        });
        jPanel9.add(cmbProveedor);

        jPanel2.add(jPanel9);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Departamento");
        jPanel8.add(jLabel4);

        cmbDepartamentos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbDepartamentos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDepartamentosItemStateChanged(evt);
            }
        });
        jPanel8.add(cmbDepartamentos);

        jPanel19.add(jPanel8);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Cuenta");
        jPanel20.add(jLabel9);

        cmbFiltCuenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        cmbFiltCuenta.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbFiltCuentaItemStateChanged(evt);
            }
        });
        jPanel20.add(cmbFiltCuenta);

        jPanel19.add(jPanel20);

        jPanel2.add(jPanel19);

        btnRefrescarFiltros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/updateIcon.png"))); // NOI18N
        btnRefrescarFiltros.setToolTipText("Refrescar filtros");
        btnRefrescarFiltros.setPreferredSize(new java.awt.Dimension(27, 27));
        btnRefrescarFiltros.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnRefrescarFiltrosItemStateChanged(evt);
            }
        });
        btnRefrescarFiltros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarFiltrosActionPerformed(evt);
            }
        });
        jPanel14.add(btnRefrescarFiltros);

        jPanel2.add(jPanel14);

        jPanel15.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel16.setPreferredSize(new java.awt.Dimension(1058, 20));
        jPanel16.setLayout(new java.awt.GridLayout(1, 0));

        jProgressBar1.setOpaque(true);
        jProgressBar1.setStringPainted(true);
        jPanel16.add(jProgressBar1);

        jPanel15.add(jPanel16, java.awt.BorderLayout.PAGE_START);

        jPanel17.setMinimumSize(new java.awt.Dimension(600, 40));
        jPanel17.setPreferredSize(new java.awt.Dimension(1058, 30));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0);
        flowLayout3.setAlignOnBaseline(true);
        jPanel17.setLayout(flowLayout3);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Factura #");
        jPanel18.add(jLabel8);

        txtBuscarConsecutivo.setMinimumSize(new java.awt.Dimension(100, 22));
        txtBuscarConsecutivo.setPreferredSize(new java.awt.Dimension(150, 25));
        jPanel18.add(txtBuscarConsecutivo);

        btnBuscarNumero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon_all_20x20.png"))); // NOI18N
        btnBuscarNumero.setToolTipText("Buscar factura por número");
        btnBuscarNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarNumeroActionPerformed(evt);
            }
        });
        jPanel18.add(btnBuscarNumero);

        jPanel17.add(jPanel18);

        jPanel15.add(jPanel17, java.awt.BorderLayout.PAGE_END);

        add(jPanel15, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jdtFinPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdtFinPropertyChange
        // TODO add your handling code here:

        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                cmbProveedor.setSelectedIndex(0);
                cmbDep.setSelectedIndex(0);
                cmbFiltCuenta.setSelectedIndex(0);
                loadingInfo = false;
                obtenerSubtipos();
                loadCmbFiltCtas();
                loadCmbProvCtas();

            }
        };
        Thread t = new Thread(r);
        t.start();


    }//GEN-LAST:event_jdtFinPropertyChange
    private void loadCmbFiltCtas() {
        loadingInfo = true;
        ArrayList<String> lista = new ArrayList<>();
        this.cmbFiltCuenta.removeAllItems();
        this.cmbFiltCuenta.addItem("Todos");
        for (SubtiposExactus l : listaSubtipos) {
            if (!lista.contains(l.getDESCRIPCION())) {
                lista.add(l.getDESCRIPCION());
            }
        }
        Collections.sort(lista);
        lista.forEach(e -> {
            cmbFiltCuenta.addItem(e);
        });
        loadingInfo = false;
    }

    private void loadCmbProvCtas() {
        loadingInfo = true;
        ArrayList<String> lista = new ArrayList<>();
        this.cmbProveedor.removeAllItems();
        this.cmbProveedor.addItem("Todos");
        for (SubtiposExactus l : listaSubtipos) {
            if (!lista.contains(l.getNombre_Proveedor())) {
                lista.add(l.getNombre_Proveedor());
            }
        }
        Collections.sort(lista);
        lista.forEach(e -> {
            cmbProveedor.addItem(e);
        });
        loadingInfo = false;
    }

    /**
     * this method calls database to obtain the information that is requested by
     * the user, then it puts the founded information to the GUI
     */
    private void obtenerSubtipos() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        java.util.Date inicio = jdtInicio.getDate();
        java.util.Date fin = jdtFin.getDate();
        txtBuscarConsecutivo.setText("");
        loadingInfo = true;
        if (inicio != null && fin != null) {

            limpiarTabla(tbMantSiubt);
            String desc = cmbDepartamentos.getSelectedItem().toString();
            Departamento d = Departamento.getDepartamentoByStringIdDesc(departamentos, desc);
            String cia = cmbSociedades.getSelectedItem().toString();
            if (cia.equals("Todas")) {
                cia = "";
            }
            String prov = cmbProveedor.getSelectedItem().toString();
            if (prov.equals("Todos")) {
                prov = "";
            }
            String cta = cmbFiltCuenta.getSelectedItem().toString();
            if (cta.equals("Todos")) {
                cta = "";
            }
            data.CRUDSubtiposExactus cex = new data.CRUDSubtiposExactus();
            this.listaSubtipos = cex.obtenerSubtiposCP(inicio, fin, cia, prov, d == null ? "" : d.getDEPARTAMENTO(), cta);// cuentas por pagar
            ArrayList<SubtiposExactus> cb = cex.obtenerSubtiposCB(inicio, fin, cia, prov, cta);
            cb.forEach(e -> {

                listaSubtipos.add(e);
            });
            DefaultTableModel model = (DefaultTableModel) this.tbMantSiubt.getModel();
            int count = 0;
            for (SubtiposExactus e : listaSubtipos) {
                addRowTbSubtipo(e, model);

                jProgressBar1.setValue(count + 1);
                jProgressBar1.setValue(count + 1);
                count++;
            }
        }
        lbTbMantResumen.setText("Filas: " + tbMantSiubt.getRowCount());
        loadingInfo = false;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);

    }

    /**
     * this method add a row to the table model received as a parameter, this
     * with the information given in the SubtiposExactus rec also receive as a
     * parameter
     */
    private void addRowTbSubtipo(SubtiposExactus rec, DefaultTableModel model) {
        model.addRow(new Object[]{
            rec.getSociedad(),
            rec.getTIPO(),
            rec.getSUBTIPO(),
            rec.getDESCRIPCION(),
            rec.getNombre_Proveedor(),
            rec.getDOCUMENTO(),
            rec.getFECHA_DOCUMENTO(),
            rec.getAPLICACION(),
            rec.getMoneda(),
            numberFormater.format(rec.getMONTO()),
            numberFormater.format(rec.getMONTO_DOLAR()),
            rec.getASIENTO(),
            rec.getIdRow(),
            false,
            rec.getTipoCuenta()
        });
    }

    private javax.swing.JComboBox getCmbCtas(ArrayList<Presupuesto> lista) {
        javax.swing.JComboBox cmb = new javax.swing.JComboBox<>();
        lista.forEach(e -> {

            cmb.addItem(e.getCTAPRESUPUESTO() + "-" + e.getCONCEPATOADETALLE());
        });
        return cmb;
    }
    private void btnCargarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarFacturasActionPerformed
        // TODO add your handling code here:
        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                cmbProveedor.setSelectedIndex(0);
                cmbFiltCuenta.setSelectedIndex(0);
                loadingInfo = false;
                obtenerSubtipos();
                loadCmbFiltCtas();
                loadCmbProvCtas();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_btnCargarFacturasActionPerformed

    private void cmbSociedadesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSociedadesItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    loadingInfo = true;
                    cmbProveedor.setSelectedIndex(0);
                    cmbFiltCuenta.setSelectedIndex(0);
                    loadingInfo = false;
                    obtenerSubtipos();
                    loadCmbFiltCtas();
                    loadCmbProvCtas();
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }//GEN-LAST:event_cmbSociedadesItemStateChanged

    private void cmbProveedorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbProveedorItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    loadingInfo = true;
                    cmbDepartamentos.setSelectedIndex(0);
                    cmbFiltCuenta.setSelectedIndex(0);
                    loadingInfo = false;
                    obtenerSubtipos();
                    loadCmbFiltCtas();
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }//GEN-LAST:event_cmbProveedorItemStateChanged

    private void cmbDepartamentosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepartamentosItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        loadingInfo = true;
                        cmbFiltCuenta.setSelectedIndex(0);
                        loadingInfo = false;

                        obtenerSubtipos();
                        loadCmbFiltCtas();
                    }
                };
                Thread t = new Thread(r);
                t.start();
            }
        }
    }//GEN-LAST:event_cmbDepartamentosItemStateChanged
    private void setCmbCtaFilt() {
        try {
            loadingInfo = true;
            cmbFiltCuenta.removeAllItems();
            cmbFiltCuenta.addItem("Todos");
            /*String desc = cmbDepartamentos.getSelectedItem().toString();
            Departamento d = Departamento.getDepartamentoByStringIdDesc(departamentos, desc);

            listaPresupuesto = crudp.obtenerPresupuestoPorDep(d.getDEPARTAMENTO());
            listaPresupuesto.forEach(e -> {
                if (e.getCTAPRESUPUESTO().endsWith("00")) {
                    cmbFiltCuenta.addItem(e.getCTAPRESUPUESTO().trim() + "-" + e.getCONCEPATOADETALLE().trim());
                } else {
                    cmbFiltCuenta.addItem("   " + e.getCTAPRESUPUESTO().trim() + "-" + e.getCONCEPATOADETALLE().trim());
                }

            });*/
            ArrayList<String> lista = new ArrayList<>();

            lista.forEach(e -> {
                cmbFiltCuenta.addItem(e);
            });
            loadingInfo = false;
        } catch (Exception e) {
            System.out.println("view.AdministracionSubtiposFacturas.setCmbCtaFilt() error " + e.getMessage());
        }
    }

    private void btnRefrescarFiltrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarFiltrosActionPerformed
        // TODO add your handling code here:

        Runnable r = new Runnable() {
            @Override
            public void run() {
                loadingInfo = true;
                cmbDepartamentos.setSelectedIndex(0);
                cmbSociedades.setSelectedIndex(0);
                cmbProveedor.setSelectedIndex(0);
                cmbFiltCuenta.setSelectedIndex(0);
                loadingInfo = false;
                obtenerSubtipos();
                loadCmbFiltCtas();
                loadCmbProvCtas();
            }
        };
        Thread t = new Thread(r);
        t.start();

    }//GEN-LAST:event_btnRefrescarFiltrosActionPerformed

    private void cmbDepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDepItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            loadingInfo = true;
            cmbCta.removeAllItems();
            String depDesc = cmbDep.getSelectedItem().toString();
            if (depDesc.equals("Factura")) {
                cmbCta.addItem("Factura");
                cmbCta.addItem("Dejar en blanco");
            } else {
                Departamento d = Departamento.getDepartamentoByStringIdDesc(departamentos, depDesc);
                listaPresupuesto = crudp.obtenerPresupuestoPorDep(d.getDEPARTAMENTO());

                listaPresupuesto.forEach(l -> {

                    if (l.getCTAPRESUPUESTO().endsWith("00")) {
                        cmbCta.addItem(l.getCTAPRESUPUESTO() + "-" + l.getCONCEPATOADETALLE().toUpperCase());
                    } else {
                        cmbCta.addItem("     " + l.getCTAPRESUPUESTO() + "-" + l.getCONCEPATOADETALLE().toUpperCase());
                    }

                });
            }

            loadingInfo = false;
        }
    }//GEN-LAST:event_cmbDepItemStateChanged

    private void btnSaveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveChangesActionPerformed
        // TODO add your handling code here:
        Runnable r = new Runnable() {
            @Override
            public void run() {
                saveChanges();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_btnSaveChangesActionPerformed
    private void saveChanges() {
        int respuesta = JOptionPane.showConfirmDialog(null, "Desea ejecutar estos cambios de cuenta de presupuesto?", "Sistema Facturacion", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.NO_OPTION) {
            view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
            return;
        }
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i = 0; i < tbMantSiubt.getRowCount(); i++) {
            boolean flag = (boolean) tbMantSiubt.getValueAt(i, 13);
            if (flag) {
                lista.add(i);

            }
        }
        this.jProgressBar1.setValue(0);
        this.jProgressBar1.setVisible(true);
        this.jProgressBar1.setMaximum(lista.size());
        for (Integer i : lista) {
            saveChangesTbMantCuentas(i);
            jProgressBar1.setValue(i);
        }
        jProgressBar1.setString("Información guardada...");
        view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);

    }

    private void saveChangesTbMantCuentas(int row) {

        if (row > -1) {

            if (cmbCta.getSelectedItem().toString().equals("Factura")
                    || cmbCta.getSelectedItem().toString().equals("Dejar en blanco")) {
                String value = cmbCta.getSelectedItem().toString();
                boolean res = saveRow(row, value);
                if (res) {
                    String rowindx = tbMantSiubt.getValueAt(row, 12).toString();
                    int tipoCuenta = (int) tbMantSiubt.getValueAt(row, 14);
                    SubtiposExactus s = SubtiposExactus.getSubtipoPorIdRow(rowindx, tipoCuenta, listaSubtipos);
                    tbMantSiubt.setValueAt(s.getASIENTO().startsWith("CP") ? value : "", row, 3);
                    tbMantSiubt.setValueAt(false, row, 13);
                }
            } else {
                String selectedsubtipo = cmbCta.getSelectedItem().toString().trim().substring(0, 11);
                if (!selectedsubtipo.endsWith("00")) {
                    boolean res = saveRow(row);
                    if (res) {

                        tbMantSiubt.setValueAt(selectedsubtipo.substring(3, 11), row, 3);
                        tbMantSiubt.setValueAt(false, row, 13);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La cuenta seleccionada no es correcta");
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un registro");
        }
    }

    private boolean saveRow(int row) {
        boolean res = false;

        String rowindx = tbMantSiubt.getValueAt(row, 12).toString();
        int tipoCuenta = (int) tbMantSiubt.getValueAt(row, 14);
        SubtiposExactus s = SubtiposExactus.getSubtipoPorIdRow(rowindx, tipoCuenta, listaSubtipos);
        if (s != null) {
            String selectedsubtipo = cmbCta.getSelectedItem().toString().trim().substring(3, 11);
            AppLogger.appLogger.log(Level.WARNING, "view.AdministracionSubtiposFacturas.saveRow() "
                    + "\nsociedad " + s.getSociedad() + " row " + s.getIdRow() + " documento " + s.getDOCUMENTO() + " cuenta " + s.getDESCRIPCION() + " nueva cuenta " + selectedsubtipo);

            data.CRUDSubtiposExactus cs = new data.CRUDSubtiposExactus();
            res = s.getTipoCuenta() == 0 ? cs.actualizarSubtiposCP(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), selectedsubtipo)
                    : cs.actualizarSubtiposCB(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), selectedsubtipo);

            if (res) {
                s.setDESCRIPCION(selectedsubtipo);
                //JOptionPane.showMessageDialog(null, "Se ha actualizado el registro");
            } else {
                JOptionPane.showMessageDialog(null, "No se ha actualizado el registro");
            }
        }else{JOptionPane.showMessageDialog(null, "No se ha encontrado el subtipo");}

        return res;
    }

    private boolean saveRow(int row, String selectedsubtipo) {
        boolean res = false;

        String rowindx = tbMantSiubt.getValueAt(row, 12).toString();
        int tipoCuenta = (int) tbMantSiubt.getValueAt(row, 14);
        SubtiposExactus s = SubtiposExactus.getSubtipoPorIdRow(rowindx, tipoCuenta, listaSubtipos);
        if (s != null) {
            //String selectedsubtipo = cmbCta.getSelectedItem().toString().trim().substring(3, 11);
            AppLogger.appLogger.log(Level.WARNING, "view.AdministracionSubtiposFacturas.saveRow() "
                    + "\nsociedad " + s.getSociedad() + " row " + s.getIdRow() + " documento " + s.getDOCUMENTO() + " cuenta " + s.getDESCRIPCION() + " nueva cuenta " + selectedsubtipo);

            data.CRUDSubtiposExactus cs = new data.CRUDSubtiposExactus();
            res = s.getTipoCuenta() == 0 ? cs.actualizarSubtiposCP(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), selectedsubtipo)
                    : cs.actualizarSubtiposCB(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), "");

            if (res) {
                s.setDESCRIPCION(s.getASIENTO().startsWith("CP")?selectedsubtipo:"");
                //JOptionPane.showMessageDialog(null, "Se ha actualizado el registro");
            } else {
                JOptionPane.showMessageDialog(null, "No se ha actualizado el registro");
            }
        }else{JOptionPane.showMessageDialog(null, "No se ha encontrado el subtipo");}

        return res;
    }

    private boolean saveRowDefault(int row) {
        boolean res = false;

        String rowindx = tbMantSiubt.getValueAt(row, 12).toString();
        int tipoCuenta = (int) tbMantSiubt.getValueAt(row, 14);
        SubtiposExactus s = SubtiposExactus.getSubtipoPorIdRow(rowindx, tipoCuenta, listaSubtipos);
        if (s != null) {
            String selectedsubtipo = s.getTipoCuenta() == 0 ? "Factura" : "";
            AppLogger.appLogger.log(Level.WARNING, "view.AdministracionSubtiposFacturas.saveRow() "
                    + "\nsociedad " + s.getSociedad() + " row " + s.getIdRow() + " documento " + s.getDOCUMENTO() + " cuenta " + s.getDESCRIPCION() + " nueva cuenta " + selectedsubtipo);

            data.CRUDSubtiposExactus cs = new data.CRUDSubtiposExactus();
            res = s.getTipoCuenta() == 0 ? cs.actualizarSubtiposCP(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), selectedsubtipo)
                    : cs.actualizarSubtiposCB(s.getSociedad(), s.getIdRow(), s.getDOCUMENTO(), selectedsubtipo);

            if (res) {
                s.setDESCRIPCION(selectedsubtipo);
                //JOptionPane.showMessageDialog(null, "Se ha actualizado el registro");
                tbMantSiubt.setValueAt(selectedsubtipo, row, 3);
            } else {
                JOptionPane.showMessageDialog(null, "No se ha actualizado el registro");
            }
        }

        return res;
    }
    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                guardarExcel();
            }
        });
        t.start();
    }//GEN-LAST:event_btnExcelActionPerformed

    private void btnBuscarNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarNumeroActionPerformed
        // TODO add your handling code here:
        buscarFacturas();
    }//GEN-LAST:event_btnBuscarNumeroActionPerformed

    private void cmbFiltCuentaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFiltCuentaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED && !loadingInfo) {
            Runnable r = new Runnable() {
                @Override
                public void run() {

                    obtenerSubtipos();

                }
            };
            Thread t = new Thread(r);
            t.start();
        }
    }//GEN-LAST:event_cmbFiltCuentaItemStateChanged

    private void btnRefrescarFiltrosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnRefrescarFiltrosItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefrescarFiltrosItemStateChanged

    private void mnDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDefaultActionPerformed
        // TODO add your handling code here:
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int row = tbMantSiubt.getSelectedRow();
                if (row > -1) {
                    jProgressBar1.setVisible(true);
                    jProgressBar1.setString("Guardando cambios...");
                    boolean res = saveRowDefault(row);

                    view.util.CustomMessages.showTemporalProgessBar(jProgressBar1, 3000);
                    if (!res) {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al guardar registro");
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }//GEN-LAST:event_mnDefaultActionPerformed
    private void guardarExcel() {
        SimpleExcelWriter sew = new SimpleExcelWriter();
        //sew.writeToExcell(this.tbConciliacionMarcas);

        boolean saved = sew.writeJtableExcelFile(tbMantSiubt, "Reporte");
        if (saved) {
            JOptionPane.showMessageDialog(null, "Se ha guardado el archivo");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha guardado el archivo");
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarNumero;
    private javax.swing.JButton btnCargarFacturas;
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnRefrescarFiltros;
    private javax.swing.JButton btnSaveChanges;
    private javax.swing.JComboBox<String> cmbCta;
    private javax.swing.JComboBox<String> cmbDep;
    private javax.swing.JComboBox<String> cmbDepartamentos;
    private javax.swing.JComboBox<String> cmbFiltCuenta;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JComboBox<String> cmbSociedades;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdtFin;
    private com.toedter.calendar.JDateChooser jdtInicio;
    private javax.swing.JLabel lbTbMantResumen;
    private javax.swing.JMenuItem mnDefault;
    private javax.swing.JTable tbMantSiubt;
    private javax.swing.JTextField txtBuscarConsecutivo;
    // End of variables declaration//GEN-END:variables
}

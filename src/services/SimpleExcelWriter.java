/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import data.CrudProvContado.CrudProvedorContado;
import data.DataUser;
import entitys.AbonoSugerido;
import entitys.AbonoSugeridoContado;
import entitys.HistoricoCP;
import entitys.Presupuesto;
import entitys.ProveedorContado.CuentaProveedorContado;
import entitys.ProveedorContado.TelefonoSinpeContado;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import logic.CommonFile;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author eobregon
 */
public class SimpleExcelWriter extends JFrame {

    public boolean writeExcelFile() {
        boolean saved = false;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Java Books");

        Object[][] bookData = {
            {"Head First Java", "Kathy Serria", "0025"},
            {"Effective Java", "Joshua Bloch", "005"},
            {"Clean Code", "Robert martin", "00626"},
            {"Thinking in Java", "Bruce Eckel", "0056"},};

        int rowCount = 0;

        for (Object[] aBook : bookData) {
            Row row = sheet.createRow(++rowCount);

            int columnCount = 0;

            for (Object field : aBook) {
                Cell cell = row.createCell(++columnCount);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }

        }
        try {
            String filename = getDirectory("Guardar Excel");
            if (!filename.isEmpty()) {
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
                saved = true;
            }
        } catch (Exception e) {
            System.err.println("error en logic.SimpleExcelWriter.writeExcelFile() " + e.getMessage());
        }
        return saved;
    }

    /**
     * this method returns a string kind URI to the location selected by the
     * user
     *
     * @param chooserTitle is the title for the chooser dialog
     */
    private String getDirectory(String chooserTitle) {
        String file = "";
        JFileChooser fchoose = new JFileChooser();
        //fchoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fchoose.setLocale(new java.util.Locale( "es"));
        fchoose.setDialogTitle(chooserTitle);
        int option = fchoose.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            String name = fchoose.getSelectedFile().getName();
            String path = fchoose.getSelectedFile().getParentFile().getPath();
            //file = path + "\\" + name + ".xls";
            file = path + "\\" + name + ".xlsx";
            //logic.util.JTableToExcel.exporJTableToExcel(this.jtbCargaTotal, new File(file));
        }
        return file;
    }

    public void writeToExcell(JTable table) {
        //new WorkbookFactory();
        Workbook wb = new XSSFWorkbook(); //Excell workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        Row row = sheet.createRow(2); //Row created at line 3
        TableModel model = table.getModel(); //Table model
        Row headerRow = sheet.createRow(0); //Create row at line 0
        for (int headings = 0; headings < model.getColumnCount(); headings++) { //For each column
            String head = model.getColumnName(headings);
            headerRow.createCell(headings).setCellValue(head);//Write column name
        }

        for (int rows = 0; rows < model.getRowCount(); rows++) { //For each table row
            row = sheet.createRow(++rows);
            int columnCount = 0;
            for (int cols = 0; cols < table.getColumnCount() - 1; cols++) { //For each table column

                row.createCell(++columnCount).setCellValue(table.getValueAt(rows, cols).toString()); //Write value
                System.out.println("col created");
                columnCount++;
            }

            //Set the row to the next one in the sequence 
            //row = sheet.createRow((++rows));
        }
        try {
            String filename = getDirectory("Guardar Excel");
            if (!filename.isEmpty()) {
                FileOutputStream outputStream = new FileOutputStream(filename);
                wb.write(outputStream);
                //saved = true;
                CommonFile common = new CommonFile();
                common.openFile(filename);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error " + e.getCause());
        }
        //wb.write(new FileOutputStream(path.toString()));//Save the file     
    }

    public boolean writeJtableExcelFile(JTable table, String tituloHoja) {
        boolean saved = false;
        XSSFWorkbook workbook = new XSSFWorkbook();
        POIXMLProperties xmlProps = workbook.getProperties();
        POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
        coreProps.setCreator(DataUser.username);

        XSSFSheet sheet = workbook.createSheet(tituloHoja);

        TableModel model = table.getModel(); //Table model
        Row headerRow = sheet.createRow(0); //Create row at line 0
        for (int headings = 0; headings < model.getColumnCount(); headings++) { //For each column
            String head = model.getColumnName(headings);
            headerRow.createCell(headings).setCellValue(head);//Write column name
        }

        int rowCount = 0;

        for (int rows = 0; rows < model.getRowCount(); rows++) { //For each table row
            Row row = sheet.createRow(++rowCount);
            //int columnCount = 0;
            for (int cols = 0; cols < table.getColumnCount(); cols++) { //For each table column
                Cell cell = row.createCell(cols);
                String value = table.getValueAt(rows, cols) == null ? "" : table.getValueAt(rows, cols).toString();
                //System.out.println("row " + rowCount + " coll " + cols + " value " + value);
                cell.setCellValue(value == null ? "" : value); //Write value
                //System.out.println("col created");
                //columnCount++;
            }

        }
        autoSizeColumns(workbook);
        try {
            String filename = getDirectory("Guardar Excel");
            if (!filename.isEmpty()) {
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                saved = true;
            }
        } catch (Exception e) {
            System.err.println("error en logic.SimpleExcelWriter.writeExcelFile() " + e.getMessage());
        }
        return saved;
    }

    private void autoSizeColumns(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(sheet.getFirstRowNum());
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                }
            }
        }
    }

    private void addHeaders(XSSFSheet sheet, int rowCount) {
        Row row = sheet.createRow(rowCount);
        String[] headers = {"CODIGO EMPLEADO", "CODIGO DE CONCEPTO", "CANTIDAD", "MONTO", "CENTRO DE COSTO", "NOMINA", "NOMBRE", "DEPARTAMENTO", "SOCIEDAD"};
        int columnCount = 0;
        for (String object : headers) {
            Cell cell = row.createCell(columnCount);
            cell.setCellValue((String) object);
            columnCount++;
        }

    }

    private void addRow(String value, Row row, int columnCount) {
        //int columnCount = 0;
        Cell cell = row.createCell(++columnCount);
        cell.setCellValue((String) value);
        columnCount++;
    }

    public boolean writeHistoricoCPToExcel(String tituloHoja, ArrayList<entitys.HistoricoCP> lista, int currentWeek, ArrayList<Presupuesto> listaPresupuesto) {
        boolean saved = false;
        XSSFWorkbook workbook = new XSSFWorkbook();
        POIXMLProperties xmlProps = workbook.getProperties();
        POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
        coreProps.setCreator(DataUser.username);

        XSSFSheet sheet = workbook.createSheet(tituloHoja);

        int rowCount = 0;
        Row header = sheet.createRow(rowCount++);
        header.createCell(0).setCellValue("CIA");
        header.createCell(1).setCellValue("Moneda");
        header.createCell(2).setCellValue("Tipo proveedor");
        header.createCell(3).setCellValue("Numero proveedor");
        header.createCell(4).setCellValue("Nombre proveedor");
        header.createCell(5).setCellValue("Fecha de documento");
        header.createCell(6).setCellValue("Documento");
        header.createCell(7).setCellValue("Tipo mora");
        header.createCell(8).setCellValue("Cuenta presupuesto");
        header.createCell(9).setCellValue("Descripcion de la cuenta");
        header.createCell(10).setCellValue("Monto");
        header.createCell(11).setCellValue("Saldo");
        header.createCell(12).setCellValue("Monto ₡");
        header.createCell(13).setCellValue("Abono");//"abono",
        header.createCell(14).setCellValue("Condicion de venta");
        header.createCell(15).setCellValue("Aprobado");
        header.createCell(16).setCellValue("Observaciones");

        for (HistoricoCP e : lista) {
            Presupuesto p = Presupuesto.getPresupuesto(e.getCTA_PRESUPUESTO(), listaPresupuesto);
            AbonoSugerido ab = AbonoSugerido.obtenerAbonoMismaSemana(e.getDOCUMENTO(), currentWeek, e.getSugeridos());
            String cta = p == null ? "ND" : p.getCONCEPATOADETALLE();
            double saldoAbonos = e.getMONTO() - entitys.AbonoSugerido.getSaldoFinal(e.getSugeridos());
            String aprobado = ab == null ? "N" : ab.getAprobado() == 1 ? "S" : "N";
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(e.getCIA());
            row.createCell(1).setCellValue(e.getMONEDA());
            row.createCell(2).setCellValue(e.getTIPOPROV());
            row.createCell(3).setCellValue(e.getPROVEEDOR());
            row.createCell(4).setCellValue(e.getNOMBRE());
            row.createCell(5).setCellValue(logic.AppStaticValues.dateFormat.format(e.getFECHA_DOCUMENTO()));
            row.createCell(6).setCellValue(e.getDOCUMENTO());
            row.createCell(7).setCellValue(e.getESTAD_MORA());
            row.createCell(8).setCellValue(e.getCTA_PRESUPUESTO());
            row.createCell(9).setCellValue(cta);
            row.createCell(10).setCellValue(e.getMONTO());
            row.createCell(11).setCellValue(e.getSaldo());
            row.createCell(12).setCellValue(e.getMonto_colones());
            row.createCell(13).setCellValue(ab.getAbono());//"abono",
            row.createCell(14).setCellValue(e.getCONTA_CRED());
            row.createCell(15).setCellValue(aprobado);
            row.createCell(16).setCellValue(e.getNotas());
        }
        autoSizeColumns(workbook);
        try {
            String filename = getDirectory("Guardar Excel");
            if (!filename.isEmpty()) {
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                saved = true;
            }
        } catch (Exception e) {
            System.err.println("error en logic.SimpleExcelWriter.writeExcelFile() " + e.getMessage());
        }
        return saved;
    }

    public boolean writeCtaContadoReportToExcel(String tituloHoja, ArrayList<entitys.HistoricoCP> lista) {
        boolean saved = false;
        XSSFWorkbook workbook = new XSSFWorkbook();
        POIXMLProperties xmlProps = workbook.getProperties();
        POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
        coreProps.setCreator(DataUser.username);

        XSSFSheet sheet = workbook.createSheet(tituloHoja);

        int rowCount = 0;
        Row header = sheet.createRow(rowCount++);
        header.createCell(0).setCellValue("Tipo proveedor");
        header.createCell(1).setCellValue("Moneda");
        header.createCell(2).setCellValue("Sociedad");
        header.createCell(3).setCellValue("Proveedor");
        header.createCell(4).setCellValue("Fecha de documento");
        header.createCell(5).setCellValue("Cuenta presupuesto");
        header.createCell(6).setCellValue("Factura");
        header.createCell(7).setCellValue("Saldo ₡");
        header.createCell(8).setCellValue("Abono ₡");
        header.createCell(9).setCellValue("Saldo $");
        header.createCell(10).setCellValue("Abono $");//"abono",
        header.createCell(11).setCellValue("Fecha abono");
        header.createCell(12).setCellValue("Id");
        header.createCell(13).setCellValue("Cta Pres");
        header.createCell(14).setCellValue("# Proveedor");
        header.createCell(15).setCellValue("SINPE");
        header.createCell(16).setCellValue("Cuentas bancarias");
        header.createCell(17).setCellValue("Forma de pago");
        header.createCell(18).setCellValue("Adelanto");

        for (HistoricoCP e : lista) {

            data.CrudProvContado.CrudProvedorContado crd = new CrudProvedorContado();
            ArrayList<entitys.ProveedorContado.CuentaProveedorContado> cuentas = new ArrayList<entitys.ProveedorContado.CuentaProveedorContado>();
            ArrayList<entitys.ProveedorContado.TelefonoSinpeContado> telefonos = new ArrayList<entitys.ProveedorContado.TelefonoSinpeContado>();
            if (!e.getPROVEEDOR().equals("")) {
                telefonos = crd.obtenerListaSinpeProveedorContado(Integer.parseInt(e.getPROVEEDOR()));
                cuentas = crd.obtenerListaCtaProveedorContado(Integer.parseInt(e.getPROVEEDOR()));

            }

            String resultT = "";
            String resultC = "";
            for (TelefonoSinpeContado t : telefonos) {
                resultT += "SINPE: " + t.getNumero()
                        + " (" + (t.getEstado() == 1 ? "Activo" : "Inactivo") + "); ";
            }
            for (CuentaProveedorContado c : cuentas) {
                resultC += "Banco: " + c.getBanco() + "   Cuenta: " + c.getNumero()
                        + "  (" + (c.getEstado() == 1 ? "Activo" : "Inactivo") + "); ";
            }

            Row row = sheet.createRow(rowCount++);

            boolean abonoEqSaldo = e.getMONTO() == e.getSaldo();
            double abEqSalCol = abonoEqSaldo ? e.getMONEDA().equals("CRC")
                    ? e.getSaldo() + 0 : 0 : 0;
            double abNeqSalCol = abonoEqSaldo ? 0 : e.getMONEDA().equals("CRC")
                    ? e.getSaldo() + 0 : 0;
            double abEqSalDol = abonoEqSaldo ? e.getMONEDA().equals("USD")
                    ? e.getSaldo() + 0 : 0 : 0;
            double abNeqSalDol = abonoEqSaldo ? 0 : e.getMONEDA().equals("USD")
                    ? e.getSaldo() + 0 : 0;
            row.createCell(0).setCellValue(e.getTIPOPROV());
            row.createCell(1).setCellValue(e.getMONEDA());
            row.createCell(2).setCellValue(e.getCIA());
            row.createCell(3).setCellValue(e.getNOMBRE());
            row.createCell(4).setCellValue(logic.AppStaticValues.dateFormat.format(e.getFECHA_DOCUMENTO()));
            row.createCell(5).setCellValue(e.getDesc_Cta_Pres());
            row.createCell(6).setCellValue(e.getDOCUMENTO());
            if (abEqSalCol == 0) {
                row.createCell(7).setCellValue("");
            } else {
                row.createCell(7).setCellValue(abEqSalCol);
            }
            if (abNeqSalCol == 0) {
                row.createCell(8).setCellValue("");
            } else {
                row.createCell(8).setCellValue(abNeqSalCol);
            }
            if (abEqSalDol == 0) {
                row.createCell(9).setCellValue("");
            } else {
                row.createCell(9).setCellValue(abEqSalDol);
            }
            if (abNeqSalDol == 0) {
                row.createCell(10).setCellValue("");
            } else {
                row.createCell(10).setCellValue(abNeqSalDol);
            }
            row.createCell(11).setCellValue(logic.AppStaticValues.dateFormat.format(e.getFecha_Creacion()));
            row.createCell(12).setCellValue(e.getId());
            row.createCell(13).setCellValue(e.getCTA_PRESUPUESTO());
            row.createCell(14).setCellValue(e.getPROVEEDOR().trim());
            row.createCell(15).setCellValue(resultT);
            row.createCell(16).setCellValue(resultC);
            row.createCell(17).setCellValue(e.getForma_Pago());
            row.createCell(18).setCellValue((e.getAdelanto() == 1 ? "Si" : "No"));
        }
        autoSizeColumns(workbook);
        try {
            String filename = getDirectory("Guardar Excel");
            if (!filename.isEmpty()) {
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                saved = true;
            }
        } catch (Exception e) {
            System.err.println("error en logic.SimpleExcelWriter.writeExcelFile() " + e.getMessage());
        }
        return saved;
    }

    public boolean writeCtaContadoReportToExcel2(String tituloHoja, ArrayList<entitys.AbonoSugeridoContado> lista) {
        boolean saved = false;
        XSSFWorkbook workbook = new XSSFWorkbook();
        POIXMLProperties xmlProps = workbook.getProperties();
        POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
        coreProps.setCreator(DataUser.username);

        XSSFSheet sheet = workbook.createSheet(tituloHoja);

        int rowCount = 0;
        Row header = sheet.createRow(rowCount++);
        header.createCell(0).setCellValue("Tipo proveedor");
        header.createCell(1).setCellValue("Moneda");
        header.createCell(2).setCellValue("Sociedad");
        header.createCell(3).setCellValue("Proveedor");
        header.createCell(4).setCellValue("Fecha de documento");
        header.createCell(5).setCellValue("Cuenta presupuesto");
        header.createCell(6).setCellValue("Factura");
        header.createCell(7).setCellValue("Saldo ₡");
        header.createCell(8).setCellValue("Abono ₡");
        header.createCell(9).setCellValue("Saldo $");
        header.createCell(10).setCellValue("Abono $");//"abono",
        header.createCell(11).setCellValue("Fecha abono");
        header.createCell(12).setCellValue("Id");
        header.createCell(13).setCellValue("Cta Pres");
        header.createCell(14).setCellValue("# Proveedor");
        header.createCell(15).setCellValue("SINPE");
        header.createCell(16).setCellValue("Cuentas bancarias");
        header.createCell(17).setCellValue("Forma de pago");
        header.createCell(18).setCellValue("Adelanto");
        for (AbonoSugeridoContado e : lista) {

            data.CrudProvContado.CrudProvedorContado crd = new CrudProvedorContado();
            ArrayList<entitys.ProveedorContado.CuentaProveedorContado> cuentas = new ArrayList<entitys.ProveedorContado.CuentaProveedorContado>();
            ArrayList<entitys.ProveedorContado.TelefonoSinpeContado> telefonos = new ArrayList<entitys.ProveedorContado.TelefonoSinpeContado>();

            telefonos = crd.obtenerListaSinpeProveedorContado(e.getProveedor());
            cuentas = crd.obtenerListaCtaProveedorContado(e.getProveedor());

            String resultT = "";
            String resultC = "";
            for (TelefonoSinpeContado t : telefonos) {
                resultT += "SINPE: " + t.getNumero()
                        + " (" + (t.getEstado() == 1 ? "Activo" : "Inactivo") + "); ";
            }
            for (CuentaProveedorContado c : cuentas) {
                resultC += "Banco: " + c.getBanco() + "   Cuenta: " + c.getNumero()
                        + "  (" + (c.getEstado() == 1 ? "Activo" : "Inactivo") + "); ";
            }

            Row row = sheet.createRow(rowCount++);

            boolean abonoEqSaldo = e.getMontoOriginal() == e.getAbono();
            double abEqSalCol = abonoEqSaldo ? e.getMoneda().equals("CRC")
                    ? e.getAbono() + 0 : 0 : 0;
            double abNeqSalCol = abonoEqSaldo ? 0 : e.getMoneda().equals("CRC")
                    ? e.getAbono() + 0 : 0;
            double abEqSalDol = abonoEqSaldo ? e.getMoneda().equals("USD")
                    ? e.getAbono() + 0 : 0 : 0;
            double abNeqSalDol = abonoEqSaldo ? 0 : e.getMoneda().equals("USD")
                    ? e.getAbono() + 0 : 0;
            row.createCell(0).setCellValue("Contado");
            row.createCell(1).setCellValue(e.getMoneda());
            row.createCell(2).setCellValue(e.getSociedad());
            row.createCell(3).setCellValue(e.getNombreProveedor());
            row.createCell(4).setCellValue(logic.AppStaticValues.dateFormat.format(e.getFechaDocumento()));
            row.createCell(5).setCellValue(e.getDescCtaPres());
            row.createCell(6).setCellValue(e.getDocumento());
            if (abEqSalCol == 0) {
                row.createCell(7).setCellValue("");
            } else {
                row.createCell(7).setCellValue(abEqSalCol);
            }
            if (abNeqSalCol == 0) {
                row.createCell(8).setCellValue("");
            } else {
                row.createCell(8).setCellValue(abNeqSalCol);
            }
            if (abEqSalDol == 0) {
                row.createCell(9).setCellValue("");
            } else {
                row.createCell(9).setCellValue(abEqSalDol);
            }
            if (abNeqSalDol == 0) {
                row.createCell(10).setCellValue("");
            } else {
                row.createCell(10).setCellValue(abNeqSalDol);
            }
            row.createCell(11).setCellValue(logic.AppStaticValues.dateFormat.format(e.getFechaSolicitud()));
            row.createCell(12).setCellValue(e.getId());
            row.createCell(13).setCellValue(e.getCtPresupuesto());
            row.createCell(14).setCellValue(e.getProveedor());
            row.createCell(15).setCellValue(resultT);
            row.createCell(16).setCellValue(resultC);
            row.createCell(17).setCellValue(e.getForma_pago());
            row.createCell(18).setCellValue((e.getAdelanto() == 1 ? "Si" : "No"));
        }
        autoSizeColumns(workbook);
        try {
            String filename = getDirectory("Guardar Excel");
            if (!filename.isEmpty()) {
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                saved = true;
            }
        } catch (Exception e) {
            System.err.println("error en logic.SimpleExcelWriter.writeExcelFile() " + e.getMessage());
        }
        return saved;
    }

    public boolean writeCtaCreditoReportToExcel(String tituloHoja, ArrayList<AbonoSugerido> lista) {
        boolean saved = false;
        XSSFWorkbook workbook = new XSSFWorkbook();
        POIXMLProperties xmlProps = workbook.getProperties();
        POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
        coreProps.setCreator(DataUser.username);
        CellStyle currencyStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        //currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat("_($* #,##0.00_);_($* (#,##0.00);_($* \"-\"??_);_(@_)"));
        currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat("###,###,###.00"));
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mm-yyyy"));
        XSSFSheet sheet = workbook.createSheet(tituloHoja);

        int rowCount = 0;
        Row header = sheet.createRow(rowCount++);
        header.createCell(0).setCellValue("Tipo proveedor");
        header.createCell(1).setCellValue("Moneda");
        header.createCell(2).setCellValue("Sociedad");
        header.createCell(3).setCellValue("Proveedor");
        header.createCell(4).setCellValue("Fecha de documento");
        header.createCell(5).setCellValue("Cuenta presupuesto");
        header.createCell(6).setCellValue("Factura");
        header.createCell(7).setCellValue("Saldo ₡");
        header.createCell(8).setCellValue("Abono ₡");
        header.createCell(9).setCellValue("Saldo $");
        header.createCell(10).setCellValue("Abono $");//"abono",
        header.createCell(11).setCellValue("Revisado");
        header.createCell(12).setCellValue("Comentarios");
        header.createCell(13).setCellValue("Cuenta presupuesto");

        for (AbonoSugerido e : lista) {

            Row row = sheet.createRow(rowCount++);
            boolean abonoEqSaldo = e.getAbono() == e.getSaldo_Actuual();

            row.createCell(0).setCellValue(e.getTipo_Proveedor());
            row.createCell(1).setCellValue(e.getMoneda());
            row.createCell(2).setCellValue(e.getCIA());
            row.createCell(3).setCellValue(e.getNombre_Proveedor());
            row.createCell(4).setCellValue(logic.AppStaticValues.dateFormat.format(e.getFecha_documento()));
            //row.getCell(4).setCellStyle(dateCellStyle);
            row.createCell(5).setCellValue(e.getDescripion_Cta_Presupuesto());
            row.createCell(6).setCellValue(e.getDocumento());
            double saldoColones = abonoEqSaldo ? e.getMoneda().equals("CRC") ? e.getSaldo_Actuual() : 0 : 0;
            if (saldoColones > 0) {
                row.createCell(7).setCellValue(saldoColones);
                //row.getCell(7).setCellStyle(currencyStyle);
            }
            double abonoColones = !abonoEqSaldo ? e.getMoneda().equals("CRC") ? e.getAbono() : 0 : 0;
            if (abonoColones > 0) {
                row.createCell(8).setCellValue(abonoColones);
                //row.getCell(8).setCellStyle(currencyStyle);
            }
            double saldodolares = abonoEqSaldo ? e.getMoneda().equals("USD") ? e.getSaldo_Actuual() : 0 : 0;
            if (saldodolares > 0) {
                row.createCell(9).setCellValue(saldodolares);
                //row.getCell(9).setCellStyle(currencyStyle);
            }
            double abonoDolares = !abonoEqSaldo ? e.getMoneda().equals("USD") ? e.getAbono() : 0 : 0;
            if (abonoDolares > 0) {
                row.createCell(10).setCellValue(abonoDolares);
                //row.getCell(10).setCellStyle(currencyStyle);
            }

            row.createCell(11).setCellValue((e.getRevisadoConta() == 1 ? "Si" : "No"));
            row.createCell(12).setCellValue(e.getComentarios());
            row.createCell(13).setCellValue(e.getCuenta_Presupuesto());
        }
        autoSizeColumns(workbook);
        try {
            String filename = getDirectory("Guardar Excel");
            if (!filename.isEmpty()) {
                FileOutputStream outputStream = new FileOutputStream(filename);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                saved = true;
            }
        } catch (Exception e) {
            System.err.println("error en logic.SimpleExcelWriter.writeExcelFile() " + e.getMessage());
        }
        return saved;
    }

}

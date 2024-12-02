/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author eobregon
 */
public class AppStaticValues {
    //this are the actual production credentials and directories, uncomment it to production mode

    public static String sosUrl = "jdbc:mysql://45.32.7.136:3306/sos";
    public static String respaldoArchivosGuardados = "\\\\192.168.0.10\\2 - soporte\\Archivos Factura Electronica\\guardados\\";
    public static String respaldoArchivosOmitidos = "\\\\192.168.0.10\\2 - soporte\\Archivos Factura Electronica\\omitidos\\";
    public static String archivosFacturasContado = "\\\\192.168.0.10\\2 - soporte\\Archivos Factura Electronica\\Facturas contado\\";
    public static boolean testMode = false;
    public static final String sqlServer = "192.168.0.9";
     
    //this are the test credentials, comment it in production mode
    /*public static final String sqlServer = "DESKTOP-QRLPE70\\SQLEXPRESS";
    public static String sosUrl = "jdbc:mysql://localhost:3306/sos";
    public static String respaldoArchivosGuardados = "C:\\Users\\eobregon\\Documents\\edras\\BackUpFacturas\\guardados\\";
    public static String respaldoArchivosOmitidos = "C:\\Users\\eobregon\\Documents\\edras\\BackUpFacturas\\omitidos\\";
    public static String archivosFacturasContado = "C:\\Users\\eobregon\\Documents\\edras\\BackUpFacturas\\Facturas contado\\";
    public static boolean testMode = true;*/

    public static String appVersion = "4.0.0.3 rev 1";
    public static String productionAppPath = "C:\\Factura Electronica";
    public static String serverAppPath = "\\\\192.168.0.10\\2 - soporte\\2-PROGRAMAS\\edras\\Factura electronica\\app";
    public static String appFileName = "Factura_Electronica.jar";
    public static String productionVersionFileName = "Factura electronica version.xml";
    public static String appName = "Factura Electronica";
    public static String appManual = "manual gestion facturas electronicas.pdf";
    public static String appManualConta = "manual gestion facturas electronicas contabilidad.pdf";
    public static String loggerPath = "C:\\Factura Electronica\\appLogger";

    ///formaters
    public static Locale locale = Locale.of("en", "US");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat dateBigFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static SimpleDateFormat shortDateformatter = new SimpleDateFormat("dd-MM");
    public static NumberFormat numberFormater = NumberFormat.getInstance(locale);
    public static Locale localeCrCurrency = Locale.forLanguageTag("es-CR");
    public static DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
}

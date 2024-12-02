/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package view;

import data.DataUser;
import entitys.Departamento;
import entitys.Presupuesto;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import logic.AppLogger;
import services.VersionHandler;
import view.util.LookAndFeel;

/**
 *
 * @author eobregon
 */
public class FacturaElectronica {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        init();
        /*Graficos g = new Graficos();
        g.setVisible(true);*/
    }

    private static void init() {
        AppLogger app = new AppLogger();
        app.createAppLogFile();
        AppLogger.initLogger();
        AppLogger.appLogger.info("inciando " + logic.AppStaticValues.appName + " version " + logic.AppStaticValues.appVersion);
        String systemVal = "mysql sos " + logic.AppStaticValues.sosUrl
                + "\nserver " + logic.AppStaticValues.sqlServer
                + "\nproduction path " + logic.AppStaticValues.productionAppPath
                + "\nversion " + logic.AppStaticValues.appVersion
                + "\nbackup files path " + logic.AppStaticValues.respaldoArchivosGuardados
                + "\nserver app path " + logic.AppStaticValues.serverAppPath
                + "\napp name " + logic.AppStaticValues.appName
                + "\ntest mode " + logic.AppStaticValues.testMode;
        AppLogger.appLogger.info(systemVal);
        LookAndFeel l = new LookAndFeel(2);
        VersionHandler vh = new VersionHandler();
        String newVersion = vh.getCurrentAppVersion();
        if (newVersion.equalsIgnoreCase(logic.AppStaticValues.appVersion)) {

            l.setTheme();
            login log = new login();
            log.setVisible(true);

        } else {
            //JOptionPane.showMessageDialog(null, "Nueva versión encontrada, espere un momento por favor...");

            UpdateAppInterface up = new UpdateAppInterface();
            up.setVisible(true);

            //update();
        }
    }

    public static void update() {
        VersionHandler vh = new VersionHandler();
        boolean res = vh.updateVersion();
        if (res) {

            JOptionPane.showMessageDialog(null, "Versión actualizada, por favor abra Gestión de Facturas Electrónicas de nuevo");

        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido actualizar, por favor intentelo nuevamente");
        }
        System.exit(0);
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.AsientoFactura;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CrudAsiento {

    public CrudAsiento() {
        System.out.println("data.CrudPresupuesto.<init>()");
        sqlPoolInstance.initPool();
    }

    public ArrayList<AsientoFactura> obtenerAsientos(java.util.Date inicio, java.util.Date fin) {
        ArrayList<AsientoFactura> listaPresupuesto = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * FROM INDICADORES.dbo.V_XML_ASIENTO where fecha_documento >= '" + sqlInicio + "' and fecha_documento <= '" + sqlFin + "';";
            System.out.println("data.CrudAsiento.obtenerAsientos() sentencia " + "\n" + Sql);
            AppLogger.appLogger.info("sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                AsientoFactura p = new AsientoFactura();
                p.setCIA(rs.getString("CIA").trim());
                p.setPROVEEDOR(rs.getString("PROVEEDOR"));
                p.setCEDJUR(rs.getString("CEDJUR"));
                p.setNOMBRE(rs.getString("NOMBRE"));
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONEDA(rs.getString("MONEDA"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setSUBTIPO(rs.getInt("SUBTIPO"));
                p.setDES_TIPO(rs.getString("DES_TIPO"));
                p.setTIPODOC(rs.getString("TIPODOC"));
                listaPresupuesto.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.info("data.CrudAsiento.obtenerAsientos() error " + e.getMessage());
            System.out.println("data.CrudAsiento.obtenerAsientos() error " + e.getMessage());
        }
        return listaPresupuesto;
    }

    public ArrayList<AsientoFactura> obtenerAsientos() {
        ArrayList<AsientoFactura> listaPresupuesto = new ArrayList<>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * FROM INDICADORES.dbo.V_XML_ASIENTO;";
            AppLogger.appLogger.info("sentencia " + Sql);
            System.out.println("data.CrudAsiento.obtenerAsientos() sentencia " + "\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                AsientoFactura p = new AsientoFactura();
                p.setCIA(rs.getString("CIA").trim());
                p.setPROVEEDOR(rs.getString("PROVEEDOR"));
                p.setCEDJUR(rs.getString("CEDJUR"));
                p.setNOMBRE(rs.getString("NOMBRE"));
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONEDA(rs.getString("MONEDA"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setTIPODOC(rs.getString("TIPODOC"));
                p.setSUBTIPO(rs.getInt("SUBTIPO"));
                p.setDES_TIPO(rs.getString("DES_TIPO"));
                listaPresupuesto.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.info("data.CrudAsiento.obtenerAsientos() error " + e.getMessage());
            System.out.println("data.CrudAsiento.obtenerAsientos() error " + e.getMessage());
        }
        return listaPresupuesto;
    }

}

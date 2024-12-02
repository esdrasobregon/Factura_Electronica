/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.DetallesHistoricoPagos;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import logic.AppLogger;
import java.sql.ResultSet;
/**
 *
 * @author eobregon
 */
public class CrudDetallesHistoricoPagos {

    public boolean agregarDetallesHistoricoPagos(DetallesHistoricoPagos h) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlDoc = new java.sql.Date(h.getFECHA().getTime());
            java.sql.Date sqlUltima = new java.sql.Date(h.getULTIMA_ACTUALIZACIO().getTime());
            String Sql = "INSERT INTO INDICADORES.[dbo].[Detalles_Historico_Pagos_Credito] "
                    + "([Observacion]"
                    + ",CIA"
                    + ",[NOMBRE_PROVEEDOR]"
                    + ",[NUM_PROVEEDOR]"
                    + ",[FACTURA]"
                    + ",[FECHA]"
                    + ",[USUARIO]"
                    + ",[ULTIMA_ACTUALIZACIO])"
                    + "VALUES"
                    + "('" + h.getObservacion() + "'"
                    + ",'" + h.getCIA() + "'"
                    + ",'" + h.getNOMBRE_PROVEEDOR() + "'"
                    + ",'" + h.getNUM_PROVEEDOR() + "'"
                    + ",'" + h.getFACTURA() + "'"
                    + ",'" + sqlDoc + "'"
                    + ",'" + h.getUSUARIO() + "'"
                    + ",'" + sqlUltima + "');";

            System.out.println("data.CrudDetallesHistoricoPagos.agregarHistoricoContado() sentencia " + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudDetallesHistoricoPagos.agregarHistoricoContado() error " + e.getMessage());
            System.out.println("data.CrudDetallesHistoricoPagos.agregarHistoricoContado() error " + e.getMessage());
            res = false;
        }
        return res;
    }

    public DetallesHistoricoPagos getDetalles_Historico_Pagos_Credito(String cia,String numProv, String doc) {
        DetallesHistoricoPagos ab = null;
        String procedureCall = "SELECT *FROM [INDICADORES].[dbo].[Detalles_Historico_Pagos_Credito]"
                + "where cia = '" + cia+"'" 
                + " and [NUM_PROVEEDOR] = '" + numProv+"'" 
                + " and factura = '" + doc + "';";
        System.out.println("data.CrudDetallesHistoricoPagos.getDetalles_Historico_Pagos_Credito() sentencia\n"+procedureCall);
        try {
            
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                ab = new DetallesHistoricoPagos();
                ab.setCIA(rs.getString("CIA").trim());
                ab.setFACTURA(rs.getString("Factura").trim());
                ab.setNUM_PROVEEDOR(rs.getString("NUM_PROVEEDOR").trim());
                ab.setObservacion(rs.getString("Observacion").trim());
                ab.setId(rs.getInt("id"));
                
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            ab = null;
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudDetallesHistoricoPagos.getDetalles_Historico_Pagos_Credito() error " + e.getMessage());
            System.err.println("data.CrudDetallesHistoricoPagos.getDetalles_Historico_Pagos_Credito() error " + e.getMessage());
        }
        return ab;
    }

    public boolean actualizarDetalles_Historico_Pagos_Credito(DetallesHistoricoPagos h) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlCre = new java.sql.Date(h.getULTIMA_ACTUALIZACIO().getTime());
            String Sql = "update indicadores.[dbo].[Detalles_Historico_Pagos_Credito] "
                    + "set Observacion ='" + h.getObservacion() + "'"
                    + ", ULTIMA_ACTUALIZACIO = '" + sqlCre + "'"
                    + "where id = "+h.getId()+";";

            System.out.println("data.CrudDetallesHistoricoPagos.ActualizarDetalles_Historico_Pagos_Credito() sentencia \n"+Sql);
            AppLogger.appLogger.log(Level.WARNING, "data.CrudDetallesHistoricoPagos.ActualizarDetalles_Historico_Pagos_Credito() sentencia  " + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudDetallesHistoricoPagos.ActualizarDetalles_Historico_Pagos_Credito() sentencia  error " + e.getMessage());
            System.out.println("data.CrudDetallesHistoricoPagos.ActualizarDetalles_Historico_Pagos_Credito() sentencia error " + e.getMessage());
            res = false;
        }
        return res;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.ResumenDetallesFacturas;

import data.DbPoolHandler;
import entitys.DetallesFacturas.ResumenTotalesDetallesFacturaE;
import entitys.DetallesFacturas.DetallesLineasFactura;
import logic.AppLogger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author eobregon
 */
public class CrudResumenTotalesDetallesFacturaE {

    public ArrayList<ResumenTotalesDetallesFacturaE> ObtResumenTotalesFacturaElectronicaPorFecha(String cedCia, String cedProv, int registradas, int asignadas, java.util.Date fechaInicio, java.util.Date fechaFin, boolean ciaDesconocidas, String moneda, boolean todasExctus) {
        ArrayList<ResumenTotalesDetallesFacturaE> res = new ArrayList<ResumenTotalesDetallesFacturaE>();
        java.sql.Date sqlInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fechaFin.getTime());
        String monedaStat = moneda.equals("USD")
                ? "and codigomoneda ='USD'"
                : "and codigomoneda !='USD'";

        try {
            String sentencia = "select "
                    + "sum(`f`.`TotalComprobante`) AS `TotalComprobante`"
                    + ",sum(`f`.`TotalDescuentos`) AS `TotalDescuentos`"
                    + ",sum(`f`.`TotalServGravados`) AS `TotalServGravados`"
                    + ",sum(`f`.`TotalServExentos`) AS `TotalServExentos`"
                    + ",sum(`f`.`TotalServExonerado`) AS `TotalServExonerado`"
                    + ",sum(`f`.`TotalMercanciasGravadas`) AS `TotalMercanciasGravadas`"
                    + ",sum(`f`.`TotalMercanciasExentas`) AS `TotalMercanciasExentas`"
                    + ",sum(`f`.`TotalExento`) AS `TotalExento`"
                    + ",sum(`f`.`TotalImpuesto`) AS `TotalImpuesto`"
                    + ",sum(`f`.`TotalOtrosCargos`) AS `TotalOtrosCargos`"
                    + ",`f`.`CodigoMoneda` AS `CodigoMoneda` from sos.`facturaelectronica_view` `f` WHERE "
                    + "f.ReceptorCedulaJuridica LIKE '%" + cedCia + "%' "
                    + (ciaDesconocidas ? "AND f.ReceptorCedulaJuridica not LIKE '%3101724817%' "
                            + "AND f.ReceptorCedulaJuridica not LIKE '%3101086411%' "
                            + "AND f.ReceptorCedulaJuridica not LIKE '%3101119637%' "
                            + "AND f.ReceptorCedulaJuridica not LIKE '%3101119531%' "
                            + "AND f.ReceptorCedulaJuridica not LIKE '%3101466557%' "
                            + "AND f.ReceptorCedulaJuridica not LIKE '%3101468003%' " : "")
                    + "AND f.EmisorCedulaJuridica LIKE '%" + cedProv + "%' "
                    + (todasExctus ? ""
                            : (registradas == 0
                                    ? ""
                                    : (registradas == 1
                                            ? "AND f.exactus = '' "
                                            : "AND f.exactus != '' ")))
                    + (asignadas == 0 ? "" : (asignadas == 1 ? "AND f.CuentaPresupuesto != '' "
                                    : "AND f.CuentaPresupuesto = '' "))
                    + "and (f.fechaemision BETWEEN '" + sqlInicio + "' "
                    + "AND '" + sqlFin + "') "
                    + monedaStat
                    + "group by f.codigomoneda;";;
            AppLogger.appLogger.info("data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE.ObtResumenTotalesFacturaElectronicaPorFecha() sentencia \n" + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                ResumenTotalesDetallesFacturaE n = new ResumenTotalesDetallesFacturaE();
                n.setCodigoMoneda(rs.getString("CodigoMoneda").trim());
                n.setTotalMercanciasGravadas(rs.getDouble("TotalMercanciasGravadas"));
                n.setTotalMercanciasExentas(rs.getDouble("TotalMercanciasExentas"));
                n.setTotalComprobante(rs.getDouble("TotalComprobante"));
                n.setTotalDescuentos(rs.getDouble("TotalDescuentos"));
                n.setTotalServGravados(rs.getDouble("TotalServGravados"));
                n.setTotalServExentos(rs.getDouble("TotalServExentos"));
                n.setTotalServExonerado(rs.getDouble("TotalServExonerado"));
                n.setTotalExento(rs.getDouble("TotalExento"));
                n.setTotalImpuesto(rs.getDouble("TotalImpuesto"));
                n.setTotalOtrosCargos(rs.getDouble("TotalOtrosCargos"));

                res.add(n);
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            AppLogger.appLogger.warning("data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE.ObtResumenTotalesFacturaElectronicaPorFecha() error " + e.getMessage());
            res = new ArrayList<>();
        }
        return res;
    }

    public ArrayList<DetallesLineasFactura> ObtResumenTotalesDetallesLineasFacturaPorFecha(String cedCia, String cedProv, int registradas, int asignadas, java.util.Date fechaInicio, java.util.Date fechaFin, boolean ciaDesconocidas, String moneda, boolean todasExctus) {
        ArrayList<DetallesLineasFactura> res = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fechaFin.getTime());
        String monedaStat = moneda.equals("USD")
                ? "and codigomoneda ='USD'"
                : "and codigomoneda !='USD'";
        try {
            String sentencia = "SELECT "
                    + " count(*) AS totalLineas,"
                    + " SUM(l.MontoTotalLinea) AS sumaMontoTotalLineas"
                    + " ,SUM(l.MontoDescuento) AS sumaMontoTotalDesc"
                    + " ,SUM(l.SubTotal) AS sumaSubTotal"
                    + " ,SUM(l.ImpuestoNeto) AS sumaMontoImpuestoNeto"
                    + " ,SUM(((l.TarifaImp/100) * l.SubTotal)) AS calcImpuestoNeto"
                    + " ,l.tarifaimp"
                    + " ,l.codigomoneda"
                    + " FROM  "
                    + " sos.lineadetallefe l"
                    + " WHERE"
                    + "(l.Fecha_Emision BETWEEN '" + sqlInicio + "' AND '" + sqlFin + "')"
                    + "	and l.ReceptorCedulaJuridica like '%" + cedCia + "%'"
                    + (ciaDesconocidas ? " AND l.ReceptorCedulaJuridica not LIKE '%3101724817%' "
                            + " AND l.ReceptorCedulaJuridica not LIKE '%3101086411%' "
                            + " AND l.ReceptorCedulaJuridica not LIKE '%3101119637%' "
                            + " AND l.ReceptorCedulaJuridica not LIKE '%3101119531%' "
                            + " AND l.ReceptorCedulaJuridica not LIKE '%3101466557%' "
                            + " AND l.ReceptorCedulaJuridica not LIKE '%3101468003%' " : "")
                    + "	and l.EmisorCedulaJuridica like '%" + cedProv + "%'"
                    + (moneda.equals("USD")
                    ? "	and l.codigomoneda ='USD'"
                    : "	and l.codigomoneda !='USD'")
                    + (todasExctus ? "" : " and l.exactus !=''")
                    + (asignadas == 1 ? " and l.CtaPresupuesto != ''" : "")
                    + " GROUP BY l.CodigoMoneda, l.TarifaImp"
                    + " ORDER BY l.codigomoneda";
            AppLogger.appLogger.info("data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE.ObtResumenTotalesDetallesLineasFacturaPorFecha() sentencia \n" + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                DetallesLineasFactura n = new DetallesLineasFactura();
                n.setCodigomoneda(rs.getString("CodigoMoneda").trim());
                n.setSumaMontoTotalLineas(rs.getDouble("sumaMontoTotalLineas"));
                n.setSumaSubTotal(rs.getDouble("sumaMontoTotalDesc"));
                n.setSumaSubTotal(rs.getDouble("sumaSubTotal"));
                n.setSumaMontoImpuestoNeto(rs.getDouble("sumaMontoImpuestoNeto"));
                n.setCalcImpuestoNeto(rs.getDouble("calcImpuestoNeto"));
                n.setTarifaimp(rs.getDouble("tarifaimp"));
                n.setCodigomoneda(rs.getString("codigomoneda"));
                n.setTotalLineas(rs.getInt("totalLineas"));
                res.add(n);
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            AppLogger.appLogger.warning("data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE.ObtResumenTotalesDetallesLineasFacturaPorFecha() error " + e.getMessage());
            res = new ArrayList<>();
        }
        return res;
    }

    public boolean updateLinesExactus() {
        boolean res = false;
        try {

            String sentencia = "call updateLines_Proc()";
            AppLogger.appLogger.info("data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE.updateLinesExactus() sentencia\n" + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            statement.execute(sentencia);
            AppLogger.appLogger.info("data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE.updateLinesExactus() sentencia"
                    + "\n" + "call updateCtaLines_Proc()");
            statement.execute("call updateCtaLines_Proc()");
            res = true;
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            AppLogger.appLogger.info("data.ResumenDetallesFacturas.CrudResumenTotalesDetallesFacturaE.updateLinesExactus() error " + e.getMessage());
            DbPoolHandler.connectionPool.initializeConnections();
        }

        return res;
    }
}

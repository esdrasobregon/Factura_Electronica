/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.TipoCambio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CRUDTipoCambio {

    public entitys.TipoCambio obtenerTipoCambioPorFecha(java.util.Date inicio) {
        entitys.TipoCambio res = null;
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT t.Fecha,t.Compra ,t.Venta "
                    + "FROM INDICADORES.dbo.tipo_cambio_view t where t.Fecha = '" + sqlInicio + "';";
            System.out.println("data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                res = new entitys.TipoCambio();
                res.setFecha(rs.getDate("Fecha"));
                res.setCompra(rs.getDouble("Compra"));
                res.setVenta(rs.getDouble("Venta"));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() error " + e.getMessage());
            System.out.println("data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() error " + e.getMessage());
            res = null;
        }
        return res;
    }

    public boolean addUpdateTipoCambio(entitys.TipoCambio tc) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlInicio = new java.sql.Date(tc.getFecha().getTime());
            /*String Sql = "INSERT INTO indicadores.dbo.tipos_cambio"
                    + "(Fecha, Compra, Venta) "
                    + "VALUES ('" + sqlInicio + "','" + tc.getCompra() + "','" + tc.getVenta() + "');";*/
            String Sql = "exec indicadores.dbo.addUpdateTipoCambio "
                    + "'" + sqlInicio + "','" + tc.getCompra() + "','" + tc.getVenta() + "';";
            System.out.println("data.CRUDTipoCambio.agregarTipoCambio() sentencia \n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDTipoCambio.agregarTipoCambio() error " + e.getMessage());
            System.out.println("data.CRUDTipoCambio.agregarTipoCambio() error " + e.getMessage());
            res = false;
        }
        return res;
    }

    public boolean eliminarTipoCambio(entitys.TipoCambio tc) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlInicio = new java.sql.Date(tc.getFecha().getTime());
            String Sql = "delete from INDICADORES.dbo.tipos_cambio WHERE Fecha = '" + sqlInicio + "';";

            System.out.println("data.CRUDTipoCambio.eliminarTipoCambio() sentencia \n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDTipoCambio.eliminarTipoCambio() error " + e.getMessage());
            System.out.println("data.CRUDTipoCambio.eliminarTipoCambio() error " + e.getMessage());
            res = false;
        }
        return res;
    }

    public ArrayList<TipoCambio> getTipoCambioPorFechas(Date inicio, Date fin) {
        ArrayList<TipoCambio> lista = new ArrayList<TipoCambio>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlfin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT t.Fecha,t.Compra ,t.Venta FROM INDICADORES.dbo.tipos_cambio t "
                    + "where t.Fecha >= '" + sqlInicio
                    + "' and t.Fecha <= '" + sqlfin + "'"
                    + " order by t.Fecha asc;";
            System.out.println("data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                TipoCambio res = new entitys.TipoCambio();
                res.setFecha(rs.getDate("Fecha"));
                res.setCompra(rs.getDouble("Compra"));
                res.setVenta(rs.getDouble("Venta"));
                res.setDiferencia(res.getVenta() - res.getCompra());
                lista.add(res);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() error " + e.getMessage());
            System.out.println("data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() error " + e.getMessage());

        }
        return lista;
    }

    public TipoCambio getTipoCambioPorFecha(Date inicio) {
        TipoCambio res = null;
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT t.Fecha,t.Compra ,t.Venta FROM INDICADORES.dbo.tipos_cambio t "
                    + "where t.Fecha = '" + sqlInicio + "' order by t.Fecha asc;";
            System.out.println("data.CRUDSubtiposExactus.getTipoCambioPorFecha() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                res = new entitys.TipoCambio();
                res.setFecha(rs.getDate("Fecha"));
                res.setCompra(rs.getDouble("Compra"));
                res.setVenta(rs.getDouble("Venta"));
                res.setDiferencia(res.getVenta() - res.getCompra());
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.getTipoCambioPorFecha() error " + e.getMessage());
            return null;
        }
        return res;
    }

}

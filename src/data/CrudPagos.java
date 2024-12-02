/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.logging.Level;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CrudPagos {

    public ArrayList<entitys.Pagos> obtPagosPorFechas(java.util.Date inicio, java.util.Date fin, int sociedad, String proveedor, String moneda, String nomProv) {
        ArrayList<entitys.Pagos> lista = new ArrayList<entitys.Pagos>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String sql = "select *from indicadores.dbo.vita_pagos_realizados"
                    + " where "
                    + "sociedad " + (sociedad == 0 ? "> 0" : "= " + sociedad)
                    + (proveedor.isEmpty() ? "" : " and proveedor = '" + proveedor + "'")
                    + (nomProv.isEmpty() ? "" : " and nombre_proveedor = '" + nomProv + "'")
                    + (moneda.isEmpty() ? "" : " and moneda_debito = '" + moneda + "'")
                    + " and (fecha between '" + sqlInicio + "' and '" + sqlFin + "');";
            System.out.println("data.CrudPagos.obtPagosPorFechas()");
            AppLogger.appLogger.log(Level.WARNING, "data.CrudPagos.obtPagosPorFechas() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.Pagos p = new entitys.Pagos();
                p.setSociedad(rs.getInt("sociedad"));
                p.setCia(rs.getString("cia").trim());
                p.setProveedor(rs.getString("proveedor"));
                p.setTipo_debito(rs.getString("Tipo_debito"));
                p.setDebito(rs.getString("debito"));
                p.setFecha(rs.getDate("fecha"));
                p.setCreateDate(rs.getDate("createDate"));
                p.setRecordDate(rs.getDate("recordDate"));
                p.setTipo_credito(rs.getString("Tipo_credito").trim());
                p.setCredito(rs.getString("credito"));
                p.setMonto_debito(rs.getDouble("Monto_debito"));
                p.setMonto_prov(rs.getDouble("Monto_prov"));
                p.setMonto_local(rs.getDouble("Monto_local"));
                p.setMonto_dolar(rs.getDouble("Monto_dolar"));
                p.setMoneda_debito(rs.getString("Moneda_debito"));
                p.setMonto_credito(rs.getDouble("Monto_credito"));
                p.setCreatedby(rs.getString("Createdby"));
                p.setUpdatedby(rs.getString("Updatedby"));
                p.setRowpointer(rs.getString("Rowpointer"));
                p.setNombreProveedor(rs.getString("nombre_proveedor").trim());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudPagos.obtPagosPorFechas() error " + e.getMessage());
        }
        return lista;
    }

}

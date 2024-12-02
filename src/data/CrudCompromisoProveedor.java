/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import logic.AppLogger;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author eobregon
 */
public class CrudCompromisoProveedor {

    public ArrayList<entitys.CompromisosProveedor> obtenerCompromisosProveedorPPorFechas(java.util.Date inicio, java.util.Date fin, int activo, String moneda) {
        ArrayList<entitys.CompromisosProveedor> lista = new ArrayList<>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
            java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
            String sql = "select *from indicadores.dbo.CompromisosProveedor_view "
                    + " where estado = " + activo
                    + (moneda.equals("Todas") ? "" : " and  moneda ='" + moneda + "'")
                    + " and (fechacreacion between '" + sqlInicio + "' and '" + sqlFin + "');";
            AppLogger.appLogger.log(Level.WARNING, "data.CrudCompromisoProveedor.obtenerCompromisosProveedorPPorFechas() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.CompromisosProveedor p = new entitys.CompromisosProveedor();
                p.setCia_prov(rs.getString("cia_prov").trim());
                p.setProveedor(rs.getString("PROVEEDOR").trim());
                p.setNombreProveedor(rs.getString("nombreproveedor"));
                p.setId(rs.getInt("id"));
                p.setFechaCreacion(rs.getDate("fechacreacion"));
                p.setResponsable(rs.getString("responsable"));
                p.setMonto(rs.getDouble("monto"));
                p.setEstado(rs.getInt("estado"));
                p.setPeriodo(rs.getInt("periodo"));
                p.setMoneda(rs.getString("moneda"));
                p.setObservaciones(rs.getString("observaciones"));
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudCompromisoProveedor.obtenerCompromisosProveedorPPorFechas() error " + e.getMessage());
            sqlPoolInstance.pool.releaseAllConnection();

        }
        return lista;
    }

    public ArrayList<entitys.CompromisosProveedor> obtenerCompromisosProveedor(int activo, String moneda) {
        ArrayList<entitys.CompromisosProveedor> lista = new ArrayList<>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String sql = "select *from indicadores.dbo.CompromisosProveedor_view "
                    + " where estado = " + activo
                    + (moneda.equals("Todas") ? "" : " and  moneda ='" + moneda + "'") + ";";
            AppLogger.appLogger.log(Level.WARNING, "data.CrudCompromisoProveedor.obtenerCompromisosProveedorPPorFechas() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.CompromisosProveedor p = new entitys.CompromisosProveedor();
                p.setCia_prov(rs.getString("cia_prov").trim());
                p.setProveedor(rs.getString("PROVEEDOR").trim());
                p.setNombreProveedor(rs.getString("nombreproveedor"));
                p.setId(rs.getInt("id"));
                p.setFechaCreacion(rs.getDate("fechacreacion"));
                p.setResponsable(rs.getString("responsable"));
                p.setMonto(rs.getDouble("monto"));
                p.setEstado(rs.getInt("estado"));
                p.setPeriodo(rs.getInt("periodo"));
                p.setMoneda(rs.getString("moneda"));
                p.setObservaciones(rs.getString("observaciones"));
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudCompromisoProveedor.obtenerCompromisosProveedorPPorFechas() error " + e.getMessage());
            sqlPoolInstance.pool.releaseAllConnection();

        }
        return lista;
    }

    public entitys.CompromisosProveedor obtenerCompromisosProveedorPorId(int id) {
        entitys.CompromisosProveedor p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String sql = "select *from indicadores.dbo.CompromisosProveedor_view "
                    + "where id =" + id + ";";
            AppLogger.appLogger.log(Level.WARNING, "data.CrudCompromisoProveedor.obtenerCompromisosProveedorPPorFechas() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                p = new entitys.CompromisosProveedor();
                p.setCia_prov(rs.getString("cia_prov").trim());
                p.setProveedor(rs.getString("PROVEEDOR").trim());
                p.setNombreProveedor(rs.getString("nombreproveedor"));
                p.setId(rs.getInt("id"));
                p.setFechaCreacion(rs.getDate("fechacreacion"));
                p.setResponsable(rs.getString("responsable"));
                p.setMonto(rs.getDouble("monto"));
                p.setEstado(rs.getInt("estado"));
                p.setPeriodo(rs.getInt("periodo"));
                p.setMoneda(rs.getString("moneda"));
                p.setObservaciones(rs.getString("observaciones"));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudCompromisoProveedor.obtenerCompromisosProveedorPPorFechas() error " + e.getMessage());
            sqlPoolInstance.pool.releaseAllConnection();
            return null;

        }
        return p;
    }

    public boolean agregarCompromisosProveedor(entitys.CompromisosProveedor ab) {
        boolean res = false;
        java.sql.Date sqlCreacion = new java.sql.Date(ab.getFechaCreacion().getTime());
        String procedureCall = "INSERT INTO [dbo].[CompromisosProveedor]([cia_prov],[nombreProveedor],[responsable],[observaciones]"
                + ",[proveedor],[Monto],[estado],[periodo],[FechaCreacion],[Moneda])"
                + "VALUES('" + ab.getCia_prov() + "'"
                + ",'" + ab.getNombreProveedor() + "'"
                + ",'" + ab.getResponsable() + "'"
                + ",'" + ab.getObservaciones() + "'"
                + ",'" + ab.getProveedor() + "'"
                + ",'" + String.format("%.2f", ab.getMonto()).replace(",", ".") + "'"
                + "," + ab.getEstado()
                + "," + ab.getPeriodo()
                + ",'" + sqlCreacion + "'"
                + ",'" + ab.getMoneda() + "');";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudCompromisoProveedor.agregarCompromisosProveedor() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudCompromisoProveedor.agregarCompromisosProveedor() error " + e.getMessage());

        }
        return res;
    }

    public boolean actCompromisosProveedor(entitys.CompromisosProveedor ab) {
        boolean res = false;
        String procedureCall = "update [dbo].[CompromisosProveedor] "
                + "set [cia_prov] ='" + ab.getCia_prov() + "'"
                + ", ResponsableUltimaModificacion ='" + DataUser.username + "'"
                + ", UltimaModificacion = getdate()"
                + ",[nombreProveedor] =' " + ab.getNombreProveedor() + "'"
                + ",[observaciones] ='" + ab.getObservaciones() + "'"
                + ",[proveedor] = '" + ab.getProveedor() + "'"
                + ",[Monto] = '" + String.format("%.2f", ab.getMonto()).replace(",", ".") + "'"
                + ",[estado] =" + ab.getEstado()
                + ",[periodo] =" + ab.getPeriodo()
                + ", moneda = '" + ab.getMoneda() + "'"
                + " where id = " + ab.getId() + ";";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudCompromisoProveedor.actCompromisosProveedor() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudCompromisoProveedor.actCompromisosProveedor() error " + e.getMessage());

        }
        return res;
    }

    public boolean deleteCompromisosProveedor(int id) {
        boolean res = false;
        String procedureCall = "delete from indicadores.[dbo].[CompromisosProveedor] where id = " + id + ";";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudCompromisoProveedor.deleteCompromisosProveedor() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudCompromisoProveedor.deleteCompromisosProveedor() error " + e.getMessage());

        }
        return res;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.GastosFijosPeriodicos;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CrudGastosFijosPeriodicos {

    public boolean agregarAbonoSugerido(entitys.GastosFijosPeriodicos ab) {
        boolean res = false;
        java.sql.Date sqlFeSol = new java.sql.Date(ab.getFechaSolicitud().getTime());
        java.sql.Date sqlFeCre = new java.sql.Date(ab.getFechaCreacion().getTime());

        String procedureCall = "INSERT INTO INDICADORES.[dbo].[GastosFijosPeriodicos] "
                + "([FechaSolicitud]"
                + ",[ProveedorActividad]"
                + ",[Moneda]"
                + ",[Monto]"
                + ",[Fecuencia]"
                + ",[Estado]"
                + ",[Departamento]"
                + ",[FechaCreacion]"
                + ",[UsuarioCreador]"
                + ",[UsuarioAutoriza]"
                + ",[IdDepartamento]"
                + ",realizado"
                + ",Observaciones"
                + ",CtaPresupuesto)"
                + "VALUES("
                + "'" + sqlFeSol + "'"
                + ",'" + ab.getProveedorActividad() + "'"
                + ",'" + ab.getMoneda() + "'"
                + ",'" + ab.getMontoF() + "'"
                + ",'" + ab.getFecuencia() + "'"
                + ",'" + ab.getEstado() + "'"
                + ",'" + ab.getDepartamento() + "'"
                + ",'" + sqlFeCre + "'"
                + ",'" + ab.getUsuarioCreador() + "'"
                + ",'" + ab.getUsuarioAutoriza() + "'"
                + ",'" + ab.getIdDepartamento() + "'"
                + "," + (ab.isRealizado() ? 1 : 0)
                + ",'" + ab.getObservaciones() + "'"
                + ",'" + ab.getCtaPresupuesto() + "');";
        System.out.println("data.CrudGastosFijosPeriodicos.agregarAbonoSugerido() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            res = false;
            System.err.println("data.CrudGastosFijosPeriodicos.agregarAbonoSugerido() error " + e.getMessage());
            sqlPoolInstance.pool.releaseAllConnection();
        }
        return res;
    }

    public ArrayList<entitys.GastosFijosPeriodicos> obtenerGastosFijosPorIdDepartamento(String iddepartamento) {

        ArrayList<entitys.GastosFijosPeriodicos> lista = new ArrayList<entitys.GastosFijosPeriodicos>();
        try {
            String sql = "select *from GastosFijosPeriodicos_View where iddepartamento = '" + iddepartamento + "';";
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            System.out.println("data.CrudGastosFijosPeriodicos.obtenerGastosFijosPorIdDepartamento() sentencia\n" + sql);
            //AppLogger.appLogger.log(Level.WARNING, "sentencia " + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.GastosFijosPeriodicos p = new entitys.GastosFijosPeriodicos();
                p.setRealizado(rs.getBoolean("Realizado"));
                p.setDepartamento(rs.getString("Departamento").trim());
                p.setEstado(rs.getInt("Estado"));
                p.setFechaCreacion(rs.getDate("FechaCreacion"));
                p.setFechaSolicitud(rs.getDate("FechaSolicitud"));
                p.setFecuencia(rs.getString("Fecuencia").trim());
                p.setId(rs.getInt("Id"));
                p.setIdDepartamento(rs.getString("IdDepartamento").trim());
                p.setMoneda(rs.getString("Moneda").trim());
                p.setMonto(rs.getDouble("Monto"));
                p.setProveedorActividad(rs.getString("ProveedorActividad").trim());
                p.setUsuarioAutoriza(rs.getString("UsuarioAutoriza"));
                p.setUsuarioCreador(rs.getString("UsuarioCreador"));
                String obser = rs.getString("Observaciones");
                String cta = rs.getString("CtaPresupuesto");
                p.setObservaciones(obser == null ? "" : obser.trim());
                p.setCtaPresupuesto(cta == null ? "" : cta.trim());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudGastosFijosPeriodicos.obtenerGastosFijosPorIdDepartamento() error " + e.getMessage());
        }
        return lista;
    }

    public entitys.GastosFijosPeriodicos obtenerGastosFijoPorId(int idGasto) {

        entitys.GastosFijosPeriodicos p = null;
        try {
            String sql = "select *from GastosFijosPeriodicos_View where id = " + idGasto + ";";
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            AppLogger.appLogger.warning("data.CrudGastosFijosPeriodicos.obtenerGastosFijoPorId() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                p = new entitys.GastosFijosPeriodicos();
                p.setRealizado(rs.getBoolean("Realizado"));
                p.setDepartamento(rs.getString("Departamento").trim());
                p.setEstado(rs.getInt("Estado"));
                p.setFechaCreacion(rs.getDate("FechaCreacion"));
                p.setFechaSolicitud(rs.getDate("FechaSolicitud"));
                p.setFecuencia(rs.getString("Fecuencia").trim());
                p.setId(rs.getInt("Id"));
                p.setIdDepartamento(rs.getString("IdDepartamento").trim());
                p.setMoneda(rs.getString("Moneda").trim());
                p.setMonto(rs.getDouble("Monto"));
                p.setProveedorActividad(rs.getString("ProveedorActividad").trim());
                p.setUsuarioAutoriza(rs.getString("UsuarioAutoriza"));
                p.setUsuarioCreador(rs.getString("UsuarioCreador"));
                String obser = rs.getString("Observaciones");
                String cta = rs.getString("CtaPresupuesto");
                p.setObservaciones(obser == null ? "" : obser.trim());
                p.setCtaPresupuesto(cta == null ? "" : cta.trim());

            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            p = null;
            logic.AppLogger.appLogger.warning("data.CrudGastosFijosPeriodicos.obtenerGastosFijoPorId() error " + e.getMessage());
        }
        return p;
    }

    public ArrayList<entitys.GastosFijosPeriodicos> obtenerGastosFijosPorDepartamento(String dep, java.util.Date inicio, java.util.Date fin, int estado, boolean realizado, int AdministradorGestionGastosPer) {
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        ArrayList<entitys.GastosFijosPeriodicos> lista = new ArrayList<entitys.GastosFijosPeriodicos>();
        try {
            String sql = "select *from GastosFijosPeriodicos_View where "
                    + "departamento like '%" + dep + "%' "
                    + "and (FechaSolicitud between '" + sqlInicio + "' and '" + sqlFin + "') "
                    + (estado == 0 ? "" : "and estado = " + (estado - 1))
                    + (AdministradorGestionGastosPer == 1 ? "" : " and usuariocreador  = '" + DataUser.username + "'")
                    + " and realizado  = " + (realizado ? 1 : 0)
                    + ";";
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            System.out.println("data.CrudGastosFijosPeriodicos.obtenerGastosFijosPorIdDepartamento() sentencia " + "\n" + sql);
            //AppLogger.appLogger.log(Level.WARNING, "sentencia " + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.GastosFijosPeriodicos p = new entitys.GastosFijosPeriodicos();
                p.setRealizado(rs.getBoolean("Realizado"));
                p.setDepartamento(rs.getString("Departamento").trim());
                p.setEstado(rs.getInt("Estado"));
                p.setFechaCreacion(rs.getDate("FechaCreacion"));
                p.setFechaSolicitud(rs.getDate("FechaSolicitud"));
                p.setFecuencia(rs.getString("Fecuencia").trim());
                p.setId(rs.getInt("Id"));
                p.setIdDepartamento(rs.getString("IdDepartamento").trim());
                p.setMoneda(rs.getString("Moneda").trim());
                p.setMonto(rs.getDouble("Monto"));
                p.setProveedorActividad(rs.getString("ProveedorActividad").trim());
                p.setUsuarioAutoriza(rs.getString("UsuarioAutoriza"));
                p.setUsuarioCreador(rs.getString("UsuarioCreador"));
                String obser = rs.getString("Observaciones");
                String cta = rs.getString("CtaPresupuesto");
                p.setObservaciones(obser == null ? "" : obser);
                p.setCtaPresupuesto(cta == null ? "" : cta);
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerSubtiposCPPorFechas() error " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(GastosFijosPeriodicos g) {
        boolean res = false;
        try {   
            String sql = "update indicadores.dbo.GastosFijosPeriodicos "
                    + "set Realizado = " + (g.isRealizado() ? 1 : 0) + " "
                    + ", Monto = '" + g.getMontoF().replaceAll("\\s","") + "'"
                    + ", observaciones = '" + g.getObservaciones() + "' "
                    + ", estado = " + g.getEstado() + " "
                    + ", ProveedorActividad = '" + g.getProveedorActividad() + "'"
                    + ", UsuarioAutoriza = '" + g.getUsuarioAutoriza() + "'"
                    + " where id = " + g.getId() + ";";
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.INFO, "data.CrudGastosFijosPeriodicos.actualizar() sentencia \n" + sql);
            sta.execute(sql);
            res = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "data.CrudGastosFijosPeriodicos.actualizar() error " + e.getMessage());
            AppLogger.appLogger.warning("data.CrudGastosFijosPeriodicos.actualizar() error " + e.getMessage());
            res = false;
            sqlPoolInstance.pool.releaseAllConnection();
        }
        return res;
    }

    public boolean actualizarFromForm(GastosFijosPeriodicos g) {
        boolean res = false;
        try {
            String sql = "update indicadores.dbo.GastosFijosPeriodicos "
                    + "set Monto = '" + g.getMontoF() + "' "
                    + ", CtaPresupuesto = '" + g.getCtaPresupuesto() + "' "
                    + ", moneda = '" + g.getMoneda() + "' "
                    + ", departamento = '" + g.getDepartamento() + "' "
                    + ", iddepartamento = '" + g.getIdDepartamento() + "' "
                    + ", observaciones = '" + g.getObservaciones() + "' "
                    + ", ProveedorActividad = '" + g.getProveedorActividad() + "'"
                    + " where id = " + g.getId() + ";";
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.INFO, "data.CrudGastosFijosPeriodicos.actualizar() sentencia \n" + sql);
            sta.execute(sql);
            res = true;
        } catch (Exception e) {
            res = false;
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.warning("data.CrudGastosFijosPeriodicos.actualizar() error " + e.getMessage());
        }
        return res;
    }

    public boolean eliminar(int idGasto) {
        boolean res = false;
        try {
            String sql = "delete GastosFijosPeriodicos where id = " + idGasto + ";";
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.INFO, "data.CrudGastosFijosPeriodicos.eliminar() sentencia \n" + sql);
            sta.execute(sql);
            res = true;
        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.warning("data.CrudGastosFijosPeriodicos.eliminar() error " + e.getMessage());
        }
        return res;
    }

}

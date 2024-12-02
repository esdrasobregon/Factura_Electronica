/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.AbonoSugerido;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import logic.AppLogger;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author eobregon
 */
public class CrudAbonoSugerido {

    public boolean agregarAbonoSugerido(entitys.AbonoSugerido ab) {
        boolean res = false;
        java.sql.Date sqlCreacion = new java.sql.Date(ab.getFecha_Creacion().getTime());
        java.sql.Date sqlFechadoc = new java.sql.Date(ab.getFecha_documento().getTime());
        String procedureCall = "INSERT INTO [dbo].[AbonoSugerido]"
                + "(Documento"
                + ",[CIA]"
                + ",[Fecha_Creacion]"
                + ",[Numero_Proveedor]"
                + ",[Cuenta_Presupuesto]"
                + ",[Monto_Original]"
                + ",[Saldo_Actuual]"
                + ",[Abono]"
                + ",[Saldo_Restante]"
                + ",[Abono_Total]"
                + ",[Usuario]"
                + ",moneda,"
                + "Nombre_Proveedor,"
                + "Tipo_Proveedor,"
                + "Fecha_Documento,"
                + "semana,"
                + "Descripion_Cta_Presupuesto,"
                + "Aprobado,"
                + "Monto_Colones,"
                + "Fecha_Solicitud)"
                + "VALUES"
                + "('" + ab.getDocumento().trim() + "'"
                + ",'" + ab.getCIA().trim() + "'"
                + ",'" + sqlCreacion + "'"
                + ",'" + ab.getNumero_Proveedor().trim() + "'"
                + ",'" + ab.getCuenta_Presupuesto().trim() + "'"
                + ",'" + String.format("%.2f", ab.getMonto_Original()).replace(",", ".") + "'"
                + ",'" + String.format("%.2f", ab.getSaldo_Actuual()).replace(",", ".") + "'"
                + ",'" + String.format("%.2f", ab.getAbono()).replace(",", ".") + "'"
                + ",'" + String.format("%.2f", ab.getSaldo_Restante()).replace(",", ".") + "'"
                + ",'" + ab.getAbono_Total() + "'"
                + ",'" + ab.getUsuario() + "'"
                + ",'" + ab.getMoneda().trim() + "'"
                + ",'" + ab.getNombre_Proveedor().trim() + "'"
                + ",'" + ab.getTipo_Proveedor().trim() + "'"
                + ",'" + sqlFechadoc + "'"
                + "," + ab.getSemana() + ""
                + ",'" + ab.getDescripion_Cta_Presupuesto() + "'"
                + "," + ab.getAprobado()
                + ",'" + String.format("%.2f", ab.getMonto_Colones()).replace(",", ".") + "'"
                + ", getdate());";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.agregarAbonoSugerido() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.agregarAbonoSugerido() error " + e.getMessage());

        }
        return res;
    }

    public boolean actualizarAbonoSugerido(entitys.AbonoSugerido ab) {
        boolean res = false;
        java.sql.Date sqlCreacion = new java.sql.Date(ab.getFecha_Creacion().getTime());
        String procedureCall = "update [dbo].[AbonoSugerido] "
                + "set [Saldo_Actuual] = '" + String.format("%.2f", ab.getSaldo_Actuual()).replace(",", ".") + "'"
                + ",[Abono] = '" + String.format("%.2f", ab.getAbono()).replace(",", ".") + "'"
                + ",[Saldo_Restante]='" + String.format("%.2f", ab.getSaldo_Restante()).replace(",", ".") + "'"
                + ",[Abono_Total] = '" + ab.getAbono_Total() + "'"
                + ",cuenta_presupuesto = '" + ab.getCuenta_Presupuesto() + "'"
                + ",Aprobado = " + ab.getAprobado()
                + ",Monto_Colones = '" + String.format("%.2f", ab.getMonto_Colones()).replace(",", ".") + "'"
                + " where documento = '" + ab.getDocumento() + "' "
                + "and fecha_creacion = '" + sqlCreacion + "';";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.actualizarAbonoSugerido() sentencia " + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.actualizarAbonoSugerido() error " + e.getMessage());
        }
        return res;
    }

    public boolean actualizarAbonoSugeridoSinAp(int id, int semana) {
        boolean res = false;
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlCreacion = new java.sql.Date(date.getTime());
        String procedureCall = "update [dbo].[AbonoSugerido] "
                + "set usuario = '" + DataUser.username + "'"
                + ", Aprobado = 1 "
                + ", fecha_creacion = '" + sqlCreacion + "'"
                + ", semana = " + semana
                + " where id = " + id + ";";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.actualizarAbonoSugeridoSinAp() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.actualizarAbonoSugeridoSinAp() error " + e.getMessage());
        }
        return res;
    }

    public boolean actualizarAbonoSugeridoConta(entitys.AbonoSugerido ab) {
        boolean res = false;
        java.sql.Date sqlCreacion = new java.sql.Date(ab.getFecha_Creacion().getTime());
        java.sql.Date sqlRev = new java.sql.Date(new java.util.Date().getTime());
        String procedureCall = "update [dbo].[AbonoSugerido] "
                + "set [RevisadoConta] = '" + (ab.getRevisadoConta() == 1 ? true : false) + "'\n"
                + ",UsuarioRevision = '" + DataUser.username + "'\n"
                //+ ",[Cuenta_Presupuesto] = '" + ab.getCuenta_Presupuesto() + "'"
                + ",[Descripion_Cta_Presupuesto] = '" + ab.getDescripion_Cta_Presupuesto() + "'\n"
                + ",Fecha_Revision_Conta = '" + sqlRev + "'\n"
                + ",Exactus_Doc = '" + ab.getExactus_Doc() + "'\n"
                + ", Fecha_Revision = getdate()\n"
                + " where documento = '" + ab.getDocumento() + "' \n"
                + "and fecha_creacion = '" + sqlCreacion + "'\n"
                + "and id = " + ab.getId() + ";\n";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.actualizarAbonoSugeridoConta() sentencia " + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.actualizarAbonoSugeridoConta() error " + e.getMessage());
            System.err.println("data.CrudAbonoSugerido.actualizarAbonoSugeridoConta() error" + e.getMessage());
        }
        return res;
    }

    public boolean actualizarAbonoSugeridoCambioCuenta(entitys.AbonoSugerido ab, String cta) {
        boolean res = false;
        String procedureCall = "UPDATE indicadores.[dbo].[AbonoSugerido] "
                + "SET [Cuenta_Presupuesto] = '" + ab.getCuenta_Presupuesto() + "'"
                + ", [Descripion_Cta_Presupuesto] = '" + ab.getDescripion_Cta_Presupuesto() + "'"
                + " WHERE documento = '" + ab.getDocumento() + "'"
                + " and cia = '" + ab.getCIA() + "'"
                + " and Nombre_Proveedor = '" + ab.getNombre_Proveedor() + "'"
                + " and cuenta_presupuesto = '" + cta + "'"
                + " and monto_original = '" + String.format("%.2f", ab.getMonto_Original()).replace(",", ".") + "';";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.actualizarAbonoSugeridoCambioCuenta() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.actualizarAbonoSugeridoCambioCuenta() error " + e.getMessage());
        }
        return res;
    }

    public double getAbonosSum(Date date) {
        double res = 0;
        java.sql.Date sqlInicio = new java.sql.Date(date.getTime());
        String procedureCall = "select sum(v.monto_colones) as sumaAbonos "
                + "from indicadores.dbo.abonosugerido v "
                + "where v.fecha_creacion = '" + sqlInicio + "'"
                + "and v.Aprobado = 1";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.actualizarAbonoSugerido() sentencia " + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                res = rs.getDouble("sumaAbonos");
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.agregarAbonoSugerido() error " + e.getMessage());

        }
        return res;
    }

    public double getAbonosSum(Date init, Date end) {
        double res = 0;
        java.sql.Date sqlInicio = new java.sql.Date(init.getTime());
        java.sql.Date sqlEnd = new java.sql.Date(end.getTime());
        String procedureCall = "select sum(v.monto_colones) as sumaAbonos \n"
                + "from indicadores.dbo.abonosugerido v \n"
                + "where (v.FECHA_CREACION between '" + sqlInicio + "'\n"
                + " and '"+sqlEnd+"')\n"
                + "and v.Aprobado = 1";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.actualizarAbonoSugerido() sentencia " + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                res = rs.getDouble("sumaAbonos");
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.agregarAbonoSugerido() error " + e.getMessage());

        }
        return res;
    }

    public AbonoSugerido getAbonoSugeridos(int week, String doc, String numProveedor, String cia) {
        AbonoSugerido ab = null;
        String procedureCall = "SELECT *FROM [INDICADORES].[dbo].[VistaAbonoSugerido]"
                + "where semana = " + week
                + " and Documento = '" + doc + "'"
                + " and cia = '" + cia + "'"
                + " and Numero_Proveedor = '" + numProveedor + "';";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.getAbonoSugeridos() sentencia \n" + procedureCall);

        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                ab = new AbonoSugerido();
                ab.setCIA(rs.getString("CIA").trim());
                ab.setDocumento(rs.getString("Documento").trim());
                ab.setCuenta_Presupuesto(rs.getString("Cuenta_Presupuesto").trim());
                ab.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                ab.setNumero_Proveedor(rs.getString("Numero_Proveedor").trim());
                ab.setMonto_Original(rs.getDouble("Monto_Original"));
                ab.setSaldo_Actuual(rs.getDouble("Saldo_Actuual"));
                ab.setAbono(rs.getDouble("Abono"));
                ab.setSaldo_Restante(ab.getSaldo_Actuual() - ab.getAbono());
                ab.setAbono_Total(rs.getBoolean("Abono_Total"));
                ab.setMoneda(rs.getString("Moneda").trim());
                ab.setUsuario(rs.getString("Usuario").trim());
                ab.setNombre_Proveedor(rs.getString("Nombre_Proveedor").trim());
                ab.setFecha_documento(rs.getDate("Fecha_Documento"));
                ab.setTipo_Proveedor(rs.getString("Tipo_Proveedor").trim());
                ab.setDescripion_Cta_Presupuesto(rs.getString("Descripion_Cta_Presupuesto").trim());
                ab.setSemana(rs.getInt("Semana"));
                ab.setMonto_Colones(rs.getDouble("Monto_Colones"));
                ab.setId(rs.getInt("Id"));
                ab.setAprobado(rs.getInt("Aprobado"));
                ab.setFecha_Solicitud(rs.getDate("Fecha_Solicitud"));
                ab.setFecha_Revision(rs.getDate("Fecha_Revision"));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            ab = null;
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.getAbonoSugeridos() error " + e.getMessage());

        }
        return ab;
    }

    public boolean eliminarAbonoSugeridos(int id) {
        boolean res = false;
        String procedureCall = "delete FROM [INDICADORES].[dbo].AbonoSugerido "
                + "where id = " + id + ";";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.eliminarAbonoSugeridos() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            res = false;
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.getAbonoSugeridos() error \n" + e.getMessage());

        }
        return res;
    }

    public ArrayList<AbonoSugerido> getAbonoSugeridos(int week, int aprobado) {
        ArrayList<AbonoSugerido> res = new ArrayList<AbonoSugerido>();
        String procedureCall = "SELECT *FROM [INDICADORES].[dbo].[VistaAbonoSugerido]"
                + "where semana = " + week + " and Aprobado = " + aprobado + ";";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.getAbonoSugeridos() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                AbonoSugerido ab = new AbonoSugerido();
                ab.setCIA(rs.getString("CIA").trim());
                ab.setDocumento(rs.getString("Documento").trim());
                ab.setCuenta_Presupuesto(rs.getString("Cuenta_Presupuesto").trim());
                ab.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                ab.setNumero_Proveedor(rs.getString("Numero_Proveedor").trim());
                ab.setMonto_Original(rs.getDouble("Monto_Original"));
                ab.setSaldo_Actuual(rs.getDouble("Saldo_Actuual"));
                ab.setAbono(rs.getDouble("Abono"));
                ab.setSaldo_Restante(ab.getSaldo_Actuual() - ab.getAbono());
                ab.setAbono_Total(rs.getBoolean("Abono_Total"));
                ab.setMoneda(rs.getString("Moneda").trim());
                ab.setUsuario(rs.getString("Usuario").trim());
                ab.setNombre_Proveedor(rs.getString("Nombre_Proveedor").trim());
                ab.setFecha_documento(rs.getDate("Fecha_Documento"));
                ab.setTipo_Proveedor(rs.getString("Tipo_Proveedor").trim());
                ab.setDescripion_Cta_Presupuesto(rs.getString("Descripion_Cta_Presupuesto").trim());
                ab.setSemana(rs.getInt("Semana"));
                ab.setMonto_Colones(rs.getDouble("Monto_Colones"));
                ab.setFecha_Solicitud(rs.getTimestamp("Fecha_Solicitud"));
                ab.setFecha_Revision(rs.getTimestamp("Fecha_Revision"));
                res.add(ab);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.getAbonoSugeridos() sentencia error \n" + e.getMessage());

        }
        return res;
    }

    public ArrayList<AbonoSugerido> getHistoricoAbonoSugerido(int id) {
        ArrayList<AbonoSugerido> res = new ArrayList<AbonoSugerido>();
        String procedureCall = "SELECT *FROM [INDICADORES].[dbo].vista_AbonoSugerido_Audit"
                + " where id = " + id +";";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.getHistoricoAbonoSugerido() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                AbonoSugerido ab = new AbonoSugerido();
                ab.setCIA(rs.getString("CIA").trim());
                ab.setDocumento(rs.getString("Documento").trim());
                ab.setCuenta_Presupuesto(rs.getString("Cuenta_Presupuesto").trim());
                ab.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                ab.setNumero_Proveedor(rs.getString("Numero_Proveedor").trim());
                ab.setMonto_Original(rs.getDouble("Monto_Original"));
                ab.setSaldo_Actuual(rs.getDouble("Saldo_Actuual"));
                ab.setAbono(rs.getDouble("Abono"));
                ab.setSaldo_Restante(ab.getSaldo_Actuual() - ab.getAbono());
                ab.setAbono_Total(rs.getBoolean("Abono_Total"));
                ab.setMoneda(rs.getString("Moneda").trim());
                ab.setUsuario(rs.getString("Usuario").trim());
                ab.setNombre_Proveedor(rs.getString("Nombre_Proveedor").trim());
                ab.setFecha_documento(rs.getDate("Fecha_Documento"));
                ab.setTipo_Proveedor(rs.getString("Tipo_Proveedor").trim());
                ab.setDescripion_Cta_Presupuesto(rs.getString("Descripion_Cta_Presupuesto").trim());
                ab.setSemana(rs.getInt("Semana"));
                ab.setMonto_Colones(rs.getDouble("Monto_Colones"));
                ab.setFecha_cambio(rs.getDate("fecha_cambio"));
                ab.setUsuarioRevision(rs.getString("UsuarioRevision"));
                ab.setRevisadoConta(rs.getInt("revisadoconta"));
                ab.setFecha_Solicitud(rs.getTimestamp("Fecha_Solicitud"));
                ab.setFecha_Revision(rs.getTimestamp("Fecha_Revision"));
                res.add(ab);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.getHistoricoAbonoSugerido() error \n" + e.getMessage());

        }
        return res;
    }

    public ArrayList<AbonoSugerido> getAbonoSugeridosSinAp() {
        ArrayList<AbonoSugerido> res = new ArrayList<AbonoSugerido>();
        String procedureCall = "SELECT *FROM [INDICADORES].[dbo].[VistaAbonoSugerido]"
                + "where Aprobado = 0;";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.getAbonoSugeridosSinAp() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                AbonoSugerido ab = new AbonoSugerido();
                ab.setId(rs.getInt("id"));
                ab.setCIA(rs.getString("CIA").trim());
                ab.setDocumento(rs.getString("Documento").trim());
                ab.setCuenta_Presupuesto(rs.getString("Cuenta_Presupuesto").trim());
                ab.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                ab.setNumero_Proveedor(rs.getString("Numero_Proveedor").trim());
                ab.setMonto_Original(rs.getDouble("Monto_Original"));
                ab.setSaldo_Actuual(rs.getDouble("Saldo_Actuual"));
                ab.setAbono(rs.getDouble("Abono"));
                ab.setSaldo_Restante(ab.getSaldo_Actuual() - ab.getAbono());
                ab.setAbono_Total(rs.getBoolean("Abono_Total"));
                ab.setMoneda(rs.getString("Moneda").trim());
                ab.setUsuario(rs.getString("Usuario").trim());
                ab.setNombre_Proveedor(rs.getString("Nombre_Proveedor").trim());
                ab.setFecha_documento(rs.getDate("Fecha_Documento"));
                ab.setTipo_Proveedor(rs.getString("Tipo_Proveedor").trim());
                ab.setDescripion_Cta_Presupuesto(rs.getString("Descripion_Cta_Presupuesto").trim());
                ab.setSemana(rs.getInt("Semana"));
                ab.setMonto_Colones(rs.getDouble("Monto_Colones"));
               ab.setFecha_Solicitud(rs.getTimestamp("Fecha_Solicitud"));
                ab.setFecha_Revision(rs.getTimestamp("Fecha_Revision"));
                res.add(ab);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.getAbonoSugeridosSinAp() error \n" + e.getMessage());

        }
        return res;
    }

    public ArrayList<AbonoSugerido> getAbonoSugeridos(String cia, String prov, String moneda) {
        ArrayList<AbonoSugerido> res = new ArrayList<AbonoSugerido>();
        String procedureCall = "SELECT *FROM [INDICADORES].[dbo].[VistaAbonoSugerido]"
                + "where  Aprobado = 1 and [RevisadoConta] = 0 "
                + "and CIA like '%" + cia + "%' "
                + "and Numero_Proveedor like '%" + prov + "%' "
                + "and moneda like '%" + moneda + "%';";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.getAbonoSugeridos() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                AbonoSugerido ab = new AbonoSugerido();
                ab.setCIA(rs.getString("CIA").trim());
                ab.setDocumento(rs.getString("Documento").trim());
                ab.setCuenta_Presupuesto(rs.getString("Cuenta_Presupuesto").trim());
                ab.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                ab.setNumero_Proveedor(rs.getString("Numero_Proveedor").trim());
                ab.setMonto_Original(rs.getDouble("Monto_Original"));
                ab.setSaldo_Actuual(rs.getDouble("Saldo_Actuual"));
                ab.setAbono(rs.getDouble("Abono"));
                ab.setSaldo_Restante(ab.getSaldo_Actuual() - ab.getAbono());
                ab.setAbono_Total(rs.getBoolean("Abono_Total"));
                ab.setMoneda(rs.getString("Moneda").trim());
                ab.setUsuario(rs.getString("Usuario").trim());
                ab.setNombre_Proveedor(rs.getString("Nombre_Proveedor").trim());
                ab.setFecha_documento(rs.getDate("Fecha_Documento"));
                ab.setTipo_Proveedor(rs.getString("Tipo_Proveedor").trim());
                ab.setDescripion_Cta_Presupuesto(rs.getString("Descripion_Cta_Presupuesto").trim());
                ab.setSemana(rs.getInt("Semana"));
                ab.setMonto_Colones(rs.getDouble("Monto_Colones"));
                ab.setExactus_Doc(rs.getString("Exactus_Doc").trim());
                ab.setUsuarioRevision(rs.getString("UsuarioRevision"));
                ab.setRevisadoConta(rs.getInt("RevisadoConta"));
                ab.setFecha_Revision_Conta(rs.getDate("Fecha_Revision_Conta"));
                ab.setId(rs.getInt("id"));
                ab.setComentarios(rs.getString("observaciones"));
                ab.setDocumentoExactus(rs.getString("documentoExactus"));
                ab.setFecha_Solicitud(rs.getTimestamp("Fecha_Solicitud"));
                ab.setFecha_Revision(rs.getTimestamp("Fecha_Revision"));
                res.add(ab);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.getAbonoSugeridos() error " + e.getMessage());
        }
        return res;
    }

    public ArrayList<AbonoSugerido> getAbonoSugeridos(String cia, String prov, String moneda, boolean filtrarRev,
            int revizadoConta, java.util.Date inicio, java.util.Date fin) {
        java.sql.Date sqlIni = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        ArrayList<AbonoSugerido> res = new ArrayList<AbonoSugerido>();
        String procedureCall = "SELECT *FROM [INDICADORES].[dbo].[VistaAbonoSugerido]\n"
                + "where CIA like '%" + cia + "%' \n"
                + "and Numero_Proveedor like '%" + prov + "%' \n"
                + (filtrarRev ? " and [RevisadoConta] = " + revizadoConta + "\n" : "")
                + "and moneda like '%" + moneda + "%'"
                + " and (Fecha_Creacion between '" + sqlIni + "' and '" + sqlFin + "');";
        AppLogger.appLogger.log(Level.WARNING, "data.CrudAbonoSugerido.getAbonoSugeridos() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                AbonoSugerido ab = new AbonoSugerido();
                ab.setCIA(rs.getString("CIA").trim());
                ab.setDocumento(rs.getString("Documento").trim());
                ab.setCuenta_Presupuesto(rs.getString("Cuenta_Presupuesto").trim());
                ab.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                ab.setNumero_Proveedor(rs.getString("Numero_Proveedor").trim());
                ab.setMonto_Original(rs.getDouble("Monto_Original"));
                ab.setSaldo_Actuual(rs.getDouble("Saldo_Actuual"));
                ab.setAbono(rs.getDouble("Abono"));
                ab.setSaldo_Restante(ab.getSaldo_Actuual() - ab.getAbono());
                ab.setAbono_Total(rs.getBoolean("Abono_Total"));
                ab.setMoneda(rs.getString("Moneda").trim());
                ab.setUsuario(rs.getString("Usuario").trim());
                ab.setNombre_Proveedor(rs.getString("Nombre_Proveedor").trim());
                ab.setFecha_documento(rs.getDate("Fecha_Documento"));
                ab.setTipo_Proveedor(rs.getString("Tipo_Proveedor").trim());
                ab.setDescripion_Cta_Presupuesto(rs.getString("Descripion_Cta_Presupuesto").trim());
                ab.setSemana(rs.getInt("Semana"));
                ab.setMonto_Colones(rs.getDouble("Monto_Colones"));
                ab.setExactus_Doc(rs.getString("Exactus_Doc").trim());
                ab.setUsuarioRevision(rs.getString("UsuarioRevision"));
                ab.setRevisadoConta(rs.getInt("RevisadoConta"));
                ab.setFecha_Revision_Conta(rs.getDate("Fecha_Revision_Conta"));
                ab.setId(rs.getInt("id"));
                ab.setComentarios(rs.getString("observaciones"));
                ab.setDocumentoExactus(rs.getString("documentoExactus"));
                ab.setAprobado(rs.getInt("Aprobado"));
                ab.setFecha_Solicitud(rs.getTimestamp("Fecha_Solicitud"));
                ab.setFecha_Revision(rs.getTimestamp("Fecha_Revision"));
                res.add(ab);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudAbonoSugerido.getAbonoSugeridos() error " + e.getMessage());
        }
        return res;
    }

}

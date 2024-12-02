/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.HistoricoCP;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CrudHistoricoContado {
    
    public boolean agregarHistoricoContado(HistoricoCP h) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlDoc = new java.sql.Date(h.getFECHA_DOCUMENTO().getTime());
            java.sql.Date sqlCre = new java.sql.Date(h.getFecha_Creacion().getTime());
            java.sql.Date sqlVen = new java.sql.Date(h.getFECHA_VENCE().getTime());
            String Sql = "INSERT INTO indicadores.[dbo].[HistoricoCPContado]([MONEDA],[TIPOPROV],[PROVEEDOR],[Nombre] ,[DOCUMENTO],[FECHA_DOCUMENTO] ,[FECHA_VENCE],[NOTAS],[diasmora],[TIPO_MORA],[MONTO],[SALDO],[Monto_colones],[Saldo_colones]"
                    + ",[CTA_PRESUPUESTO],[APLICACION],[EMBARQUE],[CONTA_CRED],[CIA],[CREADOR],[FECHA_CREACION], Tipo_Documento, Desc_Cta_Pres, Forma_Pago, adelanto)"
                    + "VALUES('" + h.getMONEDA() + "'"
                    + ",'" + h.getTIPOPROV() + "'"
                    + ",'" + h.getPROVEEDOR() + "'"
                    + ",'" + h.getNOMBRE() + "'"
                    + ",'" + h.getDOCUMENTO() + "'"
                    + ",'" + sqlDoc + "'"
                    + ",'" + sqlVen + "'"
                    + ",'" + h.getNotas() + "'"
                    + "," + h.getDIAS_CREDITO()
                    + ",'" + h.getESTAD_MORA() + "'"
                    + ",'" + String.format("%.2f", h.getMONTO()) + "'"
                    + ",'" + String.format("%.2f", h.getSaldo()) + "'"
                    + ",'" + String.format("%.2f", h.getMonto_colones()) + "'"
                    + ",'" + String.format("%.2f", h.getSaldo_colones()) + "'"
                    + ",'" + h.getCTA_PRESUPUESTO().trim() + "'"
                    + ",'" + h.getAplicacion() + "'"
                    + ",'EMBARQUE'"
                    + ",'" + h.getCONTA_CRED() + "'"
                    + ",'" + h.getCIA() + "'"
                    + ",'" + h.getCreador() + "'"
                    + ",'" + sqlCre + "'"
                    + ",'" + h.getTipo_Documento() + "'"
                    + ",'" + h.getDesc_Cta_Pres() + "'"
                    + ",'" + h.getForma_Pago() + "'"
                    + "," + h.getAdelanto() + ");";
            
            System.out.println("data.CRUDTipoCambio.agregarHistoricoContado() sentencia \n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDTipoCambio.agregarHistoricoContado() error " + e.getMessage());
            System.out.println("data.CRUDTipoCambio.agregarHistoricoContado() error " + e.getMessage());
            res = false;
        }
        return res;
    }
    
    public int agregarHistoricoContado1(HistoricoCP h) {
        int res = -1;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlDoc = new java.sql.Date(h.getFECHA_DOCUMENTO().getTime());
            java.sql.Date sqlCre = new java.sql.Date(h.getFecha_Creacion().getTime());
            java.sql.Date sqlVen = new java.sql.Date(h.getFECHA_VENCE().getTime());
            String Sql = "exec indicadores.dbo.agregarHistoricoCPContado \n"
                    + "'" + h.getMONEDA() + "'--@MONEDA nvarchar(10)\n"
                    + ",'" + h.getTIPOPROV() + "'--@TIPOPROV nvarchar(10)\n"
                    + ",'" + h.getPROVEEDOR() + "'--@PROVEEDOR nvarchar(10)\n"
                    + ",'" + h.getNOMBRE() + "'--@Nombre nvarchar(300)\n"
                    + ",'" + h.getDOCUMENTO() + "'--@DOCUMENTO nvarchar(30)\n"
                    + ",'" + sqlDoc + "'--@FECHA_DOCUMENTO date\n"
                    + ",'" + sqlVen + "'--@FECHA_VENCE date\n"
                    + ",'" + h.getNotas() + "'--@NOTAS nvarchar(300)\n"
                    + "," + h.getDIAS_CREDITO() + "--@diasmora int\n"
                    + ",'" + h.getESTAD_MORA() + "'--@TIPO_MORA nvarchar(30)\n"
                    + ",'" + String.format("%.2f", h.getMONTO())
                            .replace(",", ".") + "'--@MONTO money\n"
                    + ",'" + String.format("%.2f", h.getSaldo())
                            .replace(",", ".") + "'--@SALDO money\n"
                    + ",'" + String.format("%.2f", h.getMonto_colones())
                            .replace(",", ".") + "'--@Monto_colones money\n"
                    + ",'" + String.format("%.2f", h.getSaldo_colones())
                            .replace(",", ".") + "'--@Saldo_colones money\n"
                    + ",'" + h.getCTA_PRESUPUESTO().trim() + "'--@CTA_PRESUPUESTO nvarchar(50)\n"
                    + ",'" + h.getAplicacion() + "'--@APLICACION varchar(300)\n"
                    + ",'EMBARQUE'--@embarque\n"
                    + ",'" + h.getCONTA_CRED() + "'--@CONTA_CRED nvarchar(30)\n"
                    + ",'" + h.getCIA() + "'--@CIA nvarchar(10)\n"
                    + ",'" + h.getCreador() + "'--@CREADOR nvarchar(30)\n"
                    + ",'" + sqlCre + "'--@FECHA_CREACION date\n"
                    + ",'" + h.getTipo_Documento() + "'--@Tipo_Documento varchar(30)\n"
                    + ",'" + h.getDesc_Cta_Pres() + "'--@Desc_Cta_Pres varchar(3000)\n"
                    + ",'" + h.getForma_Pago() + "'--@Forma_Pago varchar(300)"
                    + "," + h.getAdelanto() + ";--@adelanto bit\n";
            
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDTipoCambio.agregarHistoricoContado1() sentencia \n" + Sql);
            /*ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                res = rs.getInt(1);
            }*/
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDTipoCambio.agregarHistoricoContado1() error " + e.getMessage());
            System.out.println("data.CRUDTipoCambio.agregarHistoricoContado() error " + e.getMessage());
            res = -1;
        }
        return res;
    }
    
    public int agregarHistoricoContado2(HistoricoCP h) {
        int res = -1;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlDoc = new java.sql.Date(h.getFECHA_DOCUMENTO().getTime());
            java.sql.Date sqlCre = new java.sql.Date(h.getFecha_Creacion().getTime());
            java.sql.Date sqlVen = new java.sql.Date(h.getFECHA_VENCE().getTime());
            String Sql = "exec indicadores.dbo.agregarHistoricoCPContado \n"
                    + "'" + h.getMONEDA() + "'--@MONEDA nvarchar(10)\n"
                    + ",'" + h.getTIPOPROV() + "'--@TIPOPROV nvarchar(10)\n"
                    + ",'" + h.getPROVEEDOR() + "'--@PROVEEDOR nvarchar(10)\n"
                    + ",'" + h.getNOMBRE() + "'--@Nombre nvarchar(300)\n"
                    + ",'" + h.getDOCUMENTO() + "'--@DOCUMENTO nvarchar(30)\n"
                    + ",'" + sqlDoc + "'--@FECHA_DOCUMENTO date\n"
                    + ",'" + sqlVen + "'--@FECHA_VENCE date\n"
                    + ",'" + h.getNotas() + "'--@NOTAS nvarchar(300)\n"
                    + "," + h.getDIAS_CREDITO() + "--@diasmora int\n"
                    + ",'" + h.getESTAD_MORA() + "'--@TIPO_MORA nvarchar(30)\n"
                    + ",'" + String.format("%.2f", h.getMONTO())
                            .replace(",", ".") + "'--@MONTO money\n"
                    + ",'" + String.format("%.2f", h.getSaldo())
                            .replace(",", ".") + "'--@SALDO money\n"
                    + ",'" + String.format("%.2f", h.getMonto_colones())
                            .replace(",", ".") + "'--@Monto_colones money\n"
                    + ",'" + String.format("%.2f", h.getSaldo_colones())
                            .replace(",", ".") + "'--@Saldo_colones money\n"
                    + ",'" + h.getCTA_PRESUPUESTO().trim() + "'--@CTA_PRESUPUESTO nvarchar(50)\n"
                    + ",'" + h.getAplicacion() + "'--@APLICACION varchar(300)\n"
                    + ",'EMBARQUE'--@embarque\n"
                    + ",'" + h.getCONTA_CRED() + "'--@CONTA_CRED nvarchar(30)\n"
                    + ",'" + h.getCIA() + "'--@CIA nvarchar(10)\n"
                    + ",'" + h.getCreador() + "'--@CREADOR nvarchar(30)\n"
                    + ",'" + sqlCre + "'--@FECHA_CREACION date\n"
                    + ",'" + h.getTipo_Documento() + "'--@Tipo_Documento varchar(30)\n"
                    + ",'" + h.getDesc_Cta_Pres() + "'--@Desc_Cta_Pres varchar(3000)\n"
                    + ",'" + h.getForma_Pago() + "'--@Forma_Pago varchar(300)\n"
                    + "," + h.getAdelanto() + ";--@adelanto bit\n";
            
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDTipoCambio.agregarHistoricoContado2() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                res = rs.getInt(1);
            }
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDTipoCambio.agregarHistoricoContado2() error " + e.getMessage());
            res = -1;
        }
        return res;
    }
    
    public boolean ActualizarHistoricoContado(HistoricoCP h) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlDoc = new java.sql.Date(h.getFECHA_DOCUMENTO().getTime());
            java.sql.Date sqlCre = new java.sql.Date(h.getFecha_Creacion().getTime());
            java.sql.Date sqlVen = new java.sql.Date(h.getFECHA_VENCE().getTime());
            String Sql = "update indicadores.[dbo].[HistoricoCPContado] \n"
                    + "set TIPOPROV ='" + h.getTIPOPROV() + "'\n"
                    + ", PROVEEDOR='" + h.getPROVEEDOR() + "'\n"
                    + ", Nombre = '" + h.getNOMBRE() + "'\n"
                    + ", DOCUMENTO = '" + h.getDOCUMENTO() + "'\n"
                    + ", FECHA_DOCUMENTO = '" + sqlDoc + "'\n"
                    + ", FECHA_VENCE = '" + sqlVen + "'\n"
                    + ", NOTAS = '" + h.getNotas() + "'\n"
                    + ", diasmora = " + h.getDIAS_CREDITO() + "\n"
                    + ", TIPO_MORA = '" + h.getESTAD_MORA() + "'\n"
                    + ", MONTO = '" + String.format("%.2f", h.getMONTO()).replace(",", ".") + "'\n"
                    + ", SALDO = '" + String.format("%.2f", h.getSaldo()).replace(",", ".") + "'\n"
                    + ", Monto_colones = '" + String.format("%.2f", h.getMonto_colones()).replace(",", ".") + "'\n"
                    + ", Saldo_colones = '" + String.format("%.2f", h.getSaldo_colones()).replace(",", ".") + "'\n"
                    + ", CTA_PRESUPUESTO = '" + h.getCTA_PRESUPUESTO().trim() + "'\n"
                    + ", APLICACION = '" + h.getAplicacion() + "'\n"
                    + ", CONTA_CRED = '" + h.getCONTA_CRED() + "'\n"
                    + ", CIA = '" + h.getCIA() + "'\n"
                    + ", FECHA_CREACION = '" + sqlCre + "'\n"
                    + ", Tipo_Documento = '" + h.getTipo_Documento() + "'\n"
                    + ", Forma_Pago = '" + h.getForma_Pago() + "' \n"
                    + ", Desc_Cta_Pres = '" + h.getDesc_Cta_Pres() + "' \n"
                    + ", creador = '" + DataUser.username + "' \n"
                    + ", adelanto = '" + h.getAdelanto() + "' \n"
                    + "where id = " + h.getId() + ";";
            AppLogger.appLogger.log(Level.WARNING, "data.CrudHistoricoContado.ActualizarHistoricoContado() sentencia "
                    + "\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDTipoCambio.ActualizarHistoricoContado() error " + e.getMessage());
            res = false;
        }
        return res;
    }
    
    public boolean ActualizarHistoricoContadoConta(HistoricoCP h) {
        boolean res = false;
        try {
            java.util.Date date = new java.util.Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(date);
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            java.sql.Date sqlRev = new java.sql.Date(h.getFecha_Revision_Conta().getTime());
            String Sql = "update indicadores.[dbo].[HistoricoCPContado] "
                    + "set RevisadoConta = " + h.getRevisadoConta()
                    + ", UsuarioRevision = '" + h.getUsuarioRevision() + "'"
                    + ", Fecha_Revision_Conta = '" + formattedDate + "'"
                    + " where id = " + h.getId() + ";";
            
            System.out.println("data.CRUDTipoCambio.ActualizarHistoricoContado() sentencia \n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDTipoCambio.ActualizarHistoricoContado() error " + e.getMessage());
            System.out.println("data.CRUDTipoCambio.ActualizarHistoricoContado() error " + e.getMessage());
            res = false;
        }
        return res;
    }
    
    public ArrayList<entitys.HistoricoCP> obtenerHistoricoCPContado_View(String cia, java.util.Date ini, java.util.Date fin) {
        ArrayList<entitys.HistoricoCP> lista = new ArrayList<entitys.HistoricoCP>();
        java.sql.Date sqlInicio = new java.sql.Date(ini.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select * from indicadores.dbo.HistoricoCPContado_View p"
                    + " where cia like '%" + cia + "%' "
                    + "and [Fecha_Creacion] between '" + sqlInicio + "' "
                    + "and '" + sqlFin + "';";
            Statement sta = connection.createStatement();
            System.out.println("data.CRUDHistoricoCP.obtenerHistoricoCPContado_View() sentencia \n" + sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                double calc_saldo_colones = rs.getDouble("calc_saldo_colones");
                String scalc_saldo_colones = rs.getString("calc_saldo_colones");
                String scalk_saldo = rs.getString("calk_saldo");
                String cta = rs.getString("Desc_Cta_Pres");
                entitys.HistoricoCP p = new entitys.HistoricoCP();
                p.setCIA(rs.getString("CIA").trim());
                p.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                p.setNOMBRE(rs.getString("NOMBRE"));
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                p.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                p.setSaldo(scalk_saldo == null ? rs.getDouble("saldo") : Double.parseDouble(scalk_saldo));
                p.setMonto_colones(rs.getDouble("Monto_colones"));
                p.setSaldo_colones((scalc_saldo_colones == null ? rs.getDouble("saldo_colones") : calc_saldo_colones));
                p.setMONEDA(rs.getString("MONEDA").trim());
                p.setCONTA_CRED(rs.getString("CONTA_CRED"));
                p.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO"));
                p.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                p.setMONTO(rs.getDouble("MONTO"));
                p.setAbono(rs.getDouble("abono_pendiente"));
                p.setCreador(rs.getString("Creador"));
                p.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                p.setId(rs.getInt("id"));
                p.setNotas(rs.getString("Notas"));
                p.setTipo_Documento(rs.getString("Tipo_Documento"));
                p.setForma_Pago(rs.getString("Forma_Pago"));
                p.setAdelanto(rs.getInt("adelanto"));
                p.setDesc_Cta_Pres(cta == null ? "" : cta);
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerSubtiposCPPorFechas() error " + e.getMessage());
            
        }
        return lista;
    }
    
    public entitys.HistoricoCP obtenerHistoricoCPContado(entitys.HistoricoCP h) {
        entitys.HistoricoCP p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select * from indicadores.dbo.HistoricoCPContado_View p"
                    + " where cia = '" + h.getCIA() + "' "
                    + "and documento ='" + h.getDOCUMENTO() + "' "
                    + "and proveedor = '" + h.getPROVEEDOR() + "';";
            Statement sta = connection.createStatement();
            System.out.println("data.CRUDHistoricoCP.obtenerHistoricoCPContado() sentencia \n" + sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                String cta = rs.getString("Desc_Cta_Pres");
                p = new entitys.HistoricoCP();
                p.setCIA(rs.getString("CIA").trim());
                p.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                p.setNOMBRE(rs.getString("NOMBRE"));
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                p.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                p.setSaldo(rs.getDouble("SALDO"));
                p.setMonto_colones(rs.getDouble("Monto_colones"));
                p.setSaldo_colones(rs.getDouble("Saldo_colones"));
                p.setMONEDA(rs.getString("MONEDA").trim());
                p.setCONTA_CRED(rs.getString("CONTA_CRED"));
                p.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO"));
                p.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                p.setMONTO(rs.getDouble("MONTO"));
                p.setCreador(rs.getString("Creador"));
                p.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                p.setId(rs.getInt("id"));
                p.setNotas(rs.getString("Notas"));
                p.setTipo_Documento(rs.getString("Tipo_Documento"));
                p.setForma_Pago(rs.getString("Forma_Pago"));
                p.setAdelanto(rs.getInt("adelanto"));
                p.setDesc_Cta_Pres(cta == null ? "" : cta);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerHistoricoCPContado() error " + e.getMessage());
            p = null;
        }
        return p;
    }
    
    public entitys.HistoricoCP obtenerHistoricoCPContadoById(int id) {
        entitys.HistoricoCP p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select * from indicadores.dbo.HistoricoCPContado_View p"
                    + " where id = " + id + ";";
            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDHistoricoCP.obtenerHistoricoCPContadoById() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                p = new entitys.HistoricoCP();
                String cta = rs.getString("Desc_Cta_Pres");
                p.setCIA(rs.getString("CIA").trim());
                p.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                p.setNOMBRE(rs.getString("NOMBRE"));
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                p.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                p.setSaldo(rs.getDouble("calk_saldo"));
                p.setMonto_colones(rs.getDouble("Monto_colones"));
                p.setSaldo_colones(rs.getDouble("calc_saldo_colones"));
                p.setMONEDA(rs.getString("MONEDA").trim());
                p.setCONTA_CRED(rs.getString("CONTA_CRED"));
                p.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO"));
                p.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                p.setMONTO(rs.getDouble("MONTO"));
                p.setCreador(rs.getString("Creador"));
                p.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                p.setId(rs.getInt("id"));
                p.setNotas(rs.getString("Notas"));
                p.setTipo_Documento(rs.getString("Tipo_Documento"));
                p.setForma_Pago(rs.getString("Forma_Pago"));
                p.setAdelanto(rs.getInt("adelanto"));
                p.setDesc_Cta_Pres(cta == null ? "" : cta);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerHistoricoCPContado() error " + e.getMessage());
            p = null;
        }
        return p;
    }
    
    public ArrayList<entitys.HistoricoCP> obtenerHistoricoCPContado_ViewConta(String cia, String moneda, String prov) {
        ArrayList<entitys.HistoricoCP> lista = new ArrayList<entitys.HistoricoCP>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select * from indicadores.dbo.HistoricoCPContado_View p"
                    + " where cia like '%" + cia + "%' "
                    + "and [moneda] like '%" + moneda + "%'"
                    + "and [nombre] like '%" + prov + "%'"
                    + "and [RevisadoConta] = 0;";
            Statement sta = connection.createStatement();
            System.out.println("data.CRUDHistoricoCP.obtenerHistoricoCPContado_ViewConta() sentencia \n" + sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                String cta = rs.getString("Desc_Cta_Pres");
                entitys.HistoricoCP p = new entitys.HistoricoCP();
                p.setCIA(rs.getString("CIA").trim());
                p.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                p.setNOMBRE(rs.getString("NOMBRE"));
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                p.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                p.setSaldo(rs.getDouble("SALDO"));
                p.setMonto_colones(rs.getDouble("Monto_colones"));
                p.setSaldo_colones(rs.getDouble("Saldo_colones"));
                p.setMONEDA(rs.getString("MONEDA").trim());
                p.setCONTA_CRED(rs.getString("CONTA_CRED"));
                p.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO"));
                p.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                p.setMONTO(rs.getDouble("MONTO"));
                p.setCreador(rs.getString("Creador"));
                p.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                p.setId(rs.getInt("id"));
                p.setNotas(rs.getString("Notas"));
                p.setTipo_Documento(rs.getString("Tipo_Documento"));
                p.setForma_Pago(rs.getString("Forma_Pago"));
                p.setAdelanto(rs.getInt("adelanto"));
                p.setDesc_Cta_Pres(cta == null ? "" : cta);
                p.setAbono(rs.getDouble("Abono"));
                p.setAbono_colones(rs.getDouble("abono_colones"));
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
            
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerHistoricoCPContado_ViewConta() error " + e.getMessage());
            System.out.println("data.CRUDHistoricoCP.obtenerHistoricoCPContado_ViewConta() error " + e.getMessage());
        }
        return lista;
    }
    
}

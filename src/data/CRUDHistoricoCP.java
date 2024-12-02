/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.AbonoSugerido;
import entitys.HistoricoCP;
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
public class CRUDHistoricoCP {

    public ArrayList<entitys.HistoricoCP> obtenerHistoricoCPPorFechas1(String sql) {
        ArrayList<entitys.HistoricoCP> lista = new ArrayList<entitys.HistoricoCP>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDHistoricoCP.obtenerSubtiposCPPorFechas() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
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

    public ArrayList<entitys.HistoricoCP> obtenerHistoricoCPContado_View(String cia) {
        ArrayList<entitys.HistoricoCP> lista = new ArrayList<entitys.HistoricoCP>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select * from indicadores.dbo.HistoricoCPContado_View p where cia like '%" + cia + "%';";
            Statement sta = connection.createStatement();
            System.out.println("data.CRUDHistoricoCP.obtenerHistoricoCPContado_View() sentencia \n" + sql);
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
                p.setDesc_Cta_Pres(cta == null ? "" : cta);
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

    public entitys.HistoricoCP obtHistoricoCPContadPorProvDoc(String proveedor, String documento) {
        entitys.HistoricoCP p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select * from indicadores.dbo.HistoricoCPContado_View p "
                    + "where proveedor ='" + proveedor + "'\n"
                    + " and documento= '" + documento + "';";
            Statement sta = connection.createStatement();
            System.out.println("data.CRUDHistoricoCP.obtenerHistoricoCPContado_View() sentencia \n" + sql);
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
                p.setDesc_Cta_Pres(cta == null ? "" : cta);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerSubtiposCPPorFechas() error " + e.getMessage());
            return null;
        }
        return p;
    }

    public ArrayList<entitys.SumasTransacciones> obtenerSumaHistoricoCPContado_View(java.util.Date fecha) {
        ArrayList<entitys.SumasTransacciones> lista = new ArrayList<entitys.SumasTransacciones>();
        java.sql.Date sqlInicio = new java.sql.Date(fecha.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select "
                    + "sum(monto)as sumMonto"
                    + ",sum(saldo) as sumSaldo"
                    + ",sum(monto_colones) as sumMontoColones"
                    + ",sum(saldo_colones) as sumSaldoColones"
                    + ", moneda from indicadores.dbo.HistoricoCPContado_View v"
                    + " where v.FECHA_CREACION ='" + sqlInicio + "' group by moneda;";
            Statement sta = connection.createStatement();
            System.out.println("data.CRUDHistoricoCP.obtenerSumaHistoricoCPContado_View() sentencia \n" + sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.SumasTransacciones p = new entitys.SumasTransacciones();
                p.setSumMonto(rs.getDouble("sumMonto"));
                p.setSumSaldo(rs.getDouble("sumSaldo"));
                p.setSumMontoColones(rs.getDouble("sumMontoColones"));
                p.setSumSaldoColones(rs.getDouble("sumSaldoColones"));
                p.setMoneda(rs.getString("moneda"));

                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerSumaHistoricoCPContado_View() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.SumasTransacciones> obtenerSumaHistoricoCPContado_View(java.util.Date init, java.util.Date end) {
        ArrayList<entitys.SumasTransacciones> lista = new ArrayList<entitys.SumasTransacciones>();
        java.sql.Date sqlInicio = new java.sql.Date(init.getTime());
        java.sql.Date sqlEnd = new java.sql.Date(end.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select "
                    + "sum(monto)as sumMonto\n"
                    + ",sum(saldo) as sumSaldo\n"
                    + ",sum(monto_colones) as sumMontoColones\n"
                    + ",sum(saldo_colones) as sumSaldoColones\n"
                    + ", moneda from indicadores.dbo.HistoricoCPContado_View v \n"
                    + " where (v.FECHA_CREACION between '" + sqlInicio + "' and \n"
                    + "'" + sqlEnd + "') group by moneda;";
            Statement sta = connection.createStatement();
            System.out.println("data.CRUDHistoricoCP.obtenerSumaHistoricoCPContado_View() sentencia \n" + sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.SumasTransacciones p = new entitys.SumasTransacciones();
                p.setSumMonto(rs.getDouble("sumMonto"));
                p.setSumSaldo(rs.getDouble("sumSaldo"));
                p.setSumMontoColones(rs.getDouble("sumMontoColones"));
                p.setSumSaldoColones(rs.getDouble("sumSaldoColones"));
                p.setMoneda(rs.getString("moneda"));

                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerSumaHistoricoCPContado_View() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.HistoricoCP> getcp_cilt_rymsaPlusAbonoSugerido(java.util.Date inicio, java.util.Date fin,
            String CIA, String numeroProveedor, String tipo_mora, String moneda, String tipoprov, String nombreProv) {
        ArrayList<entitys.HistoricoCP> lista = new ArrayList<entitys.HistoricoCP>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select *from indicadores.dbo.cp_cilt_rymsaPlusAbonoSugeridoview v \n"
                    + "where v.cia like '%" + CIA + "%' \n"
                    + "and v.Proveedor like '%" + numeroProveedor + "%' \n"
                    + "and v.Tipo_mora like '%" + tipo_mora + "%' \n"
                    + "and v.moneda like '%" + moneda + "%' \n"
                    + "and v.tipoprov like '%" + tipoprov + "%'\n"
                    + "and v.nombre like '%" + nombreProv + "%'\n"
                    + " order by v.FECHA_DOCUMENTO asc, v.Nombre asc; ";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDHistoricoCP.getcp_cilt_rymsaPlusAbonoSugerido() sentencia\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {

                String sugDocumento = rs.getString("sugDOCUMENTO");
                String cia = rs.getString("CIA").trim();
                String prov = rs.getString("PROVEEDOR").trim();
                if (sugDocumento != null) {
                    HistoricoCP hcp = HistoricoCP.obtenerHistoricoPorDocCIAProv(sugDocumento.trim(), cia, prov, lista);
                    if (hcp == null) {
                        hcp = new entitys.HistoricoCP();
                        hcp.setDOCUMENTO(rs.getString("DOCUMENTO").trim());
                        hcp.setCIA(rs.getString("CIA").trim());
                        hcp.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                        hcp.setNOMBRE(rs.getString("NOMBRE"));
                        hcp.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                        hcp.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                        hcp.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                        hcp.setSaldo(rs.getDouble("SALDO"));
                        hcp.setMonto_colones(rs.getDouble("Monto_colones"));
                        hcp.setSaldo_colones(rs.getDouble("Saldo_colones"));
                        hcp.setMONEDA(rs.getString("MONEDA").trim());
                        hcp.setCONTA_CRED(rs.getString("CONTA_CRED").trim());
                        hcp.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO").trim());
                        hcp.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                        hcp.setMONTO(rs.getDouble("MONTO"));
                        hcp.setComentario(rs.getString("comentario"));
                        hcp.setCredito_proveedor(rs.getInt("credito_proveedor"));
                        lista.add(hcp);
                    }
                    AbonoSugerido a = new AbonoSugerido();
                    a.setDocumento(sugDocumento.trim());
                    a.setCIA(rs.getString("sugCIA"));
                    a.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                    a.setNumero_Proveedor(rs.getString("Numero_Proveedor").trim());
                    a.setCuenta_Presupuesto(rs.getString("sugCta").trim());
                    a.setMonto_Original(rs.getDouble("Monto_Original"));
                    a.setSaldo_Actuual(rs.getDouble("Saldo_actuual"));
                    a.setAbono(rs.getDouble("Abono"));
                    a.setSaldo_Restante(rs.getDouble("Saldo_restante"));
                    a.setAbono_Total(rs.getBoolean("Abono_Total"));
                    a.setMoneda(rs.getString("sugMoneda").trim());
                    a.setUsuario(rs.getString("Usuario"));
                    a.setAprobado(rs.getInt("Aprobado"));
                    a.setMonto_Colones(rs.getDouble("sugMonto_Colones"));
                    a.setId(rs.getInt("id"));
                    a.setRevisadoConta(rs.getInt("RevisadoConta"));
                    a.setSemana(rs.getInt("semana"));
                    hcp.getSugeridos().add(a);

                } else {
                    entitys.HistoricoCP p = new entitys.HistoricoCP();
                    p.setDOCUMENTO(rs.getString("DOCUMENTO").trim());
                    p.setCIA(rs.getString("CIA").trim());
                    p.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                    p.setNOMBRE(rs.getString("NOMBRE"));
                    p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                    p.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                    p.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                    p.setSaldo(rs.getDouble("SALDO"));
                    p.setMonto_colones(rs.getDouble("Monto_colones"));
                    p.setSaldo_colones(rs.getDouble("Saldo_colones"));
                    p.setMONEDA(rs.getString("MONEDA").trim());
                    p.setCONTA_CRED(rs.getString("CONTA_CRED").trim());
                    p.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO").trim());
                    p.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                    p.setMONTO(rs.getDouble("MONTO"));
                    p.setComentario(rs.getString("comentario"));
                    p.setCredito_proveedor(rs.getInt("credito_proveedor"));
                    lista.add(p);
                }

            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.getcp_cilt_rymsaPlusAbonoSugerido() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.HistoricoCP> getcp_cilt_rymsaPlusAbonoSugeridoResumenSemanal(int semana) {
        ArrayList<entitys.HistoricoCP> lista = new ArrayList<entitys.HistoricoCP>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select *from indicadores.dbo.cp_cilt_rymsaPlusAbonoSugeridoview a \n"
                    + "where a.abonoDocumento is not null \n"
                    + " and a.semana = " + semana + " \n"
                    + "and a.DOCUMENTO is not null;";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDHistoricoCP.getcp_cilt_rymsaPlusAbonoSugeridoResumenSemanal() sentencia\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {

                String sugDocumento = rs.getString("sugDOCUMENTO");
                String cia = rs.getString("CIA").trim();
                String prov = rs.getString("PROVEEDOR").trim();
                if (sugDocumento != null) {
                    HistoricoCP hcp = HistoricoCP.obtenerHistoricoPorDocCIAProv(sugDocumento.trim(), cia, prov, lista);
                    if (hcp == null) {
                        hcp = new entitys.HistoricoCP();
                        hcp.setDOCUMENTO(rs.getString("DOCUMENTO").trim());
                        hcp.setCIA(rs.getString("CIA").trim());
                        hcp.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                        hcp.setNOMBRE(rs.getString("NOMBRE"));
                        hcp.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                        hcp.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                        hcp.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                        hcp.setSaldo(rs.getDouble("SALDO"));
                        hcp.setMonto_colones(rs.getDouble("Monto_colones"));
                        hcp.setSaldo_colones(rs.getDouble("Saldo_colones"));
                        hcp.setMONEDA(rs.getString("MONEDA").trim());
                        hcp.setCONTA_CRED(rs.getString("CONTA_CRED").trim());
                        hcp.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO").trim());
                        hcp.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                        hcp.setMONTO(rs.getDouble("MONTO"));
                        hcp.setComentario(rs.getString("comentario"));
                        hcp.setCredito_proveedor(rs.getInt("credito_proveedor"));
                        lista.add(hcp);
                    }
                    AbonoSugerido a = new AbonoSugerido();
                    a.setDocumento(sugDocumento.trim());
                    a.setCIA(rs.getString("sugCIA"));
                    a.setFecha_Creacion(rs.getDate("Fecha_Creacion"));
                    a.setNumero_Proveedor(rs.getString("Numero_Proveedor").trim());
                    a.setCuenta_Presupuesto(rs.getString("sugCta").trim());
                    a.setMonto_Original(rs.getDouble("Monto_Original"));
                    a.setSaldo_Actuual(rs.getDouble("Saldo_actuual"));
                    a.setAbono(rs.getDouble("Abono"));
                    a.setSaldo_Restante(rs.getDouble("Saldo_restante"));
                    a.setAbono_Total(rs.getBoolean("Abono_Total"));
                    a.setMoneda(rs.getString("sugMoneda").trim());
                    a.setUsuario(rs.getString("Usuario"));
                    a.setAprobado(rs.getInt("Aprobado"));
                    a.setMonto_Colones(rs.getDouble("sugMonto_Colones"));
                    a.setId(rs.getInt("id"));
                    a.setRevisadoConta(rs.getInt("RevisadoConta"));
                    a.setSemana(rs.getInt("semana"));
                    hcp.getSugeridos().add(a);

                } else {
                    entitys.HistoricoCP p = new entitys.HistoricoCP();
                    p.setDOCUMENTO(rs.getString("DOCUMENTO").trim());
                    p.setCIA(rs.getString("CIA").trim());
                    p.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                    p.setNOMBRE(rs.getString("NOMBRE"));
                    p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                    p.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                    p.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                    p.setSaldo(rs.getDouble("SALDO"));
                    p.setMonto_colones(rs.getDouble("Monto_colones"));
                    p.setSaldo_colones(rs.getDouble("Saldo_colones"));
                    p.setMONEDA(rs.getString("MONEDA").trim());
                    p.setCONTA_CRED(rs.getString("CONTA_CRED").trim());
                    p.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO").trim());
                    p.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                    p.setMONTO(rs.getDouble("MONTO"));
                    p.setComentario(rs.getString("comentario"));
                    p.setCredito_proveedor(rs.getInt("credito_proveedor"));
                    lista.add(p);
                }

            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.getcp_cilt_rymsaPlusAbonoSugerido() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.HistoricoCP> obtenerHistoricoCPPorFechas(java.util.Date inicio, java.util.Date fin) {
        ArrayList<entitys.HistoricoCP> lista = new ArrayList<entitys.HistoricoCP>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select *from exactus.dbo.cp_cilt_rymsa h";
            System.out.println("data.CRUDHistoricoCP.obtenerSubtiposCPPorFechas() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.HistoricoCP p = new entitys.HistoricoCP();
                p.setCIA(rs.getString("CIA").trim());
                p.setPROVEEDOR(rs.getString("PROVEEDOR").trim());
                //p.setACTIVO(rs.getString("ACTIVO"));
                //p.setDIAS_CREDITO(rs.getInt("DIAS_CREDITO"));
                //p.setCATEGORIA(rs.getString("CATEGORIA"));
                p.setNOMBRE(rs.getString("NOMBRE"));
                //p.setCIA_PROV(rs.getString("CIA_PROV"));
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setFECHA_VENCE(rs.getDate("FECHA_VENCE"));
                p.setESTAD_MORA(rs.getString("TIPO_MORA").trim());
                p.setSaldo(rs.getDouble("SALDO"));
                p.setMonto_colones(rs.getDouble("Monto_colones"));
                p.setSaldo_colones(rs.getDouble("Saldo_colones"));
                //p.setSALDOLOCAL(rs.getDouble("SALDOLOCAL"));
                p.setMONEDA(rs.getString("MONEDA").trim());
                //p.setFecha_corte(rs.getDate("fechacorte"));
                //p.setSALDO_DOLAR(rs.getDouble("SALDO_DOLAR"));
                p.setCONTA_CRED(rs.getString("CONTA_CRED"));
                p.setCTA_PRESUPUESTO(rs.getString("CTA_PRESUPUESTO") == null ? "NULL" : rs.getString("CTA_PRESUPUESTO"));
                p.setTIPOPROV(rs.getString("TIPOPROV") == null ? "" : rs.getString("TIPOPROV").trim());
                p.setMONTO(rs.getDouble("MONTO"));
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

    public String obtenerResummenProvedor(java.util.Date inicio, java.util.Date fin, String cia_pro) {
        String lista = "";
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        String colones = "";
        String dolares = "";
        try {
            Connection connection = indicadoresDbPool.createIndicadoresConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT "
                    + "sum(p.Monto) as sumMonto,"
                    + "p.MONEDA "
                    + "FROM [EXACTUS].[dbo].[repuestos_V_COMPRAS] p"
                    + " where p.CIA_PROV like '%" + cia_pro + "%'"
                    + "and p.FECHA_Embarque between '" + sqlInicio + "'"
                    + "and '" + sqlFin + "'"
                    + " GROUP BY p.MONEDA;";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDHistoricoCP.obtenerResummenProvedor() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                String mon = rs.getString("MONEDA");
                if (mon.equals("CRC")) {
                    colones = "Suma monto ₡" + logic.AppStaticValues.numberFormater.format(rs.getDouble("sumMonto"));
                } else {
                    dolares = "Suma monto $"
                            + logic.AppStaticValues.numberFormater.format(rs.getDouble("sumSaldo"));
                }

            }
            lista = colones + "\t" + dolares;
            rs.close();
            connection.close();

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerSubtiposCPPorFechas() error " + e.getMessage());

        }
        return lista;
    }

    public ArrayList<entitys.ResumenProveedor> obtenerResummenProvedor(String pro) {
        ArrayList<entitys.ResumenProveedor> lista = new ArrayList<>();

        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT "
                    + "sum([MONTO])as sumMonto "
                    + ",sum([SALDO]) as sumSaldo"
                    + ",sum([Monto_colones])as sumMonto_Colones"
                    + ",sum([Saldo_colones])as sumSaldo_Colones "
                    + ",cp.tipo_mora "
                    + "FROM [EXACTUS].[dbo].[CP_CILT_RYMSA] cp "
                    + "where cp.proveedor ='" + pro + "' "
                    + "group By cp.TIPO_MORA;";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDHistoricoCP.obtenerResummenProvedor() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.ResumenProveedor p = new entitys.ResumenProveedor();
                p.setSumMonto(rs.getDouble("sumMonto"));
                p.setSumMonto_Colones(rs.getDouble("SumMonto_Colones"));
                p.setSumSaldo(rs.getDouble("sumSaldo"));
                p.setSumSaldo_Colones(rs.getDouble("sumSaldo_Colones"));
                p.setTipo_mora(rs.getString("Tipo_mora"));
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerResummenProvedor() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.PagoProv> obtenerResuPagProvedor(String pro, String cia, java.util.Date in, java.util.Date end, String nombreProv) {
        ArrayList<entitys.PagoProv> lista = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(in.getTime());
        java.sql.Date sqlFin = new java.sql.Date(end.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT "
                    + "sum([MONTO]) as sumMonto"
                    + ",[MONEDA] "
                    + "FROM [EXACTUS].[dbo].[repuestos_V_Pagos_Efectuados] p"
                    + " where p.PROVEEDOR ='" + pro + "'"
                    + " and p.nombre like '%" + nombreProv + "%'"
                    + " and p.CIAS like '%" + cia + "%'"
                    + " and FECHA_ULT_CREDITO between '" + sqlInicio + "'"
                    + " and '" + sqlFin + "'"
                    + " group by moneda;";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDHistoricoCP.obtenerResuPagProvedor() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.PagoProv p = new entitys.PagoProv();
                p.setMonto(rs.getDouble("sumMonto"));
                p.setMoneda(rs.getString("moneda"));
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            sqlPoolInstance.pool.releaseAllConnection();
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerResuPagProvedor() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.CompraProv> obtenerResuComProvedor(String pro, String cia, java.util.Date in, java.util.Date end, String nombreProv) {
        ArrayList<entitys.CompraProv> lista = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(in.getTime());
        java.sql.Date sqlFin = new java.sql.Date(end.getTime());
        try {
            Connection connection = indicadoresDbPool.createIndicadoresConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT "
                    + "sum([MONTO]) as sumMonto"
                    + ",[MONEDA] "
                    + "FROM [EXACTUS].[dbo].[repuestos_V_COMPRAS] p"
                    + " where p.codpro ='" + pro + "'"
                    + " and p.nombre like '%" + nombreProv + "%'"
                    + " and p.CIA like '%" + cia + "%'"
                    //+ " and fecha_embarque between '" + sqlInicio + "'"
                    + " and fecha_OC between '" + sqlInicio + "'"
                    + " and '" + sqlFin + "'"
                    + " group by moneda;";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDHistoricoCP.obtenerResuComProvedor() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.CompraProv p = new entitys.CompraProv();
                p.setMonto(rs.getDouble("sumMonto"));
                p.setMoneda(rs.getString("moneda"));
                lista.add(p);
            }
            rs.close();
            connection.close();

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerResuComProvedor() error " + e.getMessage());

        }
        return lista;
    }

    public boolean deleteHistoricoCPContado(int id) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "DELETE FROM indicadores.[dbo].[HistoricoCPContado]  WHERE id = " + id + ";";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDTipoCambio.deleteHistoricoCPContado() sentencia \n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDTipoCambio.deleteHistoricoCPContado() error " + e.getMessage());
            sqlPoolInstance.pool.releaseAllConnection();
            res = false;
        }
        return res;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Statement;
import entitys.UsuariosPresupuesto;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CRUDUsuariosPresupuesto {

    public ArrayList<entitys.UsuariosPresupuesto> obtenerAccesos() {
        ArrayList<entitys.UsuariosPresupuesto> lista = new ArrayList<>();

        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * FROM INDICADORES.dbo.PRESUP_USERS p "
                    + "where p.COD_USER = '" + DataUser.username + "' "
                    + "and p.activo = 1 and p.ACCESO = 'S';";
            System.out.println("data.CRUDUsuariosPresupuesto.obtenerAccesos() sentencia\n"+Sql); 
            AppLogger.appLogger.info(Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                UsuariosPresupuesto p = new UsuariosPresupuesto();
                p.setDETA_CIA(rs.getString("DETA_CIA").trim());
                p.setCOD_USER(rs.getString("COD_USER").trim());
                p.setCOD_DEPA(rs.getString("COD_DEPA").trim());
                p.setDETA_DEPA(rs.getString("DETA_DEPA").trim());
                p.setDETA_USER(rs.getString("DETA_USER").trim());
                p.setACCESO(rs.getString("ACCESO").trim());
                p.setActivo(rs.getBoolean("ACTIVO"));
                p.setUsuarioConta(rs.getInt("Estado"));
                p.setExactus(rs.getInt("Exactus"));
                p.setExactus_TC(rs.getInt("Exactus_TC"));
                p.setExactus_CB(rs.getInt("Exactus_CB"));
                p.setExactus_CP(rs.getInt("Exactus_CP"));
                p.setExactus_Subtipos(rs.getInt("Exactus_Subtipos"));
                p.setMantenimientoPagos(rs.getInt("MantenimientoPagos"));
                p.setReportePagos(rs.getInt("ReportePagos"));
                p.setHistoricoCP(rs.getInt("HistoricoCP"));
                p.setAdminFactSub(rs.getInt("AdminFactSub"));
                p.setAdministradorGestionGastosPer(rs.getInt("AdministradorGestionGastosPer"));
                p.setCOD_CIA(rs.getString("COD_CIA"));
                p.setId(rs.getInt("id"));
                p.setAdministracionUsuarios(rs.getInt("administracionUsuarios"));
                p.setMantenimientoCompromisos(rs.getInt("MantenimientoCompromisos"));
                p.setReportePagoAplicados(rs.getInt("ReportePagoAplicados"));
                p.setHistoricoAbonos(rs.getInt("HistoricoAbonos"));
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.info("data.CRUDUsuariosPresupuesto.obtenerAccesos() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.UsuariosPresupuesto> obtenerUsuarios() {
        ArrayList<entitys.UsuariosPresupuesto> lista = new ArrayList<>();

        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * FROM INDICADORES.dbo.PRESUP_USERS;";
            AppLogger.appLogger.info("data.CRUDUsuariosPresupuesto.obtenerUsuarios() sentencia\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                UsuariosPresupuesto p = new UsuariosPresupuesto();
                p.setDETA_CIA(rs.getString("DETA_CIA").trim());
                p.setCOD_USER(rs.getString("COD_USER").trim());
                p.setCOD_DEPA(rs.getString("COD_DEPA").trim());
                p.setDETA_DEPA(rs.getString("DETA_DEPA").trim());
                p.setDETA_USER(rs.getString("DETA_USER").trim());
                p.setACCESO(rs.getString("ACCESO").trim());
                p.setActivo(rs.getBoolean("ACTIVO"));
                p.setUsuarioConta(rs.getInt("Estado"));
                p.setExactus(rs.getInt("Exactus"));
                p.setExactus_TC(rs.getInt("Exactus_TC"));
                p.setExactus_CB(rs.getInt("Exactus_CB"));
                p.setExactus_CP(rs.getInt("Exactus_CP"));
                p.setExactus_Subtipos(rs.getInt("Exactus_Subtipos"));
                p.setMantenimientoPagos(rs.getInt("MantenimientoPagos"));
                p.setReportePagos(rs.getInt("ReportePagos"));
                p.setHistoricoCP(rs.getInt("HistoricoCP"));
                p.setAdminFactSub(rs.getInt("AdminFactSub"));
                p.setAdministradorGestionGastosPer(rs.getInt("AdministradorGestionGastosPer"));
                p.setCOD_CIA(rs.getString("COD_CIA"));
                p.setId(rs.getInt("id"));
                p.setAdministracionUsuarios(rs.getInt("administracionUsuarios"));
                p.setMantenimientoCompromisos(rs.getInt("MantenimientoCompromisos"));
                p.setReportePagoAplicados(rs.getInt("ReportePagoAplicados"));
                p.setHistoricoAbonos(rs.getInt("HistoricoAbonos"));
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.info("data.CRUDUsuariosPresupuesto.obtenerUsuarios() error " + e.getMessage());
        }
        return lista;
    }

    public UsuariosPresupuesto obtenerUsuariosPorId(int id) {
        UsuariosPresupuesto p = null;

        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * FROM INDICADORES.dbo.PRESUP_USERS where id = " + id + ";";
            AppLogger.appLogger.info("data.CRUDUsuariosPresupuesto.obtenerUsuariosPorId() sentencia\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new UsuariosPresupuesto();
                p.setDETA_CIA(rs.getString("DETA_CIA").trim());
                p.setCOD_USER(rs.getString("COD_USER").trim());
                p.setCOD_DEPA(rs.getString("COD_DEPA").trim());
                p.setDETA_DEPA(rs.getString("DETA_DEPA").trim());
                p.setDETA_USER(rs.getString("DETA_USER").trim());
                p.setACCESO(rs.getString("ACCESO").trim());
                p.setActivo(rs.getBoolean("ACTIVO"));
                p.setUsuarioConta(rs.getInt("Estado"));
                p.setExactus(rs.getInt("Exactus"));
                p.setExactus_TC(rs.getInt("Exactus_TC"));
                p.setExactus_CB(rs.getInt("Exactus_CB"));
                p.setExactus_CP(rs.getInt("Exactus_CP"));
                p.setExactus_Subtipos(rs.getInt("Exactus_Subtipos"));
                p.setMantenimientoPagos(rs.getInt("MantenimientoPagos"));
                p.setReportePagos(rs.getInt("ReportePagos"));
                p.setHistoricoCP(rs.getInt("HistoricoCP"));
                p.setAdminFactSub(rs.getInt("AdminFactSub"));
                p.setAdministradorGestionGastosPer(rs.getInt("AdministradorGestionGastosPer"));
                p.setCOD_CIA(rs.getString("COD_CIA"));
                p.setId(rs.getInt("id"));
                p.setAdministracionUsuarios(rs.getInt("administracionUsuarios"));
                p.setMantenimientoCompromisos(rs.getInt("MantenimientoCompromisos"));
                p.setReportePagoAplicados(rs.getInt("ReportePagoAplicados"));
                p.setHistoricoAbonos(rs.getInt("HistoricoAbonos"));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.info("data.CRUDUsuariosPresupuesto.obtenerUsuariosPorId() error " + e.getMessage());
        }
        return p;
    }

    public UsuariosPresupuesto obtenerUsuariosPorUsuarioYDep(String nombreUsuario, String cod_depa) {
        UsuariosPresupuesto p = null;

        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * FROM INDICADORES.dbo.PRESUP_USERS "
                    + " where cod_user = '" + nombreUsuario + "'"
                    + " and cod_depa = '" + cod_depa + "';";
            AppLogger.appLogger.info("data.CRUDUsuariosPresupuesto.obtenerUsuariosPorId() sentencia\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new UsuariosPresupuesto();
                p.setDETA_CIA(rs.getString("DETA_CIA").trim());
                p.setCOD_USER(rs.getString("COD_USER").trim());
                p.setCOD_DEPA(rs.getString("COD_DEPA").trim());
                p.setDETA_DEPA(rs.getString("DETA_DEPA").trim());
                p.setDETA_USER(rs.getString("DETA_USER").trim());
                p.setACCESO(rs.getString("ACCESO").trim());
                p.setActivo(rs.getBoolean("ACTIVO"));
                p.setUsuarioConta(rs.getInt("Estado"));
                p.setExactus(rs.getInt("Exactus"));
                p.setExactus_TC(rs.getInt("Exactus_TC"));
                p.setExactus_CB(rs.getInt("Exactus_CB"));
                p.setExactus_CP(rs.getInt("Exactus_CP"));
                p.setExactus_Subtipos(rs.getInt("Exactus_Subtipos"));
                p.setMantenimientoPagos(rs.getInt("MantenimientoPagos"));
                p.setReportePagos(rs.getInt("ReportePagos"));
                p.setHistoricoCP(rs.getInt("HistoricoCP"));
                p.setAdminFactSub(rs.getInt("AdminFactSub"));
                p.setAdministradorGestionGastosPer(rs.getInt("AdministradorGestionGastosPer"));
                p.setCOD_CIA(rs.getString("COD_CIA"));
                p.setId(rs.getInt("id"));
                p.setAdministracionUsuarios(rs.getInt("administracionUsuarios"));
                p.setMantenimientoCompromisos(rs.getInt("MantenimientoCompromisos"));
                p.setReportePagoAplicados(rs.getInt("ReportePagoAplicados"));
                p.setHistoricoAbonos(rs.getInt("HistoricoAbonos"));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.info("data.CRUDUsuariosPresupuesto.obtenerUsuariosPorId() error " + e.getMessage());
        }
        return p;
    }

    public boolean addUpdate(UsuariosPresupuesto p) {
        boolean res = false;

        String sql = "update INDICADORES.dbo.PRESUP_USERS "
                + "set exactus_tc = " + p.getExactus_TC()
                + ", exactus_cp = " + p.getExactus_CP()
                + ", exactus = " + p.getExactus()
                + ", exactus_cb = " + p.getExactus_CB()
                + ", estado = " + p.getUsuarioConta()
                + ", acceso = '" + p.getACCESO() + "'"
                + ", DETA_USER = '" + p.getDETA_USER() + "'"
                + ", activo = " + (p.isActivo() ? 1 : 0)
                + ",MantenimientoPagos = " + p.getMantenimientoPagos()
                + ",ReportePagos = " + p.getReportePagos()
                + ",Exactus_Subtipos = " + p.getExactus_Subtipos()
                + ",HistoricoCP = " + p.getHistoricoCP()
                + ",AdminFactSub = " + p.getAdminFactSub()
                + ",administracionUsuarios = " + p.getAdministracionUsuarios()
                + ",AdministradorGestionGastosPer = " + p.getAdministradorGestionGastosPer()
                + ",MantenimientoCompromisos = " + p.getMantenimientoCompromisos()
                + ",ReportePagoAplicados = " + p.getReportePagoAplicados()
                + ",HistoricoAbonos = " + p.getHistoricoAbonos()
                + " where COD_USER = '" + p.getCOD_USER() + "' "
                + " and COD_DEPA = '" + p.getCOD_DEPA() + "';";
        AppLogger.appLogger.warning("data.CRUDUsuariosPresupuesto.addUpdate() sentencia \n" + sql);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.warning("data.CRUDUsuariosPresupuesto.addUpdate() error " + e.getMessage());
        }
        return res;
    }

    public boolean add(UsuariosPresupuesto p) {
        boolean res = false;

        String sql = "exec INDICADORES.dbo.InsertUserFe "
                + "'3101086411',"
                + "'CILT3101086411',"
                + "'" + p.getCOD_USER() + "',"
                + "'" + p.getDETA_USER() + "',"
                + "'" + p.getCOD_DEPA() + "',"
                + "'" + p.getDETA_DEPA() + "',"
                + "'" + p.getACCESO() + "',"
                + p.getUsuarioConta() + ","
                + (p.isActivo() ? 1 : 0) + ","
                + p.getExactus() + ","
                + p.getExactus_TC() + ","
                + p.getExactus_CP() + ","
                + p.getExactus_CB() + ","
                + p.getExactus_Subtipos() + ","
                + p.getMantenimientoPagos() + ","
                + p.getReportePagos() + ","
                + p.getHistoricoCP() + ","
                + p.getAdminFactSub() + ","
                + p.getAdministradorGestionGastosPer() + ","
                + p.getMantenimientoCompromisos() + ","
                + p.getReportePagoAplicados() +
                + p.getHistoricoAbonos()+";";
        AppLogger.appLogger.warning("data.CRUDUsuariosPresupuesto.add() sentencia\n" + sql);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.warning("data.CRUDUsuariosPresupuesto.add() error " + e.getMessage());
        }
        return res;
    }

    public boolean delete(int id) {
        boolean res = false;

        String sql = "delete from INDICADORES.dbo.PRESUP_USERS where id = " + id + ";";
        AppLogger.appLogger.warning("data.CRUDUsuariosPresupuesto.delete() sentencia\n" + sql);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.warning("data.CRUDUsuariosPresupuesto.delete() error " + e.getMessage());
        }
        return res;
    }
}

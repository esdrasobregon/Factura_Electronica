/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

/**
 *
 * @author eobregon
 */
import java.util.logging.Level;
import logic.AppLogger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CRUDAbonoSugeridoContado {

    public boolean agAbonoSugeridoContado(entitys.AbonoSugeridoContado ab) {
        boolean res = false;
        String procedureCall = "INSERT INTO [dbo].[AbonoSugeridoContado]"
                + "([Documento],[ID],[Saldo_Restante],[Saldo_Restante_colones],[Abono],[Abono_Colones],"
                + "[Moneda],[Usuario],[Semana],[Aprobado],[RevisadoConta],[UsuarioRevision],[Fecha_Revision_Conta],[Fecha_Solicitud],[Forma_Pago] ,[proveedor], monto_original, monto_original_colones)"
                + "VALUES(\n"
                + "'" + ab.getDocumento() + "'--<Documento, varchar(30),>\n"
                + "," + ab.getId() + "--<ID, int,>\n"
                + ",'" + String.format("%.2f", ab.getSaldoActual()).replace(",", ".") + "'--<Saldo_restante, decimal(18,4),>\n"
                + ",'" + String.format("%.2f", ab.getSaldoActualColones()).replace(",", ".") + "'--<Saldo_restante_Colones, decimal(18,4),>\n"
                + ",'" + String.format("%.2f", ab.getAbono()).replace(",", ".") + "'--Abono, decimal(18, 4),>\n"
                + ",'" + String.format("%.2f", ab.getAbonoColones()).replace(",", ".") + "'--<Abono_Colones, money,>\n"
                + ",'" + ab.getMoneda() + "'--<Moneda, varchar(5),>\n"
                + ",'" + DataUser.username + "'--<Usuario, varchar(15),>\n"
                + "," + ab.getSemana() + "--<Semana, int,>\n"
                + ",1--<Aprobado, bit,>\n"
                + ",0--<RevisadoConta, bit,>\n"
                + ",null--<UsuarioRevision, varchar(20),>\n"
                + ",null--<Fecha_Revision_Conta, datetime,>\n"
                + ",getdate()--<Fecha_Solicitud, datetime,>\n"
                + ", '" + ab.getForma_pago() + "'--<Forma_Pago, varchar(20),>\n"
                + "," + ab.getProveedor() + "--,<proveedor, int,>\n"
                + ",'" + String.format("%.2f", ab.getMontoOriginal()).replace(",", ".") + "'-- monto original\n"
                + ",'" + String.format("%.2f", ab.getMontoOriginalColones()).replace(",", ".") + "');--monto original colones\n";

        AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.agAbonoSugeridoContado() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDAbonoSugeridoContado.agAbonoSugeridoContado() error " + e.getMessage());

        }
        return res;
    }

    public boolean actSugeridoContadoRevisado(int id) {
        boolean res = false;
        String procedureCall = "UPDATE [dbo].[AbonoSugeridoContado]\n"
                + "SET [RevisadoConta] = 1--<RevisadoConta, bit,>"
                + "\n,[UsuarioRevision] = '" + DataUser.username + "'--<UsuarioRevision, varchar(20),>"
                + "\n,[Fecha_Revision_Conta] = getdate()--<Fecha_Revision_Conta, datetime"
                + "\n WHERE idAbonoSugeridoContado = " + id + ";";
        AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.actAbonoSugeridoContado() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDAbonoSugeridoContado.actAbonoSugeridoContado() error " + e.getMessage());

        }
        return res;
    }

    /**
     * this method updates the documento, forma_pago and proveedor of a
     * AbonoSugeridoContado, use it when updating the main document
     */
    public boolean actSugeridoContado(entitys.AbonoSugeridoContado ab) {
        boolean res = false;
        String procedureCall = "UPDATE [dbo].[AbonoSugeridoContado]\n"
                + "SET documento ='" + ab.getDocumento() + "'\n"
                + ",proveedor =" + ab.getProveedor() + "\n"
                + ",abono ='" + String.format("%.2f", ab.getAbono()) + "' --abono\n"
                + ",abono_colones ='" + String.format("%.2f", ab.getAbonoColones()) + "' --abono_colones\n"
                + ",saldo_restante ='" + String.format("%.2f", ab.getSaldoActual()) + "' --saldo_restante\n"
                + ",saldo_restante_colones ='" + String.format("%.2f", ab.getSaldoActualColones()) + "' --saldo_restante_colones\n"
                + ",forma_pago ='" + ab.getForma_pago() + "'\n"
                + " WHERE idAbonoSugeridoContado = " + ab.getIdAbonoSugeridoContado() + ";";
        AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.actSugeridoContado() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDAbonoSugeridoContado.actSugeridoContado() error " + e.getMessage());

        }
        return res;
    }

    public boolean elimSugeridoContado(int id) {
        boolean res = false;
        String procedureCall = "delete from [dbo].[AbonoSugeridoContado]\n"
                + " where idAbonoSugeridoContado = " + id + ";";
        AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.elimSugeridoContado() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDAbonoSugeridoContado.elimSugeridoContado() error " + e.getMessage());

        }
        return res;
    }

    public boolean elimSugeridoContadoPendiente(int id) {
        boolean res = false;
        String procedureCall = "delete from [dbo].[AbonoSugeridoContado]\n"
                + " where id = " + id + " and RevisadoConta = 0;";
        AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.elimSugeridoContadoPendiente() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDAbonoSugeridoContado.elimSugeridoContadoPendiente() error " + e.getMessage());

        }
        return res;
    }

    public ArrayList<entitys.AbonoSugeridoContado> obtenerAbonoHistoricoCPContado_ViewConta() {
        ArrayList<entitys.AbonoSugeridoContado> lista = new ArrayList<entitys.AbonoSugeridoContado>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select *from indicadores.dbo.vista_Abono_Contado;";

            Statement sta = connection.createStatement();
            System.out.println("data.CRUDAbonoSugeridoContado.obtenerAbonoHistoricoCPContado_ViewConta()");
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.obtenerAbonoHistoricoCPContado_ViewConta() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.AbonoSugeridoContado a = new entitys.AbonoSugeridoContado();
                a.setId(rs.getInt("ID"));
                a.setIdAbonoSugeridoContado(rs.getInt("idAbonoSugeridoContado"));
                a.setFechaSolicitud(rs.getDate("Fecha_Solicitud"));
                a.setDocumento(rs.getString("documento"));
                a.setAbono(rs.getDouble("abono"));
                a.setAbonoColones(rs.getDouble("abono_colones"));
                a.setMoneda(rs.getString("moneda"));
                a.setAprobado(rs.getInt("Aprobado"));
                a.setCtPresupuesto(rs.getString("CTA_PRESUPUESTO"));
                a.setDescCtaPres(rs.getString("desc_CTA_PRESUPUESTO"));
                a.setSaldoActual(rs.getDouble("Saldo_Restante"));
                a.setSaldoActualColones(rs.getDouble("Saldo_Restante_Colones"));
                a.setUsuario(rs.getString("usuario"));
                a.setSemana(rs.getInt("semana"));
                a.setAprobado(rs.getInt("aprobado"));
                a.setRevisadoConta(rs.getInt("RevisadoConta"));
                a.setUsuarioRevision(rs.getString("UsuarioRevision"));
                a.setFechaRevisionConta(rs.getDate("Fecha_Revision_Conta"));
                a.setProveedor(rs.getInt("proveedor"));
                a.setNombreProveedor(rs.getString("nombre_proveedor"));
                a.setForma_pago(rs.getString("forma_pago"));
                a.setSociedad(rs.getString("cia"));
                a.setFechaDocumento(rs.getDate("FECHA_DOCUMENTO"));
                a.setAdelanto(rs.getInt("adelanto"));
                a.setMontoOriginal(rs.getDouble("Monto_Original"));
                a.setMontoOriginalColones(rs.getDouble("Monto_original_colones"));
                lista.add(a);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerHistoricoCPContado_ViewConta() error " + e.getMessage());
            System.out.println("data.CRUDHistoricoCP.obtenerHistoricoCPContado_ViewConta() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.AbonoSugeridoContado> obtenerLineasAbonoHistoricoCPContado(int proveedor, String documento) {
        ArrayList<entitys.AbonoSugeridoContado> lista = new ArrayList<entitys.AbonoSugeridoContado>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select *from indicadores.dbo.vista_abono_sugerido_contado \n"
                    + "where proveedor = " + proveedor + " \n"
                    + "and documento = '" + documento + "';";

            Statement sta = connection.createStatement();
            System.out.println("data.CRUDAbonoSugeridoContado.obtenerLineasAbonoHistoricoCPContado()");
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.obtenerLineasAbonoHistoricoCPContado() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.AbonoSugeridoContado a = new entitys.AbonoSugeridoContado();
                a.setId(rs.getInt("ID"));
                a.setIdAbonoSugeridoContado(rs.getInt("idAbonoSugeridoContado"));
                a.setFechaSolicitud(rs.getDate("Fecha_Solicitud"));
                a.setDocumento(rs.getString("documento"));
                a.setAbono(rs.getDouble("abono"));
                a.setAbonoColones(rs.getDouble("abono_colones"));
                a.setMoneda(rs.getString("moneda"));
                a.setAprobado(rs.getInt("Aprobado"));
                a.setCtPresupuesto(rs.getString("CTA_PRESUPUESTO"));
                a.setDescCtaPres(rs.getString("desc_CTA_PRESUPUESTO"));
                a.setSaldoActual(rs.getDouble("Saldo_Restante"));
                a.setSaldoActualColones(rs.getDouble("Saldo_Restante_colones"));
                a.setUsuario(rs.getString("usuario"));
                a.setSemana(rs.getInt("semana"));
                a.setAprobado(rs.getInt("aprobado"));
                a.setRevisadoConta(rs.getInt("RevisadoConta"));
                a.setUsuarioRevision(rs.getString("UsuarioRevision"));
                a.setFechaRevisionConta(rs.getDate("Fecha_Revision_Conta"));
                a.setProveedor(rs.getInt("proveedor"));
                a.setNombreProveedor(rs.getString("nombre_proveedor"));
                a.setForma_pago(rs.getString("forma_pago"));
                a.setSociedad(rs.getString("cia"));
                a.setFechaDocumento(rs.getDate("FECHA_DOCUMENTO"));
                a.setAdelanto(rs.getInt("adelanto"));
                a.setMontoOriginal(rs.getDouble("Monto_Original"));
                a.setMontoOriginalColones(rs.getDouble("Monto_original_colones"));
                lista.add(a);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerLineasAbonoHistoricoCPContado() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.AbonoSugeridoContado> obtAbonoHistoricoCPContadoConFilt(String cia, int prov, String moneda, int revizadoConta, java.util.Date inicio, java.util.Date fin) {
        java.sql.Date sqlIni = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        ArrayList<entitys.AbonoSugeridoContado> lista = new ArrayList<entitys.AbonoSugeridoContado>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select *from indicadores.dbo.vista_abono_sugerido_contado \n"
                    + "where cia like '%" + cia + "%'--cia\n"
                    + (prov > 0 ? " and proveedor = " + prov + "--proveedor\n" : "")
                    + "and moneda like '%" + moneda + "%'--moneda\n"
                    + (revizadoConta > -1 ? " and revisadoconta = " + revizadoConta + "--revisadoconta\n" : "")
                    + " and (fecha_solicitud between '" + sqlIni + "'\n"
                    + "and '" + sqlFin + "');";

            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.obtAbonoHistoricoCPContadoPorFech() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.AbonoSugeridoContado a = new entitys.AbonoSugeridoContado();
                a.setId(rs.getInt("ID"));
                a.setIdAbonoSugeridoContado(rs.getInt("idAbonoSugeridoContado"));
                a.setFechaSolicitud(rs.getDate("Fecha_Solicitud"));
                a.setDocumento(rs.getString("documento"));
                a.setAbono(rs.getDouble("abono"));
                a.setAbonoColones(rs.getDouble("abono_colones"));
                a.setMoneda(rs.getString("moneda"));
                a.setAprobado(rs.getInt("Aprobado"));
                a.setCtPresupuesto(rs.getString("CTA_PRESUPUESTO"));
                a.setDescCtaPres(rs.getString("desc_CTA_PRESUPUESTO"));
                a.setSaldoActual(rs.getDouble("Saldo_Restante"));
                a.setSaldoActualColones(rs.getDouble("Saldo_Restante_colones"));
                a.setUsuario(rs.getString("usuario"));
                a.setSemana(rs.getInt("semana"));
                a.setAprobado(rs.getInt("aprobado"));
                a.setRevisadoConta(rs.getInt("RevisadoConta"));
                a.setUsuarioRevision(rs.getString("UsuarioRevision"));
                a.setFechaRevisionConta(rs.getDate("Fecha_Revision_Conta"));
                a.setProveedor(rs.getInt("proveedor"));
                a.setNombreProveedor(rs.getString("nombre_proveedor"));
                a.setForma_pago(rs.getString("forma_pago"));
                a.setSociedad(rs.getString("cia"));
                a.setFechaDocumento(rs.getDate("FECHA_DOCUMENTO"));
                a.setAdelanto(rs.getInt("adelanto"));
                a.setMontoOriginal(rs.getDouble("Monto_Original"));
                a.setMontoOriginalColones(rs.getDouble("Monto_original_colones"));
                lista.add(a);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDAbonoSugeridoContado.obtAbonoHistoricoCPContadoPorFech() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.AbonoSugeridoContado> obtenerAbonoHistoricoCPContado_ViewConta(String moneda, String cia) {
        ArrayList<entitys.AbonoSugeridoContado> lista = new ArrayList<entitys.AbonoSugeridoContado>();
        try {
            boolean where = !moneda.equals("Todas") || !cia.equals("Todas");
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select *from indicadores.dbo.vista_Abono_Contado \n"
                    + (where ? " where " : ";")
                    + (cia.equals("Todas") ? "" : " cia ='" + cia + "'\n")
                    + (cia.equals("Todas") ? "" : (moneda.equals("Todas") ? "" : " and "))
                    + (moneda.equals("Todas") ? ";" : " moneda = '" + moneda + "';");
            Statement sta = connection.createStatement();
            System.out.println("data.CRUDAbonoSugeridoContado.obtenerAbonoHistoricoCPContado_ViewConta()");
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.obtenerAbonoHistoricoCPContado_ViewConta() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.AbonoSugeridoContado a = new entitys.AbonoSugeridoContado();
                a.setId(rs.getInt("ID"));
                a.setIdAbonoSugeridoContado(rs.getInt("idAbonoSugeridoContado"));
                a.setFechaSolicitud(rs.getDate("Fecha_Solicitud"));
                a.setDocumento(rs.getString("documento"));
                a.setAbono(rs.getDouble("abono"));
                a.setAbonoColones(rs.getDouble("abono_colones"));
                a.setMoneda(rs.getString("moneda"));
                a.setAprobado(rs.getInt("Aprobado"));
                a.setCtPresupuesto(rs.getString("CTA_PRESUPUESTO"));
                a.setDescCtaPres(rs.getString("desc_CTA_PRESUPUESTO"));
                a.setSaldoActual(rs.getDouble("Saldo_Restante_Colones"));
                a.setSaldoActualColones(0);
                a.setUsuario(rs.getString("usuario"));
                a.setSemana(rs.getInt("semana"));
                a.setAprobado(rs.getInt("aprobado"));
                a.setRevisadoConta(rs.getInt("RevisadoConta"));
                a.setUsuarioRevision(rs.getString("UsuarioRevision"));
                a.setFechaRevisionConta(rs.getDate("Fecha_Revision_Conta"));
                a.setProveedor(rs.getInt("proveedor"));
                a.setNombreProveedor(rs.getString("nombre_proveedor"));
                a.setForma_pago(rs.getString("forma_pago"));
                a.setSociedad(rs.getString("cia"));
                a.setFechaDocumento(rs.getDate("FECHA_DOCUMENTO"));
                a.setAdelanto(rs.getInt("adelanto"));
                a.setMontoOriginal(rs.getDouble("Monto_Original"));
                a.setMontoOriginalColones(rs.getDouble("Monto_original_colones"));
                lista.add(a);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerHistoricoCPContado_ViewConta() error " + e.getMessage());
            System.out.println("data.CRUDHistoricoCP.obtenerHistoricoCPContado_ViewConta() error " + e.getMessage());
        }
        return lista;
    }

    public entitys.AbonoSugeridoContado obtenerAbonoContado_ViewContaAp(int proveedor, int id, String documento) {
        entitys.AbonoSugeridoContado a = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select *from indicadores.dbo.vista_Abono_Contado "
                    + "where proveedor = " + proveedor
                    + " and id = " + id + ""
                    + " and documento = '" + documento + "'"
                    + " and [RevisadoConta] = 0;";

            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.obtenerAbonoContado_ViewContaAp() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                a = new entitys.AbonoSugeridoContado();
                a.setId(rs.getInt("ID"));
                a.setIdAbonoSugeridoContado(rs.getInt("idAbonoSugeridoContado"));
                a.setFechaSolicitud(rs.getDate("Fecha_Solicitud"));
                a.setDocumento(rs.getString("documento"));
                a.setAbono(rs.getDouble("abono"));
                a.setAbonoColones(rs.getDouble("abono_colones"));
                a.setMoneda(rs.getString("moneda"));
                a.setAprobado(rs.getInt("Aprobado"));
                a.setCtPresupuesto(rs.getString("CTA_PRESUPUESTO"));
                a.setDescCtaPres(rs.getString("desc_CTA_PRESUPUESTO"));
                a.setSaldoActual(rs.getDouble("Saldo_Restante_Colones"));
                a.setSaldoActualColones(0);
                a.setUsuario(rs.getString("usuario"));
                a.setSemana(rs.getInt("semana"));
                a.setAprobado(rs.getInt("aprobado"));
                a.setRevisadoConta(rs.getInt("RevisadoConta"));
                a.setUsuarioRevision(rs.getString("UsuarioRevision"));
                a.setFechaRevisionConta(rs.getDate("Fecha_Revision_Conta"));
                a.setProveedor(rs.getInt("proveedor"));
                a.setNombreProveedor(rs.getString("nombre_proveedor"));
                a.setForma_pago(rs.getString("forma_pago"));
                a.setSociedad(rs.getString("cia"));
                a.setFechaDocumento(rs.getDate("FECHA_DOCUMENTO"));
                a.setAdelanto(rs.getInt("adelanto"));
                a.setMontoOriginal(rs.getDouble("Monto_Original"));
                a.setMontoOriginalColones(rs.getDouble("Monto_original_colones"));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerAbonoContado_ViewContaAp() error " + e.getMessage());

        }
        return a;
    }

    public entitys.AbonoSugeridoContado obtenerAbonoContado_ViewContaPorId(int id) {
        entitys.AbonoSugeridoContado a = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select *from indicadores.dbo.vista_Abono_Contado \n"
                    + "where idAbonoSugeridoContado = " + id + ";";

            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.obtenerAbonoContado_ViewContaAp() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                a = new entitys.AbonoSugeridoContado();
                a.setId(rs.getInt("ID"));
                a.setIdAbonoSugeridoContado(rs.getInt("idAbonoSugeridoContado"));
                a.setFechaSolicitud(rs.getDate("Fecha_Solicitud"));
                a.setDocumento(rs.getString("documento"));
                a.setAbono(rs.getDouble("abono"));
                a.setAbonoColones(rs.getDouble("abono_colones"));
                a.setMoneda(rs.getString("moneda"));
                a.setAprobado(rs.getInt("Aprobado"));
                a.setCtPresupuesto(rs.getString("CTA_PRESUPUESTO"));
                a.setDescCtaPres(rs.getString("desc_CTA_PRESUPUESTO"));
                a.setSaldoActual(rs.getDouble("Saldo_Restante"));
                a.setSaldoActualColones(rs.getDouble("Saldo_Restante_Colones"));
                a.setUsuario(rs.getString("usuario"));
                a.setSemana(rs.getInt("semana"));
                a.setAprobado(rs.getInt("aprobado"));
                a.setRevisadoConta(rs.getInt("RevisadoConta"));
                a.setUsuarioRevision(rs.getString("UsuarioRevision"));
                a.setFechaRevisionConta(rs.getDate("Fecha_Revision_Conta"));
                a.setProveedor(rs.getInt("proveedor"));
                a.setNombreProveedor(rs.getString("nombre_proveedor"));
                a.setForma_pago(rs.getString("forma_pago"));
                a.setSociedad(rs.getString("cia"));
                a.setFechaDocumento(rs.getDate("FECHA_DOCUMENTO"));
                a.setAdelanto(rs.getInt("adelanto"));
                a.setMontoOriginal(rs.getDouble("Monto_Original"));
                a.setMontoOriginalColones(rs.getDouble("Monto_original_colones"));
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerAbonoContado_ViewContaAp() error " + e.getMessage());

        }
        return a;
    }

    public int obtSumAbonoContadoRev(int id) {
        int res = 0;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "select count(*) as suma from indicadores.dbo.AbonoSugeridoContado a \n"
                    + "where a.id = " + id + "\n"
                    + " and a.RevisadoConta = 1;";

            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.obtSumAbonoContadoRev() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                res = rs.getInt("suma");
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtSumAbonoContadoRev() error " + e.getMessage());

        }
        return res;
    }

    public ArrayList<entitys.SumasTransacciones> obtenerSumaAbonoHistoricoCPContado_View() {
        ArrayList<entitys.SumasTransacciones> lista = new ArrayList<entitys.SumasTransacciones>();

        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            String sql = "SELECT sum(abono_colones) as sumAbonoColones FROM [INDICADORES].[dbo].[vista_Abono_Contado];";
            Statement sta = connection.createStatement();
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDAbonoSugeridoContado.obtenerSumaAbonoHistoricoCPContado_View() sentencia \n" + sql);
            ResultSet rs = sta.executeQuery(sql);
            while (rs.next()) {
                entitys.SumasTransacciones p = new entitys.SumasTransacciones();
                p.setSumSaldoColones(rs.getDouble("sumAbonoColones"));

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

}

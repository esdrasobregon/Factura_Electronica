/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.CrudProvContado;

import data.sqlPoolInstance;
import entitys.ProveedorContado.CuentaProveedorContado;
import entitys.ProveedorContado.ProveedorContado;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CrudProvedorContado {

    public ArrayList<entitys.ProveedorContado.ProveedorContado> obtenerListaProveedorContado(String nombre) {
        ArrayList<entitys.ProveedorContado.ProveedorContado> listaPresupuesto = new ArrayList<entitys.ProveedorContado.ProveedorContado>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select * from [INDICADORES].[dbo].[ProveedorContadoView] v"
                    + " where v.nombre like '%" + nombre + "%';";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContado() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContado() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.ProveedorContado.ProveedorContado p = new entitys.ProveedorContado.ProveedorContado();
                p.setIdProveedorContado(rs.getInt("idProveedorContado"));
                p.setSinpes(rs.getInt("telefonos"));
                p.setTotalCuentas(rs.getInt("cuentas"));
                p.setCodigo(rs.getString("codigo"));
                p.setEstado(rs.getInt("estado"));
                p.setFechaCreacion(rs.getDate("FechaCreacion"));
                p.setNombre(rs.getString("nombre"));
                p.setUltimaModificacion(rs.getDate("UltimaModificacion"));
                p.setUsuarioCreador(rs.getString("UsuarioCreador"));
                p.setUsuarioModificador(rs.getString("UsuarioModificador"));
                p.setCedulaJuridica(rs.getString("CedulaJuridica"));
                listaPresupuesto.add(p);
            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContado() error " + e.getMessage());
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContado() error " + e.getMessage());
        }
        return listaPresupuesto;
    }

    public entitys.ProveedorContado.ProveedorContado obtenerListaProveedorContadoPorId(int id) {
        entitys.ProveedorContado.ProveedorContado p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select * from [INDICADORES].[dbo].[ProveedorContadoView] v"
                    + " where v.idProveedorContado = " + id + ";";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContadoPorId() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContadoPorId() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new entitys.ProveedorContado.ProveedorContado();
                p.setIdProveedorContado(rs.getInt("idProveedorContado"));
                p.setSinpes(rs.getInt("telefonos"));
                p.setTotalCuentas(rs.getInt("cuentas"));
                p.setCodigo(rs.getString("codigo"));
                p.setEstado(rs.getInt("estado"));
                p.setFechaCreacion(rs.getDate("FechaCreacion"));
                p.setNombre(rs.getString("nombre"));
                p.setUltimaModificacion(rs.getDate("UltimaModificacion"));
                p.setUsuarioCreador(rs.getString("UsuarioCreador"));
                p.setUsuarioModificador(rs.getString("UsuarioModificador"));
                p.setCedulaJuridica(rs.getString("CedulaJuridica"));

            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            p = null;
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContadoPorId() error " + e.getMessage());
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContado() error " + e.getMessage());
        }
        return p;
    }

    public ProveedorContado obtenerListaProveedorCont(ProveedorContado prov) {
        ProveedorContado p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select * from [INDICADORES].[dbo].[ProveedorContadoView] v"
                    + " where "
                    + "v.codigo = '" + prov.getCodigo() + "'"
                    + " and v.cedulajuridica = '" + prov.getCedulaJuridica() + "'"
                    + " and v.codigo = '" + prov.getCodigo() + "'"
                    + " and v.nombre = '" + prov.getNombre() + "';";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContadoPorId() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContadoPorId() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new ProveedorContado();
                p.setIdProveedorContado(rs.getInt("idProveedorContado"));
                p.setSinpes(rs.getInt("telefonos"));
                p.setTotalCuentas(rs.getInt("cuentas"));
                p.setCodigo(rs.getString("codigo"));
                p.setEstado(rs.getInt("estado"));
                p.setFechaCreacion(rs.getDate("FechaCreacion"));
                p.setNombre(rs.getString("nombre"));
                p.setUltimaModificacion(rs.getDate("UltimaModificacion"));
                p.setUsuarioCreador(rs.getString("UsuarioCreador"));
                p.setUsuarioModificador(rs.getString("UsuarioModificador"));
                p.setCedulaJuridica(rs.getString("CedulaJuridica"));

            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            p = null;
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContadoPorId() error " + e.getMessage());
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaProveedorContado() error " + e.getMessage());
        }
        return p;
    }

    public boolean agregarProveedorContado(entitys.ProveedorContado.ProveedorContado prvCont) {
        boolean res = false;
        java.sql.Date sqlInicio = new java.sql.Date(prvCont.getFechaCreacion().getTime());
        java.sql.Date sqlMod = new java.sql.Date(prvCont.getUltimaModificacion().getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "INSERT INTO [dbo].[ProveedorContado]"
                    + "([Nombre]"
                    + ",[Estado]"
                    + ",[FechaCreacion]"
                    + ",[UltimaModificacion]"
                    + ",[UsuarioCreador]"
                    + ",[UsuarioModificador]"
                    + ",[Codigo]"
                    + ",[CedulaJuridica])"
                    + "VALUES"
                    + "('" + prvCont.getNombre() + "'"
                    + "," + prvCont.getEstado()
                    + ",'" + sqlInicio + "'"
                    + ",'" + sqlMod + "'"
                    + ",'" + prvCont.getUsuarioCreador() + "'"
                    + ",''"
                    + ",'" + prvCont.getCodigo() + "'"
                    + ",'" + prvCont.getCedulaJuridica() + "')";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.agregarProveedorContado() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.agregarProveedorContado()error " + e.getMessage());

        }
        return res;
    }

    public boolean actualizarProveedorContado(entitys.ProveedorContado.ProveedorContado prvCont) {
        boolean res = false;
        java.sql.Date sqlMod = new java.sql.Date(prvCont.getUltimaModificacion().getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "update [dbo].[ProveedorContado]"
                    + " set [Nombre] = '" + prvCont.getNombre() + "'"
                    + ",[Estado] = " + prvCont.getEstado()
                    + ",[UltimaModificacion] = '" + sqlMod + "'"
                    + ",[UsuarioModificador] = '" + prvCont.getUsuarioModificador() + "'"
                    + ",[Codigo] = '" + prvCont.getCodigo() + "'"
                    + ",[CedulaJuridica] ='" + prvCont.getCedulaJuridica() + "'"
                    + " where idProveedorContado = " + prvCont.getIdProveedorContado() + ";";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.actualizarProveedorContado() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.actualizarProveedorContado() error " + e.getMessage());

        }
        return res;
    }

    public boolean agregarCtaProveedorContado(entitys.ProveedorContado.CuentaProveedorContado cta) {
        boolean res = false;
        java.sql.Date sqlInicio = new java.sql.Date(cta.getCreado().getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "INSERT INTO [INDICADORES].dbo.[CuentaContado]"
                    + "([ProveedorContado_idProveedorContado]"
                    + ",[Banco]"
                    + ",[Numero]"
                    + ",[Estado]"
                    + ",[Creado]) "
                    + "VALUES (" + cta.getIdProveedorContado()
                    + ",'" + cta.getBanco() + "'"
                    + ",'" + cta.getNumero() + "'"
                    + "," + cta.getEstado()
                    + ",'" + sqlInicio + "');";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.agregarCtaProveedorContado() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.agregarCtaProveedorContado()error " + e.getMessage());

        }
        return res;
    }

    public boolean actualizarCtaProveedorContado(entitys.ProveedorContado.CuentaProveedorContado cta) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "update [INDICADORES].dbo.[CuentaContado]"
                    + "set [Banco] = '" + cta.getBanco() + "'"
                    + ",[Numero] = '" + cta.getNumero() + "'"
                    + ",[Estado] = " + cta.getEstado()
                    + " where idCuetaContado = " + cta.getIdCuentaContado() + ";";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.actualizarCtaProveedorContado() sentenpcia\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.actualizarCtaProveedorContado() error " + e.getMessage());

        }
        return res;
    }

    public ArrayList<entitys.ProveedorContado.CuentaProveedorContado> obtenerListaCtaProveedorContado(int idProv) {
        ArrayList<entitys.ProveedorContado.CuentaProveedorContado> lista = new ArrayList<entitys.ProveedorContado.CuentaProveedorContado>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT [idCuetaContado]"
                    + ",[ProveedorContado_idProveedorContado]"
                    + ",[Banco]"
                    + ",[Numero]"
                    + ",[Estado]"
                    + ",[Creado]"
                    + "FROM [INDICADORES].[dbo].[CuentaContado] c "
                    + "where c.ProveedorContado_idProveedorContado = " + idProv + ";";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.ProveedorContado.CuentaProveedorContado p = new entitys.ProveedorContado.CuentaProveedorContado();
                p.setIdProveedorContado(rs.getInt("ProveedorContado_idProveedorContado"));
                p.setBanco(rs.getString("Banco"));
                p.setNumero(rs.getString("Numero"));
                p.setCreado(rs.getDate("Creado"));
                p.setIdCuentaContado(rs.getInt("idCuetaContado"));
                p.setEstado(rs.getInt("Estado"));

                lista.add(p);
            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() error " + e.getMessage());
            System.err.println("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() error " + e.getMessage());
        }
        return lista;
    }

    public entitys.ProveedorContado.CuentaProveedorContado obtenerListaCtaProveedorContadoPorId(int idCta) {
        entitys.ProveedorContado.CuentaProveedorContado p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT [idCuetaContado]"
                    + ",[ProveedorContado_idProveedorContado]"
                    + ",[Banco]"
                    + ",[Numero]"
                    + ",[Estado]"
                    + ",[Creado]"
                    + "FROM [INDICADORES].[dbo].[CuentaContado] c "
                    + "where c.idCuetaContado = " + idCta + ";";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new entitys.ProveedorContado.CuentaProveedorContado();
                p.setIdProveedorContado(rs.getInt("ProveedorContado_idProveedorContado"));
                p.setBanco(rs.getString("Banco"));
                p.setNumero(rs.getString("Numero"));
                p.setCreado(rs.getDate("Creado"));
                p.setIdCuentaContado(rs.getInt("idCuetaContado"));
                p.setEstado(rs.getInt("Estado"));

            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() error " + e.getMessage());
            return null;
        }
        return p;
    }

    public boolean agregarSinpeProveedorContado(entitys.ProveedorContado.TelefonoSinpeContado tel) {
        boolean res = false;
        java.sql.Date sqlInicio = new java.sql.Date(tel.getCreado().getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "INSERT INTO INDICADORES. [dbo].[TelefonoSinpeContado]"
                    + "([Numero] "
                    + ",[Estado]"
                    + ",[Creado]"
                    + ",[ProveedorContado_idProveedorContado])"
                    + " VALUES ('" + tel.getNumero() + "'"
                    + "," + tel.getEstado()
                    + ", '" + sqlInicio + "'"
                    + ", " + tel.getProveedorContado_idProveedorContado() + ");";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.agregarSinpeProveedorContado() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.agregarSinpeProveedorContado()error " + e.getMessage());

        }
        return res;
    }

    public boolean actualizarSinpeProveedorContado(entitys.ProveedorContado.TelefonoSinpeContado tel) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "update INDICADORES. [dbo].[TelefonoSinpeContado]"
                    + " set [Numero] = '" + tel.getNumero() + "'"
                    + ",[Estado]= '" + tel.getEstado() + "'"
                    + " where idTelefonoSinpeContado = " + tel.getIdTelefonoSinpeContado() + ";";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.actualizarSinpeProveedorContado() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.actualizarSinpeProveedorContado() error " + e.getMessage());

        }
        return res;
    }

    public ArrayList<entitys.ProveedorContado.TelefonoSinpeContado> obtenerListaSinpeProveedorContado(int idProv) {
        ArrayList<entitys.ProveedorContado.TelefonoSinpeContado> lista = new ArrayList<entitys.ProveedorContado.TelefonoSinpeContado>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT "
                    + "[idTelefonoSinpeContado]"
                    + ",[Numero]"
                    + ",[Estado] "
                    + ",[Creado]"
                    + ",[ProveedorContado_idProveedorContado] FROM [INDICADORES].[dbo].[TelefonoSinpeContado] c "
                    + "where c.ProveedorContado_idProveedorContado = " + idProv + ";";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.ProveedorContado.TelefonoSinpeContado p = new entitys.ProveedorContado.TelefonoSinpeContado();
                p.setProveedorContado_idProveedorContado(rs.getInt("ProveedorContado_idProveedorContado"));
                p.setNumero(rs.getString("Numero"));
                p.setIdTelefonoSinpeContado(rs.getInt("idTelefonoSinpeContado"));
                p.setCreado(rs.getDate("Creado"));
                p.setEstado(rs.getInt("Estado"));

                lista.add(p);
            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() error " + e.getMessage());
            System.err.println("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() error " + e.getMessage());
        }
        return lista;
    }

    public entitys.ProveedorContado.TelefonoSinpeContado obtenerListaSinpeProveedorPorid(int idTele) {
        entitys.ProveedorContado.TelefonoSinpeContado p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT "
                    + "[idTelefonoSinpeContado]"
                    + ",[Numero]"
                    + ",[Estado] "
                    + ",[Creado]"
                    + ",[ProveedorContado_idProveedorContado] FROM [INDICADORES].[dbo].[TelefonoSinpeContado] c "
                    + "where c.idTelefonoSinpeContado = " + idTele + ";";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new entitys.ProveedorContado.TelefonoSinpeContado();
                p.setProveedorContado_idProveedorContado(rs.getInt("ProveedorContado_idProveedorContado"));
                p.setNumero(rs.getString("Numero"));
                p.setIdTelefonoSinpeContado(rs.getInt("idTelefonoSinpeContado"));
                p.setCreado(rs.getDate("Creado"));
                p.setEstado(rs.getInt("Estado"));

            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {

            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaCtaProveedorContado() error " + e.getMessage());
            return null;
        }
        return p;
    }

    public entitys.ProveedorContado.TelefonoSinpeContado obtenerListaSinpeProveedorContado(entitys.ProveedorContado.TelefonoSinpeContado t) {
        entitys.ProveedorContado.TelefonoSinpeContado p = null;
        java.sql.Date sqlInicio = new java.sql.Date(t.getCreado().getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT "
                    + "[idTelefonoSinpeContado]"
                    + ",[Numero]"
                    + ",[Estado] "
                    + ",[Creado]"
                    + ",[ProveedorContado_idProveedorContado] FROM [INDICADORES].[dbo].[TelefonoSinpeContado] c "
                    + "where "
                    + "c.ProveedorContado_idProveedorContado = " + t.getProveedorContado_idProveedorContado()
                    + " and c.numero = '" + t.getNumero() + "'"
                    + " and c.creado = '" + sqlInicio + "';";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerListaSinpeProveedorContado() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaSinpeProveedorContado() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new entitys.ProveedorContado.TelefonoSinpeContado();
                p.setProveedorContado_idProveedorContado(rs.getInt("ProveedorContado_idProveedorContado"));
                p.setNumero(rs.getString("Numero"));
                p.setIdTelefonoSinpeContado(rs.getInt("idTelefonoSinpeContado"));
                p.setCreado(rs.getDate("Creado"));
                p.setEstado(rs.getInt("Estado"));

            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerListaSinpeProveedorContado() error " + e.getMessage());
        }
        return p;
    }

    public CuentaProveedorContado obtenerCtaProveedor(CuentaProveedorContado cta) {
        java.sql.Date sqlInicio = new java.sql.Date(cta.getCreado().getTime());
        CuentaProveedorContado p = null;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select * from [INDICADORES].[dbo].[CuentaContado] v"
                    + " where "
                    + "v.ProveedorContado_idProveedorContado = '" + cta.getIdProveedorContado() + "'"
                    + " and v.Banco = '" + cta.getBanco() + "'"
                    + " and v.Numero = '" + cta.getNumero() + "'"
                    + " and v.Creado = '" + sqlInicio + "';";
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerCtaProveedor() sentenccia \n" + Sql);
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerCtaProveedor() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                p = new CuentaProveedorContado();
                p.setIdProveedorContado(rs.getInt("ProveedorContado_idProveedorContado"));
                p.setBanco(rs.getString("Banco"));
                p.setCreado(rs.getDate("Creado"));
                p.setNumero(rs.getString("Numero"));
                p.setIdCuentaContado(rs.getInt("idCuetaContado"));
                p.setEstado(rs.getInt("Estado"));

            }
            rs.close();

            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            p = null;
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.obtenerCtaProveedor() error " + e.getMessage());
            System.out.println("data.CrudProvContado.CrudProvedorContado.obtenerCtaProveedor() error " + e.getMessage());
        }
        return p;
    }

    public boolean deleteTelefono(int idTel) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "delete from INDICADORES. [dbo].[TelefonoSinpeContado]"
                    + " where idTelefonoSinpeContado = " + idTel + ";";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteTelefono() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteTelefono() error " + e.getMessage());

        }
        return res;
    }

    public boolean deleteProveedorContado(int idProv) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "delete from INDICADORES. [dbo].[ProveedorContado]"
                    + " where idProveedorContado = " + idProv + ";";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteProveedorContado() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteProveedorContado() error " + e.getMessage());

        }
        return res;
    }

    public boolean deleteCtaContado(int idCta) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "delete from INDICADORES. [dbo].[CuentaContado]"
                    + " where idCuetaContado = " + idCta + ";";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteTelefono() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteTelefono() error " + e.getMessage());

        }
        return res;
    }
    public boolean deleteCtaContadoByIdProv(int idProv) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "delete from INDICADORES. [dbo].[CuentaContado]"
                    + " where ProveedorContado_idProveedorContado = " + idProv + ";";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteTelefono() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteTelefono() error " + e.getMessage());

        }
        return res;
    }
    public boolean deleteTelefonoByIdProv(int idProv) {
        boolean res = false;
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "delete from INDICADORES. [dbo].[TelefonoSinpeContado]"
                    + " where ProveedorContado_idProveedorContado = " + idProv + ";";
            AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteTelefono() sentenpcia +\n" + Sql);
            sta.execute(Sql);
            res = true;
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            logic.AppLogger.appLogger.info("data.CrudProvContado.CrudProvedorContado.deleteTelefono() error " + e.getMessage());

        }
        return res;
    }


}

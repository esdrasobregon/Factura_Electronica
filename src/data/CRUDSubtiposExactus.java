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
public class CRUDSubtiposExactus {

    /**
     * this method loads the accounts for the digital receipts documents
     *
     * @param inicio a start date for the data
     * @param fin the end date for the data
     * @return ArrayList<entitys.SubtiposExactus>
     */
    public ArrayList<entitys.SubtiposExactus> obtenerSubtiposCPPorFechas(java.util.Date inicio, java.util.Date fin) {
        ArrayList<entitys.SubtiposExactus> lista = new ArrayList<entitys.SubtiposExactus>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * "
                    + "FROM INDICADORES.dbo.PRESUP_DOCUMENTOS_CP_EXACTUS e where "
                    + "e.fecha_documento >= '"
                    + sqlInicio + "' and e.fecha_documento <= '" + sqlFin + "';";
            System.out.println("data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.SubtiposExactus p = new entitys.SubtiposExactus();
                p.setTIPO(rs.getString("TIPO").trim());
                p.setSUBTIPO(rs.getString("SUBTIPO"));
                p.setDESCRIPCION(rs.getString("DESCRIPCION"));
                p.setNombre_Proveedor(rs.getString("Nombre_Proveedor").toUpperCase());
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONTO_DOLAR(rs.getDouble("MONTO_DOLAR"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setSUBTIPO(rs.getString("SUBTIPO"));
                p.setAPLICACION(rs.getString("APLICACION"));
                p.setUSUARIO_ULT_MOD(rs.getString("USUARIO_ULT_MOD"));
                p.setIdRow(rs.getString("IDROW"));
                p.setSociedad(rs.getString("Sociedad"));
                p.setMoneda(rs.getString("Moneda"));
                p.setTipoCuenta(0);
                //System.out.println("Ultimo usuario modificador " + p.getUSUARIO_ULT_MOD());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() error " + e.getMessage());

        }
        return lista;
    }

    /**
     * this method loads the accounts for the digital receipts documents
     *
     * @param inicio a start date for the data
     * @param fin the end date for the data
     * @return ArrayList<entitys.SubtiposExactus>
     */
    public ArrayList<entitys.SubtiposExactus> obtenerSubtiposCP(java.util.Date inicio, java.util.Date fin, String cia, String prov, String moneda, boolean subtipo) {
        ArrayList<entitys.SubtiposExactus> lista = new ArrayList<entitys.SubtiposExactus>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * FROM INDICADORES.dbo.PRESUP_DOCUMENTOS_CP_EXACTUS e "
                    + "where sociedad like '%" + cia + "%'"
                    + " and Nombre_Proveedor like '%" + prov + "%'"
                    + (subtipo ? " and subtipo = 0" : "")
                    + " and moneda like '%" + moneda + "%'"
                    + "and (e.fecha_documento between '" + sqlInicio + "' and '" + sqlFin + "');";
            System.out.println("data.CRUDSubtiposExactus.obtenerSubtiposCP() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.SubtiposExactus p = new entitys.SubtiposExactus();
                p.setTIPO(rs.getString("TIPO").trim());
                p.setSUBTIPO(rs.getString("SUBTIPO"));
                p.setDESCRIPCION(rs.getString("DESCRIPCION"));
                p.setNombre_Proveedor(rs.getString("Nombre_Proveedor").toUpperCase());
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONTO_DOLAR(rs.getDouble("MONTO_DOLAR"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setSUBTIPO(rs.getString("SUBTIPO"));
                p.setAPLICACION(rs.getString("APLICACION"));
                p.setUSUARIO_ULT_MOD(rs.getString("USUARIO_ULT_MOD"));
                p.setIdRow(rs.getString("IDROW"));
                p.setSociedad(rs.getString("Sociedad"));
                p.setMoneda(rs.getString("Moneda"));
                p.setTipoCuenta(0);
                //System.out.println("Ultimo usuario modificador " + p.getUSUARIO_ULT_MOD());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposCP() error " + e.getMessage());
        }
        return lista;
    }

    /**
     * this method loads the accounts for the digital receipts documents
     *
     * @param inicio a start date for the data
     * @param fin the end date for the data
     * @return ArrayList<entitys.SubtiposExactus>
     */
    public ArrayList<entitys.SubtiposExactus> obtenerSubtiposCP(java.util.Date inicio, java.util.Date fin, String cia, String prov, String codep, String cta) {
        ArrayList<entitys.SubtiposExactus> lista = new ArrayList<entitys.SubtiposExactus>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * "
                    + "FROM INDICADORES.dbo.PRESUP_DOCUMENTOS_CP_EXACTUS e where "
                    + "e.subtipo != 0"
                    + (cia.isEmpty() ? "" : " and e.Sociedad like '%" + cia + "%'")
                    + (prov.isEmpty() ? "" : " and e.nombre_proveedor like '%" + prov + "%'")
                    + (codep.isEmpty() ? "" : " and SUBSTRING(e.descripcion, 1, 2) like '%" + codep + "%'")
                    + (cta.isEmpty() ? "" : " and e.descripcion = '" + cta + "'")
                    + "and (e.fecha_documento between '" + sqlInicio + "' and  '" + sqlFin + "');";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDSubtiposExactus.obtenerSubtiposCP() sentencia\n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.SubtiposExactus p = new entitys.SubtiposExactus();
                p.setTIPO(rs.getString("TIPO").trim());
                p.setSUBTIPO(rs.getString("SUBTIPO"));
                p.setDESCRIPCION(rs.getString("DESCRIPCION"));
                p.setNombre_Proveedor(rs.getString("Nombre_Proveedor").toUpperCase());
                p.setDOCUMENTO(rs.getString("DOCUMENTO"));
                p.setFECHA_DOCUMENTO(rs.getDate("FECHA_DOCUMENTO"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONTO_DOLAR(rs.getDouble("MONTO_DOLAR"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setSUBTIPO(rs.getString("SUBTIPO"));
                p.setAPLICACION(rs.getString("APLICACION"));
                p.setUSUARIO_ULT_MOD(rs.getString("USUARIO_ULT_MOD"));
                p.setIdRow(rs.getString("IDROW"));
                p.setSociedad(rs.getString("Sociedad"));
                p.setMoneda(rs.getString("Moneda"));
                p.setTipoCuenta(0);
                //System.out.println("Ultimo usuario modificador " + p.getUSUARIO_ULT_MOD());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposCP() error " + e.getMessage());

        }
        return lista;
    }

    /**
     * this method loads the accounts for the digital receipts documents
     *
     * @param inicio a start date for the data
     * @param fin the end date for the data
     * @return ArrayList<entitys.SubtiposExactus>
     */
    public ArrayList<entitys.SubtiposExactus> obtenerSubtiposCBPorFechas(java.util.Date inicio, java.util.Date fin) {
        ArrayList<entitys.SubtiposExactus> lista = new ArrayList<entitys.SubtiposExactus>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * "
                    + "FROM INDICADORES.dbo.PRESUP_DOCUMENTOS_CB_EXACTUS e where "
                    + "e.FechaDocumento >= '"
                    + sqlInicio + "' and e.FechaDocumento <= '" + sqlFin + "';";
            System.out.println("data.CRUDSubtiposExactus.obtenerSubtiposCBPorFechas() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                String prov = rs.getString("Proveedor");
                String description = rs.getString("Cuenta_Presup");
                entitys.SubtiposExactus p = new entitys.SubtiposExactus();
                p.setTIPO(rs.getString("TIPO_DOCUMENTO").trim());
                p.setSUBTIPO(rs.getString("idSubtipo"));
                p.setDESCRIPCION(description == null ? "" : description.trim());
                p.setNombre_Proveedor(prov == null ? "" : prov.toUpperCase());
                p.setDOCUMENTO(rs.getString("NumeroDocumento"));
                p.setFECHA_DOCUMENTO(rs.getDate("FechaDocumento"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONTO_DOLAR(rs.getDouble("MONTO"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setAPLICACION(rs.getString("Referencia"));
                p.setUSUARIO_ULT_MOD("usuario sin asignar");//rs.getString("USUARIO_ULT_MOD")
                p.setIdRow(rs.getString("RowId"));
                p.setSociedad(rs.getString("CIA"));
                p.setMoneda(rs.getString("Moneda"));
                p.setTipoCuenta(1);
                //System.out.println("Ultimo usuario modificador " + p.getUSUARIO_ULT_MOD());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposCBPorFechas() error " + e.getMessage());
            System.err.println("data.CRUDSubtiposExactus.obtenerSubtiposPorFechas() error " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<entitys.SubtiposExactus> obtenerSubtiposCB(java.util.Date inicio, java.util.Date fin, String cia, String prove, String moneda, String tipo, boolean sinSubtipos) {
        ArrayList<entitys.SubtiposExactus> lista = new ArrayList<entitys.SubtiposExactus>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * "
                    + "FROM INDICADORES.dbo.PRESUP_DOCUMENTOS_CB_EXACTUS e"
                    + " where cia like '%" + cia + "%'"
                    + " and proveedor like '%" + prove + "%'"
                    + " and Tipo_Documento like '%" + tipo + "%'"
                    + (sinSubtipos ? " and Cuenta_Presup is null" : "")
                    + " and moneda like '%" + moneda + "%'"
                    + " and (e.FechaDocumento between '"
                    + sqlInicio + "' and '" + sqlFin + "');";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDSubtiposExactus.obtenerSubtiposCBPorFechas() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                String prov = rs.getString("Proveedor");
                String description = rs.getString("Cuenta_Presup");
                entitys.SubtiposExactus p = new entitys.SubtiposExactus();
                p.setTIPO(rs.getString("TIPO_DOCUMENTO").trim());
                p.setSUBTIPO(rs.getString("idSubtipo"));
                p.setDESCRIPCION(description == null ? "" : description.trim());
                p.setNombre_Proveedor(prov == null ? "" : prov.toUpperCase());
                p.setDOCUMENTO(rs.getString("NumeroDocumento"));
                p.setFECHA_DOCUMENTO(rs.getDate("FechaDocumento"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONTO_DOLAR(rs.getDouble("MONTO"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setAPLICACION(rs.getString("Referencia"));
                p.setUSUARIO_ULT_MOD("usuario sin asignar");//rs.getString("USUARIO_ULT_MOD")
                p.setIdRow(rs.getString("RowId"));
                p.setSociedad(rs.getString("CIA"));
                p.setMoneda(rs.getString("Moneda"));
                p.setTipoCuenta(1);
                //System.out.println("Ultimo usuario modificador " + p.getUSUARIO_ULT_MOD());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposCBPorFechas() error " + e.getMessage());

        }
        return lista;
    }

    public ArrayList<entitys.SubtiposExactus> obtenerSubtiposCB(java.util.Date inicio, java.util.Date fin, String cia, String prove, String cuenta) {
        ArrayList<entitys.SubtiposExactus> lista = new ArrayList<entitys.SubtiposExactus>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * "
                    + "FROM INDICADORES.dbo.PRESUP_DOCUMENTOS_CB_EXACTUS e"
                    + " where cia like '%" + cia + "%'"
                    + " and proveedor like '%" + prove + "%'"
                    + " and cuenta_presup like '%" + cuenta + "%'"
                    + " and Cuenta_Presup is not null"
                    + " and (e.FechaDocumento between '"
                    + sqlInicio + "' and '" + sqlFin + "');";
            AppLogger.appLogger.log(Level.WARNING, "data.CRUDSubtiposExactus.obtenerSubtiposCBPorFechas() sentencia \n" + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                String prov = rs.getString("Proveedor");
                String description = rs.getString("Cuenta_Presup");
                entitys.SubtiposExactus p = new entitys.SubtiposExactus();
                p.setTIPO(rs.getString("TIPO_DOCUMENTO").trim());
                p.setSUBTIPO(rs.getString("idSubtipo"));
                p.setDESCRIPCION(description == null ? "" : description.trim());
                p.setNombre_Proveedor(prov == null ? "" : prov.toUpperCase());
                p.setDOCUMENTO(rs.getString("NumeroDocumento"));
                p.setFECHA_DOCUMENTO(rs.getDate("FechaDocumento"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONTO_DOLAR(rs.getDouble("MONTO"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setAPLICACION(rs.getString("Referencia"));
                p.setUSUARIO_ULT_MOD("usuario sin asignar");//rs.getString("USUARIO_ULT_MOD")
                p.setIdRow(rs.getString("RowId"));
                p.setSociedad(rs.getString("CIA"));
                p.setMoneda(rs.getString("Moneda"));
                p.setTipoCuenta(1);
                //System.out.println("Ultimo usuario modificador " + p.getUSUARIO_ULT_MOD());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposCBPorFechas() error " + e.getMessage());

        }
        return lista;
    }

    public ArrayList<entitys.SubtiposExactus> obtenerSubtiposCGPorFechas(java.util.Date inicio, java.util.Date fin) {
        ArrayList<entitys.SubtiposExactus> lista = new ArrayList<entitys.SubtiposExactus>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "SELECT * "
                    + "FROM INDICADORES.dbo.PRESUP_DOCUMENTOS_CG_EXACTUS e where "
                    + "e.FechaRegistro >= '"
                    + sqlInicio + "' and e.FechaRegistro <= '" + sqlFin + "';";
            System.out.println("data.CRUDSubtiposExactus.obtenerSubtiposCGPorFechas() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.SubtiposExactus p = new entitys.SubtiposExactus();
                p.setTIPO(rs.getString("TIPO_DOCUMENTO").trim());
                p.setSUBTIPO(rs.getString("idSubtipo"));
                p.setDESCRIPCION(rs.getString("Cuenta_Presup"));
                p.setNombre_Proveedor(rs.getString("Proveedor").toUpperCase());
                p.setDOCUMENTO(rs.getString("NumeroDocumento"));
                p.setFECHA_DOCUMENTO(rs.getDate("FechaRegistro"));
                p.setMONTO(rs.getDouble("MONTO"));
                p.setMONTO_DOLAR(rs.getDouble("MONTO"));
                p.setASIENTO(rs.getString("ASIENTO"));
                p.setAPLICACION(rs.getString("Referencia"));
                p.setUSUARIO_ULT_MOD("usuario sin asignar");//rs.getString("USUARIO_ULT_MOD")
                p.setIdRow(rs.getString("RowId"));
                p.setSociedad(rs.getString("CIA"));
                p.setMoneda(rs.getString("Moneda"));
                //System.out.println("Ultimo usuario modificador " + p.getUSUARIO_ULT_MOD());
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.obtenerSubtiposCGPorFechas() error " + e.getMessage());
            System.err.println("data.CRUDSubtiposExactus.obtenerSubtiposCGPorFechas() error " + e.getMessage());
        }
        return lista;
    }

    /**
     * this method updates the description of a register on database
     *
     * @param cia is the company associated to the register to update
     * @param rowPointer is a unique string associated to the register
     * @param documento is the string document number associated to the register
     * @param subtipoNuevo is the new string value for the description
     * @return a Boolean value, true if success, a false value if it fails
     */
    public boolean actualizarSubtiposCP(String cia, String rowPointer, String documento, String subtipoNuevo) {
        boolean res = false;
        String procedureCall = "exec indicadores.dbo.UPDATE_SUBTIPO_CP '"
                + cia + "','"
                + rowPointer + "','"
                + documento + "','"
                + subtipoNuevo + "';";
        AppLogger.appLogger.log(Level.WARNING, "data.CRUDSubtiposExactus.actualizarSubtiposCP() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.actualizarSubtiposCP() error " + e.getMessage());

        }
        return res;
    }

    public boolean actualizarSubtiposCB(String cia, String rowPointer, String documento, String subtipoNuevo) {
        boolean res = false;
        String procedureCall = "exec indicadores.dbo.UPDATE_SUBTIPO_CB '"
                + cia + "','"
                + rowPointer + "','"
                + documento + "','"
                + subtipoNuevo + "';";
        AppLogger.appLogger.log(Level.WARNING, "data.CRUDSubtiposExactus.actualizarSubtiposCB() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.actualizarSubtiposCB() error " + e.getMessage());

        }
        return res;
    }

    public boolean actualizarSubtiposCG(String cia, String rowPointer, String documento, String subtipoNuevo) {
        boolean res = false;
        String procedureCall = "exec indicadores.dbo.UPDATE_SUBTIPO_CG '"
                + cia + "','"
                + rowPointer + "','"
                + documento + "','"
                + subtipoNuevo + "';";
        AppLogger.appLogger.log(Level.WARNING, "data.CRUDSubtiposExactus.actualizarSubtiposCG() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            sta.execute(procedureCall);
            res = true;
            sta.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDSubtiposExactus.actualizarSubtiposCG() error " + e.getMessage());
            System.err.println("data.CRUDSubtiposExactus.actualizarSubtiposCB() error " + e.getMessage());
        }
        return res;
    }

}

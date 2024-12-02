/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.Proveedor;
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
public class CrudProveedor {

    public ArrayList<Proveedor> getProveedores() {
        ArrayList<Proveedor> res = new ArrayList<Proveedor>();
        String procedureCall = "select *from INDICADORES.dbo.obtenerProv_View p where  p.Activo = 'S';";
        System.out.println("data.CrudProveedor.getProveedores() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            while (rs.next()) {
                Proveedor p = new Proveedor();
                p.setCIA_PROV(rs.getString("Cia_Prov").trim());
                p.setProveedor(rs.getString("Proveedor").trim());
                p.setNombre(rs.getString("Nombre").trim());
                p.setAlias(rs.getString("Alias"));
                p.setActivo(rs.getString("Activo"));
                res.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudProveedor.getProveedores() error " + e.getMessage());
            System.out.println("data.CrudProveedor.getProveedores() error " + e.getMessage());
        }
        return res;
    }

    public Proveedor getProveedor(String cia_prov) {
        Proveedor res = null;
        String procedureCall = "select *from INDICADORES.dbo.obtenerProv_View p where  p.cia_prov = '" + cia_prov + "';";
        System.out.println("data.CrudProveedor.getProveedor() sentencia \n" + procedureCall);
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(procedureCall);
            Proveedor p = new Proveedor();
            while (rs.next()) {
                p.setCIA_PROV(rs.getString("Cia_Prov").trim());
                p.setProveedor(rs.getString("Proveedor").trim());
                p.setNombre(rs.getString("Nombre").trim());
                p.setAlias(rs.getString("Alias"));
                p.setActivo(rs.getString("Activo"));
            }
            res = p;
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CrudProveedor.getProveedor() error " + e.getMessage());
            System.out.println("data.CrudProveedor.getProveedor() error " + e.getMessage());
            res = null;
        }
        return res;
    }

    public ArrayList<entitys.DetalleMoraProv> obtenerResuMoraProvedor(String pro) {
        ArrayList<entitys.DetalleMoraProv> lista = new ArrayList<>();
        try {
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            String Sql = "select "
                    + "sum(Monto)as sumMonto"
                    + ", sum(saldo) as sumSaldo"
                    + ", v.TIPO_MORA  from EXACTUS.dbo.cp_cilt_rymsa v "
                    + "where v.proveedor = '" + pro + "'"
                    + " group by v.TIPO_MORA";
            System.out.println("data.CRUDHistoricoCP.obtenerResuPagProvedor() sentencia " + "\n" + Sql);
            AppLogger.appLogger.log(Level.WARNING, "sentencia " + Sql);
            ResultSet rs = sta.executeQuery(Sql);
            while (rs.next()) {
                entitys.DetalleMoraProv p = new entitys.DetalleMoraProv();
                p.setSumSaldo(rs.getDouble("sumSaldo"));
                p.setTipo_mora(rs.getString("tipo_mora"));
                lista.add(p);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);

        } catch (Exception e) {
            AppLogger.appLogger.log(Level.SEVERE, "data.CRUDHistoricoCP.obtenerResuPagProvedor() error " + e.getMessage());
            System.out.println("data.CRUDHistoricoCP.obtenerResuPagProvedor() error " + e.getMessage());
        }
        return lista;
    }

}

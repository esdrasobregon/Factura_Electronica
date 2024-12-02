/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.CorreoExcluidoFE;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CrudCorreoExcluidoFE {
    
    public static boolean addUpdateCorreoExcluidoFe(CorreoExcluidoFE c) {
        boolean res = false;
        try {
            //here sonoo is database name, root is username and password 
            String sentencia = "call Addcorreosexluidosfe('"
                    + c.getIdCorreo() + "','"
                    + c.getNombreCorreo() + "','"
                    + new java.sql.Date(c.getFecha().getTime()) + "')";
            System.out.println("data.CrudCorreoExcluidoFE.addUpdateFacturaElectronica() sentencia " + "\n sentencia " + sentencia);
            AppLogger.appLogger.info("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            statement.execute(sentencia);
            res = true;
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            AppLogger.appLogger.info("data.CrudCorreoExcluidoFE.addUpdateFacturaElectronica() error " + e.getMessage());
            System.out.println("data.CrudCorreoExcluidoFE.addUpdateFacturaElectronica() error " + e.getMessage());
        }
        return res;
    }
    
    public ArrayList<CorreoExcluidoFE> obtenerCorreosExclidosPorFecha(java.util.Date inicio, java.util.Date fin) {
        ArrayList<CorreoExcluidoFE> res = new ArrayList<>();
        java.sql.Date sqlInicio = new java.sql.Date(inicio.getTime());
        java.sql.Date sqlFin = new java.sql.Date(fin.getTime());
        try {
            String sentencia = "call obtenerCorreosExclidosPorFecha('" + sqlInicio.toString() + "','" + sqlFin + "')";
            System.out.println("sentencia " + sentencia);
            AppLogger.appLogger.info("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                CorreoExcluidoFE n = new CorreoExcluidoFE();
                n.setIdCorreo(rs.getInt("Idcorreo"));
                n.setNombreCorreo(rs.getString("nombreCorreo"));
                n.setFecha(rs.getDate("fecha"));
                
                res.add(n);
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            AppLogger.appLogger.info("data.CrudCorreoExcluidoFE.obtenerCorreosExclidosPorFecha() error " + e.getMessage());
            System.out.println("data.CrudCorreoExcluidoFE.obtenerCorreosExclidosPorFecha() error " + e.getMessage());
            DbPoolHandler.connectionPool.initializeConnections();
        }
        return res;
    }
}

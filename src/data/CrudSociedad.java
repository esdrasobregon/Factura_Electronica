/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.Sociedad;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author eobregon
 */
public class CrudSociedad {

    public  ArrayList<Sociedad> obtenerSociedades() {
        ArrayList<Sociedad> res = new ArrayList<>();
        try {
            String sentencia = "call obtenerSociedades()";
            System.out.println("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                Sociedad n = new Sociedad();
                n.setNombre(rs.getString("Nombre"));
                n.setCedula(rs.getString("Cedula"));
                n.setCorreo(rs.getString("Correo"));
                n.setActivo(rs.getInt("Activo"));
                res.add(n);
            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            System.err.println("data.CrudSociedad.obtenerFacturasPorFecha() error " + e.getMessage());
        }
        return res;
    }

}

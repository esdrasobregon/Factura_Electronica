/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.Departamento;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author eobregon
 */
public class crudDepartamento {

        
    public ArrayList<Departamento> getSqlDepartamentos() {
        ArrayList<Departamento> list = new ArrayList<>();

        try {
            
            String sentencia = "select *from PRESUP_DEPARTAMENTOS d"
                    + " where "
                    + "d.DEPARTAMENTO !='ND' "
                    + "and d.DESCRIPCION != 'JUNTA DIRECTIVA'";
            System.out.println("sentencia " + sentencia);
            Connection connection = sqlPoolInstance.pool.getConnection();
            Statement sta = connection.createStatement();
            ResultSet rs = sta.executeQuery(sentencia);
            while (rs.next()) {
                Departamento d = new Departamento();
                d.setDEPARTAMENTO(rs.getString("DEPARTAMENTO").trim());
                d.setDescripcion(rs.getString("DESCRIPCION").trim());
                d.setJEFE(rs.getString("JEFE"));
                list.add(d);
            }
            rs.close();
            sqlPoolInstance.pool.releaseConnection(connection);
            return list;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error " + e.getMessage());
            return null;
        }
    }

}

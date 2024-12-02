/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import entitys.LineaDetalle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import logic.AppLogger;

/**
 *
 * @author eobregon
 */
public class CRUDLineaDetalle {

    public static ArrayList<LineaDetalle> obtenerLineas(String clave) {
        ArrayList<LineaDetalle> res = new ArrayList<>();
        try {
            String sentencia = "SELECT * FROM sos.lineadetallefe l WHERE l.Clave = '" + clave + "';";
            AppLogger.appLogger.info("data.CRUDLineaDetalle.obtenerLineas() sentencia\n" + sentencia);
            System.out.println("sentencia " + sentencia);
            Connection conn = DbPoolHandler.connectionPool.getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sentencia);
            while (rs.next()) {
                LineaDetalle n = new LineaDetalle();
                n.setCodigo(rs.getString("Codigo"));
                n.setCantidad(rs.getString("Cantidad"));
                n.setUnidadMedida(rs.getString("UnidadMedida"));
                n.setDetalle(rs.getString("Detalle"));
                n.setMontoTotalLinea(rs.getDouble("MontoTotalLinea"));
                n.setPrecioUnitario(rs.getDouble("PrecioUnitario"));
                n.setMontoTotal(rs.getDouble("MontoTotal"));
                n.setSubTotal(rs.getDouble("SubTotal"));
                n.setNumeroLinea(rs.getInt("NumeroLinea"));
                res.add(n);

            }
            rs.close();
            DbPoolHandler.connectionPool.releaseConnection(conn);
            statement.close();
        } catch (Exception e) {
            DbPoolHandler.connectionPool.initializeConnections();
            System.err.println("data.CRUDLineaDetalle.obtenerLineas() sentencia error " + e.getMessage());

        }
        return res;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;
import data.DataUser;
import data.indicadoresDbPool;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author eobregon
 */
public class sqlPoolInstance {

    public static indicadoresDbPool pool;

    public static void initPool() {
        try {
            if (sqlPoolInstance.pool == null) {
                sqlPoolInstance.pool = indicadoresDbPool.create(DataUser.username, DataUser.password);
                System.out.println("sql pool initiated in a request method");
                System.out.println("pool connections " + sqlPoolInstance.pool.getSize());
            }
        } catch (Exception e) {
            System.err.println("data.sqlPoolInstance.initPool() error " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://DESKTOP-QRLPE70\\SQLEXPRESS:1433;databaseName=indicadores;";
        String user = "YourUsername";
        String password = "YourPassword";

        try {
            Connection connection = DriverManager.getConnection(url, "EOBREGON", "Obr5612on");
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed. Error: " + e.getMessage());
        }
    }
}

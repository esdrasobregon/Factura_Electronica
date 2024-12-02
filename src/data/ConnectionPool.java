/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eobregon
 */
public class ConnectionPool {

    private static final int MAX_CONNECTIONS = 10;
    private List<Connection> availableConnections;

    public ConnectionPool() {
        availableConnections = new ArrayList<>();
        initializeConnections();
    }

    public void initializeConnections() {
        for (int i = availableConnections.size(); i < MAX_CONNECTIONS; i++) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(logic.AppStaticValues.sosUrl, DataUser.username, DataUser.password);
                availableConnections.add(connection);
            } catch (Exception e) {
                System.out.println("Error initializing connection: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        if (availableConnections.isEmpty()) {
            System.out.println("No available connections. Please try again later.");
            return null;
        }
        Connection connection = availableConnections.remove(0);
        return connection;
    }

    public void releaseConnection(Connection connection) {
        availableConnections.add(connection);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import logic.AppLogger;

/**
 *
 * @author programador1
 */
public class indicadoresDbPool {
    //private static String server = "149.28.33.140";

    /*private static final String port = "1433";
    private static final String db = "EXACTUS";
     */
    private static final String port = "1433";
    private static final String db = "Indicadores";
    private static final int MAX_POOL_SIZE = 20;
    private static final int MAX_TIMEOUT = 5;

    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static final int INITIAL_POOL_SIZE = 10;

    public indicadoresDbPool() {

    }

    private indicadoresDbPool(List<Connection> pool) {
        //DataUser.password = "TapaAdmin3201**";
        this.connectionPool = pool;
        this.usedConnections = new ArrayList<>();
    }

    public static indicadoresDbPool create(String user,
            String password) throws SQLException {

        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            //System.out.println("data.SqlExactusDbPool.create() creating connection wiht user " + user + " password " + password);
            Connection connection = createConnection(user, password, "Indicadores");
            if (connection == null) {
                System.err.println("data.SqlExactusDbPool.create() connection is null");
            } else {
                pool.add(connection);
            }
        }
        return new indicadoresDbPool(pool);
    }

    /*
    // standard constructors
    public Connection getConnection() {
        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }*/
    public Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection(DataUser.username, DataUser.password, "Indicadores"));
            } else {
                releaseAllConnection();
                throw new RuntimeException(
                        "Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool
                .remove(connectionPool.size() - 1);

        if (!connection.isValid(MAX_TIMEOUT)) {
            connection = createConnection(DataUser.username, DataUser.password, "Indicadores");
        }

        usedConnections.add(connection);
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    public void releaseAllConnection() {
        AppLogger.appLogger.warning("releasing all connections...");
        for (Connection con : usedConnections) {
            connectionPool.add(con);
            usedConnections.remove(con);
        }
        AppLogger.appLogger.warning("data.indicadoresDbPool.releaseAllConnection() avilable connections "+connectionPool.size());
        
    }

    private static Connection createConnection(String user, String password, String db) {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            String url = "jdbc:sqlserver://" + logic.AppStaticValues.sqlServer + ":" + port + ";databaseName=" + db;
            //String url = "jdbc:sqlserver://DESKTOP-QRLPE70\\SQLEXPRESS" + ";databaseName=" + db;
            conn = DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            System.err.println("Data.SqlExactusDbPool.createConnection() error " + e.getMessage());
        }
        return conn;
    }
    public static Connection createIndicadoresConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            String url = "jdbc:sqlserver://" + logic.AppStaticValues.sqlServer + ":" + port + ";databaseName=" + db;
            //String url = "jdbc:sqlserver://DESKTOP-QRLPE70\\SQLEXPRESS" + ";databaseName=" + db;
            conn = DriverManager.getConnection(url, DataUser.username, DataUser.password);

        } catch (Exception e) {
            System.err.println("Data.SqlExactusDbPool.createConnection() error " + e.getMessage());
        }
        return conn;
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

}

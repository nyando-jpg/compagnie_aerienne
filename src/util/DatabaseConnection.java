package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections
 * This class provides methods to connect to the PostgreSQL database
 */
public class DatabaseConnection {
    
    // Database configuration constants
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/compagnie";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    private static final String DB_DRIVER = "org.postgresql.Driver";
    
    /**
     * Static block to load the PostgreSQL JDBC driver
     */
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("PostgreSQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC Driver not found");
            e.printStackTrace();
        }
    }
    
    /**
     * Get a connection to the database
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established successfully");
            return connection;
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection");
            throw e;
        }
    }
    
    /**
     * Close the database connection
     * @param connection the connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully");
            } catch (SQLException e) {
                System.err.println("Error closing database connection");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test the database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed");
            e.printStackTrace();
            return false;
        }
    }
}

// ============================================
// DatabaseConnection.java
// ============================================
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton Pattern - Manages database connection
 * FIXED: Connection stays open throughout application lifecycle
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static final String URL = "jdbc:sqlite:DatabaseSQLite.db";
    private Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            // IMPORTANT: Keep connection open
            connection.setAutoCommit(true);
            initializeDatabase();
            System.out.println("Database connection established");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // If connection is closed or null, recreate it
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                connection.setAutoCommit(true);
                System.out.println("Database connection re-established");
            }
        } catch (SQLException e) {
            System.err.println("Error getting connection: " + e.getMessage());
        }
        return connection;
    }

    private void initializeDatabase() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL UNIQUE,
                user_password TEXT NOT NULL,
                first_name TEXT NOT NULL,
                last_name TEXT NOT NULL,
                address TEXT,
                phone TEXT,
                role TEXT NOT NULL CHECK(role IN ('ADMIN', 'CUSTOMER', 'AGENT')),
                promotions_opt_in BOOLEAN NOT NULL DEFAULT 0,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """;

        String createFlightsTable = """
            CREATE TABLE IF NOT EXISTS flights (
                flight_id INTEGER PRIMARY KEY AUTOINCREMENT,
                flight_number TEXT NOT NULL UNIQUE,
                origin TEXT NOT NULL,
                destination TEXT NOT NULL,
                departure_time DATETIME NOT NULL,
                arrival_time DATETIME NOT NULL,
                airline TEXT NOT NULL,
                available_seats INTEGER NOT NULL,
                price REAL NOT NULL,
                aircraft_type TEXT
            )
        """;

        String createBookingsTable = """
            CREATE TABLE IF NOT EXISTS bookings (
                booking_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                flight_id INTEGER NOT NULL,
                booking_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                status TEXT NOT NULL CHECK(status IN ('CONFIRMED', 'CANCELLED')) DEFAULT 'CONFIRMED',
                seat_number TEXT,
                FOREIGN KEY (user_id) REFERENCES users(user_id),
                FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
            )
        """;

        String createPaymentsTable = """
            CREATE TABLE IF NOT EXISTS payments (
                payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                booking_id INTEGER NOT NULL UNIQUE,
                amount REAL NOT NULL,
                payment_method TEXT NOT NULL,
                card_number TEXT,
                payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createFlightsTable);
            stmt.execute(createBookingsTable);
            stmt.execute(createPaymentsTable);
            System.out.println("Database tables initialized");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Close connection when application exits
     * Call this in a shutdown hook
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
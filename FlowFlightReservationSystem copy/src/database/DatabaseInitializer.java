// ============================================
// DatabaseInitializer.java
// Run this ONCE to create admin and agent test accounts
// ============================================
package database;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

/**
 * Utility class to initialize test users
 */
public class DatabaseInitializer {

    public static void main(String[] args) {
        initializeTestUsers();
    }

    public static void initializeTestUsers() {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        
        try {
            // Check if admin exists
            if (!userExists(conn, "admin@flight.com")) {
                createUser(conn, "admin@flight.com", "admin123", "Admin", "1", 
                          "123 Admin St", "403-555-0001", "ADMIN", false);
                System.out.println("✓ Admin account created:");
                System.out.println("  Email: admin@flight.com");
                System.out.println("  Password: admin123");
            }
            
            // Check if agent exists
            if (!userExists(conn, "agent@flight.com")) {
                createUser(conn, "agent@flight.com", "agent123", "Agent", "1", 
                          "456 Agent Ave", "403-555-0002", "AGENT", false);
                System.out.println("✓ Agent account created:");
                System.out.println("  Email: agent@flight.com");
                System.out.println("  Password: agent123");
            }
            
            // Check if test customer exists
            if (!userExists(conn, "customer@test.com")) {
                createUser(conn, "customer@test.com", "customer123", "John", "Doe", 
                          "789 Customer Rd", "403-555-0003", "CUSTOMER", true);
                System.out.println("✓ Test customer account created:");
                System.out.println("  Email: customer@test.com");
                System.out.println("  Password: customer123");
            }
            
            System.out.println("\n✓ Database initialization complete!");
            System.out.println("\nYou can now login with:");
            System.out.println("1. Admin: admin@flight.com / admin123");
            System.out.println("2. Agent: agent@flight.com / agent123");
            System.out.println("3. Customer: customer@test.com / customer123");
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static boolean userExists(Connection conn, String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            System.err.println("Error checking user: " + e.getMessage());
        }
        return false;
    }
    
    private static void createUser(Connection conn, String email, String password, 
                                   String firstName, String lastName, String address, 
                                   String phone, String role, boolean promotions) {
        String hashedPassword = hashPassword(password);
        String query = "INSERT INTO users (email, user_password, first_name, last_name, " +
                      "address, phone, role, promotions_opt_in) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, address);
            stmt.setString(6, phone);
            stmt.setString(7, role);
            stmt.setInt(8, promotions ? 1 : 0);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }
    
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return password;
        }
    }
}
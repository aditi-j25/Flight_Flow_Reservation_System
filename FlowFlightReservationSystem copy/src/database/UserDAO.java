// ============================================
// UserDAO.java
// ============================================
package database;

import businessLogic.control.UserFactory;
import businessLogic.entity.User;
import java.sql.*;

/**
 * Data Access Object for User operations
 */
public class UserDAO {

    public User authenticateUser(String email, String hashedPassword) {
        String query = "SELECT * FROM users WHERE email = ? AND user_password = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }

    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
        }
        return false;
    }

    public int insertUser(String email, String hashedPassword, String firstName,
                          String lastName, String address, String phone,
                          String role, boolean receivePromotions) {
        String query = "INSERT INTO users (email, user_password, first_name, last_name, " +
                "address, phone, role, promotions_opt_in) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, address);
            stmt.setString(6, phone);
            stmt.setString(7, role);
            stmt.setInt(8, receivePromotions ? 1 : 0);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
        return -1;
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        return null;
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
        }
        return null;
    }

    public boolean updatePromotionPreference(int userId, boolean receivePromotions) {
        String query = "UPDATE users SET promotions_opt_in = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, receivePromotions ? 1 : 0);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating promotion preference: " + e.getMessage());
        }
        return false;
    }

    /**
     * NEW: Update user profile information
     */
    public boolean updateUserProfile(int userId, String firstName, String lastName, 
                                     String address, String phone) {
        String query = "UPDATE users SET first_name = ?, last_name = ?, address = ?, phone = ? " +
                      "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.setInt(5, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user profile: " + e.getMessage());
        }
        return false;
    }

    public int getTotalUserCount() {
        String query = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
        }
        return 0;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String password = rs.getString("user_password");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String address = rs.getString("address");
        String phone = rs.getString("phone");
        String role = rs.getString("role");
        int receivePromotions = rs.getInt("promotions_opt_in");

        return UserFactory.createUser(userId, email, password, firstName,
                lastName, address, phone, role, receivePromotions);
    }
}
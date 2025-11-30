package database;

import businessLogic.control.UserFactory;
import businessLogic.entity.User;
import java.sql.*;

/**
 * Data Access Object for User operations
 */
public class UserDAO {

    /**
     * Authenticate user with email and password
     * @return User object if found, null otherwise
     */
    public User authenticateUser(String email, String hashedPassword) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

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

    /**
     * Check if email already exists in database
     */
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

    /**
     * Insert new user into database
     * @return userId if successful, -1 if failed
     */
    public int insertUser(String email, String hashedPassword, String firstName,
                          String lastName, String address, String phone,
                          String role, boolean receivePromotions) {
        String query = "INSERT INTO users (email, password, first_name, last_name, " +
                "address, phone, role, receive_promotions) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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

    /**
     * Get user by ID
     */
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

    /**
     * Update user's promotion preference
     */
    public boolean updatePromotionPreference(int userId, boolean receivePromotions) {
        String query = "UPDATE users SET receive_promotions = ? WHERE user_id = ?";

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
     * Extract User object from ResultSet using UserFactory
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String address = rs.getString("address");
        String phone = rs.getString("phone");
        String role = rs.getString("role");
        int receivePromotions = rs.getInt("receive_promotions");

        return UserFactory.createUser(userId, email, password, firstName,
                lastName, address, phone, role, receivePromotions);
    }
}
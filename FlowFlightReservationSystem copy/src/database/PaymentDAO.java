// ============================================
// PaymentDAO.java
// ============================================
package database;

import businessLogic.entity.Payment;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data Access Object for Payment operations
 */
public class PaymentDAO {

    public int createPayment(int bookingId, double amount, String paymentMethod, String cardNumber) {
        String query = "INSERT INTO payments (booking_id, amount, payment_method, card_number) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bookingId);
            stmt.setDouble(2, amount);
            stmt.setString(3, paymentMethod);
            stmt.setString(4, cardNumber);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
        }
        return -1;
    }

    public Payment getPaymentByBookingId(int bookingId) {
        String query = "SELECT * FROM payments WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // FIXED: Handle SQLite datetime format (space instead of 'T')
                String paymentDateStr = rs.getString("payment_date");
                LocalDateTime paymentDate = LocalDateTime.parse(paymentDateStr.replace(" ", "T"));
                
                return new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("booking_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method"),
                        rs.getString("card_number"),
                        paymentDate
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment: " + e.getMessage());
        }
        return null;
    }
}
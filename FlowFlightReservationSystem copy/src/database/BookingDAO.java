// ============================================
// BookingDAO.java
// ============================================
package database;

import businessLogic.entity.Booking;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Booking operations
 */
public class BookingDAO {

    public int createBooking(int userId, int flightId, String seatNumber) {
        String query = "INSERT INTO bookings (user_id, flight_id, seat_number, status) " +
                "VALUES (?, ?, ?, 'CONFIRMED')";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, flightId);
            stmt.setString(3, seatNumber);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
        }
        return -1;
    }

    public boolean cancelBooking(int bookingId) {
        String query = "UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
        }
        return false;
    }

    public List<Booking> getUserBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        // FIXED: Changed f.departure_city to f.origin and f.arrival_city to f.destination
        String query = "SELECT b.*, f.flight_number, f.origin, f.destination, " +
                "u.first_name, u.last_name, u.email " +
                "FROM bookings b " +
                "JOIN flights f ON b.flight_id = f.flight_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "WHERE b.user_id = ? ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting user bookings: " + e.getMessage());
        }
        return bookings;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        // FIXED: Changed f.departure_city to f.origin and f.arrival_city to f.destination
        String query = "SELECT b.*, f.flight_number, f.origin, f.destination, " +
                "u.first_name, u.last_name, u.email " +
                "FROM bookings b " +
                "JOIN flights f ON b.flight_id = f.flight_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                bookings.add(extractBookingFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
        }
        return bookings;
    }

    public Booking getBookingById(int bookingId) {
        // FIXED: Changed f.departure_city to f.origin and f.arrival_city to f.destination
        String query = "SELECT b.*, f.flight_number, f.origin, f.destination, " +
                "u.first_name, u.last_name, u.email " +
                "FROM bookings b " +
                "JOIN flights f ON b.flight_id = f.flight_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "WHERE b.booking_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractBookingFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting booking: " + e.getMessage());
        }
        return null;
    }

    private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
        // FIXED: Handle SQLite datetime format (space instead of 'T')
        String bookingDateStr = rs.getString("booking_date");
        LocalDateTime bookingDate = LocalDateTime.parse(bookingDateStr.replace(" ", "T"));
        
        Booking booking = new Booking(
                rs.getInt("booking_id"),
                rs.getInt("user_id"),
                rs.getInt("flight_id"),
                bookingDate,
                rs.getString("status"),
                rs.getString("seat_number")
        );

        booking.setFlightNumber(rs.getString("flight_number"));
        // FIXED: Changed from departure_city/arrival_city to origin/destination
        booking.setRoute(rs.getString("origin") + " → " + rs.getString("destination"));
        booking.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
        booking.setCustomerEmail(rs.getString("email"));

        return booking;
    }
}
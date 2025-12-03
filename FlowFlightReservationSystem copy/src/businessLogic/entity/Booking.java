// ============================================
// Booking.java
// ============================================
package businessLogic.entity;

import java.time.LocalDateTime;

/**
 * <<entity>>
 * Represents a flight booking
 */
public class Booking {
    private int bookingId;
    private int userId;
    private int flightId;
    private LocalDateTime bookingDate;
    private String status;
    private String seatNumber;

    // For display purposes
    private String customerName;
    private String customerEmail;
    private String flightNumber;
    private String route;

    public Booking(int bookingId, int userId, int flightId, LocalDateTime bookingDate,
                   String status, String seatNumber) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.seatNumber = seatNumber;
    }

    // Getters
    public int getBookingId() { return bookingId; }
    public int getUserId() { return userId; }
    public int getFlightId() { return flightId; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public String getStatus() { return status; }
    public String getSeatNumber() { return seatNumber; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getFlightNumber() { return flightNumber; }
    public String getRoute() { return route; }

    // Setters
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
    public void setStatus(String status) { this.status = status; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setRoute(String route) { this.route = route; }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + bookingId +
                ", flight=" + flightNumber +
                ", seat=" + seatNumber +
                ", status=" + status +
                '}';
    }
}
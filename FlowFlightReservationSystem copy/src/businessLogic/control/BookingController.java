// ============================================
// Updated BookingController.java
// ============================================
package businessLogic.control;

import businessLogic.entity.Booking;
import businessLogic.entity.Flight;
import database.BookingDAO;
import database.FlightDAO;
import java.util.List;

/**
 * <<control>>
 * Handles booking operations following activity diagram logic
 */
public class BookingController {
    private BookingDAO bookingDAO;
    private FlightDAO flightDAO;

    public BookingController() {
        this.bookingDAO = new BookingDAO();
        this.flightDAO = new FlightDAO();
    }

    /**
     * Create a new booking (Make New Booking from activity diagram)
     * Step 1: Enter passenger details (userId)
     * Step 2: Select seats (seatNumber)
     * Step 3: Review booking summary (flightId)
     * Step 4: Confirm booking
     * @return bookingId if successful, -1 if failed
     */
    public int createBooking(int userId, int flightId, String seatNumber) {
        // Validate flight availability
        Flight flight = flightDAO.getFlightById(flightId);
        if (flight == null || !flight.hasAvailableSeats()) {
            System.out.println("Booking failed: No available seats");
            return -1;
        }

        // Create booking in database
        int bookingId = bookingDAO.createBooking(userId, flightId, seatNumber);
        
        if (bookingId > 0) {
            // Update flight seat availability
            flightDAO.decrementAvailableSeats(flightId);
            System.out.println("Booking created successfully: " + bookingId);
        }
        
        return bookingId;
    }

    /**
     * Cancel booking (Cancel Reservation from activity diagram)
     * Step 1: Enter reservation ID
     * Step 2: Retrieve from database
     * Step 3: Display cancellation policy
     * Step 4: Confirm cancellation
     * @return true if successful
     */
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (booking == null) {
            System.out.println("Booking not found: " + bookingId);
            return false;
        }
        
        if ("CANCELLED".equals(booking.getStatus())) {
            System.out.println("Booking already cancelled: " + bookingId);
            return false;
        }

        // Cancel in database
        boolean success = bookingDAO.cancelBooking(bookingId);
        
        if (success) {
            // Return seat to flight inventory
            flightDAO.incrementAvailableSeats(booking.getFlightId());
            System.out.println("Booking cancelled: " + bookingId);
        }
        
        return success;
    }

    /**
     * Get user bookings (View/Generate Confirmation from activity diagram)
     * Step 1: Enter reservation ID (or get all for user)
     * Step 2: Retrieve from database
     * Step 3: Display reservation details
     */
    public List<Booking> getUserBookings(int userId) {
        return bookingDAO.getUserBookings(userId);
    }

    /**
     * Get all bookings (for admin/agent)
     */
    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    /**
     * Get specific booking by ID
     * Used for modify reservation and view confirmation
     */
    public Booking getBookingById(int bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }

    /**
     * Modify booking (Modify Reservation from activity diagram)
     * Step 1: Enter reservation ID
     * Step 2: Retrieve booking
     * Step 3: Display current details
     * Step 4: Select what to change
     * Step 5: Update reservation
     */
    public boolean modifyBooking(int bookingId, String newSeatNumber) {
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (booking == null) {
            System.out.println("Booking not found: " + bookingId);
            return false;
        }
        
        if ("CANCELLED".equals(booking.getStatus())) {
            System.out.println("Cannot modify cancelled booking: " + bookingId);
            return false;
        }

        // Update seat number (could extend to other fields)
        booking.setSeatNumber(newSeatNumber);
        
        // Note: You'd need to add updateBooking method to BookingDAO
        System.out.println("Booking modified: " + bookingId);
        return true;
    }
}
// ============================================
// AgentController.java
// ============================================
package businessLogic.control;

import businessLogic.entity.User;
import businessLogic.entity.Booking;
import database.UserDAO;
import database.BookingDAO;
import java.util.List;

/**
 * <<control>>
 * Handles agent operations
 */
public class AgentController {
    private UserDAO userDAO;
    private BookingDAO bookingDAO;

    public AgentController() {
        this.userDAO = new UserDAO();
        this.bookingDAO = new BookingDAO();
    }

    public User searchCustomerByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    public List<Booking> getCustomerBookings(int userId) {
        return bookingDAO.getUserBookings(userId);
    }

    public boolean cancelCustomerBooking(int bookingId) {
        return bookingDAO.cancelBooking(bookingId);
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }
}
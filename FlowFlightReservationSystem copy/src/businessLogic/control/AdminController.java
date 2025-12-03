// ============================================
// AdminController.java
// ============================================
package businessLogic.control;

import businessLogic.entity.Flight;
import database.FlightDAO;
import database.UserDAO;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <<control>>
 * Handles admin operations
 */
public class AdminController {
    private FlightDAO flightDAO;
    private UserDAO userDAO;

    public AdminController() {
        this.flightDAO = new FlightDAO();
        this.userDAO = new UserDAO();
    }

    public int addFlight(String flightNumber, String departureCity, String arrivalCity,
                        LocalDateTime departureTime, LocalDateTime arrivalTime,
                        int totalSeats, double price) {
        return flightDAO.addFlight(flightNumber, departureCity, arrivalCity,
                departureTime, arrivalTime, totalSeats, price);
    }

    public boolean updateFlight(int flightId, String flightNumber, String departureCity,
                               String arrivalCity, LocalDateTime departureTime,
                               LocalDateTime arrivalTime, int totalSeats, double price) {
        return flightDAO.updateFlight(flightId, flightNumber, departureCity, arrivalCity,
                departureTime, arrivalTime, totalSeats, price);
    }

    public boolean deleteFlight(int flightId) {
        return flightDAO.deleteFlight(flightId);
    }

    public List<Flight> getAllFlights() {
        return flightDAO.getAllFlights();
    }

    public int getTotalUsers() {
        return userDAO.getTotalUserCount();
    }

    public int getTotalFlights() {
        return flightDAO.getTotalFlightCount();
    }
}

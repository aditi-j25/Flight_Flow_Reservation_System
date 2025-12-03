// ============================================
// FlightSearchController.java
// ============================================
package businessLogic.control;

import businessLogic.entity.Flight;
import database.FlightDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <<control>>
 * Handles flight search operations
 */
public class FlightSearchController {
    private FlightDAO flightDAO;

    public FlightSearchController() {
        this.flightDAO = new FlightDAO();
    }

    /**
     * Search flights with optional filters (date as LocalDate)
     */
    public List<Flight> searchFlights(String origin, String destination, LocalDate date) {
        String dateString = (date != null) ? date.toString() : null;
        return flightDAO.searchFlights(origin, destination, dateString, null);
    }

    /**
     * Search flights with optional filters (original method with String date)
     */
    public List<Flight> searchFlights(String origin, String destination, String date, String airline) {
        return flightDAO.searchFlights(origin, destination, date, airline);
    }

    /**
     * Get all flights
     */
    public List<Flight> getAllFlights() {
        return flightDAO.getAllFlights();
    }

    /**
     * Get specific flight by ID
     */
    public Flight getFlightById(int flightId) {
        return flightDAO.getFlightById(flightId);
    }

    /**
     * Get all unique origin cities
     */
    public List<String> getAllOrigins() {
        return flightDAO.getAllOrigins();
    }

    /**
     * Get all unique destination cities
     */
    public List<String> getAllDestinations() {
        return flightDAO.getAllDestinations();
    }

    /**
     * Get all unique airlines
     */
    public List<String> getAllAirlines() {
        return flightDAO.getAllAirlines();
    }

    /**
     * Get all unique cities (both origins and destinations combined)
     */
    public List<String> getAllCities() {
        List<String> cities = new ArrayList<>();
        cities.addAll(flightDAO.getAllOrigins());
        cities.addAll(flightDAO.getAllDestinations());
        
        // Remove duplicates and sort
        return cities.stream()
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
    }
}
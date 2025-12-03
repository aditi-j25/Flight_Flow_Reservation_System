// ============================================
// Flight.java
// ============================================
package businessLogic.entity;

import java.time.LocalDateTime;

/**
 * <<entity>>
 * Represents a flight in the system
 */
public class Flight {
    private int flightId;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String airline;
    private int availableSeats;
    private double price;
    private String aircraftType;
    private int totalSeats; // Total capacity

    public Flight(int flightId, String flightNumber, String origin, String destination,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, String airline,
                  int availableSeats, double price, String aircraftType) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.airline = airline;
        this.availableSeats = availableSeats;
        this.totalSeats = availableSeats; // Initially, total seats = available seats
        this.price = price;
        this.aircraftType = aircraftType;
    }

    public boolean hasAvailableSeats() {
        return availableSeats > 0;
    }

    // Getters
    public int getFlightId() { return flightId; }
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    
    // Alias methods for compatibility
    public String getDepartureCity() { return origin; }
    public String getArrivalCity() { return destination; }
    
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public String getAirline() { return airline; }
    public int getAvailableSeats() { return availableSeats; }
    public int getTotalSeats() { return totalSeats; }
    public double getPrice() { return price; }
    public String getAircraftType() { return aircraftType; }

    // Setters
    public void setFlightId(int flightId) { this.flightId = flightId; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setOrigin(String origin) { this.origin = origin; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setAirline(String airline) { this.airline = airline; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public void setPrice(double price) { this.price = price; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", " + origin + " → " + destination +
                ", airline='" + airline + '\'' +
                ", seats=" + availableSeats +
                ", $" + price +
                '}';
    }
}
// ============================================
// FlightDAO.java
// ============================================
package database;

import businessLogic.entity.Flight;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Flight operations
 * FIXED: Don't store connection, get it fresh each time
 */
public class FlightDAO {

    public FlightDAO() {
        initializeSampleData();
    }

    private void initializeSampleData() {
        try {
            // Get fresh connection
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String checkData = "SELECT COUNT(*) FROM flights";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkData);
            
            if (rs.next() && rs.getInt(1) == 0) {
                insertSampleFlights();
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error checking sample data: " + e.getMessage());
        }
    }

    private void insertSampleFlights() {
        String insertSQL = """
            INSERT INTO flights (flight_number, origin, destination, departure_time, 
                               arrival_time, airline, available_seats, price, aircraft_type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try {
            // Get fresh connection
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            
            Object[][] flights = {
                {"AC101", "Calgary", "Toronto", "2025-12-01T08:00", "2025-12-01T14:30", "Air Canada", 150, 350.00, "Boeing 737"},
                {"AC102", "Calgary", "Vancouver", "2025-12-01T10:00", "2025-12-01T11:30", "Air Canada", 120, 220.00, "Airbus A320"},
                {"WJ201", "Calgary", "Toronto", "2025-12-01T09:00", "2025-12-01T15:30", "WestJet", 180, 320.00, "Boeing 787"},
                {"WJ202", "Calgary", "Montreal", "2025-12-02T07:00", "2025-12-02T13:45", "WestJet", 160, 380.00, "Boeing 737"},
                {"AC103", "Toronto", "Calgary", "2025-12-01T16:00", "2025-12-01T18:30", "Air Canada", 140, 360.00, "Boeing 737"},
                {"AC104", "Vancouver", "Calgary", "2025-12-01T13:00", "2025-12-01T14:30", "Air Canada", 130, 230.00, "Airbus A320"},
                {"WJ203", "Calgary", "Edmonton", "2025-12-01T11:00", "2025-12-01T12:00", "WestJet", 90, 150.00, "Bombardier Q400"},
                {"AC105", "Calgary", "Ottawa", "2025-12-02T08:30", "2025-12-02T15:00", "Air Canada", 170, 420.00, "Boeing 787"},
                {"WJ204", "Toronto", "Vancouver", "2025-12-01T10:00", "2025-12-01T13:00", "WestJet", 200, 450.00, "Boeing 787"},
                {"AC106", "Montreal", "Calgary", "2025-12-02T09:00", "2025-12-02T11:30", "Air Canada", 145, 395.00, "Airbus A320"}
            };
            
            for (Object[] flight : flights) {
                pstmt.setString(1, (String) flight[0]);
                pstmt.setString(2, (String) flight[1]);
                pstmt.setString(3, (String) flight[2]);
                pstmt.setString(4, (String) flight[3]);
                pstmt.setString(5, (String) flight[4]);
                pstmt.setString(6, (String) flight[5]);
                pstmt.setInt(7, (Integer) flight[6]);
                pstmt.setDouble(8, (Double) flight[7]);
                pstmt.setString(9, (String) flight[8]);
                pstmt.executeUpdate();
            }
            
            pstmt.close();
            System.out.println("Sample flight data inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting sample flights: " + e.getMessage());
        }
    }

    public int addFlight(String flightNumber, String origin, String destination,
                        LocalDateTime departureTime, LocalDateTime arrivalTime,
                        int availableSeats, double price) {
        String insertSQL = """
            INSERT INTO flights (flight_number, origin, destination, departure_time, 
                               arrival_time, airline, available_seats, price, aircraft_type)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try {
            // Get fresh connection each time
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, flightNumber);
            pstmt.setString(2, origin);
            pstmt.setString(3, destination);
            pstmt.setString(4, departureTime.toString());
            pstmt.setString(5, arrivalTime.toString());
            pstmt.setString(6, "Unknown");
            pstmt.setInt(7, availableSeats);
            pstmt.setDouble(8, price);
            pstmt.setString(9, "Unknown");
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int result = generatedKeys.getInt(1);
                    generatedKeys.close();
                    pstmt.close();
                    return result;
                }
            }
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error adding flight: " + e.getMessage());
        }
        
        return -1;
    }

    public boolean updateFlight(int flightId, String flightNumber, String origin,
                               String destination, LocalDateTime departureTime,
                               LocalDateTime arrivalTime, int availableSeats, double price) {
        String updateSQL = """
            UPDATE flights SET flight_number = ?, origin = ?, destination = ?, 
                             departure_time = ?, arrival_time = ?, available_seats = ?, price = ?
            WHERE flight_id = ?
        """;
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            
            pstmt.setString(1, flightNumber);
            pstmt.setString(2, origin);
            pstmt.setString(3, destination);
            pstmt.setString(4, departureTime.toString());
            pstmt.setString(5, arrivalTime.toString());
            pstmt.setInt(6, availableSeats);
            pstmt.setDouble(7, price);
            pstmt.setInt(8, flightId);
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating flight: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFlight(int flightId) {
        String deleteSQL = "DELETE FROM flights WHERE flight_id = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(deleteSQL);
            pstmt.setInt(1, flightId);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting flight: " + e.getMessage());
            return false;
        }
    }

    public List<Flight> searchFlights(String origin, String destination, String date, String airline) {
        List<Flight> flights = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM flights WHERE 1=1");
        List<String> params = new ArrayList<>();
        
        if (origin != null && !origin.trim().isEmpty() && !origin.equalsIgnoreCase("All Cities")) {
            query.append(" AND LOWER(origin) = LOWER(?)");
            params.add(origin.trim());
        }
        
        if (destination != null && !destination.trim().isEmpty() && !destination.equalsIgnoreCase("All Cities")) {
            query.append(" AND LOWER(destination) = LOWER(?)");
            params.add(destination.trim());
        }
        
        if (date != null && !date.trim().isEmpty()) {
            query.append(" AND DATE(departure_time) = ?");
            params.add(date.trim());
        }
        
        if (airline != null && !airline.trim().isEmpty() && !airline.equalsIgnoreCase("All Airlines")) {
            query.append(" AND LOWER(airline) = LOWER(?)");
            params.add(airline.trim());
        }
        
        query.append(" ORDER BY departure_time");
        
        try {
            // Get fresh connection
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query.toString());
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                flights.add(extractFlightFromResultSet(rs));
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error searching flights: " + e.getMessage());
            e.printStackTrace();
        }
        
        return flights;
    }

    public Flight getFlightById(int flightId) {
        String query = "SELECT * FROM flights WHERE flight_id = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, flightId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Flight flight = extractFlightFromResultSet(rs);
                rs.close();
                pstmt.close();
                return flight;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting flight by ID: " + e.getMessage());
        }
        
        return null;
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM flights ORDER BY departure_time";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                flights.add(extractFlightFromResultSet(rs));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting all flights: " + e.getMessage());
        }
        
        return flights;
    }

    public List<String> getAllOrigins() {
        List<String> origins = new ArrayList<>();
        String query = "SELECT DISTINCT origin FROM flights ORDER BY origin";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                origins.add(rs.getString("origin"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting origins: " + e.getMessage());
        }
        
        return origins;
    }

    public List<String> getAllDestinations() {
        List<String> destinations = new ArrayList<>();
        String query = "SELECT DISTINCT destination FROM flights ORDER BY destination";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                destinations.add(rs.getString("destination"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting destinations: " + e.getMessage());
        }
        
        return destinations;
    }

    public List<String> getAllAirlines() {
        List<String> airlines = new ArrayList<>();
        String query = "SELECT DISTINCT airline FROM flights ORDER BY airline";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                airlines.add(rs.getString("airline"));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting airlines: " + e.getMessage());
        }
        
        return airlines;
    }

    public boolean updateAvailableSeats(int flightId, int newSeatCount) {
        String updateSQL = "UPDATE flights SET available_seats = ? WHERE flight_id = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setInt(1, newSeatCount);
            pstmt.setInt(2, flightId);
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating available seats: " + e.getMessage());
            return false;
        }
    }

    public boolean decrementAvailableSeats(int flightId) {
        String updateSQL = "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_id = ? AND available_seats > 0";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setInt(1, flightId);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error decrementing seats: " + e.getMessage());
            return false;
        }
    }

    public boolean incrementAvailableSeats(int flightId) {
        String updateSQL = "UPDATE flights SET available_seats = available_seats + 1 WHERE flight_id = ?";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(updateSQL);
            pstmt.setInt(1, flightId);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error incrementing seats: " + e.getMessage());
            return false;
        }
    }

    public int getTotalFlightCount() {
        String query = "SELECT COUNT(*) FROM flights";
        
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                stmt.close();
                return count;
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error counting flights: " + e.getMessage());
        }
        
        return 0;
    }

    private Flight extractFlightFromResultSet(ResultSet rs) throws SQLException {
        // FIXED: Handle both 'T' and space separators in datetime
        String departureStr = rs.getString("departure_time");
        String arrivalStr = rs.getString("arrival_time");
        
        LocalDateTime departureTime = LocalDateTime.parse(departureStr.replace(" ", "T"));
        LocalDateTime arrivalTime = LocalDateTime.parse(arrivalStr.replace(" ", "T"));
        
        return new Flight(
            rs.getInt("flight_id"),
            rs.getString("flight_number"),
            rs.getString("origin"),
            rs.getString("destination"),
            departureTime,
            arrivalTime,
            rs.getString("airline"),
            rs.getInt("available_seats"),
            rs.getDouble("price"),
            rs.getString("aircraft_type")
        );
    }
}
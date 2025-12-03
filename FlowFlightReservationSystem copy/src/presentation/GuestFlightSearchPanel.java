// ============================================
// GuestFlightSearchPanel.java
// ============================================
package presentation;

import businessLogic.control.FlightSearchController;
import businessLogic.entity.Flight;
import businessLogic.entity.Guest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <<boundary>>
 * Guest Flight Search Panel - Browse only, no booking
 */
public class GuestFlightSearchPanel extends JPanel {
    private FlightSearchController searchController;
    private Guest guest;

    private JComboBox<String> departureCityCombo;
    private JComboBox<String> arrivalCityCombo;
    private JComboBox<String> airlineCombo;
    private JTextField dateField;
    private JTable flightTable;
    private DefaultTableModel tableModel;
    private JButton searchButton;
    private JButton clearButton;

    public GuestFlightSearchPanel(Guest guest) {
        this.guest = guest;
        this.searchController = new FlightSearchController();
        initializeUI();
        loadFilters();
        loadAllFlights();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title with guest notice
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Browse Available Flights");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(85, 107, 47));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        JLabel noticeLabel = new JLabel("Login or Sign up to book flights");
        noticeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        noticeLabel.setForeground(new Color(204, 102, 0));
        noticeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        titlePanel.add(noticeLabel, BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
                "Search Criteria",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(85, 107, 47)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: From and To
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Arial", Font.BOLD, 13));
        searchPanel.add(fromLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        departureCityCombo = new JComboBox<>();
        departureCityCombo.setPreferredSize(new Dimension(200, 30));
        departureCityCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        searchPanel.add(departureCityCombo, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.BOLD, 13));
        searchPanel.add(toLabel, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        arrivalCityCombo = new JComboBox<>();
        arrivalCityCombo.setPreferredSize(new Dimension(200, 30));
        arrivalCityCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        searchPanel.add(arrivalCityCombo, gbc);

        // Row 2: Date and Airline
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 13));
        searchPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(200, 30));
        dateField.setFont(new Font("Arial", Font.PLAIN, 13));
        dateField.setToolTipText("Format: YYYY-MM-DD (leave empty for all dates)");
        searchPanel.add(dateField, gbc);

        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel airlineLabel = new JLabel("Airline:");
        airlineLabel.setFont(new Font("Arial", Font.BOLD, 13));
        searchPanel.add(airlineLabel, gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        airlineCombo = new JComboBox<>();
        airlineCombo.setPreferredSize(new Dimension(200, 30));
        airlineCombo.setFont(new Font("Arial", Font.PLAIN, 13));
        searchPanel.add(airlineCombo, gbc);

        // Row 3: Buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        searchButton = new JButton("Search Flights");
        searchButton.setBackground(new Color(107, 142, 35));
        searchButton.setForeground(new Color(85, 107, 47));
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setFocusPainted(false);
        searchButton.setPreferredSize(new Dimension(150, 35));
        searchButton.addActionListener(e -> searchFlights());
        buttonPanel.add(searchButton);

        clearButton = new JButton("Show All");
        clearButton.setBackground(new Color(143, 151, 121));
        clearButton.setForeground(new Color(85, 107, 47));
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearButton.setFocusPainted(false);
        clearButton.setPreferredSize(new Dimension(150, 35));
        clearButton.addActionListener(e -> {
            clearFilters();
            loadAllFlights();
        });
        buttonPanel.add(clearButton);

        searchPanel.add(buttonPanel, gbc);

        // Create center panel to hold search panel
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // Flight Table
        String[] columns = {"Flight #", "Airline", "From", "To", "Departure", "Arrival", "Aircraft", "Seats", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        flightTable = new JTable(tableModel);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightTable.setRowHeight(30);
        flightTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        flightTable.getTableHeader().setBackground(new Color(143, 151, 121));
        flightTable.getTableHeader().setForeground(Color.WHITE);
        flightTable.setFont(new Font("Arial", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(flightTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(143, 151, 121), 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadFilters() {
        List<String> cities = searchController.getAllCities();
        departureCityCombo.removeAllItems();
        arrivalCityCombo.removeAllItems();
        departureCityCombo.addItem("All Cities");
        arrivalCityCombo.addItem("All Cities");
        for (String city : cities) {
            departureCityCombo.addItem(city);
            arrivalCityCombo.addItem(city);
        }

        List<String> airlines = searchController.getAllAirlines();
        airlineCombo.removeAllItems();
        airlineCombo.addItem("All Airlines");
        for (String airline : airlines) {
            airlineCombo.addItem(airline);
        }
    }

    private void clearFilters() {
        departureCityCombo.setSelectedIndex(0);
        arrivalCityCombo.setSelectedIndex(0);
        airlineCombo.setSelectedIndex(0);
        dateField.setText("");
    }

    private void loadAllFlights() {
        List<Flight> flights = searchController.getAllFlights();
        displayFlights(flights);
    }

    private void searchFlights() {
        String departureCity = (String) departureCityCombo.getSelectedItem();
        String arrivalCity = (String) arrivalCityCombo.getSelectedItem();
        String airline = (String) airlineCombo.getSelectedItem();
        String dateText = dateField.getText().trim();

        if (departureCity != null && departureCity.equals("All Cities")) departureCity = null;
        if (arrivalCity != null && arrivalCity.equals("All Cities")) arrivalCity = null;
        if (airline != null && airline.equals("All Airlines")) airline = null;
        if (dateText.isEmpty()) dateText = null;

        if (dateText != null) {
            try {
                LocalDate.parse(dateText);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use YYYY-MM-DD",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        List<Flight> flights = searchController.searchFlights(departureCity, arrivalCity, dateText, airline);
        displayFlights(flights);

        if (flights.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No flights found matching your criteria.\nTry different search parameters.",
                "No Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayFlights(List<Flight> flights) {
        tableModel.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm");

        for (Flight flight : flights) {
            Object[] row = {
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime().format(formatter),
                flight.getArrivalTime().format(formatter),
                flight.getAircraftType(),
                flight.getAvailableSeats(),
                String.format("$%.2f", flight.getPrice())
            };
            tableModel.addRow(row);
        }
    }
}
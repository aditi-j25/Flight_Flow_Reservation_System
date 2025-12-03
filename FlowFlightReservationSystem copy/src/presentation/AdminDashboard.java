// ============================================
// AdminDashboard.java
// ============================================
package presentation;

import businessLogic.entity.Admin;
import businessLogic.entity.Flight;
import businessLogic.entity.Booking;
import businessLogic.control.AuthenticationController;
import businessLogic.control.AdminController;
import businessLogic.control.BookingController;
import businessLogic.control.FlightSearchController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <<boundary>>
 * Complete Admin Dashboard with Flight Management
 */
public class AdminDashboard extends JFrame {
    private static final Color SAGE_GREEN = new Color(85, 107, 47);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    
    private Admin admin;
    private AdminController adminController;
    private BookingController bookingController;
    private FlightSearchController flightSearchController;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        this.adminController = new AdminController();
        this.bookingController = new BookingController();
        this.flightSearchController = new FlightSearchController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Flight Reservation System - Admin Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(107, 142, 35));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel welcomeLabel = new JLabel("Administrator: " + admin.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        topBar.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(85, 107, 47));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutButton.addActionListener(e -> logout());
        topBar.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Navigation
        JPanel navPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        navPanel.setBackground(new Color(143, 151, 121));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navPanel.setPreferredSize(new Dimension(200, 0));

        JButton dashboardBtn = createNavButton("Dashboard");
        JButton manageFlightsBtn = createNavButton("Manage Flights");
        JButton viewBookingsBtn = createNavButton("View Bookings");
        JButton scheduleBtn = createNavButton("Flight Schedule");

        navPanel.add(dashboardBtn);
        navPanel.add(manageFlightsBtn);
        navPanel.add(viewBookingsBtn);
        navPanel.add(scheduleBtn);

        mainPanel.add(navPanel, BorderLayout.WEST);

        // Content
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createDashboardPanel(), "DASHBOARD");
        cardPanel.add(createManageFlightsPanel(), "FLIGHTS");
        cardPanel.add(createViewBookingsPanel(), "BOOKINGS");
        cardPanel.add(createReportsPanel(), "REPORTS");

        dashboardBtn.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));
        manageFlightsBtn.addActionListener(e -> {
            cardPanel.remove(1);
            cardPanel.add(createManageFlightsPanel(), "FLIGHTS", 1);
            cardLayout.show(cardPanel, "FLIGHTS");
        });
        viewBookingsBtn.addActionListener(e -> {
            cardPanel.remove(2);
            cardPanel.add(createViewBookingsPanel(), "BOOKINGS", 2);
            cardLayout.show(cardPanel, "BOOKINGS");
        });
        scheduleBtn.addActionListener(e -> {
            cardPanel.remove(3);
            cardPanel.add(createReportsPanel(), "REPORTS", 3);
            cardLayout.show(cardPanel, "REPORTS");
        });

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        add(mainPanel);
        cardLayout.show(cardPanel, "DASHBOARD");
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(107, 142, 35));
        button.setForeground(SAGE_GREEN);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(128, 170, 42));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(107, 142, 35));
            }
        });

        return button;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel titleLabel = new JLabel("System Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(SAGE_GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel usersLabel = new JLabel("Total Users: " + adminController.getTotalUsers());
        usersLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(usersLabel, gbc);

        gbc.gridy = 2;
        JLabel flightsLabel = new JLabel("Total Flights: " + adminController.getTotalFlights());
        flightsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(flightsLabel, gbc);

        gbc.gridy = 3;
        List<Booking> allBookings = bookingController.getAllBookings();
        JLabel bookingsLabel = new JLabel("Total Bookings: " + allBookings.size());
        bookingsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(bookingsLabel, gbc);

        return panel;
    }

    private JPanel createManageFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Manage Flights");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SAGE_GREEN);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Flights Table
        String[] columns = {"ID", "Flight #", "Airline", "From", "To", "Departure", "Arrival", "Seats", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable flightTable = new JTable(tableModel);
        flightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flightTable.setRowHeight(30);
        flightTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        flightTable.getTableHeader().setBackground(new Color(143, 151, 121));
        flightTable.getTableHeader().setForeground(Color.WHITE);

        // Load flights
        List<Flight> flights = flightSearchController.getAllFlights();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm");
        for (Flight flight : flights) {
            Object[] row = {
                flight.getFlightId(),
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime().format(formatter),
                flight.getArrivalTime().format(formatter),
                flight.getAvailableSeats(),
                String.format("$%.2f", flight.getPrice())
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(flightTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(143, 151, 121), 2));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = new JButton("Add Flight");
        styleButton(addButton, new Color(107, 142, 35));
        addButton.addActionListener(e -> showAddFlightDialog());
        buttonPanel.add(addButton);

        JButton editButton = new JButton("Edit Flight");
        styleButton(editButton, new Color(107, 142, 35));
        editButton.addActionListener(e -> {
            int selectedRow = flightTable.getSelectedRow();
            if (selectedRow >= 0) {
                int flightId = (int) tableModel.getValueAt(selectedRow, 0);
                Flight flight = flightSearchController.getFlightById(flightId);
                showEditFlightDialog(flight);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a flight to edit");
            }
        });
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete Flight");
        styleButton(deleteButton, new Color(107, 142, 35));
        deleteButton.addActionListener(e -> {
            int selectedRow = flightTable.getSelectedRow();
            if (selectedRow >= 0) {
                int flightId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this flight?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (adminController.deleteFlight(flightId)) {
                        JOptionPane.showMessageDialog(this, "Flight deleted successfully");
                        cardLayout.show(cardPanel, "DASHBOARD");
                        cardPanel.remove(1);
                        cardPanel.add(createManageFlightsPanel(), "FLIGHTS", 1);
                        cardLayout.show(cardPanel, "FLIGHTS");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete flight");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a flight to delete");
            }
        });
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showAddFlightDialog() {
        JDialog dialog = new JDialog(this, "Add New Flight", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField flightNumberField = new JTextField(15);
        JTextField originField = new JTextField(15);
        JTextField destinationField = new JTextField(15);
        JTextField departureField = new JTextField(15);
        JTextField arrivalField = new JTextField(15);
        JTextField seatsField = new JTextField(15);
        JTextField priceField = new JTextField(15);

        int row = 0;
        addFormField(dialog, gbc, row++, "Flight Number:", flightNumberField);
        addFormField(dialog, gbc, row++, "Origin:", originField);
        addFormField(dialog, gbc, row++, "Destination:", destinationField);
        addFormField(dialog, gbc, row++, "Departure (yyyy-MM-ddTHH:mm):", departureField);
        addFormField(dialog, gbc, row++, "Arrival (yyyy-MM-ddTHH:mm):", arrivalField);
        addFormField(dialog, gbc, row++, "Total Seats:", seatsField);
        addFormField(dialog, gbc, row++, "Price:", priceField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Add Flight");
        styleButton(saveButton, new Color(107, 142, 35));
        saveButton.addActionListener(e -> {
            try {
                int flightId = adminController.addFlight(
                    flightNumberField.getText(),
                    originField.getText(),
                    destinationField.getText(),
                    LocalDateTime.parse(departureField.getText(), DATE_TIME_FORMATTER),
                    LocalDateTime.parse(arrivalField.getText(), DATE_TIME_FORMATTER),
                    Integer.parseInt(seatsField.getText()),
                    Double.parseDouble(priceField.getText())
                );
                if (flightId > 0) {
                    JOptionPane.showMessageDialog(dialog, "Flight added successfully!");
                    dialog.dispose();
                    cardPanel.remove(1);
                    cardPanel.add(createManageFlightsPanel(), "FLIGHTS", 1);
                    cardLayout.show(cardPanel, "FLIGHTS");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add flight");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage());
            }
        });
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(128, 128, 128));
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }

    private void showEditFlightDialog(Flight flight) {
        JDialog dialog = new JDialog(this, "Edit Flight", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField flightNumberField = new JTextField(flight.getFlightNumber(), 15);
        JTextField originField = new JTextField(flight.getOrigin(), 15);
        JTextField destinationField = new JTextField(flight.getDestination(), 15);
        JTextField departureField = new JTextField(flight.getDepartureTime().format(DATE_TIME_FORMATTER), 15);
        JTextField arrivalField = new JTextField(flight.getArrivalTime().format(DATE_TIME_FORMATTER), 15);
        JTextField seatsField = new JTextField(String.valueOf(flight.getAvailableSeats()), 15);
        JTextField priceField = new JTextField(String.valueOf(flight.getPrice()), 15);

        int row = 0;
        addFormField(dialog, gbc, row++, "Flight Number:", flightNumberField);
        addFormField(dialog, gbc, row++, "Origin:", originField);
        addFormField(dialog, gbc, row++, "Destination:", destinationField);
        addFormField(dialog, gbc, row++, "Departure (yyyy-MM-ddTHH:mm):", departureField);
        addFormField(dialog, gbc, row++, "Arrival (yyyy-MM-ddTHH:mm):", arrivalField);
        addFormField(dialog, gbc, row++, "Available Seats:", seatsField);
        addFormField(dialog, gbc, row++, "Price:", priceField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save Changes");
        styleButton(saveButton, new Color(107, 142, 35));
        saveButton.addActionListener(e -> {
            try {
                boolean success = adminController.updateFlight(
                    flight.getFlightId(),
                    flightNumberField.getText(),
                    originField.getText(),
                    destinationField.getText(),
                    LocalDateTime.parse(departureField.getText(), DATE_TIME_FORMATTER),
                    LocalDateTime.parse(arrivalField.getText(), DATE_TIME_FORMATTER),
                    Integer.parseInt(seatsField.getText()),
                    Double.parseDouble(priceField.getText())
                );
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Flight updated successfully!");
                    dialog.dispose();
                    cardPanel.remove(1);
                    cardPanel.add(createManageFlightsPanel(), "FLIGHTS", 1);
                    cardLayout.show(cardPanel, "FLIGHTS");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update flight");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage());
            }
        });
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(128, 128, 128));
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }

    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("All Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SAGE_GREEN);
        panel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Booking ID", "Customer", "Email", "Flight", "Route", "Seat", "Date", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable bookingTable = new JTable(tableModel);
        bookingTable.setRowHeight(30);
        bookingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        bookingTable.getTableHeader().setBackground(new Color(143, 151, 121));
        bookingTable.getTableHeader().setForeground(Color.WHITE);

        List<Booking> bookings = bookingController.getAllBookings();
        for (Booking booking : bookings) {
            Object[] row = {
                booking.getBookingId(),
                booking.getCustomerName(),
                booking.getCustomerEmail(),
                booking.getFlightNumber(),
                booking.getRoute(),
                booking.getSeatNumber(),
                booking.getBookingDate().format(DISPLAY_FORMATTER),
                booking.getStatus()
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Flight Schedule");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SAGE_GREEN);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Schedule Table
        String[] columns = {"Flight #", "Airline", "Route", "Departure", "Arrival", "Aircraft", "Available Seats"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable scheduleTable = new JTable(tableModel);
        scheduleTable.setRowHeight(30);
        scheduleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        scheduleTable.getTableHeader().setBackground(new Color(143, 151, 121));
        scheduleTable.getTableHeader().setForeground(Color.WHITE);
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 12));

        // Load flight schedule
        List<Flight> flights = flightSearchController.getAllFlights();
        for (Flight flight : flights) {
            Object[] row = {
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getOrigin() + " → " + flight.getDestination(),
                flight.getDepartureTime().format(DISPLAY_FORMATTER),
                flight.getArrivalTime().format(DISPLAY_FORMATTER),
                flight.getAircraftType(),
                flight.getAvailableSeats() + " / " + flight.getTotalSeats()
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(143, 151, 121), 2));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel infoLabel = new JLabel(String.format("Total Flights: %d  |  Showing all scheduled flights", flights.size()));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);
        
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addFormField(JDialog dialog, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        dialog.add(lbl, gbc);

        gbc.gridx = 1;
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        dialog.add(field, gbc);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(bgColor.equals(new Color(178, 34, 34)) ? Color.WHITE : SAGE_GREEN);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void logout() {
        AuthenticationController.getInstance().logout();
        dispose();
        new HomePage().setVisible(true);
    }
}
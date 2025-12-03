// ============================================
// AgentDashboard.java - COMPLETE with Customer & Booking Management
// ============================================
package presentation;

import businessLogic.entity.Agent;
import businessLogic.entity.User;
import businessLogic.entity.Booking;
import businessLogic.control.AuthenticationController;
import businessLogic.control.AgentController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <<boundary>>
 * Complete Agent Dashboard with Customer and Booking Management
 */
public class AgentDashboard extends JFrame {
    private static final Color SAGE_GREEN = new Color(85, 107, 47);
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    
    private Agent agent;
    private AgentController agentController;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public AgentDashboard(Agent agent) {
        this.agent = agent;
        this.agentController = new AgentController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Flight Reservation System - Agent Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 225));

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(107, 142, 35));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel welcomeLabel = new JLabel("Flight Agent: " + agent.getFullName());
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
        navPanel.setPreferredSize(new Dimension(220, 0));

        JButton dashboardBtn = createNavButton("Dashboard");
        JButton searchCustomerBtn = createNavButton("Search Customer");
        JButton viewScheduleBtn = createNavButton("View Schedule");
        JButton viewBookingsBtn = createNavButton("View All Bookings");

        navPanel.add(dashboardBtn);
        navPanel.add(searchCustomerBtn);
        navPanel.add(viewScheduleBtn);
        navPanel.add(viewBookingsBtn);

        mainPanel.add(navPanel, BorderLayout.WEST);

        // Content Area
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        cardPanel.add(createDashboardPanel(), "DASHBOARD");
        cardPanel.add(createSearchCustomerPanel(), "SEARCH");
        cardPanel.add(createManageCustomersPanel(), "SCHEDULE");
        cardPanel.add(createViewAllBookingsPanel(), "BOOKINGS");

        dashboardBtn.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));
        searchCustomerBtn.addActionListener(e -> cardLayout.show(cardPanel, "SEARCH"));
        viewScheduleBtn.addActionListener(e -> {
            cardPanel.remove(2);
            cardPanel.add(createManageCustomersPanel(), "SCHEDULE", 2);
            cardLayout.show(cardPanel, "SCHEDULE");
        });
        viewBookingsBtn.addActionListener(e -> {
            cardPanel.remove(3);
            cardPanel.add(createViewAllBookingsPanel(), "BOOKINGS", 3);
            cardLayout.show(cardPanel, "BOOKINGS");
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

        JLabel titleLabel = new JLabel("Agent Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(SAGE_GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        List<Booking> allBookings = agentController.getAllBookings();
        JLabel bookingsLabel = new JLabel("Total Bookings in System: " + allBookings.size());
        bookingsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(bookingsLabel, gbc);

        gbc.gridy = 2;
        long confirmedCount = allBookings.stream().filter(b -> "CONFIRMED".equals(b.getStatus())).count();
        JLabel confirmedLabel = new JLabel("Confirmed Bookings: " + confirmedCount);
        confirmedLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(confirmedLabel, gbc);

        gbc.gridy = 3;
        long cancelledCount = allBookings.stream().filter(b -> "CANCELLED".equals(b.getStatus())).count();
        JLabel cancelledLabel = new JLabel("Cancelled Bookings: " + cancelledCount);
        cancelledLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(cancelledLabel, gbc);

        return panel;
    }

    private JPanel createSearchCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Search Customer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SAGE_GREEN);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
                "Search by Email",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                SAGE_GREEN
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel emailLabel = new JLabel("Customer Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 13));
        searchPanel.add(emailLabel);

        JTextField emailField = new JTextField(25);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(emailField);

        JButton searchButton = new JButton("Search");
        styleButton(searchButton, new Color(107, 142, 35));
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout(10, 10));
        resultsPanel.setBackground(Color.WHITE);

        // Customer Info Area
        JTextArea customerInfoArea = new JTextArea(5, 40);
        customerInfoArea.setEditable(false);
        customerInfoArea.setFont(new Font("Arial", Font.PLAIN, 13));
        customerInfoArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Customer Information"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        resultsPanel.add(new JScrollPane(customerInfoArea), BorderLayout.NORTH);

        // Bookings Table
        String[] columns = {"Booking ID", "Flight", "Route", "Seat", "Date", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable bookingsTable = new JTable(tableModel);
        bookingsTable.setRowHeight(30);
        bookingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        bookingsTable.getTableHeader().setBackground(new Color(143, 151, 121));
        bookingsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Customer Bookings"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Store current customer reference
        final User[] currentCustomer = {null};

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton cancelBookingButton = new JButton("Cancel Selected Booking");
        styleButton(cancelBookingButton, new Color(107, 142, 35));
        cancelBookingButton.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                String status = (String) tableModel.getValueAt(selectedRow, 5);
                
                if ("CANCELLED".equals(status)) {
                    JOptionPane.showMessageDialog(this, "This booking is already cancelled");
                    return;
                }
                
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel this booking?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (agentController.cancelCustomerBooking(bookingId)) {
                        JOptionPane.showMessageDialog(this, "Booking cancelled successfully");
                        // Refresh the search
                        searchButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel booking");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to cancel");
            }
        });
        actionPanel.add(cancelBookingButton);

        JButton editCustomerButton = new JButton("Edit Customer Info");
        styleButton(editCustomerButton, new Color(107, 142, 35));
        editCustomerButton.addActionListener(e -> {
            if (currentCustomer[0] != null) {
                showEditCustomerDialog(currentCustomer[0]);
                // Refresh the search after editing
                searchButton.doClick();
            } else {
                JOptionPane.showMessageDialog(this, "Please search for a customer first");
            }
        });
        actionPanel.add(editCustomerButton);

        resultsPanel.add(actionPanel, BorderLayout.SOUTH);

        panel.add(resultsPanel, BorderLayout.CENTER);

        // Search Button Action
        searchButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a customer email");
                return;
            }

            User customer = agentController.searchCustomerByEmail(email);
            if (customer == null) {
                customerInfoArea.setText("No customer found with email: " + email);
                tableModel.setRowCount(0);
                currentCustomer[0] = null;
                return;
            }

            // Store current customer
            currentCustomer[0] = customer;

            // Display customer info
            String info = String.format(
                "Customer ID: %d\n" +
                "Name: %s %s\n" +
                "Email: %s\n" +
                "Phone: %s\n" +
                "Address: %s\n" +
                "Role: %s",
                customer.getUserId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getRole()
            );
            customerInfoArea.setText(info);

            // Load customer bookings
            tableModel.setRowCount(0);
            List<Booking> bookings = agentController.getCustomerBookings(customer.getUserId());
            for (Booking booking : bookings) {
                Object[] row = {
                    booking.getBookingId(),
                    booking.getFlightNumber(),
                    booking.getRoute(),
                    booking.getSeatNumber(),
                    booking.getBookingDate().format(DISPLAY_FORMATTER),
                    booking.getStatus()
                };
                tableModel.addRow(row);
            }

            if (bookings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "This customer has no bookings");
            }
        });

        return panel;
    }

    private JPanel createManageCustomersPanel() {
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
        businessLogic.control.FlightSearchController flightController = new businessLogic.control.FlightSearchController();
        List<businessLogic.entity.Flight> flights = flightController.getAllFlights();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        
        for (businessLogic.entity.Flight flight : flights) {
            Object[] row = {
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getOrigin() + " → " + flight.getDestination(),
                flight.getDepartureTime().format(formatter),
                flight.getArrivalTime().format(formatter),
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
        
        JLabel infoLabel = new JLabel(String.format("Total Flights: %d  |  View all scheduled flights", flights.size()));
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel);
        
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showEditCustomerDialog(User customer) {
        JDialog dialog = new JDialog(this, "Edit Customer - " + customer.getEmail(), true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create email field and make it read-only
        JTextField emailField = new JTextField(customer.getEmail(), 20);
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        
        JTextField firstNameField = new JTextField(customer.getFirstName(), 20);
        JTextField lastNameField = new JTextField(customer.getLastName(), 20);
        JTextField addressField = new JTextField(customer.getAddress(), 20);
        JTextField phoneField = new JTextField(customer.getPhone(), 20);

        int row = 0;
        addFormField(dialog, gbc, row++, "Email (read-only):", emailField);
        addFormField(dialog, gbc, row++, "First Name:", firstNameField);
        addFormField(dialog, gbc, row++, "Last Name:", lastNameField);
        addFormField(dialog, gbc, row++, "Address:", addressField);
        addFormField(dialog, gbc, row++, "Phone:", phoneField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save Changes");
        styleButton(saveButton, new Color(107, 142, 35));
        saveButton.addActionListener(e -> {
            // Update customer in database
            database.UserDAO userDAO = new database.UserDAO();
            boolean success = userDAO.updateUserProfile(
                customer.getUserId(),
                firstNameField.getText(),
                lastNameField.getText(),
                addressField.getText(),
                phoneField.getText()
            );
            
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Customer updated successfully!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update customer");
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

    private JPanel createViewAllBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("All System Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SAGE_GREEN);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Bookings Table
        String[] columns = {"Booking ID", "Customer", "Email", "Flight", "Route", "Seat", "Date", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable bookingsTable = new JTable(tableModel);
        bookingsTable.setRowHeight(30);
        bookingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        bookingsTable.getTableHeader().setBackground(new Color(143, 151, 121));
        bookingsTable.getTableHeader().setForeground(Color.WHITE);

        // Load all bookings
        List<Booking> bookings = agentController.getAllBookings();
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

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(143, 151, 121), 2));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewDetailsButton = new JButton("View Details");
        styleButton(viewDetailsButton, new Color(107, 142, 35));
        viewDetailsButton.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                String customer = (String) tableModel.getValueAt(selectedRow, 1);
                String email = (String) tableModel.getValueAt(selectedRow, 2);
                String flight = (String) tableModel.getValueAt(selectedRow, 3);
                String route = (String) tableModel.getValueAt(selectedRow, 4);
                String seat = (String) tableModel.getValueAt(selectedRow, 5);
                String date = (String) tableModel.getValueAt(selectedRow, 6);
                String status = (String) tableModel.getValueAt(selectedRow, 7);

                String details = String.format(
                    "Booking Details\n\n" +
                    "Booking ID: #%d\n" +
                    "Customer: %s\n" +
                    "Email: %s\n" +
                    "Flight: %s\n" +
                    "Route: %s\n" +
                    "Seat: %s\n" +
                    "Date: %s\n" +
                    "Status: %s",
                    bookingId, customer, email, flight, route, seat, date, status
                );

                JOptionPane.showMessageDialog(this, details, "Booking Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to view");
            }
        });
        buttonPanel.add(viewDetailsButton);

        JButton cancelButton = new JButton("Cancel Booking");
        styleButton(cancelButton, new Color(107, 142, 35));
        cancelButton.addActionListener(e -> {
            int selectedRow = bookingsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                String status = (String) tableModel.getValueAt(selectedRow, 7);

                if ("CANCELLED".equals(status)) {
                    JOptionPane.showMessageDialog(this, "This booking is already cancelled");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to cancel this booking?",
                    "Confirm Cancellation",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (agentController.cancelCustomerBooking(bookingId)) {
                        JOptionPane.showMessageDialog(this, "Booking cancelled successfully");
                        // Refresh
                        cardPanel.remove(2);
                        cardPanel.add(createViewAllBookingsPanel(), "BOOKINGS", 2);
                        cardLayout.show(cardPanel, "BOOKINGS");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to cancel booking");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to cancel");
            }
        });
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(bgColor.equals(new Color(178, 34, 34)) ? Color.WHITE : SAGE_GREEN);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void logout() {
        AuthenticationController.getInstance().logout();
        dispose();
        new HomePage().setVisible(true);
    }
}
// ============================================
// BookingPanel.java
// ============================================
package presentation;

import businessLogic.control.BookingController;
import businessLogic.control.PaymentController;
import businessLogic.entity.Booking;
import businessLogic.entity.Customer;
import businessLogic.entity.Payment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <<boundary>>
 * Bookings Panel - View and manage customer bookings
 */
public class BookingPanel extends JPanel {
    private Customer customer;
    private BookingController bookingController;
    private PaymentController paymentController;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private JButton viewDetailsButton;
    private JButton cancelBookingButton;
    private JButton refreshButton;

    public BookingPanel(Customer customer) {
        this.customer = customer;
        this.bookingController = new BookingController();
        this.paymentController = new PaymentController();
        initializeUI();
        loadBookings();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(85, 107, 47));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(107, 142, 35));
        refreshButton.setForeground(new Color(85, 107, 47));
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadBookings());
        titlePanel.add(refreshButton, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Booking ID", "Flight #", "Route", "Seat", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        bookingsTable = new JTable(tableModel);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingsTable.setRowHeight(35);
        bookingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        bookingsTable.getTableHeader().setBackground(new Color(143, 151, 121));
        bookingsTable.getTableHeader().setForeground(Color.WHITE);
        bookingsTable.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(143, 151, 121), 2));
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setBackground(new Color(107, 142, 35));
        viewDetailsButton.setForeground(new Color(85, 107, 47));
        viewDetailsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setPreferredSize(new Dimension(150, 40));
        viewDetailsButton.addActionListener(e -> viewBookingDetails());
        buttonPanel.add(viewDetailsButton);

        cancelBookingButton = new JButton("Cancel Booking");
        cancelBookingButton.setBackground(new Color(178, 34, 34));
        cancelBookingButton.setForeground(new Color(85, 107, 47));
        cancelBookingButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBookingButton.setFocusPainted(false);
        cancelBookingButton.setPreferredSize(new Dimension(150, 40));
        cancelBookingButton.addActionListener(e -> cancelBooking());
        buttonPanel.add(cancelBookingButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingController.getUserBookings(customer.getUserId());

        if (bookings.isEmpty()) {
            // Show empty state
            tableModel.addRow(new Object[]{
                "No bookings found", "", "", "", "", ""
            });
            viewDetailsButton.setEnabled(false);
            cancelBookingButton.setEnabled(false);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            
            for (Booking booking : bookings) {
                Object[] row = {
                    booking.getBookingId(),
                    booking.getFlightNumber(),
                    booking.getRoute(),
                    booking.getSeatNumber(),
                    booking.getBookingDate().format(formatter),
                    booking.getStatus()
                };
                tableModel.addRow(row);
            }
            viewDetailsButton.setEnabled(true);
            cancelBookingButton.setEnabled(true);
        }
    }

    private void viewBookingDetails() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a booking to view details",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        Booking booking = bookingController.getBookingById(bookingId);

        if (booking == null) {
            JOptionPane.showMessageDialog(this,
                "Booking not found",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get payment info
        Payment payment = paymentController.getPaymentByBookingId(bookingId);

        // Create details panel
        JPanel detailsPanel = createBookingDetailsPanel(booking, payment);

        JOptionPane.showMessageDialog(this,
            detailsPanel,
            "Booking Details - #" + bookingId,
            JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createBookingDetailsPanel(Booking booking, Payment payment) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

        int row = 0;

        // Booking Information Header
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        JLabel bookingHeader = new JLabel("✈ Booking Information");
        bookingHeader.setFont(new Font("Arial", Font.BOLD, 16));
        bookingHeader.setForeground(new Color(85, 107, 47));
        panel.add(bookingHeader, gbc);

        gbc.gridwidth = 1;

        // Booking ID
        gbc.gridy = row++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Booking ID:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel("#" + booking.getBookingId()), gbc);

        // Flight Number
        gbc.gridy = row++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Flight Number:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(booking.getFlightNumber()), gbc);

        // Route
        gbc.gridy = row++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Route:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(booking.getRoute()), gbc);

        // Seat Number
        gbc.gridy = row++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Seat Number:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(booking.getSeatNumber()), gbc);

        // Booking Date
        gbc.gridy = row++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Booking Date:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(booking.getBookingDate().format(formatter)), gbc);

        // Status
        gbc.gridy = row++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Status:"), gbc);
        gbc.gridx = 1;
        JLabel statusLabel = createLabel(booking.getStatus());
        if ("CONFIRMED".equals(booking.getStatus())) {
            statusLabel.setForeground(new Color(34, 139, 34));
        } else if ("CANCELLED".equals(booking.getStatus())) {
            statusLabel.setForeground(new Color(178, 34, 34));
        }
        panel.add(statusLabel, gbc);

        // Payment Information
        if (payment != null) {
            gbc.gridy = row++;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(20, 10, 8, 10);
            JLabel paymentHeader = new JLabel("💳 Payment Information");
            paymentHeader.setFont(new Font("Arial", Font.BOLD, 16));
            paymentHeader.setForeground(new Color(85, 107, 47));
            panel.add(paymentHeader, gbc);

            gbc.gridwidth = 1;
            gbc.insets = new Insets(8, 10, 8, 10);

            // Amount
            gbc.gridy = row++;
            gbc.gridx = 0;
            panel.add(createBoldLabel("Amount Paid:"), gbc);
            gbc.gridx = 1;
            JLabel amountLabel = createLabel(String.format("$%.2f", payment.getAmount()));
            amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(amountLabel, gbc);

            // Payment Method
            gbc.gridy = row++;
            gbc.gridx = 0;
            panel.add(createBoldLabel("Payment Method:"), gbc);
            gbc.gridx = 1;
            panel.add(createLabel(payment.getPaymentMethod()), gbc);

            // Card
            gbc.gridy = row++;
            gbc.gridx = 0;
            panel.add(createBoldLabel("Card:"), gbc);
            gbc.gridx = 1;
            panel.add(createLabel("**** " + payment.getCardNumber()), gbc);

            // Payment Date
            gbc.gridy = row++;
            gbc.gridx = 0;
            panel.add(createBoldLabel("Payment Date:"), gbc);
            gbc.gridx = 1;
            panel.add(createLabel(payment.getPaymentDate().format(formatter)), gbc);
        }

        panel.setPreferredSize(new Dimension(450, 400));
        return panel;
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        return label;
    }

    private void cancelBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a booking to cancel",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 5);

        if ("CANCELLED".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This booking is already cancelled",
                "Already Cancelled",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Show cancellation policy
        String message = "Are you sure you want to cancel this booking?\n\n" +
                        "Cancellation Policy:\n" +
                        "• Full refund if cancelled 24 hours before departure\n" +
                        "• 50% refund if cancelled within 24 hours\n" +
                        "• No refund for no-shows\n\n" +
                        "Booking ID: #" + bookingId;

        int confirm = JOptionPane.showConfirmDialog(this,
            message,
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bookingController.cancelBooking(bookingId);

            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Booking cancelled successfully!\n" +
                    "A confirmation email has been sent to " + customer.getEmail(),
                    "Cancellation Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                loadBookings(); // Refresh the list
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to cancel booking. Please try again or contact support.",
                    "Cancellation Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
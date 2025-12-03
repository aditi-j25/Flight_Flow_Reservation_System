// ============================================
// BookingHistoryPanel.java 
// ============================================
package presentation;

import businessLogic.entity.Customer;
import businessLogic.entity.Booking;
import businessLogic.control.BookingController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <<boundary>>
 * Booking History Panel - View customer bookings
 */
public class BookingHistoryPanel extends JPanel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    
    private final Customer customer;
    private final BookingController bookingController;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JButton cancelButton;
    private JButton refreshButton;
    private JLabel noBookingsLabel;

    public BookingHistoryPanel(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        this.customer = customer;
        this.bookingController = new BookingController();
        initializeUI();
        loadBookings();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(85, 107, 47));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Booking ID", "Flight #", "Route", "Date", "Seat", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingTable = new JTable(tableModel);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.setRowHeight(35);
        bookingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        bookingTable.getTableHeader().setBackground(new Color(143, 151, 121));
        bookingTable.getTableHeader().setForeground(Color.WHITE);
        bookingTable.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(143, 151, 121), 2));
        
        // Create a container panel to hold both the scroll pane and the no bookings label
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Initialize and add the no bookings label
        noBookingsLabel = new JLabel("No bookings found. Start booking flights!");
        noBookingsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        noBookingsLabel.setForeground(Color.GRAY);
        noBookingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noBookingsLabel.setVisible(false);
        containerPanel.add(noBookingsLabel, BorderLayout.CENTER);
        
        add(containerPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(143, 151, 121));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(120, 40));
        refreshButton.addActionListener(e -> loadBookings());
        buttonPanel.add(refreshButton);

        cancelButton = new JButton("Cancel Booking");
        cancelButton.setBackground(new Color(200, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(150, 40));
        cancelButton.addActionListener(e -> cancelBooking());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadBookings() {
        try {
            tableModel.setRowCount(0);
            List<Booking> bookings = bookingController.getUserBookings(customer.getUserId());

            for (Booking booking : bookings) {
                Object[] row = {
                    booking.getBookingId(),
                    booking.getFlightNumber(),
                    booking.getRoute(),
                    booking.getBookingDate() != null ? booking.getBookingDate().format(DATE_FORMATTER) : "N/A",
                    booking.getSeatNumber() != null ? booking.getSeatNumber() : "N/A",
                    booking.getStatus() != null ? booking.getStatus() : "UNKNOWN"
                };
                tableModel.addRow(row);
            }

            // Update the no bookings label visibility
            if (noBookingsLabel != null) {
                noBookingsLabel.setVisible(bookings.isEmpty());
            }
            
            // Show/hide the table based on whether there are bookings
            if (bookingTable != null) {
                bookingTable.setVisible(!bookings.isEmpty());
            }
            
        } catch (Exception e) {
            // Show error message in the UI
            if (noBookingsLabel != null) {
                noBookingsLabel.setText("Error loading bookings: " + e.getMessage());
                noBookingsLabel.setVisible(true);
            }
            
            // Log the error
            System.err.println("Error loading bookings: " + e.getMessage());
            e.printStackTrace();
            
            // Show error dialog to user
            JOptionPane.showMessageDialog(this,
                "Failed to load bookings. Please try again later.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelBooking() {
        int selectedRow = bookingTable.getSelectedRow();
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

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this booking?\nBooking ID: " + bookingId,
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingController.cancelBooking(bookingId)) {
                JOptionPane.showMessageDialog(this,
                        "Booking cancelled successfully!",
                        "Cancelled",
                        JOptionPane.INFORMATION_MESSAGE);
                loadBookings();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to cancel booking. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
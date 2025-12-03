// ============================================
// BookingDialog.java
// ============================================
package presentation;

import businessLogic.control.BookingController;
import businessLogic.control.PaymentController;
import businessLogic.entity.Customer;
import businessLogic.entity.Flight;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * <<boundary>>
 * Complete booking dialog with seat selection and payment
 */
public class BookingDialog extends JDialog {
    private static final Color SAGE_GREEN = new Color(85, 107, 47);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    
    private Customer customer;
    private Flight flight;
    private BookingController bookingController;
    private PaymentController paymentController;
    
    private JTextField seatNumberField;
    private JComboBox<String> paymentMethodCombo;
    private JTextField cardNumberField;
    private JTextField cvvField;
    private JTextField expiryField;
    private JTextField nameOnCardField;
    private boolean bookingSuccessful = false;

    public BookingDialog(Frame parent, Customer customer, Flight flight) {
        super(parent, "Book Flight - " + flight.getFlightNumber(), true);
        this.customer = customer;
        this.flight = flight;
        this.bookingController = new BookingController();
        this.paymentController = new PaymentController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Complete Your Booking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(SAGE_GREEN);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Flight Details Section
        contentPanel.add(createFlightDetailsPanel());
        contentPanel.add(Box.createVerticalStrut(20));

        // Seat Selection Section
        contentPanel.add(createSeatSelectionPanel());
        contentPanel.add(Box.createVerticalStrut(20));

        // Payment Section
        contentPanel.add(createPaymentPanel());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(178, 34, 34));
        cancelButton.setForeground(SAGE_GREEN);
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(107, 142, 35));
        confirmButton.setForeground(SAGE_GREEN);
        confirmButton.setFocusPainted(false);
        confirmButton.setPreferredSize(new Dimension(150, 40));
        confirmButton.addActionListener(e -> confirmBooking());
        buttonPanel.add(confirmButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFlightDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel header = new JLabel("✈ Flight Details");
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setForeground(SAGE_GREEN);
        panel.add(header, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // Flight Number
        gbc.gridx = 0;
        panel.add(createBoldLabel("Flight:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(flight.getFlightNumber() + " - " + flight.getAirline()), gbc);

        // Route
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Route:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(flight.getOrigin() + " → " + flight.getDestination()), gbc);

        // Departure
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Departure:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(flight.getDepartureTime().format(TIME_FORMATTER)), gbc);

        // Arrival
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Arrival:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(flight.getArrivalTime().format(TIME_FORMATTER)), gbc);

        // Available Seats
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Available Seats:"), gbc);
        gbc.gridx = 1;
        panel.add(createLabel(String.valueOf(flight.getAvailableSeats())), gbc);

        // Price
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(createBoldLabel("Price:"), gbc);
        gbc.gridx = 1;
        JLabel priceLabel = createLabel(String.format("$%.2f", flight.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(107, 142, 35));
        panel.add(priceLabel, gbc);

        return panel;
    }

    private JPanel createSeatSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
                "Seat Selection",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                SAGE_GREEN
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createBoldLabel("Seat Number:"), gbc);

        gbc.gridx = 1;
        seatNumberField = new JTextField(10);
        seatNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        seatNumberField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        seatNumberField.setToolTipText("Example: 12A, 23F, 1B");
        panel.add(seatNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JLabel hintLabel = new JLabel("Examples: 1A-32F (rows 1-32, seats A-F)");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        hintLabel.setForeground(Color.GRAY);
        panel.add(hintLabel, gbc);

        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
                "Payment Information",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                SAGE_GREEN
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Payment Method
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createBoldLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "PayPal"});
        paymentMethodCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(paymentMethodCombo, gbc);

        // Name on Card
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createBoldLabel("Name on Card:"), gbc);
        gbc.gridx = 1;
        nameOnCardField = new JTextField(20);
        styleTextField(nameOnCardField);
        panel.add(nameOnCardField, gbc);

        // Card Number
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createBoldLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        cardNumberField = new JTextField(20);
        styleTextField(cardNumberField);
        cardNumberField.setToolTipText("Enter 16-digit card number");
        panel.add(cardNumberField, gbc);

        // CVV
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createBoldLabel("CVV:"), gbc);
        gbc.gridx = 1;
        cvvField = new JTextField(5);
        styleTextField(cvvField);
        cvvField.setToolTipText("3-digit security code");
        panel.add(cvvField, gbc);

        // Expiry
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createBoldLabel("Expiry (MM/YY):"), gbc);
        gbc.gridx = 1;
        expiryField = new JTextField(7);
        styleTextField(expiryField);
        expiryField.setToolTipText("Example: 12/25");
        panel.add(expiryField, gbc);

        // Security Note
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel securityLabel = new JLabel("Secure Payment - This is a simulated transaction");
        securityLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        securityLabel.setForeground(Color.GRAY);
        panel.add(securityLabel, gbc);

        return panel;
    }

    private void confirmBooking() {
        // Validate seat number
        String seatNumber = seatNumberField.getText().trim();
        if (seatNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a seat number",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate payment details
        String cardNumber = cardNumberField.getText().trim();
        String cvv = cvvField.getText().trim();
        String expiry = expiryField.getText().trim();
        String nameOnCard = nameOnCardField.getText().trim();
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();

        if (nameOnCard.isEmpty() || cardNumber.isEmpty() || cvv.isEmpty() || expiry.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all payment fields",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!paymentController.validateCardNumber(cardNumber)) {
            JOptionPane.showMessageDialog(this,
                "Invalid card number. Must be 16 digits.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!paymentController.validateCVV(cvv)) {
            JOptionPane.showMessageDialog(this,
                "Invalid CVV. Must be 3 digits.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!expiry.matches("\\d{2}/\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                "Invalid expiry format. Use MM/YY",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create booking
        int bookingId = bookingController.createBooking(
            customer.getUserId(),
            flight.getFlightId(),
            seatNumber
        );

        if (bookingId <= 0) {
            JOptionPane.showMessageDialog(this,
                "Failed to create booking. Please try again.",
                "Booking Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Process payment
        int paymentId = paymentController.processPayment(
            bookingId,
            flight.getPrice(),
            paymentMethod,
            cardNumber
        );

        if (paymentId <= 0) {
            // Payment failed, cancel the booking
            bookingController.cancelBooking(bookingId);
            JOptionPane.showMessageDialog(this,
                "Payment processing failed. Booking cancelled.",
                "Payment Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Success!
        bookingSuccessful = true;
        
        String message = String.format(
            "Booking Confirmed!\n\n" +
            "Booking ID: #%d\n" +
            "Flight: %s\n" +
            "Route: %s → %s\n" +
            "Seat: %s\n" +
            "Amount Paid: $%.2f\n\n" +
            "A confirmation email has been sent to %s",
            bookingId,
            flight.getFlightNumber(),
            flight.getOrigin(),
            flight.getDestination(),
            seatNumber,
            flight.getPrice(),
            customer.getEmail()
        );

        JOptionPane.showMessageDialog(this,
            message,
            "Booking Successful",
            JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    public boolean isBookingSuccessful() {
        return bookingSuccessful;
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

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
}
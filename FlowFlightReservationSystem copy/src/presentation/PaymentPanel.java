// ============================================
// PaymentPanel.java
// ============================================
package presentation;

import businessLogic.control.PaymentController;
import javax.swing.*;
import java.awt.*;

/**
 * <<boundary>>
 * Payment Panel
 */
public class PaymentPanel extends JPanel {
    private PaymentController paymentController;
    private int bookingId;
    private double amount;
    
    private JComboBox<String> paymentMethodCombo;
    private JTextField cardNumberField;
    private JTextField cvvField;
    private JTextField expiryField;
    private JTextField nameOnCardField;

    public PaymentPanel(int bookingId, double amount) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentController = new PaymentController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Payment Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(85, 107, 47));
        add(titleLabel, gbc);

        // Amount
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(createLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        JLabel amountLabel = new JLabel(String.format("$%.2f", amount));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        amountLabel.setForeground(new Color(107, 142, 35));
        add(amountLabel, gbc);

        // Payment Method
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(createLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "Debit Card"});
        styleComboBox(paymentMethodCombo);
        add(paymentMethodCombo, gbc);

        // Name on Card
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(createLabel("Name on Card:"), gbc);
        gbc.gridx = 1;
        nameOnCardField = new JTextField(20);
        styleTextField(nameOnCardField);
        add(nameOnCardField, gbc);

        // Card Number
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(createLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        cardNumberField = new JTextField(20);
        styleTextField(cardNumberField);
        cardNumberField.setToolTipText("Enter 16-digit card number");
        add(cardNumberField, gbc);

        // CVV and Expiry on same row
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(createLabel("CVV:"), gbc);
        gbc.gridx = 1;
        cvvField = new JTextField(5);
        styleTextField(cvvField);
        cvvField.setToolTipText("3-digit security code");
        add(cvvField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(createLabel("Expiry (MM/YY):"), gbc);
        gbc.gridx = 1;
        expiryField = new JTextField(7);
        styleTextField(expiryField);
        expiryField.setToolTipText("Example: 12/25");
        add(expiryField, gbc);

        // Note
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("<html><i>Secure Payment - This is a simulated transaction</i></html>");
        noteLabel.setForeground(Color.GRAY);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        add(noteLabel, gbc);

        setPreferredSize(new Dimension(450, 400));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Arial", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
    }

    public boolean processPayment() {
        String cardNumber = cardNumberField.getText().trim().replaceAll("\\s", "");
        String cvv = cvvField.getText().trim();
        String expiry = expiryField.getText().trim();
        String nameOnCard = nameOnCardField.getText().trim();
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();

        // Validation
        if (nameOnCard.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter name on card", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cardNumber.isEmpty() || cvv.isEmpty() || expiry.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all payment fields", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!paymentController.validateCardNumber(cardNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid card number. Must be 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!paymentController.validateCVV(cvv)) {
            JOptionPane.showMessageDialog(this, "Invalid CVV. Must be 3 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!expiry.matches("\\d{2}/\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid expiry format. Use MM/YY", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Process payment
        int paymentId = paymentController.processPayment(bookingId, amount, paymentMethod, cardNumber);
        
        return paymentId > 0;
    }
}
package presentation;

import businessLogic.control.AuthenticationController;
import businessLogic.entity.User;
import javax.swing.*;
import java.awt.*;

/**
 * <<boundary>>
 * Signup Panel GUI
 */
public class SignupPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JCheckBox receivePromotionsCheckBox;
    private JButton signupButton;
    private JButton switchToLoginButton;
    private AuthFrame parentFrame;
    private String preSelectedRole;

    public SignupPanel(AuthFrame parentFrame, String preSelectedRole) {
        this.parentFrame = parentFrame;
        this.preSelectedRole = preSelectedRole;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 225)); // Light Cream
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back to Home Button - Top Left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, -70, 0, 0); // Negative left inset pushes it left

        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.PLAIN, 40));
        backButton.setForeground(new Color(85, 107, 47)); // your olive green
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            parentFrame.dispose();
            new HomePage().setVisible(true);
        });

        add(backButton, gbc);

        // Decorative top accent
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 10);
        JPanel accentPanel = new JPanel();
        accentPanel.setBackground(new Color(107, 142, 35));
        accentPanel.setPreferredSize(new Dimension(400, 5));
        add(accentPanel, gbc);

        // Title with icon
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel titleLabel = new JLabel("✈ Sign Up as " + preSelectedRole);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(85, 107, 47)); // Dark Olive Green
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, gbc);

        // Subtitle
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 15, 10);
        JLabel subtitleLabel = new JLabel("Create your account and start your journey!");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(100, 100, 90));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(subtitleLabel, gbc);

        // First Name
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(8, 10, 8, 10);
        addLabelAndField("First Name:", firstNameField = new JTextField(20), gbc);

        // Last Name
        gbc.gridy = 5;
        addLabelAndField("Last Name:", lastNameField = new JTextField(20), gbc);

        // Email
        gbc.gridy = 6;
        addLabelAndField("Email:", emailField = new JTextField(20), gbc);

        // Password
        gbc.gridy = 7;
        addLabelAndField("Password:", passwordField = new JPasswordField(20), gbc);

        // Confirm Password
        gbc.gridy = 8;
        addLabelAndField("Confirm Password:", confirmPasswordField = new JPasswordField(20), gbc);

        // Address
        gbc.gridy = 9;
        addLabelAndField("Address:", addressField = new JTextField(20), gbc);

        // Phone
        gbc.gridy = 10;
        addLabelAndField("Phone:", phoneField = new JTextField(20), gbc);

        // Receive Promotions (only for customers)
        if (preSelectedRole.equalsIgnoreCase("CUSTOMER")) {
            gbc.gridx = 0;
            gbc.gridy = 11;
            gbc.gridwidth = 2;
            receivePromotionsCheckBox = new JCheckBox("I want to receive monthly promotional news");
            receivePromotionsCheckBox.setFont(new Font("Arial", Font.PLAIN, 13));
            receivePromotionsCheckBox.setBackground(new Color(240, 240, 225)); // Light Cream
            receivePromotionsCheckBox.setForeground(new Color(60, 60, 50)); // Dark Grey
            receivePromotionsCheckBox.setSelected(true); // Default checked
            add(receivePromotionsCheckBox, gbc);
        }

        // Spacer
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        add(Box.createVerticalStrut(5), gbc);

        // Signup Button
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Arial", Font.BOLD, 16));
        signupButton.setBackground(new Color(107, 142, 35)); // Olive Drab
        signupButton.setForeground(new Color(60, 60, 50)); // Dark Grey
        signupButton.setFocusPainted(false);
        signupButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(85, 107, 47), 2),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        signupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = signupButton.getBackground();
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signupButton.setBackground(new Color(128, 170, 42)); // Lighter olive
                signupButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(107, 142, 35), 2),
                        BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signupButton.setBackground(originalColor);
                signupButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(85, 107, 47), 2),
                        BorderFactory.createEmptyBorder(12, 20, 12, 20)
                ));
            }
        });

        signupButton.addActionListener(e -> handleSignup());
        add(signupButton, gbc);

        // Decorative divider
        gbc.gridy = 14;
        gbc.insets = new Insets(15, 40, 15, 40);
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(143, 151, 121));
        add(separator, gbc);

        // Switch to Login
        gbc.gridy = 15;
        gbc.insets = new Insets(5, 10, 5, 10);
        switchToLoginButton = new JButton("Already have an account? Login");
        switchToLoginButton.setFont(new Font("Arial", Font.PLAIN, 13));
        switchToLoginButton.setForeground(new Color(107, 142, 35)); // Olive Drab
        switchToLoginButton.setBorderPainted(false);
        switchToLoginButton.setContentAreaFilled(false);
        switchToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchToLoginButton.addActionListener(e -> parentFrame.showLoginPanel());
        add(switchToLoginButton, gbc);
    }

    private void addLabelAndField(String labelText, JTextField field, GridBagConstraints gbc) {
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(60, 60, 50)); // Dark Grey
        add(label, gbc);

        gbc.gridx = 1;
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2), // Sage
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        add(field, gbc);
    }

    private void handleSignup() {
        // Get values
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        boolean receivePromotions = (receivePromotionsCheckBox != null &&
                receivePromotionsCheckBox.isSelected());

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields",
                    "Signup Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        AuthenticationController authController = AuthenticationController.getInstance();

        // Validate email format
        if (!authController.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address",
                    "Signup Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate password
        if (!authController.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters long",
                    "Signup Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Signup Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Attempt signup
        User user = authController.signup(email, password, firstName, lastName,
                address, phone, preSelectedRole, receivePromotions);

        if (user != null) {
            String message = "Welcome, " + user.getFullName() + "!\n" +
                    "Your account has been created successfully.";

            if (receivePromotions) {
                message += "\n\nYou are subscribed to monthly promotions!";
            }

            JOptionPane.showMessageDialog(this,
                    message,
                    "Signup Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            // Close auth frame and open appropriate dashboard
            parentFrame.dispose();
            user.displayDashboard();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Email already exists. Please use a different email or login.",
                    "Signup Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Clear password fields
        passwordField.setText("");
        confirmPasswordField.setText("");
    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        if (receivePromotionsCheckBox != null) {
            receivePromotionsCheckBox.setSelected(true);
        }
    }
}
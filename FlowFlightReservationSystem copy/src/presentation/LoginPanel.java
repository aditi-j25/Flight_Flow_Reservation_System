// ============================================
// LoginPanel.java
// ============================================
package presentation;

import businessLogic.control.AuthenticationController;
import businessLogic.entity.User;
import javax.swing.*;
import java.awt.*;

/**
 * <<boundary>>
 * Login Panel GUI
 */
public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton switchToSignupButton;
    private AuthFrame parentFrame;
    private String preSelectedRole;

    public LoginPanel(AuthFrame parentFrame, String preSelectedRole) {
        this.parentFrame = parentFrame;
        this.preSelectedRole = preSelectedRole;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 225));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back Button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, -70, 0, 0);
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.PLAIN, 40));
        backButton.setForeground(new Color(85, 107, 47));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            parentFrame.dispose();
            new HomePage().setVisible(true);
        });
        add(backButton, gbc);

        // Accent
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

        // Title
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel titleLabel = new JLabel("✈ Login as " + preSelectedRole);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(85, 107, 47));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, gbc);

        // Subtitle
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 20, 10);
        JLabel subtitleLabel = new JLabel("Welcome back! Please enter your credentials");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(100, 100, 90));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(subtitleLabel, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(60, 60, 50));
        gbc.gridx = 0;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBackground(Color.WHITE);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(60, 60, 50));
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 10, 10);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(107, 142, 35));
        loginButton.setForeground(new Color(60, 60, 50));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(85, 107, 47), 2),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        add(loginButton, gbc);

        // Divider
        gbc.gridy = 8;
        gbc.insets = new Insets(15, 40, 15, 40);
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(143, 151, 121));
        add(separator, gbc);

        // Signup Switch
        if (preSelectedRole.equalsIgnoreCase("CUSTOMER")) {
            gbc.gridy = 9;
            gbc.insets = new Insets(5, 10, 5, 10);
            switchToSignupButton = new JButton("Don't have an account? Sign up");
            switchToSignupButton.setFont(new Font("Arial", Font.PLAIN, 13));
            switchToSignupButton.setForeground(new Color(107, 142, 35));
            switchToSignupButton.setBorderPainted(false);
            switchToSignupButton.setContentAreaFilled(false);
            switchToSignupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            switchToSignupButton.addActionListener(e -> parentFrame.showSignupPanel());
            add(switchToSignupButton, gbc);
        }

        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        AuthenticationController authController = AuthenticationController.getInstance();
        User user = authController.login(email, password);

        if (user != null) {
            if (!user.getRole().equalsIgnoreCase(preSelectedRole)) {
                JOptionPane.showMessageDialog(this,
                        "This account is registered as " + user.getRole() + ", not " + preSelectedRole,
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                authController.logout();
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Welcome back, " + user.getFullName() + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            parentFrame.dispose();
            user.displayDashboard();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid email or password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        passwordField.setText("");
    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }
}
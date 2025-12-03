// ============================================
// Updated AuthFrame.java
// ============================================
package presentation;

import javax.swing.*;
import java.awt.*;

/**
 * <<boundary>>
 * Main Authentication Frame that switches between Login and Signup panels
 */
public class AuthFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private SignupPanel signupPanel;
    private String role; // The role this auth frame is for

    public AuthFrame(String role) {
        this.role = role.toUpperCase();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Flight Reservation System - Authentication");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this, role);
        signupPanel = new SignupPanel(this, role);

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(signupPanel, "SIGNUP");

        add(cardPanel);

        // Show login by default
        cardLayout.show(cardPanel, "LOGIN");
    }

    public void showLoginPanel() {
        signupPanel.clearFields();
        cardLayout.show(cardPanel, "LOGIN");
    }

    public void showSignupPanel() {
        loginPanel.clearFields();
        cardLayout.show(cardPanel, "SIGNUP");
    }
}
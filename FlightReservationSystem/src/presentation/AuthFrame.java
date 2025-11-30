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
    private String selectedRole;

    public AuthFrame(String role) {
        this.selectedRole = role;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Flight Reservation System - Authentication");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // CardLayout to switch between Login and Signup
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create panels
        loginPanel = new LoginPanel(this, selectedRole);
        signupPanel = new SignupPanel(this, selectedRole);

        // Add panels to card panel
        cardPanel.add(loginPanel, "LOGIN");

        // Only show signup panel for customers
        if (selectedRole.equalsIgnoreCase("CUSTOMER")) {
            cardPanel.add(signupPanel, "SIGNUP");
        }

        // Add to frame
        add(cardPanel);

        // Show login panel by default
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
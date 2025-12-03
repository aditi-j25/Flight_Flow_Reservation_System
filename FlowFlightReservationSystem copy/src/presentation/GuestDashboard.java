// ============================================
// GuestDashboard.java 
// ============================================
package presentation;

import businessLogic.entity.Guest;
import javax.swing.*;
import java.awt.*;

/**
 * <<boundary>>
 * Guest Dashboard - Browse flights without login
 */
public class GuestDashboard extends JFrame {
    private Guest guest;

    public GuestDashboard() {
        this.guest = new Guest();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Flight Reservation System - Guest Mode");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 225));

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(107, 142, 35));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel welcomeLabel = new JLabel("Guest Mode - Browse Flights");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        topBar.add(welcomeLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(107, 142, 35));

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(85, 107, 47));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> {
            dispose();
            new HomePage().setVisible(true);
        });
        buttonPanel.add(loginButton);

        JButton signupButton = new JButton("Sign Up");
        signupButton.setBackground(new Color(128, 170, 42));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(new Font("Arial", Font.BOLD, 13));
        signupButton.setFocusPainted(false);
        signupButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.addActionListener(e -> {
            dispose();
            new AuthFrame("CUSTOMER").setVisible(true);
        });
        buttonPanel.add(signupButton);

        topBar.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Content Area - Flight Search Panel
        GuestFlightSearchPanel searchPanel = new GuestFlightSearchPanel(guest);
        mainPanel.add(searchPanel, BorderLayout.CENTER);

        // Info Banner
        JPanel infoBanner = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoBanner.setBackground(new Color(255, 250, 205));
        infoBanner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(255, 215, 0)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel infoLabel = new JLabel("You are browsing as a guest. Login or Sign up to book flights and access more features!");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoLabel.setForeground(new Color(204, 102, 0));
        infoBanner.add(infoLabel);

        mainPanel.add(infoBanner, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
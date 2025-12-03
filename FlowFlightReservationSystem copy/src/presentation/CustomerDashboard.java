// ============================================
// CustomerDashboard.java
// ============================================
package presentation;

import businessLogic.entity.Customer;
import businessLogic.control.AuthenticationController;
import javax.swing.*;
import java.awt.*;

/**
 * <<boundary>>
 * Customer Dashboard with Sage Green styled buttons
 */
public class CustomerDashboard extends JFrame {
    private static final Color SAGE_GREEN = new Color(85, 107, 47);
    private Customer customer;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public CustomerDashboard(Customer customer) {
        this.customer = customer;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Flight Reservation System - Customer Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 225));

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(107, 142, 35));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + customer.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        topBar.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(85, 107, 47));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        topBar.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Navigation
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(3, 1, 10, 10));
        navPanel.setBackground(new Color(143, 151, 121));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navPanel.setPreferredSize(new Dimension(200, 0));

        JButton searchFlightsBtn = createNavButton("Search Flights");
        JButton bookingsBtn = createNavButton("My Bookings");
        JButton profileBtn = createNavButton("Profile");

        navPanel.add(searchFlightsBtn);
        navPanel.add(bookingsBtn);
        navPanel.add(profileBtn);

        mainPanel.add(navPanel, BorderLayout.WEST);

        // Content Area
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        // Add all panels
        cardPanel.add(new FlightSearchPanel(customer), "SEARCH");
        cardPanel.add(new BookingPanel(customer), "BOOKINGS");
        cardPanel.add(new ProfilePanel(customer), "PROFILE");

        searchFlightsBtn.addActionListener(e -> cardLayout.show(cardPanel, "SEARCH"));
        bookingsBtn.addActionListener(e -> {
            cardPanel.remove(1); // Remove old bookings panel
            cardPanel.add(new BookingPanel(customer), "BOOKINGS", 1); // Add fresh one
            cardLayout.show(cardPanel, "BOOKINGS");
        });
        profileBtn.addActionListener(e -> cardLayout.show(cardPanel, "PROFILE"));

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        add(mainPanel);
        cardLayout.show(cardPanel, "SEARCH");
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

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            AuthenticationController.getInstance().logout();
            dispose();
            new HomePage().setVisible(true);
        }
    }
}
// ============================================
// HomePage.java 
// ============================================
package presentation;

import businessLogic.entity.Guest;
import javax.swing.*;
import java.awt.*;

/**
 * <<boundary>>
 * Home page with role selection including Guest
 */
public class HomePage extends JFrame {

    public HomePage() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Flight Reservation System - Welcome");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 225));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel titleLabel = new JLabel("✈ Flight Flow Reservation System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(85, 107, 47));
        mainPanel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("Select your role to continue");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(100, 100, 90));
        mainPanel.add(subtitleLabel, gbc);

        // Spacer
        gbc.gridy = 2;
        mainPanel.add(Box.createVerticalStrut(20), gbc);

        // Guest Button
        gbc.gridy = 3;
        JButton guestButton = createRoleButton("Browse as Guest", "View flights without creating an account");
        guestButton.setBackground(new Color(143, 151, 121));
        guestButton.addActionListener(e -> openGuestMode());
        mainPanel.add(guestButton, gbc);

        // Customer Button
        gbc.gridy = 4;
        JButton customerButton = createRoleButton("Customer", "Book flights and manage reservations");
        customerButton.addActionListener(e -> openAuthFrame("CUSTOMER"));
        mainPanel.add(customerButton, gbc);

        // Agent Button
        gbc.gridy = 5;
        JButton agentButton = createRoleButton("Flight Agent", "Manage customer bookings");
        agentButton.addActionListener(e -> openAuthFrame("AGENT"));
        mainPanel.add(agentButton, gbc);

        // Admin Button
        gbc.gridy = 6;
        JButton adminButton = createRoleButton("Administrator", "Manage flights and system");
        adminButton.addActionListener(e -> openAuthFrame("ADMIN"));
        mainPanel.add(adminButton, gbc);

        add(mainPanel);
    }

    private JButton createRoleButton(String role, String description) {
        JButton button = new JButton("<html><center><b>" + role + "</b><br>" +
                "<font size='3' color='#556B2F'>" + description + "</font></center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(400, 80));
        button.setBackground(new Color(107, 142, 35));
        // SAGE GREEN TEXT COLOR
        button.setForeground(new Color(85, 107, 47));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(85, 107, 47), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = button.getBackground();
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(128, 170, 42));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    private void openGuestMode() {
        dispose();
        new GuestDashboard().setVisible(true);
    }

    private void openAuthFrame(String role) {
        dispose();
        new AuthFrame(role).setVisible(true);
    }
}
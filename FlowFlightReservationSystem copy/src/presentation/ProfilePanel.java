// ============================================
// ProfilePanel.java
// ============================================
package presentation;

import businessLogic.entity.Customer;
import businessLogic.control.PromotionManager;
import database.UserDAO;
import javax.swing.*;
import java.awt.*;

/**
 * <<boundary>>
 * Profile Panel - Customer profile management
 */
public class ProfilePanel extends JPanel {
    private Customer customer;
    private UserDAO userDAO;
    private PromotionManager promotionManager;

    private JTextField emailField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JCheckBox promotionsCheckbox;
    private JTextArea promotionDisplayArea;
    private JPanel promotionPanel;

    public ProfilePanel(Customer customer) {
        this.customer = customer;
        this.userDAO = new UserDAO();
        this.promotionManager = PromotionManager.getInstance();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(85, 107, 47));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Main Content Panel (scrollable)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Profile Information Section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel profileInfoLabel = new JLabel("Profile Information");
        profileInfoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        profileInfoLabel.setForeground(new Color(85, 107, 47));
        contentPanel.add(profileInfoLabel, gbc);

        gbc.gridwidth = 1;

        // Email (read-only)
        gbc.gridy = 1;
        gbc.gridx = 0;
        contentPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(customer.getEmail());
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        styleTextField(emailField);
        contentPanel.add(emailField, gbc);

        // First Name
        gbc.gridy = 2;
        gbc.gridx = 0;
        contentPanel.add(createLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(customer.getFirstName());
        styleTextField(firstNameField);
        contentPanel.add(firstNameField, gbc);

        // Last Name
        gbc.gridy = 3;
        gbc.gridx = 0;
        contentPanel.add(createLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(customer.getLastName());
        styleTextField(lastNameField);
        contentPanel.add(lastNameField, gbc);

        // Address
        gbc.gridy = 4;
        gbc.gridx = 0;
        contentPanel.add(createLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(customer.getAddress());
        styleTextField(addressField);
        contentPanel.add(addressField, gbc);

        // Phone
        gbc.gridy = 5;
        gbc.gridx = 0;
        contentPanel.add(createLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(customer.getPhone());
        styleTextField(phoneField);
        contentPanel.add(phoneField, gbc);

        // Promotions Section
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        JLabel promotionsLabel = new JLabel("Promotional Preferences");
        promotionsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        promotionsLabel.setForeground(new Color(85, 107, 47));
        contentPanel.add(promotionsLabel, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(10, 10, 10, 10);
        promotionsCheckbox = new JCheckBox("I want to receive monthly promotional news and special offers");
        promotionsCheckbox.setSelected(customer.isReceivePromotions());
        promotionsCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        promotionsCheckbox.setBackground(Color.WHITE);
        promotionsCheckbox.addActionListener(e -> togglePromotionDisplay());
        contentPanel.add(promotionsCheckbox, gbc);

        // Promotion Display (conditionally shown)
        if (customer.isReceivePromotions()) {
            gbc.gridy = 8;
            promotionPanel = createPromotionDisplayPanel();
            contentPanel.add(promotionPanel, gbc);
        }

        // Update Button
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        JButton updateButton = new JButton("Save Changes");
        updateButton.setBackground(new Color(107, 142, 35));
        updateButton.setForeground(new Color(85, 107, 47));
        updateButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateButton.setFocusPainted(false);
        updateButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(85, 107, 47), 2),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.addActionListener(e -> updateProfile());
        contentPanel.add(updateButton, gbc);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createPromotionDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 100), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel promoTitle = new JLabel("Current Promotions & Offers");
        promoTitle.setFont(new Font("Arial", Font.BOLD, 16));
        promoTitle.setForeground(new Color(204, 102, 0));
        panel.add(promoTitle, BorderLayout.NORTH);

        promotionDisplayArea = new JTextArea(
            "MONTHLY SPECIAL: Get 20% off on all domestic flights!\n" +
            "Valid until end of month - Book now and save big!\n\n" +
            "EARLY BIRD: Book 2 weeks in advance and save an additional 15%\n\n" +
            "NEW CUSTOMER BONUS: Get 10% off your first booking!\n\n" +
            "LOYALTY REWARD: Earn points with every booking\n\n" +
            "You will receive exclusive deals via email on the 1st of each month"
        );
        promotionDisplayArea.setEditable(false);
        promotionDisplayArea.setFont(new Font("Arial", Font.PLAIN, 13));
        promotionDisplayArea.setBackground(new Color(255, 250, 240));
        promotionDisplayArea.setLineWrap(true);
        promotionDisplayArea.setWrapStyleWord(true);
        promotionDisplayArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(promotionDisplayArea, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(600, 200));

        return panel;
    }

    private void togglePromotionDisplay() {
        // This will be handled when user clicks "Save Changes"
        // Just update the visual state for now
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(350, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(143, 151, 121), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void updateProfile() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        boolean receivePromotions = promotionsCheckbox.isSelected();

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "First name and last name are required",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update customer object
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setPhone(phone);

        // Update in database
        boolean profileUpdated = userDAO.updateUserProfile(
            customer.getUserId(), firstName, lastName, address, phone
        );

        // Handle promotion preference change
        boolean previousPromoPref = customer.isReceivePromotions();
        boolean promoUpdated = true;

        if (previousPromoPref != receivePromotions) {
            promoUpdated = userDAO.updatePromotionPreference(
                customer.getUserId(), receivePromotions
            );

            if (promoUpdated) {
                customer.setReceivePromotions(receivePromotions);

                if (receivePromotions) {
                    promotionManager.subscribe(customer);
                } else {
                    promotionManager.unsubscribe(customer);
                }
            }
        }

        // Show result
        if (profileUpdated && promoUpdated) {
            String message = "Profile updated successfully!";
            
            if (previousPromoPref != receivePromotions) {
                if (receivePromotions) {
                    message += "\n\nYou've been subscribed to monthly promotions!\n" +
                              "You'll receive exclusive deals on the 1st of each month.";
                } else {
                    message += "\n\nYou've been unsubscribed from promotions.";
                }
            }

            JOptionPane.showMessageDialog(this,
                    message,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh panel to show/hide promotion box
            refreshPanel();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update profile. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshPanel() {
        // Refresh the entire panel to show/hide promotions display
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            parent.add(new ProfilePanel(customer), "PROFILE");
            parent.revalidate();
            parent.repaint();
        }
    }
}
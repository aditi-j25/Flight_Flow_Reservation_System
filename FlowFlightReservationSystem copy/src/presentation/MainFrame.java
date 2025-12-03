// ============================================
// MainFrame.java - Application Entry Point
// Run DatabaseInitializer.jave first to generate default Admin and Agent roles
// ============================================
package presentation;

import database.DatabaseConnection;
import javax.swing.*;

/**
 * <<boundary>>
 * Main entry point for the Flight Reservation System
 */
public class MainFrame {
    public static void main(String[] args) {
        // Add shutdown hook to close database connection properly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnection.getInstance().close();
        }));
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HomePage().setVisible(true);
        });
    }
}
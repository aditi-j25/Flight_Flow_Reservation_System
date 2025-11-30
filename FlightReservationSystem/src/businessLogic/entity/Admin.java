package businessLogic.entity;

/**
 * <<entity>>
 * Admin user type - can manage flights and system data
 */
public class Admin extends User {

    public Admin(int userId, String email, String password, String firstName,
                 String lastName, String address, String phone) {
        super(userId, email, password, firstName, lastName, address, phone,
                "ADMIN", false); // Admins don't receive promotions
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Admin Dashboard for: " + getFullName());
        // This will show admin-specific features like flight management
    }

    public void manageFlight() {
        // Admin-specific functionality
        System.out.println("Admin managing flights");
    }

    public void manageSystem() {
        // Admin-specific functionality
        System.out.println("Admin managing system");
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId=" + getUserId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getFullName() + '\'' +
                '}';
    }
}
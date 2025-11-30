package businessLogic.entity;

import java.time.LocalDateTime;

/**
 * <<entity>>
 * Admin user type - can manage flights and system data
 */
public class Admin extends User {

    public Admin(int userId, String email, String password, String firstName,
                 String lastName, String address, String phone,
                 boolean receivePromotions, LocalDateTime createdAt) {

        super(userId, email, password, firstName, lastName, address, phone,
              "Admin", receivePromotions, createdAt);
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Admin Dashboard for: " + getFullName());
        // Launch admin dashboard GUI here later
    }

    public void manageFlight() {
        System.out.println("Admin managing flights");
    }

    public void manageSystem() {
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

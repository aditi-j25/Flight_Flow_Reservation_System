// ============================================
// Admin.java
// ============================================
package businessLogic.entity;

import presentation.AdminDashboard;

/**
 * <<entity>>
 * Admin user type - can manage flights and system data
 */
public class Admin extends User {

    public Admin(int userId, String email, String password, String firstName,
                 String lastName, String address, String phone) {
        super(userId, email, password, firstName, lastName, address, phone,
                "ADMIN", false);
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Admin Dashboard for: " + getFullName());
        new AdminDashboard(this).setVisible(true);
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
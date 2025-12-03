// ============================================
// Agent.java
// ============================================
package businessLogic.entity;

import presentation.AgentDashboard;

/**
 * <<entity>>
 * Flight Agent user type - can manage customer bookings
 */
public class Agent extends User {

    public Agent(int userId, String email, String password, String firstName,
                 String lastName, String address, String phone) {
        super(userId, email, password, firstName, lastName, address, phone,
                "AGENT", false);
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Agent Dashboard for: " + getFullName());
        new AgentDashboard(this).setVisible(true);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "userId=" + getUserId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getFullName() + '\'' +
                '}';
    }
}
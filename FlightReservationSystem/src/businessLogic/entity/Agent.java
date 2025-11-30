package businessLogic.entity;



/**
 * <<entity>>
 * Flight Agent user type - can manage customer bookings
 */
public class Agent extends User {

    public Agent(int userId, String email, String password, String firstName,
                 String lastName, String address, String phone) {
        super(userId, email, password, firstName, lastName, address, phone,
                "AGENT", false); // Agents don't receive promotions
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Agent Dashboard for: " + getFullName());
        // This will show agent-specific features
    }

    public void manageCustomerBooking() {
        // Agent-specific functionality
        System.out.println("Agent managing customer bookings");
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
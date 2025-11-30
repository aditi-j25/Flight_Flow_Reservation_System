package businessLogic.entity;

import java.time.LocalDateTime;

/**
 * <<entity>>
 * Flight Agent user type - can manage customer bookings
 */
public class Agent extends User {

    public Agent(int userId, String email, String password, String firstName,
                 String lastName, String address, String phone,
                 boolean receivePromotions, LocalDateTime createdAt) {

        super(userId, email, password, firstName, lastName, address, phone,
              "Agent", receivePromotions, createdAt);
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Agent Dashboard for: " + getFullName());
    }

    public void manageCustomerBooking() {
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

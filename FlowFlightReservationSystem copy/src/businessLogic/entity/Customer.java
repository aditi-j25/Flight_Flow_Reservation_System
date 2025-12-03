// ============================================
// Customer.java
// ============================================
package businessLogic.entity;

import presentation.CustomerDashboard;

/**
 * <<entity>>
 * Customer user type - can book flights and receive promotions
 */
public class Customer extends User implements PromotionObserver {

    public Customer(int userId, String email, String password, String firstName,
                    String lastName, String address, String phone, boolean receivePromotions) {
        super(userId, email, password, firstName, lastName, address, phone,
                "CUSTOMER", receivePromotions);
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Customer Dashboard for: " + getFullName());
        new CustomerDashboard(this).setVisible(true);
    }

    @Override
    public void receivePromotion(String promotionMessage) {
        if (isReceivePromotions()) {
            System.out.println("Promotion received by " + getEmail() + ": " + promotionMessage);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "userId=" + getUserId() +
                ", email='" + getEmail() + '\'' +
                ", name='" + getFullName() + '\'' +
                '}';
    }
}
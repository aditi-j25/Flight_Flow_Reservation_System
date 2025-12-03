// ============================================
// UserFactory.java - Factory Pattern
// ============================================
package businessLogic.control;

import businessLogic.entity.*;

/**
 * <<control>>
 * Factory Pattern - Creates appropriate user objects based on role
 */
public class UserFactory {

    public static User createUser(int userId, String email, String password,
                                  String firstName, String lastName, String address,
                                  String phone, String role, boolean receivePromotions) {

        switch (role.toUpperCase()) {
            case "CUSTOMER":
                Customer customer = new Customer(userId, email, password, firstName,
                        lastName, address, phone, receivePromotions);
                if (receivePromotions) {
                    PromotionManager.getInstance().subscribe(customer);
                }
                return customer;

            case "AGENT":
                return new Agent(userId, email, password, firstName,
                        lastName, address, phone);

            case "ADMIN":
                return new Admin(userId, email, password, firstName,
                        lastName, address, phone);

            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public static User createUser(int userId, String email, String password,
                                  String firstName, String lastName, String address,
                                  String phone, String role, int receivePromotionsInt) {
        boolean receivePromotions = (receivePromotionsInt == 1);
        return createUser(userId, email, password, firstName, lastName,
                address, phone, role, receivePromotions);
    }
}
package businessLogic.control;



import businessLogic.entity.*;

/**
 * <<control>>
 * Factory Pattern - Creates appropriate user objects based on role
 */
public class UserFactory {

    /**
     * Creates a User object of the appropriate type based on role
     * @param userId User's database ID
     * @param email User's email
     * @param password User's password
     * @param firstName User's first name
     * @param lastName User's last name
     * @param address User's address
     * @param phone User's phone number
     * @param role User's role (CUSTOMER, AGENT, ADMIN)
     * @param receivePromotions Whether user wants to receive promotions
     * @return User object of appropriate type
     */
    public static User createUser(int userId, String email, String password,
                                  String firstName, String lastName, String address,
                                  String phone, String role, boolean receivePromotions) {

        switch (role.toUpperCase()) {
            case "CUSTOMER":
                Customer customer = new Customer(userId, email, password, firstName,
                        lastName, address, phone, receivePromotions);

                // If customer wants promotions, subscribe them to PromotionManager
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

    /**
     * Creates a User object from database query results
     * Overloaded method for convenience
     */
    public static User createUser(int userId, String email, String password,
                                  String firstName, String lastName, String address,
                                  String phone, String role, int receivePromotionsInt) {
        boolean receivePromotions = (receivePromotionsInt == 1);
        return createUser(userId, email, password, firstName, lastName,
                address, phone, role, receivePromotions);
    }
}
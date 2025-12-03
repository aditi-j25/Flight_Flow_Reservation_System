// ============================================
// AuthenticationController.java - Singleton
// ============================================
package businessLogic.control;

import database.UserDAO;
import businessLogic.entity.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * <<control>>
 * Singleton Pattern - Handles authentication logic
 */
public class AuthenticationController {
    private static AuthenticationController instance;
    private User currentUser;
    private UserDAO userDAO;

    private AuthenticationController() {
        this.userDAO = new UserDAO();
        this.currentUser = null;
    }

    public static synchronized AuthenticationController getInstance() {
        if (instance == null) {
            instance = new AuthenticationController();
        }
        return instance;
    }

    public User login(String email, String password) {
        try {
            String hashedPassword = hashPassword(password);
            User user = userDAO.authenticateUser(email, hashedPassword);

            if (user != null) {
                this.currentUser = user;
                System.out.println("Login successful: " + user.getFullName());
                return user;
            } else {
                System.out.println("Login failed: Invalid credentials");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }

    public User signup(String email, String password, String firstName,
                       String lastName, String address, String phone,
                       String role, boolean receivePromotions) {
        try {
            if (userDAO.emailExists(email)) {
                System.out.println("Signup failed: Email already exists");
                return null;
            }

            String hashedPassword = hashPassword(password);
            int userId = userDAO.insertUser(email, hashedPassword, firstName,
                    lastName, address, phone, role, receivePromotions);

            if (userId > 0) {
                User newUser = UserFactory.createUser(userId, email, hashedPassword,
                        firstName, lastName, address, phone, role, receivePromotions);
                this.currentUser = newUser;
                System.out.println("Signup successful: " + newUser.getFullName());
                return newUser;
            } else {
                System.out.println("Signup failed: Database error");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Signup error: " + e.getMessage());
            return null;
        }
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Logout: " + currentUser.getFullName());
            this.currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return password;
        }
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}
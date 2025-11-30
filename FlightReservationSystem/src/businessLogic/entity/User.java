package businessLogic.entity;

import java.time.LocalDateTime;

/**
 * <<entity>>
 * Abstract base class for all user types
 */
public abstract class User {
    private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String role; 
    private boolean receivePromotions;
    private LocalDateTime createdAt;

    // Constructor for NEW users (created now)
    public User(int userId, String email, String password, String firstName,
                String lastName, String address, String phone, String role,
                boolean receivePromotions) {

        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.role = role;
        this.receivePromotions = receivePromotions;
        this.createdAt = LocalDateTime.now(); // default for new users
    }

    // Constructor for users LOADED FROM DATABASE
    public User(int userId, String email, String password, String firstName,
                String lastName, String address, String phone, String role,
                boolean receivePromotions, LocalDateTime createdAt) {

        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.role = role;
        this.receivePromotions = receivePromotions;
        this.createdAt = createdAt; // use DB value
    }

    public abstract void displayDashboard();

    // Getters
    public int getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public boolean isReceivePromotions() { return receivePromotions; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setReceivePromotions(boolean receivePromotions) {
        this.receivePromotions = receivePromotions;
    }
}

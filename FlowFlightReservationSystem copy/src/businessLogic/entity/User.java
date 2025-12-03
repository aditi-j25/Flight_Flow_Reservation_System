// ============================================
// User.java
// ============================================
package businessLogic.entity;

/**
 * <<entity>>
 * Abstract base class for all users (Customer, Agent, Admin, Guest)
 */
public abstract class User {
    private int userId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String role; // CUSTOMER, AGENT, ADMIN, GUEST
    private boolean receivePromotions;

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
    }

    // Abstract method - each user type displays their own dashboard
    public abstract void displayDashboard();

    // Check if user is a guest
    public boolean isGuest() {
        return "GUEST".equals(role);
    }

    // Check if user can book flights
    public boolean canBook() {
        return !isGuest(); // Only non-guests can book
    }

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

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRole(String role) { this.role = role; }
    public void setReceivePromotions(boolean receivePromotions) { 
        this.receivePromotions = receivePromotions; 
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", name='" + getFullName() + '\'' +
                '}';
    }
}
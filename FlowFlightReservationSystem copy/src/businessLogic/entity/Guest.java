// ============================================
// Guest.java
// ============================================
package businessLogic.entity;

import presentation.GuestDashboard;

/**
 * <<entity>>
 * Guest user type - can browse flights but cannot book
 */
public class Guest extends User {

    public Guest() {
        super(-1, "guest@temporary.com", "", "Guest", "User", 
              "", "", "GUEST", false);
    }

    @Override
    public void displayDashboard() {
        System.out.println("Displaying Guest Dashboard");
        new GuestDashboard().setVisible(true);
    }

    @Override
    public boolean canBook() {
        return false;
    }

    @Override
    public boolean isGuest() {
        return true;
    }

    @Override
    public String toString() {
        return "Guest{browsing mode}";
    }
}
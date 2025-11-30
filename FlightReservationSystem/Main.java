import businessLogic.entity.*;
import businessLogic.control.*;
import database.*;
import presentation.*;



import java.util.*;
import java.sql.*; 
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        // Create example users
        Admin admin = new Admin(
                1, "admin@gmail.com", "admin123",
                "Alice", "Admin", "1 Admin Street", "111-222-3333", LocalDateTime.now()
        );

        Agent agent = new Agent(
                2, "agent@gmail.com", "agent123",
                "Bob", "Agent", "45 Agent Road", "444-555-6666", LocalDateTime.now()
        );

        Customer customer = new Customer(
                3, "customer@gmail.com", "cust123",
                "Charlie", "Customer", "789 Customer Ave", "777-888-9999",
                true, LocalDateTime.now() // receives promotions
        );

        //================TESTS================//
        // Display dashboards
        System.out.println("===== DASHBOARDS =====");
        admin.displayDashboard();
        agent.displayDashboard();
        customer.displayDashboard();

        // Test role-specific actions
        System.out.println("\n===== ROLE ACTIONS =====");
        admin.manageFlight();
        admin.manageSystem();
        agent.manageCustomerBooking();

        // Test promotion system
        System.out.println("\n===== PROMOTIONS =====");
        //sendPromotion(customer, "Special Offer: 25% OFF your next ticket!");

        // Verify data using toString()
        System.out.println("\n===== OBJECT DATA =====");
        System.out.println(admin);
        System.out.println(agent);
        System.out.println(customer);
    }
/*
    private static void sendPromotion(PromotionObserver observer, String message) {
        observer.receivePromotion(message);
    }*/
}
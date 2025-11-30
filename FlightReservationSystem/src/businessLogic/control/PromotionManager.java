package businessLogic.control;

import businessLogic.entity.PromotionObserver;
import businessLogic.entity.Customer;
import java.util.ArrayList;
import java.util.List;

/**
 * <<control>>
 * Singleton + Observer Pattern
 * Manages promotional notifications to subscribed customers
 */
public class PromotionManager {
    private static PromotionManager instance;
    private List<PromotionObserver> subscribers;

    // Private constructor for Singleton
    private PromotionManager() {
        subscribers = new ArrayList<>();
    }

    // Singleton getInstance
    public static synchronized PromotionManager getInstance() {
        if (instance == null) {
            instance = new PromotionManager();
        }
        return instance;
    }

    /**
     * Subscribe a customer to promotional notifications
     * Prevents duplicate subscriptions by checking userId
     */
    public void subscribe(PromotionObserver observer) {
        if (observer instanceof Customer) {
            Customer newCustomer = (Customer) observer;

            // Check if this userId is already subscribed
            boolean alreadySubscribed = false;
            for (PromotionObserver existing : subscribers) {
                if (existing instanceof Customer) {
                    Customer existingCustomer = (Customer) existing;
                    if (existingCustomer.getUserId() == newCustomer.getUserId()) {
                        alreadySubscribed = true;
                        break;
                    }
                }
            }

            if (!alreadySubscribed) {
                subscribers.add(observer);
                System.out.println("Subscriber added: " + newCustomer.getEmail() +
                        " (Total unique subscribers: " + subscribers.size() + ")");
            } else {
                System.out.println("User already subscribed: " + newCustomer.getEmail());
            }
        } else {
            // Non-customer observers (shouldn't happen, but handle it)
            if (!subscribers.contains(observer)) {
                subscribers.add(observer);
            }
        }
    }

    /**
     * Unsubscribe a customer from promotional notifications
     * Uses userId to find and remove the correct subscriber
     */
    public void unsubscribe(PromotionObserver observer) {
        if (observer instanceof Customer) {
            Customer customerToRemove = (Customer) observer;

            // Find and remove by userId
            subscribers.removeIf(existing -> {
                if (existing instanceof Customer) {
                    Customer existingCustomer = (Customer) existing;
                    return existingCustomer.getUserId() == customerToRemove.getUserId();
                }
                return false;
            });

            System.out.println("Subscriber removed: " + customerToRemove.getEmail() +
                    " (Total subscribers: " + subscribers.size() + ")");
        } else {
            subscribers.remove(observer);
            System.out.println("Subscriber removed. Total subscribers: " + subscribers.size());
        }
    }

    /**
     * Send promotional message to all subscribers
     * This would be called on the first day of each month
     */
    public void notifyAllSubscribers(String promotionMessage) {
        System.out.println("Sending promotion to " + subscribers.size() + " subscribers...");
        for (PromotionObserver observer : subscribers) {
            observer.receivePromotion(promotionMessage);
        }
    }

    /**
     * Simulate monthly promotion (for demonstration)
     */
    public void sendMonthlyPromotion() {
        String message = "MONTHLY SPECIAL: Get 20% off on all domestic flights! " +
                "Book now and save big. Offer valid until end of month.";
        notifyAllSubscribers(message);
    }

    /**
     * Get number of current subscribers
     */
    public int getSubscriberCount() {
        return subscribers.size();
    }

    /**
     * Check if a customer is already subscribed
     */
    public boolean isSubscribed(int userId) {
        for (PromotionObserver observer : subscribers) {
            if (observer instanceof Customer) {
                Customer customer = (Customer) observer;
                if (customer.getUserId() == userId) {
                    return true;
                }
            }
        }
        return false;
    }
}
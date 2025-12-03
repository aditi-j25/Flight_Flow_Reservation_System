// ============================================
// PromotionManager.java - Singleton + Observer
// ============================================
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

    private PromotionManager() {
        subscribers = new ArrayList<>();
    }

    public static synchronized PromotionManager getInstance() {
        if (instance == null) {
            instance = new PromotionManager();
        }
        return instance;
    }

    public void subscribe(PromotionObserver observer) {
        if (observer instanceof Customer) {
            Customer newCustomer = (Customer) observer;
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
                System.out.println("Subscriber added: " + newCustomer.getEmail());
            }
        } else {
            if (!subscribers.contains(observer)) {
                subscribers.add(observer);
            }
        }
    }

    public void unsubscribe(PromotionObserver observer) {
        if (observer instanceof Customer) {
            Customer customerToRemove = (Customer) observer;
            subscribers.removeIf(existing -> {
                if (existing instanceof Customer) {
                    Customer existingCustomer = (Customer) existing;
                    return existingCustomer.getUserId() == customerToRemove.getUserId();
                }
                return false;
            });
            System.out.println("Subscriber removed: " + customerToRemove.getEmail());
        } else {
            subscribers.remove(observer);
        }
    }

    public void notifyAllSubscribers(String promotionMessage) {
        System.out.println("Sending promotion to " + subscribers.size() + " subscribers...");
        for (PromotionObserver observer : subscribers) {
            observer.receivePromotion(promotionMessage);
        }
    }

    public void sendMonthlyPromotion() {
        String message = "MONTHLY SPECIAL: Get 20% off on all domestic flights! " +
                "Book now and save big. Offer valid until end of month.";
        notifyAllSubscribers(message);
    }

    public int getSubscriberCount() {
        return subscribers.size();
    }

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
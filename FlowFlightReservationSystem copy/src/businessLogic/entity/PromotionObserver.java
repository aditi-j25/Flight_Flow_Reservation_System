// ============================================
// PromotionObserver.java - Interface
// ============================================
package businessLogic.entity;

/**
 * Observer Pattern Interface
 * Implemented by users who want to receive promotional notifications
 */
public interface PromotionObserver {
    void receivePromotion(String promotionMessage);
}
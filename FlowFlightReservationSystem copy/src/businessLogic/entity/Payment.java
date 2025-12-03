// ============================================
// Payment.java
// ============================================
package businessLogic.entity;

import java.time.LocalDateTime;

/**
 * <<entity>>
 * Represents a payment transaction
 */
public class Payment {
    private int paymentId;
    private int bookingId;
    private double amount;
    private String paymentMethod;
    private String cardNumber;
    private LocalDateTime paymentDate;

    public Payment(int paymentId, int bookingId, double amount, String paymentMethod,
                   String cardNumber, LocalDateTime paymentDate) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.paymentDate = paymentDate;
    }

    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    // Getters
    public int getPaymentId() { return paymentId; }
    public int getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getCardNumber() { return cardNumber; }
    public LocalDateTime getPaymentDate() { return paymentDate; }

    // Setters
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + paymentId +
                ", amount=$" + amount +
                ", method=" + paymentMethod +
                ", card=" + getMaskedCardNumber() +
                '}';
    }
}
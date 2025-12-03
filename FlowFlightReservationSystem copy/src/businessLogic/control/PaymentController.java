// ============================================
// PaymentController.java
// ============================================
package businessLogic.control;

import businessLogic.entity.Payment;
import database.PaymentDAO;

/**
 * <<control>>
 * Handles payment processing
 */
public class PaymentController {
    private PaymentDAO paymentDAO;

    public PaymentController() {
        this.paymentDAO = new PaymentDAO();
    }

    public int processPayment(int bookingId, double amount, String paymentMethod, String cardNumber) {
        String maskedCardNumber = maskCardNumber(cardNumber);
        int paymentId = paymentDAO.createPayment(bookingId, amount, paymentMethod, maskedCardNumber);
        
        if (paymentId > 0) {
            System.out.println("Payment processed successfully: " + paymentId);
        }
        return paymentId;
    }

    public Payment getPaymentByBookingId(int bookingId) {
        return paymentDAO.getPaymentByBookingId(bookingId);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "0000";
        }
        return cardNumber.replaceAll("\\s", "").substring(cardNumber.length() - 4);
    }

    public boolean validateCardNumber(String cardNumber) {
        String cleaned = cardNumber.replaceAll("\\s", "");
        return cleaned.matches("\\d{16}");
    }

    public boolean validateCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }
}
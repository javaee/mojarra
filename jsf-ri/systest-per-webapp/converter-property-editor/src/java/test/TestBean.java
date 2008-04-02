/*
 *
 * Created on 10 agosto 2006, 16.45
 */

package test;

import javax.faces.model.SelectItem;


/**
 *
 * @author agori
 */
public class TestBean {

    private Payment payment;
    private Payment[] payments = new Payment[0];

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Payment[] getPayments() {
        return payments;
    }

    public void setPayments(Payment[] payments) {
        System.out.println(this + " setting payments to " + java.util.Arrays.asList(payments));
        this.payments = payments;
    }
}

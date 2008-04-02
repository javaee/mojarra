/*
 * Payment.java
 *
 * Created on 10 novembre 2005, 16.34
 *
 */

package test;

import java.math.BigDecimal;

public class Payment {
    
    private String value;
    private String label;
    
    
    public Payment() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

   
}

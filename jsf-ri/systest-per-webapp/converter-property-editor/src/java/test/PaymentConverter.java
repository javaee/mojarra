/*
 * PaymentConverter.java
 *
 * Created on 10 novembre 2005, 20.25
 *
 */

package test;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class PaymentConverter  implements Converter {
    
    public String getAsString(FacesContext context, UIComponent component,
            Object object) throws ConverterException {
        
        if(object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String) object;
        }
        Payment payment = (Payment) object;
        return payment.getValue();
    }
    
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) throws ConverterException {
        System.out.println("CONVERTER CALLED!!!!!!!!");
        
        if (value == null || value.equals("")) {
            return null;
        }
        
        Payment p = new Payment();
        p.setLabel("credit card " + value);
        p.setValue(value);
        
        return p;
    }
}

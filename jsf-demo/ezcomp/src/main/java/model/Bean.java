package model;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

public class Bean implements ActionListener, ValueChangeListener {
    
    private String message = "Hello World!";
    
    public String getMessage() {
        return message;
    }
    
    public String loginAction() {
        return "login";
    }

    public String backAction() {
        return "back";
    }

    public ActionListener getActionListener() {
        return this;
    }
    
    public ValueChangeListener getValueChangeListener() {
        return this;
    }

    public void processValueChange(ValueChangeEvent arg0) throws AbortProcessingException {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
                put("valueChangeListenerCalled", Boolean.TRUE);
    }
    
    

    public void processAction(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
                put("actionListenerCalled", Boolean.TRUE);
        
    }

}

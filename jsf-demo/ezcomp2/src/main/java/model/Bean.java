package model;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

public class Bean implements ActionListener, ValueChangeListener {

    public Bean() {
    }

    private String requestKey = "Hello World!";

    public void setRequestKey(String message) {
        this.requestKey = message;
    }
    
    public String getRequestKey() {
        return requestKey;
    }

    private String actionListenerKey = "actionListenerCalled";

    public void setActionListenerKey(String message) {
        this.actionListenerKey = message;
    }
    
    public String getActionListenerKey() {
        return actionListenerKey;
    }



    
    public String loginAction() {
        return "login";
    }

    public String backAction() {
        return "back";
    }

    public ActionListener getLoginEventListener() {
	Bean result = new Bean();
	result.setRequestKey("Login Event Listener");
	result.setActionListenerKey("loginEventCalled");
	return result;
    }

    public ActionListener getCancelEventListener() {
	Bean result = new Bean();
	result.setRequestKey("Cancel Event Listener");
	result.setActionListenerKey("cancelEventCalled");
	return result;
    }

    public ActionListener getAllEventsListener() {
	Bean result = new Bean();
	result.setRequestKey("All Events Listener");
	result.setActionListenerKey("allEventsCalled");
	return result;
    }
    
    public ValueChangeListener getUseridValueChangeListener() {
        Bean other = new Bean();
        other.setRequestKey("useridValueChangeListener");
        return ((ValueChangeListener) other);
    }

    public ValueChangeListener getPasswordValueChangeListener() {
        Bean other = new Bean();
        other.setRequestKey("passwordValueChangeListener");
        return ((ValueChangeListener) other);
    }
    
    // PENDING(edburns): This ends up being installed twice each time
    // the page says install it once, not correct.
    
    public void processValueChange(ValueChangeEvent arg0) throws AbortProcessingException {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
                put(getRequestKey(), Boolean.TRUE);
    }
    
    

    public void processAction(ActionEvent event) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().
	    put(getActionListenerKey(), getRequestKey());
        
    }

}

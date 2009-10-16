package model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

@ManagedBean(name = "bean")
@RequestScoped
public class Bean implements ActionListener {

    public ActionListener getLoginEventListener() {
        return new Bean();
    }

    public void processAction(ActionEvent e) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getRequestMap().put("actionCalled",
                Boolean.TRUE.toString());
    }

    private boolean isTransient;

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    public Object saveState(FacesContext context) {
        return null;
    }

    public void restoreState(FacesContext context, Object stateObj) {
    }

}

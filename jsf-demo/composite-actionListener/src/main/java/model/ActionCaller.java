package model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;


@ManagedBean(name="actionBean")
@RequestScoped
public class ActionCaller implements ActionListener {

    public ActionListener getListener() {
        return new ActionCaller();
    }

    public void processAction(ActionEvent e) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getRequestMap().put("actionCalled",
                Boolean.TRUE.toString());
    }

}

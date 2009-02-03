import javax.faces.model.ManagedBean;
import javax.faces.model.RequestScoped;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

@ManagedBean(name="bean")
@RequestScoped
public class Bean implements ActionListener {

    public ActionListener getLoginEventListener() {
	ActionListener result = new Bean();
	return result;
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

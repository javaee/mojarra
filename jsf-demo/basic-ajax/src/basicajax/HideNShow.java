package basicajax;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "hidenshow")
@SessionScoped
public class HideNShow {
    Boolean render = false;

    public Boolean getRender() {
        return render;
    }

    public void toggle(ActionEvent ae) {
        render = !render;
    }

}
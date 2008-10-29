package basicajax;

import javax.faces.model.ManagedBean;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "hidenshow", scope = "session")
public class HideNShow {
    Boolean render = false;

    public Boolean getRender() {
        return render;
    }

    public void toggle(ActionEvent ae) {
        render = !render;
    }

}
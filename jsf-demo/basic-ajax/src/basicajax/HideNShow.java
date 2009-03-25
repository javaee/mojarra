package basicajax;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
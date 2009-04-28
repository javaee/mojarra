package basicajax;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ManagedBean(name = "hidenshow")
@SessionScoped
public class HideNShow implements Serializable {


    private static final long serialVersionUID = -7299773608161438216L;

    Boolean render = false;
    
    public Boolean getRender() {
        return render;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void toggle(ActionEvent ae) {
        render = !render;
    }

}
package basicajax;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "echo")
@SessionScoped
public class Echo {
    String str = "hello";

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void process(ActionEvent ae) {
    }

    public void reset(ActionEvent ae) {
        str = "";
    }

}

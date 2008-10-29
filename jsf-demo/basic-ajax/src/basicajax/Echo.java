package basicajax;

import javax.faces.model.ManagedBean;

@ManagedBean(name = "echo")
@SessionScoped
public class Echo {
    String str = "marco";

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

}

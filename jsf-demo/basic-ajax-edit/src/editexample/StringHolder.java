package editexample;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;

@ManagedBean(name = "stringholder")
@SessionScoped
public class StringHolder {

    String str = "Text Edit Magic";

    public void setStr(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}

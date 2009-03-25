package editexample;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "stringholder")
@SessionScoped
public class StringHolder {

    private String str = "Text Edit Magic";

    public void setStr(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}

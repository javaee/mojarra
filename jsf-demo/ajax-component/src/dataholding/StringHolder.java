package dataholding;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "stringholder")
@SessionScoped
public class StringHolder implements Serializable {

    private static final long serialVersionUID = -2988876386472612330L;
    private String str = "Text Edit Magic";

    public void setStr(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
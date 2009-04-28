package ezcomp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;


@ManagedBean(name="navigation")
@RequestScoped
public class NavigationHandler implements Serializable {

    private static final long serialVersionUID = -3311984234775408121L;

    private String target = "index";

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String goNav() {
        return target;
    }

    public String goNav1() {
        return "nav1";
    }

    public String goNav2() {
        return "nav2";
    }
}

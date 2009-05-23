package switchlist;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


@ManagedBean(name="listholder1")
@SessionScoped
public class ListHolder1 implements ListHolder, Serializable {

    private static final long serialVersionUID = -4047970327214634942L;

    String[] list = null;
    Map<String, String> items = new LinkedHashMap<String, String>();

    {
        items.put("one", "one");
        items.put("two", "two");
        items.put("three", "three");
        items.put("four", "four");
    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public Map<String, String> getItems() {
        return items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }
}

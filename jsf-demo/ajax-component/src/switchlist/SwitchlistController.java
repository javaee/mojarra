package switchlist;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Map;
import java.io.Serializable;

@ManagedBean
@RequestScoped
public class SwitchlistController implements Serializable {

    private static final long serialVersionUID = -4002627066189080830L;

    ListHolder listholder1, listholder2;

    public String m1_2() {
        String[] list1 = listholder1.getList();
        Map<String, String> items2 = listholder2.getItems();
        Map<String, String> items1 = listholder1.getItems();
        if (list1 != null && list1.length > 0) {
            for (String item : list1) {
                items2.put(item, items1.remove(item));
            }
        }
        return null;
    }

    public String m2_1() {
        String[] list2 = listholder2.getList();
        Map<String, String> items2 = listholder2.getItems();
        Map<String, String> items1 = listholder1.getItems();
        if (list2 != null && list2.length > 0) {
            for (String item : list2) {
                items1.put(item, items2.remove(item));
            }
        }
        return null;
    }

    public String m1_2(ListHolder listholder1, ListHolder listholder2) {
        this.listholder1 = listholder1;
        this.listholder2 = listholder2;
        return m1_2();
    }

    public String m2_1(ListHolder listholder1, ListHolder listholder2) {
        this.listholder1 = listholder1;
        this.listholder2 = listholder2;
        return m2_1();
    }


    public void setListHolder1(ListHolder listholder1) {
        this.listholder1 = listholder1;
    }

    public void setListHolder2(ListHolder listholder2) {
        this.listholder2 = listholder2;
    }

}

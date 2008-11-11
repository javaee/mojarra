package example;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.model.SelectItem;
import javax.faces.event.ActionEvent;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;


@ManagedBean(name="listholder")
@SessionScoped
public class ListHolder {

    private Map<String, String> items1 = new LinkedHashMap<String, String>();

    private Map<String, String> items2 = new LinkedHashMap<String, String>();

    {
        items1.put("one", "one");
        items1.put("two", "two");
        items1.put("three", "three");
        items1.put("four", "four");
    }

    {
        items2.put("five", "five");
        items2.put("six", "six");
        items2.put("seven", "seven");
        items2.put("eight", "eight");
    }

    private String[] list1 = null;
    private String[] list2 = null;

    public String[] getList1() {
        return list1;
    }

    public void setList1(String list[]) {
        this.list1 = list;
    }

    public String[] getList2() {
        return list2;
    }

    public void setList2(String list[]) {
        this.list2 = list;
    }

    public Map getItems1() {
        return items1;    
    }

    public Map getItems2() {
        return items2;
    }

    public void move1to2(ActionEvent ae) {
        if (list1 != null && list1.length > 0) {
            for (String item : list1 ) {
                items2.put(item, items1.remove(item));
            }
        }
    }

    public void move2to1(ActionEvent ae) {
        if (list2 != null && list2.length > 0) {
            for (String item : list2 ) {
                items1.put(item, items2.remove(item));
            }
        }
    }

    public String getList1Cat() {
        return concat(list1);
    }

    public String getList2Cat() {
        return concat(list2);
    }
    
    private String concat(String[] arr) {
        String ret = "";
        if (arr == null) return ret;
        for (String val : arr) {
            ret = ret + val + " ";
        }
        return ret;
    }

}

package switchlist;

import java.util.Map;

public interface ListHolder {

    public String[] getList();

    public void setList(String[] list);

    public Map<String, String> getItems();

    public void setItems(Map<String, String> items);

}

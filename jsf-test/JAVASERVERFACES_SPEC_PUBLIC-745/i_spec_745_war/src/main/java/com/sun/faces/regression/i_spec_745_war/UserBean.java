package com.sun.faces.regression.i_spec_745_war;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class UserBean {
    
    private Map<String, String> items1 = new LinkedHashMap<String, String>();
    private List<String> list1;
 
    public List<String> getList1() {
        return list1;
    }

    public void setList1(List<String> list) {
        this.list1 = list;
    }

    public Map getItems1() {
        return items1;    
    }
    
    private Double doubleValue = Double.MAX_VALUE;

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }
    
}


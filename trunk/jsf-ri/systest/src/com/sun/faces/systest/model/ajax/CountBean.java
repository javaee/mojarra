package com.sun.faces.systest.model.ajax;

import javax.faces.model.ManagedBean;
import javax.faces.model.SessionScoped;
import javax.faces.event.ActionEvent;


@ManagedBean(name="ajaxcount")
@SessionScoped
public class CountBean {
    private Integer count1 = 0;
    private Integer count2 = 0;
    private Integer count3 = 0;
    private Integer count4 = 0;
    private Integer count5 = 0;

    public void resetCount(ActionEvent ae) {
        count1 = 0;
        count2 = 0;
        count3 = 0;
        count4 = 0;
        count5 = 0;
    }

    public Integer getCount1() {
        return count1++;
    }

    public Integer getCount2() {
        return count2++;
    }

    public Integer getCount3() {
        return count3++;
    }

    public Integer getCount4() {
        return count4++;
    }

    public Integer getCount5() {
        return count5++;
    }
}


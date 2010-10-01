package com.sun.faces.systest;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ProcessAsJspxBean {

    public String getProp() {
	return "Hello < World";
    }

}

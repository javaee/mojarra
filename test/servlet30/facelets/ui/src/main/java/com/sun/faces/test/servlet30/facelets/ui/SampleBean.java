package com.sun.faces.test.servlet30.facelets.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@RequestScoped
public class SampleBean implements Serializable {

	private List<KeyValue> list;
	
	public void method() {
        System.out.println(this.list);
    }

    @PostConstruct
    public void init() {
        this.list = new ArrayList<KeyValue>();
        this.list.add(new KeyValue("aaa", "AAA"));
        this.list.add(new KeyValue("bbb", "BBB"));
        this.list.add(new KeyValue("ccc", "CCC"));
    }

    public void setList(List<KeyValue> list) {
        this.list = list;
    }

    public List<KeyValue> getList() {
        return list;
    }
}

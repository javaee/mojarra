package com.sun.faces.test.agnostic.facelets.c;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "nestedForEachBean")
@ViewScoped
public class NestedForEachBean implements Serializable {

    List<List<NestedForEachItem>> items;

    @PostConstruct
    public void init() {
        items = new ArrayList();

        for (int i = 0; i < 3; i++) {
            List<NestedForEachItem> list = new ArrayList();
            list.add(new NestedForEachItem("list" + i + "item1"));
            list.add(new NestedForEachItem("list" + i + "item2"));
            list.add(new NestedForEachItem("list" + i + "item3"));
            items.add(list);
        }
    }

    public void add(List<NestedForEachItem> list) {
        list.add(new NestedForEachItem("new" + list.size()));
    }

    public List<List<NestedForEachItem>> getItems() {
        return items;
    }
}

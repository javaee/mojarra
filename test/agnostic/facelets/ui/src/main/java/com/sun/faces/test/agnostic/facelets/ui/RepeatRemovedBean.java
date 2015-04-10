package com.sun.faces.test.agnostic.facelets.ui;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean(name = "repeatRemovedBean")
public class RepeatRemovedBean implements Serializable {

    private static final long serialVersionUID = 1L;
    List<Integer> ints = new ArrayList<Integer>();
    ValueHolderList vl = new ValueHolderList();

    public RepeatRemovedBean() {
        ints.add(1);
    }

    public ValueHolderList getList() {
        return vl;
    }

    public void removeInt(int index) {
        ints.remove(index);
    }

    public class ValueHolder {

        int index;

        public ValueHolder(int index) {
            super();
            this.index = index;
        }

        public int getValue() {
            return ints.get(index);
        }

        public void setValue(int value) {
            ints.set(index, value);
        }
    }

    public class ValueHolderList extends AbstractList<ValueHolder> {

        public ValueHolderList() {
            super();
        }

        public ValueHolder get(int index) {
            return new ValueHolder(index);
        }

        @Override
        public int size() {
            return ints.size();
        }
    }
}

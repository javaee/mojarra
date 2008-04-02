/*
 * $Id: Bean62.java,v 1.1 2005/08/04 20:39:52 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.systest.model;

import java.util.ArrayList;
import javax.faces.model.ListDataModel;

/**
 *
 * @author edburns
 */
public class Bean62 {
    
    /** Creates a new instance of Bean62 */
    public Bean62() {
    }
    
    public Bean62(String label) {
        this.label = label;
    }

    /**
     * Holds value of property model.
     */
    private ListDataModel model;

    /**
     * Getter for property model.
     * @return Value of property model.
     */
    public ListDataModel getModel() {
        if (null == this.model) {
            ArrayList list = new ArrayList();
            if (isRoot()) {
                list.add(new Bean62(label + ".one"));
                list.add(new Bean62(label + ".two"));
                list.add(new Bean62(label + ".three"));
            }
            else {
                list.add("leaf1");
                list.add("leaf2");
                list.add("leaf3");
            }
            model = new ListDataModel(list);
        }
        return this.model;
    }

    /**
     * Setter for property model.
     * @param model New value of property model.
     */
    public void setModel(ListDataModel model) {

        this.model = model;
    }

    /**
     * Holds value of property root.
     */
    private boolean root = false;

    /**
     * Getter for property root.
     * @return Value of property root.
     */
    public boolean isRoot() {

        return this.root;
    }

    /**
     * Setter for property root.
     * @param root New value of property root.
     */
    public void setRoot(boolean root) {

        this.root = root;
    }

    /**
     * Holds value of property label.
     */
    private String label;

    /**
     * Getter for property label.
     * @return Value of property label.
     */
    public String getLabel() {

        return this.label;
    }

    /**
     * Setter for property label.
     * @param label New value of property label.
     */
    public void setLabel(String label) {

        this.label = label;
    }
    
    public String action() {
        Bean62 yyyInstance = (Bean62) this.model.getRowData();
        Object wwwInstance = yyyInstance.getModel().getRowData();
        
        setCurStatus(wwwInstance);
        
        return null;
    }

    /**
     * Holds value of property curStatus.
     */
    private Object curStatus;

    /**
     * Getter for property curStatus.
     * @return Value of property curStatus.
     */
    public Object getCurStatus() {

        return this.curStatus;
    }

    /**
     * Setter for property curStatus.
     * @param curStatus New value of property curStatus.
     */
    public void setCurStatus(Object curStatus) {

        this.curStatus = curStatus;
    }
}

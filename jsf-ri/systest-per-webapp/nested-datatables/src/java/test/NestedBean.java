/*
 * $Id: NestedBean.java,v 1.1 2005/08/03 02:23:01 edburns Exp $
 */
/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package test;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author edburns
 */
public class NestedBean {
    
    /** Creates a new instance of NestedBean */
    public NestedBean() {
    }
    
    private String id;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void executeLink(ActionEvent event) {
        String whichLink = "You clicked on link: "+id;
        System.out.println(whichLink);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("whichLink", whichLink);
    }
    
}

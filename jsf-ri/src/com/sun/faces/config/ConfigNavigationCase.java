/*
 * $Id: ConfigNavigationCase.java,v 1.1 2003/05/01 07:20:17 rkitain Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p>Config Bean for a Navigation Rule .</p>
 */
public class ConfigNavigationCase {

    private String fromTreeId = null;
    private String fromActionRef = null;
    private String fromOutcome = null;
    private String toTreeId = null;

    public String getFromTreeId() {
        return (this.fromTreeId);
    }
    public void setFromTreeId(String fromTreeId) {
        this.fromTreeId = fromTreeId;
    }

    public String getFromActionRef() {
        return (this.fromActionRef);
    }
    public void setFromActionRef(String fromActionRef) {
        this.fromActionRef= fromActionRef;
    }

    public String getFromOutcome() {
        return (this.fromOutcome);
    }
    public void setFromOutcome(String fromOutcome) {
        this.fromOutcome = fromOutcome;
    }

    public String getToTreeId() {
        return (this.toTreeId);
    }
    public void setToTreeId(String toTreeId) {
        this.toTreeId = toTreeId;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("FROM TREE ID:"+getFromTreeId());
        sb.append("\nFROM ACTION REF:"+getFromActionRef());
        sb.append("\nFROM OUTCOME:"+getFromOutcome());
        sb.append("\nTO TREE ID:"+getToTreeId());
        return sb.toString();
    }
}

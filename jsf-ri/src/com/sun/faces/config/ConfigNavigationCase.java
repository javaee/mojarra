/*
 * $Id: ConfigNavigationCase.java,v 1.2 2003/08/22 16:49:41 eburns Exp $
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

    private String fromViewId = null;
    private String fromActionRef = null;
    private String fromOutcome = null;
    private String toViewId = null;

    public String getFromViewId() {
        return (this.fromViewId);
    }
    public void setFromViewId(String fromViewId) {
        this.fromViewId = fromViewId;
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

    public String getToViewId() {
        return (this.toViewId);
    }
    public void setToViewId(String toViewId) {
        this.toViewId = toViewId;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("FROM VIEW ID:"+getFromViewId());
        sb.append("\nFROM ACTION REF:"+getFromActionRef());
        sb.append("\nFROM OUTCOME:"+getFromOutcome());
        sb.append("\nTO VIEW ID:"+getToViewId());
        return sb.toString();
    }
}

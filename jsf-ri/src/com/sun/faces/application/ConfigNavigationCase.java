/*
 * $Id: ConfigNavigationCase.java,v 1.11 2007/04/22 21:41:04 rlubke Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.application;


/**
 * <p>Config Bean for a Navigation Rule .</p>
 */
public class ConfigNavigationCase {

    private String fromViewId = null;
    private String fromAction = null;
    private String fromOutcome = null;
    private String toViewId = null;
    private String key = null;
    private boolean redirect;


    public ConfigNavigationCase(String fromViewId,
                                String fromAction,
                                String fromOutcome,
                                String toViewId,
                                boolean redirect) {
        this.fromViewId = fromViewId;
        this.fromAction = fromAction;
        this.fromOutcome = fromOutcome;
        this.toViewId = toViewId;
        this.key = fromViewId
                   + ((fromAction == null) ? "-" : fromAction)
                   + ((fromOutcome == null) ? "-" : fromOutcome);
        this.redirect = redirect;
    }


    public String getFromViewId() {
        return (this.fromViewId);
    }


    public void setFromViewId(String fromViewId) {
        this.fromViewId = fromViewId;
    }


    public String getFromAction() {
        return (this.fromAction);
    }


    public void setFromAction(String fromAction) {
        this.fromAction = fromAction;
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


    public boolean hasRedirect() {
        return redirect;
    }


    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }


    /**
     * The "key" is defined as the combination of
     * <code>from-view-id</code><code>from-action</code>
     * <code>from-outcome</code>.
     */
    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("from-view-id=").append(getFromViewId());
        sb.append(" from-action=").append(getFromAction());
        sb.append(" from-outcome=").append(getFromOutcome());
        sb.append(" to-view-id=").append(getToViewId());
        sb.append(" redirect=").append(hasRedirect());
        return sb.toString();
    }
}

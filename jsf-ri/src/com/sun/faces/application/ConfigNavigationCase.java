/*
 * $Id: ConfigNavigationCase.java,v 1.12 2007/04/27 22:00:53 ofung Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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

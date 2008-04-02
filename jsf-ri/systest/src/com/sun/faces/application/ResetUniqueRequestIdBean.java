/*
 * $Id: ResetUniqueRequestIdBean.java,v 1.3 2005/08/22 22:10:31 ofung Exp $
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

import javax.faces.context.FacesContext;
import com.sun.faces.RIConstants;
import org.apache.commons.collections.LRUMap;

public class ResetUniqueRequestIdBean extends Object {

    public ResetUniqueRequestIdBean() {}

    protected String reset = "Unique Id Counter Has Been Reset";
    public String getReset() {
	FacesContext context = FacesContext.getCurrentInstance();
	LRUMap lruMap = new LRUMap(15);
	context.getExternalContext().getSessionMap().put(RIConstants.LOGICAL_VIEW_MAP, lruMap);
	((StateManagerImpl)context.getApplication().getStateManager()).requestIdSerial = (char) -1;
	return reset;
    }

    public void setReset(String newReset) {
	reset = newReset;
    }

}

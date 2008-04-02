/*
 * $Id: TestNavigationHandler.java,v 1.2 2003/07/08 15:38:41 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestNavigationHandler.java

package com.sun.faces;

import com.sun.faces.application.NavigationHandlerImpl;

import java.util.Map;

public class TestNavigationHandler extends NavigationHandlerImpl {

    public Map getCaseListMap() {
        return caseListMap;
    }
}

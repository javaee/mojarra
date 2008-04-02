/*
 * $Id: MockFacesContextFactoryExtender.java,v 1.5 2005/10/19 19:51:14 edburns Exp $
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

package javax.faces.mock;

import com.sun.faces.mock.MockFacesContextFactory;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContextFactory;

public class MockFacesContextFactoryExtender extends MockFacesContextFactory {
    public MockFacesContextFactoryExtender() {}
    public MockFacesContextFactoryExtender(FacesContextFactory oldImpl) {
	System.setProperty(FactoryFinder.FACES_CONTEXT_FACTORY, 
			   this.getClass().getName());
	System.setProperty("oldImpl", oldImpl.getClass().getName());
    }
}


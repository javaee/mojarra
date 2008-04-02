/*
 * $Id: MockRenderKitFactory.java,v 1.6 2005/08/22 22:08:25 ofung Exp $
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


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;


public class MockRenderKitFactory extends RenderKitFactory {

    public MockRenderKitFactory(RenderKitFactory oldImpl) {
	System.setProperty(FactoryFinder.RENDER_KIT_FACTORY, 
			   this.getClass().getName());
    }
    public MockRenderKitFactory() {}
    
    private Map renderKits = new HashMap();


    public void addRenderKit(String renderKitId, RenderKit renderKit) {
        if ((renderKitId == null) || (renderKit == null)) {
            throw new NullPointerException();
        }
        synchronized (renderKits) {
            if (renderKits.containsKey(renderKitId)) {
                throw new IllegalArgumentException(renderKitId);
            }
            renderKits.put(renderKitId, renderKit);
        }
    }


    public RenderKit getRenderKit(FacesContext context, String renderKitId) {
        if (renderKitId == null) {
            throw new NullPointerException();
        }
        synchronized (renderKits) {
            RenderKit renderKit = (RenderKit) renderKits.get(renderKitId);
            if (renderKit == null) {
                throw new IllegalArgumentException(renderKitId);
            }
            return (renderKit);
        }
    }


    public Iterator getRenderKitIds() {
        synchronized (renderKits) {
            return (renderKits.keySet().iterator());
        }
    }


}

/*
 * $Id: BaseRenderer.java,v 1.9 2003/12/17 15:19:07 rkitain Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package components.renderkit;


import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Iterator;

/**
 * <p>Convenient base class for <code>Renderer</code> implementations.</p>
 */

public abstract class BaseRenderer extends Renderer {

    public static final String BUNDLE_ATTR = "com.sun.faces.bundle";

    public String convertClientId(FacesContext context, String clientId) {          
	return clientId;
    }

    protected String getKeyAndLookupInBundle(FacesContext context,
					     UIComponent component, 
					     String keyAttr) throws MissingResourceException{
	String key = null, bundleName = null;
	ResourceBundle bundle = null;

        key = (String) component.getAttributes().get(keyAttr);
        bundleName = (String)component.getAttributes().get(BUNDLE_ATTR);

        // if the bundleName is null for this component, it might have
        // been set on the root component.
        if ( bundleName == null ) {
            UIComponent root = context.getViewRoot();

            bundleName = (String)root.getAttributes().get(BUNDLE_ATTR);
        }
	// verify our component has the proper attributes for key and bundle.
	if (null == key || null == bundleName) {
	    throw new MissingResourceException("Can't load JSTL classes", 
					       bundleName, key);
	}
	
	// verify the required Class is loadable
	// PENDING(edburns): Find a way to do this once per ServletContext.
	if (null == Thread.currentThread().getContextClassLoader().
	    getResource("javax.servlet.jsp.jstl.fmt.LocalizationContext")){
	    Object [] params = { "javax.servlet.jsp.jstl.fmt.LocalizationContext" };
	    throw new MissingResourceException("Can't load JSTL classes", 
					       bundleName, key);
	}
	
	// verify there is a ResourceBundle in scoped namescape.
	javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
	if (null == (locCtx = (javax.servlet.jsp.jstl.fmt.LocalizationContext) 
            (Util.getValueBinding(bundleName)).getValue(context)) ||
	    null == (bundle = locCtx.getResourceBundle())) {
	    throw new MissingResourceException("Can't load ResourceBundle ", 
                    bundleName, key);
	}
	
	return bundle.getString(key);
    }


    protected void encodeRecursive(FacesContext context, UIComponent component)
        throws IOException {

        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);

    }


}

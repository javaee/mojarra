/*
 * $Id: BaseRenderer.java,v 1.2 2005/08/22 22:08:54 ofung Exp $
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

package components.renderkit;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
                                             String keyAttr)
        throws MissingResourceException {
        String key = null, bundleName = null;
        ResourceBundle bundle = null;

        key = (String) component.getAttributes().get(keyAttr);
        bundleName = (String) component.getAttributes().get(BUNDLE_ATTR);

        // if the bundleName is null for this component, it might have
        // been set on the root component.
        if (bundleName == null) {
            UIComponent root = context.getViewRoot();

            bundleName = (String) root.getAttributes().get(BUNDLE_ATTR);
        }
        // verify our component has the proper attributes for key and bundle.
        if (null == key || null == bundleName) {
            throw new MissingResourceException("Can't load JSTL classes",
                                               bundleName, key);
        }

        // verify the required Class is loadable
        // PENDING(edburns): Find a way to do this once per ServletContext.
        if (null == Thread.currentThread().getContextClassLoader().
            getResource("javax.servlet.jsp.jstl.fmt.LocalizationContext")) {
            Object[] params = {
                "javax.servlet.jsp.jstl.fmt.LocalizationContext"
            };
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

/*
 * $Id: StylesheetRenderer.java,v 1.1.36.2 2007/04/27 21:27:13 ofung Exp $
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

package components.renderkit;


import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import java.io.IOException;


/**
 * <p>Render a stylesheet link for the value of our component's
 * <code>path</code> attribute, prefixed by the context path of this
 * web application.</p>
 */

public class StylesheetRenderer extends BaseRenderer {


    public boolean supportsComponentType(UIComponent component) {
        return (component instanceof UIOutput);
    }


    public void decode(FacesContext context, UIComponent component) {
    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        ;
    }


    public void encodeChildren(FacesContext context, UIComponent component)
        throws IOException {
        ;
    }


    /**
     * <p>Render a relative HTML <code>&lt;link&gt;</code> element for a
     * <code>text/css</code> stylesheet at the specified context-relative
     * path.</p>
     *
     * @param context   FacesContext for the request we are processing
     * @param component UIComponent to be rendered
     *
     * @throws IOException          if an input/output error occurs while rendering
     * @throws NullPointerException if <code>context</code>
     *                              or <code>component</code> is null
     */
    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }

        ResponseWriter writer = context.getResponseWriter();
        String contextPath = context.getExternalContext()
            .getRequestContextPath();
        writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");

        writer.write(contextPath);
        writer.write((String) component.getAttributes().get("path"));
        writer.write("\">");

    }


}

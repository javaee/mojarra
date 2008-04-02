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

package j2meDemo.renderkit;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class FormRenderer extends Renderer {
    public void encodeBegin(FacesContext context,
                            UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.write("form=" + component.getId() + "\n");

        Iterator ids = context.getClientIdsWithMessages();
        while (ids.hasNext()) {
            String id = (String) ids.next();
            Iterator messages = context.getMessages(id);
            String msg = null;
            while (messages.hasNext()) {
                FacesMessage m = (FacesMessage) messages.next();
                if (msg == null) {
                    msg = m.getSummary();
                } else {
                    msg = msg + "," + m.getSummary();
                }
            }
            if (msg != null) {
                writer.write("messages");
                if (id != null) {
                    writer.write("." + id);
                }
                writer.write("=" + URLEncoder.encode(msg, "UTF8") + "\n");
            }
        }
    }

    public void decode(FacesContext context, UIComponent component) {
        Map map = context.getExternalContext().getRequestParameterMap();
        ((UIForm) component).setSubmitted(
              component.getId().equals(map.get("form")));
    }
}

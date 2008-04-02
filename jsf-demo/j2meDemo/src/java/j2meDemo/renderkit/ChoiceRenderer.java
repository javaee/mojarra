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

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import j2meDemo.util.Util;

public class ChoiceRenderer extends Renderer {
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        EditableValueHolder input = (EditableValueHolder) component;
        String id = component.getId();
        List items = Util.getSelectItems(component);
        String value = input.getValue().toString();
        String label = findLabel(items, value);
        writer.write(id + "=" + URLEncoder.encode(label, "UTF8") + "\n");
        for (int i = 0; i < items.size(); i++) {
            SelectItem item = (SelectItem) items.get(i);
            writer.write(id
                         + ".label."
                         + i
                         + "="
                         + URLEncoder.encode(item.getLabel(), "UTF8")
                         + "\n");
        }
    }

    public void decode(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            return;
        }

        String id = component.getId();
        Map requestMap
              = context.getExternalContext().getRequestParameterMap();
        if (requestMap.containsKey(id)
            && component instanceof ValueHolder) {
            String label = (String) requestMap.get(id);
            List items = Util.getSelectItems(component);
            Object value = findValue(items, label);
            ((ValueHolder) component).setValue(value);
        }
    }

    private static Object findValue(List list, String label) {
        for (int i = 0; i < list.size(); i++) {
            SelectItem item = (SelectItem) list.get(i);
            if (item.getLabel().equals(label)) {
                return item.getValue();
            }
        }
        return null;
    }

    private static String findLabel(List list, Object value) {
        for (int i = 0; i < list.size(); i++) {
            SelectItem item = (SelectItem) list.get(i);
            if (item.getValue().equals(value)) {
                return item.getLabel();
            }
        }
        return null;
    }
}



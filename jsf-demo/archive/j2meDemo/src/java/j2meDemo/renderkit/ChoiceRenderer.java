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



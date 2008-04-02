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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class TextRenderer extends Renderer {
   public void encodeBegin(FacesContext context, UIComponent component)
      throws IOException {
      ResponseWriter writer = context.getResponseWriter();
      String id = component.getId();
      String value = "" + ((ValueHolder) component).getValue();
      writer.write(id + "=" + URLEncoder.encode(value, "UTF8") + "\n");
   }

   public void decode(FacesContext context, UIComponent component) {
      if (context == null || component == null) return;

      String id = component.getId();
      Map requestMap 
         = context.getExternalContext().getRequestParameterMap();
      if (requestMap.containsKey(id) 
         && component instanceof ValueHolder) {
         String newValue = (String) requestMap.get(id);
         ((ValueHolder) component).setValue(newValue);
      }
   }
}

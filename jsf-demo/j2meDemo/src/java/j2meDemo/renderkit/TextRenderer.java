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

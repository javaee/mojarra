package j2meDemo.renderkit;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.render.Renderer;

public class ChoiceRenderer extends Renderer {
   public void encodeEnd(FacesContext context, UIComponent component)
      throws IOException {
      ResponseWriter writer = context.getResponseWriter();
      EditableValueHolder input = (EditableValueHolder) component;
      String id = component.getId();
      List items = j2meDemo.util.Renderers.getSelectItems(component);
      String value = input.getValue().toString();
      String label = findLabel(items, value);
      writer.write(id + "=" + URLEncoder.encode(label, "UTF8") + "\n");
      for (int i = 0; i < items.size(); i++) {
         SelectItem item = (SelectItem) items.get(i);
         writer.write(id + ".label." + i 
            + "=" + URLEncoder.encode(item.getLabel(), "UTF8") + "\n");
      }
   }

   public void decode(FacesContext context, UIComponent component) {
      if (context == null || component == null) return;

      String id = component.getId();
      Map requestMap 
         = context.getExternalContext().getRequestParameterMap();
      if (requestMap.containsKey(id) 
         && component instanceof ValueHolder) {
         String label = (String) requestMap.get(id);
         List items = j2meDemo.util.Renderers.getSelectItems(component); 
         Object value = findValue(items, label);
         ((ValueHolder) component).setValue(value);
      }
   }

   private static Object findValue(List list, String label) {
      for (int i = 0; i < list.size(); i++) {
         SelectItem item = (SelectItem) list.get(i);
         if (item.getLabel().equals(label)) return item.getValue();
      }
      return null;
   }

   private static String findLabel(List list, Object value) {    
      for (int i = 0; i < list.size(); i++) {
         SelectItem item = (SelectItem) list.get(i);
         if (item.getValue().equals(value)) return item.getLabel();
      }
      return null;
   }
}



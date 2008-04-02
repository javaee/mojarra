package j2meDemo.renderkit;

import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

public class CommandRenderer extends Renderer {
   public void decode(FacesContext context, UIComponent component) {
      if (context == null || component == null) return;

      String id = component.getId();
      Map requestMap 
         = context.getExternalContext().getRequestParameterMap();
      if (requestMap.containsKey(id)) {
         component.queueEvent(new ActionEvent(component));
      }
   }
}

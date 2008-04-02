package j2meDemo.taglib;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GameBoardTag extends UIComponentTag { 
    private String value;
   
    public void setValue(String newValue) { value = newValue; }

    public void setProperties(UIComponent component) { 
        super.setProperties(component); 
      
        if (value != null) {
            if (isValueReference(value)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application application = context.getApplication();
                ValueBinding vb = application.createValueBinding(value);
                component.setValueBinding("value", vb);
            } else {
                component.getAttributes().put("value", value);
            }
        }
    } 

    public void release() {
        value = null;
    }

    public String getRendererType() { 
        return "j2meDemo.renderkit.GameBoard"; 
    } 
    public String getComponentType() { 
        return "j2meDemo.renderkit.GameBoard"; 
    }  
}

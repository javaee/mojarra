package headbody;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class MyComponentTag extends UIComponentELTag {

    public String getRendererType() {
        return null;
    }

    public String getComponentType() {
        return "headbody.MyComponent";
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
    }

}

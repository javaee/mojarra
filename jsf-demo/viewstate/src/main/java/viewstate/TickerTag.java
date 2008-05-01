package viewstate;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class TickerTag extends UIComponentELTag {

    public String getRendererType() {
        return null;
    }

    public String getComponentType() {
        return "viewstate.Ticker";
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
    }

}

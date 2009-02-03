package stock;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class StockTableTag extends UIComponentELTag {

    public String getRendererType() {
        return null;
    }

    public String getComponentType() {
        return "stock.StockTable";
    }

    @Override
    protected void setProperties(UIComponent component) {

        super.setProperties(component);
        if (border != null) {
            component.setValueExpression("border", border);
        }
        if (cellspacing != null) {
            component.setValueExpression("cellspacing", cellspacing);
        }
        if (columns != null) {
            component.setValueExpression("columns", columns);
        }
        if (style != null) {
            component.setValueExpression("style", style);
        }
        if (styleClass != null) {
            component.setValueExpression("styleClass", styleClass);
        }
    }

    private ValueExpression border;
    public void setBorder(javax.el.ValueExpression border) {
        this.border = border;
    }

    // PROPERTY: cellspacing
    private ValueExpression cellspacing;
    public void setCellspacing(javax.el.ValueExpression cellspacing) {
        this.cellspacing = cellspacing;
    }

    // PROPERTY: columns
    private ValueExpression columns;
    public void setColumns(javax.el.ValueExpression columns) {
        this.columns = columns;
    }

    // PROPERTY: style
    private ValueExpression style;
    public void setStyle(javax.el.ValueExpression style) {
        this.style = style;
    }

    // PROPERTY: styleClass
    private ValueExpression styleClass;
    public void setStyleClass(javax.el.ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

}

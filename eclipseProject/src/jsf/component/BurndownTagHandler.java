package jsf.component;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class BurndownTagHandler extends UIComponentELTag {

	private ValueExpression value;

	@Override
	public String getComponentType() {
		return "BURNDOWN_OUTPUT";
	}

	@Override
	public String getRendererType() {
		return "BURNDOWN_RENDERER";
	}

	protected void setProperties(UIComponent component) {
		super.setProperties(component);
		component.setValueExpression("sprint", this.value);
	}

	public void setValue(ValueExpression value) {
		this.value = value;
	}

	public void release() {
		super.release();
		value = null;
	}
}

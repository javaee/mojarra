package jsf.component;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

public class BurndownTagHandler extends UIComponentELTag {

	private ValueExpression sprint;

	public BurndownTagHandler(){
		System.out.printf("%s %n%n",this.getClass().getSimpleName());
	}
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
		component.setValueExpression("sprint", this.sprint);
	}

	public void setSprint(ValueExpression value) {
		this.sprint = value;
		System.out.printf("%s", value.getExpressionString());
	}

	public void release() {
		super.release();
		this.sprint = null;
	}
}

package headbody;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;

@ResourceDependency (name="mycomponent.js", library="mylib", target="head")
public class MyComponent extends UIOutput {

    public static final String COMPONENT_TYPE = "headbody.MyComponent";

    public MyComponent() {
        setRendererType(null);
    }

    public String getFamily() {
        return COMPONENT_TYPE;
    }
}

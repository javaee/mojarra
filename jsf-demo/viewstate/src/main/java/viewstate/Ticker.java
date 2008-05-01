package viewstate;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;

@ResourceDependencies( {
    @ResourceDependency (name="ajax.js", library="javax.faces", target="head"),
    @ResourceDependency (name="viewstate.js", target="head"),
    @ResourceDependency (name="ticker.js", target="head"),
    @ResourceDependency (name="stylesheet.css") } )

public class Ticker extends UIOutput {

    public static final String COMPONENT_TYPE = "viewstate.Ticker";

    public Ticker() {
        setRendererType(null);
    }

    public String getFamily() {
        return COMPONENT_TYPE;
    }
}

package stock;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIPanel;

@ResourceDependencies( {
    @ResourceDependency (name="prototype.js", library="javax.faces", target="head"),
    @ResourceDependency (name="com_sun_faces_jsf.js", library="javax.faces", target="head"),
    @ResourceDependency (name="jsf.js", library="javax.faces", target="head"),
    @ResourceDependency (name="stock-query.js", target="head") } )

public class StockTable extends UIPanel{

    public static final String COMPONENT_TYPE = "stock.StockTable";

    public StockTable() {
        setRendererType(null);
    }

    public String getFamily() {
        return COMPONENT_TYPE;
    }
}

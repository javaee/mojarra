import javax.faces.component.UINamingContainer;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@ResourceDependencies( [
  @ResourceDependency(library="ezcomp", name="css_master.css"),
  @ResourceDependency(library="ezcomp", name="layout.css"),
  @ResourceDependency(library="ezcomp", name="typography.css"),
  @ResourceDependency(library="ezcomp", name="colorAndMedia.css"),
  @ResourceDependency(library="ezcomp", name="table2.css"),
  @ResourceDependency(library="ezcomp", name="commontaskssection.css"),
  @ResourceDependency(library="ezcomp", name="progressBar.css"),
  @ResourceDependency(library="ezcomp", name="css_ns6up.css")
                       ])
public class loginPanel extends UINamingContainer {
    
    public void processUpdates(FacesContext context) {
        
        ValueHolder username = this.findComponent("username");
        ValueHolder password = this.findComponent("password");
        
        context.getExternalContext().getRequestMap().put("groovyCalled", 
                                                         username.getValue());

        super.processUpdates(context);

    } 
    
    public String getFamily() {
        return "ezcomp.LoginPanel";
    }
    
}

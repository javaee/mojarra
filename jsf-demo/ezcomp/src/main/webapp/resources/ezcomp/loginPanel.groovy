import javax.faces.component.UINamingContainer;
import javax.faces.component.ValueHolder;
import javax.faces.component.CompositeComponent;
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
  @ResourceDependency(library="ezcomp", name="css_ns6up.css"),
  @ResourceDependency(library="ezcomp", name="LoginProductName.png"),
  @ResourceDependency(library="ezcomp", name="login-backimage.jpg"),
  @ResourceDependency(library="ezcomp", name="dot.gif"),
  @ResourceDependency(library="ezcomp", name="primary-enabled.gif"),
  @ResourceDependency(library="ezcomp", name="gradlogsides.jpg"),
  @ResourceDependency(library="ezcomp", name="gradlogbot.jpg"),
  @ResourceDependency(library="ezcomp", name="primary-roll.gif")
                       ])
public class loginPanel extends UINamingContainer implements CompositeComponent {
    
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

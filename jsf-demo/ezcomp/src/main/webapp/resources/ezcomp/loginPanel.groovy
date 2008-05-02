import javax.faces.component.UINamingContainer;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

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

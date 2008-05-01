import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class loginPanel extends UIComponentBase {

  public void decode(FacesContext context) {
    context.getExternalContext().getRequestMap().put("groovyCalled", 
                                                     Boolean.TRUE);
  } 
    
}

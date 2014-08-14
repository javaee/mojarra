package hello

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.ResponseWriter;

public class AgeComponent extends UIInput {
  public AgeComponent() {
    System.out.println("AgeComponent initialized...");
  }

   
  public void encodeBegin(FacesContext context) throws IOException {
     super.encodeBegin(context);
      ExternalContext extContext = context.getExternalContext();
      Map<String, String> requestParamMap = extContext.getRequestParameterMap();
      ResponseWriter out = context.getResponseWriter();
      out.startElement("p", this);
      out.writeText("@tstamp@", this, "prefix");
      out.endElement("p");


  }
  
  
}
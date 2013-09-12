package hello

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;

public class MessageRenderer extends Renderer {

  public MessageRenderer() {
    System.out.println("MessageRenderer initialized...");
  }

  public String getFamily()
  {
    return "HelloFamily";
  }

  @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
      String hellomsg = (String)component.getAttributes().get("hellomsg");
      ResponseWriter writer = context.getResponseWriter();

      System.out.println("MessageRenderer : hellomsg = " + hellomsg);
      writer.startElement("h3",component);
      if(hellomsg != null) {
        System.out.println("MessageRenderer : hellomsg is not null...");
        writer.writeText(hellomsg, "hellomsg");
      } else
        writer.writeText("could not retrieve the message from ManagedBean HelloBean...", null);
      writer.endElement("h3");
      writer.startElement("p", component);
      writer.writeText(" Today is: " + new Date(), null);
      writer.endElement("p");
    }

}
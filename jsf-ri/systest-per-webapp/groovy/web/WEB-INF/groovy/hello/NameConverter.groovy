package hello

 import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import com.sun.faces.systest.Name;

public class NameConverter implements Converter {

       private String message = "04:48";


    public NameConverter() {
        System.out.println("NameConverter instantiated " + message);
    }
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
      com.sun.faces.systest.Name name = new com.sun.faces.systest.Name(value);
      return name.getFirst() + " " + name.getLast();

    }

    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {
      return value.toString();
    }

  
}

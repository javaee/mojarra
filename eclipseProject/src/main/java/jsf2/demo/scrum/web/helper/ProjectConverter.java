package jsf2.demo.scrum.web.helper;

import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import jsf2.demo.scrum.model.entities.Project;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@FacesConverter(forClass = Project.class)
public class ProjectConverter implements Converter {

    private static Map<Long, Project> cache = new HashMap<Long, Project>();

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null && value.equals("0")) {
            return null;
        }
        try {
            return cache.get(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new ConverterException("Invalid value: " + value, e);
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object object) {
        Project project = (Project) object;
        Long id = project.getId();
        if (id != null) {
            cache.put(id, project);
            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
}

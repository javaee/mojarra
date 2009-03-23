package jsf.converter;

import java.util.TimeZone;

import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;

/**
 * Default date converter for this app
 * @author Vinny
 * @ since 2008
 *
 */
public class AppDateTimeConverter extends DateTimeConverter {

	public AppDateTimeConverter() {
		super();
		setTimeZone(TimeZone.getDefault());
		this.setDateStyle("short");
//		setPattern("dd/MM/yyyy");
		this.setLocale(FacesContext.getCurrentInstance().getApplication().getDefaultLocale());
	}
}
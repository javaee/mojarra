package beanvalidation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.ManagedBean;
import javax.faces.model.RequestScoped;

/**
 * @author Dan Allen
 */
@RequestScoped @ManagedBean
public class DefaultValidatorsReport {

	private List<String> validatorIds;

	@PostConstruct
	public void onCreate() {
		validatorIds = new ArrayList<String>();
		FacesContext ctx = FacesContext.getCurrentInstance();
		for (Iterator<String> it = ctx.getApplication().getDefaultValidatorIds(); it.hasNext();) {
			validatorIds.add(it.next());
		}
	}

	public List<String> getValidatorIds() {
		return validatorIds;
	}
}

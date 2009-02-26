package beanvalidation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.Application;
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
                Application app = ctx.getApplication();
                Map<String, String> defaultValidators = app.getDefaultValidatorInfo();
                
		for (String cur : defaultValidators.keySet()) {
			validatorIds.add(cur);
		}
	}

	public List<String> getValidatorIds() {
		return validatorIds;
	}
}

package jsf.util;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import services.ServicesException;

/**
 * Utility methods
 * @author Vinny
 * @ since 2008
 *
 */
public class FacesUtil {

	private static FacesUtil instance;
	public static synchronized FacesUtil getInstance() {
		if (instance == null) instance = new FacesUtil();
		return instance;
	}
	
	private FacesUtil(){}

	public void addInfoMessage(String $id, String $summary, String $detail) {
		this.addMessage($id, FacesMessage.SEVERITY_INFO, $summary, $detail);
	}

	public void addGlobalInfoMessage(String $summary, String $detail) {
		this.addGlobalMessage(FacesMessage.SEVERITY_INFO, $summary, $detail);
	}
	
	private void addGlobalMessage(FacesMessage.Severity $severity, String $summary, String $detail){
		FacesContext.getCurrentInstance().addMessage(null, this.createMessage($severity, $summary, $detail));
	}
	
	private void addMessage(String $id, FacesMessage.Severity $severity, String $summary, String $detail){
		FacesContext.getCurrentInstance().addMessage($id, this.createMessage($severity, $summary, $detail));
	}

	public FacesMessage createErrorMessage(String $summary, String $detail) {
		return this.createMessage(FacesMessage.SEVERITY_ERROR, $summary, $detail);
	}

	public FacesMessage createInfoMessage(String $summary, String $detail) {
		return this.createMessage(FacesMessage.SEVERITY_INFO, $summary, $detail);
	}
	
	private FacesMessage createMessage(FacesMessage.Severity $severity, String $summary, String $detail){
		return new FacesMessage($severity, $summary, $detail);
	}

	public FacesMessage createErrorMessage(ServicesException $e) {
		$e.printStackTrace();
		return this.createErrorMessage($e.getLocalizedMessage(), $e.getLocalizedMessage());
	}

	public void putRequestAttr(String $key, Serializable $value) {
		FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put($key, $value);
	}

	public Serializable getRequestAttr(String $key) {
		return (Serializable) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get($key);
	}
	
}

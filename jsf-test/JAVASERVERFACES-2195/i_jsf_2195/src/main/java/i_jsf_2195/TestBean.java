package i_jsf_2195;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean(name = "test")
public class TestBean implements Serializable {

    public void processAction(ActionEvent event)
        throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIOutput output = (UIOutput) context.getViewRoot().findComponent("form:cc_action:out");
        output.setValue("PROCESSACTION CALLED");
    }

    public void valueChange(ValueChangeEvent event) 
        throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIOutput output = (UIOutput) context.getViewRoot().findComponent("form:cc_value:out");
        output.setValue("VALUECHANGE CALLED");
    }

    public void processAjaxBehavior(AjaxBehaviorEvent event) 
        throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIOutput output = (UIOutput) context.getViewRoot().findComponent("form:cc_ajax:out");
        output.setValue("PROCESSAJAXBEHAVIOR CALLED");
    }

    public void listener(ComponentSystemEvent event) 
        throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIOutput output = (UIOutput) context.getViewRoot().findComponent("form:out");
        output.setValue("LISTENER CALLED");
    }

}

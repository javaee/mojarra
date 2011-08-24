
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author guido
 */
@ManagedBean(name="testBinderBean")
@RequestScoped
public class TestBinderBean {
    
    private FacesContext facesContext;
    private HashMap <String,UIComponent> componentHash;
    private UIComponent field2Component;
    
    
    @PostConstruct
    public void init() {

        this.field2Component = null;
        this.facesContext = FacesContext.getCurrentInstance();

    }
    
     private void log(String text) {
        Logger.getLogger("").log(Level.INFO,  text + "\n");
    }
    
    public UIComponent getComponentForField1() {
        

        UIInput uiInput = (UIInput) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UIInput.COMPONENT_TYPE);        
        uiInput.setImmediate(false);
        uiInput.setValueExpression("value", facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{testBean.field1}", String.class));            
        return uiInput;
        
    }
    
    public void setComponentForField1(UIComponent component){
    }
    
    
    public UIComponent getComponentPanelForField2() {
        
        if (field2Component == null) {    
            UIPanel uiPanel = (UIPanel) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UIPanel.COMPONENT_TYPE);
            UIInput uiInput = (UIInput) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UIInput.COMPONENT_TYPE);        
            uiInput.setImmediate(false);
            uiInput.setValueExpression("value", facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{testBean.field2}", String.class));        
            uiPanel.getChildren().add(uiInput);
            field2Component = uiPanel;
        }
        return field2Component;
        
    }
    
    public void setComponentPanelForField2(UIComponent component){
    }
    
    public UIComponent getComponentForField3() {
        
        
        UIInput uiInput = (UIInput) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UIInput.COMPONENT_TYPE);        
        uiInput.setImmediate(false);
        uiInput.setValueExpression("value", facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{testBean.field3}", String.class));
        
        return uiInput;
        
    }
    
    public void setComponentForField3(UIComponent component){
        this.log(" - inside setComponentForField3");
    }
    
    
    public UIComponent getComponentPanelForField3() {
        
            
        UIPanel uiPanel = (UIPanel) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UIPanel.COMPONENT_TYPE);        
        UIInput uiInput = (UIInput) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UIInput.COMPONENT_TYPE);   
        
        uiInput.setValueExpression("binding", facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), "#{testBinderBean.componentForField3}", UIComponent.class));
        uiPanel.getChildren().add(uiInput);
        return uiPanel;
        
    }
    
    public void setComponentPanelForField3(UIComponent component){
    }    
    
    public UIComponent getComponentSubmitButton() {
        
        UICommand uiCommand = (UICommand) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UICommand.COMPONENT_TYPE);        
        uiCommand.setValue("dynamic button from UICommand ( no nesting from UIPanel)");
        uiCommand.addActionListener(new ActionListener(){
                                        public void processAction(ActionEvent event) throws AbortProcessingException{
                                           Logger.getLogger("").log(Level.INFO,  " - INSIDE ACTION LISTENER INVOKED DYNAMICALLY (DIRECT NOT NESTED) - WORKS \n");
                                         }
                                    });
        return uiCommand;
        
    }    
    
    public void setComponentSubmitButton(UIComponent component){
    } 
     
    public UIComponent getComponentPanelWithSubmitButton() {
        
        UIPanel uiPanel  = (UIPanel) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UIPanel.COMPONENT_TYPE);
        UICommand uiCommand = (UICommand) this.facesContext.getCurrentInstance().getApplication().createComponent(javax.faces.component.UICommand.COMPONENT_TYPE);        
        uiCommand.setValue("dynamic button with UICommand nested in UIPanel");
        uiCommand.addActionListener(new ActionListener(){
                                        public void processAction(ActionEvent event) throws AbortProcessingException{
                                           Logger.getLogger("").log(Level.INFO,  " - INSIDE ACTION LISTENER INVOKED DYNAMICALLY (NESTED TO UIPANEL) - DOES NOT WORK ( you will never see this row ) \n");
                                         }
                                    });
        uiPanel.getChildren().add(uiCommand);
        return uiPanel;
        
    }    
    
     public void setComponentPanelWithSubmitButton(UIComponent component){
    }    
    
}

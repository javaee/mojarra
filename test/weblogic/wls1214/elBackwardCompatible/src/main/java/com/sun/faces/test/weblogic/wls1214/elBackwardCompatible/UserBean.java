package com.sun.faces.test.weblogic.wls1214.elBackwardCompatible;

import com.sun.el.ExpressionFactoryImpl;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ExpressionFactory;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;
import org.jboss.weld.el.WeldExpressionFactory;

@Named
@RequestScoped
public class UserBean implements Serializable {
    
    private final Map<String, Object> m_oNumberFilterBetweenOperatorMinValue;
    private final Map<String, Object> m_oNumberFilterBetweenOperatorMaxValue;
    
    public Map<String, Object> getNumberFilterBetweenOperatorMinValue() {
        return m_oNumberFilterBetweenOperatorMinValue;
    }
    
    public Map<String, Object> getNumberFilterBetweenOperatorMaxValue() {
        return m_oNumberFilterBetweenOperatorMaxValue;
    }
    
    private Integer intProp;

    public Integer getIntProp() {
        return intProp;
    }

    public void setIntProp(Integer intProp) {
        this.intProp = intProp;
    }
    
    private final List<String> listValues;

    public List<String> getListValues() {
        return listValues;
    }
    
    public UserBean() {
        this.listValues = new ArrayList<String>();
        listValues.add("1");
        listValues.add("2");
        listValues.add("3");
        
        m_oNumberFilterBetweenOperatorMinValue = new HashMap<String, Object>();        
        m_oNumberFilterBetweenOperatorMaxValue = new HashMap<String, Object>();
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        final String flagName = "forceSetFlag";

        Map<String, String> params = extContext.getRequestParameterMap();
        boolean forceSetFlag = false;
        if (params.containsKey(flagName)) {
            forceSetFlag = Boolean.valueOf(params.get(flagName));
        }
        
        if (forceSetFlag) {
            try {
                forceSetFlag();
            } catch (Exception ex) {
                throw new FacesException(ex);
            }
        }
    }
    
    private void forceSetFlag() throws Exception{
        final String flagName = "isBackwardCompatible22";
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        ServletContext sc = (ServletContext) extContext.getContext();
        JspApplicationContext jspAppContext = JspFactory.getDefaultFactory()
                .getJspApplicationContext(sc);
        ExpressionFactory ef = jspAppContext.getExpressionFactory();
        
        Field delegateField = WeldExpressionFactory.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        Object delegateInstance = delegateField.get(ef);
        
        if (!(delegateInstance instanceof ExpressionFactoryImpl)) {
        
            // dereference twice to get the true ExpressionFactoryImpl instance
            delegateInstance = delegateField.get(delegateInstance);
        }
                
        Field isBackwardCompatible22Field = ExpressionFactoryImpl.class.getDeclaredField(flagName);
        isBackwardCompatible22Field.setAccessible(true);
        
        boolean flagValue = true;
        Map<String, String> params = extContext.getRequestParameterMap();
        if (params.containsKey(flagName)) {
            flagValue = Boolean.valueOf(params.get(flagName));
        }
        
        isBackwardCompatible22Field.setBoolean(delegateInstance, flagValue);
        
    }
    
}


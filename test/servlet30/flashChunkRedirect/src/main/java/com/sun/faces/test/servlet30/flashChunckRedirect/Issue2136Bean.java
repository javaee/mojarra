package com.sun.faces.test.servlet30.flashChunckRedirect;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * @author Manfred Riem (manfred.riem@oracle.com)
 */
@ManagedBean
@RequestScoped
public class Issue2136Bean implements Serializable {
    /**
     * Stores the flashValue.
     */
    private String flashValue;
    
    /**
     * Stores the value.
     */
    private String value;
    
    /**
     * Constructor.
     */
    public Issue2136Bean() {
        if (FacesContext.getCurrentInstance().getExternalContext().getFlash().get("flashValue") != null) {
            this.flashValue = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("flashValue").toString();
        }
    }
    
    /**
     * Get the flash value.
     * 
     * @return the flash value.
     */
    public String getFlashValue() {
        return this.flashValue;
    }
    
    /**
     * Get the value.
     * 
     * @return the value.
     */
    public String getValue() {
        return this.value;
    }
    
    /**
     * Set the value.
     * 
     * @param value the value.
     */
    public void setValue(String value) {
        this.value = value;
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("flashValue", value);
    }
}

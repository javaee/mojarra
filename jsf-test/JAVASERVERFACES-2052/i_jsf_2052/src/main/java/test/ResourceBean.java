package test;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.ResourceHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class ResourceBean {

    private String exists = "false";

    public ResourceBean() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceHandler handler = context.getApplication().getResourceHandler();
        if (handler.libraryExists("FooBar")) {
            exists = "true";
        } else {
            exists = "false";
        }

    }
	
    public String getLibraryExists() {

        return exists;
    }

    public void setLibraryExists(String exists) {
        this.exists = exists;
    }
}



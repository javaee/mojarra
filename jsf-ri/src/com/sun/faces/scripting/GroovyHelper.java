package com.sun.faces.scripting;

import java.lang.reflect.Constructor;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Created by IntelliJ IDEA. User: rlubke Date: Apr 16, 2008 Time: 11:15:48 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GroovyHelper {


    public static boolean isGroovyScript(String value) {

        return value.endsWith(".groovy");

    }


    public static GroovyHelper getCurrentInstance(FacesContext ctx) {

        return (GroovyHelper) ctx.getExternalContext().getApplicationMap().get("com.sun.faces.groovyhelper");

    }


    public static GroovyHelper getCurrentInstance(ServletContext sc) {
        return (GroovyHelper) sc.getAttribute("com.sun.faces.groovyhelper");
    }


    public static GroovyHelper getCurrentInstance() {

        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx != null) {
            return getCurrentInstance(ctx);
        }
        return null;

    }
    public abstract Class<?> loadScript(String name);

    public Object newInstance(String name, Class<?> type, Object root) {
        Class<?> delegate = loadScript(name);
        try {
            Constructor decorationCtor = requiresDecoration(delegate, type, root);
            if (decorationCtor != null) {
                return decorationCtor.newInstance(root);
            } else {
                return delegate.newInstance();
            }
        } catch (Exception e) {
            throw new FacesException(e);
        }
    }

    public Object newInstance(String name) {
        return newInstance(name, null, null);
    }

    public abstract void setClassLoader();


    // --------------------------------------------------------- Private Methods


     private Constructor requiresDecoration(Class<?> groovyClass, Class<?> ctorArgument, Object root) {
        if (root != null) {
            try {
                return groovyClass.getConstructor(ctorArgument);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}

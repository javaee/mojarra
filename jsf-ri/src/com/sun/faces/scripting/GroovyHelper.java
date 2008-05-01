package com.sun.faces.scripting;

import java.lang.reflect.Constructor;

import java.net.URL;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.sun.faces.util.Util;

/**
 * Base class for interfacing with Groovy.
 */
public abstract class GroovyHelper {


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

    public static Object newInstance(String name, Class<?> type, Object root)
    throws Exception {
        Class<?> delegate = Util.loadClass(name, GroovyHelper.class);
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

    public static Object newInstance(String name) throws Exception {
        return newInstance(name, null, null);
    }

    public abstract void setClassLoader();
    
    public abstract void addURL(URL toAdd);

    // --------------------------------------------------------- Private Methods


     private static Constructor requiresDecoration(Class<?> groovyClass, Class<?> ctorArgument, Object root) {
        if (root != null) {
            try {
                return groovyClass.getConstructor(ctorArgument);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}

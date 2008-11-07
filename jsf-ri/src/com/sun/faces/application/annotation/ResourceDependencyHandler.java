package com.sun.faces.application.annotation;

import java.util.Map;
import java.util.HashMap;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependency;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.sun.faces.el.ELUtils;
import javax.faces.application.Resource;

/**
 * {@link RuntimeAnnotationHandler} responsible for processing {@link ResourceDependency} annotations.
 */
class ResourceDependencyHandler implements RuntimeAnnotationHandler {

    private ResourceDependency[] dependencies;
    private Map<ResourceDependency,Expressions> expressionsMap;


    // ------------------------------------------------------------ Constructors


    public ResourceDependencyHandler(ResourceDependency[] dependencies) {

        this.dependencies = dependencies;
        Map<Object, Object> attrs = FacesContext.getCurrentInstance().getAttributes();
        expressionsMap = new HashMap<ResourceDependency,Expressions>(dependencies.length, 1.0f);
        for (ResourceDependency dep : dependencies) {
            Expressions exprs = new Expressions();
            exprs.name = dep.name();
            String lib = dep.library();
            if (lib.length() > 0) {
                // Take special action to resolve the "this" library name
                if ("this".equals(lib)) {
                    String thisLibrary = (String)
                            attrs.get(com.sun.faces.application.ApplicationImpl.THIS_LIBRARY);
                    assert(null != thisLibrary);
                    lib = thisLibrary;
                }

                exprs.library = lib;
            }
            String tgt = dep.target();
            if (tgt.length() > 0) {
                exprs.target = tgt;
            }
            expressionsMap.put(dep, exprs);
        }

    }


    // ------------------------------------------ Methods from AnnotationHandler
    

    @SuppressWarnings({"UnusedDeclaration"})
    public void apply(FacesContext ctx, Object... params) {

        for (ResourceDependency dep : dependencies) {
            if (!hasBeenProcessed(ctx, dep)) {
                pushResourceToRoot(ctx, createComponentResource(ctx, dep));
                markProcssed(ctx, dep);
            }
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * Adds the specified {@link UIComponent} as a component resource to the
     * {@link javax.faces.component.UIViewRoot}
     * @param ctx the {@link FacesContext} for the current request
     * @param c the component resource
     */
    private void pushResourceToRoot(FacesContext ctx, UIComponent c) {

        ctx.getViewRoot().addComponentResource(ctx, c, (String) c .getAttributes().get("target"));

    }


    /**
     * Determines of the specified {@link ResourceDependency} has already been
     * previously processed.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param dep the {@link ResourceDependency} in question
     * @return <code>true</code> if the {@link ResourceDependency} has been
     *  processed, otherwise <code>false</code>
     */
    private boolean hasBeenProcessed(FacesContext ctx, ResourceDependency dep) {

        return (ctx.getAttributes().containsKey(dep));

    }


    /**
     * Construct a new component resource based off the provided {@link ValueExpression}s.
     *
     * @param ctx the {@link FacesContext} for the current request
     * @param dep the ResourceDependency that the component resource will be
     *  constructed from
     * @return a new component resource based of the provided annotation
     */
    private UIComponent createComponentResource(FacesContext ctx, ResourceDependency dep) {

        Expressions exprs = expressionsMap.get(dep);
        Application app = ctx.getApplication();
        String resname = exprs.getName(ctx);
        UIComponent c = ctx.getApplication().createComponent("javax.faces.Output");
        c.setRendererType(app.getResourceHandler().getRendererTypeForResourceName(resname));
        Map<String,Object> attrs = c.getAttributes();
        attrs.put("name", resname);
        if (exprs.library != null) {
            attrs.put("library", exprs.getLibrary(ctx));
        }
        if (exprs.target != null) {
            attrs.put("target", exprs.getTarget(ctx));
        }
        return c;

    }


    /**
     * Indicates that the specified ResourceDependency has been processed.
     * @param ctx the {@link FacesContext} for the current request
     * @param dep the {@link ResourceDependency}
     */
    private void markProcssed(FacesContext ctx, ResourceDependency dep) {

        ctx.getAttributes().put(dep, dep);
        
    }


    // ----------------------------------------------------------- Inner Classes


    /**
     * This helper class hides expression evaluation complexity.
     */
    private static final class Expressions {

        ValueExpression nameExpression;
        ValueExpression libraryExpression;
        ValueExpression targetExpression;
        String name;
        String library;
        String target;

        String getName(FacesContext ctx) {
            if (nameExpression == null) {
                nameExpression = ELUtils.createValueExpression(name, String.class);
            }
            return (String) nameExpression.getValue(ctx.getELContext());
        }

        String getLibrary(FacesContext ctx) {
            if (library != null) {
                if (libraryExpression == null) {
                    libraryExpression = ELUtils.createValueExpression(library, String.class);
                }
                return (String) libraryExpression.getValue(ctx.getELContext());
            }
            return null;
        }

        String getTarget(FacesContext ctx) {
            if (target != null) {
                if (targetExpression == null) {
                    targetExpression = ELUtils.createValueExpression(target, String.class);
                }
                return (String) targetExpression.getValue(ctx.getELContext());
            }
            return null;
        }


    }


}

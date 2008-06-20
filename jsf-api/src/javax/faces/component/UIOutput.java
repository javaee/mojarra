/*
 * $Id: UIOutput.java,v 1.54.12.1 2008/04/21 20:31:24 edburns Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.faces.component;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;



/**
 * <p><strong>UIOutput</strong> is a {@link UIComponent} that has a
 * value, optionally retrieved from a model tier bean via a value
 * expression, that is displayed to the user.  The user cannot directly
 * modify the rendered value; it is for display purposes only.</p>
 *
 * <p>During the <em>Render Response</em> phase of the request processing
 * lifecycle, the current value of this component must be
 * converted to a String (if it is not already), according to the following
 * rules:</p>
 * <ul>
 * <li>If the current value is not <code>null</code>, and is not already
 *     a <code>String</code>, locate a {@link Converter} (if any) to use
 *     for the conversion, as follows:
 *     <ul>
 *     <li>If <code>getConverter()</code> returns a non-null {@link Converter},
 *         use that one, otherwise</li>
 *     <li>If <code>Application.createConverter(Class)</code>, passing the
 *         current value's class, returns a non-null {@link Converter},
 *         use that one.</li>
 *     </ul></li>
 * <li>If the current value is not <code>null</code> and a {@link Converter}
 *     was located, call its <code>getAsString()</code> method to perform
 *     the conversion.</li>
 * <li>If the current value is not <code>null</code> but no {@link Converter}
 *     was located, call <code>toString()</code> on the current value to perform
 *     the conversion.</li>
 * </ul>
 *
 * <p>By default, the <code>rendererType</code> property must be set to
 * "<code>javax.faces.Text</code>".  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIOutput extends UIComponentBase
    implements ValueHolder {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Output";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Output";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIOutput} instance with default property
     * values.</p>
     */
    public UIOutput() {

        super();
        setRendererType("javax.faces.Text");

    }


    // ------------------------------------------------------ Instance Variables


    private Converter converter = null;
    private Object value = null;



    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // --------------------------------------- EditableValueHolder Properties


    public Converter getConverter() {

	if (this.converter != null) {
	    return (this.converter);
	}
	ValueExpression ve = getValueExpression("converter");
	if (ve != null) {
	    try {
		return ((Converter) ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (null);
	}

    }


    public void setConverter(Converter converter) {

        this.converter = converter;
        if (converter != null) {
            processResourceDependencyAnnotation(FacesContext.getCurrentInstance(),
                                                converter);
        }

    }

   private static void processResourceDependencyAnnotation(FacesContext context,
                                                            Object source) {
        Class<?> sourceClass = source.getClass();
        // NOTE - calling isAnnotationPresent and getAnnotation without
        // caching the metadata will be a performance sink as these methods
        // are backed by a sync'd utility method.  We'll need to come up
        // with something better.
        if (sourceClass.isAnnotationPresent(ResourceDependencies.class)) {
            ResourceDependencies resourceDeps =
                  source.getClass()
                        .getAnnotation(ResourceDependencies.class);
            ResourceDependency[] dependencies = resourceDeps.value();
            if (dependencies != null) {
                for (ResourceDependency dependency : dependencies) {
                    createComponentResource(context, dependency);
                }
            }
        } else if (sourceClass.isAnnotationPresent(ResourceDependency.class)) {
            ResourceDependency resource =
                  sourceClass.getAnnotation(ResourceDependency.class);
            createComponentResource(context, resource);
        }

    }

    private static void createComponentResource(FacesContext context,
                                                ResourceDependency resourceDep) {

        //noinspection unchecked
        List<ResourceDependency> addedResources = (List<ResourceDependency>)
              context.getAttributes().get("javax.faces.ADDED_RESOURCES");
        if (addedResources == null) {
            addedResources = new ArrayList<ResourceDependency>();
            context.getAttributes().put("javax.faces.ADDED_RESOURCES", addedResources);
        }
        if (addedResources.size() > 0 && addedResources.contains(resourceDep)) {
            // resource annotation has already been processed, don't add another
            // component.
            return;
        }
        addedResources.add(resourceDep);
        // Create a component resource
        UIOutput resourceComponent = (UIOutput) context.getApplication()
              .createComponent("javax.faces.Output");

        String resourceName = resourceDep.name();
        String library = resourceDep.library();
        String target = resourceDep.target();

        if (resourceName.length() == 0) {
            resourceName = null;
        }

        if (library.length() == 0) {
            library = null;
        }

        if (target.length() == 0) {
            target = null;
        }

        // Create a resource around it
        ResourceHandler resourceHandler =
              context.getApplication().getResourceHandler();
        // Imbue the component resource with the metadata

        resourceComponent
              .setRendererType(resourceHandler.getRendererTypeForResourceName(resourceName));
        Map<String, Object> attrs = resourceComponent.getAttributes();
        attrs.put("name", resourceName);
        if (null != library) {
            attrs.put("library", library);
        }
        if (null != target) {
            attrs.put("target", target);
        }

        // Tell the viewRoot we have this resource
        if (null != target) {
            context.getViewRoot()
                  .addComponentResource(context, resourceComponent, target);
        } else {
            context.getViewRoot()
                  .addComponentResource(context, resourceComponent);
        }
    }


    public Object getLocalValue() {

	return (this.value);

    }


    public Object getValue() {

	if (this.value != null) {
	    return (this.value);
	}
	ValueExpression ve = getValueExpression("value");
	if (ve != null) {
	    try {
		return (ve.getValue(getFacesContext().getELContext()));
	    }
	    catch (ELException e) {
		throw new FacesException(e);
	    }
	} else {
	    return (null);
	}

    }


    public void setValue(Object value) {

        this.value = value;

    }


    // ----------------------------------------------------- StateHolder Methods


    private Object[] values;

    public Object saveState(FacesContext context) {

        if (values == null) {
             values = new Object[3];
        }
       
        values[0] = super.saveState(context);
        values[1] = saveAttachedState(context, converter);
        values[2] = value;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
        converter = (Converter) restoreAttachedState(context, values[1]);
        value = values[2];

    }


}

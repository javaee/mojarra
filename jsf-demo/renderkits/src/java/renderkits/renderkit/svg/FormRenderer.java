/*
 * Copyright 2005 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// FormRenderer.java

package renderkits.renderkit.svg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.Renderer;
import javax.faces.webapp.FacesServlet;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <B>FormRenderer</B> is a class that renders a <code>UIForm<code> as a Form.
 * This class specifically renders <code>SVG</code> markup - a <code><g></code>
 * element.  The element is rendered with the necessary attributes to facilitate
 * a form submission. 
 */

public class FormRenderer extends Renderer {

    private Lifecycle lifecycle = null;
    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(FormRenderer.class);
    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables


    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public FormRenderer() {
        super();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //

    //
    // Methods From Renderer
    //

    /**
     * Determine if this form caused the submission.  
     * Install a <code>PhaseListener</code> that will listen for
     * <code>XMLHttpRequest(s)</code>.
     *
     * @see renderkits.renderkit.svg.ResponsePhaseListener
     */
    public void decode(FacesContext context, UIComponent component) {
        // Was our form the one that was submitted?  If so, we need to set
        // the indicator accordingly..
        //
        String clientId = component.getClientId(context);
        if (log.isTraceEnabled()) {
            log.trace("Begin decoding component " + component.getId());
        }
        Map requestParameterMap = context.getExternalContext()
            .getRequestParameterMap();
        if (requestParameterMap.containsKey(clientId)) {
            ((UIForm) component).setSubmitted(true);
        } else {
            ((UIForm) component).setSubmitted(false);
        }
        if (log.isTraceEnabled()) {
            log.trace("End decoding component " + component.getId());
        }

        getLifecycle(context).addPhaseListener(new ResponsePhaseListener());
    }


    /**
     * Render the starting <code><g></code> element.
     */
    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {
        String styleClass = null;

        if (context == null || component == null) {
            //PENDING - i18n
            throw new NullPointerException("'context' and/or 'component' is null");
        }
        if (log.isTraceEnabled()) {
            log.trace("Begin encoding component " +
                      component.getId());
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("g", component);
        writer.writeAttribute("id", component.getClientId(context), "clientId");
        writer.writeAttribute("method", "post", null);
        writer.writeAttribute("action", getActionStr(context), null);
        String acceptcharset = null;
        if (null != (acceptcharset = (String)
            component.getAttributes().get("acceptcharset"))) {
            writer.writeAttribute("accept-charset", acceptcharset, 
                    "acceptcharset");
        }
        
        writer.writeText("\n", null);
    }


    /**
     * <p>Return the value to be rendered as the <code>action</code> attribute
     * of the form generated for this component.</p>
     *
     * @param context FacesContext for the response we are creating
     */
    private String getActionStr(FacesContext context) {
        String viewId = context.getViewRoot().getViewId();
        String actionURL =
            context.getApplication().getViewHandler().
            getActionURL(context, viewId);
        return (context.getExternalContext().encodeActionURL(actionURL));
    }


    /**
     * Render the necessary <code>ECMAScript</code> to facilitate a 
     * form submission.  Render the closing <code><g></code> element.
     */
    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        if (context == null || component == null) {
            //PENDING - i18n
            throw new NullPointerException("'context' and/or 'component' is null");
        }
        // suppress rendering if "rendered" property on the component is
        // false.
        if (!component.isRendered()) {
            if (log.isTraceEnabled()) {
                log.trace("End encoding component " +
                          component.getId() + " since " +
                          "rendered attribute is set to false ");
            }
            return;
        }

        context.getApplication().getViewHandler().writeState(context);

        // Render the end tag for form
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("g");
        writer.writeText("\n", null);
        if (log.isTraceEnabled()) {
            log.trace("End encoding component " + component.getId());
        }

        buildPost(context, component);
    }

    /**
     * Helper method to render the <code>ECMAScript</code>.
     */
    private void buildPost(FacesContext context, UIComponent component) 
        throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("script", component);
        writer.writeText("<![CDATA[\n", null);
        String formMethodName = component.getClientId(context) + "_post";
        String formMethodText = "function "+formMethodName+"(evt) {\n";

        // write out global variables

        writer.writeText(formMethodText, null);
        writer.writeText("  var control = evt.target;\n", null);
        writer.writeText("  var form = getForm(control);\n", null);
        writer.writeText("  var postData = getPostData(form, control);\n", null);
        writer.writeText("  var url = \"", null);
        writer.writeText(getActionStr(context), null);
        writer.writeText("\";\n", null);
        writer.writeText("  sendRequest(url, postData);\n", null);
        writer.writeText("}\n", null);
        writer.writeText("//]]>\n", null);
        writer.endElement("script");
    }
                                                                                      
    /**
     * Helper method used to install <code>PhaseListener</code>.
     */
    private Lifecycle getLifecycle(FacesContext context) {
        if (null != lifecycle) {
          return lifecycle;
        }
            LifecycleFactory lifecycleFactory = (LifecycleFactory)
                FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            String lifecycleId =
                ((ServletContext) context.getExternalContext().getContext()).getInitParameter
                (FacesServlet.LIFECYCLE_ID_ATTR);
            if (lifecycleId == null) {
                lifecycleId = LifecycleFactory.DEFAULT_LIFECYCLE;
            }
            lifecycle = lifecycleFactory.getLifecycle(lifecycleId);
                                                                                                                       
        return lifecycle;
    }



} // end of class FormRenderer

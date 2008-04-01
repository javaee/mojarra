/*
 * $Id: SelectBoolean_CheckboxTag.java,v 1.16 2002/01/17 02:17:04 edburns Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// SelectBoolean_CheckboxTag.java

package com.sun.faces.taglib.html_basic;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import javax.faces.Constants;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.Renderer;
import javax.faces.RenderKit;
import javax.faces.UIForm;
import javax.faces.UISelectBoolean;
import javax.faces.ObjectManager;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 *  <B>SelectBoolean_CheckboxTag</B> is a class ...
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: SelectBoolean_CheckboxTag.java,v 1.16 2002/01/17 02:17:04 edburns Exp $
 * 
 * @see	Blah
 * @see	Bloo
 *
 */

public class SelectBoolean_CheckboxTag extends TagSupport {
    //
    // Protected Constants
    //

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    // Attribute Instance Variables

    private String checked = null;
    private String id = null;
    private String value = null;
    private String label = null;
    private String model = null;
    private String scope = null;
    private String valueChangeListener = null;

    // Relationship Instance Variables

    //
    // Constructors and Initializers    
    //

    public SelectBoolean_CheckboxTag() {
        super();
        // ParameterCheck.nonNull();
        this.init();
    }

    protected void init() {
        // super.init();
    }

    //
    // Class methods
    //

    //
    // General Methods
    //
    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id; 
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns the value of valueChangeListener attribute
     *
     * @return String value of valueChangeListener attribute
     */
    public String getValueChangeListener() {
        return this.valueChangeListener;
    }

    /**
     * Sets valueChangeListener attribute
     * @param change_listener value of formListener attribute
     */
    public void setValueChangeListener(String change_listener) {
        this.valueChangeListener = change_listener;
    }

    /**
     * Returns the value of the scope attribute
     *
     * @return String value of scope attribute
     */
    public String getScope() {
        return this.scope;
    }

    /**
     * Sets scope attribute
     * @param scope value of scope attribute
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Returns the value of the model attribute
     *
     * @return String value of model attribute
     */
    public String getModel() {
        return this.model;
    }

    /**
     * Sets the model attribute
     * @param model value of model attribute
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Process the start of this tag.
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        Assert.assert_it( pageContext != null );
        ObjectManager ot = (ObjectManager) pageContext.getServletContext().
                getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
        RenderContext renderContext = 
            (RenderContext)ot.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

        if (id != null) {

            // 1. Get or create the component instance.
            //
            UISelectBoolean wSelectBoolean = (UISelectBoolean) 
                ot.get(pageContext.getRequest(), id);
            if ( wSelectBoolean == null ) {
                wSelectBoolean = createComponent(renderContext);
                addToScope(wSelectBoolean, ot);
            }

            // 2. Render the component.
            //
            try {
                wSelectBoolean.setRendererType(
                    "CheckboxRenderer");
                wSelectBoolean.render(renderContext);
            } catch (java.io.IOException e) {
                throw new JspException("Problem rendering component: "+
                    e.getMessage());
            } catch (FacesException f) {
                throw new JspException("Problem rendering component: "+
                    f.getMessage());
            }
        }
        return (EVAL_BODY_INCLUDE);
    }

    /**
     * End Tag Processing
     */
    public int doEndTag() throws JspException{

        Assert.assert_it( pageContext != null );
        // get ObjectManager from ServletContext.
        ObjectManager ot = (ObjectManager)pageContext.getServletContext().
                 getAttribute(Constants.REF_OBJECTMANAGER);
        Assert.assert_it( ot != null );
        RenderContext renderContext =
            (RenderContext)ot.get(pageContext.getSession(),
            Constants.REF_RENDERCONTEXT);
        Assert.assert_it( renderContext != null );

//PENDING(rogerk)can we eliminate this extra get if component is instance
//variable? If so, threading issue?
//
        UISelectBoolean wSelectBoolean =
            (UISelectBoolean) ot.get(pageContext.getRequest(), id);
        Assert.assert_it( wSelectBoolean != null );

        // Complete the rendering process
        //
        try {
            wSelectBoolean.renderComplete(renderContext);
        } catch (java.io.IOException e) {
            throw new JspException("Problem completing rendering: "+
                e.getMessage());
        } catch (FacesException f) {
            throw new JspException("Problem completing rendering: "+
                f.getMessage());
        }

        return EVAL_PAGE;
    }

    /**
     * Tag cleanup method.
     */
    public void release() {

        super.release();

        checked = null;
        id = null;
        value = null;
        label = null;
        model = null;
        scope = null;
        valueChangeListener = null;
    }

    /**
     * Creates a TextEntry component and sets renderer specific
     * properties.
     *
     * @param rc renderContext client information
     */
    protected UISelectBoolean createComponent(RenderContext renderContext) 
            throws JspException {

        UISelectBoolean wSelectBoolean = new UISelectBoolean();

        // set renderer specific properties
        wSelectBoolean.setAttribute("id", getId());
        wSelectBoolean.setAttribute("value", getValue());
        wSelectBoolean.setAttribute("label", getLabel());

        // If model attribute is not found get it
        // from parent form if it exists. If not
        // set text as an attribute so that it can be
        // used during rendering.

        // PENDING ( visvan )
        // make sure that the model object is registered
        if ( model != null ) {
            wSelectBoolean.setModel(model);
        } else {
            // PENDING ( visvan ) all tags should implement a common
            // interface ??
            FormTag ancestor = null;
            try {
                ancestor = (FormTag) findAncestorWithClass(this,
                    FormTag.class);
               String model_str = ancestor.getModel();
               if ( model_str != null ) {
                   model = "$" + model_str + "." + id;
                   wSelectBoolean.setModel(model);
               }
            } catch ( Exception e ) {
                // If form tag cannot be found then model is null
            }
        }
        if ( checked != null ) {
             boolean state = (Boolean.valueOf(checked)).booleanValue();
             wSelectBoolean.setSelected(renderContext, state);
        }
        return wSelectBoolean;
    }

    /** Adds the component and listener to the ObjectManager
     * in the appropriate scope
     *
     * @param c UIComponent to be stored in namescope
     * @param ot Object pool
     */
    public void addToScope(UISelectBoolean c, ObjectManager ot) {
   
        // PENDING ( visvan ) right now, we are not saving the state of the
        // components. So if the scope is specified as reques, when the form
        // is resubmitted we would't be able to retrieve the state of the
        // components. So to get away with that we are storing in session
        // scope. This should be fixed later.
        ot.put(pageContext.getSession(), id, c);

        if ( valueChangeListener != null ) {
            String lis_name = id.concat(Constants.REF_VALUECHANGELISTENERS);
            Vector listeners = (Vector) ot.get(pageContext.getRequest(), lis_name);
            if ( listeners == null) {
                listeners = new Vector();
            }
            // this vector contains only the name of the listeners. The
            // listener itself is stored in the objectManager. We do this
            // because if the listeners are stored in the components, then
            // they have to exist for the event listeners to be dispatched
            // at the time we process the events.
            // According to the spec, listeners should be dispatched
            // independent of components.
            listeners.add(valueChangeListener);
            ot.put(pageContext.getSession(),lis_name, listeners);
        }
    }

} // end of class SelectBoolean_CheckboxTag

/*
 * $Id: UIParameterBase.java,v 1.3 2003/08/27 00:56:51 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.base;


import java.io.IOException;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UIParameterBase</strong> is a convenience base class that
 * implements the default concrete behavior of all methods defined by
 * {@link UIParameter}.</p>
 */

public class UIParameterBase extends UIOutputBase implements UIParameter {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIParameterBase} instance with default property
     * values.</p>
     */
    public UIParameterBase() {

        super();
        setRendererType(null);

    }


    // -------------------------------------------------------------- Properties


    /**
     * <p>The optional parameter name for this parameter.</p>
     */
    private String name = null;


    public String getName() {

        return (this.name);

    }


    public void setName(String name) {

        this.name = name;

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object getState(FacesContext context) {

        Object values[] = new Object[2];
        values[0] = super.getState(context);
        values[1] = name;
        return (values);

    }


    public void restoreState(FacesContext context, Object state)
        throws IOException {

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        name = (String) values[1];

    }


}

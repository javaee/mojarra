/*
 * $Id: SelectItem.java,v 1.2 2003/07/26 17:54:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import java.io.Serializable;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;


/**
 * <p><strong>SelectItem</strong> represents a single <em>item</em> in the
 * list of supported <em>items</em> associated with a {@link UISelectMany}
 * or {@link UISelectOne} component.</p>
 */

public class SelectItem implements Serializable {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a <code>SelectItem</code> instance with the specified
     * property values.</p>
     *
     * @param value Value to be returned as a request parameter if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     * @param description Description of this item, for use in tools
     */

    public SelectItem(Object value, String label, String description) {

        super();
        this.value = value;
        this.label = label;
        this.description = description;

    }


    // ------------------------------------------------------ Instance Variables


    private String description = null;
    private String label = null;
    private Object value = null;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return a description of this item, for use in development tools.
     */
    public String getDescription() {

        return (this.description);

    }


    /**
     * <p>Return the label of this item, to be rendered visibly for the user.
     */
    public String getLabel() {

        return (this.label);

    }


    /**
     * <p>Return the value of this item, to be returned as a request parameter
     * if this item is selected by the user.
     */
    public Object getValue() {

        return (this.value);

    }


}

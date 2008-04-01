/*
 * $Id: SelectItem.java,v 1.1 2002/05/25 22:28:40 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UISelectItem</strong> represents a single <em>item</em> in the
 * list of supported <em>items</em> associated with a {@link UISelectMany}
 * or {@link UISelectOne} component.  It is not itself a {@link UIComponent}.
 * </p>
 */

public class SelectItem {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a <code>UISelectItem</code> instance with the specified
     * property values.</p>
     *
     * @param value Value to be returned as a request parameter if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     * @param description Description of this item, for use in tools
     */

    public SelectItem(String value, String label, String description) {

        super();
        this.value = value;
        this.label = label;
        this.description = description;

    }


    // ----------------------------------------------------- Instance Variables


    private String description = null;
    private String label = null;
    private String value = null;


    // ------------------------------------------------------------- Properties


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
    public String getValue() {

        return (this.value);

    }



}

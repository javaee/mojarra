/*
 * $Id: SelectItem.java,v 1.14 2006/08/08 15:02:03 rogerk Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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

    private static final long serialVersionUID = 876782311414654999L;


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a <code>SelectItem</code> with no initialized property
     * values.</p>
     */
    public SelectItem() {

        super();

    }


    /**
     * <p>Construct a <code>SelectItem</code> with the specified value.  The
     * <code>label</code> property will be set to the value (converted to a
     * String, if necessary), the <code>description</code> property will be
     * set to <code>null</code>, and the <code>disabled</code> property will
     * be set to <code>false</code>.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     */
    public SelectItem(Object value) {

        this(value, value == null ? null : value.toString(), null, false, true);

    }


    /**
     * <p>Construct a <code>SelectItem</code> with the specified value and
     * label.  The <code>description</code> property will be set to
     * <code>null</code>, and the <code>disabled</code> property will be
     * set to <code>false</code>.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     */
    public SelectItem(Object value, String label) {

        this(value, label, null, false, true);

    }


    /**
     * <p>Construct a <code>SelectItem</code> instance with the specified
     * value, label and description.  This <code>disabled</code> property
     * will be set to <code>false</code>.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     * @param description Description of this item, for use in tools
     */
    public SelectItem(Object value, String label, String description) {

        this(value, label, description, false, true);

    }


    /**
     * <p>Construct a <code>SelectItem</code> instance with the specified
     * property values.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     * @param description Description of this item, for use in tools
     * @param disabled Flag indicating that this option is disabled
     */
    public SelectItem(Object value, String label, String description,
                      boolean disabled) {

        this(value, label, description, disabled, true);

    }
    
    /**
     * <p>Construct a <code>SelectItem</code> instance with the specified
     * property values.</p>
     *
     * @param value Value to be delivered to the model if this
     *  item is selected by the user
     * @param label Label to be rendered for this item in the response
     * @param description Description of this item, for use in tools
     * @param disabled Flag indicating that this option is disabled
     * @param escape Flag indicating that the text of this option should be
     * escaped when rendered.
     * @since 1.2
     */
    public SelectItem(Object value, String label, String description,
                      boolean disabled, boolean escape) {

        super();
        setValue(value);
        setLabel(label);
        setDescription(description);
        setDisabled(disabled);
        setEscape(escape);

    }

    


    // ------------------------------------------------------ Instance Variables


    private String description = null;
    private boolean disabled = false;
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
     * <p>Set the description of this item, for use in development tools.</p>
     *
     * @param description The new description
     */
    public void setDescription(String description) {

        this.description = description;

    }


    /**
     * <p>Return the disabled flag for this item, which should modify the
     * rendered output to make this item unavailable for selection by the user
     * if set to <code>true</code>.</p>
     */
    public boolean isDisabled() {

        return (this.disabled);

    }


    /**
     * <p>Set the disabled flag for this item, which should modify the
     * rendered output to make this item unavailable for selection by the user
     * if set to <code>true</code>.</p>
     *
     * @param disabled The new disabled flag
     */
    public void setDisabled(boolean disabled) {

        this.disabled = disabled;

    }


    /**
     * <p>Return the label of this item, to be rendered visibly for the user.
     */
    public String getLabel() {

        return (this.label);

    }


    /**
     * <p>Set the label of this item, to be rendered visibly for the user.
     *
     * @param label The new label
     */
    public void setLabel(String label) {

        this.label = label;

    }


    /**
     * <p>Return the value of this item, to be delivered to the model
     * if this item is selected by the user.
     */
    public Object getValue() {

        return (this.value);

    }


    /**
     * <p>Set the value of this item, to be delivered to the model
     * if this item is selected by this user.
     *
     * @param value The new value
     *
     */
    public void setValue(Object value) {

        this.value = value;

    }

    /**
     * Holds value of property escape.
     */
    private boolean escape;

    /**
     * Getter for property escape.
     * @return Value of property escape.
     */
    public boolean isEscape() {
        return this.escape;
    }

    /**
     * Setter for property escape.
     * @param escape New value of property escape.
     */
    public void setEscape(boolean escape) {
        this.escape = escape;
    }


}

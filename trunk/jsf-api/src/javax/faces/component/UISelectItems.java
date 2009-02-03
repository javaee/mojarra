/*
 * $Id: UISelectItems.java,v 1.31 2007/04/27 22:00:05 ofung Exp $
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

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;


/**
 * <p><strong>UISelectItems</strong> is a component that may be nested
 * inside a {@link UISelectMany} or {@link UISelectOne} component, and
 * causes the addition of one or more {@link SelectItem} instances to the
 * list of available options in the parent component.  The
 * <code>value</code> of this component (set either directly, or acquired
 * indirectly a {@link javax.el.ValueExpression}, can be of any
 * of the following types:</p>
 * <ul>
 * <li><em>Single instance of {@link SelectItem}</em> - This instance is
 *     added to the set of available options for the parent tag.</li>
 * <li><em>Array of {@link SelectItem}</em> - This set of instances is
 *     added to the set of available options for the parent component,
 *     in ascending subscript order.</li>
 * <li><em>Collection of {@link SelectItem}</em> - This set of instances is
 *     added to the set of available options for the parent component,
 *     in the order provided by an iterator over them.</li>
 * <li><em>Map</em> - The keys of this object (once converted to
 *     Strings) are assumed to be labels, and the values of this object
 *     (once converted to Strings)
 *     are assumed to be values, of {@link SelectItem} instances that will
 *     be constructed dynamically and added to the set of available options
 *     for the parent component, in the order provided by an iterator over
 *     the keys.</li>
 * </ul>
 */

public class UISelectItems extends UIComponentBase {


    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.SelectItems";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.SelectItems";


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UISelectItems} instance with default property
     * values.</p>
     */
    public UISelectItems() {

        super();
        setRendererType(null);

    }


    // ------------------------------------------------------ Instance Variables


    private Object value = null;


    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    // -------------------------------------------------- ValueHolder Properties


    /**
     * <p>Returns the <code>value</code> property of the
     * <code>UISelectItems</code>.</p>
     */
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


    /**
     * <p>Sets the <code>value</code> property of the
     * <code>UISelectItems</code>.</p>
     * 
     * @param value the new value
     */
    public void setValue(Object value) {

        this.value = value;

    }


    // ----------------------------------------------------- StateHolder Methods


    private Object[] values;

    public Object saveState(FacesContext context) {

        if (values == null) {
             values = new Object[2];
        }
      
        values[0] = super.saveState(context);
        values[1] = value;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
        value = values[1];

    }




}

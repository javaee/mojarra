/*
 * $Id: UISelectItems.java,v 1.9 2003/03/13 01:11:59 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


import java.io.IOException;
import javax.faces.context.FacesContext;


/**
 * <p><strong>UISelectItems</strong> is a component that may be nested
 * inside a {@link UISelectMany} or {@link UISelectOne} component, and
 * causes the addition of one or more {@link SelectItem} instances to the
 * list of available options in the parent component.  The
 * <code>value</code> of this component (set either directly, or acquired
 * indirectly via the <code>modelReference</code> property, can be of any
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

public class UISelectItems extends UIOutput {


    // ------------------------------------------------------- Static Variables


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "javax.faces.component.UISelectItems";


    // ------------------------------------------------------------- Properties


    public String getComponentType() {

        return (TYPE);

    }


    /**
     * <p>Return <code>true</code> to indicate that no
     * {@link javax.faces.render.Renderer} needs to be associated
     * with this component.</p>
     */
    public boolean getRendersSelf() {

        return (true);

    }


}

/*
 * $Id: UIOutput.java,v 1.1 2002/05/07 19:29:18 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component;


/**
 * <p><strong>UIOutput</strong> is a {@link UIComponent} that can display
 * output to the user.  The user cannot manipulate this component; it is
 * for display purposes only.  Any localization of the text to be rendered
 * is the responsibility of the application.</p>
 *
 * <h3>Properties</h3>
 *
 * <p>Each <code>UIOutput</code> instance supports the following JavaBean
 * properties to describe its render-independent characteristics:</p>
 * <ul>
 * <li><strong>text</strong> (java.lang.String) - the text value that will
 *     be rendered (verbatim) for this component.</li>
 * </ul>
 */

public abstract class UIOutput extends UIComponent {


    /**
     * The component type of this {@link UIComponent} subclass.
     */
    public static final String TYPE = "Output";


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the text to be rendered for this component, according to the
     * following algorithm:</p>
     * <ul>
     * <li>If a non-null value has been set with
     *     <a href="#setText(java.lang.String)">setText()</a>, return it;
     *     else</li>
     * <li>If the <code>model</code> property is non-null, evaluate the
     *     expression, convert the result to a String (if necessary), and
     *     return that value; else</li>
     * <li>Return a zero-length String ("").</li>
     * </ul>
     */
    public abstract String getText();


    /**
     * <p>Set the text to be rendered for this component.  A <code>null</code>
     * value erases any previously stored text value, which will cause the
     * <a href="#getText()">getText()</a> method to evaluate the
     * <code>model</code> expression (if any).</p>
     *
     * @param text New text to be rendered, or <code>null</code> to
     *  select the corresponding model value (if any)
     */
    public abstract void setText(String text);


}

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package javax.faces.component;


import javax.faces.context.FacesContext;


/**
 * <p><strong>Repeater</strong> is an interface that must be implemented
 * by {@link UIComponent} classes that want their child components to
 * represent a repeating set of data values (modeled as "rows" of data
 * in a "source"), rather than a single data value.  Components that implement
 * this interface will interact with their child components once per row,
 * instead of only once.</p>
 */

public interface Repeater {


    /**
     * <p>Generate and return a unique client identifier for the currently
     * selected row of the child component with the specified identifier.</p>
     *
     * @param context {@link FacesContext} for this request
     * @param childClientId Base client identifier for the
     *  descendant component
     *
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public String getChildClientId(FacesContext context,
				   String childClientId);


    /**
     * <p>Return the previous value for the currently selected row of the
     * specified child component.</p>
     *
     * @param component Child {@link UIComponent} for which to retrieve a
     *  previous value for the current row
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public Object getChildPrevious(UIComponent component);


    /**
     * <p>Set the previous value for the currently selected row of the specified
     * child component.</p>
     *
     * @param component Child {@link UIComponent} for which to set a previous
     *  value for the current row
     * @param previous The previous value to be stored
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public void setChildPrevious(UIComponent component, Object previous);


    /**
     * <p>Return <code>true</code> if the local value of the specified
     * child component, for the current row, is valid.</p>
     *
     * @param component Child {@link UIComponent} for which to retrieve the
     *  validity
     */
    public boolean isChildValid(UIComponent component);


    /**
     * <p>Set the valid property for the specified child component, for the
     * current row, to the specified state.</p>
     *
     * @param component Child {@link UIComponent} for which to set validity
     * @param valid New valid state
     */
    public void setChildValid(UIComponent component, boolean valid);


    /**
     * <p>Return the local value for the currently selected row of the
     * specified child component.</p>
     *
     * @param component Child {@link UIComponent} for which to retrieve a local
     *  value for the current row
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public Object getChildValue(UIComponent component);


    /**
     * <p>Set the local value for the currently selected row of the specified
     * child component.</p>
     *
     * @param component Child {@link UIComponent} for which to set a local
     *  value for the current row
     *
     * @exception NullPointerException if <code>component</code>
     *  is <code>null</code>
     */
    public void setChildValue(UIComponent component, Object value);


    /**
     * Return the number of rows of data in a source.
     */
    public int getRowCount();


    /**
     * <p>Return the zero-relative index of the current row being iterated
     * over, or -1 to indicate we are not iterating over any particular
     * row.</p>
     */
    public int getRowIndex();


    /**
     * <p>Set the zero-relative row index of the current row being iterated
     * over, or -1 to indicate that we are not iterating over any
     * particular row.
     *
     * @param index The new index value
     *
     * @exception IllegalArgumentException if <code>index</code>
     *  is <code>negative</code>
     */
    public void setRowIndex(int index);
    

}

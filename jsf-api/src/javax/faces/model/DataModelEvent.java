/*
 * $Id: DataModelEvent.java,v 1.3 2003/10/15 01:45:54 craigmcc Exp $
 */

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

package javax.faces.model;


import java.util.EventObject;


/**
 * <p><strong>DataModelEvent</strong> represents an event of interest to
 * registered listeners that occurred on the specified {@link DataModel}.</p>
 */

public class DataModelEvent extends EventObject {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct an event object that is associated with the specified
     * row index and associated data.</p>
     *
     * @param model The [@link DataModel} on which this event occurred
     * @param index The one relative row index for which this event occurred,
     *   or zero for no specific row association
     * @param data Representation of the data for the row specified by
     *  <code>index</code>, or <code>null</code> for no particular row data
     */
    public DataModelEvent(DataModel model, int index, Object data) {

	super(model);
	this.index = index;
	this.data = data;

    }


    // ------------------------------------------------------ Instance Variables


    private Object data = null;


    private int index = 0;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link DataModel} that fired this event.</p>
     */
    public DataModel getDataModel() {

	return ((DataModel) getSource());

    }


    /**
     * <p>Return the object representing the data for the specified row index,
     * or <code>null</code> for no associated row data.</p>
     */
    public Object getRowData() {

	return (this.data);

    }


    /**
     * <p>Return the row index for this event, or zero for no specific row.</p>
     */
    public int getRowIndex() {

	return (this.index);

    }



}

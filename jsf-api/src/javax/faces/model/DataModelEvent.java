/*
 * $Id: DataModelEvent.java,v 1.10 2004/05/12 18:29:13 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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
     * @param model The {@link DataModel} on which this event occurred
     * @param index The zero relative row index for which this event occurred,
     *   or -1 for no specific row association
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
     * <p>Return the row index for this event, or -1 for no specific row.</p>
     */
    public int getRowIndex() {

	return (this.index);

    }



}

/*
 * $Id: ListDataModel.java,v 1.2 2003/09/11 15:25:59 craigmcc Exp $
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

package javax.faces.component;


import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;


/**
 * <p><strong>ListDataModel</strong> is a private implementation of
 * {@link DataModel} that wraps a <code>List</code> of Java objects.</p>
 */

class ListDataModel implements DataModel {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link ListDataModel} wrapping the specified
     * <code>List</code>.</p>
     *
     * @param list <code>List</code> to be wrapped
     *
     * @exception NullPointerException if <code>list</code>
     *  is <code>null</code>
     */
    public ListDataModel(List list) {

        if (list == null) {
            throw new NullPointerException();
        }
        this.list = list;

    }


    // ------------------------------------------------------ Instance Variables


    // The current row index (one relative)
    private int index = 0;


    // The list we are wrapping
    private List list = null;


    // The DataModelListeners interested in our events
    private List listeners = null;


    // The open flag
    private boolean open = false;


    // ------------------------------------------------------- Lifecycle Methods


    public void close() {

        if (!open) {
            throw new IllegalStateException();
        }
        if (listeners != null) {
            DataModelEvent event = new DataModelEvent(this);
            int n = listeners.size();
            for (int i = 0; i < n; i++) {
                ((DataModelListener) listeners.get(i)).modelClosed(event);
            }
        }
        this.list = null;
        this.open = false;

    }


    public void open() throws FacesException {

        if (open) {
            throw new IllegalStateException();
        }
        index = 0;
        open = true;
        if (listeners != null) {
            DataModelEvent event = new DataModelEvent(this);
            int n = listeners.size();
            for (int i = 0; i < n; i++) {
                ((DataModelListener) listeners.get(i)).modelOpened(event);
            }
        }

    }


    // -------------------------------------------------------------- Properties


    public boolean isOpen() {

        return (open);

    }


    public int getRowCount() {

        if (!open) {
            throw new IllegalStateException();
        }
        return (list.size());

    }


    public Object getRowData() {

        if (!open) {
            throw new IllegalStateException();
        }
        if ((index < 0) || (index > getRowCount())) {
            throw new IllegalArgumentException();
        }
        if (index == 0) {
            return (null);
        } else {
            return (list.get(index - 1));
        }

    }


    public int getRowIndex() {

        if (!open) {
            throw new IllegalStateException();
        }
        return (index);

    }


    public void setRowIndex(int rowIndex) {

        if (!open) {
            throw new IllegalStateException();
        }
        if (rowIndex < 0) {
            throw new IllegalArgumentException();
        }
        int old = index;
        index = rowIndex;
        if ((old != index) && (listeners != null)) {
            DataModelEvent event =
                event = new DataModelEvent(this, index, getRowData());
            int n = listeners.size();
            for (int i = 0; i < n; i++) {
                ((DataModelListener) listeners.get(i)).modelSelected(event);
            }
        }

    }


    // --------------------------------------------- Event Listener Registration


    public void addDataModelListener(DataModelListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);

    }


    public void removeDataModelListener(DataModelListener listener) {

        if (listener == null) {
            throw new NullPointerException();
        }
        if (listeners != null) {
            listeners.remove(listener);
        }

    }


}

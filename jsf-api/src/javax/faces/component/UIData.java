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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.faces.model.ScalarDataModel;
import javax.servlet.jsp.jstl.sql.Result;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * <p><strong>UIData</strong> is a {@link UIComponent} that supports data
 * binding to a collection of data objects represented by a {@link DataModel}
 * instance, which is the current value of this component itself (typically
 * established via a {@link ValueExpression}). During iterative processing over
 * the rows of data in the data model, the object for the current row is exposed
 * as a request attribute under the key specified by the <code>var</code>
 * property.</p>
 * <p/>
 * <p>Only children of type {@link UIColumn} should be processed by renderers
 * associated with this component.</p>
 * <p/>
 * <p>By default, the <code>rendererType</code> property is set to
 * <code>javax.faces.Table</code>.  This value can be changed by calling the
 * <code>setRendererType()</code> method.</p>
 */

public class UIData extends UIComponentBase
      implements NamingContainer {

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "javax.faces.Data";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "javax.faces.Data";

    // ------------------------------------------------------------ Constructors


    /**
     * <p>Create a new {@link UIData} instance with default property
     * values.</p>
     */
    public UIData() {

        super();
        setRendererType("javax.faces.Table");

    }

    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The first row number (zero-relative) to be displayed.</p>
     */
    private int first = 0;
    private boolean firstSet = false;


    /**
     * <p>The {@link DataModel} associated with this component, lazily
     * instantiated if requested.  This object is not part of the saved and
     * restored state of the component.</p>
     */
    private DataModel model = null;


    /**
     * <p> During iteration through the rows of this table, This ivar is used to
     * store the previous "var" value for this instance.  When the row iteration
     * is complete, this value is restored to the request map.
     */
    private Object oldVar;


    /**
     * <p>The zero-relative index of the current row number, or -1 for no
     * current row association.</p>
     */
    private int rowIndex = -1;


    /**
     * <p>The number of rows to display, or zero for all remaining rows in the
     * table.</p>
     */
    private int rows = 0;
    private boolean rowsSet = false;


    /**
     * <p>This map contains <code>SavedState</code> instances for each
     * descendant component, keyed by the client identifier of the descendant.
     * Because descendant client identifiers will contain the
     * <code>rowIndex</code> value of the parent, per-row state information is
     * actually preserved.</p>
     */
    @SuppressWarnings({"CollectionWithoutInitialCapacity"})
    private Map<String, SavedState> saved = new HashMap<String, SavedState>();


    /**
     * <p>The local value of this {@link UIComponent}.</p>
     */
    private Object value = null;


    /**
     * <p>The request scope attribute under which the data object for the
     * current row will be exposed when iterating.</p>
     */
    private String var = null;


    /**
     * <p>Holds the base client ID that will be used to generate per-row
     * client IDs (this will be null if this UIData is nested within another).</p>
     *
     * <p>This is not part of the component state.</p>
     */
    private String baseClientId = null;


    /**
     * <p> Length of the cached <code>baseClientId</code> plus one for the
     * NamingContainer.SEPARATOR_CHAR. </p>
     *
     * <p>This is not part of the component state.</p>
     */
    private int baseClientIdLength;


    /**
     * <p>StringBuilder used to build per-row client IDs.</p>
     *
     * <p>This is not part of the component state.</p>
     */
    private StringBuilder clientIdBuilder = null;


    /**
     * <p>Flag indicating whether or not this UIData instance is nested
     * within another UIData instance</p>
     *
     * <p>This is not part of the component state.</p>
     */
    private Boolean isNested = null;
    

    // -------------------------------------------------------------- Properties


    public String getFamily() {

        return (COMPONENT_FAMILY);

    }


    /**
     * <p>Return the zero-relative row number of the first row to be
     * displayed.</p>
     */
    public int getFirst() {

        if (this.firstSet) {
            return (this.first);
        }
        ValueExpression ve = getValueExpression("first");
        if (ve != null) {
            Integer value;
            try {
                value = (Integer) ve.getValue(getFacesContext().getELContext());
            }
            catch (ELException e) {
                throw new FacesException(e);
            }
            if (null == value) {
                return first;
            }
            return (value.intValue());
        } else {
            return (this.first);
        }

    }


    /**
     * <p>Set the zero-relative row number of the first row to be
     * displayed.</p>
     *
     * @param first New first row number
     *
     * @throws IllegalArgumentException if <code>first</code> is negative
     */
    public void setFirst(int first) {

        if (first < 0) {
            throw new IllegalArgumentException(String.valueOf(first));
        }
        this.first = first;
        this.firstSet = true;

    }


    /**
     * <p>Return the footer facet of this component (if any).  A convenience
     * method for <code>getFacet("footer")</code>.</p>
     */
    public UIComponent getFooter() {

        return getFacet("footer");

    }


    /**
     * <p>Set the footer facet of this component.  A convenience method for
     * <code>getFacets().put("footer", footer)</code>.</p>
     *
     * @param footer the new footer facet
     *
     * @throws NullPointerException if <code>footer</code> is <code>null</code>
     */
    public void setFooter(UIComponent footer) {

        getFacets().put("footer", footer);

    }


    /**
     * <p>Return the header facet of this component (if any).  A convenience
     * method for <code>getFacet("header")</code>.</p>
     */
    public UIComponent getHeader() {

        return getFacet("header");

    }


    /**
     * <p>Set the header facet of this component.  A convenience method for
     * <code>getFacets().put("header", header)</code>.</p>
     *
     * @param header the new header facet
     *
     * @throws NullPointerException if <code>header</code> is <code>null</code>
     */
    public void setHeader(UIComponent header) {

        getFacets().put("header", header);

    }


    /**
     * <p>Return a flag indicating whether there is <code>rowData</code>
     * available at the current <code>rowIndex</code>.  If no
     * <code>wrappedData</code> is available, return <code>false</code>.</p>
     *
     * @throws FacesException if an error occurs getting the row availability
     */
    public boolean isRowAvailable() {

        return (getDataModel().isRowAvailable());

    }


    /**
     * <p>Return the number of rows in the underlying data model.  If the number
     * of available rows is unknown, return -1.</p>
     *
     * @throws FacesException if an error occurs getting the row count
     */
    public int getRowCount() {

        return (getDataModel().getRowCount());

    }


    /**
     * <p>Return the data object representing the data for the currently
     * selected row index, if any.</p>
     *
     * @throws FacesException           if an error occurs getting the row data
     * @throws IllegalArgumentException if now row data is available at the
     *                                  currently specified row index
     */
    public Object getRowData() {

        return (getDataModel().getRowData());

    }


    /**
     * <p>Return the zero-relative index of the currently selected row.  If we
     * are not currently positioned on a row, return -1.  This property is
     * <strong>not</strong> enabled for value binding expressions.</p>
     *
     * @throws FacesException if an error occurs getting the row index
     */
    public int getRowIndex() {

        return (this.rowIndex);

    }


    /**
     * <p>Set the zero relative index of the current row, or -1 to indicate that
     * no row is currently selected, by implementing the following algorithm.
     * It is possible to set the row index at a value for which the underlying
     * data collection does not contain any row data.  Therefore, callers may
     * use the <code>isRowAvailable()</code> method to detect whether row data
     * will be available for use by the <code>getRowData()</code> method.</p>
     *</p>
     * <ul>
     * <li>Save current state information for all descendant components (as
     *     described below).
     * <li>Store the new row index, and pass it on to the {@link DataModel}
     *     associated with this {@link UIData} instance.</li>
     * <li>If the new <code>rowIndex</code> value is -1:
     *     <ul>
     *     <li>If the <code>var</code> property is not null,
     *         remove the corresponding request scope attribute (if any).</li>
     *     <li>Reset the state information for all descendant components
     *         (as described below).</li>
     *     </ul></li>
     * <li>If the new <code>rowIndex</code> value is not -1:
     *     <ul>
     *     <li>If the <code>var</code> property is not null, call
     *         <code>getRowData()</code> and expose the resulting data object
     *         as a request scope attribute whose key is the <code>var</code>
     *         property value.</li>
     *     <li>Reset the state information for all descendant components
     *         (as described below).
     *     </ul></li>
     * </ul>
     *
     * <p>To save current state information for all descendant components,
     * {@link UIData} must maintain per-row information for each descendant
     * as follows:<p>
     * <ul>
     * <li>If the descendant is an instance of <code>EditableValueHolder</code>, save
     *     the state of its <code>localValue</code> property.</li>
     * <li>If the descendant is an instance of <code>EditableValueHolder</code>,
     *     save the state of the <code>localValueSet</code> property.</li>
     * <li>If the descendant is an instance of <code>EditableValueHolder</code>, save
     *     the state of the <code>valid</code> property.</li>
     * <li>If the descendant is an instance of <code>EditableValueHolder</code>,
     *     save the state of the <code>submittedValue</code> property.</li>
     * </ul>
     *
     * <p>To restore current state information for all descendant components,
     * {@link UIData} must reference its previously stored information for the
     * current <code>rowIndex</code> and call setters for each descendant
     * as follows:</p>
     * <ul>
     * <li>If the descendant is an instance of <code>EditableValueHolder</code>,
     *     restore the <code>value</code> property.</li>
     * <li>If the descendant is an instance of <code>EditableValueHolder</code>,
     *     restore the state of the <code>localValueSet</code> property.</li>
     * <li>If the descendant is an instance of <code>EditableValueHolder</code>,
     *     restore the state of the <code>valid</code> property.</li>
     * <li>If the descendant is an instance of <code>EditableValueHolder</code>,
     *     restore the state of the <code>submittedValue</code> property.</li>
     * </ul>
     *
     * @param rowIndex The new row index value, or -1 for no associated row
     *
     * @throws FacesException if an error occurs setting the row index
     * @throws IllegalArgumentException if <code>rowIndex</code>
     *  is less than -1
     */
    public void setRowIndex(int rowIndex) {

        // Save current state for the previous row index
        saveDescendantState();

        // Update to the new row index        
        this.rowIndex = rowIndex;
        DataModel localModel = getDataModel();
        localModel.setRowIndex(rowIndex);

        // Clear or expose the current row data as a request scope attribute
        if (var != null) {
            Map<String, Object> requestMap =
                  getFacesContext().getExternalContext().getRequestMap();
            if (rowIndex == -1) {
                oldVar = requestMap.remove(var);
            } else if (isRowAvailable()) {
                requestMap.put(var, getRowData());
            } else {
                requestMap.remove(var);
                if (null != oldVar) {
                    requestMap.put(var, oldVar);
                    oldVar = null;
                }
            }
        }

        // Reset current state information for the new row index
        restoreDescendantState();

    }


    /**
     * <p>Return the number of rows to be displayed, or zero for all remaining
     * rows in the table.  The default value of this property is zero.</p>
     */
    public int getRows() {

        if (this.rowsSet) {
            return (this.rows);
        }
        ValueExpression ve = getValueExpression("rows");
        if (ve != null) {
            Integer value;
            try {
                value = (Integer) ve.getValue(getFacesContext().getELContext());
            }
            catch (ELException e) {
                throw new FacesException(e);
            }

            if (null == value) {
                return rows;
            }
            return (value.intValue());
        } else {
            return (this.rows);
        }

    }


    /**
     * <p>Set the number of rows to be displayed, or zero for all remaining rows
     * in the table.</p>
     *
     * @param rows New number of rows
     *
     * @throws IllegalArgumentException if <code>rows</code> is negative
     */
    public void setRows(int rows) {

        if (rows < 0) {
            throw new IllegalArgumentException(String.valueOf(rows));
        }
        this.rows = rows;
        this.rowsSet = true;

    }


    /**
     * <p>Return the request-scope attribute under which the data object for the
     * current row will be exposed when iterating.  This property is
     * <strong>not</strong> enabled for value binding expressions.</p>
     */
    public String getVar() {

        return (this.var);

    }


    /**
     * <p>Set the request-scope attribute under which the data object for the
     * current row wil be exposed when iterating.</p>
     *
     * @param var The new request-scope attribute name
     */
    public void setVar(String var) {

        this.var = var;

    }

    // ----------------------------------------------------- StateHolder Methods


    private Object[] values;

    public Object saveState(FacesContext context) {

        if (values == null) {
            values = new Object[9];
        }

        values[0] = super.saveState(context);
        values[1] = new Integer(first);
        values[2] = firstSet ? Boolean.TRUE : Boolean.FALSE;
        values[3] = new Integer(rowIndex);
        values[4] = new Integer(rows);
        values[5] = rowsSet ? Boolean.TRUE : Boolean.FALSE;
        values[6] = saved;
        values[7] = value;
        values[8] = var;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        values = (Object[]) state;
        super.restoreState(context, values[0]);
        first = ((Integer) values[1]).intValue();
        firstSet = ((Boolean) values[2]).booleanValue();
        rowIndex = ((Integer) values[3]).intValue();
        rows = ((Integer) values[4]).intValue();
        rowsSet = ((Boolean) values[5]).booleanValue();
        saved = TypedCollections
              .dynamicallyCastMap((Map) values[6], String.class, SavedState.class);
        value = values[7];
        var = (String) values[8];

    }


    /**
     * <p>Return the value of the UIData.  This value must either be
     * be of type {@link DataModel}, or a type that can be adapted
     * into a {@link DataModel}.  <code>UIData</code> will automatically
     * adapt the following types:</p>
     * <ul>
     * <li>Arrays</li>
     * <li><code>java.util.List</code></li>
     * <li><code>java.sql.ResultSet</code></li>
     * <li><code>javax.servlet.jsp.jstl.sql.Result</code></li>
     * </ul>
     * <p>All other types will be adapted using the {@link ScalarDataModel}
     * class, which will treat the object as a single row of data.</p>
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
     * <p>Set the value of the <code>UIData</code>.  This value must either be
     * be of type {@link DataModel}, or a type that can be adapted into a {@link
     * DataModel}.</p>
     *
     * @param value the new value
     */
    public void setValue(Object value) {
        setDataModel(null);
        this.value = value;

    }

    // ----------------------------------------------------- UIComponent Methods


    /**
     * <p>If "name" is something other than "value", "var", or "rowIndex", rely
     * on the superclass conversion from <code>ValueBinding</code> to
     * <code>ValueExpression</code>.</p>
     *
     * @param name    Name of the attribute or property for which to set a
     *                {@link ValueBinding}
     * @param binding The {@link ValueBinding} to set, or <code>null</code> to
     *                remove any currently set {@link ValueBinding}
     *
     * @throws IllegalArgumentException if <code>name</code> is one of
     *                                  <code>id</code>, <code>parent</code>,
     *                                  <code>var</code>, or <code>rowIndex</code>
     * @throws NullPointerException     if <code>name</code> is <code>null</code>
     * @deprecated This has been replaced by {@link #setValueExpression(java.lang.String,
     *javax.el.ValueExpression)}.
     */
    public void setValueBinding(String name, ValueBinding binding) {

        if ("value".equals(name)) {
            setDataModel(null);
        } else if ("var".equals(name) || "rowIndex".equals(name)) {
            throw new IllegalArgumentException();
        }
        super.setValueBinding(name, binding);

    }

    /**
     * <p>Set the {@link ValueExpression} used to calculate the value for the
     * specified attribute or property name, if any.  In addition, if a {@link
     * ValueExpression} is set for the <code>value</code> property, remove any
     * synthesized {@link DataModel} for the data previously bound to this
     * component.</p>
     *
     * @param name    Name of the attribute or property for which to set a
     *                {@link ValueExpression}
     * @param binding The {@link ValueExpression} to set, or <code>null</code>
     *                to remove any currently set {@link ValueExpression}
     *
     * @throws IllegalArgumentException if <code>name</code> is one of
     *                                  <code>id</code>, <code>parent</code>,
     *                                  <code>var</code>, or <code>rowIndex</code>
     * @throws NullPointerException     if <code>name</code> is <code>null</code>
     * @since 1.2
     */
    public void setValueExpression(String name, ValueExpression binding) {

        if ("value".equals(name)) {
            this.model = null;
        } else if ("var".equals(name) || "rowIndex".equals(name)) {
            throw new IllegalArgumentException();
        }
        super.setValueExpression(name, binding);

    }

    /**
     * <p>Return a client identifier for this component that includes the
     * current value of the <code>rowIndex</code> property, if it is not set to
     * -1.  This implies that multiple calls to <code>getClientId()</code> may
     * return different results, but ensures that child components can
     * themselves generate row-specific client identifiers (since {@link UIData}
     * is a {@link NamingContainer}).</p>
     *
     * @throws NullPointerException if <code>context</code> is <code>null</code>
     */
    public String getClientId(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // If baseClientId and clientIdBuilder are both null, this is the
        // first time that getClientId() has been called.
        // If we're not nested within another UIData, then:
        //   - create a new StringBuilder assigned to clientIdBuilder containing
        //   our client ID.
        //   - toString() the builder - this result will be our baseClientId
        //     for the duration of the component
        //   - append SEPARATOR_CHAR to the builder
        //  If we are nested within another UIData, then:
        //   - create an empty StringBuilder that will be used to build
        //     this instance's ID
        if (baseClientId == null && clientIdBuilder == null) {
            if (!isNestedWithinUIData()) {
                clientIdBuilder = new StringBuilder(super.getClientId(context));
                baseClientId = clientIdBuilder.toString();
                baseClientIdLength = (baseClientId.length() + 1);
                clientIdBuilder.append(NamingContainer.SEPARATOR_CHAR);
                clientIdBuilder.setLength(baseClientIdLength);
            } else {
                clientIdBuilder = new StringBuilder();
            }
        }
        if (rowIndex >= 0) {
            String cid;
            if (!isNestedWithinUIData()) {
                // we're not nested, so the clientIdBuilder is already
                // primed with clientID + SEPARATOR_CHAR.  Append
                // the current rowIndex, and toString() the builder.
                // reset the builder to it's primed state.
                cid = clientIdBuilder.append(rowIndex).toString();
                clientIdBuilder.setLength(baseClientIdLength);
            } else {
                // we're nested, so we have to build the ID from scratch
                // each time.  Reuse the same clientIdBuilder instance
                // for each call by resetting the length to 0 after
                // the ID has been computed.
                cid = clientIdBuilder.append(super.getClientId(context))
                      .append(NamingContainer.SEPARATOR_CHAR).append(rowIndex)
                      .toString();
                clientIdBuilder.setLength(0);
            }
            return (cid);
        } else {
            if (!isNestedWithinUIData()) {
                // Not nested and no row available, so just return our baseClientId
                return (baseClientId);
            } else {
                // nested and no row available, return the result of getClientId().
                // this is necessary as the client ID will reflect the row that
                // this table represents
                return super.getClientId(context);
            }
        }

    }

    /**
     * <p>Override behavior from {@link UIComponentBase#invokeOnComponent} to
     * provide special care for positioning the data properly before finding the
     * component and invoking the callback on it.  If the argument
     * <code>clientId</code> is equal to <code>this.getClientId()</code> simply
     * invoke the <code>contextCallback</code>, passing the <code>context</code>
     * argument and <b>this</b> as arguments, and return <code>true.</code>
     * Otherwise, attempt to extract a rowIndex from the <code>clientId</code>.
     * For example, if the argument <code>clientId</code> was
     * <code>form:data:3:customerHeader</code> the rowIndex would be
     * <code>3</code>.  Let this value be called <code>newIndex</code>. The
     * current rowIndex of this instance must be saved aside and restored before
     * returning in all cases, regardless of the outcome of the search or if any
     * exceptions are thrown in the process.</p>
     * 
     * <p>The implementation of this method must never return <code>true</code>
     * if setting the rowIndex of this instance to be equal to
     * <code>newIndex</code> causes this instance to return <code>false</code>
     * from {@link #isRowAvailable}.</p>
     *
     * @throws NullPointerException {@inheritDoc}
     * @throws FacesException       {@inheritDoc}  Also throws <code>FacesException</code>
     *                              if any exception is thrown when deriving the
     *                              rowIndex from the argument <code>clientId</code>.
     * @since 1.2
     */
    public boolean invokeOnComponent(FacesContext context, String clientId,
                                     ContextCallback callback)
          throws FacesException {
        if (null == context || null == clientId || null == callback) {
            throw new NullPointerException();
        }

        String myId = super.getClientId(context);
        boolean found = false;
        if (clientId.equals(myId)) {
            try {
                callback.invokeContextCallback(context, this);
                return true;
            }
            catch (Exception e) {
                throw new FacesException(e);
            }
        }
        int lastSep, newRow, savedRowIndex = this.getRowIndex();
        try {
            // If we need to strip out the rowIndex from our id
            // PENDING(edburns): is this safe with respect to I18N?
            if (myId.endsWith(String.valueOf(savedRowIndex))) {
                lastSep = myId.lastIndexOf(NamingContainer.SEPARATOR_CHAR);
                assert (-1 != lastSep);
                myId = myId.substring(0, lastSep);
            }

            // myId will be something like form:outerData for a non-nested table,
            // and form:outerData:3:data for a nested table.
            // clientId will be something like form:outerData:3:outerColumn
            // for a non-nested table.  clientId will be something like
            // outerData:3:data:3:input for a nested table.
            if (clientId.startsWith(myId)) {
                int preRowIndexSep, postRowIndexSep;

                if (-1 != (preRowIndexSep =
                      clientId.indexOf(NamingContainer.SEPARATOR_CHAR,
                                       myId.length()))) {
                    // Check the length
                    if (++preRowIndexSep < clientId.length()) {
                        if (-1 != (postRowIndexSep =
                              clientId.indexOf(NamingContainer.SEPARATOR_CHAR,
                                               preRowIndexSep + 1))) {
                            try {
                                newRow = Integer
                                      .valueOf(clientId.substring(preRowIndexSep,
                                                                  postRowIndexSep))
                                      .intValue();
                            } catch (NumberFormatException ex) {
                                // PENDING(edburns): I18N
                                String message =
                                      "Trying to extract rowIndex from clientId \'"
                                      +
                                      clientId
                                      + "\' "
                                      + ex.getMessage();
                                throw new NumberFormatException(message);
                            }
                            this.setRowIndex(newRow);
                            if (this.isRowAvailable()) {
                                found = super.invokeOnComponent(context,
                                                                clientId,
                                                                callback);
                            }
                        }
                    }
                }
            }
        }
        catch (FacesException fe) {
            throw fe;
        }
        catch (Exception e) {
            throw new FacesException(e);
        }
        finally {
            this.setRowIndex(savedRowIndex);
        }
        return found;
    }


    /**
     * <p>Override the default {@link UIComponentBase#queueEvent} processing to
     * wrap any queued events in a wrapper so that we can reset the current row
     * index in <code>broadcast()</code>.</p>
     *
     * @param event {@link FacesEvent} to be queued
     *
     * @throws IllegalStateException if this component is not a descendant of a
     *                               {@link UIViewRoot}
     * @throws NullPointerException  if <code>event</code> is <code>null</code>
     */
    public void queueEvent(FacesEvent event) {

        super.queueEvent(new WrapperEvent(this, event, getRowIndex()));

    }


    /**
     * <p>Override the default {@link UIComponentBase#broadcast} processing to
     * unwrap any wrapped {@link FacesEvent} and reset the current row index,
     * before the event is actually broadcast.  For events that we did not wrap
     * (in <code>queueEvent()</code>), default processing will occur.</p>
     *
     * @param event The {@link FacesEvent} to be broadcast
     *
     * @throws AbortProcessingException Signal the JavaServer Faces
     *                                  implementation that no further
     *                                  processing on the current event should
     *                                  be performed
     * @throws IllegalArgumentException if the implementation class of this
     *                                  {@link FacesEvent} is not supported by
     *                                  this component
     * @throws NullPointerException     if <code>event</code> is <code>null</code>
     */
    public void broadcast(FacesEvent event)
          throws AbortProcessingException {

        if (!(event instanceof WrapperEvent)) {
            super.broadcast(event);
            return;
        }

        // Set up the correct context and fire our wrapped event
        WrapperEvent revent = (WrapperEvent) event;
        if (isNestedWithinUIData()) {
            setDataModel(null);
        }
        int oldRowIndex = getRowIndex();
        setRowIndex(revent.getRowIndex());
        FacesEvent rowEvent = revent.getFacesEvent();
        rowEvent.getComponent().broadcast(rowEvent);
        setRowIndex(oldRowIndex);

    }


    /**
     * <p>In addition to the default behavior, ensure that any saved per-row
     * state for our child input components is discarded unless it is needed to
     * rerender the current page with errors.
     *
     * @param context FacesContext for the current request
     *
     * @throws IOException          if an input/output error occurs while
     *                              rendering
     * @throws NullPointerException if <code>context</code> is <code>null</code>
     */
    public void encodeBegin(FacesContext context) throws IOException {

        setDataModel(null); // re-evaluate even with server-side state saving
        if (!keepSaved(context)) {
            //noinspection CollectionWithoutInitialCapacity
            saved = new HashMap<String, SavedState>();
        }
        super.encodeBegin(context);

    }


    /**
     * <p>Override the default {@link UIComponentBase#processDecodes} processing
     * to perform the following steps.</p> <ul> <li>If the <code>rendered</code>
     * property of this {@link UIComponent} is <code>false</code>, skip further
     * processing.</li> <li>Set the current <code>rowIndex</code> to -1.</li>
     * <li>Call the <code>processDecodes()</code> method of all facets of this
     * {@link UIData}, in the order determined by a call to
     * <code>getFacets().keySet().iterator()</code>.</li> <li>Call the
     * <code>processDecodes()</code> method of all facets of the {@link
     * UIColumn} children of this {@link UIData}.</li> <li>Iterate over the set
     * of rows that were included when this component was rendered (i.e. those
     * defined by the <code>first</code> and <code>rows</code> properties),
     * performing the following processing for each row: <ul> <li>Set the
     * current <code>rowIndex</code> to the appropriate value for this row.</li>
     * <li>If <code>isRowAvailable()</code> returns <code>true</code>, iterate
     * over the children components of each {@link UIColumn} child of this
     * {@link UIData} component, calling the <code>processDecodes()</code>
     * method for each such child.</li> </ul></li> <li>Set the current
     * <code>rowIndex</code> to -1.</li> <li>Call the <code>decode()</code>
     * method of this component.</li> <li>If a <code>RuntimeException</code> is
     * thrown during decode processing, call {@link FacesContext#renderResponse}
     * and re-throw the exception.</li> </ul>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @throws NullPointerException if <code>context</code> is <code>null</code>
     */
    public void processDecodes(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }

        setDataModel(null); // Re-evaluate even with server-side state saving
        if (null == saved || !keepSaved(context)) {
            //noinspection CollectionWithoutInitialCapacity
            saved = new HashMap<String, SavedState>(); // We don't need saved state here
        }

        iterate(context, PhaseId.APPLY_REQUEST_VALUES);
        decode(context);

    }


    /**
     * <p>Override the default {@link UIComponentBase#processValidators}
     * processing to perform the following steps.</p> <ul> <li>If the
     * <code>rendered</code> property of this {@link UIComponent} is
     * <code>false</code>, skip further processing.</li> <li>Set the current
     * <code>rowIndex</code> to -1.</li> <li>Call the <code>processValidators()</code>
     * method of all facets of this {@link UIData}, in the order determined by a
     * call to <code>getFacets().keySet().iterator()</code>.</li> <li>Call the
     * <code>processValidators()</code> method of all facets of the {@link
     * UIColumn} children of this {@link UIData}.</li> <li>Iterate over the set
     * of rows that were included when this component was rendered (i.e. those
     * defined by the <code>first</code> and <code>rows</code> properties),
     * performing the following processing for each row: <ul> <li>Set the
     * current <code>rowIndex</code> to the appropriate value for this row.</li>
     * <li>If <code>isRowAvailable()</code> returns <code>true</code>, iterate
     * over the children components of each {@link UIColumn} child of this
     * {@link UIData} component, calling the <code>processValidators()</code>
     * method for each such child.</li> </ul></li> <li>Set the current
     * <code>rowIndex</code> to -1.</li> </ul>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @throws NullPointerException if <code>context</code> is <code>null</code>
     */
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        if (isNestedWithinUIData()) {
            setDataModel(null);
        }
        iterate(context, PhaseId.PROCESS_VALIDATIONS);
        // This is not a EditableValueHolder, so no further processing is required

    }


    /**
     * <p>Override the default {@link UIComponentBase#processUpdates}
     * processing to perform the following steps.</p>
     * <ul>
     * <li>If the <code>rendered</code> property of this {@link UIComponent}
     *     is <code>false</code>, skip further processing.</li>
     * <li>Set the current <code>rowIndex</code> to -1.</li>
     * <li>Call the <code>processUpdates()</code> method of all facets
     *     of this {@link UIData}, in the order determined
     *     by a call to <code>getFacets().keySet().iterator()</code>.</li>
     * <li>Call the <code>processUpdates()</code> method of all facets
     *     of the {@link UIColumn} children of this {@link UIData}.</li>
     * <li>Iterate over the set of rows that were included when this
     *     component was rendered (i.e. those defined by the <code>first</code>
     *     and <code>rows</code> properties), performing the following
     *     processing for each row:
     *     <ul>
     *     <li>Set the current <code>rowIndex</code> to the appropriate
     *         value for this row.</li>
     *     <li>If <code>isRowAvailable()</code> returns <code>true</code>,
     *         iterate over the children components of each {@link UIColumn}
     *         child of this {@link UIData} component, calling the
     *         <code>processUpdates()</code> method for each such child.</li>
     *     </ul></li>
     * <li>Set the current <code>rowIndex</code> to -1.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the current request
     *
     * @throws NullPointerException if <code>context</code> is <code>null</code>
     */
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!isRendered()) {
            return;
        }
        if (isNestedWithinUIData()) {
            setDataModel(null);
        }
        iterate(context, PhaseId.UPDATE_MODEL_VALUES);
        // This is not a EditableValueHolder, so no further processing is required

    }

    // --------------------------------------------------------- Protected Methods


    /**
     * <p>Return the internal {@link DataModel} object representing the data
     * objects that we will iterate over in this component's rendering.</p>
     * <p/>
     * <p>If the model has been cached by a previous call to {@link
     * #setDataModel}, return it.  Otherwise call {@link #getValue}.  If the
     * result is null, create an empty {@link ListDataModel} and return it.  If
     * the result is an instance of {@link DataModel}, return it.  Otherwise,
     * adapt the result as described in {@link #getValue} and return it.</p>
     */
    protected DataModel getDataModel() {

        // Return any previously cached DataModel instance
        if (this.model != null) {
            return (model);
        }

        // Synthesize a DataModel around our current value if possible
        Object current = getValue();
        if (current == null) {
            setDataModel(new ListDataModel(Collections.EMPTY_LIST));
        } else if (current instanceof DataModel) {
            setDataModel((DataModel) current);
        } else if (current instanceof List) {
            setDataModel(new ListDataModel((List) current));
        } else if (Object[].class.isAssignableFrom(current.getClass())) {
            setDataModel(new ArrayDataModel((Object[]) current));
        } else if (current instanceof ResultSet) {
            setDataModel(new ResultSetDataModel((ResultSet) current));
        } else if (current instanceof Result) {
            setDataModel(new ResultDataModel((Result) current));
        } else {
            setDataModel(new ScalarDataModel(current));
        }
        return (model);

    }

    /**
     * <p>Set the internal DataModel.  This <code>UIData</code> instance must
     * use the given {@link DataModel} as its internal value representation from
     * now until the next call to <code>setDataModel</code>.  If the given
     * <code>DataModel</code> is <code>null</code>, the internal
     * <code>DataModel</code> must be reset in a manner so that the next call to
     * {@link #getDataModel} causes lazy instantion of a newly refreshed
     * <code>DataModel</code>.</p>
     * <p/>
     * <p>Subclasses might call this method if they either want to restore the
     * internal <code>DataModel</code> during the <em>Restore View</em> phase or
     * if they want to explicitly refresh the current <code>DataModel</code> for
     * the <em>Render Response</em> phase.</p>
     *
     * @param dataModel the new <code>DataModel</code> or <code>null</code> to
     *                  cause the model to be refreshed.
     */

    protected void setDataModel(DataModel dataModel) {
        this.model = dataModel;
    }

    // ---------------------------------------------------- Private Methods


    /**
     * <p>Perform the appropriate phase-specific processing and per-row
     * iteration for the specified phase, as follows:
     * <ul>
     * <li>Set the <code>rowIndex</code> property to -1, and process the facets
     *     of this {@link UIData} component exactly once.</li>
     * <li>Set the <code>rowIndex</code> property to -1, and process the facets
     *     of the {@link UIColumn} children of this {@link UIData} component
     *     exactly once.</li>
     * <li>Iterate over the relevant rows, based on the <code>first</code>
     *     and <code>row</code> properties, and process the children
     *     of the {@link UIColumn} children of this {@link UIData} component
     *     once per row.</li>
     * </ul>
     *
     * @param context {@link FacesContext} for the current request
     * @param phaseId {@link PhaseId} of the phase we are currently running
     */
    private void iterate(FacesContext context, PhaseId phaseId) {

        // Process each facet of this component exactly once
        setRowIndex(-1);
        if (getFacetCount() > 0) {
            for (UIComponent facet : getFacets().values()) {
                if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                    facet.processDecodes(context);
                } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
                    facet.processValidators(context);
                } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                    facet.processUpdates(context);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }

        // Process each facet of our child UIColumn components exactly once
        setRowIndex(-1);
        if (getChildCount() > 0) {
            for (UIComponent column : getChildren()) {
                if (!(column instanceof UIColumn) || !column.isRendered()) {
                    continue;
                }
                if (column.getFacetCount() > 0) {
                    for (UIComponent columnFacet : column.getFacets().values()) {                      
                        if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                            columnFacet.processDecodes(context);
                        } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
                            columnFacet.processValidators(context);
                        } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                            columnFacet.processUpdates(context);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
        }

        // Iterate over our UIColumn children, once per row
        int processed = 0;
        int rowIndex = getFirst() - 1;
        int rows = getRows();

        while (true) {

            // Have we processed the requested number of rows?
            if ((rows > 0) && (++processed > rows)) {
                break;
            }

            // Expose the current row in the specified request attribute
            setRowIndex(++rowIndex);
            if (!isRowAvailable()) {
                break; // Scrolled past the last row
            }

            // Perform phase-specific processing as required
            // on the *children* of the UIColumn (facets have
            // been done a single time with rowIndex=-1 already)
            if (getChildCount() > 0) {
                for (UIComponent kid : getChildren()) {
                    if (!(kid instanceof UIColumn) || !kid.isRendered()) {
                        continue;
                    }
                    if (kid.getChildCount() > 0) {
                        for (UIComponent grandkid : kid.getChildren()) {
                            if (!grandkid.isRendered()) {
                                continue;
                            }
                            if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                                grandkid.processDecodes(context);
                            } else if (phaseId == PhaseId.PROCESS_VALIDATIONS) {
                                grandkid.processValidators(context);
                            } else if (phaseId == PhaseId.UPDATE_MODEL_VALUES) {
                                grandkid.processUpdates(context);
                            } else {
                                throw new IllegalArgumentException();
                            }
                        }
                    }
                }
            }

        }

        // Clean up after ourselves
        setRowIndex(-1);

    }


    /**
     * <p>Return <code>true</code> if we need to keep the saved
     * per-child state information.  This will be the case if any of the
     * following are true:</p>
     *
     * <ul>
     *
     * <li>any of the saved state corresponds to components that have
     * messages that must be displayed</li>
     *
     * <li>this <code>UIData</code> instance is nested inside of another
     * <code>UIData</code> instance</li>
     *
     * </ul>
     *
     * @param context {@link FacesContext} for the current request
     */
    private boolean keepSaved(FacesContext context) {

        if (!isNestedWithinUIData()) {
            // we're not nested, so we can rely on the fact that
            // baseClientId is not null.  Get all client IDs that have messages
            // and if any of them start with the baseClientId, then get the
            // messages and see if any are ERROR or FATAL.
            for (Iterator<String> ids = context.getClientIdsWithMessages();
                  ids.hasNext();) {
                String id = ids.next();
                if (id == null) {
                    // getClientIdsWithMessages() will return a null element
                    // for messages that don't have client IDs - not sure why
                    // it was spec'd that way....
                    continue;
                }
                if (id.startsWith(baseClientId)) {
                    for (Iterator<FacesMessage> msgs = context.getMessages(id);
                          msgs.hasNext();) {
                        FacesMessage msg = msgs.next();
                        if (FacesMessage.SEVERITY_ERROR.compareTo(msg.getSeverity()) >= 0) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            return true;
        }
        
    }

    private Boolean isNestedWithinUIData() {
        if (isNested == null) {
            UIComponent parent = this;
            while (null != (parent = parent.getParent())) {
                if (parent instanceof UIData) {
                    isNested = Boolean.TRUE;
                    break;
                }
            }
            if (isNested == null) {
                isNested = Boolean.FALSE;
            }
            return isNested;
        } else {
            return isNested;
        }
    }


    /**
     * <p>Restore state information for all descendant components, as described
     * for <code>setRowIndex()</code>.</p>
     */
    private void restoreDescendantState() {

        FacesContext context = getFacesContext();
        if (getChildCount() > 0) {
            for (UIComponent kid : getChildren()) {
                if (kid instanceof UIColumn) {
                    restoreDescendantState(kid, context);
                }
            }
        }

    }


    /**
     * <p>Restore state information for the specified component and its
     * descendants.</p>
     *
     * @param component Component for which to restore state information
     * @param context   {@link FacesContext} for the current request
     */
    private void restoreDescendantState(UIComponent component,
                                        FacesContext context) {

        // Reset the client identifier for this component
        String id = component.getId();
        component.setId(id); // Forces client id to be reset

        // Restore state for this component (if it is a EditableValueHolder)
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            String clientId = component.getClientId(context);
            SavedState state = saved.get(clientId);
            if (state == null) {
                state = new SavedState();
            }
            input.setValue(state.getValue());
            input.setValid(state.isValid());
            input.setSubmittedValue(state.getSubmittedValue());
            // This *must* be set after the call to setValue(), since
            // calling setValue() always resets "localValueSet" to true.
            input.setLocalValueSet(state.isLocalValueSet());
        }

        // Restore state for children of this component
        if (component.getChildCount() > 0) {
            for (UIComponent kid : component.getChildren()) {
                restoreDescendantState(kid, context);
            }
        }

        // Restore state for facets of this component
        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                restoreDescendantState(facet, context);
            }
        }

    }


    /**
     * <p>Save state information for all descendant components, as described for
     * <code>setRowIndex()</code>.</p>
     */
    private void saveDescendantState() {

        FacesContext context = getFacesContext();
        if (getChildCount() > 0) {
            for (UIComponent kid : getChildren()) {
                if (kid instanceof UIColumn) {
                    saveDescendantState(kid, context);
                }
            }
        }

    }


    /**
     * <p>Save state information for the specified component and its
     * descendants.</p>
     *
     * @param component Component for which to save state information
     * @param context   {@link FacesContext} for the current request
     */
    private void saveDescendantState(UIComponent component,
                                     FacesContext context) {

        // Save state for this component (if it is a EditableValueHolder)
        if (component instanceof EditableValueHolder) {
            EditableValueHolder input = (EditableValueHolder) component;
            String clientId = component.getClientId(context);
            SavedState state = saved.get(clientId);
            if (state == null) {
                state = new SavedState();
                saved.put(clientId, state);
            }
            state.setValue(input.getLocalValue());
            state.setValid(input.isValid());
            state.setSubmittedValue(input.getSubmittedValue());
            state.setLocalValueSet(input.isLocalValueSet());
        }

        // Save state for children of this component
        if (component.getChildCount() > 0) {
            for (UIComponent uiComponent : component.getChildren()) {
                saveDescendantState(uiComponent, context);
            }
        }

        // Save state for facets of this component
        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                saveDescendantState(facet, context);
            }
        }

    }

}

// ------------------------------------------------------------- Private Classes


// Private class to represent saved state information
@SuppressWarnings({"SerializableHasSerializationMethods",
      "NonSerializableFieldInSerializableClass"})
class SavedState implements Serializable {

    private static final long serialVersionUID = 2920252657338389849L;
    private Object submittedValue;

    Object getSubmittedValue() {
        return (this.submittedValue);
    }

    void setSubmittedValue(Object submittedValue) {
        this.submittedValue = submittedValue;
    }

    private boolean valid = true;

    boolean isValid() {
        return (this.valid);
    }

    void setValid(boolean valid) {
        this.valid = valid;
    }

    private Object value;

    Object getValue() {
        return (this.value);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    private boolean localValueSet;

    boolean isLocalValueSet() {
        return (this.localValueSet);
    }

    public void setLocalValueSet(boolean localValueSet) {
        this.localValueSet = localValueSet;
    }

    public String toString() {
        return ("submittedValue: " + submittedValue +
                " value: " + value +
                " localValueSet: " + localValueSet);
    }

}


// Private class to wrap an event with a row index
class WrapperEvent extends FacesEvent {


    public WrapperEvent(UIComponent component, FacesEvent event, int rowIndex) {
        super(component);
        this.event = event;
        this.rowIndex = rowIndex;
    }

    private FacesEvent event = null;
    private int rowIndex = -1;

    public FacesEvent getFacesEvent() {
        return (this.event);
    }

    public int getRowIndex() {
        return (this.rowIndex);
    }

    public PhaseId getPhaseId() {
        return (this.event.getPhaseId());
    }

    public void setPhaseId(PhaseId phaseId) {
        this.event.setPhaseId(phaseId);
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return (false);
    }

    public void processListener(FacesListener listener) {
        throw new IllegalStateException();
    }


}

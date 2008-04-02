/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
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

package components.components;

import javax.faces.context.FacesContext;
import javax.faces.component.UIOutput;
import javax.faces.el.ValueBinding;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIViewRoot;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import components.model.ChartItem;


/**
 * <p>{@link ChartComponent} is a JavaServer Faces component that can render 
 * given set of data as a bar or pie chart.</p>
 */

public class ChartComponent extends UIOutput {

    /**
     * <p>The standard component type for this component.</p>
     */
    public static final String COMPONENT_TYPE = "Chart";


    /**
     * <p>The standard component family for this component.</p>
     */
    public static final String COMPONENT_FAMILY = "Chart";
    
    /**
     * <p>Name of the servlet that renders the image.</p>
     */
    public static final String CHART_SERVLET_NAME = "ChartServlet";
    
    // ------------------------------------------------------ Instance Variables
    private String width = null;
    private String height = null;
    private String orientation = null;
    private String type= null;
    
    // --------------------------------------------------------------Constructors 

    public ChartComponent() {
        super();
        setRendererType(null);
    }

    
    // -------------------------------------------------------------- Properties
    /**
     * <p>Return the width of the chart</p>
     */
    public String getWidth() {
        if (null != this.width) {
            return this.width;
        }
        ValueBinding _vb = getValueBinding("width");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext());
        } else {
            return null;
        }
    }


    /**
     * <p>Set the width of the chart</p>
     *
     * @param width The new width of the chart
     */
    public void setWidth(String width) {
        this.width = width;
        
    }
    
    /**
     * <p>Return the height of the chart</p>
     */
    public String getHeight() {
        if (null != this.height) {
            return this.height;
        }
        ValueBinding _vb = getValueBinding("height");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext());
        } else {
            return null;
        }
    }


    /**
     * <p>Set the height of the chart</p>
     *
     * @param height The new height of the chart
     */
    public void setHeight(String height) {
        this.height = height;
    }
    
    /**
     * <p>Return the orientation of the chart</p>
     */
    public String getOrientation() {
        if (null != this.orientation) {
            return this.orientation;
        }
        ValueBinding _vb = getValueBinding("orientation");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext());
        } else {
            return null;
        }
    }


    /**
     * <p>Set the orientation of the chart</p>
     *
     * @param orientation The new orientation of the chart
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    
    /**
     * <p>Return the type of the chart</p>
     */
    public String getType() {
        if (null != this.type) {
            return this.type;
        }
        ValueBinding _vb = getValueBinding("type");
        if (_vb != null) {
            return (java.lang.String) _vb.getValue(getFacesContext());
        } else {
            return null;
        }
    }


    /**
     * <p>Set the type of the chart</p>
     *
     * @param type The new type of the chart
     */
    public void setType(String type) {
        this.type = type;
    }


    /**
     * <p>Return the component family for this component.</p>
     */
    public String getFamily() {

        return (COMPONENT_FAMILY);

    }
   
    // ----------------------------------------------------- StateHolder Methods
    /**
     * <p>Return the state to be saved for this component.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     */
    public Object saveState(FacesContext context) {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = width;
        values[2] = height;
        values[3] = orientation;
        return (values);
    }


    /**
     * <p>Restore the state for this component.</p>
     *
     * @param context <code>FacesContext</code> for the current request
     * @param state   State to be restored
     *
     * @throws IOException if an input/output error occurs
     */
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        width = (String) values[1];
        height = (String) values[2];
        orientation = (String) values[3];
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
        placeChartDataInScope();
        // render an image that would initiate a separate to a URL pointing 
        // back into the webapp passing in whatever parameters are needed to 
        // create the dynamic image.
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("img", this);
        writeIdAttributeIfNecessary(context, writer, this);
        writer.writeAttribute("src", src(context, this), "value");
        writer.endElement("img");
    }
    
    // ----------------------------------------------------- Private Methods
    
    protected void writeIdAttributeIfNecessary(FacesContext context,
                                               ResponseWriter writer,
                                               UIComponent component) {
        String id;
        if ((id = component.getId()) != null &&
            !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
            try {
                writer.writeAttribute("id", component.getClientId(context),
                                      "id");
            } catch (IOException e) {
               /* if (log.isDebugEnabled()) {
                    log.debug("Can't write ID attribute" + e.getMessage());
                } */
            }
        }
    }
    
    private String src(FacesContext context, UIComponent component) {
        String contextPath = context.getExternalContext().getRequestContextPath();
        StringBuffer result = new StringBuffer(contextPath);
        result.append("/");
        result.append(CHART_SERVLET_NAME);
        // append parameters to be passed to be servlet
        result.append("?height=");
        if ( getHeight() != null ) {
            result.append(getHeight());
        }
        result.append("&");
        
        result.append("width=");
        if ( getWidth() != null ) {
            result.append(getWidth());
        }
        result.append("&");
     
        result.append("orientation=");
        if ( getOrientation() != null ) {
            result.append(getOrientation());
        }
        result.append("&");
        
        result.append("type=");
        if ( type != null ) {
            result.append(type);
        }
      
        return (result.toString());
     }
    
    /** Place the appropriate data for chart in session scope, so that
     * it will be there when the separate request for the image is
     * processed by the chart servlet. This servlet is responsible for 
     * writing out the chart as an image into the respone stream.
     */
    protected void placeChartDataInScope() {
        int i = 0;
        ChartItem[] chartItems = null;
        // if there is a value attribute set on the bean, data for the chart is
        // retrieved from the bean. If not, we build an array of ChartItem
        // using the children of this component.
        chartItems = (ChartItem[]) getValue();
        if (chartItems == null || chartItems.length == 0 ) {
            chartItems = new ChartItem[getChildCount()];
            Iterator kids = this.getChildren().iterator();
            while ( kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if (kid instanceof ChartItemComponent) {
                    ChartItemComponent ci = (ChartItemComponent) kid;
                    ChartItem item = (ChartItem) ci.getValue();
                    if (item == null) {
                        int itemVal = 
                            (new Integer((String)ci.getItemValue())).intValue();
                        item = new ChartItem(ci.getItemLabel(),itemVal,
                            ci.getItemColor());
                    }
                    chartItems[i] = item;
                    ++i;
                }
            }
        }

        Map sessionMap =
            getFacesContext().getExternalContext().getSessionMap();
        sessionMap.put("chart", chartItems);
    }
    
}

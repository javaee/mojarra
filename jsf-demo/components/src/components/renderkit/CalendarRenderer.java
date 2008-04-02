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

package components.renderkit;

import java.util.Locale;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIForm;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;

/**
 * <p>Renderer for {@link CalendarComponent} in an HTML environment.</p>
 */

public class CalendarRenderer extends BaseRenderer {
    
    /**
     * Calendar dynamically generates children components for which
     * it is responsible.
     */
    public boolean getRendersChildren() {
        return true;
    }
    
    //**************************************************************************
    // Rendering methods
    
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeBegin(context, component);
        //com.sun.faces.util.DebugUtil.printTree(component.getParent(), System.out);
        
        UIComponent calendar = component;
        ResponseWriter writer = context.getResponseWriter();
        String contextPath = context.getExternalContext().getRequestContextPath();
        contextPath = contextPath + "/";
        
        // <link rel="STYLESHEET" type="text/css" href="styles/calendar.css"/>
        // FIXME --> Ideally should only appear once on a page if multiple calendar tags
        writer.startElement("link", calendar);
        writer.writeAttribute("rel", "STYLESHEET", null);
        writer.writeAttribute("type", "text/css", null);
        writer.writeURIAttribute("href", contextPath + "images/calendar.css", null);
        writer.endElement("link");
        
        // <script language="JavaScript" src="javascript/simplecalendar.js" type="text/javascript"/>
        // FIXME --> Ideally should only appear once on a page if multiple calendar tags
        writer.startElement("script", calendar);
        writer.writeAttribute("language", "JavaScript", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeURIAttribute("src", contextPath + "images/simplecalendar.js", null);
        writer.endElement("script");
        
        // <table border="0" cellspacing="0" cellpadding="0">
        //   <tr>
        //     <td>
        writer.startElement("table", calendar);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("cellspacing", "0", null);
        writer.writeAttribute("cellpadding", "0", null);
        writer.startElement("tr", calendar);
        writer.startElement("td", calendar);
        
        // The child UIInput component will be rendered under this <td>
    }
    
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeEnd(context, component);
        
        UIComponent calendar = component;
        ResponseWriter writer = context.getResponseWriter();
        String contextPath = context.getExternalContext().getRequestContextPath();
        contextPath = contextPath + "/";
        
        // Get formatting used according to locale so we can set the
        // DHTML to the same.
        Locale locale = context.getViewRoot().getLocale();
        String dateFormatPattern = "MM/dd/yy";
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        if (format instanceof SimpleDateFormat) {
            dateFormatPattern = ((SimpleDateFormat)format).toLocalizedPattern();
        }
        dateFormatPattern = dateFormatPattern.toLowerCase();
        
        // </td> (end the child UIInput component)
        writer.endElement("td");
        
        // <td>
        //   <a href="javascript: void(0);"
        //      onClick="g_Calendar.show(event, form_o_date, false); return false;">
        //   <img src="..." ... />
        //   </a>
        // </td>
        // FIXME: using the form 'id' does not work in the javascript code of 'onclick'
        // FIXME: although it should according to HTML 4.0. For now, just use forms[0]
        // FIXME: String formName = getMyForm(context, component).getClientId(context);
        String formName = "forms[0]";
        String dateName = component.findComponent("date").getClientId(context);
        
        writer.startElement("td", calendar);
        writer.startElement("a", calendar);
        writer.writeAttribute("href", "javascript: void(0);", null);
        writer.writeAttribute("onClick", "g_Calendar.show(event, document." + formName + "[\"" + dateName + "\"], false, '" + dateFormatPattern + "'); return false;", null);//FIXME
        writer.startElement("img", calendar);
        writer.writeURIAttribute("src", contextPath + "images/calendar.gif", null);
        writer.writeAttribute("name", "imgCalendar", null);
        writer.writeAttribute("width", "34", null);
        writer.writeAttribute("height", "21", null);
        writer.writeAttribute("border", "0", null);
        //writer.writeAttribute("alt", "", null);
        writer.endElement("img");
        writer.endElement("a");
        writer.endElement("td");
        
        //   </tr>
        // </table>
        writer.endElement("tr");
        writer.endElement("table");
    }
    
    //**************************************************************************
    // Utility methods
    
    protected UIForm getMyForm(FacesContext context, UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }
}

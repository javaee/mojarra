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

package components.renderkit;


import components.components.CalendarComponent;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.DateTimeConverter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * <p>Renderer for {@link CalendarComponent} in an HTML environment.</p>
 */

public class CalendarRenderer extends BaseRenderer {


    // -------------------------------------------------------- Renderer Methods


    /**
     * <p>{@link CalendarComponent}  dynamically generates children components
     * for which it is responsible.</p>
     */
    public boolean getRendersChildren() {

        return (true);

    }


    public void encodeBegin(FacesContext context, UIComponent component)
        throws IOException {

        super.encodeBegin(context, component);

        UIComponent calendar = component;
        ResponseWriter writer = context.getResponseWriter();

        // Render the pre-field markup
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

        // Update the Locale of the converter for our date component
        UIInput date = (UIInput) component.findComponent("date");
        DateTimeConverter conv = (DateTimeConverter) date.getConverter();
        conv.setLocale(context.getViewRoot().getLocale());
        date.setSubmittedValue(null);

    }


    public void encodeEnd(FacesContext context, UIComponent component)
        throws IOException {
        super.encodeEnd(context, component);

        UIComponent calendar = component;
        ResponseWriter writer = context.getResponseWriter();
        String contextPath = context.getExternalContext()
            .getRequestContextPath();
        
        // Get formatting used according to locale so we can set the
        // DHTML to the same.
        Locale locale = context.getViewRoot().getLocale();
        String dateFormatPattern = "MM/dd/yyyy";
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT,
                                                       locale);
        if (format instanceof SimpleDateFormat) {
            dateFormatPattern =
                ((SimpleDateFormat) format).toLocalizedPattern();
        }
        dateFormatPattern = dateFormatPattern.toLowerCase();
        
        // </td> (end the child UIInput component)
        writer.endElement("td");
        
        // <td>
        //   <a href="javascript: void(0);"
        //      onclick="g_Calendar.show(event, form_o_date, false); return false;">
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
        // writer.writeAttribute("onclick", "g_Calendar.show(event, document." + formName + "[\"" + dateName + "\"], false, '" + dateFormatPattern + "'); return false;", null);//FIXME
        writer.writeAttribute("onclick",
                              "g_Calendar.show(event, document." + formName +
                              "['" +
                              dateName +
                              "'], false, '" +
                              dateFormatPattern +
                              "'); return false;",
                              null);//FIXME
        writer.startElement("img", calendar);
        writer.writeURIAttribute("src", contextPath + "/images/calendar.gif",
                                 null);
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


}

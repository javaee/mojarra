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

package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;

import com.sun.faces.sandbox.component.YuiCalendar;
import com.sun.faces.sandbox.util.MessageFactory;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author Jason Lee
 */
public class YuiCalendarRenderer extends HtmlBasicRenderer {//Renderer {
    private static final String scriptIds[] = {
        YuiConstants.JS_YAHOO_DOM_EVENT
        ,YuiConstants.JS_CALENDAR
        ,YuiConstants.JS_SANDBOX_HELPER
        ,YuiConstants.JS_YUI_CALENDAR_HELPER
    };
    
    private static final String cssIds[] = {
        YuiConstants.CSS_CALENDAR,
        YuiConstants.CSS_SANDBOX
    };
    
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String DATE_FORMAT_YAHOO = "MM/dd/yyyy";
    
    public YuiCalendarRenderer() {
    }
    
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }

        Calendar tempcal = Calendar.getInstance();
        YuiCalendar cal = (YuiCalendar) component;
        
        if (null != cal.getValue() && cal.getValue() instanceof Date) {
            Date oldDate = (Date) cal.getValue();
            tempcal.setTime(oldDate);
        }        
        
        ResponseWriter writer = context.getResponseWriter();
        for (int i = 0; i < scriptIds.length; i++) {
            Util.linkJavascript(writer, scriptIds[i], true);
        }
        
        for (int i = 0; i < cssIds.length; i++) {
            Util.linkStyleSheet(writer, cssIds[i]);
        }
//        YuiRendererHelper.renderSandboxJavaScript(context, context.getResponseWriter(), component);
//        YuiRendererHelper.renderSandboxStylesheet(context, context.getResponseWriter(), component);
    }
    
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }
        YuiCalendar cal = (YuiCalendar) component;
        
        if (Boolean.TRUE.equals(cal.getShowMenus()==true)) {
            renderYearSelectField(context, context.getResponseWriter(), cal);
            renderMonthSelectField(context, context.getResponseWriter(), cal);
            renderDaySelectField(context, context.getResponseWriter(), cal);
        } else {
            renderInputField(context, cal);
        }
        
        writeCalendarMarkUp(context, context.getResponseWriter(), cal);
    }
    
    protected void renderInputField(FacesContext context, YuiCalendar component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", component);
        writer.writeAttribute("id", component.getClientId(context), "id");
        writer.writeAttribute("name", component.getClientId(context), "name");
        writer.writeAttribute("type", "text", "type");
        writer.writeAttribute("size", "10", "size");
        writer.writeAttribute("readonly", "readonly", "readonly");
        writer.writeAttribute("value", getStringValue(context, component), "value");
        if (component.getOnChange() != null) {
            writer.writeAttribute("onchange", component.getOnChange(), "onchange");
        }
        writer.endElement("input");
    }
    
    protected void renderDaySelectField(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        writer.startElement("select", component);
        writer.writeAttribute("id", clientId+"Day", null);
        writer.writeAttribute("name", clientId+"Day", null);
        
        // TODO should be internationalized
        writer.startElement("option", component);
        writer.writeAttribute("value", "", null);
        writer.writeText("Day", null);
        writer.endElement("option");
        writer.endElement("select");
    }
    
    protected void renderMonthSelectField(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        writer.startElement("select", component);
        writer.writeAttribute("id", clientId+"Month", null);
        writer.writeAttribute("name", clientId+"Month", null);
        
        // TODO should be internationalized.
        writer.startElement("option", component);
        writer.writeAttribute("value", "", null);
        writer.writeText("Month", null);
        writer.endElement("option");
        writer.endElement("select");
    }
    
    protected void renderYearSelectField(FacesContext context, ResponseWriter writer, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        writer.startElement("select", component);
        writer.writeAttribute("id", clientId+"Year", null);
        writer.writeAttribute("name", clientId+"Year", null);
        
        // TODO should be internationalized
        writer.startElement("option", component);
        writer.writeAttribute("value", "", null);
        writer.writeText("Year", null);
        writer.endElement("option");
        
        writer.endElement("select");
    }
    
    protected void writeCalendarMarkUp(FacesContext context,
            ResponseWriter writer, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        YuiCalendar cal = (YuiCalendar) component;
        Date date = (Date) cal.getValue();
        String minDateString = cal.getMinDate();
        String maxDateString = cal.getMaxDate();
        
        String javaScript = String.format("new SANDBOX.Calendar('%sContainer','%sTrigger', '%sDay', '%sMonth', '%sYear','%s','%s','%s',%s,%s,%s,%s,%s,%s,'%s','%s',%s);",
                component.getId(), component.getId(), clientId, clientId, clientId, clientId,
                (date != null) ? new SimpleDateFormat("MM/yyyy").format(date) : new SimpleDateFormat("MM/yyyy").format(new Date()),
                (date != null) ? new SimpleDateFormat("MM/dd/yyyy").format(date) : "null",
                cal.getMultiSelect().toString(),
                cal.getShowWeekdays().toString(),
                cal.getStartWeekday().toString(),
                cal.getShowWeekHeader().toString(),
                cal.getShowWeekFooter().toString(),
                cal.getHideBlankWeeks().toString(),
                (minDateString != null) ? minDateString : "null",
                (maxDateString != null) ? maxDateString : "null",
                cal.getShowMenus().toString());
        
        renderSupportingMarkup(context, writer, component);
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "style");
        writer.writeText("YAHOO.util.Event.onContentReady('"+ component.getId() +"Container', function() {" + javaScript + "});", null);
        writer.endElement("script");
    }
    
    protected void renderSupportingMarkup(FacesContext context,
            ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement("img", component);
        writer.writeAttribute("id", component.getId() + "Trigger", "id");
        writer.writeAttribute("alt", "calendar", "alt");
        writer.writeAttribute("style", "vertical-align: middle", "style");
        writer.writeAttribute("src", Util.generateStaticUri("/sandbox/calendar_icon.gif"), "src");
        writer.endElement("img");
        
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getId() + "Container", "id");
        writer.writeAttribute("style", "display: none;", "style");
        writer.endElement("div");
    }
    
    public void decode(FacesContext context, UIComponent component) {
        if (context == null) {
            throw new NullPointerException("param 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("param 'component' is null");
        }

        // suppress rendering if "rendered" property on the component is false.
        if (!component.isRendered()) {
            return;
        }
        
        if (!(component instanceof YuiCalendar)) {
            // decode needs to be invoked only for components that are
            // instances or subclasses of UIInput.
            return;
        }
        
        YuiCalendar cal = (YuiCalendar) component;
        
        // TODO: If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
//        if (RendererHelper.componentIsDisabledOrReadonly(component)) {
//            return;
//        }
        
        String clientId = component.getClientId(context);
        assert(clientId != null);
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        
        Calendar tempcal = Calendar.getInstance();
        Date newDate = null;
        
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_YAHOO);
        Date minDate = null;
        if (null != cal.getMinDate()) {
            try {
                tempcal.setTime(format.parse(cal.getMinDate()));
                minDate = tempcal.getTime();
            } catch(ParseException e) {
                throw new IllegalArgumentException("Invalid mindate", e);
            }
        }
        Date maxDate = null;
        if (null != cal.getMaxDate()) {
            try {
                tempcal.setTime(format.parse(cal.getMaxDate()));
                maxDate = tempcal.getTime();
            } catch(ParseException e) {
                throw new IllegalArgumentException("Invalid maxdate", e);
            }
        }
        format.applyPattern(DATE_FORMAT);
        
        if (Boolean.TRUE.equals(cal.getShowMenus()==true)) {
            String daySelectId = clientId + "Day";
            String monthSelectId = clientId + "Month";
            String yearSelectId = clientId + "Year";
            
            if (requestMap.containsKey(daySelectId) &&
                    requestMap.containsKey(monthSelectId) &&
                    requestMap.containsKey(yearSelectId)) {
                
                String y = requestMap.get(yearSelectId);
                String m = requestMap.get(monthSelectId);
                String d = requestMap.get(daySelectId);
                
                if ("".equals(d) || "".equals(m) || "".equals(y)) {
                    if ("".equals(d) && "".equals(m) && "".equals(y)) {
                        if ( Boolean.TRUE.equals(cal.isRequired()==true)) {
                            addRequiredMessage(context, cal);
                        } else {
                            cal.setSubmittedValue(null);
                        }
                    } else {
                        addInvalidMessage(context, cal);
                        cal.setSubmittedValue(null);
                    }
                    return;
                }
                
                tempcal.set((int) Integer.parseInt(y), (int) Integer.parseInt(m),(int) Integer.parseInt(d));
                newDate = tempcal.getTime();
                if (null != minDate) {
                    if (newDate.before(minDate)) {
                        addInvalidMessage(context, cal);
                        return;
                    }
                }
                if (null != maxDate) {
                    if (newDate.after(maxDate)) {
                        addInvalidMessage(context, cal);
                        return;
                    }
                }
                
                String newValue = format.format(newDate);
                cal.setSubmittedValue(newValue);
            }
        } else {
            // Don't overwrite the value unless you have to!
            if (requestMap.containsKey(clientId)) {
                String newValue = requestMap.get(clientId);
                
                try {
                    newDate = format.parse(newValue);
                    if (null != minDate) {
                        if (newDate.before(minDate)) {
                            addInvalidMessage(context, cal);
                            return;
                        }
                    }
                    if (null != maxDate) {
                        if (newDate.after(maxDate)) {
                            addInvalidMessage(context, cal);
                            return;
                        }
                    }
                } catch(ParseException pe) {
//                    throw new IllegalStateException("Could not parse submitted date value", pe);
                }
                
                cal.setSubmittedValue(newValue);
            }
        }
    }
    
    protected Converter getConverter(FacesContext context, UIComponent component) {
        ValueHolder vh = (ValueHolder)component;
        Converter converter = vh.getConverter();
        if (converter == null) {
            DateTimeConverter dtc = (DateTimeConverter) context.getApplication().createConverter("javax.faces.DateTime");
            dtc.setPattern(DATE_FORMAT);
            dtc.setLocale(context.getViewRoot().getLocale());
            dtc.setTimeZone(TimeZone.getDefault());
            converter = dtc;
        }
        return converter;
    }
    
    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        return getConverter(context, component).getAsObject(context, component, (String)submittedValue);
    }
    
    protected String getStringValue(FacesContext context, UIComponent component) throws ConverterException {
        return getConverter(context, component).getAsString(context, component, ((YuiCalendar)component).getValue());
    }

    private void addRequiredMessage(FacesContext context, YuiCalendar cal) {
        FacesMessage message;
        if (null != cal.getRequiredMessage()) {
            message = new FacesMessage(cal.getRequiredMessage());
        } else {
            message = MessageFactory.getMessage(context, YuiCalendar.REQUIRED_MESSAGE_ID, MessageFactory.getLabel(context, cal));
        }
        context.addMessage(cal.getClientId(context), message);
    }
    
    
    private void addInvalidMessage(FacesContext context, YuiCalendar cal) {
        FacesMessage message = MessageFactory.getMessage(context, YuiCalendar.INVALID_MESSAGE_ID, MessageFactory.getLabel(context, cal));
        context.addMessage(cal.getClientId(context), message);
    }
    
    /*
    private void addUpdateMessage(FacesContext context, YuiCalendar cal) {
        FacesMessage message = MessageFactory.getMessage(context, cal.UPDATE_MESSAGE_ID, MessageFactory.getLabel(context, cal));
        context.addMessage(cal.getClientId(context), message);
    }
    
    private void addConversionMessage(FacesContext context, YuiCalendar cal) {
        FacesMessage message;
        if (null != cal.getConverterMessage()) {
            message = new FacesMessage(cal.getConverterMessage());
        } else {
            message = MessageFactory.getMessage(context, cal.CONVERSION_MESSAGE_ID, MessageFactory.getLabel(context, cal));
        }
        context.addMessage(cal.getClientId(context), message);
    }
    */
}
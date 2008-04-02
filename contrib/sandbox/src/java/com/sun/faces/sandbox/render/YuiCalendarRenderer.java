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

import org.apache.shale.remoting.Mechanism;

import com.sun.faces.sandbox.component.YuiCalendar;
import com.sun.faces.sandbox.util.Util;
import com.sun.faces.sandbox.util.YuiConstants;

/**
 * @author Jason Lee
 */
public class YuiCalendarRenderer extends HtmlBasicRenderer {//Renderer {
    private static final String scriptIds[] = {
        YuiConstants.JS_YAHOO_DOM_EVENT,
        YuiConstants.JS_CALENDAR,
        YuiConstants.JS_YUI_CALENDAR_HELPER
    };
    
    private static final String cssIds[] = {
        YuiConstants.CSS_CALENDAR
    };
    
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String DATE_FORMAT_YAHOO = "MM/dd/yyyy";
    
    // Can't be final, but treat as if they were.
    private int MIN_YEAR = 1900;
    private int MAX_YEAR = 2100;
    
    private Date minDate;
    private String minDateString;
    private Date maxDate;
    private String maxDateString;
    
    public YuiCalendarRenderer() {
        Calendar tempcal = Calendar.getInstance();
        MIN_YEAR = tempcal.get(Calendar.YEAR) - 100;
        MAX_YEAR = tempcal.get(Calendar.YEAR) + 100;
        minDateString = "01/01/" + MIN_YEAR;
        maxDateString = "12/31/" + MAX_YEAR;
    }
    
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        
        YuiCalendar cal = (YuiCalendar) component;

        if (null != cal.getMinDate()) {
            minDateString = cal.getMinDate();
        }        
        if (null != cal.getMaxDate()) {
            maxDateString = cal.getMaxDate();
        }
        
        Calendar tempcal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_YAHOO);
        try {
            tempcal.setTime(format.parse(minDateString));
            minDate = tempcal.getTime();
            tempcal.setTime(format.parse(maxDateString));
            maxDate = tempcal.getTime();
        } catch(ParseException e) {
            throw new IllegalArgumentException("Invalid mindate or maxdate", e);
        }
        
        if (null != cal.getValue()) {
            Date date = (Date) cal.getValue();
            if (date.before(minDate) || date.after(maxDate)) {
                addErrorMessage(context, cal.getClientId(context));
            }
        }
        
        for (int i = 0; i < scriptIds.length; i++) {
            Util.getXhtmlHelper().linkJavascript(context, component,
                    context.getResponseWriter(), Mechanism.CLASS_RESOURCE,
                    scriptIds[i]);
        }
        for (int i = 0; i < cssIds.length; i++) {
            Util.getXhtmlHelper().linkStylesheet(context, component,
                    context.getResponseWriter(), Mechanism.CLASS_RESOURCE,
                    cssIds[i]);
        }
//        YuiRendererHelper.renderSandboxJavaScript(context, context.getResponseWriter(), component);
        YuiRendererHelper.renderSandboxStylesheet(context, context.getResponseWriter(), component);
    }
    
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
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
        
        Calendar tempcal = Calendar.getInstance();
        tempcal.setTime(minDate);
        int minYear = tempcal.get(Calendar.YEAR);
        tempcal.setTime(maxDate);
        int maxYear = tempcal.get(Calendar.YEAR);
        
        for (int i = minYear; i < maxYear+1; i++) {
            writer.startElement("option", component);
            writer.writeAttribute("value", i, null);
            writer.writeText(i, null);
            writer.endElement("option");
        }
        writer.endElement("select");
    }
    
    protected void writeCalendarMarkUp(FacesContext context,
            ResponseWriter writer, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        YuiCalendar cal = (YuiCalendar) component;
        Date date = (Date) cal.getValue();
        
        String javaScript = String.format("new RISANDBOX.Calendar('%sContainer','%sTrigger', '%sDay', '%sMonth', '%sYear','%s','%s','%s',%s,%s,%s,%s,%s,%s,'%s','%s',%s);",
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
        writer.writeText(javaScript, null);
        writer.endElement("script");
    }
    
    protected void renderSupportingMarkup(FacesContext context,
            ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement("img", component);
        writer.writeAttribute("id", component.getId() + "Trigger", "id");
        writer.writeAttribute("alt", "calendar", "alt");
        writer.writeAttribute("src",
                Util.getXhtmlHelper().mapResourceId(context, Mechanism.CLASS_RESOURCE,"/yui/assets/calendar_icon.gif"), "src");
        writer.endElement("img");
        
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getId() + "Container", "id");
        writer.writeAttribute("style", "display: none;", "style");
        writer.endElement("div");
    }
    
    public void decode(FacesContext context, UIComponent component) {
        if (context == null) {
            throw new NullPointerException("Argument Error: Parameter 'context' is null");
        }
        if (component == null) {
            throw new NullPointerException("Argument Error: Parameter 'component' is null");
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
        SimpleDateFormat format = new SimpleDateFormat();
        Date newDate = null;
        
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
                    if ("".equals(d) && "".equals(m) && "".equals(y) && Boolean.FALSE.equals(cal.isRequired()==true)) {
                        return;
                    }
                    // TODO use requiredMessage
                    addErrorMessage(context, clientId);
                    return;
                }
                
                tempcal.set((int) Integer.parseInt(y), (int) Integer.parseInt(m),(int) Integer.parseInt(d));
                newDate = tempcal.getTime();
                if (newDate.before(minDate) || newDate.after(maxDate)) {
                    addErrorMessage(context, clientId);
                    return;
                }
                
                format.applyPattern(DATE_FORMAT);
                String newValue = format.format(newDate);
                cal.setSubmittedValue(newValue);
            }
        } else {
            // Don't overwrite the value unless you have to!
            if (requestMap.containsKey(clientId)) {
                String newValue = requestMap.get(clientId);
                format.applyPattern(DATE_FORMAT);
                
                try {
                    newDate = format.parse(newValue);
                } catch(ParseException pe) {
                    throw new IllegalStateException("Could not parse submitted date value", pe);
                }
                
                if (newDate.before(minDate) || newDate.after(maxDate)) {
                    addErrorMessage(context, clientId);
                    return;
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
    
    private void addErrorMessage(FacesContext context, String clientId) {
        // TODO Move to resource bundle
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "invalid date", "invalid date");
        context.addMessage(clientId, msg);
    }
}
package com.sun.faces.sandbox.render;

import java.io.IOException;     
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

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

    private final int MIN_YEAR = 1900;
    private final int MAX_YEAR = 2100;
    
    // TODO: i18n
    private static final String monthNames[] = {
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };

    public YuiCalendarRenderer() {
        //
    }

    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
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
        
        if (Boolean.TRUE.equals(cal.getShowSelects()==true)) {
            Date date = (Date) cal.getValue();
            if (date == null) {
                date = new Date();
            }
            Calendar tempcal = Calendar.getInstance();
            tempcal.setTime(date);
            int currDay = tempcal.get(Calendar.DAY_OF_MONTH);
            int currMonth = tempcal.get(Calendar.MONTH);
            int currYear = tempcal.get(Calendar.YEAR);
            renderYearSelectField(context, context.getResponseWriter(), cal, currYear);
            renderMonthSelectField(context, context.getResponseWriter(), cal, currMonth);
            renderDaySelectField(context, context.getResponseWriter(), cal, currDay);
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
        if (component.getShowInput() == false) {
            writer.writeAttribute("style", "display: none", "style");
        }
        writer.endElement("input");
    }

    protected void renderDaySelectField(FacesContext context, ResponseWriter writer, UIComponent component, int currDay) throws IOException {
        String clientId = component.getClientId(context);
        writer.startElement("select", component);
        writer.writeAttribute("id", component.getId()+"Day", null);
        writer.writeAttribute("name", component.getId()+"Day", null);
        
        // TODO should be internationalized
        writer.startElement("option", component);
        writer.writeText("Day", null);
        writer.endElement("option");
        
        for (int i = 1; i < 32; i++) {
            writer.startElement("option", component);
            writer.writeAttribute("value", i, null);
            if (i == currDay) {
                writer.writeAttribute("selected", Boolean.TRUE, null);
            }
            writer.writeText(i, null);
            writer.endElement("option");
        }
        writer.endElement("select");
    }
    
    protected void renderMonthSelectField(FacesContext context, ResponseWriter writer, UIComponent component, int currMonth) throws IOException {
        String clientId = component.getClientId(context);
        
        writer.startElement("select", component);
        writer.writeAttribute("id", component.getId()+"Month", null);
        writer.writeAttribute("name", component.getId()+"Month", null);

        // TODO should be internationalized
        writer.startElement("option", component);
        writer.writeText("Month", null);
        writer.endElement("option");
        
        for (int i = 0; i < 12; i++) {
            writer.startElement("option", component);
            writer.writeAttribute("value", i, null);
            if (i == currMonth) {
                writer.writeAttribute("selected", Boolean.TRUE, null);
            }
            writer.writeText(monthNames[i], null);
            writer.endElement("option");
        }
        writer.endElement("select");
    }
    
    protected void renderYearSelectField(FacesContext context, ResponseWriter writer, UIComponent component, int currYear) throws IOException {
        String clientId = component.getClientId(context);
        writer.startElement("select", component);
        writer.writeAttribute("id", component.getId()+"Year", null);
        writer.writeAttribute("name", component.getId()+"Year", null);
        
        // TODO should be internationalized
        writer.startElement("option", component);
        writer.writeText("Year", null);
        writer.endElement("option");
        
        YuiCalendar cal = (YuiCalendar) component;
        Calendar tempcal = Calendar.getInstance();
        
        int minYear, maxYear;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        
        if (cal.getMaxDate() != null) {
            try {
                tempcal.setTime(format.parse(cal.getMaxDate()));
                maxYear = tempcal.get(Calendar.YEAR);
            } catch(ParseException e) {
                maxYear = MAX_YEAR;
            }
        } else {
            maxYear = MAX_YEAR;
        }
        
        if (cal.getMinDate() != null) {
            try {
                tempcal.setTime(format.parse(cal.getMinDate()));
                minYear = tempcal.get(Calendar.YEAR);
            } catch(ParseException e) {
                minYear = MIN_YEAR;
            }
        } else {
            minYear = MIN_YEAR;
        }
        
        // If we've set an illogical year then reset to defaults.
        if (minYear > maxYear) {
            minYear = MIN_YEAR;
            maxYear = MAX_YEAR;
        }
        
        for (int i = minYear; i < maxYear+1; i++) {
            writer.startElement("option", component);
            writer.writeAttribute("value", i, null);
            if (i == currYear) {
                writer.writeAttribute("selected", Boolean.TRUE, null);
            }
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
        if (date == null) {
            date = new Date();
        }
        String javaScript = String.format("new RISANDBOX.Calendar('%sContainer','%sTrigger', '%sDay', '%sMonth', '%sYear','%s','%s','%s',%s,%s,%s,%s,%s,%s,'%s','%s',%s,%s);",
                component.getId(), component.getId(), component.getId(),component.getId(),component.getId(), clientId,
                (date != null) ? new SimpleDateFormat("MM/yyyy").format(date) : "null",
                (date != null) ? new SimpleDateFormat("MM/dd/yyyy").format(date) : "null",
                cal.getMultiSelect().toString(),
                cal.getShowWeekdays().toString(),
                cal.getStartWeekday().toString(),
                cal.getShowWeekHeader().toString(),
                cal.getShowWeekFooter().toString(),
                cal.getHideBlankWeeks().toString(),
                (cal.getMinDate() != null) ? cal.getMinDate() : Integer.toString(MIN_YEAR),
                (cal.getMaxDate() != null) ? cal.getMaxDate() : Integer.toString(MAX_YEAR),
                cal.getShowInput().toString(),
                cal.getShowSelects().toString());
        
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

        // TODO: If the component is disabled, do not change the value of the
        // component, since its state cannot be changed.
//        if (RendererHelper.componentIsDisabledOrReadonly(component)) {
//            return;
//        }

        String clientId = component.getClientId(context);
        assert(clientId != null);
        Map<String, String> requestMap = context.getExternalContext().getRequestParameterMap();
        // Don't overwrite the value unless you have to!
        if (requestMap.containsKey(clientId)) {
            String newValue = requestMap.get(clientId);
            ((YuiCalendar) component).setSubmittedValue(newValue);
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

    protected String getStringValue (FacesContext context, UIComponent component) throws ConverterException {
        return getConverter(context, component).getAsString(context, component, ((YuiCalendar)component).getValue());
    }
}

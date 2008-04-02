package com.sun.faces.sandbox.render;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
    }

    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        renderInputField(context, (YuiCalendar)component);
        writeCalendarMarkUp(context, context.getResponseWriter(), component);
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

    protected void writeCalendarMarkUp(FacesContext context,
            ResponseWriter writer, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        YuiCalendar cal = (YuiCalendar) component;
        Date date = (Date) cal.getValue();
        if (date == null) {
            date = new Date();
        }

        String javaScript = String.format("new RISANDBOX.Calendar('%sContainer','%sTrigger','%s','%s','%s',%s,%s,%s,%s,%s);",
                component.getId(), component.getId(), clientId,
                (date != null) ? new SimpleDateFormat("MM/yyyy").format(date) : "null",
                (date != null) ? new SimpleDateFormat("MM/dd/yyyy").format(date) : "null",
                cal.getMultiSelect().toString(),
                cal.getShowWeekdays().toString(),
                cal.getStartWeekday().toString(),
                cal.getShowWeekHeader().toString(),
                cal.getShowWeekFooter().toString(),
                cal.getHideBlankWeeks().toString());
        
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

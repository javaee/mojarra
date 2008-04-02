/*
 * $Id: DateTimeConverter.java,v 1.4 2003/09/20 02:43:03 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>{@link Converter} implementation for <code>java.util.Date</code>
 * values.</p>
 *
 * <p>The <code>getAsObject()</code> method parses a String into a
 * <code>java.util.Date</code>, according to the following algorithm:</p>
 * <ul>
 * <li>If the specified String is null or zero length, return
 *     a <code>null</code>.  Otherwise, trim leading and trailing
 *     whitespace before proceeding.</li>
 * <li>If the <code>parseLocale</code> property is not null,
 *     use that <code>Locale</code> for managing parsing.  Otherwise, use the
 *     <code>Locale</code> from the <code>FacesContext</code>.</li>
 * <li>If a <code>pattern</code> has been specified, its syntax must conform
 *     the rules specified by <code>java.text.SimpleDateFormat</code>.  Such
 *     a pattern will be used to parse, and the <code>type</code>,
 *     <code>dateStyle</code>, and <code>timeStyle</code> properties
 *     will be ignored.</li>
 * <li>If a <code>pattern</code> has not been specified, parsing will be based
 *     on the <code>type</code> property, which expects a date value, a time
 *     value, or both.  Any date and time values included will be parsed in
 *     accordance to the styles specified by <code>dateStyle</code> and
 *     <code>timeStyle</code>, respectively.</li>
 * <li>In all cases, parsing must be non-lenient; the given string must
 *     strictly adhere to the parsing format.</li>
 * </ul>
 *
 * <p>The <code>getAsString()</code> method expects a value of type
 * <code>java.util.Date</code> (or a subclass), and creates a formatted
 * String according to the following algorithm:</p>
 * <ul>
 * <li>If the specified value is null or a zero-length String, return
 *     a <code>null</code> String.</li>
 * <li>If the <code>parseLocale</code> property is not null,
 *     use that <code>Locale</code> for managing formatting.  Otherwise, use the
 *     <code>Locale</code> from the <code>FacesContext</code>.</li>
 * <li>If a <code>pattern</code> has been specified, its syntax must conform
 *     the rules specified by <code>java.text.SimpleDateFormat</code>.  Such
 *     a pattern will be used to format, and the <code>type</code>,
 *     <code>dateStyle</code>, and <code>timeStyle</code> properties
 *     will be ignored.</li>
 * <li>If a <code>pattern</code> has not been specified, formatting will be
 *     based on the <code>type</code> property, which includes a date value,
 *     a time value, or both into the formatted String.  Any date and time
 *     values included will be formatted in accordance to the styles specified
 *     by <code>dateStyle</code> and <code>timeStyle</code>, respectively.</li>
 * </ul>
 */

public class DateTimeConverter implements Converter, StateHolder {


    // ------------------------------------------------------ Instance Variables


    private String dateStyle = "default";
    private boolean lenient = false;
    private Locale parseLocale = null;
    private String pattern = null;
    private String timeStyle = "default";
    private TimeZone timeZone = null;
    private String type = "date";


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the style to be used to format or parse dates.  If not set,
     * the default value will be "default".</p>
     */
    public String getDateStyle() {

        return (this.dateStyle);

    }


    /**
     * <p>Set the style to be used to format or parse dates.  Valid values
     * are <code>default</code>, <code>short</code>, <code>medium</code>,
     * <code>long</code>, and <code>full</code>.
     * An invalid value will cause a {@link ConverterException} when
     * <code>getAsObject()</code> or <code>getAsString()</code> is called.</p>
     *
     * @param dateStyle The new style code
     */
    public void setDateStyle(String dateStyle) {

        this.dateStyle = dateStyle;

    }


    /**
     * <p>Return the "is parsing lenient" flag for this {@link Converter}.
     * If not set, the default value will be <code>false</code>.</p>
     */
    public boolean isLenient() {

        return (this.lenient);

    }


    /**
     * <p>Set the "is parsing lenient" flag for this {@link Converter}.
     * This flag value affects conversion of Strings to dates in the same
     * manner as that specified by the <code>lenient</code> property on
     * <code>java.text.DateFormat</code>.</p>
     *
     * @param lenient The new lenient flag
     */
    public void setLenient(boolean lenient) {

        this.lenient = lenient;

    }


    /**
     * <p>Return the <code>Locale</code> to be used when parsing numbers.
     * If this value is <code>null</code>, the <code>Locale</code> stored
     * in {@link FacesContext} for the current request will be utilized.</p>
     */
    public Locale getParseLocale() {

        return (this.parseLocale);

    }


    /**
     * <p>Set the <code>Locale</code> to be used when parsing numbers.
     * If set to <code>null</code>, the <code>Locale</code> stored in
     * {@link FacesContext} for the current request will be utilized.</p>
     *
     * @param parseLocale The new <code>Locale</code> (or <code>null</code>)
     */
    public void setParseLocale(Locale parseLocale) {

        this.parseLocale = parseLocale;

    }


    /**
     * <p>Return the format pattern to be used when formatting and
     * parsing dates and times.</p>
     */
    public String getPattern() {

        return (this.pattern);

    }


    /**
     * <p>Set the format pattern to be used when formatting and parsing
     * dates and times.  Valid values are those supported by
     * <code>java.text.DateFormat</code>.
     * An invalid value will cause a {@link ConverterException} when
     * <code>getAsObject()</code> or <code>getAsString()</code> is called.</p>
     *
     * @param pattern The new format pattern
     */
    public void setPattern(String pattern) {

        this.pattern = pattern;

    }


    /**
     * <p>Return the style to be used to format or parse times.  If not set,
     * the default value will be "default".</p>
     */
    public String getTimeStyle() {

        return (this.timeStyle);

    }


    /**
     * <p>Set the style to be used to format or parse times.  Valid values
     * are <code>default</code>, <code>short</code>, <code>medium</code>,
     * <code>long</code>, and <code>full</code>.
     * An invalid value will cause a {@link ConverterException} when
     * <code>getAsObject()</code> or <code>getAsString()</code> is called.</p>
     *
     * @param timeStyle The new style code
     */
    public void setTimeStyle(String timeStyle) {

        this.timeStyle = timeStyle;

    }


    /**
     * <p>Return the <code>TimeZone</code> used to interpret a time value.
     * If not set, the default time zone of the server will be used.</p>
     */
    public TimeZone getTimeZone() {

        return (this.timeZone);

    }


    /**
     * <p>Set the <code>TimeZone</code> used to interpret a time value.</p>
     *
     * @param timeZone The new time zone
     */
    public void setTimeZone(TimeZone timeZone) {

        this.timeZone = timeZone;

    }


    /**
     * <p>Return the type of value to be formatted or parsed.
     * If not modified, the default type is <code>date</code>.</p>
     */
    public String getType() {

        return (this.type);

    }


    /**
     * <p>Set the type of value to be formatted or parsed.
     * Valid values are <code>both</code>, <code>date</code>, or
     * <code>time</code>.
     * An invalid value will cause a {@link ConverterException} when
     * <code>getAsObject()</code> or <code>getAsString()</code> is called.</p>
     *
     * @param type The new number style
     */
    public void setType(String type) {

        this.type = type;

    }


    // ------------------------------------------------------- Converter Methods


    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) throws ConverterException {

        try {

            // If the specified value is null or zero-length, return null
            if (value == null) {
                return (null);
            }
            value = value.trim();
            if (value.length() < 1) {
                return (null);
            }

            // Identify the Locale to use for parsing
            Locale locale = getLocale(context, component);

            // Create and configure the parser to be used
            DateFormat parser =
                getDateFormat(context, component, locale);
            if (timeZone != null) {
                parser.setTimeZone(timeZone);
            }

            // Perform the requested parsing
            return (parser.parse(value));

        } catch (ConverterException e) {
            throw e;
        } catch (ParseException e) {
            // PENDING(craigmcc) - i18n
            throw new ConverterException("Error parsing '" + value + "'");
        }


    }


    public String getAsString(FacesContext context, UIComponent component,
                              Object value) throws ConverterException {

        try {

            // If the specified value is null or zero-length, return null
            if ((value == null) || value.equals("")) {
                return (null);
            }

            // Identify the Locale to use for formatting
            Locale locale = getLocale(context, component);

            // Create and configure the formatter to be used
            DateFormat formatter =
                getDateFormat(context, component, locale);
            if (timeZone != null) {
                formatter.setTimeZone(timeZone);
            }

            // Perform the requested formatting
            return (formatter.format(value));

        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            throw new ConverterException(e);
        }

    }


    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return a <code>DateFormat</code> instance to use for formatting
     * and parsing in this {@link Converter}.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param component The {@link UIComponent} for which we are converting
     * @param locale The <code>Locale</code> used to select formatting
     *  and parsing conventions
     *
     * @exception ConverterException if no instance can be created
     */
    private DateFormat getDateFormat
        (FacesContext context, UIComponent component, Locale locale) {

        // PENDING(craigmcc) - Implement pooling if needed for performance?

        DateFormat df = null;
        if ((pattern != null) && !pattern.equals("")) {
            df = new SimpleDateFormat(pattern, locale);
        } else if (type.equals("both")) {
            df = DateFormat.getDateTimeInstance
                (getStyle(dateStyle), getStyle(timeStyle));
        } else if (type.equals("date")) {
            df = DateFormat.getDateInstance(getStyle(dateStyle));
        } else if (type.equals("time")) {
            df = DateFormat.getTimeInstance(getStyle(timeStyle));
        } else {
            // PENDING(craigmcc) - i18n
            throw new ConverterException
                (new IllegalArgumentException(type));
        }
        df.setLenient(lenient);
        return (df);

    }


    /**
     * <p>Return the <code>Locale</code> we will use for localizing our
     * formatting and parsing processing.</p>
     *
     * @param context The {@link FacesContext} for the current request
     * @param component The {@link UIComponent} for which we are converting
     */
    private Locale getLocale(FacesContext context, UIComponent component) {

        // PENDING(craigmcc) - JSTL localization context?
        Locale locale = parseLocale;
        if (locale == null) {
            locale = context.getLocale();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return (locale);

    }


    /**
     * <p>Return the style constant for the specified style name.</p>
     *
     * @param name Name of the style for which to return a constant
     *
     * @exception ConverterException if the style name is not valid
     */
    private int getStyle(String name) {

        if (name.equals("default")) {
            return (DateFormat.DEFAULT);
        } else if (name.equals("short")) {
            return (DateFormat.SHORT);
        } else if (name.equals("medium")) {
            return (DateFormat.MEDIUM);
        } else if (name.equals("long")) {
            return (DateFormat.LONG);
        } else if (name.equals("full")) {
            return (DateFormat.FULL);
        } else {
            // PENDING(craigmcc) - i18n
            throw new ConverterException("Invalid style '" + name + "'");
        }

    }


    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        Object values[] = new Object[7];
        values[0] = dateStyle;
        values[1] = lenient ? Boolean.TRUE : Boolean.FALSE;
        values[2] = parseLocale;
        values[3] = pattern;
        values[4] = timeStyle;
        values[5] = timeZone;
        values[6] = type;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        dateStyle = (String) values[0];
        lenient = ((Boolean) values[1]).booleanValue();
        parseLocale = (Locale) values[2];
        pattern = (String) values[3];
        timeStyle = (String) values[4];
        timeZone = (TimeZone) values[5];
        type = (String) values[6];

    }


    private boolean transientFlag = false;


    public boolean isTransient() {
        return (transientFlag);
    }


    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }


}

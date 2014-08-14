/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package javax.faces.convert;


import javax.faces.component.UIComponent;
import javax.faces.component.PartialStateHolder;
import javax.faces.context.FacesContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * <p><span class="changed_modified_2_0_rev_a">{@link Converter}</span>
 * implementation for <code>java.util.Date</code> values.</p>
 *
 * <p>The <code>getAsObject()</code> method parses a String into a
 * <code>java.util.Date</code>, according to the following algorithm:</p>
 * <ul>
 * <li>If the specified String is null, return
 * a <code>null</code>.  Otherwise, trim leading and trailing
 * whitespace before proceeding.</li>
 * <li>If the specified String - after trimming - has a zero length,
 * return <code>null</code>.</li>
 * <li>If the <code>locale</code> property is not null,
 * use that <code>Locale</code> for managing parsing.  Otherwise, use the
 * <code>Locale</code> from the <code>UIViewRoot</code>.</li>
 * <li>If a <code>pattern</code> has been specified, its syntax must conform
 * the rules specified by <code>java.text.SimpleDateFormat</code>.  Such
 * a pattern will be used to parse, and the <code>type</code>,
 * <code>dateStyle</code>, and <code>timeStyle</code> properties
 * will be ignored.</li>
 * <li>If a <code>pattern</code> has not been specified, parsing will be based
 * on the <code>type</code> property, which expects a date value, a time
 * value, or both.  Any date and time values included will be parsed in
 * accordance to the styles specified by <code>dateStyle</code> and
 * <code>timeStyle</code>, respectively.</li>
 * <li>If a <code>timezone</code> has been specified, it must be passed
 * to the underlying <code>DateFormat</code> instance.  Otherwise
 * the "GMT" timezone is used.</li>
 * <li>In all cases, parsing must be non-lenient; the given string must
 * strictly adhere to the parsing format.</li>
 * </ul>
 * <p/>
 * <p>The <code>getAsString()</code> method expects a value of type
 * <code>java.util.Date</code> (or a subclass), and creates a formatted
 * String according to the following algorithm:</p>
 * <ul>
 * <li>If the specified value is null, return a zero-length String.</li>
 * <li>If the specified value is a String, return it unmodified.</li>
 * <li>If the <code>locale</code> property is not null,
 * use that <code>Locale</code> for managing formatting.  Otherwise, use the
 * <code>Locale</code> from the <code>UIViewRoot</code>.</li>
 * <li>If a <code>timezone</code> has been specified, it must be passed
 * to the underlying <code>DateFormat</code> instance.  Otherwise
 * the "GMT" timezone is used.</li>
 * <li>If a <code>pattern</code> has been specified, its syntax must conform
 * the rules specified by <code>java.text.SimpleDateFormat</code>.  Such
 * a pattern will be used to format, and the <code>type</code>,
 * <code>dateStyle</code>, and <code>timeStyle</code> properties
 * will be ignored.</li>
 * <li>If a <code>pattern</code> has not been specified, formatting will be
 * based on the <code>type</code> property, which includes a date value,
 * a time value, or both into the formatted String.  Any date and time
 * values included will be formatted in accordance to the styles specified
 * by <code>dateStyle</code> and <code>timeStyle</code>, respectively.</li>
 * </ul>
 */

public class DateTimeConverter implements Converter, PartialStateHolder {

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String CONVERTER_ID = "javax.faces.DateTime";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Date</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String DATE_ID =
         "javax.faces.converter.DateTimeConverter.DATE";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Time</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String TIME_ID =
         "javax.faces.converter.DateTimeConverter.TIME";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>DateTime</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String DATETIME_ID =
         "javax.faces.converter.DateTimeConverter.DATETIME";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion of the <code>DateTime</code> value to
     * <code>String</code> fails.   The message format string for this message
     * may optionally include the following placeholders:
     * <ul>
     * <li><code>{0}</code> relaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String STRING_ID =
         "javax.faces.converter.STRING";


    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");

    // ------------------------------------------------------ Instance Variables


    private String dateStyle = "default";
    private Locale locale = null;
    private String pattern = null;
    private String timeStyle = "default";
    private TimeZone timeZone = DEFAULT_TIME_ZONE;
    private String type = "date";

    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the style to be used to format or parse dates.  If not set,
     * the default value, <code>default</code>, is returned.</p>
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

        clearInitialState();
        this.dateStyle = dateStyle;

    }


    /**
     * <p>Return the <code>Locale</code> to be used when parsing or formatting
     * dates and times. If not explicitly set, the <code>Locale</code> stored
     * in the {@link javax.faces.component.UIViewRoot} for the current
     * request is returned.</p>
     */
    public Locale getLocale() {

        if (this.locale == null) {
            this.locale =
                 getLocale(FacesContext.getCurrentInstance());
        }
        return (this.locale);

    }


    /**
     * <p>Set the <code>Locale</code> to be used when parsing or formatting
     * dates and times.  If set to <code>null</code>, the <code>Locale</code>
     * stored in the {@link javax.faces.component.UIViewRoot} for the current
     * request will be utilized.</p>
     *
     * @param locale The new <code>Locale</code> (or <code>null</code>)
     */
    public void setLocale(Locale locale) {

        clearInitialState();
        this.locale = locale;

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
     * <code>java.text.SimpleDateFormat</code>.
     * An invalid value will cause a {@link ConverterException} when
     * <code>getAsObject()</code> or <code>getAsString()</code> is called.</p>
     *
     * @param pattern The new format pattern
     */
    public void setPattern(String pattern) {

        clearInitialState();
        this.pattern = pattern;

    }


    /**
     * <p>Return the style to be used to format or parse times.  If not set,
     * the default value, <code>default</code>, is returned.</p>
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

        clearInitialState();
        this.timeStyle = timeStyle;

    }


    /**
     * <p>Return the <code>TimeZone</code> used to interpret a time value.
     * If not explicitly set, the default time zone of <code>GMT</code>
     * returned.</p>
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

        clearInitialState();
        this.timeZone = timeZone;

    }


    /**
     * <p>Return the type of value to be formatted or parsed.
     * If not explicitly set, the default type, <code>date</code>
     * is returned.</p>
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
     * @param type The new date style
     */
    public void setType(String type) {

        clearInitialState();
        this.type = type;

    }

    // ------------------------------------------------------- Converter Methods

    /**
     * @throws ConverterException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {

        if (context == null || component == null) {
            throw new NullPointerException();
        }

        Object returnValue = null;
        DateFormat parser = null;

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
            Locale locale = getLocale(context);

            // Create and configure the parser to be used
            parser = getDateFormat(locale);
            if (null != timeZone) {
                parser.setTimeZone(timeZone);
            }

            // Perform the requested parsing
            returnValue = parser.parse(value);
        } catch (ParseException e) {
            if ("date".equals(type)) {
                throw new ConverterException(MessageFactory.getMessage(
                     context, DATE_ID, value,
                     parser.format(new Date(System.currentTimeMillis())),
                     MessageFactory.getLabel(context, component)), e);
            } else if ("time".equals(type)) {
                throw new ConverterException(MessageFactory.getMessage(
                     context, TIME_ID, value,
                     parser.format(new Date(System.currentTimeMillis())),
                     MessageFactory.getLabel(context, component)), e);
            } else if ("both".equals(type)) {
                throw new ConverterException(MessageFactory.getMessage(
                     context, DATETIME_ID, value,
                     parser.format(new Date(System.currentTimeMillis())),
                     MessageFactory.getLabel(context, component)), e);
            }
        } catch (Exception e) {
            throw new ConverterException(e);
        }
        return returnValue;
    }

    /**
     * @throws ConverterException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    public String getAsString(FacesContext context, UIComponent component,
                              Object value) {

        if (context == null || component == null) {
            throw new NullPointerException();
        }

        try {

            // If the specified value is null, return a zero-length String
            if (value == null) {
                return "";
            }

            // If the incoming value is still a string, play nice
            // and return the value unmodified
            if (value instanceof String) {
                return (String) value;
            }

            // Identify the Locale to use for formatting
            Locale locale = getLocale(context);

            // Create and configure the formatter to be used
            DateFormat formatter = getDateFormat(locale);
            if (null != timeZone) {
                formatter.setTimeZone(timeZone);
            }

            // Perform the requested formatting
            return (formatter.format(value));

        } catch (ConverterException e) {
            throw new ConverterException(MessageFactory.getMessage(
                 context, STRING_ID, value,
                 MessageFactory.getLabel(context, component)), e);
        } catch (Exception e) {
            throw new ConverterException(MessageFactory.getMessage(
                 context, STRING_ID, value,
                 MessageFactory.getLabel(context, component)), e);
        }
    }

    // --------------------------------------------------------- Private Methods


    /**
     * <p>Return a <code>DateFormat</code> instance to use for formatting
     * and parsing in this {@link Converter}.</p>
     *
     * @param locale  The <code>Locale</code> used to select formatting
     *                and parsing conventions
     * @throws ConverterException if no instance can be created
     */
    private DateFormat getDateFormat(Locale locale) {

        // PENDING(craigmcc) - Implement pooling if needed for performance?

        if (pattern == null && type == null) {
            throw new IllegalArgumentException("Either pattern or type must" +
                 " be specified.");
        }

        DateFormat df;
        if (pattern != null) {
            df = new SimpleDateFormat(pattern, locale);
        } else if (type.equals("both")) {
            df = DateFormat.getDateTimeInstance
                 (getStyle(dateStyle), getStyle(timeStyle), locale);
        } else if (type.equals("date")) {
            df = DateFormat.getDateInstance(getStyle(dateStyle), locale);
        } else if (type.equals("time")) {
            df = DateFormat.getTimeInstance(getStyle(timeStyle), locale);
        } else {
            // PENDING(craigmcc) - i18n
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        df.setLenient(false);
        return (df);

    }


    /**
     * <p>Return the <code>Locale</code> we will use for localizing our
     * formatting and parsing processing.</p>
     *
     * @param context The {@link FacesContext} for the current request
     */
    private Locale getLocale(FacesContext context) {

        // PENDING(craigmcc) - JSTL localization context?
        Locale locale = this.locale;
        if (locale == null) {
            locale = context.getViewRoot().getLocale();
        }
        return (locale);

    }


    /**
     * <p>Return the style constant for the specified style name.</p>
     *
     * @param name Name of the style for which to return a constant
     * @throws ConverterException if the style name is not valid
     */
    private static int getStyle(String name) {

        if ("default".equals(name)) {
            return (DateFormat.DEFAULT);
        } else if ("short".equals(name)) {
            return (DateFormat.SHORT);
        } else if ("medium".equals(name)) {
            return (DateFormat.MEDIUM);
        } else if ("long".equals(name)) {
            return (DateFormat.LONG);
        } else if ("full".equals(name)) {
            return (DateFormat.FULL);
        } else {
            // PENDING(craigmcc) - i18n
            throw new ConverterException("Invalid style '" + name + '\'');
        }

    }

    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[6];
            values[0] = dateStyle;
            values[1] = locale;
            values[2] = pattern;
            values[3] = timeStyle;
            values[4] = timeZone;
            values[5] = type;
            return (values);
        }
        return null;

    }


    public void restoreState(FacesContext context, Object state) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (state != null) {
            Object values[] = (Object[]) state;
            dateStyle = (String) values[0];
            locale = (Locale) values[1];
            pattern = (String) values[2];
            timeStyle = (String) values[3];
            timeZone = (TimeZone) values[4];
            type = (String) values[5];
        }

    }


    private boolean transientFlag = false;


    public boolean isTransient() {
        return (transientFlag);
    }


    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }

    private boolean initialState;

    public void markInitialState() {
        initialState = true;
    }

    public boolean initialStateMarked() {
        return initialState;
    }

    public void clearInitialState() {
        initialState = false;
    }
}

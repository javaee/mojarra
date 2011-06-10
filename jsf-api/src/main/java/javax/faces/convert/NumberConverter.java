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


import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.PartialStateHolder;
import javax.faces.context.FacesContext;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


/**
 * <p><span class="changed_modified_2_0_rev_a">{@link Converter}</span>
 * implementation for <code>java.lang.Number</code> values.</p>
 *
 * <p>The <code>getAsObject()</code> method parses a String into an
 * <code>java.lang.Double</code> or <code>java.lang.Long</code>, according
 * to the following algorithm:</p>
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
 * the rules specified by <code>java.text.DecimalFormat</code>.  Such
 * a pattern will be used to parse, and the <code>type</code> property
 * will be ignored.</li>
 * <li>If a <code>pattern</code> has not been specified, parsing will be based
 * on the <code>type</code> property, which expects a currency, a number,
 * or a percent.  The parse pattern for currencies, numbers, and
 * percentages is determined by calling the
 * <code>getCurrencyInstance()</code>, <code>getNumberInstance()</code>,
 * or <code>getPercentInstance()</code> method of the
 * <code>java.text.NumberFormat</code> class, passing in the selected
 * <code>Locale</code>.</li>
 * <li>If the <code>integerOnly</code> property has been set to true, only
 * the integer portion of the String will be parsed.  See the JavaDocs
 * for the <code>setParseIntegerOnly()</code> method of the
 * <code>java.text.NumberFormat</code> class for more information.</li>
 * </ul>
 * <p/>
 * <p>The <code>getAsString()</code> method expects a value of type
 * <code>java.lang.Number</code> (or a subclass), and creates a formatted
 * String according to the following algorithm:</p>
 * <ul>
 * <li>If the specified value is null, return a zero-length String.</li>
 * <li>If the specified value is a String, return it unmodified.</li>
 * <li>If the <code>locale</code> property is not null,
 * use that <code>Locale</code> for managing formatting.  Otherwise, use the
 * <code>Locale</code> from the <code>FacesContext</code>.</li>
 * <li>If a <code>pattern</code> has been specified, its syntax must conform
 * the rules specified by <code>java.text.DecimalFormat</code>.  Such
 * a pattern will be used to format, and the <code>type</code> property
 * (along with related formatting options described in the next paragraph)
 * will be ignored.</li>
 * <li>If a <code>pattern</code> has not been specified, formatting will be
 * based on the <code>type</code> property, which formats the value as a
 * currency, a number, or a percent.  The format pattern for currencies,
 * numbers, and percentages is determined by calling the
 * percentages is determined by calling the
 * <code>getCurrencyInstance()</code>, <code>getNumberInstance()</code>,
 * or <code>getPercentInstance()</code> method of the
 * <code>java.text.NumberFormat</code> class, passing in the selected
 * <code>Locale</code>.  In addition, the following properties will be
 * applied to the format pattern, if specified:
 * <ul>
 * <li>If the <code>groupingUsed</code> property is <code>true</code>, the
 * <code>setGroupingUsed(true)</code> method on the corresponding
 * <code>NumberFormat</code> instance will be called.</li>
 * <li>The minimum and maximum number of digits in the integer and
 * fractional portions of the result will be configured based on
 * any values set for the <code>maxFractionDigits</code>,
 * <code>maxIntegerDigits</code>, <code>minFractionDigits</code>,
 * and <code>minIntegerDigits</code> properties.</li>
 * <li>If the type is set to <code>currency</code>, it is also possible
 * to configure the currency symbol to be used, using either the
 * <code>currencyCode</code> or <code>currencySymbol</code> properties.
 * If both are set, the value for <code>currencyCode</code> takes
 * precedence on a JDK 1.4 (or later) JVM; otherwise, the value
 * for <code>currencySymbol</code> takes precedence.</li>
 * </ul></li>
 * </ul>
 */

public class NumberConverter implements Converter, PartialStateHolder {

    // ------------------------------------------------------ Manifest Constants


    /**
     * <p>The standard converter id for this converter.</p>
     */
    public static final String CONVERTER_ID = "javax.faces.Number";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Number</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String CURRENCY_ID =
         "javax.faces.converter.NumberConverter.CURRENCY";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Number</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>                              HA
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String NUMBER_ID =
         "javax.faces.converter.NumberConverter.NUMBER";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Number</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String PATTERN_ID =
         "javax.faces.converter.NumberConverter.PATTERN";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion to <code>Number</code> fails.  The message format
     * string for this message may optionally include the following
     * placeholders:
     * <ul>
     * <li><code>{0}</code> replaced by the unconverted value.</li>
     * <li><code>{1}</code> replaced by an example value.</li>
     * <li><code>{2}</code> replaced by a <code>String</code> whose value
     * is the label of the input component that produced this message.</li>
     * </ul></p>
     */
    public static final String PERCENT_ID =
         "javax.faces.converter.NumberConverter.PERCENT";

    /**
     * <p>The message identifier of the {@link javax.faces.application.FacesMessage} to be created if
     * the conversion of the <code>Number</code> value to
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


     private static final String NBSP = "\u00a0";

    // ------------------------------------------------------ Instance Variables


    private String currencyCode = null;
    private String currencySymbol = null;
    private Boolean groupingUsed = true;
    private Boolean integerOnly = false;
    private Integer maxFractionDigits;
    private Integer maxIntegerDigits;
    private Integer minFractionDigits;
    private Integer minIntegerDigits;
    private Locale locale = null;
    private String pattern = null;
    private String type = "number";

    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the ISO 4217 currency code used by <code>getAsString()</code>
     * with a <code>type</code> of <code>currency</code>.  If not set,
     * the value used will be based on the formatting <code>Locale</code>.</p>
     */
    public String getCurrencyCode() {

        return (this.currencyCode);

    }


    /**
     * <p>Set the ISO 4217 currency code used by <code>getAsString()</code>
     * with a <code>type</code> of <code>currency</code>.</p>
     *
     * @param currencyCode The new currency code
     */
    public void setCurrencyCode(String currencyCode) {

        clearInitialState();
        this.currencyCode = currencyCode;

    }


    /**
     * <p>Return the currency symbol used by <code>getAsString()</code>
     * with a <code>type</code> of <code>currency</code>.  If not set,
     * the value used will be based on the formatting <code>Locale</code>.</p>
     */
    public String getCurrencySymbol() {

        return (this.currencySymbol);

    }


    /**
     * <p>Set the currency symbol used by <code>getAsString()</code>
     * with a <code>type</code> of <code>currency</code>.</p>
     *
     * @param currencySymbol The new currency symbol
     */
    public void setCurrencySymbol(String currencySymbol) {

        clearInitialState();
        this.currencySymbol = currencySymbol;

    }


    /**
     * <p>Return <code>true</code> if <code>getAsString</code> should include
     * grouping separators if necessary.  If not modified, the default value
     * is <code>true</code>.</p>
     */
    public boolean isGroupingUsed() {

        return (this.groupingUsed != null ? this.groupingUsed : true);

    }


    /**
     * <p>Set the flag indicating whether <code>getAsString()</code> should
     * include grouping separators if necessary.</p>
     *
     * @param groupingUsed The new grouping used flag
     */
    public void setGroupingUsed(boolean groupingUsed) {

        clearInitialState();
        this.groupingUsed = groupingUsed;

    }


    /**
     * <p>Return <code>true</code> if only the integer portion of the given
     * value should be returned from <code>getAsObject()</code>.  If not
     * modified, the default value is <code>false</code>.</p>
     */
    public boolean isIntegerOnly() {

        return (this.integerOnly != null ? this.integerOnly : false);

    }


    /**
     * <p>Set to <code>true</code> if only the integer portion of the given
     * value should be returned from <code>getAsObject()</code>.</p>
     *
     * @param integerOnly The new integer-only flag
     */
    public void setIntegerOnly(boolean integerOnly) {

        clearInitialState();
        this.integerOnly = integerOnly;

    }


    /**
     * <p>Return the maximum number of digits <code>getAsString()</code> should
     * render in the fraction portion of the result.</p>
     */
    public int getMaxFractionDigits() {

        return (this.maxFractionDigits != null ? this.maxFractionDigits : 0);

    }


    /**
     * <p>Set the maximum number of digits <code>getAsString()</code> should
     * render in the fraction portion of the result.  If not set, the number of
     * digits depends on the value being converted.</p>
     *
     * @param maxFractionDigits The new limit
     */
    public void setMaxFractionDigits(int maxFractionDigits) {

        clearInitialState();
        this.maxFractionDigits = maxFractionDigits;

    }


    /**
     * <p>Return the maximum number of digits <code>getAsString()</code> should
     * render in the integer portion of the result.</p>
     */
    public int getMaxIntegerDigits() {

        return (this.maxIntegerDigits != null ? this.maxIntegerDigits : 0);

    }


    /**
     * <p>Set the maximum number of digits <code>getAsString()</code> should
     * render in the integer portion of the result.  If not set, the number of
     * digits depends on the value being converted.</p>
     *
     * @param maxIntegerDigits The new limit
     */
    public void setMaxIntegerDigits(int maxIntegerDigits) {

        clearInitialState();
        this.maxIntegerDigits = maxIntegerDigits;

    }


    /**
     * <p>Return the minimum number of digits <code>getAsString()</code> should
     * render in the fraction portion of the result.</p>
     */
    public int getMinFractionDigits() {

        return (this.minFractionDigits != null ? this.minFractionDigits : 0);

    }


    /**
     * <p>Set the minimum number of digits <code>getAsString()</code> should
     * render in the fraction portion of the result.  If not set, the number of
     * digits depends on the value being converted.</p>
     *
     * @param minFractionDigits The new limit
     */
    public void setMinFractionDigits(int minFractionDigits) {

        clearInitialState();
        this.minFractionDigits = minFractionDigits;

    }


    /**
     * <p>Return the minimum number of digits <code>getAsString()</code> should
     * render in the integer portion of the result.</p>
     */
    public int getMinIntegerDigits() {

        return (this.minIntegerDigits != null ? this.minIntegerDigits : 0);

    }


    /**
     * <p>Set the minimum number of digits <code>getAsString()</code> should
     * render in the integer portion of the result.  If not set, the number of
     * digits depends on the value being converted.</p>
     *
     * @param minIntegerDigits The new limit
     */
    public void setMinIntegerDigits(int minIntegerDigits) {

        clearInitialState();
        this.minIntegerDigits = minIntegerDigits;

    }


    /**
     * <p>Return the <code>Locale</code> to be used when parsing numbers.
     * If this value is <code>null</code>, the <code>Locale</code> stored
     * in the {@link javax.faces.component.UIViewRoot} for the current request
     * will be utilized.</p>
     */
    public Locale getLocale() {

        if (this.locale == null) {
            this.locale =
                 getLocale(FacesContext.getCurrentInstance());
        }
        return (this.locale);

    }


    /**
     * <p>Set the <code>Locale</code> to be used when parsing numbers.
     * If set to <code>null</code>, the <code>Locale</code> stored in the
     * {@link javax.faces.component.UIViewRoot} for the current request
     * will be utilized.</p>
     *
     * @param locale The new <code>Locale</code> (or <code>null</code>)
     */
    public void setLocale(Locale locale) {

        clearInitialState();
        this.locale = locale;

    }


    /**
     * <p>Return the format pattern to be used when formatting and
     * parsing numbers.</p>
     */
    public String getPattern() {

        return (this.pattern);

    }


    /**
     * <p>Set the format pattern to be used when formatting and parsing
     * numbers.  Valid values are those supported by
     * <code>java.text.DecimalFormat</code>.
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
     * <p>Return the number type to be used when formatting and parsing numbers.
     * If not modified, the default type is <code>number</code>.</p>
     */
    public String getType() {

        return (this.type);

    }


    /**
     * <p>Set the number type to be used when formatting and parsing numbers.
     * Valid values are <code>currency</code>, <code>number</code>, or
     * <code>percent</code>.
     * An invalid value will cause a {@link ConverterException} when
     * <code>getAsObject()</code> or <code>getAsString()</code> is called.</p>
     *
     * @param type The new number style
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
        NumberFormat parser = null;

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
            parser = getNumberFormat(locale);
            if (((pattern != null) && pattern.length() != 0)
                 || "currency".equals(type)) {
                configureCurrency(parser);
            }
            parser.setParseIntegerOnly(isIntegerOnly());
            boolean groupSepChanged = false;
            // BEGIN HACK 4510618
            // This lovely bit of code is for a workaround in some
            // oddities in the JDK's parsing code.
            // See:  http://bugs.sun.com/view_bug.do?bug_id=4510618
            if (parser instanceof DecimalFormat) {
                DecimalFormat dParser = (DecimalFormat) parser;

                // Take a small hit in performance to avoid a loss in
                // precision due to DecimalFormat.parse() returning Double
                ValueExpression ve = component.getValueExpression("value");
                if (ve != null) {
                    Class<?> expectedType = ve.getType(context.getELContext());
                    if (expectedType != null && expectedType.isAssignableFrom(BigDecimal.class)) {
                        dParser.setParseBigDecimal(true);
                    }
                }
                DecimalFormatSymbols symbols =
                      dParser.getDecimalFormatSymbols();
                if (symbols.getGroupingSeparator() == '\u00a0') {
                    groupSepChanged = true;
                    String tValue;
                    if (value.contains(NBSP)) {
                        tValue = value.replace('\u00a0', ' ');
                    } else {
                        tValue = value;
                    }
                    symbols.setGroupingSeparator(' ');
                    dParser.setDecimalFormatSymbols(symbols);
                    try {
                        return dParser.parse(tValue);
                    } catch (ParseException pe) {
                        if (groupSepChanged) {
                            symbols.setGroupingSeparator('\u00a0');
                            dParser.setDecimalFormatSymbols(symbols);
                        }
                    }
                }
            }
            // END HACK 4510618

            // Perform the requested parsing
            returnValue = parser.parse(value);
        } catch (ParseException e) {
            if (pattern != null) {
                throw new ConverterException(MessageFactory.getMessage(
                     context, PATTERN_ID, value, "#,##0.0#",
                     MessageFactory.getLabel(context, component)), e);
            } else if (type.equals("currency")) {
                throw new ConverterException(MessageFactory.getMessage(
                     context, CURRENCY_ID, value,
                     parser.format(99.99),
                     MessageFactory.getLabel(context, component)), e);
            } else if (type.equals("number")) {
                throw new ConverterException(MessageFactory.getMessage(
                     context, NUMBER_ID, value,
                     parser.format(99),
                     MessageFactory.getLabel(context, component)), e);
            } else if (type.equals("percent")) {
                throw new ConverterException(MessageFactory.getMessage(
                     context, PERCENT_ID, value,
                     parser.format(.75),
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
            NumberFormat formatter =
                 getNumberFormat(locale);
            if (((pattern != null) && pattern.length() != 0)
                 || "currency".equals(type)) {
                configureCurrency(formatter);
            }
            configureFormatter(formatter);

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


    private static Class currencyClass;

    static {
        try {
            currencyClass = Class.forName("java.util.Currency");
            // container's runtime is J2SE 1.4 or greater
        } catch (Exception ignored) {
        }
    }

    private static final Class[] GET_INSTANCE_PARAM_TYPES =
         new Class[]{String.class};


    /**
     * <p/>
     * Override the formatting locale's default currency symbol with the
     * specified currency code (specified via the "currencyCode" attribute) or
     * currency symbol (specified via the "currencySymbol" attribute).</p>
     * <p/>
     * <p>If both "currencyCode" and "currencySymbol" are present,
     * "currencyCode" takes precedence over "currencySymbol" if the
     * java.util.Currency class is defined in the container's runtime (that
     * is, if the container's runtime is J2SE 1.4 or greater), and
     * "currencySymbol" takes precendence over "currencyCode" otherwise.</p>
     * <p/>
     * <p>If only "currencyCode" is given, it is used as a currency symbol if
     * java.util.Currency is not defined.</p>
     * <pre>
     * Example:
     * <p/>
     * JDK    "currencyCode" "currencySymbol" Currency symbol being displayed
     * -----------------------------------------------------------------------
     * all         ---            ---         Locale's default currency symbol
     * <p/>
     * <1.4        EUR            ---         EUR
     * >=1.4       EUR            ---         Locale's currency symbol for Euro
     * <p/>
     * all         ---           \u20AC       \u20AC
     * <p/>
     * <1.4        EUR           \u20AC       \u20AC
     * >=1.4       EUR           \u20AC       Locale's currency symbol for Euro
     * </pre>
     *
     * @param formatter The <code>NumberFormatter</code> to be configured
     */
    private void configureCurrency(NumberFormat formatter) throws Exception {

        // Implementation copied from JSTL's FormatNumberSupport.setCurrency()

        String code = null;
        String symbol = null;

        if ((currencyCode == null) && (currencySymbol == null)) {
            return;
        }

        if ((currencyCode != null) && (currencySymbol != null)) {
            if (currencyClass != null)
                code = currencyCode;
            else
                symbol = currencySymbol;
        } else if (currencyCode == null) {
            symbol = currencySymbol;
        } else {
            if (currencyClass != null)
                code = currencyCode;
            else
                symbol = currencyCode;
        }

        if (code != null) {
            Object[] methodArgs = new Object[1];

            /*
            * java.util.Currency.getInstance()
            */
            Method m = currencyClass.getMethod("getInstance",
                 GET_INSTANCE_PARAM_TYPES);
            methodArgs[0] = code;
            Object currency = m.invoke(null, methodArgs);

            /*
            * java.text.NumberFormat.setCurrency()
            */
            Class[] paramTypes = new Class[1];
            paramTypes[0] = currencyClass;
            Class numberFormatClass = Class.forName("java.text.NumberFormat");
            m = numberFormatClass.getMethod("setCurrency", paramTypes);
            methodArgs[0] = currency;
            m.invoke(formatter, methodArgs);
        } else {
            /*
            * Let potential ClassCastException propagate up (will almost
            * never happen)
            */
            DecimalFormat df = (DecimalFormat) formatter;
            DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
            dfs.setCurrencySymbol(symbol);
            df.setDecimalFormatSymbols(dfs);
        }

    }


    /**
     * <p>Configure the specified <code>NumberFormat</code> based on the
     * formatting properties that have been set.</p>
     *
     * @param formatter The <code>NumberFormat</code> instance to configure
     */
    private void configureFormatter(NumberFormat formatter) {

        formatter.setGroupingUsed(groupingUsed);
        if (isMaxIntegerDigitsSet()) {
            formatter.setMaximumIntegerDigits(maxIntegerDigits);
        }
        if (isMinIntegerDigitsSet()) {
            formatter.setMinimumIntegerDigits(minIntegerDigits);
        }
        if (isMaxFractionDigitsSet()) {
            formatter.setMaximumFractionDigits(maxFractionDigits);
        }
        if (isMinFractionDigitsSet()) {
            formatter.setMinimumFractionDigits(minFractionDigits);
        }

    }


    private boolean isMaxIntegerDigitsSet() {

        return (maxIntegerDigits != null);

    }


    private boolean isMinIntegerDigitsSet() {

        return (minIntegerDigits != null);

    }


    private boolean isMaxFractionDigitsSet() {

        return (maxFractionDigits != null);

    }


    private boolean isMinFractionDigitsSet() {

        return (minFractionDigits != null);

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
     * <p>Return a <code>NumberFormat</code> instance to use for formatting
     * and parsing in this {@link Converter}.</p>
     *
     * @param locale The <code>Locale</code> used to select formatting
     *               and parsing conventions
     * @throws ConverterException if no instance can be created
     */
    private NumberFormat getNumberFormat(Locale locale) {

        if (pattern == null && type == null) {
            throw new IllegalArgumentException("Either pattern or type must" +
                 " be specified.");
        }

        // PENDING(craigmcc) - Implement pooling if needed for performance?

        // If pattern is specified, type is ignored
        if (pattern != null) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
            return (new DecimalFormat(pattern, symbols));
        }

        // Create an instance based on the specified type
        else if (type.equals("currency")) {
            return (NumberFormat.getCurrencyInstance(locale));
        } else if (type.equals("number")) {
            return (NumberFormat.getNumberInstance(locale));
        } else if (type.equals("percent")) {
            return (NumberFormat.getPercentInstance(locale));
        } else {
            // PENDING(craigmcc) - i18n
            throw new ConverterException
                 (new IllegalArgumentException(type));
        }


    }

    // ----------------------------------------------------- StateHolder Methods


    public Object saveState(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }
        if (!initialStateMarked()) {
            Object values[] = new Object[11];
            values[0] = currencyCode;
            values[1] = currencySymbol;
            values[2] = groupingUsed;
            values[3] = integerOnly;
            values[4] = maxFractionDigits;
            values[5] = maxIntegerDigits;
            values[6] = minFractionDigits;
            values[7] = minIntegerDigits;
            values[8] = locale;
            values[9] = pattern;
            values[10] = type;
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
            currencyCode = (String) values[0];
            currencySymbol = (String) values[1];
            groupingUsed = (Boolean) values[2];
            integerOnly = (Boolean) values[3];
            maxFractionDigits = (Integer) values[4];
            maxIntegerDigits = (Integer) values[5];
            minFractionDigits = (Integer) values[6];
            minIntegerDigits = (Integer) values[7];
            locale = (Locale) values[8];
            pattern = (String) values[9];
            type = (String) values[10];
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

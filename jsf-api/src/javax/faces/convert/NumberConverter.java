/*
 * $Id: NumberConverter.java,v 1.14 2004/01/10 03:16:32 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.convert;


import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>{@link Converter} implementation for <code>java.lang.Number</code>
 * values.</p>
 *
 * <p>The <code>getAsObject()</code> method parses a String into an
 * <code>java.lang.Double</code> or <code>java.lang.Long</code>, according
 * to the following algorithm:</p>
 * <ul>
 * <li>If the specified String is null or zero length, return
 *     a <code>null</code>.  Otherwise, trim leading and trailing
 *     whitespace before proceedng.</li>
 * <li>If the <code>locale</code> property is not null,
 *     use that <code>Locale</code> for managing parsing.  Otherwise, use the
 *     <code>Locale</code> from the <code>UIViewRoot</code>.</li>
 * <li>If a <code>pattern</code> has been specified, its syntax must conform
 *     the rules specified by <code>java.text.DecimalFormat</code>.  Such
 *     a pattern will be used to parse, and the <code>type</code> property
 *     will be ignored.</li>
 * <li>If a <code>pattern</code> has not been specified, parsing will be based
 *     on the <code>type</code> property, which expects a currency, a number,
 *     or a percent.  The parse pattern for currencies, numbers, and
 *     percentages is determined by calling the
 *     <code>getCurrencyInstance()</code>, <code>getNumberInstance()</code>,
 *     or <code>getPercentInstance()</code> method of the
 *     <code>java.text.NumberFormat</code> class, passing in the selected
 *     <code>Locale</code>.</li>
 * <li>If the <code>integerOnly</code> property has been set, only the integer
 *     portion of the String will be parsed.  See the JavaDocs for the
 *     <code>setParseIntegerOnly()</code> method of the
 *     <code>java.text.NumberFormat</code> class for more information.</li>
 * </ul>
 *
 * <p>The <code>getAsString()</code> method expects a value of type
 * <code>java.lang.Number</code> (or a subclass), and creates a formatted
 * String according to the following algorithm:</p>
 * <ul>
 * <li>If the specified value is null, return a zero-length String.</li>
 * <li>If the specified value is a String, return it unmodified.</li>
 * <li>If the <code>locale</code> property is not null,
 *     use that <code>Locale</code> for managing formatting.  Otherwise, use the
 *     <code>Locale</code> from the <code>FacesContext</code>.</li>
 * <li>If a <code>pattern</code> has been specified, its syntax must conform
 *     the rules specified by <code>java.text.DecimalFormat</code>.  Such
 *     a pattern will be used to format, and the <code>type</code> property
 *     (along with related formatting options described in the next paragraph)
 *     will be ignored.</li>
 * <li>If a <code>pattern</code> has not been specified, formatting will be
 *     based on the <code>type</code> property, which formats the value as a
 *     currency, a number, or a percent.  The format pattern for currencies,
 *     numbers, and percentages is determined by calling the
 *     percentages is determined by calling the
 *     <code>getCurrencyInstance()</code>, <code>getNumberInstance()</code>,
 *     or <code>getPercentInstance()</code> method of the
 *     <code>java.text.NumberFormat</code> class, passing in the selected
 *     <code>Locale</code>.  In addition, the following properties will be
 *     applied to the format pattern, if specified:
 *     <ul>
 *     <li>If the <code>groupingUsed</code> property is <code>true</code>, the
 *         <code>setGroupingUsed(true)</code> method on the corresponding
 *         <code>NumberFormat</code> instance will be called.</li>
 *     <li>The minimum and maximum number of digits in the integer and
 *         fractional portions of the result will be configured based on
 *         any values set for the <code>maxFractionDigits</code>,
 *         <code>maxIntegerDigits</code>, <code>minFractionDigits</code>,
 *         and <code>minIntegerDigits</code> properties.</li>
 *     <li>If the type is set to <code>currency</code>, it is also possible
 *         to configure the currency symbol to be used, using either the
 *         <code>currencyCode</code> or <code>currencySymbol</code> properties.
 *         If both are set, the value for <code>currencyCode</code> takes
 *         precedence on a JDK 1.4 (or later) JVM; otherwise, the value
 *         for <code>currencySymbol</code> takes precedence.</li>
 *     </ul></li>
 * </ul>
 */

public class NumberConverter implements Converter, StateHolder {


    // ------------------------------------------------------ Instance Variables


    private String currencyCode = null;
    private String currencySymbol = null;
    private boolean groupingUsed = true;
    private boolean integerOnly = false;
    private int maxFractionDigits = 0;
    private boolean maxFractionDigitsSpecified = false;
    private int maxIntegerDigits = 0;
    private boolean maxIntegerDigitsSpecified = false;
    private int minFractionDigits = 0;
    private boolean minFractionDigitsSpecified = false;
    private int minIntegerDigits = 0;
    private boolean minIntegerDigitsSpecified = false;
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

        this.currencySymbol = currencySymbol;

    }


    /**
     * <p>Return <code>true</code> if <code>getAsString</code> should include
     * grouping separators if necessary.  If not modified, the default value
     * is <code>true</code>.</p>
     */
    public boolean isGroupingUsed() {

        return (this.groupingUsed);

    }


    /**
     * <p>Set the flag indicating whether <code>getAsString()</code> should
     * include grouping separators if necessary.</p>
     *
     * @param groupingUsed The new grouping used flag
     */
    public void setGroupingUsed(boolean groupingUsed) {

        this.groupingUsed = groupingUsed;

    }


    /**
     * <p>Return <code>true</code> if only the integer portion of the given
     * value should be returned from <code>getAsObject()</code>.  If not
     * modified, the default value is <code>false</code>.</p>
     */
    public boolean isIntegerOnly() {

        return (this.integerOnly);

    }


    /**
     * <p>Set to <code>true</code> if only the integer portion of the given
     * value should be returned from <code>getAsObject()</code>.</p>
     *
     * @param integerOnly The new integer-only flag
     */
    public void setIntegerOnly(boolean integerOnly) {

        this.integerOnly = integerOnly;

    }


    /**
     * <p>Return the maximum number of digits <code>getAsString()</code> should
     * render in the fraction portion of the result.</p>
     */
    public int getMaxFractionDigits() {

        return (this.maxFractionDigits);

    }


    /**
     * <p>Set the maximum number of digits <code>getAsString()</code> should
     * render in the fraction portion of the result.  If not set, the number of
     * digits depends on the value being converted.</p>
     *
     * @param maxFractionDigits The new limit
     */
    public void setMaxFractionDigits(int maxFractionDigits) {

        this.maxFractionDigits = maxFractionDigits;
        this.maxFractionDigitsSpecified = true;

    }


    /**
     * <p>Return the maximum number of digits <code>getAsString()</code> should
     * render in the integer portion of the result.</p>
     */
    public int getMaxIntegerDigits() {

        return (this.maxIntegerDigits);

    }


    /**
     * <p>Set the maximum number of digits <code>getAsString()</code> should
     * render in the integer portion of the result.  If not set, the number of
     * digits depends on the value being converted.</p>
     *
     * @param maxIntegerDigits The new limit
     */
    public void setMaxIntegerDigits(int maxIntegerDigits) {

        this.maxIntegerDigits = maxIntegerDigits;
        this.maxIntegerDigitsSpecified = true;

    }


    /**
     * <p>Return the minimum number of digits <code>getAsString()</code> should
     * render in the fraction portion of the result.</p>
     */
    public int getMinFractionDigits() {

        return (this.minFractionDigits);

    }


    /**
     * <p>Set the minimum number of digits <code>getAsString()</code> should
     * render in the fraction portion of the result.  If not set, the number of
     * digits depends on the value being converted.</p>
     *
     * @param minFractionDigits The new limit
     */
    public void setMinFractionDigits(int minFractionDigits) {

        this.minFractionDigits = minFractionDigits;
        this.minFractionDigitsSpecified = true;

    }


    /**
     * <p>Return the minimum number of digits <code>getAsString()</code> should
     * render in the integer portion of the result.</p>
     */
    public int getMinIntegerDigits() {

        return (this.minIntegerDigits);

    }


    /**
     * <p>Set the minimum number of digits <code>getAsString()</code> should
     * render in the integer portion of the result.  If not set, the number of
     * digits depends on the value being converted.</p>
     *
     * @param minIntegerDigits The new limit
     */
    public void setMinIntegerDigits(int minIntegerDigits) {

        this.minIntegerDigits = minIntegerDigits;
        this.minIntegerDigitsSpecified = true;

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

        this.type = type;

    }


    // ------------------------------------------------------- Converter Methods

    /**
     * @exception ConverterException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
     */ 
    public Object getAsObject(FacesContext context, UIComponent component,
                              String value) {
        
        if (context == null || component == null) {
            throw new NullPointerException();
        }

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
            NumberFormat parser = getNumberFormat(locale);
            parser.setParseIntegerOnly(isIntegerOnly());

            // Perform the requested parsing
            return (parser.parse(value));

        } catch (ConverterException e) {
            throw e;
        } catch (ParseException e) {
            // PENDING(craigmcc) - i18n
            throw new ConverterException("Error parsing '" + value + "'");
        }


    }

    /**
     * @exception ConverterException {@inheritDoc}
     * @exception NullPointerException {@inheritDoc}
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
            if (((pattern != null) && !pattern.equals(""))
                || "currency".equals(type)) {
                configureCurrency(formatter);
            }
            configureFormatter(formatter);

            // Perform the requested formatting
            return (formatter.format(value));

        } catch (ConverterException e) {
            throw e;
        } catch (Exception e) {
            throw new ConverterException(e);
        }

    }


    // --------------------------------------------------------- Private Methods


    private static Class currencyClass;
    
    static {
        try {
            currencyClass = Class.forName("java.util.Currency");
            // container's runtime is J2SE 1.4 or greater
        } catch (Exception cnfe) {
        }
    }
    
    private static final Class[] GET_INSTANCE_PARAM_TYPES =
	new Class[] { String.class };


    /**
     * <p>
     * Override the formatting locale's default currency symbol with the
     * specified currency code (specified via the "currencyCode" attribute) or
     * currency symbol (specified via the "currencySymbol" attribute).</p>
     *
     * <p>If both "currencyCode" and "currencySymbol" are present,
     * "currencyCode" takes precedence over "currencySymbol" if the
     * java.util.Currency class is defined in the container's runtime (that
     * is, if the container's runtime is J2SE 1.4 or greater), and
     * "currencySymbol" takes precendence over "currencyCode" otherwise.</p>
     *
     * <p>If only "currencyCode" is given, it is used as a currency symbol if
     * java.util.Currency is not defined.</p>
     * <pre>
     * Example:
     *
     * JDK    "currencyCode" "currencySymbol" Currency symbol being displayed
     * -----------------------------------------------------------------------
     * all         ---            ---         Locale's default currency symbol
     *
     * <1.4        EUR            ---         EUR
     * >=1.4       EUR            ---         Locale's currency symbol for Euro
     *
     * all         ---           \u20AC       \u20AC
     * 
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
        if (maxIntegerDigitsSpecified) {
            formatter.setMaximumIntegerDigits(maxIntegerDigits);
        }
        if (minIntegerDigitsSpecified) {
            formatter.setMinimumIntegerDigits(minIntegerDigits);
        }
        if (maxFractionDigitsSpecified) {
            formatter.setMaximumFractionDigits(maxFractionDigits);
        }
        if (minFractionDigitsSpecified) {
            formatter.setMinimumFractionDigits(minFractionDigits);
        }

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
     *  and parsing conventions
     *
     * @exception ConverterException if no instance can be created
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

        Object values[] = new Object[15];
        values[0] = currencyCode;
        values[1] = currencySymbol;
        values[2] = isGroupingUsed() ? Boolean.TRUE : Boolean.FALSE;
        values[3] = isIntegerOnly() ? Boolean.TRUE : Boolean.FALSE;
        values[4] = new Integer(maxFractionDigits);
        values[5] = maxFractionDigitsSpecified ? Boolean.TRUE : Boolean.FALSE;
        values[6] = new Integer(maxIntegerDigits);
        values[7] = maxIntegerDigitsSpecified ? Boolean.TRUE : Boolean.FALSE;
        values[8] = new Integer(minFractionDigits);
        values[9] = minFractionDigitsSpecified ? Boolean.TRUE : Boolean.FALSE;
        values[10] = new Integer(minIntegerDigits);
        values[11] = minIntegerDigitsSpecified ? Boolean.TRUE : Boolean.FALSE;
        values[12] = locale;
        values[13] = pattern;
        values[14] = type;
        return (values);

    }


    public void restoreState(FacesContext context, Object state) {

        Object values[] = (Object[]) state;
        currencyCode = (String) values[0];
        currencySymbol = (String) values[1];
        groupingUsed = ((Boolean) values[2]).booleanValue();
        integerOnly = ((Boolean) values[3]).booleanValue();
        maxFractionDigits = ((Integer) values[4]).intValue();
        maxFractionDigitsSpecified = ((Boolean) values[5]).booleanValue();
        maxIntegerDigits = ((Integer) values[6]).intValue();
        maxIntegerDigitsSpecified = ((Boolean) values[7]).booleanValue();
        minFractionDigits = ((Integer) values[8]).intValue();
        minFractionDigitsSpecified = ((Boolean) values[9]).booleanValue();
        minIntegerDigits = ((Integer) values[10]).intValue();
        minIntegerDigitsSpecified = ((Boolean) values[11]).booleanValue();
        locale = (Locale) values[12];
        pattern = (String) values[13];
        type = (String) values[14];

    }


    private boolean transientFlag = false;


    public boolean isTransient() {
        return (transientFlag);
    }


    public void setTransient(boolean transientFlag) {
        this.transientFlag = transientFlag;
    }

}

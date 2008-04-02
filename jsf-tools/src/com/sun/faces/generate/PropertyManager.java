package com.sun.faces.generate;

import java.util.Properties;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

/**
 * <p>This class manages properties common to <code>jsf-tools</code>
 * generators.</p>
 */
public class PropertyManager {

    /**
     * <p>The minimum required version of JSP.</p>
     */
    public static final String JSP_VERSION_PROPERTY = "jsp.version";

    /**
     * <p>The short-name of the generated tag library.</p>
     */
    public static final String TAGLIB_SHORT_NAME = "taglib.shortname";

    /**
     * <p>The URI by which the tag library will be known by.</p>
     */
    public static final String TAGLIB_URI = "taglib.uri";

    /**
     * <p>The description of the tag library (may be <code>null</code>).</p>
     */
    public static final String TAGLIB_DESCRIPTION = "taglib.description";

    /**
     * <p>Tags which must be generated as <code>BodyTag</code> instances.
     * This property may have multiple values delimited by
     * commas (may be <code>null</code>).
     */
    public static final String TAGLIB_BODY_TAGS = "taglib.body.tags";

    /**
     * <p>The file name of the generated tag library descriptor.</p>
     */
    public static final String TAGLIB_FILE_NAME = "taglib.file.name";

    /**
     * <p>The filename of a file to be included as part of the tag
     * library generation process (may be <code>null</code>).</p>
     */
    public static final String TAGLIB_INCLUDE = "taglib.include";

    /**
     * <p>Components which are instances of <code>ValueHolder</code>
     * or <code>ConvertibleValueHolder</code>.  This property may have multiple
     * values delimited by pipes ('|') (may be <code>null</code>).
     */
    public static final String VALUE_HOLDER_COMPONENTS =
        "value.holder.components";

    /**
     * <p>Properties which accept <code>ValueBinding</code>s.  This property
     * may have multiple values delimited by pipes ('|') (may be <code>null<code>).
     */
    public static final String VALUE_BINDING_PROPERTIES =
        "value.binding.properties";

    /**
     * <p>Properties which accept <code>MethodBinding</code>s.  This property
     * may have multiple values delimited by pipes ('|') (may be <code>null</code>).
     */
    public static final String METHOD_BINDING_PROPERTIES =
        "method.binding.properties";

    /**
     * <p>A copyright to be included at the beginning of any generated file
     * (may be <code>null</code>).</p>
     */
    public static final String COPYRIGHT = "copyright";

    /**
     * <p>The ID of the <code>RenderKit</code> for which the generation is
     * being performed.</p>
     */
    public static final String RENDERKIT_ID = "renderkit.id";

    /**
     * <p>The target package for any generated code.</p>
     */
    public static final String TARGET_PACKAGE = "target.package";

    /**
     * <p>The base directory in which all generated files are written to.</p>
     */
    public static final String BASE_OUTPUT_DIR = "base.output.dir";


    /**
     * <p>Known properties.</p>
     */
    private static final String[] VALID_PROPS = {
        JSP_VERSION_PROPERTY,
        TAGLIB_URI,
        TAGLIB_DESCRIPTION,
        TAGLIB_SHORT_NAME,
        COPYRIGHT,
        RENDERKIT_ID,
        TARGET_PACKAGE,
        TAGLIB_BODY_TAGS,
        VALUE_BINDING_PROPERTIES,
        METHOD_BINDING_PROPERTIES,
        VALUE_HOLDER_COMPONENTS,
        TAGLIB_FILE_NAME,
        TAGLIB_INCLUDE,
        BASE_OUTPUT_DIR,
    };

    /**
     * <p>Properties which may have no value.</p>
     */
    private static final String[] NULLABLE_PROPS = {
        COPYRIGHT,
        TAGLIB_DESCRIPTION,
        TAGLIB_BODY_TAGS,
        VALUE_BINDING_PROPERTIES,
        METHOD_BINDING_PROPERTIES,
        VALUE_HOLDER_COMPONENTS,
        TAGLIB_INCLUDE,
    };

    // Sort the arrays so we can use Arrays.binarySearch()
    static {
        Arrays.sort(VALID_PROPS);
        Arrays.sort(NULLABLE_PROPS);
    }


    /**
     * <p>The <code>Properties</code> object which backs this class.</p>
     */
    private Properties props;


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Constructs a new <code>PropertyManager</code> instance.</p>
     * @param props - properties
     */
    private PropertyManager(Properties props) {

        this.props = props;

    } // END PropertyManager


    // ---------------------------------------------------------- Public Methods


    /**
     * <p>Obtain a new <code>PropertyManager</code> instance backed by the
     * properies contained in the specified file.</p>
     * @param propertyFile - property file
     * @return a configured <code>PropertyManager</code> instance
     */
    public static PropertyManager newInstance(String propertyFile) {

        Properties props = new Properties();
        try {
            props.load(
                new BufferedInputStream(
                    new FileInputStream(new File(propertyFile))));

        } catch (Exception e) {
            throw new IllegalStateException(e.toString());
        }

        return new PropertyManager(props);

    } // END newInstance


    /**
     * <p>Return the property specified by <code>propertyName</code>.</p>
     * @param propertyName - the property name
     * @return the value(s) of the property or <code>null</code> if no value
     *  is defined
     *
     * @throws IllegalArgumentException of <code>propertyName</code> isn't
     *  a known property
     * @throws IllegalStateException if <code>propertyName</code> illegally
     *  has no value
     */
    public String[] getProperty(String propertyName) {

        if (Arrays.binarySearch(VALID_PROPS, propertyName) < 0) {
            throw new IllegalArgumentException("Unknown Property '" +
                propertyName + '\'');
        }

        String propValue = props.getProperty(propertyName).trim();

        if (propValue == null) {
            if (Arrays.binarySearch(NULLABLE_PROPS, propertyName) >= 0) {
                propValue = "";
            } else {
                throw new IllegalStateException("Property '" + propertyName +
                    "' must be defined.");
            }
        }

        return propValue.split("\\|");

    } // END getProperty


    public String toString() {

        ByteArrayOutputStream propsOutput = new ByteArrayOutputStream();
        props.list(new PrintStream(propsOutput));
        return propsOutput.toString();

    } // END toString

}

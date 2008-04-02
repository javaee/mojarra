/*
 * $Id: ResultDataModelTestCase.java,v 1.1 2003/10/12 21:30:19 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.mock.MockResult;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Unit tests for {@link ResultDataModel}.</p>
 */

public class ResultDataModelTestCase extends DataModelTestCaseBase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public ResultDataModelTestCase(String name) {

        super(name);

    }


    // ------------------------------------------------------ Instance Variables


    // The Result passed to our ResultDataModel
    protected MockResult result = null;


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    public void setUp() throws Exception {

        beans = new TestBean[5];
        for (int i = 0; i < beans.length; i++) {
            beans[i] = new TestBean();
        }
        configure();
        result = new MockResult(beans);
        model = new ResultDataModel(result);
        super.setUp();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(ResultDataModelTestCase.class));

    }


    // ------------------------------------------------- Individual Test Methods


    // ------------------------------------------------------- Protected Methods


    protected TestBean data() throws Exception {

        Object data = model.getRowData();
        assertTrue(data instanceof Map);
        TestBean bean = new TestBean();
        Map map = (Map) data;

        bean.setBooleanProperty
            (((Boolean) map.get("booleanProperty")).booleanValue());
        bean.setBooleanSecond
            (((Boolean) map.get("booleanSecond")).booleanValue());
        bean.setByteProperty
            (((Byte) map.get("byteProperty")).byteValue());
        bean.setDoubleProperty
            (((Double) map.get("doubleProperty")).doubleValue());
        bean.setFloatProperty
            (((Float) map.get("floatProperty")).floatValue());
        bean.setIntProperty
            (((Integer) map.get("intProperty")).intValue());
        bean.setLongProperty
            (((Long) map.get("longProperty")).longValue());
        bean.setNullProperty((String) map.get("nullProperty"));
        bean.setShortProperty
            (((Short) map.get("shortProperty")).shortValue());
        bean.setStringProperty((String) map.get("stringProperty"));
        bean.setWriteOnlyProperty
            ((String) map.get("writeOnlyPropertyValue"));

        return (bean);

    }


}

/*
 * $Id: DataModelTestCaseBase.java,v 1.2 2003/10/15 01:45:58 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.DataModelEvent;
import javax.faces.model.DataModelListener;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * <p>Abstract base class for {@link DataModel} tests.</p>
 */

public abstract class DataModelTestCaseBase extends TestCase {


    // ------------------------------------------------------------ Constructors


    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public DataModelTestCaseBase(String name) {

        super(name);

    }


    // ------------------------------------------------------ Instance Variables


    // The array of beans we will be wrapping (must be initialized before setUp)
    protected TestBean beans[] = new TestBean[0];


    // The DataModel we are testing
    protected DataModel model = null;


    // ---------------------------------------------------- Overall Test Methods


    // Configure the properties of the beans we will be wrapping
    protected void configure() {

        for (int i = 0; i < beans.length; i++) {
            TestBean bean = beans[i];
            bean.setBooleanProperty((i % 2) == 0);
            bean.setBooleanSecond(!bean.getBooleanProperty());
            bean.setByteProperty((byte) i);
            bean.setDoubleProperty(((double) i) * 100.0);
            bean.setFloatProperty(((float) i) * ((float) 10.0));
            bean.setIntProperty(1000 * i);
            bean.setLongProperty((long) 10000 * (long) i);
            bean.setStringProperty("This is string " + i);
        }

    }


    // Set up instance variables required by this test case.
    public void setUp() throws Exception {

        // Subclasses must create "beans", call "configure()", create "model"
        super.setUp();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(DataModelTestCaseBase.class));

    }


    // Tear down instance variables required by ths test case
    public void tearDown() throws Exception {

        super.tearDown();
        beans = null;
        model = null;

    }


    // ------------------------------------------------- Individual Test Methods


    // Test invalid arguments to listener methods
    public void testInvalidListeners() throws Exception {

        try {
            model.addDataModelListener(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }

        try {
            model.removeDataModelListener(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            ; // Expected result
        }


    }


    // Test positioning to all rows in ascending order
    public void testPositionAscending() throws Exception {

        StringBuffer sb = new StringBuffer();
        model.addDataModelListener(new TestListener());
        TestListener.trace(null);

        int n = model.getRowCount();
        for (int i = 1; i <= n; i++) {
            checkRow(i);
            sb.append("/" + i);
        }
        assertEquals(sb.toString(), TestListener.trace());

    }


    // Test positioning to all rows in descending order
    public void testPositionDescending() throws Exception {

        StringBuffer sb = new StringBuffer();
        model.addDataModelListener(new TestListener());
        TestListener.trace(null);

        int n = model.getRowCount();
        for (int i = n; i > 0; i--) {
            checkRow(i);
            sb.append("/" + i);
        }
        assertEquals(sb.toString(), TestListener.trace());

    }


    // Test a pristine DataModel instance
    public void testPristine() throws Exception {

        // Unopened instance
        assertNotNull("beans exists", beans);
        assertNotNull("model exists", model);

        // Correct row count
        assertEquals("correct row count", beans.length, model.getRowCount());

    }


    // Test removing listener
    public void testRemoveListener() throws Exception {

        TestListener listener = new TestListener();
        TestListener.trace(null);
        model.addDataModelListener(listener);
        model.setRowIndex(1);
        model.setRowIndex(1); // No movement so no event
        model.setRowIndex(0);
        model.removeDataModelListener(listener);
        model.setRowIndex(1);
        assertEquals("/1/0", TestListener.trace());

    }


    // Test row index manipulations
    public void testRowIndex() throws Exception {

        assertEquals("correct row index", 0, model.getRowIndex());

        // Positive setRowIndex() tests
        model.setRowIndex(1);
        model.setRowIndex(0);

        // Negative setRowIndex() tests
        try {
            model.setRowIndex(-1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }
        try {
            model.setRowIndex(model.getRowCount() + 1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            ; // Expected result
        }

    }


    // ------------------------------------------------------- Protected Methods


    protected TestBean data() throws Exception {

        Object data = model.getRowData();
        assertNotNull(data);
        assertTrue(data instanceof TestBean);
        return ((TestBean) data);

    }


    protected void checkRow(int i) throws Exception {

        model.setRowIndex(i);
        String prompt = "Row " + i + " property ";
        TestBean bean = data();
        assertNotNull("Row " + i + " data", bean);
        assertEquals(prompt + "booleanProperty",
                     beans[i-1].getBooleanProperty(),
                     bean.getBooleanProperty());
        assertEquals(prompt + "booleanSecond",
                     beans[i-1].isBooleanSecond(),
                     bean.isBooleanSecond());
        assertEquals(prompt + "byteProperty",
                     beans[i-1].getByteProperty(),
                     bean.getByteProperty());
        assertEquals(prompt + "doubleProperty",
                     "" + beans[i-1].getDoubleProperty(),
                     "" + bean.getDoubleProperty());
        assertEquals(prompt + "floatProperty",
                     "" + beans[i-1].getFloatProperty(),
                     "" + bean.getFloatProperty());
        assertEquals(prompt + "intProperty",
                     beans[i-1].getIntProperty(),
                     bean.getIntProperty());
        assertEquals(prompt + "longProperty",
                     beans[i-1].getLongProperty(),
                     bean.getLongProperty());
        assertEquals(prompt + "nullProperty",
                     beans[i-1].getNullProperty(),
                     bean.getNullProperty());
        assertEquals(prompt + "readOnlyProperty",
                     beans[i-1].getReadOnlyProperty(),
                     bean.getReadOnlyProperty());
        assertEquals(prompt + "shortProperty",
                     beans[i-1].getShortProperty(),
                     bean.getShortProperty());
        assertEquals(prompt + "stringProperty",
                     beans[i-1].getStringProperty(),
                     bean.getStringProperty());
        assertEquals(prompt + "writeOnlyProperty",
                     beans[i-1].getWriteOnlyPropertyValue(),
                     bean.getWriteOnlyPropertyValue());

    }


}

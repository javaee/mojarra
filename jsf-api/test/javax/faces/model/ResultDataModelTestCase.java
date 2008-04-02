/*
 * $Id: ResultDataModelTestCase.java,v 1.4 2005/08/22 22:08:28 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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

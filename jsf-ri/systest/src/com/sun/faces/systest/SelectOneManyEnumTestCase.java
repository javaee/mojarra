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
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.systest;

import javax.faces.component.NamingContainer;

import java.util.List;
import java.util.ArrayList;

import com.sun.faces.htmlunit.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import junit.framework.Test;
import junit.framework.TestSuite;

public class SelectOneManyEnumTestCase extends AbstractTestCase {

    /**
     * Construct a new instance of this test case.
     *
     * @param name Name of the test case
     */
    public SelectOneManyEnumTestCase(String name) {
        super(name);
    }
    
    /**
     * Return the tests included in this test suite.
     */
    public static Test suite() {
        return (new TestSuite(NavigationTestCase.class));
    }

    // ------------------------------------------------------------ Test Methods

    public void testEnums() throws Exception {
        HtmlPage page = getPage("/faces/standard/selectonemanyenum.jsp");
        HtmlForm form = getFormById(page, "test");
        assertNotNull(form);
        List<HtmlSelect> selectList = getAllElementsOfGivenClass(page, 
                                                     new ArrayList<HtmlSelect>(), 
                                                     HtmlSelect.class);
        assertTrue(selectList.size() == 5);
        
        // ID selected
        HtmlSelect select = selectList.get(0);
        assertTrue(select.getId().contains("selected"));
        List<HtmlOption> selectedOptions = select.getSelectedOptions();
        assertTrue(selectedOptions.size() == 1);
        assertTrue("Value2".equals(selectedOptions.get(0).getValue()));
        select.setSelectedAttribute(selectedOptions.get(0), false);
        select.setSelectedAttribute("Value1", true);
        
        // ID selected2
        HtmlSelect select2 = selectList.get(1);
        assertTrue(select2.getId().contains("selected2"));
        List<HtmlOption> selectedOptions2 = select2.getSelectedOptions();
        assertTrue(selectedOptions2.size() == 1);
        assertTrue("Value3".equals(selectedOptions2.get(0).getValue()));
        select2.setSelectedAttribute(selectedOptions2.get(0), false);
        select2.setSelectedAttribute("Value2", true);
        
        // ID selected3
        HtmlSelect select3 = selectList.get(2);
        assertTrue(select3.getId().contains("selected3"));
        List<HtmlOption> selectedOptions3 = select3.getSelectedOptions();
        assertTrue(selectedOptions3.size() == 1);
        assertTrue("Value4".equals(selectedOptions3.get(0).getValue()));
        select3.setSelectedAttribute(selectedOptions3.get(0), false);
        select3.setSelectedAttribute("Value3", true);
        
        // ID array
        HtmlSelect selectArray = selectList.get(3);
        assertTrue(selectArray.getId().contains("array"));
        List<HtmlOption> selectedOptionsArray = selectArray.getSelectedOptions();
        assertTrue(selectedOptionsArray.size() == 2);
        assertTrue("Value2".equals(selectedOptionsArray.get(0).getValue()));
        assertTrue("Value4".equals(selectedOptionsArray.get(1).getValue()));
        selectArray.setSelectedAttribute(selectedOptionsArray.get(0), false);
        selectArray.setSelectedAttribute(selectedOptionsArray.get(1), false);
        selectArray.setSelectedAttribute("Value1", true);
        selectArray.setSelectedAttribute("Value3", true);
        
              
        // ID list
        HtmlSelect selectListt = selectList.get(4);
        assertTrue(selectListt.getId().contains("list"));
        List<HtmlOption> selectedOptionsList = selectListt.getSelectedOptions();
        assertTrue(selectedOptionsList.size() == 2);
        assertTrue("Value1".equals(selectedOptionsList.get(0).getValue()));
        assertTrue("Value2".equals(selectedOptionsList.get(1).getValue()));
        selectListt.setSelectedAttribute(selectedOptionsList.get(0), false);
        selectListt.setSelectedAttribute(selectedOptionsList.get(1), false);
        selectListt.setSelectedAttribute("Value2", true);
        selectListt.setSelectedAttribute("Value4", true);
        
        HtmlSubmitInput submit = (HtmlSubmitInput)
            form.getInputByName("test" +
                                NamingContainer.SEPARATOR_CHAR +
                                "submit");
        page = (HtmlPage) submit.click();
        
        // verify the correct options were selected
        
        selectList = getAllElementsOfGivenClass(page, 
                                                     new ArrayList<HtmlSelect>(), 
                                                     HtmlSelect.class);
        assertTrue(selectList.size() == 5);
        
        assertTrue(selectList.size() == 5);
        
        // ID selected
        select = selectList.get(0);
        assertTrue(select.getId().contains("selected"));
        selectedOptions = select.getSelectedOptions();
        assertTrue(selectedOptions.size() == 1);
        assertTrue("Value1".equals(selectedOptions.get(0).getValue()));        
        
        // ID selected2
        select2 = selectList.get(1);
        assertTrue(select2.getId().contains("selected2"));
        selectedOptions2 = select2.getSelectedOptions();
        assertTrue(selectedOptions2.size() == 1);
        assertTrue("Value2".equals(selectedOptions2.get(0).getValue()));       
        
        // ID selected3
        select3 = selectList.get(2);
        assertTrue(select3.getId().contains("selected3"));
        selectedOptions3 = select3.getSelectedOptions();
        assertTrue(selectedOptions3.size() == 1);
        assertTrue("Value3".equals(selectedOptions3.get(0).getValue()));        
        
        // ID array
        selectArray = selectList.get(3);
        assertTrue(selectArray.getId().contains("array"));
        selectedOptionsArray = selectArray.getSelectedOptions();
        assertTrue(selectedOptionsArray.size() == 2);
        assertTrue("Value1".equals(selectedOptionsArray.get(0).getValue()));
        assertTrue("Value2".equals(selectedOptionsArray.get(1).getValue()));                
              
        // ID list
        selectListt = selectList.get(4);
        assertTrue(selectListt.getId().contains("list"));
        selectedOptionsList = selectListt.getSelectedOptions();
        assertTrue(selectedOptionsList.size() == 2);
        assertTrue("Value2".equals(selectedOptionsList.get(0).getValue()));
        assertTrue("Value3".equals(selectedOptionsList.get(1).getValue()));
                
    }
    


} // END SelectOneManyEnumTestCase
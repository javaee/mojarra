/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.examples.misc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sylvain Vieujot (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:36 $
 */
public class TestCheckBoxList
{
    private List testCheckBoxes;
    
    public TestCheckBoxList(){
        testCheckBoxes = new ArrayList(3);
        
        for(int i=0 ; i<3 ; i++)
            testCheckBoxes.add( new TestCheckBox() );
    }

    public List getTestCheckBoxes() {
        return testCheckBoxes;
    }
    public void setTestCheckBoxes(List testCheckBoxes) {
        this.testCheckBoxes = testCheckBoxes;
    }
}

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.examples.misc;

import java.io.Serializable;

/**
 * @author Sylvain Vieujot (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:35 $
 */
public class TestCheckBox implements Serializable
{
    private boolean checked;
    private String text;

 
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}

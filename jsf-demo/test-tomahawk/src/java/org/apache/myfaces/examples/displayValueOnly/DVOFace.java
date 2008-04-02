/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package org.apache.myfaces.examples.displayValueOnly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sylvain Vieujot (latest modification by $Author: edburns $)
 * @version $Revision: 1.1 $ $Date: 2005/11/08 06:08:13 $
 */
public class DVOFace implements Serializable
{
    private boolean attribute = true;
    
    private Map map = new HashMap(){
    	public Object get(Object key){
    		Object held = super.get( key );
    		if( held != null )
    			return held;
    		if( key.toString().toLowerCase().indexOf("list")>0 )
    			return new ArrayList();
    		
    		return null;	
    	}
    };

	public boolean isAttribute() {
		return attribute;
	}
	public void setAttribute(boolean attribute) {
		this.attribute = attribute;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
}
package com.sun.faces.systest.model;

public class Bean {


    public String getScriptAttribute() {
	String result = "javascript:var element = document.getElementById(\"modifiedByScript\");element.innerHTML = \"<b>new value!</b>\";";

	return result;
    }
	  
}

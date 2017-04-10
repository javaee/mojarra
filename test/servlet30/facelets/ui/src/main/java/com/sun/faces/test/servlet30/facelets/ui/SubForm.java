package com.sun.faces.test.servlet30.facelets.ui;

import javax.validation.constraints.Size;

public class SubForm {

	private String text1;

	@Size(max = 3)
	private String text2;

	public SubForm(String text1, String text2) {
		super();
		
		TRACE(">< SubForm(" + text1 + "," + text2 + ")");
		
		this.text1 = text1;
		this.text2 = text2;
	}

	public String getText1() {
		TRACE(">< getText1() return " + text1);

		return text1;
	}

	public void setText1(String text1) {
		TRACE(">< setText1(" + text1 + ")");

		this.text1 = text1;
	}

	public String getText2() {
		TRACE(">< getText2() return " + text2);

		return text2;
	}

	public void setText2(String text2) {
		TRACE(">< setText2(" + text2 + ")");

		this.text2 = text2;
	}
	
	public String toString(){
		String val = null;
		
		return "text1:" + text1 + ",text2:" + text2;
	}
	public void TRACE(String val){
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss Z");
		java.util.Date now = new java.util.Date();
		
		System.out.println("[DBG][SubForm][" + sd.format(now) + "]" + val);
	}
}

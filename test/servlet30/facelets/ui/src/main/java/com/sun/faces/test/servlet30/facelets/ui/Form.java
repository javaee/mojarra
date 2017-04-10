package com.sun.faces.test.servlet30.facelets.ui;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Form {

	private List<SubForm> rows = new ArrayList<SubForm>();

	public List<SubForm> getRows() {
		TRACE(">< getRows() return " + rows.toString());

		return rows;
	}

	public void setRows(List<SubForm> rows) {
		TRACE(">< setRows(" + rows.toString() + ")");

		this.rows = rows;
	}

	public String toString() {
		String val = null;
		java.util.Iterator<SubForm> it = rows.iterator();
		while(it.hasNext()){
			SubForm sf = it.next();
			val = "," + sf.toString();
		}
		return val;
	}
	public void TRACE(String val){
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss Z");
		java.util.Date now = new java.util.Date();
		
		System.out.println("[DBG][From][" + sd.format(now) + "]" + val);
	}
}


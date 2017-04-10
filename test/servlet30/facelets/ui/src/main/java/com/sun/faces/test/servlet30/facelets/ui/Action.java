package com.sun.faces.test.servlet30.facelets.ui;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;


@ManagedBean
@RequestScoped
public class Action {

	@ManagedProperty("#{form}")
	private Form form;

	public void setForm(Form form) {
		TRACE("> setFrom(" + form.toString() + ")");

		this.form = form;
		
		TRACE("< setFrom(10) return.");		
	}

	public void preRenderView(ComponentSystemEvent event) throws AbortProcessingException {
		TRACE("> preRenderView(" + event.toString() + ")");

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		List<SubForm> rows = Arrays.asList(new SubForm("row1", "aaa"), new SubForm("row2", "bbb"));
		form.setRows(rows);
		
		TRACE("rows:" + rows.toString());

		TRACE("< preRenderView(10) return.");
	}

	public String action() {
		TRACE(">< action()");
		return null;
	}
	
	public void TRACE(String val){
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss Z");
		java.util.Date now = new java.util.Date();
		
		System.out.println("[DBG][Action][" + sd.format(now) + "]" + val);
	}
}

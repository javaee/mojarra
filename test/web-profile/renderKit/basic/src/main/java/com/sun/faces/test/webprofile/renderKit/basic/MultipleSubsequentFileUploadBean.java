package com.sun.faces.test.webprofile.renderKit.basic;

import javax.servlet.http.Part;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class MultipleSubsequentFileUploadBean {

	private Part file;
	
	public String doUpload(){
		
		return null;
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}
	
}

package com.sun.faces.test.javaee6web.facelets;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("sampleMBean")
@SessionScoped
public class Issue4073Bean implements Serializable {
        private String button2SetValue;

        public String getButton2SetValue() {
            return button2SetValue;
        }

        public void setButton2SetValue(String button2SetValue) {
            this.button2SetValue = button2SetValue;
        }

	public void initialize() {
		System.out.println("# called initialize()");
	}

	public String button1() {
		System.out.println("# called button1()");
		return null;
	}

	public String button2(String value) {
		System.out.println("# called button2() value=" + value);
                button2SetValue = value;
		return null;
	}

	public String getText() {
		return "Hello World";
	}

}

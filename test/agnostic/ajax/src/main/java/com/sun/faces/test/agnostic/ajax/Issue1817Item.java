package com.sun.faces.test.agnostic.ajax;

import java.util.ArrayList;
import java.util.List;

public class Issue1817Item {

	private String label;

	public Issue1817Item(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public List<Issue1817Item> getSubItems() {
		List<Issue1817Item> subItems = new ArrayList<Issue1817Item>();
		subItems.add(new Issue1817Item(label + ".0"));
		subItems.add(new Issue1817Item(label + ".1"));
		subItems.add(new Issue1817Item(label + ".2"));
		
		return subItems;
	}
		
}

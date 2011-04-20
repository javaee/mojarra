package test;

import java.util.ArrayList;
import java.util.List;

public class TestItem {

	private String label;

	public TestItem(String label) {
		super();
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public List<TestItem> getSubItems() {
		List<TestItem> subItems = new ArrayList<TestItem>();
		subItems.add(new TestItem(label + ".0"));
		subItems.add(new TestItem(label + ".1"));
		subItems.add(new TestItem(label + ".2"));
		
		return subItems;
	}
		
}

package test;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class TestController {

	private List<TestItem> items; 

	public TestController() {
		items = new ArrayList<TestItem>();
		items.add(new TestItem("0"));
		items.add(new TestItem("1"));
		items.add(new TestItem("2"));
	}
	
	public List<TestItem> getItems() {
		return items;
	}
	
}



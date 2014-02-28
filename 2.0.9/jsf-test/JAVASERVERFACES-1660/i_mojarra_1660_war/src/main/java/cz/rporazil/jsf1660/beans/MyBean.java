package cz.rporazil.jsf1660.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import cz.rporazil.jsf1660.enums.ComplexEnum;
import cz.rporazil.jsf1660.enums.SimpleEnum;

@ManagedBean
@RequestScoped
public class MyBean {
	private SimpleEnum simpleValue = SimpleEnum.VALUE1;
	private ComplexEnum complexValue = ComplexEnum.VALUE2;

	public SimpleEnum getSimpleValue() {
		return simpleValue;
	}

	public void setSimpleValue(SimpleEnum simpleValue) {
		this.simpleValue = simpleValue;
	}

	public ComplexEnum getComplexValue() {
		return complexValue;
	}

	public void setComplexValue(ComplexEnum complexValue) {
		this.complexValue = complexValue;
	}

	public List<SelectItem> getSimpleValues() {
		List<SelectItem> ret = new ArrayList<SelectItem>();
		for (SimpleEnum val : SimpleEnum.values()) {
			ret.add(new SelectItem(val, val.toString()));
		}
		return ret;
	}
	public List<SelectItem> getComplexValues() {
		List<SelectItem> ret = new ArrayList<SelectItem>();
		for (ComplexEnum val : ComplexEnum.values()) {
			ret.add(new SelectItem(val, val.toString()));
		}
		return ret;
	}
}

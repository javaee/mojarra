package jsf.component;

import javax.faces.component.UIOutput;

import entities.Sprint;

public class BurndownComponent extends UIOutput {

	private static final String FAMILY = "SCRUMTOYS_COMPONENT_FAMILY";
	private Sprint sprint;
	
	public BurndownComponent(){
		System.out.printf("%s %n%n",this.getClass().getSimpleName());
	}
	
	@Override
	public String getFamily() {
		return FAMILY;
	}
	public Sprint getSprint() {
		return sprint;
	}
	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
		System.out.printf("sprint %s foi recebida ", this.sprint.getName());
	}
	
}

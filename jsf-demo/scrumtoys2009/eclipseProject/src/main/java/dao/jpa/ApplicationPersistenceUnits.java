package dao.jpa;

public enum ApplicationPersistenceUnits {
	DEFAULT("scrumtoysPU");
	
	private String unitName;
	
	public String getUnitName() {
		return unitName;
	}

	ApplicationPersistenceUnits(String $unitName){
		this.unitName = $unitName;
	}
}

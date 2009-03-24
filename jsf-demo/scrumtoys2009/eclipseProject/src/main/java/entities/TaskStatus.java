package entities;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @category domain entity
 * @author Vinny
 * @ since 2008
 *
 */
public enum TaskStatus {
	TODO(),
	WORKING(),
	DONE();
	public static final String RESOURCE_NAME = "i18n";
	private String single;
	private String plural;
	
	TaskStatus(){
		final ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_NAME);
		this.single = bundle.getString(this.getSingleKey());
		this.plural = bundle.getString(getPluralKey());
	}

	public String getPluralKey() {
		return "taskstatus."+this.name().toLowerCase()+".plural";
	}

	public String getSingleKey() {
		return "taskstatus."+this.name().toLowerCase()+".single";
	}

	public String getSingle() {
		return this.single;
	}

	public String getSingle(Locale $locale) {
		return ResourceBundle.getBundle(RESOURCE_NAME, $locale).getString(this.getSingleKey());
	}
	
	public String getPlural() {
		return this.plural;
	}

	public String getPlural(Locale $locale) {
		return ResourceBundle.getBundle(RESOURCE_NAME, $locale).getString(this.getPluralKey());
	}
	
	
}

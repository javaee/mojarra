package beanvalidation;

import beanvalidation.constraints.CreditCard;
import beanvalidation.constraints.Email;
import beanvalidation.groups.Order;
import beanvalidation.groups.Personal;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

@SessionScoped @ManagedBean(name = "user")
public class UserBean implements Serializable {
	private String lastName;
	private String firstName;
	private String emailAddress;
	private String streetAddress;
	private String city;
	private String state;
	private String zipCode;
	private String creditCard;

	// TODO should probably validate *if* a value is provided
	@NotNull(groups = Order.class)
	@NotEmpty(groups = Order.class)
	@CreditCard(groups = Order.class)
	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	@NotNull(groups = Order.class)
	@NotEmpty(groups = Order.class)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@NotNull
	@NotEmpty
	@Email
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@NotNull(groups = { Default.class, Personal.class })
	@NotEmpty(groups = { Default.class, Personal.class })
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull(groups = { Default.class, Personal.class })
	@NotEmpty(groups = { Default.class, Personal.class })
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@NotNull(groups = Order.class)
	@NotEmpty(groups = Order.class)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@NotNull(groups = Order.class)
	@NotEmpty(groups = Order.class)
	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	@NotNull(groups = Order.class)
	@NotEmpty(groups = Order.class)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}

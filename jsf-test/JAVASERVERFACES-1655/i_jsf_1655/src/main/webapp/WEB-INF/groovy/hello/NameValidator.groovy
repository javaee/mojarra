package hello

import javax.faces.validator.Validator
import javax.faces.validator.ValidatorException
import javax.faces.context.FacesContext
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;

public class NameValidator implements Validator {

  void validate(FacesContext context, UIComponent component, Object value) {
    System.out.println("NameValidator : value = " + (Integer)value);

    int age = ((Integer)value).intValue();
      if (age < 0 || age > 65)
        throw new ValidatorException(new FacesMessage("please enter a valid age between 0 and 65. 04:06"));
  }

}




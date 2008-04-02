/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */
 
package guessNumber;

import java.util.Random;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.validator.LongRangeValidator;

public class UserNumberBean {

    Integer userNumber = null;
    Integer randomInt = null;
    String response = null;
    

    public UserNumberBean () {
	Random randomGR = new Random();
	randomInt = new Integer(randomGR.nextInt(10));
        System.out.println("Duke's number: "+randomInt);
    }
  
    public void setUserNumber(Integer user_number) {
        userNumber = user_number;
        System.out.println("Set userNumber " + userNumber);
    }

    public Integer getUserNumber() {
        System.out.println("get userNumber " + userNumber);
        return userNumber;
    }

    public String getResponse() {
    	if(userNumber != null && userNumber.compareTo(randomInt) == 0) {
            return "Yay! You got it!"; 
        }
	else {
            return "Sorry, "+userNumber+" is incorrect.";
        }
    }

    protected String [] status = null;
    public String [] getStatus() {
    	return status;
    }

    public void setStatus(String [] newStatus) {
	status = newStatus;
    }

    private int maximum = 0;
    private boolean maximumSet = false;

    public int getMaximum() {
        return (this.maximum);
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
        this.maximumSet = true;
    }

    private int minimum = 0;
    private boolean minimumSet = false;

    public int getMinimum() {
        return (this.minimum);
    }


    public void setMinimum(int minimum) {
        this.minimum = minimum;
        this.minimumSet = true;
    }

    public void validate(FacesContext context,
                         UIComponent  component,
                         Object       value) throws ValidatorException {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        if (value != null) {
            try {
                int converted = intValue(value);
                if (maximumSet &&
                    (converted > maximum)) {
		    if (minimumSet) {
                        throw new ValidatorException(MessageFactory.getMessage
					   (context,
					    Validator.NOT_IN_RANGE_MESSAGE_ID,
					    new Object[] {
						new Integer(minimum),
						new Integer(maximum) }));
			
		    }
		    else {
                        throw new ValidatorException(MessageFactory.getMessage
					   (context,
					    LongRangeValidator.MAXIMUM_MESSAGE_ID,
					    new Object[] {
						new Integer(maximum) }));
		    }
                }
                if (minimumSet &&
                    (converted < minimum)) {
		    if (maximumSet) {
                        throw new ValidatorException(MessageFactory.getMessage
					   (context,
					    Validator.NOT_IN_RANGE_MESSAGE_ID,
					    new Object[] {
						new Double(minimum),
						new Double(maximum) }));
			
		    }
		    else {
                        throw new ValidatorException(MessageFactory.getMessage
					   (context,
					    LongRangeValidator.MINIMUM_MESSAGE_ID,
					    new Object[] {
						new Integer(minimum) }));
		    }
                }
            } catch (NumberFormatException e) {
                throw new ValidatorException(MessageFactory.getMessage
                                     (context, LongRangeValidator.TYPE_MESSAGE_ID));
            }
        }

    }

    private int intValue(Object attributeValue)
        throws NumberFormatException {

        if (attributeValue instanceof Number) {
            return ( ((Number) attributeValue).intValue() );
        } else {
            return (Integer.parseInt(attributeValue.toString()));
        }

    }




}

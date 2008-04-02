/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package guessNumber;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import java.util.Random;

public class UserNumberBean {

    Integer userNumber = null;
    Integer randomInt = null;
    String response = null;


    public UserNumberBean() {
        Random randomGR = new Random();
        randomInt = new Integer(randomGR.nextInt(10));
        System.out.println("Duke's number: " + randomInt);
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
        if (userNumber != null && userNumber.compareTo(randomInt) == 0) {
            return "Yay! You got it!";
        } else {
            return "Sorry, " + userNumber + " is incorrect.";
        }
    }


    protected String[] status = null;


    public String[] getStatus() {
        return status;
    }


    public void setStatus(String[] newStatus) {
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
                         UIComponent component,
                         Object value) throws ValidatorException {

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
                                                      new Object[]{
                                                          new Integer(minimum),
                                                          new Integer(maximum)
                                                      }));

                    } else {
                        throw new ValidatorException(MessageFactory.getMessage
                                                     (context,
                                                      LongRangeValidator.MAXIMUM_MESSAGE_ID,
                                                      new Object[]{
                                                          new Integer(maximum)
                                                      }));
                    }
                }
                if (minimumSet &&
                    (converted < minimum)) {
                    if (maximumSet) {
                        throw new ValidatorException(MessageFactory.getMessage
                                                     (context,
                                                      Validator.NOT_IN_RANGE_MESSAGE_ID,
                                                      new Object[]{
                                                          new Double(minimum),
                                                          new Double(maximum)
                                                      }));

                    } else {
                        throw new ValidatorException(MessageFactory.getMessage
                                                     (context,
                                                      LongRangeValidator.MINIMUM_MESSAGE_ID,
                                                      new Object[]{
                                                          new Integer(minimum)
                                                      }));
                    }
                }
            } catch (NumberFormatException e) {
                throw new ValidatorException(MessageFactory.getMessage
                                             (context,
                                              LongRangeValidator.TYPE_MESSAGE_ID));
            }
        }

    }


    private int intValue(Object attributeValue)
        throws NumberFormatException {

        if (attributeValue instanceof Number) {
            return (((Number) attributeValue).intValue());
        } else {
            return (Integer.parseInt(attributeValue.toString()));
        }

    }


}

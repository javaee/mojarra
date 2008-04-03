/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package guessNumber;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.ValidatorException;

import java.util.Random;


public class UserNumberBean {

    Integer userNumber = null;
    Integer randomInt = null;
    String response = null;


    public UserNumberBean() {
        Random randomGR = new Random();
        do {
            randomInt = new Integer(randomGR.nextInt(10));
        } while (randomInt.intValue() == 0);
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
        } else if (userNumber == null) {
            return "Sorry, " + userNumber +
                   " is incorrect. Try a larger number.";
        } else {
            int num = userNumber.intValue();
            if (num > randomInt.intValue()) {
                return "Sorry, " + userNumber +
                       " is incorrect. Try a smaller number.";
            } else {
                return "Sorry, " + userNumber +
                       " is incorrect. Try a larger number.";
            }
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
                        throw new ValidatorException(
                              MessageFactory.getMessage
                                    (context,
                                     LongRangeValidator.NOT_IN_RANGE_MESSAGE_ID,
                                     new Object[]{
                                           new Integer(minimum),
                                           new Integer(maximum),
                                           MessageFactory.getLabel(context,
                                                                   component)
                                     }));

                    } else {
                        throw new ValidatorException(
                              MessageFactory.getMessage
                                    (context,
                                     LongRangeValidator.MAXIMUM_MESSAGE_ID,
                                     new Object[]{
                                           new Integer(maximum),
                                           MessageFactory.getLabel(context,
                                                                   component)
                                     }));
                    }
                }
                if (minimumSet &&
                    (converted < minimum)) {
                    if (maximumSet) {
                        throw new ValidatorException(MessageFactory.getMessage
                              (context,
                               LongRangeValidator.NOT_IN_RANGE_MESSAGE_ID,
                               new Object[]{
                                     new Double(minimum),
                                     new Double(maximum),
                                     MessageFactory.getLabel(context, component)
                               }));

                    } else {
                        throw new ValidatorException(
                              MessageFactory.getMessage
                                    (context,
                                     LongRangeValidator.MINIMUM_MESSAGE_ID,
                                     new Object[]{
                                           new Integer(minimum),
                                           MessageFactory.getLabel(context,
                                                                   component)
                                     }));
                    }
                }
            } catch (NumberFormatException e) {
                throw new ValidatorException(
                      MessageFactory.getMessage
                            (context, LongRangeValidator.TYPE_MESSAGE_ID,
                             new Object[]{MessageFactory.getLabel(context,
                                                                  component)}));
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

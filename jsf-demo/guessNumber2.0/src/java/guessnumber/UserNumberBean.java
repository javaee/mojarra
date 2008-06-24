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

package guessnumber;

import java.text.MessageFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


/**
 * <p>This bean is used to track the user's guesses.</p>
 */
public class UserNumberBean implements Serializable {

    private static final long serialVersionUID = 7288057381787349138L;
    private static final Logger LOGGER = Logger.getLogger("guessnumber");
    

    // ----------------------------------------------------- Injected Properties


    @Resource(name="maximum")
    private Integer maximum;

    @Resource(name="minimum")
    private Integer minimum;


    // -------------------------------------------------------------- Properties

    private enum Response {
        Correct("Yay! You got it!"),
        Larger("Sorry, {0} is incorrect.  Try a larger number."),
        Smaller("Sorry, {0} is incorrect. Try a smaller number.");

        private String msg;
        Response(String msg) {
            this.msg = msg;
        }

        String getMessage(Object... params) {
            return MessageFormat.format(msg, params);
        }
    }

    private Integer userNumber;
    private int randomInt;
    private String greeting;


    // ---------------------------------------------------------- Public Methods

    public String getGreeting() {
        return greeting;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }


    public String getResponse() {

        int comp = userNumber.compareTo(randomInt);
        if (comp == 0) {
            return Response.Correct.getMessage();
        } else if (comp < 0) {
            return Response.Larger.getMessage(userNumber);
        } else {
            return Response.Smaller.getMessage(userNumber);
        }

    }


    // --------------------------------------------------------- Private Methods


    @SuppressWarnings({"UnusedDeclaration"})
    @PostConstruct
    private void construct() {

        Random random = new Random();
        while (randomInt == 0) {
            randomInt = random.nextInt(10);
        }
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                       MessageFormat.format("Duke's number: {0}", randomInt));
        }

        greeting = "Hi!  My name is Duke.  I'm thinking of a number between "
                   + minimum
                   + " and "
                   + maximum
                   + ".  Can you guess it?";

    }

}

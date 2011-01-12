/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package ifnavigation;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.io.Serializable;

@ManagedBean
@SessionScoped
@SuppressWarnings("unused")
public class NumberGuess implements Serializable {

    private static final long serialVersionUID = 0L;

    private boolean started = false;

    private int upperBound;

    private int lowerBound;

    private int maxGuesses;

    private int guessAttempts;

    private int randomNumber;

    private Integer currentGuess;

    private List<Integer> possibleGuesses;

    public NumberGuess() {
    }

    public void begin() {
        randomNumber = new Random().nextInt(10) + 1;
        currentGuess = null;
        guessAttempts = 0;
        maxGuesses = 5;
        started = true;
        upperBound = 10;
        lowerBound = 1;
        updatePossibleGuesses();
    }

    public void guess() {
        if (currentGuess > randomNumber) {
             upperBound = currentGuess - 1;
        }
        if (currentGuess < randomNumber) {
             lowerBound = currentGuess + 1;
        }

        updatePossibleGuesses();
        guessAttempts++;
    }

    public boolean isCorrect() {
        return currentGuess != null && randomNumber == currentGuess;
    }   

    public Integer getCurrentGuess() {
        return currentGuess;
    }

    public void setCurrentGuess(Integer guess) {
        this.currentGuess = guess;
    }
    
    public List<Integer> getPossibleGuesses() {
        return possibleGuesses;
    }   
    
    public boolean isStarted() {
        return started;
    }

    public int getGuessAttempts() {
        return guessAttempts;
    }

    public boolean isGuessesExhausted() {
        return guessAttempts == maxGuesses;
    }

    public int getRemainingGuesses() {
        return maxGuesses - guessAttempts;
    }

    public String getViewIdForIncorrectGuess() {
        return isGuessesExhausted() ? "/gameover.xhtml" : "/guess.xhtml";
    }

    protected void updatePossibleGuesses() {
        List<Integer> l = new ArrayList<Integer>();
        for (int i = lowerBound; i <= upperBound; i++) {
            l.add(i);
        }
        possibleGuesses = l;
    }
}

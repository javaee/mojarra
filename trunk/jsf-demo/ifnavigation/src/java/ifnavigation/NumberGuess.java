package ifnavigation;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.io.Serializable;

public class NumberGuess implements Serializable {

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
        randomNumber = new Random().nextInt(100) + 1;
        currentGuess = null;
        guessAttempts = 0;
        maxGuesses = 5;
        started = true;
        upperBound = 100;
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
        return currentGuess != null ? randomNumber == currentGuess : false;
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

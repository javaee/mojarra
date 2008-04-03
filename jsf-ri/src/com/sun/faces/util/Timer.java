package com.sun.faces.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a simple wrapper for timing method calls.
 * The traditional methid is to add two variables, start, and stop,
 * and display the difference of these values.  Encapsulates
 * the process.
 */
public class Timer {

    private static final Logger LOGGER =
         Logger.getLogger(FacesLogger.TIMING.getLoggerName());

    long start;
    long stop;


    // ------------------------------------------------------------ Constructors


    private Timer() { }


    // ---------------------------------------------------------- Public Methods


    /**
     * @return a new <code>Timer</code> instance if the <code>TIMING</code>
     *  logging level is <code>FINE</code>, otherwise, return null;
     */
    public static Timer getInstance() {
        if (LOGGER.isLoggable(Level.FINE)) {
            return new Timer();
        }
        return null;
    }


    /**
     * Start timing.
     */
    public void startTiming() {
        start = System.currentTimeMillis();
    }


    /**
     * Stop timing.
     */
    public void stopTiming() {
        stop = System.currentTimeMillis();
    }


    /**
     * Log the timing result.
     * @param taskInfo task description
     */
    public void logResult(String taskInfo) {
        LOGGER.log(Level.FINE,
                   " [TIMING] - [" + getTimingResult() + "ms] : " + taskInfo);
    }


    // --------------------------------------------------------- Private Methods


    /**
     * @return the time for this task
     */
    private long getTimingResult() {
        return (stop - start);
    }
}

package renderkits.util;

import java.util.logging.Logger;
import java.util.logging.Level;

public class Util extends Object {
                                                                                                               
    //
    // Private/Protected Constants
    //
    public static final String FACES_LOGGER = "javax.enterprise.resource.jsf.";
                                                                                                               
    public static final String FACES_LOG_STRINGS =
            "com.sun.faces.LogStrings";
                                                                                                               
    // Log instance for this class
    private static Logger logger;
    static {
        logger = getLogger(FACES_LOGGER);
    }

    public static Logger getLogger( String loggerName ) {
        return Logger.getLogger(loggerName, FACES_LOG_STRINGS );
    }
}


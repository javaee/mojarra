import java.util.Hashtable; 
import javax.microedition.lcdui.CommandListener; 
import javax.microedition.midlet.MIDlet; 

/**
 * A generic abstraction of a MIDlet.  Add common methods
 * as necessary.
 */
public abstract class AbstractMIDlet extends MIDlet 
    implements CommandListener { 

    protected void connectionCompleted(Hashtable response) {};
}


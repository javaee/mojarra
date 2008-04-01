package cardemo;

import javax.faces.CommandListener;
import javax.faces.CommandEvent;
import javax.faces.CommandFailedException;
import javax.faces.ValueChangeListener;
import javax.faces.ValueChangeEvent;
import javax.faces.NavigationHandler;
import javax.faces.UIComponent;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * The listener interface for handling command events.
 * An object should implement this interface if it needs
 * to respond to a command event.
 */
public class OptionListener implements CommandListener, ValueChangeListener {

    public OptionListener ( ) {
        System.out.println("OptionListener created");
    }

    public void doCommand(CommandEvent e, NavigationHandler nh )
    throws CommandFailedException {
        
        String cmdName = e.getCommandName();
        System.out.println("Command is: " + cmdName);
        
        try {
            nh.handleCommandSuccess(cmdName);
        }
        catch ( CommandFailedException ce) {
            if ( nh != null ) {
                nh.handleCommandException(cmdName, ce);
            }
            throw ce;
        }
    }
    
    public void handleValueChange (ValueChangeEvent e) {
        // do nothing for now
    }


    public boolean requiresValidation(CommandEvent e ) {
        return false;
    }
    
}


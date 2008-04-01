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
public class BasicListener implements CommandListener{

    public BasicListener ( ) {
        System.out.println("BasicListener created");
    }

    public void doCommand(CommandEvent e, NavigationHandler nh )  
            throws CommandFailedException {

       if ( nh != null ) {
           System.out.println("Command " + e.getCommandName() + " Execution Successful");
           nh.handleCommandSuccess(e.getCommandName());
       } 
       UIComponent source = e.getSourceComponent();
       System.out.println("Name of UIComponent that generated event " + 
			  source.getId());
       System.out.println("CommandEvent processing successful");
    }


    public boolean requiresValidation(CommandEvent e ) {
        return true;
    }
    
}

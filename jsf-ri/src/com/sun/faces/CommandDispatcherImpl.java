package com.sun.faces;

import org.mozilla.util.Assert;
import org.mozilla.util.Debug;
import org.mozilla.util.Log;
import org.mozilla.util.ParameterCheck;

import java.io.IOException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import javax.faces.CommandDispatcher;
import java.util.EventObject;
import javax.faces.CommandEvent;
import javax.faces.CommandListener;
import java.util.Vector;
import javax.faces.Command;
import javax.faces.Constants;
import javax.faces.ObjectTable;
import javax.servlet.ServletException;
import javax.faces.FacesException;
import javax.faces.RenderContext;
import javax.faces.CommandFailedException;

/**
 * A class which dispatches command events to appropriate target 
 * command listener objects.  This command dispatcher implements 
 * appropriate flow-control when it dispatches to listeners which 
 * implement the <code>Command</code>interface.
 *
 * @version $Id: CommandDispatcherImpl.java,v 1.2 2001/12/10 18:17:59 visvan Exp $
 * @author Jayashri Visvanathan
 *
 * @see CommandEvent
 * @see CommandListener
 * @see Command
 * @see WCommand#addCommandListener
 */
public class CommandDispatcherImpl extends CommandDispatcher {

    public CommandDispatcherImpl () {
    }

    public void dispatch(ServletRequest request, ServletResponse response,
			 EventObject event) throws IOException, FacesException {

        boolean cmd_failed = false;

        String redirectPath = null;
        
        ParameterCheck.nonNull(request);
        ParameterCheck.nonNull(response);
        ParameterCheck.nonNull(event);

        ObjectTable ot = ObjectTable.getInstance();
        Assert.assert_it( ot != null );
        
        CommandEvent cmd_event = null;
        if ( event instanceof CommandEvent)  {
            cmd_event = (CommandEvent) event;
        } else {
            throw new FacesException("Invalid event type. " + 
                    "Expected CommandEvent");
        }
        // invoke the doCommand on all listeners
        String srcName = cmd_event.getSourceName();  
        Assert.assert_it(srcName != null );
 
        String lis_name = srcName + Constants.REF_COMMANDLISTENERS;
        Vector lis_list = (Vector) ot.get(request, lis_name);
        if ( lis_list != null && lis_list.size() > 0 ) {
            for ( int i = 0; i < lis_list.size(); ++i) {
                // tags could have put in null for listener name.
                String lis_ref_name = (String) lis_list.elementAt(i);
                if ( lis_ref_name != null ) {
                    CommandListener cl = (CommandListener) 
                        ot.get(request, lis_ref_name);
                    Assert.assert_it ( cl != null );
                    try {
                        cl.doCommand(cmd_event);
                    } catch ( CommandFailedException e ) {
                        // PENDING ( visvan ) skip for now till
                        // we decide how to do error handling in listeners.
                    } 
                }   
            }    
        }
    
        // process commands if any
        String cmd_ref_name = srcName + Constants.REF_COMMAND;
        String cmd_name = (String) ot.get(request, cmd_ref_name);
        if (cmd_name != null ) {
            Command cmd = (Command)  ot.get(request, cmd_name);
            Assert.assert_it ( cmd != null );
            try {
                cmd.doCommand(cmd_event);
                redirectPath = cmd.getCompletionPath(cmd_event);
            } catch ( CommandFailedException e ) {
                redirectPath = cmd.getErrorPath(cmd_event);
            }
        }
 
        if ( redirectPath != null ) {
            try {
                HttpServletResponse res = (HttpServletResponse) response;
                RequestDispatcher reqD = 
                request.getRequestDispatcher(res.encodeURL(redirectPath));
                reqD.forward(request, response); 
            } catch ( ServletException se ) {
                throw new FacesException( se.getMessage());
            }
        }
    }
}
